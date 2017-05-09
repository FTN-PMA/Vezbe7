package rs.reviewer.adapters;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import rs.reviewer.R;
import rs.reviewer.model.Comment;
import rs.reviewer.tools.ReviewerTools;

public class CommentsAdapter extends AbstractArrayAdapter<Comment>
{
	public CommentsAdapter(Context context)
	{
		super(context, R.layout.comment_item);
	}

	@Override
	protected void populateView(View view, Comment item)
	{
		TextView content = (TextView)view.findViewById(R.id.content);
		content.setText(item.getContent());
		
		TextView userCreated = (TextView)view.findViewById(R.id.user_created);
		userCreated.setText(item.getUserCreated().getName());
		
		TextView dateCreated = (TextView)view.findViewById(R.id.date_created);
		dateCreated.setText(ReviewerTools.preapreDate(item.getDateModified()));
	}
	
	@Override
	protected String getTextToFilter(Comment item)
	{
		return item.getUserCreated().getName();
	}
}
