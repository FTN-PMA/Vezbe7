package rs.reviewer.loaders;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;

import com.activeandroid.Model;
import com.activeandroid.query.From;
import com.activeandroid.query.Select;
import rs.reviewer.adapters.AbstractArrayAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Koristi com.activeandroid.loaders.ModelLoader za rad ucitavanje podataka iz ActiveAndroid orm modela.
 * Moze se zadati From query koji ce se koristiti za dobavljanje podataka ili potpuno redefinisati
 * metoda {@link #getData()} koja vraca podatke za punjenje. Ako se nista ne zada dobavljaju se svi
 * redovi iz odgovarajuce tabele.
 * @author LaptopX
 *
 * @param <T> tip modela
 */
public class ModelLoaderCallbacks<T extends Model> implements LoaderCallbacks<List<T>>
{
	protected Class<T> modelClass;
	protected Context context;
	protected AbstractArrayAdapter<T> adapter;
	private boolean updateOnRelChange = false; // radi se update kada se povezane tabele updateuju
	protected From query = null; // active-android sql upit npr.: new Select().from(MojModel.class).where("n=?",5)
	private List<Class<? extends Model>> observedTables;
	
	public ModelLoaderCallbacks(Context context, Class<T> modelClass, AbstractArrayAdapter<T> adapter)
	{
		this.context = context;
		this.modelClass = modelClass;
		this.adapter = adapter;
	}
	
	public ModelLoaderCallbacks(Context context, Class<T> modelClass,
			AbstractArrayAdapter<T> adapter, boolean updateOnRelChange)
	{
		this(context, modelClass, adapter);
		this.updateOnRelChange = updateOnRelChange;
	}
	
	public ModelLoaderCallbacks(Context context, Class<T> modelClass,
			AbstractArrayAdapter<T> adapter, From query)
	{
		this(context, modelClass, adapter);
		this.query = query;
	}
	
	public ModelLoaderCallbacks(Context context, Class<T> modelClass,
			AbstractArrayAdapter<T> adapter, From query, boolean updateOnRelChange)
	{
		this(context, modelClass, adapter, query);
		this.updateOnRelChange = updateOnRelChange;
	}
	
	public ModelLoaderCallbacks(Context context, Class<T> modelClass,
			AbstractArrayAdapter<T> adapter, Class<? extends Model>... observedTables)
	{
		this(context, modelClass, adapter);
		this.observedTables = new ArrayList<Class<? extends Model>>(Arrays.asList(observedTables));
	}
	
	@Override
	public Loader<List<T>> onCreateLoader(int id, Bundle args)
	{
		ModelLoader<T> loader = new ModelLoader<T>(context, modelClass, updateOnRelChange)
		{
			@Override
			public List<T> loadInBackground()
			{
				return getData();
			}
		};
		
		if(observedTables != null)
		{
			loader.addObservedTables(observedTables);
		}
		
		return loader;
	}

	@Override
	public void onLoadFinished(Loader<List<T>> loader, List<T> data)
	{
		adapter.clear();
		adapter.addAll(data);
		//adapter.notifyDataSetChanged();
		// iz nekog razloga ne updateuje view dok se ne uradi filter
		// http://stackoverflow.com/questions/3414490/listview-not-updating-after-filtering
		adapter.getFilter().filter(null); 
	}

	@Override
	public void onLoaderReset(Loader<List<T>> loader)
	{
		adapter.setNotifyOnChange(false);
		adapter.clear();
		//adapter.notifyDataSetChanged();
		// iz nekog razloga ne updateuje view dok se ne uradi filter
		// http://stackoverflow.com/questions/3414490/listview-not-updating-after-filtering
		adapter.getFilter().filter(null);
	}
	
	/**
	 * Preuzima podatke iz baze. Sve ako nije nista zadato ili dobijene upitom ako je zadat query.
	 * Moze se redefinisati u klijentskom kodu.
	 * @return lista modela tipa T
	 */
	protected List<T> getData()
	{
		List<T> results;

		if (query == null)
		{
			results = new Select().from(modelClass).execute();
		}
		else
		{
			results = query.execute();
		}
		
		return results;
	}
}