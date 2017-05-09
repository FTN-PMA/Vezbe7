package rs.reviewer.fragments.reviewobjects;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteConstraintException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import rs.reviewer.R;
import rs.reviewer.dialogs.DefaultCancelListener;
import rs.reviewer.dialogs.ShowDialog;
import rs.reviewer.exceptions.ValidationException;
import rs.reviewer.model.Image;
import rs.reviewer.model.ReviewObject;
import rs.reviewer.model.Tag;
import rs.reviewer.tools.CurrentUser;
import rs.reviewer.tools.FragmentTransition;
import rs.reviewer.tools.ImageUtils;
import rs.reviewer.tools.ReviewerTools;
import rs.reviewer.validators.NameValidator;
import rs.reviewer.validators.TextValidator;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerDragListener;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ReviewObjectFormFragment extends Fragment //implements LocationListener, OnMapReadyCallback
{
	private CharSequence[] items = { "Take Photo", "Choose from Library", "Cancel" };
	private int REQUEST_CAMERA = 1;
	private int SELECT_PHOTO = 2;
	
	// key names
	private static final String ID = "ID";
	private static final String NAME = "NAME";
	private static final String DESCRIPTION = "DESCRIPTION";
	private static final String IMAGE = "IMAGE";
	private static final String IMAGE_PATH = "IMAGE_PATH";
	private static final String LONGITUDE = "LONGITUDE";
	private static final String LATITUDE = "LATITUDE";
	private static final String TAGS = "TAGS";
	private static final String EDITED_IMAGE = "EDITED_IMAGE";
	
	// views
	private TextView textName;
	private TextView textDesc;
	private ImageView mImageView;
	private MapView mapView;
	
	// data
	private Bitmap bitmap;
	private ArrayList<String> tags;
	private String id;
	private boolean editedImage = false;
	private Marker placeMarker;
	
	private static final double NULL_COORDINATE = 500; // nemoguca vrednost za long i lat
	
	// map
	private GoogleMap map;
	private LocationManager locationManager;
	private String provider;
	
	// validators
	private TextValidator nameValidator;
	private TextValidator descriptionValidator;
	
	/**
	 * Za create.
	 */
	public static ReviewObjectFormFragment newInstance()
	{
		return new ReviewObjectFormFragment();
	}
	
	/**
	 * Za update.
	 * @param modelId postojeceg ReviewObjecta
	 */
	public static ReviewObjectFormFragment newInstance(String modelId)
	{
		ReviewObjectFormFragment newFrag = new ReviewObjectFormFragment();
		Bundle bundle = new Bundle();
		
		ReviewObject revob = ReviewObject.getByModelId(ReviewObject.class, modelId);
		
		bundle.putString(ID, modelId);
		bundle.putString(NAME, revob.getName());
		bundle.putString(DESCRIPTION, revob.getDescription());
		Image image = revob.getMainImage();
		if(image != null) bundle.putString(IMAGE_PATH, image.getPath());
		bundle.putDouble(LONGITUDE, revob.getLocationLong());
		bundle.putDouble(LATITUDE, revob.getLocationLat());
		ArrayList<String> tagnames = new ArrayList<String>();
		for(Tag tag : revob.getTags())
		{
			tagnames.add(tag.getName());
		}
		bundle.putStringArrayList(TAGS, tagnames);
		
		newFrag.setArguments(bundle);
		return newFrag;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		//postaviti da fragment ima meni
		setHasOptionsMenu(true);
		
		// Get LocationManager object from System Service LOCATION_SERVICE
		locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
		provider = locationManager.getBestProvider(new Criteria(), true);
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState)
	{
		super.onSaveInstanceState(outState);
		
		outState.putString(ID, id);
		
		outState.putParcelable(IMAGE, bitmap);
		if(placeMarker != null)
		{
			outState.putDouble(LONGITUDE, placeMarker.getPosition().longitude);
			outState.putDouble(LATITUDE,  placeMarker.getPosition().latitude);
		}
		else
		{
			outState.putDouble(LONGITUDE, NULL_COORDINATE);
			outState.putDouble(LATITUDE,  NULL_COORDINATE);
		}
		outState.putStringArrayList(TAGS, tags);
		outState.putBoolean(EDITED_IMAGE, editedImage);
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		
		//dodati meni
		inflater.inflate(R.menu.create_review_menu, menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		// handle item selection
		switch (item.getItemId()) {
			case R.id.save_item:
				save();
				return true;
			case R.id.cancel_item:
				FragmentTransition.remove(this, getActivity());
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		double longitude = NULL_COORDINATE;
		double latitude = NULL_COORDINATE;
		String name = null;
		String description = null;
		
		// get views
		View view = inflater.inflate(R.layout.create_rev_object, container, false);
		
		mImageView = (ImageView)view.findViewById(R.id.reviewobject_image);
		textName = (TextView)view.findViewById(R.id.reviewobject_name_edit);
		textDesc = (TextView)view.findViewById(R.id.reviewobject_desc);
		final TextView textTags = (TextView)view.findViewById(R.id.review_object_tags_list);
		
		// set listeners
		ImageButton choose_tags = (ImageButton)view.findViewById(R.id.choose_object_tags);
		choose_tags.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				addTagDialog(textTags);
			}
		});
		
		ImageButton removeTags = (ImageButton)view.findViewById(R.id.remove_object_tags);
		removeTags.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(tags.size() > 0) 
				{
					tags.remove(tags.size()-1);
					textTags.setText(ReviewerTools.getTagsString(tags));
				}
			}
		});
		
		ImageButton chooseImage = (ImageButton)view.findViewById(R.id.reviewobject_image_choose);
		chooseImage.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				selectImage();
			}
		});
		
		// set validators
		nameValidator = new TextValidator(textName)
		{
			int maxLength = 20;
			
			@Override
			public void validate(TextView textView, String text)
			{
				if(text == null || "".equals(text.trim()))
				{
					textView.setError(getActivity().getString(R.string.name_empty_message));
					throw new ValidationException();
				}
				else if(!isAlphanumeric(text))
				{
					textView.setError(getActivity().getString(R.string.name_alphanum_message));
					throw new ValidationException();
				}
				else if(text.length() > maxLength)
				{
					textView.setError(getActivity().getString(R.string.name_maxlength_message).replace("{}", Integer.toString(maxLength)));
					throw new ValidationException();
				}
				else
				{
					textView.setError(null);
				}
			}
		};
		textName.addTextChangedListener(nameValidator);
		
		descriptionValidator = new TextValidator(textDesc)
		{
			int maxLength = 500;
			
			@Override
			public void validate(TextView textView, String text)
			{
				if(!isAlphanumericWithInterpunction(text))
				{
					textView.setError(getActivity().getString(R.string.desc_alphanum_message));
					throw new ValidationException();
				}
				else if(text.length() > maxLength)
				{
					textView.setError(getActivity().getString(R.string.desc_maxlength_message).replace("{}", Integer.toString(maxLength)));
					throw new ValidationException();
				}
				else
				{
					textView.setError(null);
				}
			}
		};
		textDesc.addTextChangedListener(descriptionValidator);
		
		// restore state
		if (savedInstanceState != null)
		{
			id = savedInstanceState.getString(ID);
			bitmap = (Bitmap) savedInstanceState.getParcelable(IMAGE);
			longitude = savedInstanceState.getDouble(LONGITUDE);
			latitude = savedInstanceState.getDouble(LATITUDE);
			tags = savedInstanceState.getStringArrayList(TAGS);
			editedImage = savedInstanceState.getBoolean(EDITED_IMAGE);
		}
		else
		{
			Bundle arguments = getArguments();
			if(arguments != null)
			{
				id = arguments.getString(ID);
				name = arguments.getString(NAME);
				description = arguments.getString(DESCRIPTION);
				String imagePath = arguments.getString(IMAGE_PATH);
				if(imagePath != null) bitmap = ImageUtils.loadImageFromStorage(imagePath);
				longitude = arguments.getDouble(LONGITUDE);
				latitude = arguments.getDouble(LATITUDE);
				tags = arguments.getStringArrayList(TAGS);
			}
		}
		
		// populate view
		if(name != null)
		{
			textName.setText(name);
		}
		
		if(description != null)
		{
			textDesc.setText(description);
		}
		
		if(tags == null)
		{
			tags = new ArrayList<String>();
		}
		else
		{
			textTags.setText(ReviewerTools.getTagsString(tags));
		}
		
		if(bitmap != null){
			mImageView.setImageBitmap(bitmap);
		}
		
		// Gets the MapView from the XML layout and creates it
		mapView = (MapView) view.findViewById(R.id.mapview);
		mapView.onCreate(savedInstanceState);
		
		// Gets to GoogleMap from the MapView and does initialization stuff
		map = mapView.getMap();
		map.getUiSettings().setMyLocationButtonEnabled(true);
		map.setMyLocationEnabled(true);
		map.setOnMapLongClickListener(new OnMapLongClickListener()
		{
			@Override
			public void onMapLongClick(LatLng loc)
			{
				setMarker(loc);
			}
		});
		
		// iz nekog razloga nece da se updateuje pozicija markera ako mapa nema setovan ovaj listener
		map.setOnMarkerDragListener(new EmptyOnMarkerDragListener());
		
		if(latitude != NULL_COORDINATE && longitude != NULL_COORDINATE) // ako postoje sacuvane koordinate
		{
			setMarker(new LatLng(latitude, longitude)); // podesi marker na njih
		}
		else // inace
		{
			Location location = locationManager.getLastKnownLocation(provider); // podesi na trenutnu lokaciju
			if(location != null && placeMarker == null) setMarker(location);
		}
		
		return view;
	}
	
	// save/update to database
	private void save()
	{
		String name = textName.getText().toString();
		String desc = textDesc.getText().toString();
		
		// validate
		try
		{
			if(placeMarker == null)
			{
				ShowDialog.error(getActivity().getString(R.string.enter_place_location_message), getActivity());
				return;
			}
			nameValidator.validate();
			descriptionValidator.validate();
		}
		catch(ValidationException ex)
		{
			return;
		}
		
		// save
		try
		{
			ReviewObject newReviewObject;
			if(id != null) // update
			{
				newReviewObject = ReviewObject.getByModelId(ReviewObject.class, id);
				newReviewObject.setName(name);
				newReviewObject.setDescription(desc);
				newReviewObject.setLocation(placeMarker.getPosition().longitude, placeMarker.getPosition().latitude);
				newReviewObject.setDateModified(new Date());
			}
			else // create new
			{
				newReviewObject = new ReviewObject(name, desc, placeMarker.getPosition().longitude,
						placeMarker.getPosition().latitude, 
						CurrentUser.getModel(getActivity()));
			}
			
			newReviewObject.saveOrThrow(); // persist
			
			List<Tag> existingTagModels = newReviewObject.getTags();
			List<String> existingTagNames = new ArrayList<String>();
			
			// obrisi sve tagove koji su izbaceni
			for(Tag tag : existingTagModels)
			{
				if(!tags.contains(tag.getName())) // sve koji nisu vise u tags listi
				{
					newReviewObject.removeTag(tag); // obrisi
				}
				else
				{
					existingTagNames.add(tag.getName()); // dodaj u listu postojecih imena za kasnije
				}
			}
			
			// dodaj sve tagove koji su dodati
			for(String tagName : tags)
			{
				if(!existingTagNames.contains(tagName)) // ako nije vec dodat
				{
					Tag tag = Tag.getByName(tagName);
					if(tag == null) // ako vec ne postji tag
					{
						tag = new Tag(tagName);
						tag.saveOrThrow(); // dodaj nov
					}
					
					newReviewObject.addTag(tag); // dodaj tag u newReviewObject
				}
			}
			
			if(bitmap != null && editedImage) // ako postoji slika i izmenjena je
			{
				Image oldMainImage = newReviewObject.getMainImage();
				if(oldMainImage != null) // ako postoji stara slika podesi je da nije main
				{
					oldMainImage.setMain(false);
					oldMainImage.save();
				}
				
				// ime slike je <id_rev_objekta>_<broj_slika>
				String imagePath = ImageUtils.save(bitmap, newReviewObject.getModelId() + "_" + newReviewObject.getImages().size(),
						getActivity());
				new Image(imagePath, true, newReviewObject).saveOrThrow();
			}
			
			if(id == null)
			{
				Toast.makeText(getActivity(), R.string.created, Toast.LENGTH_SHORT).show();
			}
			else
			{
				Toast.makeText(getActivity(), R.string.edited, Toast.LENGTH_SHORT).show();
			}
			
			FragmentTransition.remove(this, getActivity());
		}
		catch(SQLiteConstraintException ex)
		{
			ShowDialog.error(getActivity().getString(R.string.error_saving_message), getActivity());
		}
	}
	
	private void selectImage() {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setTitle(R.string.add_photo);
		builder.setItems(items, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int position) {
				if (items[position].equals("Take Photo")) {
					Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
					startActivityForResult(intent, REQUEST_CAMERA);
				}else if (items[position].equals("Choose from Library")) {
					Intent intent = new Intent(Intent.ACTION_PICK,
							MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
					intent.setType("image/*");
					startActivityForResult(Intent.createChooser(intent, "Select photo"),
							SELECT_PHOTO);
				}else if (items[position].equals("Cancel")) {
					dialog.cancel();
				}
			}
			
		});
		builder.show();
	}
	
	private void setUpImage(Intent data){
		InputStream stream = null;
		try {
			// recyle unused bitmaps
			if (bitmap != null) {
				bitmap.recycle();
			}
			
			stream = getActivity().getContentResolver().openInputStream(data.getData());
			
			bitmap = BitmapFactory.decodeStream(stream);
			editedImage = true;
			
			mImageView.setImageBitmap(bitmap);
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally{
			if (stream != null){
				try {
					stream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
	}
	
	private void takePhoto(Bundle extras){
		// recyle unused bitmaps
		if (bitmap != null) {
			bitmap.recycle();
		}
		
		bitmap = (Bitmap) extras.get("data");
		editedImage = true;
		mImageView.setImageBitmap(bitmap);
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == Activity.RESULT_OK) {
			if(requestCode == REQUEST_CAMERA){
				takePhoto(data.getExtras());
			}else if(requestCode == SELECT_PHOTO){
				setUpImage(data);
			}
		}
	}
	
	private void setMarker(Location location)
	{
		LatLng loc = new LatLng(location.getLatitude(), location.getLongitude());
		setMarker(loc);
	}
	
	private void setMarker(LatLng loc)
	{
		if(placeMarker != null){
			placeMarker.remove();
		}
		
		placeMarker = map.addMarker(new MarkerOptions()
		.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
		.position(loc)
		.draggable(true));
		
		CameraPosition cameraPosition = new CameraPosition
				.Builder().target(loc)
				.zoom(12).build();
		
		map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
	}
	
	private void addTagDialog(final TextView tagContent)
	{
		final EditText content = new EditText(getActivity());
		final AlertDialog dialog = new AlertDialog.Builder(getActivity())
		.setView(content)
		.setTitle(R.string.tag_name)
		.setPositiveButton(R.string.tag_name, new DialogInterface.OnClickListener()
		{
			@SuppressLint("DefaultLocale")
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				String newTag = content.getText().toString().toUpperCase();
				
				if(!tags.contains(newTag))
				{
					tags.add(newTag);
					tagContent.setText(ReviewerTools.getTagsString(tags));
				}
				else
				{
					ShowDialog.error(getActivity().getString(R.string.contains_tag_message), getActivity());
				}
			}
		})
		.setNegativeButton(R.string.cancel, new DefaultCancelListener())
		.create();
		
		TextValidator tagValidator = new NameValidator(dialog, content, 10);
		content.addTextChangedListener(tagValidator);
		
		dialog.show();
		dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
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
	
	// iz nekog razloga nece da se updateuje pozicija markera ako mapa nema setovan ovaj listener
	// http://stackoverflow.com/questions/14829195/google-maps-error-markers-position-is-not-updated-after-drag
	private class EmptyOnMarkerDragListener implements OnMarkerDragListener
	{
		public void onMarkerDragStart(Marker arg0) {}
		public void onMarkerDragEnd(Marker arg0) {}
		public void onMarkerDrag(Marker arg0) {}
	}
}
