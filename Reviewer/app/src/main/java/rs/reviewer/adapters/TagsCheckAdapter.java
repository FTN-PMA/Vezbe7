package rs.reviewer.adapters;

import android.content.Context;

import rs.reviewer.model.Tag;

public abstract class TagsCheckAdapter extends AbstractCheckArrayAdapter<Tag>
{
	public TagsCheckAdapter(Context context)
	{
		super(context);
	}
	
	@Override
	protected String getMainDataToDisplay(Tag item)
	{
		return item.getName();
	}
}