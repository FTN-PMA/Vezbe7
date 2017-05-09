package rs.reviewer.fragments.reviewobjects;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import rs.reviewer.R;
import rs.reviewer.adapters.AbstractArrayAdapter;
import rs.reviewer.adapters.TagCheckAdapter;
import rs.reviewer.fragments.AbstractCheckListFragment;
import rs.reviewer.loaders.ModelLoaderCallbacks;
import rs.reviewer.model.ReviewObject;
import rs.reviewer.model.Tag;
import rs.reviewer.model.TagToReviewObject;
import rs.reviewer.tools.FragmentTransition;

public class ReviewObjectTagsCheckListFragment extends AbstractCheckListFragment<Tag>
{
	public static final String RELATED_ID = "RELATED_ID";
	
	public ReviewObjectTagsCheckListFragment()
	{}
	
	public ReviewObjectTagsCheckListFragment(String itemId)
	{
		super(R.id.REVIEW_OBJECT_TAG_CHECK_LOADER, R.menu.standard_list_menu);
		getArguments().putString(RELATED_ID, itemId);
	}

	@Override
	protected ModelLoaderCallbacks<Tag> createLoaderCallbacks()
	{
		return new ModelLoaderCallbacks<Tag>(getActivity(), Tag.class, adapter, TagToReviewObject.class);
	}
	
	@Override
	protected void onItemCheck(Tag item, boolean checked)
	{
		ReviewObject reviewObj = ReviewObject.getByModelId(ReviewObject.class, getArguments().getString(RELATED_ID));
		if(checked)
		{
			item.addReviewObject(reviewObj);
		}
		else
		{
			item.removeReviewObject(reviewObj);
		}
	}

	@Override
	protected AbstractArrayAdapter<Tag> createAdapter()
	{
		final String reviewObjId = getArguments().getString(RELATED_ID);
		
		return new TagCheckAdapter(getActivity())
		{
			@Override
			protected boolean isChecked(Tag item)
			{
				return item.hasReviewObject(reviewObjId);
			}
		};
	}

	@Override
	protected void configureMenu(Menu menu, MenuInflater inflater)
	{
		menu.findItem(R.id.menu_action)
		.setIcon(R.drawable.ic_action_accept)
		.setTitle(R.string.accept);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		// handle item selection
		switch (item.getItemId()) {
			case R.id.menu_action:
				onMenuAction();
				return true;
		    default:
		    	return super.onOptionsItemSelected(item);
		}
	}
	
	private void onMenuAction()
	{
		FragmentTransition.remove(this, getActivity());
	}
}