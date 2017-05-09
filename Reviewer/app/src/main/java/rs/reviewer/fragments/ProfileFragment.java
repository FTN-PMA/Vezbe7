package rs.reviewer.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import rs.reviewer.R;
import rs.reviewer.activities.SplashScreenActivity;
import rs.reviewer.tools.CurrentUser;

public class ProfileFragment extends Fragment {
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View view = inflater.inflate(R.layout.profile_layout, container, false);
		
		String usernameContent = CurrentUser.getName(getActivity());
		
		TextView userName = (TextView) view.findViewById(R.id.user_name_content);
		userName.setText(usernameContent);
		
		String emailContent = CurrentUser.getEmail(getActivity());
		
		TextView email = (TextView) view.findViewById(R.id.user_mail_content);
		email.setText(emailContent);
		
		return view;
	}
	
	@Override
	public void onResume() {
		super.onResume();

		((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(R.string.profile);
		setHasOptionsMenu(true);
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		
		//dodati meni
		inflater.inflate(R.menu.profile_menu, menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		// handle item selection
		switch (item.getItemId()) {
			case R.id.logout_action:
				CurrentUser.logout(getActivity());
				startActivity(new Intent(getActivity(), SplashScreenActivity.class));
				getActivity().finish();
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}
}
