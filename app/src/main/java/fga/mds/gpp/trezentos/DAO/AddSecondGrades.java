package fga.mds.gpp.trezentos.DAO;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import fga.mds.gpp.trezentos.Model.Exam;
import fga.mds.gpp.trezentos.Model.UserClass;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class AddSecondGrades extends AsyncTask<String, String, String>{
    private UserClass userClass;
    private Exam exam;
    private final String url = "https://trezentos-api.herokuapp.com/api/exam/second_grades";

    public AddSecondGrades(UserClass userClass, Exam exam) {
        this.userClass = userClass;
        this.exam = exam;
    }

    @Override
    protected String doInBackground(String... params) {
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        OkHttpClient client = new OkHttpClient();
        String newBodySecondGrades = createBody(userClass, exam);
        Log.d("newBody", newBodySecondGrades);

        RequestBody body = RequestBody.create(JSON, newBodySecondGrades);
        Request request = new Request.Builder()
                .url(url)
                .put(body)
                .build();

        try {
            Response response = client.newCall(request).execute();
            return response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
            Log.i("LOG", "IOException in doInBackground method");
        }
        return null;
    }

    public String createBody(UserClass userClass, Exam exam) {

        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("email", exam.getClassOwnerEmail());
            Log.d("JSON", exam.getClassOwnerEmail());
        } catch (JSONException e) {
            Log.d("JSONEXCEPTION", e.toString());
            e.printStackTrace();
        }
        try {
            jsonBody.put("userClassName", userClass.getClassName());
            Log.d("JSONCLASS", userClass.getClassName());
        } catch (JSONException e) {
            Log.d("JSONEXCEPTION", e.toString());
            e.printStackTrace();
        }
        try {
            jsonBody.put("name", exam.getNameExam());
            Log.d("JSON", exam.getNameExam());
        } catch (JSONException e) {
            Log.d("JSONEXCEPTION", e.toString());
            e.printStackTrace();
        }
        try {
            jsonBody.put("secondGrades", exam.getSecondGrades());
            Log.d("JSON", exam.getSecondGrades());
        } catch (JSONException e) {
            Log.d("JSONEXCEPTION", e.toString());
            e.printStackTrace();
        }

        Log.d("JSON", jsonBody.toString());
        return jsonBody.toString();
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
    }
}
