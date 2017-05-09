package rs.reviewer.fragments.reviewobjects;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteConstraintException;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;

import rs.reviewer.R;
import rs.reviewer.adapters.AbstractArrayAdapter;
import rs.reviewer.adapters.TagsAdapter;
import rs.reviewer.dialogs.DefaultCancelListener;
import rs.reviewer.dialogs.ShowDialog;
import rs.reviewer.fragments.AbstractDetailListFragment;
import rs.reviewer.loaders.ModelLoaderCallbacks;
import rs.reviewer.model.ReviewObject;
import rs.reviewer.model.Tag;
import rs.reviewer.model.TagToReviewObject;
import rs.reviewer.tools.CurrentUser;
import rs.reviewer.tools.FragmentTransition;
import rs.reviewer.validators.NameValidator;
import rs.reviewer.validators.TextValidator;

import java.util.List;

public class ReviewObjectTagsListFragment extends AbstractDetailListFragment<Tag>
{
	public static final String RELATED_ID = "RELATED_ID";
	
	public ReviewObjectTagsListFragment()
	{}
	
	public ReviewObjectTagsListFragment(String itemId)
	{
		super(R.id.TAG_LOADER, R.menu.review_tags_list_menu);
		getArguments().putString(RELATED_ID, itemId);
	}

	@Override
	protected AbstractArrayAdapter<Tag> createAdapter()
	{
		return new TagsAdapter(getActivity());
	}

	@Override
	protected void onItemClick(Tag item)
	{
	}
	
	@SuppressWarnings("unchecked")
	@Override
	protected ModelLoaderCallbacks<Tag> createLoaderCallbacks()
	{
		return new ModelLoaderCallbacks<Tag>(getActivity(), Tag.class, adapter, TagToReviewObject.class)
		{
			@Override
			protected List<Tag> getData()
			{
				return getReviewObject().getTags();
			}
		};
	}
	
	@Override
	protected void configureMenu(Menu menu, MenuInflater inflater)
	{
		if(getReviewObject().isCreatedBy(CurrentUser.getId(getActivity())))
		{
			menu.findItem(R.id.menu_action)
			.setIcon(R.drawable.ic_action_edit)
			.setTitle(R.string.edit_item);
		}
		else
		{
			menu.removeItem(R.id.menu_action);
			menu.removeItem(R.id.add_action);
		}
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		// handle item selection
		switch (item.getItemId()) {
			case R.id.menu_action:
				onMenuAction();
				return true;
			case R.id.add_action:
				addTagDialog();
				return true;
		    default:
		    	return super.onOptionsItemSelected(item);
		}
	}

	private void onMenuAction()
	{
		FragmentTransition.to(new ReviewObjectTagsCheckListFragment(getArguments().getString(RELATED_ID)), getActivity());
	}
	
	private ReviewObject getReviewObject()
	{
		return ReviewObject.getByModelId(ReviewObject.class, getArguments().getString(RELATED_ID));
	}
	
	private void addTagDialog()
	{
		final EditText content = new EditText(getActivity());
		final AlertDialog dialog = new AlertDialog.Builder(getActivity())
		.setView(content)
		.setTitle(R.string.tag_name)
		.setPositiveButton(R.string.tag_name, new DialogInterface.OnClickListener()
		{
			@SuppressLint("DefaultLocale")
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				String newTag = content.getText().toString().toUpperCase();
				
				Tag tag = new Tag(newTag);
				try
				{
					tag.saveOrThrow();
					getReviewObject().addTag(tag);
				}
				catch(SQLiteConstraintException ex)
				{
					ShowDialog.error(getActivity().getString(R.string.contains_tag_message), getActivity());
				}
			}
		})
		.setNegativeButton(R.string.cancel, new DefaultCancelListener())
		.create();
		
		TextValidator tagValidator = new NameValidator(dialog, content, 10);
		content.addTextChangedListener(tagValidator);
		
		dialog.show();
		dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
	}
}
