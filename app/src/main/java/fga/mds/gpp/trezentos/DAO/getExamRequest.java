package fga.mds.gpp.trezentos.DAO;

import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class getExamRequest {

    private final String email;
    private final String userClassName;
    private final String url = "https://trezentos-api.herokuapp.com/api/exam/class/user/find";

    public getExamRequest(String email, String userClassName){
        this.email = email;
        this.userClassName = userClassName;

    }

    public String get(){
        OkHttpClient client = new OkHttpClient();

        String urlWithParameters = getUrlWithParameters();

        Request request = new Request.Builder()
                .url(urlWithParameters)
                .build();

        try{
            Response response = client.newCall(request).execute();
            return response.body().string();
        }catch (IOException e){
            e.printStackTrace();
            Log.i("LOG", "IOException in doInBackground method");
        }
        return null;

    }

    private String getUrlWithParameters(){
        HttpUrl.Builder builder = HttpUrl.parse(url).newBuilder();

        builder.addQueryParameter("email", email);
        builder.addQueryParameter("userClassName", userClassName);
        return builder.build().toString();
    }
}