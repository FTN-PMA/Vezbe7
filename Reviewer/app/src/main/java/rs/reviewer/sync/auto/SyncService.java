package rs.reviewer.sync.auto;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import java.util.Date;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rs.reviewer.MainActivity;
import rs.reviewer.model.TagToSend;
import rs.reviewer.service.ServiceUtils;
import rs.reviewer.sync.tasks.SyncTask;
import rs.reviewer.tools.ReviewerTools;

public class SyncService extends Service {

	public static String RESULT_CODE = "RESULT_CODE";
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		
		Intent ints = new Intent(MainActivity.SYNC_DATA);
		int status = ReviewerTools.getConnectivityStatus(getApplicationContext());
		ints.putExtra(RESULT_CODE, status);
		
		//ima konekcije ka netu skini sta je potrebno i sinhronizuj bazu
		if(status == ReviewerTools.TYPE_WIFI){
//			new SyncTask(getApplicationContext()).execute();

			TagToSend tts = new TagToSend();
			tts.setName("Test tag");
			tts.setDateModified("2017-05-09");
			Call<ResponseBody> call = ServiceUtils.reviewerService.add(tts);
			call.enqueue(new Callback<ResponseBody>() {
				@Override
				public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
					System.out.println("Meesage recieved");
				}

				@Override
				public void onFailure(Call<ResponseBody> call, Throwable t) {
					System.out.println("Error!");
				}
			});


		}
		
		sendBroadcast(ints);
		
		stopSelf();
		
		return START_NOT_STICKY;
	}
	
	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

}
