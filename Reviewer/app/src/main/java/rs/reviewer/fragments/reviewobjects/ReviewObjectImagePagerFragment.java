package rs.reviewer.fragments.reviewobjects;

import rs.reviewer.adapters.pagers.ImagePagerAdapter;
import rs.reviewer.adapters.pagers.ReviewObjectImagePagerAdapter;
import rs.reviewer.fragments.ImagePagerFragment;

public class ReviewObjectImagePagerFragment extends ImagePagerFragment
{
	public ReviewObjectImagePagerFragment()
	{}
	
	public ReviewObjectImagePagerFragment(String id, int initialPosition)
	{
		super(id, initialPosition);
	}

	@Override
	protected ImagePagerAdapter getImagePagerAdapter()
	{
		return new ReviewObjectImagePagerAdapter(getArguments().getString(RELATED_ID), getChildFragmentManager(), getActivity());
	}
}
