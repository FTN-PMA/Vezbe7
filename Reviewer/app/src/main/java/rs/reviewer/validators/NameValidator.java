package rs.reviewer.validators;

import android.app.AlertDialog;
import android.widget.Button;
import android.widget.TextView;

import rs.reviewer.R;

public class NameValidator extends TextValidator
{
	private AlertDialog dialog;
	private int maxLength;
	
	public NameValidator(AlertDialog dialog, TextView textView, int maxLength)
	{
		super(textView);
		this.dialog = dialog;
		this.maxLength = maxLength;
	}
	
	@Override
	public void validate(TextView textView, String text)
	{
		final Button okButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
		
		if(text == null || "".equals(text.trim()))
		{
			textView.setError(dialog.getContext().getString(R.string.name_empty_message));
			okButton.setEnabled(false);
		}
		else if(!isAlphanumeric(text))
		{
			textView.setError(dialog.getContext().getString(R.string.name_alphanum_message));
			okButton.setEnabled(false);
		}
		else if(text.length() > maxLength)
		{
			textView.setError(dialog.getContext().getString(R.string.name_maxlength_message).replace("{}", Integer.toString(maxLength)));
			okButton.setEnabled(false);
		}
		else
		{
			textView.setError(null);
			okButton.setEnabled(true);
		}
	}
}
