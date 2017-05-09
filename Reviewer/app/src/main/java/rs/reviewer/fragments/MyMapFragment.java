package rs.reviewer.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import rs.reviewer.R;
import rs.reviewer.dialogs.LocationDialog;
import rs.reviewer.fragments.reviewobjects.ReviewObjectFormFragment;
import rs.reviewer.fragments.reviewobjects.ReviewObjectTabsFragment;
import rs.reviewer.model.Review;
import rs.reviewer.model.ReviewObject;
import rs.reviewer.tools.FragmentTransition;
import rs.reviewer.tools.ReviewerTools;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MyMapFragment extends Fragment implements LocationListener, OnMapReadyCallback{

	public static int REQUEST_TAG_FILTER = 0;
	public static String TAG_FILTER = "TAG_FILTER";
	private ArrayList<String> tagFilter;

	private GoogleMap map;
	private SupportMapFragment mMapFragment;
	private LocationManager locationManager;
	private String provider;
	private AlertDialog dialog;

	private Marker home;
	private HashMap<Marker, ReviewObject> markers;

	public static MyMapFragment newInstance() {
		
		MyMapFragment mpf = new MyMapFragment();
		
		//mpf.markers = new HashMap<Marker, ReviewObject>();
		
		return mpf;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		createMapFragmentAndInflate();
		
		if(savedInstanceState != null)
		{
			tagFilter = savedInstanceState.getStringArrayList(TAG_FILTER);
		}
		else
		{
			tagFilter = new ArrayList<String>();
		}
	}

	
	private void showLocatonDialog(){
		if(dialog == null){
			dialog = new LocationDialog(getActivity()).prepareDialog();
		}else{
			if(dialog.isShowing()){
				dialog.dismiss();
			}
		}
		
		dialog.show();
	}

	@Override
	public void onSaveInstanceState(Bundle outState)
	{
		super.onSaveInstanceState(outState);
		outState.putStringArrayList(TAG_FILTER, tagFilter);
	}
	
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		// Toast.makeText(getActivity(), "onResume()",
		// Toast.LENGTH_SHORT).show();

//		getActivity().getActionBar().setTitle(R.string.home);
		setHasOptionsMenu(true);

		//Toast.makeText(getActivity(), "onResume()", Toast.LENGTH_SHORT).show();

		createMapFragmentAndInflate();

		boolean gps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
		boolean wifi = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
		
		if (!gps && !wifi){
			//new LocationDialog(getActivity()).prepareDialog().show();
			showLocatonDialog();
		} else {
			// Toast.makeText(getActivity(), "noService",
			// Toast.LENGTH_SHORT).show();
			
			locationManager.requestLocationUpdates(provider, 0, 0, this);
		}
		
		if(markers == null)
		{
			markers = new HashMap<Marker, ReviewObject>();
		}

	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);

		// dodati meni
		inflater.inflate(R.menu.home_menu, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// handle item selection
		switch (item.getItemId()) {
			case R.id.home_add_item:
				FragmentTransition.to(ReviewObjectFormFragment.newInstance(), getActivity());
				return true;
		    default:
		    	return super.onOptionsItemSelected(item);
		}
	}

	private void createMapFragmentAndInflate() {
		// Get LocationManager object from System Service LOCATION_SERVICE
		locationManager = (LocationManager) getActivity().getSystemService(
				Context.LOCATION_SERVICE);

		Criteria criteria = new Criteria();
		provider = locationManager.getBestProvider(criteria, true);

		mMapFragment = SupportMapFragment.newInstance();
		FragmentTransaction transaction = getChildFragmentManager()
				.beginTransaction();
		transaction.replace(R.id.map_container, mMapFragment).commit();

		mMapFragment.getMapAsync(this);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup vg, Bundle data) {

		View view = inflater.inflate(R.layout.map_layout, vg, false);

		return view;

	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();

		locationManager.removeUpdates(this);
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();

		locationManager.removeUpdates(this);
	}

	private void fillTheMapWithRevObjects(GoogleMap map, Location location)
	{
		SharedPreferences sharedPreferences = PreferenceManager
				.getDefaultSharedPreferences(getActivity().getApplicationContext());
		String lookupRadius = sharedPreferences.getString(getString(R.string.pref_radius), "1");//1km
		double radius = Double.parseDouble(lookupRadius);
		
		List<ReviewObject> list = ReviewObject.getFiltered(ReviewerTools.stringListToTagList(tagFilter),
				location.getLatitude(), location.getLongitude(), radius);
		
		//clear from list
		map.clear();
		
		//clear marker
		markers.clear();
		
		for(ReviewObject rev : list)
		{
			LatLng loc = new LatLng(rev.getLocationLat(), rev.getLocationLong());
			Marker marker = map.addMarker(new MarkerOptions()
				.title(rev.getName())
				.snippet(rev.getDescription())
				.icon(BitmapDescriptorFactory
				.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
				.position(loc));
			
			markers.put(marker, rev);
		}
	}
	
	private void addMarker(Location location) {
		// Toast.makeText(getActivity(), "addMarker",
		// Toast.LENGTH_SHORT).show();
		LatLng loc = new LatLng(location.getLatitude(), location.getLongitude());

		if (home != null) {
			home.remove();
		}
		
		fillTheMapWithRevObjects(map, location);
		
		home = map.addMarker(new MarkerOptions()
				.title("Hey there.")
				.snippet("You are here at the moment :)")
				.icon(BitmapDescriptorFactory
				.defaultMarker(BitmapDescriptorFactory.HUE_RED))
				.position(loc));

		CameraPosition cameraPosition = new CameraPosition.Builder()
				.target(loc).zoom(12).build();

		map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
	}

	@Override
	public void onLocationChanged(Location location) {
		//Toast.makeText(getActivity(), "onLocationChange()", Toast.LENGTH_SHORT).show();

		addMarker(location);
	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onMapReady(GoogleMap googleMap) {
		Location location = locationManager.getLastKnownLocation(provider);

		//Toast.makeText(getActivity(), "onMapReady()", Toast.LENGTH_SHORT).show();

		map = googleMap;
		map.setInfoWindowAdapter(new MapInfoAdapter());
		map.setOnMarkerClickListener(new OnMarkerClickListener() {
			
			@Override
			public boolean onMarkerClick(Marker marker) {
				
				if(markers.containsKey(marker))
				{
					marker.showInfoWindow();
				}
				return true;
			}
		});
		
		map.setOnInfoWindowClickListener(new OnInfoWindowClickListener() {
			
			@Override
			public void onInfoWindowClick(Marker arg0) {
				ReviewObject rev = markers.get(arg0);
				
				FragmentTransition.to(ReviewObjectTabsFragment.newInstance(rev.getModelId()), getActivity());
				
			}
		});

		if (location != null) {
			addMarker(location);
		}
	}
	
	private class MapInfoAdapter implements InfoWindowAdapter{

		public MapInfoAdapter()
		{
			// TODO Auto-generated constructor stub
		}
		
		@Override
		public View getInfoContents(Marker arg0)
		{
			View view = getLayoutInflater(null).inflate(R.layout.info_window_layout, null);
			
			ReviewObject rev = markers.get(arg0);
			
			TextView title = (TextView) view.findViewById(R.id.marker_name);
			title.setText(rev.getName());
				
			TextView detail = (TextView) view.findViewById(R.id.marked_detail);
			detail.setText(rev.getDescription());
				
			ImageView image = (ImageView) view.findViewById(R.id.marker_picture);
				
			if(rev.getMainImage() != null)
			{
				if(!rev.getMainImage().getPath().equals(""))
				{
					Bitmap bmp = BitmapFactory.decodeFile(rev.getMainImage().getPath());
					image.setImageBitmap(bmp);
				}
			}
			
			
			float rat = 0;
				
			for(Review revobj : rev.getReviews())
			{
				rat+=revobj.getRating();
			}
				
			if(rat > 0)
			{
				rat = rat / rev.getReviews().size();
			}
			
			RatingBar rating = (RatingBar) view.findViewById(R.id.marker_rating);
			rating.setRating(rat);
			
			return view;
		}

		@Override
		public View getInfoWindow(Marker arg0) {
			return null;
		}
		
	}

}