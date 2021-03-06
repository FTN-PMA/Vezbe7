package rs.reviewer.service;

import java.util.concurrent.TimeUnit;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServiceUtils {

	//EXAMPLE: http://192.168.43.73:8080/rs.ftn.reviewer.rest/rest/proizvodi/
    public static final String SERVICE_API_PATH = "http://<service_ip_adress>:<service_port>/rs.ftn.reviewer.rest/rest/proizvodi/";
    public static final String ADD = "add";

    /*
    * Ovo ce nam sluziti za debug, da vidimo da li zahtevi i odgovoru idu
    * odnosno dolaze i kako izgeldaju.
    * */
    public static OkHttpClient test(){
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(120, TimeUnit.SECONDS)
                .readTimeout(120, TimeUnit.SECONDS)
                .writeTimeout(120, TimeUnit.SECONDS)
                .addInterceptor(interceptor).build();

        return client;
    }

    /*
     * Prvo je potrebno da definisemo retrofit instancu preko koje ce komunikacija ici
     * */
    public static Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(SERVICE_API_PATH)
            .addConverterFactory(GsonConverterFactory.create())
            .client(test())
            .build();

    /*
     * Definisemo konkretnu instancu servisa na intnerntu sa kojim
     * vrsimo komunikaciju
     * */
    public static ReviewerService reviewerService = retrofit.create(ReviewerService.class);
}
