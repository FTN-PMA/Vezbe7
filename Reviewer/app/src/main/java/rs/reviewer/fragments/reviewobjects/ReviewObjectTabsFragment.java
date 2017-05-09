package rs.reviewer.fragments.reviewobjects;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import rs.reviewer.R;
import rs.reviewer.adapters.pagers.ReviewObjectPagerAdapter;

import java.util.List;

public class ReviewObjectTabsFragment extends Fragment
{
	private ReviewObjectPagerAdapter mReviewObjectPagerAdapter;
	private ViewPager mViewPager;
	
	public static final String ID = "ID";
	
	public static ReviewObjectTabsFragment newInstance(String id)
	{
		ReviewObjectTabsFragment newFrag = new ReviewObjectTabsFragment();
		Bundle bundle = new Bundle();
		bundle.putString(ID, id);
		newFrag.setArguments(bundle);
		return newFrag;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View v = inflater.inflate(R.layout.revob_fragment_tabbed, container, false);
		mReviewObjectPagerAdapter = new ReviewObjectPagerAdapter(getArguments().getString(ID), getChildFragmentManager(), getActivity());
        
        mViewPager = (ViewPager) v.findViewById(R.id.revob_pager);
        mViewPager.setAdapter(mReviewObjectPagerAdapter);
        
        //Laja buraz resio :D
        mViewPager.setOffscreenPageLimit(mReviewObjectPagerAdapter.getCount()-1);
        
        return v;
	}
	
	@Override
	public void onResume() {
		super.onResume();
		// TODO: Promeniti
//		getActivity().getActionBar().setTitle(R.string.review_object_details);
	}
	
	@Override
	// da bi se pozvao onActivityResult u fragmentima u ugnjezdenim tabovima
	// http://stackoverflow.com/questions/13580075/onactivityresult-not-called-in-new-nested-fragment-api
	public void onActivityResult(int requestCode, int resultCode, Intent data)
	{
        super.onActivityResult(requestCode, resultCode, data);
        List<Fragment> fragments = getChildFragmentManager().getFragments();
        if (fragments != null) {
            for (Fragment fragment : fragments)
            {
                fragment.onActivityResult(requestCode, resultCode, data);
            }
        }
    }
}