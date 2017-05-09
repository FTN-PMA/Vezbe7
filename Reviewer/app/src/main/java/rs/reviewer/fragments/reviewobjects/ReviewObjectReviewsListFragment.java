package rs.reviewer.fragments.reviewobjects;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import rs.reviewer.R;
import rs.reviewer.fragments.reviews.AbstractReviewsListFragment;
import rs.reviewer.fragments.reviews.CreateReviewFragment;
import rs.reviewer.loaders.ModelLoaderCallbacks;
import rs.reviewer.model.Review;
import rs.reviewer.model.ReviewObject;
import rs.reviewer.model.Tag;
import rs.reviewer.tools.CurrentUser;
import rs.reviewer.tools.FragmentTransition;

import java.util.ArrayList;
import java.util.List;

public class ReviewObjectReviewsListFragment extends AbstractReviewsListFragment
{
	public static int REQUEST_TAG_FILTER = 0;
	public static String TAG_FILTER = "TAG_FILTER";
	private ArrayList<String> tagFilter;
	
	public ReviewObjectReviewsListFragment()
	{}
	
	public ReviewObjectReviewsListFragment(String itemId)
	{
		super(itemId, R.menu.review_list_menu);
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		if(savedInstanceState != null)
		{
			tagFilter = savedInstanceState.getStringArrayList(TAG_FILTER);
		}
		else
		{
			tagFilter = new ArrayList<String>();
		}
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState)
	{
		super.onSaveInstanceState(outState);
		outState.putStringArrayList(TAG_FILTER, tagFilter);
	}
	
	@Override
	protected ModelLoaderCallbacks<Review> createLoaderCallbacks()
	{
		return new ModelLoaderCallbacks<Review>(getActivity(), Review.class, adapter)
		{
			@Override
			protected List<Review> getData()
			{
				List<Tag> tags = new ArrayList<Tag>();
				for(String tagId : tagFilter)
				{
					tags.add(Tag.getByModelId(Tag.class, tagId));
				}
				return getRevob().getFilteredReviews(tags, CurrentUser.getModel(context));
			}
		};
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		// handle item selection
		switch (item.getItemId()) {
			case R.id.add_action:
				onMenuAction();
				return true;
		    default:
		    	return super.onOptionsItemSelected(item);
		}
	}

	private void onMenuAction()
	{
		FragmentTransition.to(CreateReviewFragment.newCreateInstance(getArguments().getString(RELATED_ID)), getActivity());
	}
	
	private ReviewObject getRevob()
	{
		return ReviewObject.getByModelId(ReviewObject.class, getArguments().getString(RELATED_ID));
	}
}
