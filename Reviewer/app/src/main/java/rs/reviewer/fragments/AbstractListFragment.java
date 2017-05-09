package rs.reviewer.fragments;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SearchView;

import rs.reviewer.R;
import rs.reviewer.adapters.AbstractArrayAdapter;
import rs.reviewer.loaders.ModelLoaderCallbacks;
import rs.reviewer.model.AbstractModel;

public abstract class AbstractListFragment<T extends AbstractModel> extends ListFragment
{
	public static final String MENU_LAYOUT = "MENU_ACTION_ICON";
	public static final String LOADER_ID = "LOADER_ID";
	
	protected AbstractArrayAdapter<T> adapter;
	
	public AbstractListFragment()
	{}
	
	public AbstractListFragment(int loaderId, int menuLayout)
	{
		Bundle bundle = new Bundle();
		bundle.putInt(MENU_LAYOUT, menuLayout);
		bundle.putInt(LOADER_ID, loaderId);
		setArguments(bundle);
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		//postaviti da fragment ima meni
		setHasOptionsMenu(true);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		View view = new ListView(getActivity());
		
		adapter = createAdapter();
		
		// restartuj ili ako ne postoji pokreni nov loader
		getActivity().getSupportLoaderManager().restartLoader(getLoaderId(), null, createLoaderCallbacks());
		
		setListAdapter(adapter);
		
		return view;
	}
	
	public void reloadData()
	{
		getActivity().getSupportLoaderManager().restartLoader(getLoaderId(), null, createLoaderCallbacks());
	}
	
	public int getLoaderId()
	{
		return getArguments().getInt(LOADER_ID);
	};
	
	protected abstract ModelLoaderCallbacks<T> createLoaderCallbacks();
	protected abstract AbstractArrayAdapter<T> createAdapter();

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
	{
		super.onCreateOptionsMenu(menu, inflater);
		
		//dodati meni
		inflater.inflate(getArguments().getInt(MENU_LAYOUT), menu);
		
		// podesiti sta treba za custom meni
		configureMenu(menu, inflater);
		
		MenuItem searchItem = menu.findItem(R.id.action_search);
		if(searchItem != null)
		{
			SearchView searchView = (SearchView)searchItem.getActionView();
			SearchView.OnQueryTextListener textChangeListener = new SearchView.OnQueryTextListener()
	        {
	            @Override
	            public boolean onQueryTextChange(String newText)
	            {
	            	if(adapter != null && newText != null)
	            	{
	            		adapter.getFilter().filter(newText.toString());
	            		return true;
	            	}
	            	else
	            	{
	            		return false;
	            	}
	            }
	            @Override
	            public boolean onQueryTextSubmit(String query)
	            {
	                return false;
	            }
	        };
//	        searchView.setOnQueryTextListener(textChangeListener);
		}
	}
	
	protected void configureMenu(Menu menu, MenuInflater inflater){}

	public T getItem(int position)
	{
		return adapter.getItem(position);
	}
}
