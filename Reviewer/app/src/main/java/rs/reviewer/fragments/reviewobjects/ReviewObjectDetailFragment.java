package rs.reviewer.fragments.reviewobjects;

import android.annotation.SuppressLint;
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
import rs.reviewer.dialogs.DefaultCancelListener;
import rs.reviewer.loaders.ModelObserver;
import rs.reviewer.model.Image;
import rs.reviewer.model.Review;
import rs.reviewer.model.ReviewObject;
import rs.reviewer.model.Tag;
import rs.reviewer.model.TagToReviewObject;
import rs.reviewer.model.User;
import rs.reviewer.tools.CurrentUser;
import rs.reviewer.tools.FragmentTransition;
import rs.reviewer.tools.ImageUtils;
import rs.reviewer.tools.ReviewerTools;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

public class ReviewObjectDetailFragment extends Fragment
{
	public static final String NAME = "NAME";
	public static final String DESCRIPTION = "DESCRIPTION";
	public static final String LONGITUDE = "LONGITUDE";
	public static final String LATITUDE = "LATITUDE";
	public static final String IMAGE_PATH = "IMAGE_PATH";
	public static final String USER_CREATED = "USER_CREATED";
	public static final String TAGS = "TAGS";
	public static final String RATING = "RATING";
	public static final String ID = "ID";
	
	private ModelObserver modelObserver;
	
	private MapView mapView;
	private GoogleMap map;
	private Marker placeMarker;
	
	public static ReviewObjectDetailFragment newInstance(ReviewObject revob)
	{
		ReviewObjectDetailFragment newFrag = new ReviewObjectDetailFragment();
		newFrag.setArguments(new Bundle());
		newFrag.dataToArguments(revob);
		return newFrag;
	}
	
	private void dataToArguments(ReviewObject revob)
	{
		Bundle bundle = getArguments();
		bundle.putString(NAME, revob.getName());
		bundle.putString(DESCRIPTION, revob.getDescription());
		bundle.putDouble(LONGITUDE, revob.getLocationLong());
		bundle.putDouble(LATITUDE, revob.getLocationLat());
		Image mainImage = revob.getMainImage();
		String imagePath = (mainImage != null) ? mainImage.getPath() : null;
		bundle.putString(IMAGE_PATH, imagePath);
		bundle.putString(USER_CREATED, revob.getUserCreated().getName());
		List<String> tagNames = new ArrayList<String>();
		for(Tag tag : revob.getTags())
		{
			tagNames.add(tag.getName());
		}
		bundle.putString(TAGS,  ReviewerTools.getTagsString(tagNames));
		bundle.putFloat(RATING, revob.getAverageRating());
		bundle.putString(ID, revob.getModelId());
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		//postaviti da fragment ima meni
		setHasOptionsMenu(true);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void onAttach(Activity activity)
	{
		super.onAttach(activity);
		
		modelObserver = new ModelObserver(activity, ReviewObject.class, Review.class, TagToReviewObject.class, Image.class)
		{
			@Override
			public void onChange(boolean selfChange, Uri uri)
			{
				refreshView(getRevob());
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
		mapView.onResume();
		super.onResume();
	}
	
	@Override
	public void onDestroyView() {
		super.onDestroy();
		mapView.onDestroy();
	}
	
	@Override
	public void onLowMemory() {
		super.onLowMemory();
		mapView.onLowMemory();
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
	{
		super.onCreateOptionsMenu(menu, inflater);
		User currentUser = CurrentUser.getModel(getActivity());
		String currentUserId = currentUser.getModelId();
		
		//dodati meni
		inflater.inflate(R.menu.fragment_detail_menu, menu);
		
		if(!getRevob().isCreatedBy(currentUserId)) // ako nije kreirao trenutni user
		{
			menu.removeItem(R.id.edit_item);
			menu.removeItem(R.id.delete_item);
		}
	}
	
	@SuppressLint("InflateParams")
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		// handle item selection
		switch (item.getItemId()) {
			case R.id.edit_item:
				showEditDialog();
				return true;
			case R.id.delete_item:
				showDeleteDialog();
				return true;
		    default:
		    	return super.onOptionsItemSelected(item);
		}
	}
	
	private void showEditDialog()
	{
		FragmentTransition.to(ReviewObjectFormFragment.newInstance(getArguments().getString(ID)), getActivity());
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
				// obrisi revob
				getRevob().deleteSynced();
				
				Toast.makeText(getActivity(), R.string.deleted, Toast.LENGTH_SHORT).show();
				
				// obrisi ovaj fragment
				FragmentTransition.remove(ReviewObjectDetailFragment.this, getActivity());
			}
		})
		.setNegativeButton(R.string.cancel, new DefaultCancelListener())
		.show();
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View view = inflater.inflate(R.layout.revob_detail, container, false);
		
		// Gets the MapView from the XML layout and creates it
		mapView = (MapView) view.findViewById(R.id.mapview);
		mapView.onCreate(savedInstanceState);
		
		// Gets to GoogleMap from the MapView and does initialization stuff
		map = mapView.getMap();
		map.getUiSettings().setMyLocationButtonEnabled(true);
		map.setMyLocationEnabled(true);
		
		populateView(view);
		
		return view;
	}
	
	private void populateView(View view)
	{
		Bundle bundle = getArguments();
		
		TextView name = (TextView)view.findViewById(R.id.revob_name_content);
		name.setText(bundle.getString(NAME));
		
		TextView userCreated = (TextView)view.findViewById(R.id.revob_user_created_content);
		userCreated.setText(bundle.getString(USER_CREATED));
		
		RatingBar rating = (RatingBar)view.findViewById(R.id.revob_rating_content);
		rating.setRating(bundle.getFloat(RATING));
		
		TextView tags = (TextView)view.findViewById(R.id.revob_tags_content);
		tags.setText(bundle.getString(TAGS));
		
		TextView description = (TextView)view.findViewById(R.id.revob_description_content);
		description.setText(bundle.getString(DESCRIPTION));
		
		ImageView image = (ImageView)view.findViewById(R.id.revob_image_content);
		ImageUtils.setImageFromPath(image, bundle.getString(IMAGE_PATH), 128, 128);
		
		LatLng location = new LatLng(bundle.getDouble(LATITUDE), bundle.getDouble(LONGITUDE));
		
		if(placeMarker != null){
			placeMarker.remove();
		}
		
		placeMarker = map.addMarker(new MarkerOptions()
		.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
		.position(location));
		
		CameraPosition cameraPosition = new CameraPosition
				.Builder().target(location)
				.zoom(12).build();
		
		map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
	}
	
	private void refreshView(ReviewObject revob)
	{
		if(revob != null) dataToArguments(revob);
		if(getView() != null) populateView(getView());
	}
	
	private ReviewObject getRevob()
	{
		return ReviewObject.getByModelId(ReviewObject.class, getArguments().getString(ID));
	}
}