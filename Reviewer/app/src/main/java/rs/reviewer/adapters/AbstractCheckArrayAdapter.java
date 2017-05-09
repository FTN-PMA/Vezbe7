package rs.reviewer.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckedTextView;
import android.widget.ListView;

/**
 * Apstraktni adapter za check liste. 
 * Treba redefinisati getMainDataToDisplay(T item) koja vraca String koji se ispisuje za svaki red 
 * i isChecked(T item) koja vraca da li red sa odgovarajucim itemom treba da bude cekiran.
 * @param <T>
 */
public abstract class AbstractCheckArrayAdapter<T> extends AbstractArrayAdapter<T>
{
	public AbstractCheckArrayAdapter(Context context)
	{
		super(context, android.R.layout.simple_list_item_multiple_choice);
	}

	@Override
	protected void populateView(View view, T item)
	{
		((CheckedTextView)view).setText(getMainDataToDisplay(item));
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		View view = super.getView(position, convertView, parent);
		((ListView)parent).setItemChecked(position, isChecked(getItem(position)));
		return view;
	}

	protected abstract boolean isChecked(T item);
}
