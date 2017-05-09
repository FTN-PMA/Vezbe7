
package rs.reviewer.fragments.reviews;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import rs.reviewer.R;
import rs.reviewer.loaders.ModelObserver;
import rs.reviewer.model.Comment;
import rs.reviewer.model.GroupToReview;
import rs.reviewer.model.Image;
import rs.reviewer.model.Review;
import rs.reviewer.model.TagToReview;
import rs.reviewer.model.User;
import rs.reviewer.tools.CurrentUser;
import rs.reviewer.tools.FragmentTransition;
import rs.reviewer.tools.ImageUtils;
import rs.reviewer.tools.ReviewerTools;

public class ReviewDetailFragment extends Fragment {

	public static final String ID = "ID";
	public static final String NAME ="NAME";
	public static final String DESCRIPTION ="DESCRIPTION";
	public static final String CREATED = "CREATED";
	public static final String CREATED_BY = "CREATED_BY";
	public static final String IMAGE = "IMAGE";
	public static final String RATING = "RATING";
	
	private ModelObserver modelObserver;
	
	public static ReviewDetailFragment newInstance(Review review)
	{
		ReviewDetailFragment newFrag = new ReviewDetailFragment();
		newFrag.setArguments(new Bundle());
		newFrag.dataToArguments(review);
		return newFrag;
	}
	
	private void dataToArguments(Review review)
	{
		Bundle bundle = getArguments();
		bundle.putString(NAME, review.getName());
		bundle.putString(DESCRIPTION, review.getDescription());
		bundle.putString(CREATED,ReviewerTools.preapreDate(review.getDateCreated()));
		bundle.putString(CREATED_BY, review.getUserCreated().getName());
		Image mainImage = review.getMainImage();
		if(mainImage != null)
		{
			bundle.putString(IMAGE, mainImage.getPath());
		}
		bundle.putFloat(RATING, review.getRating());
		bundle.putString(ID, review.getModelId());
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//postaviti da fragment ima meni
		setHasOptionsMenu(true);
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
	{
		super.onCreateOptionsMenu(menu, inflater);
		
		User currentUser = CurrentUser.getModel(getActivity());
		String currentUserId = currentUser.getModelId();
		
		//dodati meni
		inflater.inflate(R.menu.fragment_detail_menu, menu);
		
		if(!getReview().isCreatedBy(currentUserId))
		{
			menu.removeItem(R.id.edit_item);
			menu.removeItem(R.id.delete_item);
		}
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		// handle item selection
		switch (item.getItemId()) {
			case R.id.edit_item:
				showEditDialog();
				return true;
			case R.id.delete_item:
				showDeleteDialog();
				//Toast.makeText(getActivity(), "Delete Review item pressed", Toast.LENGTH_LONG).show();
				return true;
		    default:
		    	return super.onOptionsItemSelected(item);
		}
	}
	
	private void showEditDialog() 
	{
		FragmentTransition.to(CreateReviewFragment.newEditInstance(getArguments().getString(ID)), getActivity());
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
				// obrisi review
				getReview().deleteSynced();
				
				Toast.makeText(getActivity(), R.string.deleted, Toast.LENGTH_SHORT).show();
				
				// obrisi ovaj fragment
				FragmentTransition.remove(ReviewDetailFragment.this, getActivity());
			}
		})
		.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener()
		{
			public void onClick(DialogInterface dialog, int id)
			{
				dialog.cancel();
			}
		})
		.show();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View view = inflater.inflate(R.layout.review_detail, container, false);

		populateView(view);
		
		return view;
	}
	
	private void populateView(View view)
	{
		Bundle bundle = getArguments();
		
		TextView name = (TextView)view.findViewById(R.id.review_name_content);
		name.setText(bundle.getString(NAME));
		
		TextView created = (TextView)view.findViewById(R.id.review_desc_contnt);
		created.setText(bundle.getString(CREATED));
		
		TextView created_by = (TextView)view.findViewById(R.id.created_by_content);
		created_by.setText(bundle.getString(CREATED_BY));
		
		RatingBar rating = (RatingBar)view.findViewById(R.id.review_rating_content);
		rating.setRating(bundle.getFloat(RATING));
		
		TextView description = (TextView)view.findViewById(R.id.review_description_contnt);
		description.setText(bundle.getString(DESCRIPTION));
		
		ImageView image = (ImageView)view.findViewById(R.id.review_image_content);
		ImageUtils.setImageFromPath(image, bundle.getString(IMAGE), 128, 128);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void onAttach(Activity activity)
	{
		super.onAttach(activity);
		
		modelObserver = new ModelObserver(activity, Review.class, GroupToReview.class, Comment.class,
				TagToReview.class, Image.class)
		{
			@Override
			public void onChange(boolean selfChange, Uri uri)
			{
				refreshView(getReview());
			}
		};
	}	
	
	@Override
	public void onDestroy()
	{
		super.onDestroy();
		modelObserver.unRegister();
	}
	
	@Override
	public void onResume() {
		super.onResume();
		setHasOptionsMenu(true);
	}
	
	@Override
	public void onDestroyView() {
		super.onDestroy();
	}
	
	@Override
	public void onLowMemory() {
		super.onLowMemory();
	}
	
	private Review getReview()
	{
		return Review.getByModelId(Review.class, getArguments().getString(ID));
	}
	
	private void refreshView(Review review)
	{
		if(review != null)
		{
			dataToArguments(review);
		}
		
		if(getView() != null)
		{
			populateView(getView());
		}
	}
}
