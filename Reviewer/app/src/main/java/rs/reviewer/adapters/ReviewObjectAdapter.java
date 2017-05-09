package rs.reviewer.adapters;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import rs.reviewer.R;
import rs.reviewer.model.Image;
import rs.reviewer.model.ReviewObject;
import rs.reviewer.tools.ImageUtils;
import rs.reviewer.tools.ReviewerTools;

public class ReviewObjectAdapter extends AbstractArrayAdapter<ReviewObject>
{
	public ReviewObjectAdapter(Context context)
	{
		super(context, R.layout.review_object_item);
	}

	@Override
	protected void populateView(View view, ReviewObject item)
	{
		TextView name = (TextView)view.findViewById(R.id.revob_name);
		name.setText(item.getName());
		
		TextView date = (TextView)view.findViewById(R.id.revob_short_description);
		date.setText(ReviewerTools.getShortString(item.getDescription(), 20));
		
		RatingBar rating = (RatingBar)view.findViewById(R.id.revob_rating_list);
		rating.setRating(item.getAverageRating());
		
		ImageView image = (ImageView)view.findViewById(R.id.revob_item_icon);
		Image mainImage = item.getMainImage();
		String imagePath = (mainImage != null) ? mainImage.getPath() : null;
		ImageUtils.setImageFromPath(image, imagePath, 40, 40, R.drawable.ic_action_place);
	}
	
	@Override
	protected String getTextToFilter(ReviewObject item)
	{
		return item.getName();
	}
}
