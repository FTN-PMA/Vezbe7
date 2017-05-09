package rs.reviewer.fragments;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import rs.reviewer.model.AbstractModel;

/**
 * Check lista, treba implementirati onItemCheck(T item, boolean checked) koji reaguje na cekiranje reda u listi.
 * @param <T>
 */
public abstract class AbstractCheckListFragment<T extends AbstractModel> extends AbstractListFragment<T>
{
	public AbstractCheckListFragment()
	{}
	
	public AbstractCheckListFragment(int loaderId, int menuLayout)
	{
		super(loaderId, menuLayout);
	}
	
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);
		getListView().setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
	}
	
	@Override
	public void onListItemClick(ListView l, View v, int position, long id)
	{
		super.onListItemClick(l, v, position, id);
		boolean checked = getListView().getCheckedItemPositions().get(position);
		onItemCheck(getItem(position), checked);
	}

	protected abstract void onItemCheck(T item, boolean checked);
}
