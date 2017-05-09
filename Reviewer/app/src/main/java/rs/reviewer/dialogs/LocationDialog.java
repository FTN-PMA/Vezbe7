package rs.reviewer.dialogs;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import rs.reviewer.R;


public class LocationDialog extends AlertDialog.Builder{

	public LocationDialog(Context context) {
		super(context, AlertDialog.THEME_HOLO_LIGHT);
		
		setUpDialog();
	}
	
	private void setUpDialog(){
		setTitle(R.string.oops);
	    setMessage(R.string.location_disabled_message);
	    setCancelable(false);
	    
	    setPositiveButton(R.string.sure, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
         	   getContext().startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
               dialog.dismiss();
            }
        });
	    
	    setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                 dialog.cancel();
            }
        });
	    
	}
	
	public AlertDialog prepareDialog(){
		AlertDialog dialog = create();
		dialog.setCanceledOnTouchOutside(false);
		
		return dialog;
	}
	
}
