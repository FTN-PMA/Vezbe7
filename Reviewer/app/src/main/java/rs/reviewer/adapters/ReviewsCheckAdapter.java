package rs.reviewer.adapters;

import android.content.Context;

import rs.reviewer.model.Review;

public abstract class ReviewsCheckAdapter extends AbstractCheckArrayAdapter<Review>
{
	public ReviewsCheckAdapter(Context context)
	{
		super(context);
	}
	
	@Override
	protected String getMainDataToDisplay(Review item)
	{
		return item.getName();
	}
}
