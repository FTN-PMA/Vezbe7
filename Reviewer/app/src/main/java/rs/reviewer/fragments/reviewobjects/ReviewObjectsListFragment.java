package rs.reviewer.fragments.reviewobjects;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import rs.reviewer.R;
import rs.reviewer.adapters.AbstractArrayAdapter;
import rs.reviewer.adapters.ReviewObjectAdapter;
import rs.reviewer.fragments.AbstractDetailListFragment;
import rs.reviewer.loaders.ModelLoaderCallbacks;
import rs.reviewer.model.ReviewObject;
import rs.reviewer.model.Tag;
import rs.reviewer.tools.FragmentTransition;

import java.util.ArrayList;
import java.util.List;

public class ReviewObjectsListFragment extends AbstractDetailListFragment<ReviewObject>
{
	public static int REQUEST_TAG_FILTER = 0;
	public static String TAG_FILTER = "TAG_FILTER";
	private ArrayList<String> tagFilter;
	
	public ReviewObjectsListFragment()
	{
		super(R.id.REVIEW_OBJECT_LOADER, R.menu.review_object_list_menu);
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
	protected ModelLoaderCallbacks<ReviewObject> createLoaderCallbacks()
	{
		return new ModelLoaderCallbacks<ReviewObject>(getActivity(), ReviewObject.class, adapter)
		{
			protected List<ReviewObject> getData()
			{
				List<Tag> tags = new ArrayList<Tag>();
				for(String tagId : tagFilter)
				{
					tags.add(Tag.getByModelId(Tag.class, tagId));
				}
				return ReviewObject.getFilteredByTags(tags);
			}
		};
	}

	@Override
	protected AbstractArrayAdapter<ReviewObject> createAdapter()
	{
		return new ReviewObjectAdapter(getActivity());
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
		FragmentTransition.to(ReviewObjectFormFragment.newInstance(), getActivity());
	}

	@Override
	protected void onItemClick(ReviewObject item)
	{
		FragmentTransition.to(ReviewObjectTabsFragment.newInstance(item.getModelId()), getActivity());
	}
	
	@Override
	public void onResume()
	{
		super.onResume();
		// TODO: Promeniti
//		getActivity().getActionBar().setTitle(R.string.places);
	}
}