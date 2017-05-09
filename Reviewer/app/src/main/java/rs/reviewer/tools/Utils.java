package rs.reviewer.tools;

import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Created by skapl on 26-Jan-16.
 */
public class Utils {

    private static Utils instance = null;

    private Utils() {}

    public static Utils getInstance() {
        if (instance == null) {
            instance = new Utils();
        }
        return instance;
    }

    /**
     */
    public JSONObject requestHttpPost(String url, JSONObject jsonObject) throws IOException, JSONException {
        JSONObject returnJSONObject = null;
        HttpEntity httpEntity = null;
        HttpClient defaultHttpClient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(url);
        StringEntity se = new StringEntity(jsonObject.toString());
        httppost.setEntity(se);
        //httppost.setHeader("Accept", "application/json");
        httppost.setHeader("Content-type", "application/json");
        //httppost.setHeader("Accept", "JSON");
        // Execute HTTP Post Request
        HttpResponse response = defaultHttpClient.execute(httppost);
        httpEntity = response.getEntity();

        String entityResponse = EntityUtils.toString(httpEntity);
        Log.i("TAG", entityResponse +" RESPONSE");
        returnJSONObject = new JSONObject("{"+entityResponse+"}");
        return returnJSONObject;
    }
}
