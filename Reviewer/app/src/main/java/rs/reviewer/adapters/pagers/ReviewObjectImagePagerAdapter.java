package rs.reviewer.adapters.pagers;

import android.content.Context;
import android.support.v4.app.FragmentManager;

import rs.reviewer.model.ReviewObject;

public class ReviewObjectImagePagerAdapter extends ImagePagerAdapter
{
	public ReviewObjectImagePagerAdapter(String relatedId, FragmentManager fm, Context context)
	{
		super(relatedId, fm, context);
	}

	@Override
	protected void refreshState()
	{
		images = getRevob().getImages();
	}
	
	private ReviewObject getRevob()
	{
		return ReviewObject.getByModelId(ReviewObject.class, relatedId);
	}
}
