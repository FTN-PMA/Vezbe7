package rs.reviewer.tools;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;

import rs.reviewer.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class ImageUtils
{
	public static final String IMAGE_DIRECTORY	= "images";
	
	public static String save(Bitmap bitmapImage, String name, Context context)
	{
		ContextWrapper cw = new ContextWrapper(context);
		File directory = cw.getDir(IMAGE_DIRECTORY, Context.MODE_PRIVATE);
		String path = new File(directory, name + ".png").getAbsolutePath();
		
		saveTo(bitmapImage, path, context);
		
		return path;
	}
	
	public static void saveTo(Bitmap bitmapImage, String path, Context context)
	{
		// Create imageDir
		File mypath = new File(path);
		if(mypath.getParentFile() != null)
		{
			mypath.getParentFile().mkdirs();
		}
		
		FileOutputStream fos = null;
		try
		{
			fos = new FileOutputStream(mypath);
			
			// Use the compress method on the BitMap object to write image to
			// the OutputStream
			bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
			fos.close();
		} 
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public static boolean delete(String path)
	{
		File file = new File(path);
		return file.delete();
	}
	
	public static Bitmap loadImageFromStorage(String path)
	{
		try
		{
			File f = new File(path);
			return BitmapFactory.decodeStream(new FileInputStream(f));
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
			return null;
		}
	}
	
	public static void setImageFromPath(ImageView imageView, String path, int reqWidth, int reqHeight)
	{
		setImageFromPath(imageView, path, reqWidth, reqHeight, null);
	}
	
	public static void setImageFromPath(ImageView imageView, String path, int reqWidth, int reqHeight, Integer defaultImage)
	{
		if(path != null && new File(path).exists())
		{
			Bitmap myBitmap = ImageUtils.decodeSampledBitmapFromFile(path, reqWidth, reqHeight);
			imageView.setImageBitmap(myBitmap);
		}
		else
		{
			if(defaultImage != null)
			{
				imageView.setImageResource(defaultImage);
			}
			else
			{
				imageView.setImageResource(R.drawable.ic_action_picture);
			}
		}
	}
	
	public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId, int reqWidth, int reqHeight)
	{
		// First decode with inJustDecodeBounds=true to check dimensions
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeResource(res, resId, options);
		
		// Calculate inSampleSize
		options.inSampleSize = calculateInSampleSize(options, reqWidth,
				reqHeight);
		
		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeResource(res, resId, options);
	}
	
	public static Bitmap decodeSampledBitmapFromFile(String path, int reqWidth, int reqHeight)
	{
		// First decode with inJustDecodeBounds=true to check dimensions
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(path, options);
		
		// Calculate inSampleSize
		options.inSampleSize = calculateInSampleSize(options, reqWidth,
				reqHeight);
		
		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeFile(path, options);
	}
	
	public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight)
	{
		// Raw height and width of image
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;
		
		if(height > reqHeight || width > reqWidth)
		{
			
			final int halfHeight = height / 2;
			final int halfWidth = width / 2;
			
			// Calculate the largest inSampleSize value that is a power of 2 and
			// keeps both
			// height and width larger than the requested height and width.
			while((halfHeight / inSampleSize) > reqHeight
					&& (halfWidth / inSampleSize) > reqWidth)
			{
				inSampleSize *= 2;
			}
		}
		
		return inSampleSize;
	}
}
