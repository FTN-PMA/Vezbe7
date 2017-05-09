package rs.reviewer.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.MapView;

public class ScrollableMapView extends MapView
{
	public ScrollableMapView(Context context)
	{
		super(context);
	}

	public ScrollableMapView(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
	}

	public ScrollableMapView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
	}

	public ScrollableMapView(Context context, GoogleMapOptions options)
	{
		super(context, options);
	}
	
	@SuppressLint("ClickableViewAccessibility")
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
	    int action = ev.getAction();
	    switch (action) {
	    case MotionEvent.ACTION_DOWN:
	        // Disallow ScrollView to intercept touch events.
	        this.getParent().requestDisallowInterceptTouchEvent(true);
	        break;

	    case MotionEvent.ACTION_UP:
	        // Allow ScrollView to intercept touch events.
	        this.getParent().requestDisallowInterceptTouchEvent(false);
	        break;
	    }

	    // Handle MapView's touch events.
	    super.dispatchTouchEvent(ev);
	    return true;
	}
}
