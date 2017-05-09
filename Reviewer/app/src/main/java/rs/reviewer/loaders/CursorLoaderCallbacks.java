package rs.reviewer.loaders;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.CursorAdapter;

import com.activeandroid.Model;
import com.activeandroid.content.ContentProvider;

/**
 * LoaderCallbacks koji koristi CursorLoader za rad sa active-android orm.
 * Deprecated, koristiti {@link #ModelLoaderCallbacks} (pogodniji za orm).
 * @author LaptopX
 *
 */
@Deprecated
public class CursorLoaderCallbacks implements LoaderCallbacks<Cursor>
{
	private Context currentContext;
	private CursorAdapter cursorAdapter;
	private Class<? extends Model> entityClass;
	private String[] projection = null;
	private String selection = null;
	private String[] selectionArgs = null;
	private String sortOrder = null;
	
	public CursorLoaderCallbacks(Context currentContext, CursorAdapter cursorAdapter, Class<? extends Model> entityClass)
	{
		this.currentContext = currentContext;
		this.cursorAdapter = cursorAdapter;
		this.entityClass = entityClass;
	}

	public CursorLoaderCallbacks projection(String... projection)
	{
		this.projection = projection;
		return this;
	}
	
	public CursorLoaderCallbacks selection(String selection, String... selectionArgs)
	{
		this.selection = selection;
		this.selectionArgs = selectionArgs;
		return this;
	}
	
	public CursorLoaderCallbacks sortOrder(String sortOrder)
	{
		this.sortOrder = sortOrder;
		return this;
	}
	
    @Override
    public Loader<Cursor> onCreateLoader(int arg0, Bundle cursor)
    {
        return new CursorLoader(currentContext,
            ContentProvider.createUri(entityClass, null),
            projection, selection, selectionArgs, sortOrder
        );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> arg0, Cursor cursor)
    {
        cursorAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> arg0)
    {
    	cursorAdapter.swapCursor(null);
    }
}
