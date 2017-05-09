package rs.reviewer.fragments.users;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import rs.reviewer.R;
import rs.reviewer.model.User;
import rs.reviewer.tools.ReviewerTools;

public class UserDetailFragment extends Fragment
{
	public static final String NAME = "NAME";
	public static final String LAST_MODIFIED = "LAST MODIFIED";
	public static final String ID = "ID";
	
	public UserDetailFragment()
	{}
	
	public UserDetailFragment(User user)
	{
		setArguments(new Bundle());
		dataToArguments(user);
	}
	
	private void dataToArguments(User user)
	{
		Bundle bundle = getArguments();
		bundle.putString(NAME, user.getName());
		bundle.putString(LAST_MODIFIED, ReviewerTools.preapreDate(user.getDateModified()));
		bundle.putString(ID, user.getModelId());
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View view = inflater.inflate(R.layout.user_detail_fragment, container, false);
		
		populateView(view);
		
		return view;
	}
	
	private void populateView(View view)
	{
		Bundle bundle = getArguments();
		
		TextView gTitle = (TextView)view.findViewById(R.id.user_title);
		gTitle.setText(bundle.getString(NAME));
		
		TextView gText = (TextView)view.findViewById(R.id.user_lastModified);
		gText.setText(bundle.getString(LAST_MODIFIED));
	}

	@Override
	public void onResume() {
		super.onResume();
		getActivity().getActionBar().setTitle(R.string.detail);
		setHasOptionsMenu(true);
	}
}
