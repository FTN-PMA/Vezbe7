package rs.reviewer.fragments.reviews;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import rs.reviewer.R;
import rs.reviewer.loaders.ModelLoaderCallbacks;
import rs.reviewer.model.Comment;
import rs.reviewer.model.Review;
import rs.reviewer.tools.CurrentUser;
import rs.reviewer.validators.TextValidator;

import java.util.List;

public class CommentsListFragment extends AbstractCommentsListFragment
{
	public CommentsListFragment()
	{}
	
	// prima id reviewa i dobavlja komentare za njega
	public CommentsListFragment(String itemId)
	{
		super(itemId, R.menu.activity_itemdetail);
	}
	
	@Override
	protected ModelLoaderCallbacks<Comment> createLoaderCallbacks()
	{
		return new ModelLoaderCallbacks<Comment>(getActivity(), Comment.class, adapter)
		{
			@Override
			protected List<Comment> getData()
			{	
				Log.d("KOMENTARI REVIEWA", "review_id=" + getReview().getModelId() + "komentari=" + getReview().getComments());
				return getReview().getComments();
			}
		};
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		// handle item selection
		switch (item.getItemId()) {
			case R.id.add_item:
				//Toast.makeText(getActivity(), "Add Comment item pressed", Toast.LENGTH_LONG).show();
				commentDialog();
				return true;
		    default:
		    	return super.onOptionsItemSelected(item);
		}
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		//postaviti da fragment ima meni
		setHasOptionsMenu(true);
	}
	
	@SuppressLint("InflateParams")
	private void commentDialog(){
		
		LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
		final View promptView = layoutInflater.inflate(R.layout.comment_dialog, null);
		
		final EditText editText = (EditText) promptView.findViewById(R.id.edittext);
		
		final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		
		builder.setView(promptView)
			.setCancelable(false)
			.setPositiveButton(R.string.comment, new OnClickListener()
			{	
				@Override
				public void onClick(DialogInterface dialog, int which)
				{
					
					String comm = editText.getText().toString();
					
					Review review = getReview();
					
					//create object
					Comment newComment = new Comment(comm, CurrentUser.getModel(getActivity()), review);
					
					// save to database
					newComment.saveOrThrow();
					
					//Log.d("COMMENT","commnet  "+newComment.getReview().getName());
					Toast.makeText(getActivity(), R.string.created, Toast.LENGTH_SHORT).show();
					
					//update original list
					//((CommentsAdapter) myAdapter).getItemsOriginal().add(newComment);
					
					//notify adapter
					//myAdapter.notifyDataSetChanged();
				}
			})
			.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener()
			{
				public void onClick(DialogInterface dialog, int id)
				{
					dialog.cancel();
				}
			});
			
			final AlertDialog dialog = builder.create();

			
			TextValidator nameValidator = new TextValidator(editText)
			{
				int maxLength = 200;
				
				@Override
				public void validate(TextView textView, String text)
				{
					final Button okButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
					
					if(text == null || "".equals(text.trim()))
					{
						textView.setError(getActivity().getString(R.string.comment_empty_message));
						okButton.setEnabled(false);
					}
					else if(!isAlphanumericWithInterpunction(text))
					{
						textView.setError(getActivity().getString(R.string.comment_alphanum_message));
						okButton.setEnabled(false);
					}
					else if(text.length() > 200)
					{
						textView.setError(getActivity().getString(R.string.comment_maxlength_message).replace("{}", Integer.toString(maxLength)));
						okButton.setEnabled(false);
					}
					else
					{
						textView.setError(null);
						okButton.setEnabled(true);
					}
				}
			};
			editText.addTextChangedListener(nameValidator);
			
			dialog.show();
			dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
			
			
			editText.addTextChangedListener(new TextWatcher()
			{
			    @Override
			    public void onTextChanged(CharSequence s, int start, int before,int count)
			    {
			    }

			    @Override
			    public void beforeTextChanged(CharSequence s, int start, int count,int after)
			    {
			    }

			    @Override
			    public void afterTextChanged(Editable s)
			    {
			        

			    }
			});
			
			dialog.show();
			dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
	}
	
	@Override
	public void onResume() {
		super.onResume();
		setHasOptionsMenu(true);
	}
	
	private Review getReview()
	{
		return Review.getByModelId(Review.class, getArguments().getString(RELATED_ID));
	}
	
}
