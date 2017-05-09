package rs.reviewer.fragments.reviews;

import rs.reviewer.adapters.pagers.ImagePagerAdapter;
import rs.reviewer.adapters.pagers.ReviewImagePagerAdapter;
import rs.reviewer.fragments.ImagePagerFragment;

public class ReviewImagePagerFragment extends ImagePagerFragment {

	public ReviewImagePagerFragment()
	{}
	
	public ReviewImagePagerFragment(String id, int initialPosition)
	{
		super(id, initialPosition);
	}

	@Override
	protected ImagePagerAdapter getImagePagerAdapter()
	{
		return new ReviewImagePagerAdapter(getArguments().getString(RELATED_ID), getChildFragmentManager(), getActivity());
	}
}
