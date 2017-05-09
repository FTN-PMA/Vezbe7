package rs.reviewer.adapters;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

/**
 * Jednostavan adapter koji nasledjuje AbstractArrayAdapter i popunjava jedno text polje u layoutu.
 * Potrebno je redefinisati {@link #getDataToDisplay(T)} za konkretan model.
 * @author LaptopX
 *
 * @param <T> tip modela
 */
public class SingleFieldArrayAdapter<T> extends AbstractArrayAdapter<T>
{
	public SingleFieldArrayAdapter(Context context)
	{
		super(context, android.R.layout.simple_expandable_list_item_1);
	}
	
	public SingleFieldArrayAdapter(Context context, int resource)
	{
		super(context, resource);
	}
	
	public SingleFieldArrayAdapter(Context context, int resource, int textViewResourceId)
	{
		super(context, resource, textViewResourceId);
	}
	
	@Override
	protected void populateView(View view, T item)
	{
		TextView text;
		try
		{
			if (mFieldId == 0)
			{
				// If no custom field is assigned, assume the whole resource is
				// a TextView
				text = (TextView) view;
			}
			else
			{
				// Otherwise, find the TextView field within the layout
				text = (TextView) view.findViewById(mFieldId);
			}
		}
		catch (ClassCastException e)
		{
			Log.e("ArrayAdapter",
					"You must supply a resource ID for a TextView");
			throw new IllegalStateException(
					"ArrayAdapter requires the resource ID to be a TextView", e);
		}
		
		if (item instanceof CharSequence)
		{
			text.setText((CharSequence) item);
		}
		else
		{
			text.setText(getDataToDisplay(item)[0]);
		}
	}
}
