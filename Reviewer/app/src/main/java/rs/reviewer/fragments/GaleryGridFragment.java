package rs.reviewer.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import rs.reviewer.R;
import rs.reviewer.adapters.GaleryAdapter;
import rs.reviewer.loaders.ModelLoaderCallbacks;
import rs.reviewer.model.Image;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public abstract class GaleryGridFragment extends Fragment
{
	private static final CharSequence[] items = { "Take Photo", "Choose from Library", "Cancel" };
	private static final int REQUEST_CAMERA = 1;
	private static final int SELECT_PHOTO = 2;
	protected static final String LOADER_ID = "LOADER_ID";
	private GaleryAdapter myAdapter;
	
	public GaleryGridFragment()
	{}
	
	public GaleryGridFragment(int loaderId)
	{
		Bundle bundle = new Bundle();
		bundle.putInt(LOADER_ID, loaderId);
		setArguments(bundle);
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
	{
		super.onCreateOptionsMenu(menu, inflater);
		
		//dodati meni
		inflater.inflate(R.menu.galery, menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		// handle item selection
		switch (item.getItemId()) {
			case R.id.add_item:
				selectImage();
				return true;
		    default:
		    	return super.onOptionsItemSelected(item);
		}
	}
	
	//Choose image from camera or galery
	private void selectImage() {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setTitle("Add Photo");
		builder.setItems(items, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int position) {
				if (items[position].equals("Take Photo")) {
					Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
					getParentFragment().startActivityForResult(intent, REQUEST_CAMERA);
				}else if (items[position].equals("Choose from Library")) {
					Intent intent = new Intent(Intent.ACTION_PICK,
							MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
					intent.setType("image/*");
					getParentFragment().startActivityForResult(Intent.createChooser(intent, "Select photo"),
							SELECT_PHOTO);
				}else if (items[position].equals("Cancel")) {
					dialog.cancel();
				}
			}
			
		});
		builder.show();
	}
	
	private Bitmap setUpImage(Intent data){
		InputStream stream = null;
		try {
			stream = getActivity().getContentResolver().openInputStream(data.getData());
			Bitmap bitmap = Bitmap.createBitmap(BitmapFactory.decodeStream(stream));
			return bitmap;
			
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
		return null;
	}
	
	private Bitmap takePhoto(Bundle extras){
		return (Bitmap) extras.get("data");
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		if (resultCode == Activity.RESULT_OK) {
			if(requestCode == REQUEST_CAMERA){
				addImage(takePhoto(data.getExtras()));
			}else if(requestCode == SELECT_PHOTO){
				addImage(setUpImage(data));
			}
		}
	}
	
	protected abstract void addImage(Bitmap bitmap);
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		View view = inflater.inflate(R.layout.galery_layout, container, false);
		
		myAdapter = new GaleryAdapter(getActivity());
		
		getActivity().getSupportLoaderManager().restartLoader(getArguments().getInt(LOADER_ID), null, getModelLoaderCallbacks(myAdapter));
		
		GridView gridview = (GridView)view.findViewById(R.id.gridview);
		gridview.setAdapter(myAdapter);
		gridview.setOnItemClickListener(new OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id)
			{
				GaleryGridFragment.this.onItemClick(parent, view, position, id);
			}
		});
		
		return view;
	}
	
	protected abstract ModelLoaderCallbacks<Image> getModelLoaderCallbacks(GaleryAdapter myAdapter);
	protected abstract void onItemClick(AdapterView<?> parent, View view, int position, long id);
}
