package rs.reviewer.validators;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.TextView;

import rs.reviewer.exceptions.ValidationException;

public abstract class TextValidator implements TextWatcher
{
    private final TextView textView;

    public TextValidator(TextView textView)
    {
        this.textView = textView;
    }

    protected abstract void validate(TextView textView, String text);
    
    public void validate()
    {
    	validate(textView, textView.getText().toString());
    }

    @Override
    final public void afterTextChanged(Editable s)
    {
    	try
    	{
    		validate();
    	}
    	catch(ValidationException ex)
    	{} // ignorisemo exception
    }
    
    protected boolean isAlphanumeric(String str)
    {
    	return str.matches("[a-zA-Z0-9 ]*");
    }
    
    protected boolean isAlphanumericWithInterpunction(String str)
    {
    	return str.matches("[a-zA-Z0-9 _.,!\"'/;:@#$%&()*<>?]*");
    }
    
    @Override
    final public void beforeTextChanged(CharSequence s, int start, int count, int after) { /* Don't care */ }

    @Override
    final public void onTextChanged(CharSequence s, int start, int before, int count) { /* Don't care */ }
}