package rs.reviewer.adapters.pagers;

import android.content.Context;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;

import rs.reviewer.fragments.ImageDetailFragment;
import rs.reviewer.loaders.ModelObserver;
import rs.reviewer.model.Image;

import java.util.List;

public abstract class ImagePagerAdapter extends FragmentStatePagerAdapter
{
	protected String relatedId;
	protected List<Image> images;
	private ModelObserver modelObserver;
	
	@SuppressWarnings("unchecked")
	public ImagePagerAdapter(String relatedId, FragmentManager fm, Context context)
	{
		super(fm);
		this.relatedId = relatedId;
		
		modelObserver = new ModelObserver(context, Image.class)
		{
			@Override
			public void onChange(boolean selfChange, Uri uri)
			{
				refreshState();
				notifyDataSetChanged();
			}
		};
		
		refreshState();
	}
	
	public void unregisterObservers()
	{
		modelObserver.unRegister();
	}
	
	protected abstract void refreshState();
	
	@Override
	public int getCount()
	{
		return images.size();
	}
	
	@Override
	// da bi hteo da obrise fragment kad se obrise slika
	// http://stackoverflow.com/questions/10033315/destroy-pages-on-viewpager-and-fragmentstatepageradapter
	public int getItemPosition(Object object)
	{
		return PagerAdapter.POSITION_NONE;
	}
	
	@Override
	public Fragment getItem(int position)
	{
		return ImageDetailFragment.getInstance(images.get(position));
	}
}