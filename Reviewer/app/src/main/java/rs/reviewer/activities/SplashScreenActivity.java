package rs.reviewer.activities;

import android.accounts.AccountManager;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.database.sqlite.SQLiteConstraintException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import rs.reviewer.MainActivity;
import rs.reviewer.R;
import rs.reviewer.model.User;
import rs.reviewer.sync.tasks.RegisterTask;
import rs.reviewer.sync.tasks.SyncTask;
import rs.reviewer.tools.CurrentUser;
import rs.reviewer.tools.SyncUtils;
import rs.reviewer.validators.NameValidator;
import rs.reviewer.validators.TextValidator;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.common.AccountPicker;

public class SplashScreenActivity extends Activity
{
	private static final int REQUEST_CODE_EMAIL = 1;
	private static int SPLASH_TIME_OUT = 3000; // splash ce biti vidljiv minimum SPLASH_TIME_OUT milisekundi
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash);
		
		// uradi inicijalizaciju u pozadinksom threadu
		new InitTask().execute();
	}
	
	private class InitTask extends AsyncTask<Void, Void, Void>
	{
		private long startTime;
		
		@Override
        protected void onPreExecute()
        {
			startTime = System.currentTimeMillis();
        }
		
		@Override
		protected Void doInBackground(Void... arg0)
		{
			if(SyncUtils.getLastSyncronizationDate(SplashScreenActivity.this) == null) // ako jos nije sinhronizovan
			{
//				new SyncTask(SplashScreenActivity.this)
//				{
//					protected void onPostExecute(Void result) // kad se sinhronizuje
//					{
//						continueLogin(); // nastavi login
//					};
//				}
//				.execute(); // sinhronizuj sad i sacekaj da zavrsi
				continueLogin();
			}
			else // inace
			{
				continueLogin(); // samo nastavi login
			}
			
			return null;
		}
		
		private void continueLogin()
		{
			// sacekaj tako da splash bude vidljiv minimum SPLASH_TIME_OUT milisekundi
			long timeLeft = SPLASH_TIME_OUT - (System.currentTimeMillis() - startTime);
			if(timeLeft < 0) timeLeft = 0;
			SystemClock.sleep(timeLeft);
			
			// uloguj se
			login();
		}
	}
	
    /**
     * Proveri da li je logovan user, ako nije registruj ga.
     */
	private void login()
	{
		if(!CurrentUser.exists(this))
		{
			register(); // ako nije logovan registruj ga
		}
		else
		{
			startMainActivity();
		}
	}

	/**
	 * Zatrazi od korisnika da odabere nalog sa validnim emailom.
	 */
	private void register()
	{
		try
		{
			Intent intent = AccountPicker.newChooseAccountIntent(null, null,
					new String[] { GoogleAuthUtil.GOOGLE_ACCOUNT_TYPE }, false, null, null, null, null);
			startActivityForResult(intent, REQUEST_CODE_EMAIL);
	    }
		catch (ActivityNotFoundException e)
		{
			Toast.makeText(this, R.string.cant_aquire_account_message, Toast.LENGTH_LONG).show();
			finish();
	    }
	}
	
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == REQUEST_CODE_EMAIL)
        {
        	if(resultCode == RESULT_OK) // korisnik je odabrao nalog sa emailom
        	{
            	String email = data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
            	if(Patterns.EMAIL_ADDRESS.matcher(email).matches())
            	{
            		User existingUser = User.getByEmail(email);
            		if(existingUser != null) // ako vec postoji user sa ovim emailom
            		{
            			login(existingUser); // uloguj ga
            		}
            		else // inace
            		{
            			register(email); // registruj sa emailom
            		}
            	}
            	else
            	{
            		Toast.makeText(this, R.string.no_valid_email_message, Toast.LENGTH_LONG).show();
            		register(); // probaj ovo opet
            	}
        	}
        	else
        	{
        		Toast.makeText(this, R.string.select_account_with_email_message, Toast.LENGTH_LONG).show();
        		register(); // probaj ovo opet
        	}
        }
    }
	
    /**
     * Znamo email, pitaj korisnika za jedinstveni username.
     * @param currentUserEmail
     */
	private void register(String currentUserEmail)
	{
		createRegisterDialog(currentUserEmail); // pitaj za username
	}
	
	/**
	 * Pitaj korisnika za jedinstveni username.
	 * @param currentUserEmail
	 */
	@SuppressLint("InflateParams")
	private void createRegisterDialog(String currentUserEmail)
	{
		LayoutInflater layoutInflater = LayoutInflater.from(this);
		final View promptView = layoutInflater.inflate(R.layout.register_dialog, null);
		final AlertDialog dialog = new AlertDialog.Builder(this)
			.setView(promptView)
			.setCancelable(false)
			.setPositiveButton(R.string.register, new OnClickListener()
			{
				private String email;
				public OnClickListener setEmail(String email)
				{
					this.email = email;
					return this;
				}
				@Override
				public void onClick(DialogInterface dialog, int which)
				{
					//get data
					EditText editText = (EditText) promptView.findViewById(R.id.edittext);
					String newUserName = editText.getText().toString();
					register(email, newUserName); // registruj korisnika sa email i username
				}
			}
			.setEmail(currentUserEmail))
			.create();
		
		EditText editText = (EditText) promptView.findViewById(R.id.edittext);
		TextValidator nameValidator = new NameValidator(dialog, editText, 20);
		editText.addTextChangedListener(nameValidator);
		
		dialog.show();
		dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
	}

	/**
	 * Znamo i email i username, pokusaj da dodas korisnika u bazu.
	 * @param currentUserEmail
	 * @param newUserName
	 */
	private void register(final String currentUserEmail, String newUserName)
	{
		final User newUser = new User(newUserName, currentUserEmail);
		
		try
		{
			newUser.saveOrThrow(); // snimi novog usera u bazu
			new RegisterTask() // sinhronizuj sa serverom
			{
				protected void onPostExecute(String result) 
				{
					if(OK.equals(result))
					{
						login(newUser); // dodavanje je uspelo, uloguj novog korisnika
					}
					else if(NAME_EXISTS.equals(result))
					{
						Toast.makeText(SplashScreenActivity.this, R.string.username_exists_message, Toast.LENGTH_LONG).show();
						register(currentUserEmail); // probaj opet da se registrujes
					}
					else if(EMAIL_EXISTS.equals(result))
					{
						Toast.makeText(SplashScreenActivity.this, R.string.email_exists_message, Toast.LENGTH_LONG).show();
						register(); // probaj opet da pitas za mail
					}
					else if(NO_CONNECTION.equals(result))
					{
						Toast.makeText(SplashScreenActivity.this, R.string.cant_register_message, Toast.LENGTH_LONG).show();
						register(currentUserEmail); // probaj opet da se registrujes
					}
					else
					{
						Log.e("SYNC", "Internal error, unknown return code from RegisterTask.");
					}
					
					if(!OK.equals(result)) // ako nije uspelo obrisi usera
					{
						newUser.delete();
					}
				}
			}
			.execute(newUser.getName(), newUser.getEmail(), newUser.getModelId());
		}
		catch(SQLiteConstraintException ex) // ako nije prosla registracija
		{
			newUser.delete();
			Toast.makeText(this, R.string.username_exists_message, Toast.LENGTH_LONG).show();
			register(currentUserEmail); // probaj opet da se registrujes
		}
		catch(Exception ex) // ako nije prosla registracija
		{
			newUser.delete();
			Toast.makeText(this, R.string.cant_register_message, Toast.LENGTH_LONG).show();
			register(currentUserEmail); // probaj opet da se registrujes
		}
	}
	
	/**
	 * Uloguj korisnika user, smesti podatke u shared preferences.
	 * @param user
	 */
	private void login(User user)
	{
		CurrentUser.login(user, this);
		startMainActivity();
	}
	
	private void startMainActivity()
	{
		startActivity(new Intent(SplashScreenActivity.this, MainActivity.class));
		finish(); // da nebi mogao da ode back na splash
	}
}
