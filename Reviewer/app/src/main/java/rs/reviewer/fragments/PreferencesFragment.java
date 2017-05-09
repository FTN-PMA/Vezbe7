package rs.reviewer.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import rs.reviewer.R;

public class PreferencesFragment extends Fragment {
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		return inflater.inflate(R.layout.frag_3, container, false);
	}
	
	@Override
	public void onResume() {
		super.onResume();
		getActivity().getActionBar().setTitle(R.string.preferences);
		setHasOptionsMenu(true);
	}
	
}
