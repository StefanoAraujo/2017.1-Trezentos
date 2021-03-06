package fga.mds.gpp.trezentos.Controller;


import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import fga.mds.gpp.trezentos.DAO.AddFirstGrades;
import fga.mds.gpp.trezentos.DAO.AddSecondGrades;
import fga.mds.gpp.trezentos.DAO.CreateExamPost;
import fga.mds.gpp.trezentos.DAO.getExamRequest;
import fga.mds.gpp.trezentos.Exception.UserClassException;
import fga.mds.gpp.trezentos.Exception.UserException;
import fga.mds.gpp.trezentos.Model.Exam;
import fga.mds.gpp.trezentos.Model.UserClass;

public class UserExamControl{
    private static UserExamControl instance;
    private final Context context;
    private Exam exam;

    private UserExamControl(final Context context){
        this.context = context;
    }

    public static UserExamControl getInstance(final Context context){
        if(instance == null){
            instance = new UserExamControl(context);
        }
        return instance;
    }

    public void validateCreateExam(String examName, String userClassName, String classOwnnerEmail)
            throws UserException{

        try{
            Exam exam = new Exam(examName, userClassName, classOwnnerEmail);

            CreateExamPost createExamPost = new CreateExamPost(exam);
            createExamPost.execute();

        }catch (UserException userException){
            userException.printStackTrace();
        }
    }

    public String validateInformation(String examName, String userClassName, String classOwnerEmail)
            throws UserException{
        String erro;

        try{
            exam = new Exam(examName, userClassName, classOwnerEmail);
            erro = "Sucesso";
            return erro;

        }catch (UserException userException){
            erro = userException.getMessage();
            return erro;
        }
    }

    public String validateAddsFirstGrade(UserClass userClass, Exam exam)
            throws UserClassException, ExecutionException, InterruptedException{

        String serverResponse;
        Log.d("OBJETOS",exam.getFirstGrades());
        Log.d("OBJETOS",userClass.getClassName());
        Log.d("OBJETOS",exam.getClassOwnerEmail());
        Log.d("OBJETOS",exam.getNameExam());

        AddFirstGrades addFirstGrades = new AddFirstGrades(userClass, exam);
        serverResponse = addFirstGrades.execute().get();

        Log.d("JSON", serverResponse);
        return serverResponse;
    }

    public String addSecondGrade(UserClass userClass, Exam exam) throws ExecutionException, InterruptedException {
        String serverResponse;

        AddSecondGrades addSecondGrades = new AddSecondGrades(userClass, exam);
        serverResponse = addSecondGrades.execute().get();

        return serverResponse;
    }

    //GET FROM API
    public ArrayList<Exam> getExamsFromUser(String email, String userClassName) {

        getExamRequest examRequest = new getExamRequest(email, userClassName);

        String serverResponse = "404";
        serverResponse = examRequest.get();
        ArrayList<Exam> userExams = new ArrayList<Exam>();

        try{
            userExams = getArrayList(serverResponse);
        }catch (JSONException e){
            e.printStackTrace();
        }

        return userExams;
    }

    private ArrayList<Exam> getArrayList(String serverResponse) throws JSONException{
        JSONArray array = null;

        try{
            array = new JSONArray(serverResponse);
        }catch (JSONException e){
            e.printStackTrace();
        }

        ArrayList<Exam> userExams = new ArrayList<>();

        for(int i = 0; i < array.length(); i++){
            Exam exam = getUserExamFromJson(array.getJSONObject(i));
            userExams.add(exam);
        }

        return userExams;
    }

    private Exam getUserExamFromJson(JSONObject jsonObject) {

        Exam exam = new Exam();

        try{
            exam.setNameExam(jsonObject.getString("name"));
            exam.setUserClassName(jsonObject.getString("userClassName"));
            exam.setClassOwnerEmail(jsonObject.getString("classOwnerEmail"));
            exam.setFirstGrades(jsonObject.getString("firstGrades"));

        }catch (JSONException | UserException e){
            e.printStackTrace();
        }

        return exam;
    }
}
