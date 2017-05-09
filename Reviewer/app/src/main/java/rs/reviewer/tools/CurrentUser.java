package rs.reviewer.tools;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import rs.reviewer.R;
import rs.reviewer.activities.SplashScreenActivity;
import rs.reviewer.model.User;

public class CurrentUser
{
	public static boolean exists(Context context)
	{
		return getId(context) != null;
	}
	
	public static String getName(Context context)
	{
		return getProfileData(context, R.string.current_user_name);
	}
	
	public static String getEmail(Context context)
	{
		return getProfileData(context, R.string.current_user_email);
	}
	
	public static String getId(Context context)
	{
		return getProfileData(context, R.string.current_user_id);
	}
	
	private static String getProfileData(Context context, int id)
	{
		return getSharedPreferences(context).getString(context.getString(id), null);
	}
	
	public static User getModel(Context context)
	{
		return User.getByEmail(getEmail(context));
	}
	
	public static void login(User user, Context context)
	{
		getSharedPreferences(context).edit()
		.putString(context.getString(R.string.current_user_id), user.getModelId())
		.putString(context.getString(R.string.current_user_email), user.getEmail())
		.putString(context.getString(R.string.current_user_name), user.getName())
		.commit();
		Log.d("LOGIN",
				"logovo se sa id: " + user.getModelId() +
						" useraname: " + user.getName() +
						" email: " + user.getEmail());
	}
	
	public static void logout(Context context)
	{
		getSharedPreferences(context).edit()
		.putString(context.getString(R.string.current_user_id), null)
		.putString(context.getString(R.string.current_user_email), null)
		.putString(context.getString(R.string.current_user_name), null)
		.commit();
		context.startActivity(new Intent(context, SplashScreenActivity.class)
			.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
			.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
	}
	
	private static SharedPreferences getSharedPreferences(Context context)
	{
		return context.getSharedPreferences(context.getString(R.string.current_user_preferences), Context.MODE_PRIVATE);
	}
}
