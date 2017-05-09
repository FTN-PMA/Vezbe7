package rs.reviewer.adapters;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import rs.reviewer.R;
import rs.reviewer.model.Image;
import rs.reviewer.model.Review;
import rs.reviewer.tools.ImageUtils;
import rs.reviewer.tools.ReviewerTools;

public class ReviewsAdapter extends AbstractArrayAdapter<Review>
{
	public ReviewsAdapter(Context context)
	{
		super(context, R.layout.review_item);
	}

	@Override
	protected void populateView(View view, Review item)
	{
		TextView name = (TextView)view.findViewById(R.id.review_name);
		name.setText(item.getName());
		
		TextView date = (TextView)view.findViewById(R.id.review_date_created);
		date.setText(ReviewerTools.preapreDate(item.getDateCreated()));
		
		RatingBar rating = (RatingBar)view.findViewById(R.id.review_rating_list);
		rating.setRating((float)item.getRating());
		
		ImageView image = (ImageView)view.findViewById(R.id.review_item_icon);
		Image mainImage = item.getMainImage();
		String imagePath = (mainImage != null) ? mainImage.getPath() : null;
		ImageUtils.setImageFromPath(image, imagePath, 40, 40, R.drawable.ic_action_picture);
	}
	
	@Override
	protected String getTextToFilter(Review item)
	{
		return item.getName();
	}
}
