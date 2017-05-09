package rs.reviewer.sync.tasks;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import rs.reviewer.tools.SyncUtils;
import rs.reviewer.tools.Utils;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class SyncTask extends AsyncTask<Void, Void, Void> {

    private Context context;

    private Date dateOfSynchronization;
    private Date dateLastSynchronized;
    private SimpleDateFormat dateFormat;
    private String dateLastSynchronizedString;

    public static String RESULT_CODE = "RESULT_CODE";
    public static String RESULT_COMMENT_ON_REVIEW = "RESULT_COMMENT_ON_REVIEW";
    public static String RESULT_COMMENT_ON_COMMENT = "RESULT_COMMENT_ON_COMMENT";

    public SyncTask(Context context) {
        this.context = context;
    }

    @SuppressLint("SimpleDateFormat")
    @Override
    protected void onPreExecute() {
        dateOfSynchronization = new Date();
        dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
    }

    @Override
    protected Void doInBackground(Void... params) {
        dateLastSynchronized = SyncUtils.getLastSyncronizationDate(context);
        if (dateLastSynchronized == null) // ako nije do sad sinhronizovan
        {
            dateLastSynchronized = new Date(0); // sinhronizuj sve od pocetka epohe
        }
        dateLastSynchronizedString = dateFormat.format(dateLastSynchronized);

        try {
            download();
            upload();
            deleteFromServer();
            persist();

            SyncUtils.setLastSyncronizationDate(context, new Date());
        } catch (IOException e) {
            Log.e("SYNC", "SyncTask", e);
        }
        return null;
    }

	/*
	 * DOWNLOAD DATA FROM SERVER
	 */

    private void download() throws IOException {

    }

//	/*
//	 * UPLOAD DATA TO SERVER
//	 */

    private void upload() throws IOException {
        uploadGroups();
        uploadComments();
        uploadImages();
        uploadReviews();
        uploadReviewObjects();
        uploadGroupToReview();
        uploadGroupToUser();
    }

    // TODO: Dobavi neku grupu i posalji na rest
	private void uploadGroups() throws IOException
	{
		JSONObject obj = new JSONObject();
		try {
			//obj.put("id", 1);
			obj.put("name", "ime");
			obj.put("dateModified", "2016-01-28");
			Utils.getInstance().requestHttpPost("http://<service_ip_adress>:<service_port>/rs.ftn.reviewer.rest/rest/proizvodi/add", obj);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}


	private void uploadComments() throws IOException
	{

	}

	private void uploadImages() throws IOException
	{

	}

	private void uploadReviews() throws IOException
	{

	}

	private void uploadReviewObjects() throws IOException
	{

	}

	private void uploadGroupToReview() throws IOException
	{

	}

	private void uploadGroupToUser() throws IOException
	{

	}

	/*
	 * DELETE FROM SERVER
	 */

	private void deleteFromServer() throws IOException
	{

	}

//	/*
//	 * PERSIST CHANGES TO DATABASE
//	 */

	private void persist()
	{
		persistUsers();
		persistReviewObjects();
		persistReviews();
		persistComments();
		persistGroups();
		persistImages();
		persistGroupToReviews();
		persistGroupToUsers();
	}

	private void persistComments()
	{

	}

	private void persistGroups()
	{

	}

	private void persistImages()
	{

	}

	private void persistReviews()
	{

	}

	private void addTagsToReview()
	{

	}

	private void persistReviewObjects()
	{

	}

	private void addTagsToReviewObject()
	{

	}

	private void persistUsers()
	{

	}

	private void persistGroupToReviews()
	{

	}

	private void persistGroupToUsers()
	{

	}


	@Override
	protected void onPostExecute(Void result) {

	}
}
