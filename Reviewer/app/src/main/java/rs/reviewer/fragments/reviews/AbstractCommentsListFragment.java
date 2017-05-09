package rs.reviewer.fragments.reviews;

import rs.reviewer.R;
import rs.reviewer.adapters.AbstractArrayAdapter;
import rs.reviewer.adapters.CommentsAdapter;
import rs.reviewer.fragments.AbstractListFragment;
import rs.reviewer.model.Comment;

public abstract class AbstractCommentsListFragment extends AbstractListFragment<Comment> {

	public static final String RELATED_ID = "RELATED_ID";
	
	public AbstractCommentsListFragment()
	{}
	
	public AbstractCommentsListFragment(String itemId, int menuLayout)
	{
		super(R.id.COMMENT_LOADER, menuLayout);
		getArguments().putString(RELATED_ID, itemId);
	}
	
	@Override
	protected AbstractArrayAdapter<Comment> createAdapter()
	{
		return new CommentsAdapter(getActivity());
	}
	
}
