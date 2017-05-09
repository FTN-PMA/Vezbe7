package rs.reviewer.fragments.reviews;

import android.graphics.Bitmap;
import android.view.View;
import android.widget.AdapterView;

import rs.reviewer.R;
import rs.reviewer.adapters.GaleryAdapter;
import rs.reviewer.fragments.GaleryGridFragment;
import rs.reviewer.loaders.ModelLoaderCallbacks;
import rs.reviewer.model.Image;
import rs.reviewer.model.Review;
import rs.reviewer.tools.FragmentTransition;
import rs.reviewer.tools.ImageUtils;

import java.util.List;

public class ReviewGalleryFragment extends GaleryGridFragment{

	public static final String RELATED_ID = "RELATED_ID";
	
	public ReviewGalleryFragment()
	{}
	
	public ReviewGalleryFragment(String itemId)
	{
		super(R.id.REVIEW_IMAGE_LOADER);
		getArguments().putString(RELATED_ID, itemId);
	}
	
	@Override
	protected void addImage(Bitmap bitmap)
	{
		Review review = getReview();
		String imagePath = ImageUtils.save(bitmap, review.getModelId() + "_" + review.getImages().size(), getActivity());
		new Image(imagePath, false, review).save();		
	}

	@Override
	protected ModelLoaderCallbacks<Image> getModelLoaderCallbacks(GaleryAdapter myAdapter)
	{
		return new ModelLoaderCallbacks<Image>(getActivity(), Image.class, myAdapter)
		{
			@Override
			protected List<Image> getData()
			{
				return getReview().getImages();
			}
		};
	}
	
	private Review getReview()
	{
		return Review.getByModelId(Review.class, getArguments().getString(RELATED_ID));
	}

	@Override
	protected void onItemClick(AdapterView<?> parent, View view, int position, long id)
	{
		FragmentTransition.to(new ReviewImagePagerFragment(getArguments().getString(RELATED_ID), position), getActivity());
	}

}
