package rs.reviewer.fragments.reviews;

import rs.reviewer.R;
import rs.reviewer.adapters.AbstractArrayAdapter;
import rs.reviewer.adapters.ReviewsAdapter;
import rs.reviewer.fragments.AbstractDetailListFragment;
import rs.reviewer.model.Review;
import rs.reviewer.tools.FragmentTransition;

/**
 * Lista reviewova sa detaljima. Na klik otvara detalje za review.
 */
public abstract class AbstractReviewsListFragment extends AbstractDetailListFragment<Review>
{
	public static final String RELATED_ID = "RELATED_ID";
	
	public AbstractReviewsListFragment()
	{}
	
	public AbstractReviewsListFragment(String itemId, int menuLayout)
	{
		super(R.id.GROUP_REVIEW_LOADER, menuLayout);
		getArguments().putString(RELATED_ID, itemId);
	}

	@Override
	protected AbstractArrayAdapter<Review> createAdapter()
	{
		return new ReviewsAdapter(getActivity());
	}

	@Override
	protected void onItemClick(Review item)
	{
		FragmentTransition.to(new ReviewTabFragment(item.getModelId()), getActivity());
	}
}
