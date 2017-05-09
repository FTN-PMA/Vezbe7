package rs.reviewer.loaders;

import android.content.ContentResolver;
import android.content.Context;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;

import com.activeandroid.Model;
import com.activeandroid.content.ContentProvider;

public class ModelObserver extends ContentObserver
{
	private ContentResolver contentResolver;
	
	public ModelObserver(Context context, Class<? extends Model>... observedTables)
	{
		super(new Handler(context.getMainLooper()));
		this.contentResolver = context.getContentResolver();
		
		for(Class<? extends Model> observedTable : observedTables)
		{
			contentResolver.registerContentObserver(
					ContentProvider.createUri(observedTable, null),
					true, this);
		}
	}
	
	@Override
	public void onChange(boolean selfChange)
	{
		onChange(selfChange, null);
	}

	@Override
	public void onChange(boolean selfChange, Uri uri)
	{}
	
	public void unRegister()
	{
		// Stop monitoring for changes.
		contentResolver.unregisterContentObserver(this);
	}
}
