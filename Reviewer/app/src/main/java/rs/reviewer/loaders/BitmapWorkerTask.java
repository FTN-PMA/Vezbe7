package rs.reviewer.loaders;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.widget.ImageView;

import rs.reviewer.tools.ImageUtils;

import java.lang.ref.WeakReference;

public class BitmapWorkerTask extends AsyncTask<String, Void, Bitmap>
{
	private final WeakReference<ImageView> imageViewReference;
	private String path;
	private int width;
	private int height;
	
	public BitmapWorkerTask(ImageView imageView, int width, int height)
	{
		// Use a WeakReference to ensure the ImageView can be garbage
		// collected
		imageViewReference = new WeakReference<ImageView>(imageView);
		this.width = width;
		this.height = height;
	}
	
	// Decode image in background.
	@Override
    protected Bitmap doInBackground(String... params)
	{
        path = params[0];
        return ImageUtils.decodeSampledBitmapFromFile(path, width, height);
    }
	
	// Once complete, see if ImageView is still around and set bitmap.
	@Override
	protected void onPostExecute(Bitmap bitmap)
	{
		if(isCancelled())
		{
			bitmap = null;
		}
		
		if(imageViewReference != null && bitmap != null)
		{
			final ImageView imageView = imageViewReference.get();
			final BitmapWorkerTask bitmapWorkerTask = getBitmapWorkerTask(imageView);
			if(this == bitmapWorkerTask && imageView != null)
			{
				imageView.setImageBitmap(bitmap);
			}
		}
	}
	
	private static BitmapWorkerTask getBitmapWorkerTask(ImageView imageView)
	{
		if(imageView != null)
		{
			final Drawable drawable = imageView.getDrawable();
			if(drawable instanceof AsyncDrawable)
			{
				final AsyncDrawable asyncDrawable = (AsyncDrawable) drawable;
				return asyncDrawable.getBitmapWorkerTask();
			}
		}
		return null;
	}
	
	public static class AsyncDrawable extends BitmapDrawable
	{
		private final WeakReference<BitmapWorkerTask> bitmapWorkerTaskReference;
		
		public AsyncDrawable(Resources res, Bitmap bitmap, BitmapWorkerTask bitmapWorkerTask)
		{
			super(res, bitmap);
			bitmapWorkerTaskReference = new WeakReference<BitmapWorkerTask>(bitmapWorkerTask);
		}
		
		public BitmapWorkerTask getBitmapWorkerTask()
		{
			return bitmapWorkerTaskReference.get();
		}
	}
	
	public static boolean cancelPotentialWork(String path, ImageView imageView)
	{
		final BitmapWorkerTask bitmapWorkerTask = getBitmapWorkerTask(imageView);
		
		if(bitmapWorkerTask != null)
		{
			final String bitmapPath = bitmapWorkerTask.path;
			// If bitmapData is not yet set or it differs from the new data
			if(bitmapPath == null || !bitmapPath.equals(path))
			{
				// Cancel previous task
				bitmapWorkerTask.cancel(true);
			}
			else
			{
				// The same work is already in progress
				return false;
			}
		}
		// No task associated with the ImageView, or an existing task was
		// cancelled
		return true;
	}
	
	public static void loadBitmap(String path, ImageView imageView, int width, int height, int defaultImage, Context context)
	{
		if(BitmapWorkerTask.cancelPotentialWork(path, imageView))
		{
			Resources res = context.getResources();
			final BitmapWorkerTask task = new BitmapWorkerTask(imageView, width, height);
			final AsyncDrawable asyncDrawable = new AsyncDrawable(res, BitmapFactory.decodeResource(res, defaultImage), task);
			imageView.setImageDrawable(asyncDrawable);
			task.execute(path);
		}
	}
}
