package rs.reviewer.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

import rs.reviewer.R;
import rs.reviewer.dialogs.DefaultCancelListener;
import rs.reviewer.loaders.BitmapWorkerTask;
import rs.reviewer.model.Image;
import rs.reviewer.model.Review;
import rs.reviewer.model.ReviewObject;
import rs.reviewer.tools.ImageUtils;

public class ImageDetailFragment extends Fragment
{
	public static final String ID = "ID";
	public static final String PATH = "PATH";
	private ImageView mImageView;
	
	public static ImageDetailFragment getInstance(Image image)
	{
		ImageDetailFragment newFrag = new ImageDetailFragment();
		final Bundle args = new Bundle();
		args.putString(ID, image.getModelId());
		args.putString(PATH, image.getPath());
		newFrag.setArguments(args);
		return newFrag;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		//postaviti da fragment ima meni
		setHasOptionsMenu(true);
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		
		//dodati meni
		inflater.inflate(R.menu.image, menu);
		
		if(getImage().isMain())
		{
			menu.removeItem(R.id.set_as_main);
		}
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		// image_detail_fragment.xml contains just an ImageView
		final View v = inflater.inflate(R.layout.image_detail_fragment, container, false);
		mImageView = (ImageView) v.findViewById(R.id.imageView);
		
		// Load image into ImageView
		final Context context = getActivity();
		ViewTreeObserver viewTreeObserver = mImageView.getViewTreeObserver();
		viewTreeObserver.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
		    public boolean onPreDraw() {
		        // Remove after the first run so it doesn't fire forever
		    	mImageView.getViewTreeObserver().removeOnPreDrawListener(this);
		        int height = mImageView.getMeasuredHeight();
		        int width = mImageView.getMeasuredWidth();
		        // load in background
		        BitmapWorkerTask.loadBitmap(getArguments().getString(PATH), mImageView, width, height,
						R.drawable.ic_action_picture, context);
		        return true;
		    }
		});
		
		return v;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		// handle item selection
		switch (item.getItemId()) {
			case R.id.delete_item:
				showDeleteDialog();
				return true;
			case R.id.set_as_main:
				setAsMain();
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}

	protected void setAsMain()
	{
		Image image = getImage();
		Image prevMainImage = null;
		ReviewObject revob = image.getReviewObject();
		Review review = image.getReview();
		
		if(revob != null)
		{
			prevMainImage = revob.getMainImage();
		}
		if(review != null)
		{
			prevMainImage = review.getMainImage();
		}
		
		if(prevMainImage != null)
		{
			prevMainImage.setMain(false);
			prevMainImage.save();
		}
		
		image.setMain(true);
		image.save();
	}

	protected void delete()
	{
		Image image = getImage();
		image.deleteSynced();
		ImageUtils.delete(image.getPath());
	}
	
	private void showDeleteDialog()
	{
		new AlertDialog.Builder(getActivity())
		.setTitle(R.string.remove_item)
		.setMessage(R.string.are_you_sure)
		.setPositiveButton(R.string.remove_item, new OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				delete();
			}
		})
		.setNegativeButton(R.string.cancel, new DefaultCancelListener())
		.show();
	}
	
	private Image getImage()
	{
		return Image.getByModelId(Image.class, getArguments().getString(ID));
	}
}
