package rs.reviewer.fragments.reviewobjects;

import android.graphics.Bitmap;
import android.view.View;
import android.widget.AdapterView;

import rs.reviewer.R;
import rs.reviewer.adapters.GaleryAdapter;
import rs.reviewer.fragments.GaleryGridFragment;
import rs.reviewer.loaders.ModelLoaderCallbacks;
import rs.reviewer.model.Image;
import rs.reviewer.model.ReviewObject;
import rs.reviewer.tools.FragmentTransition;
import rs.reviewer.tools.ImageUtils;

import java.util.List;

public class ReviewObjectGalleryFragment extends GaleryGridFragment
{
	public static final String RELATED_ID = "RELATED_ID";
	
	public ReviewObjectGalleryFragment()
	{}
	
	public ReviewObjectGalleryFragment(String itemId)
	{
		super(R.id.REVOB_IMAGE_LOADER);
		getArguments().putString(RELATED_ID, itemId);
	}

	@Override
	protected void addImage(Bitmap bitmap)
	{
		ReviewObject revob = getRevob();
		String imagePath = ImageUtils.save(bitmap, revob.getModelId() + "_" + revob.getImages().size(), getActivity());
		new Image(imagePath, false, revob).save();
	}

	@Override
	protected ModelLoaderCallbacks<Image> getModelLoaderCallbacks(GaleryAdapter myAdapter)
	{
		return new ModelLoaderCallbacks<Image>(getActivity(), Image.class, myAdapter)
		{
			@Override
			protected List<Image> getData()
			{
				return getRevob().getImages();
			}
		};
	}
	
	private ReviewObject getRevob()
	{
		return ReviewObject.getByModelId(ReviewObject.class, getArguments().getString(RELATED_ID));
	}

	@Override
	protected void onItemClick(AdapterView<?> parent, View view, int position, long id)
	{
		FragmentTransition.to(new ReviewObjectImagePagerFragment(getArguments().getString(RELATED_ID), position), getActivity());
	}
}
