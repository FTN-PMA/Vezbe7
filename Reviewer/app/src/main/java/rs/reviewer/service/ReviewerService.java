package rs.reviewer.service;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import rs.reviewer.model.Tag;
import rs.reviewer.model.TagToSend;

/**
 * Created by skapl on 09-May-17.
 */
public interface ReviewerService {

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })
    @POST(ServiceUtils.ADD)
    Call<ResponseBody> add(@Body TagToSend tag);
}
