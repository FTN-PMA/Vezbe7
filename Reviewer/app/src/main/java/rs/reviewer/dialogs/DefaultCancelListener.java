package rs.reviewer.dialogs;

import android.content.DialogInterface;

public class DefaultCancelListener implements DialogInterface.OnClickListener
{
	@Override
	public void onClick(DialogInterface dialog, int which)
	{
		dialog.cancel();
	}
}
