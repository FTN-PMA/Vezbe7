package rs.reviewer.events;

import android.app.ActionBar.Tab;
import android.app.ActionBar.TabListener;
import android.app.Fragment;
import android.app.FragmentTransaction;

public class MyTabListener implements TabListener {
	
	private Fragment fragment;
	
	public MyTabListener(Fragment fragment) {
		super();
		this.fragment = fragment;
	}

	@Override
	public void onTabReselected(Tab arg0, FragmentTransaction ft) {
		
		
	}

	@Override
	public void onTabSelected(Tab arg0, FragmentTransaction ft) {
		//ft.replace(R.id.fragmentContainer, fragment);
		
	}

	@Override
	public void onTabUnselected(Tab arg0, FragmentTransaction ft) {
		ft.remove(fragment);
		
	}

}
