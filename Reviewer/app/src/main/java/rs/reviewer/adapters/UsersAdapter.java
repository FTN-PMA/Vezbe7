package rs.reviewer.adapters;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import rs.reviewer.R;
import rs.reviewer.model.User;
import rs.reviewer.tools.ReviewerTools;

public class UsersAdapter extends AbstractArrayAdapter<User>
{
	public UsersAdapter(Context context)
	{
		super(context, R.layout.user_item);
	}

	@Override
	protected void populateView(View view, User item)
	{
		TextView name = (TextView)view.findViewById(R.id.user_name);
		name.setText(item.getName());
		
		TextView date = (TextView)view.findViewById(R.id.user_date_created);
		date.setText(ReviewerTools.preapreDate(item.getDateModified()));
	}
	
	@Override
	protected String getTextToFilter(User item)
	{
		return item.getName();
	}
}
