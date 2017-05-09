package rs.reviewer.fragments;

import android.view.View;
import android.widget.ListView;

import rs.reviewer.model.AbstractModel;

/**
 * Lista sa detaljima. Treba implementirati onItemClick(T item) koji reaguje na klik na red u listi.
 * @param <T>
 */
public abstract class AbstractDetailListFragment<T extends AbstractModel> extends AbstractListFragment<T>
{
	public AbstractDetailListFragment()
	{}
	
	public AbstractDetailListFragment(int loaderId, int menuLayout)
	{
		super(loaderId, menuLayout);
	}
	
	@Override
	public void onListItemClick(ListView l, View v, int position, long id)
	{
		super.onListItemClick(l, v, position, id);
		onItemClick(getItem(position));
	}

	protected abstract void onItemClick(T item);
}
