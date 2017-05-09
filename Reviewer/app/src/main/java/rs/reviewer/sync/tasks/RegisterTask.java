package rs.reviewer.sync.tasks;

import android.os.AsyncTask;
import android.util.Log;

//import com.appspot.elevated_surge_702.crud.model.MessagesStringMessage;
//import com.appspot.elevated_surge_702.crud.model.MessagesUserMessage;
//import com.appspot.elevated_surge_702.sync.Sync;
import rs.reviewer.tools.SyncUtils;
//import com.google.api.client.util.DateTime;

import java.io.IOException;
import java.util.Date;

/**
 * Registruje korisnika na server. String parametri su userName, email, uuid. Vraca String kod uspesnosti.
 * @author LaptopX
 *
 */
public class RegisterTask extends AsyncTask<String, Void, String>
{
	public static final String OK = "ok";
	public static final String NAME_EXISTS = "name_exists";
	public static final String EMAIL_EXISTS = "email_exists";
	public static final String NO_CONNECTION = "no_connection";
	
	@Override
	protected String doInBackground(String... params)
	{
//		String result = "no_connection";
//		String userName = params[0];
//		String email = params[1];
//		String uuid = params[2];
//
//		Sync api = SyncUtils.buildSyncApi();
//
//		MessagesUserMessage message = new MessagesUserMessage();
//		message.setUuid(uuid);
//		message.setUserName(userName);
//		message.setEmail(email);
//		message.setDeleted(false);
//		message.setLastModified(new DateTime(new Date()));
//
//		try
//		{
//			MessagesStringMessage resultMessage = api.user().register(message).execute();
//			if(resultMessage != null) result = resultMessage.getContent();
//		}
//		catch(IOException e)
//		{
//			Log.d("SYNC", "RegisterTask", e);
//		}
		
		return "ok";
	}
}
