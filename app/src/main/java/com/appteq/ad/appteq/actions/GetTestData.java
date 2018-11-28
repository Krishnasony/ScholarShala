package com.appteq.ad.appteq.actions;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import com.appteq.ad.appteq.data.DBHandler;
import com.appteq.ad.appteq.model.AnswerModel;
import com.appteq.ad.appteq.model.PassageModel;
import com.appteq.ad.appteq.model.QuestionModel;
import com.appteq.ad.appteq.model.TestModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

import androidx.work.WorkManager;
import androidx.work.WorkStatus;

public class GetTestData extends ViewModel{
    private static final String TAG_OUTPUT = "OUTPUT";
    private DatabaseReference appdb;
    private TestModel testModel;
    private TestModel testMode2;

    private WorkManager mWorkManager;
    private LiveData<List<WorkStatus>> mSavedWorkStatus;

    public GetTestData() {
        mWorkManager = WorkManager.getInstance();

        // This transformation makes sure that whenever the current work Id changes the WorkStatus
        // the UI is listening to changes
        mSavedWorkStatus = mWorkManager.getStatusesByTag(TAG_OUTPUT);
    }

    //--------------------------------------------------------------
    public void fetchTestData(final Context context) {
        testModel = new TestModel();
        appdb = FirebaseDatabase.getInstance().getReference(SharedPrefManager.getInstance(context).getDataMessage());
        appdb.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    String key = postSnapshot.getKey();
                    if (key.equalsIgnoreCase("chapter_id")){
                        testModel.setChapter_id(Integer.parseInt(postSnapshot.getValue().toString()));
                    } else if (key.equalsIgnoreCase("class_id")) {
                        testModel.setClass_id(Integer.parseInt(postSnapshot.getValue().toString()));
                    } else if (key.equalsIgnoreCase("test_start_time")) {
                        testModel.setStart_time(postSnapshot.getValue().toString());
                    }else if (key.equalsIgnoreCase("test_end_time")) {
                        testModel.setEnd_time(postSnapshot.getValue().toString());
                    }else if (key.equalsIgnoreCase("test_instruction")) {
                        testModel.setTest_ins(postSnapshot.getValue().toString());
                    }else if (key.equalsIgnoreCase("test_name")) {
                        testModel.setTest_name(postSnapshot.getValue().toString());
                    }else if (key.equalsIgnoreCase("test_id")) {
                        testModel.setTest_id(Integer.parseInt(postSnapshot.getValue().toString()));
                    } else if (key.equalsIgnoreCase("subject_id")) {
                        testModel.setSubject_id(Integer.parseInt(postSnapshot.getValue().toString()));
                        //Passage
                    } else if (key.equalsIgnoreCase("passage")){
                        ArrayList<PassageModel> passageModelArrayList= new ArrayList<PassageModel>();
                        Iterable<DataSnapshot> passage = postSnapshot.getChildren();
                        for (DataSnapshot pass : passage){
                            PassageModel passageModel = new PassageModel();
                            Toast.makeText(context,""+pass.getChildren(),Toast.LENGTH_SHORT).show();
                            for (DataSnapshot a : pass.getChildren()){
                                String akey = a.getKey();
                                if (akey.equalsIgnoreCase("id")){
                                    passageModel.setPassageId(Integer.parseInt(a.getValue().toString()));
                                }else if (akey.equalsIgnoreCase("passage_text")){
                                    passageModel.setPassageText(a.getValue().toString());
                                }
                            }
                            passageModelArrayList.add(passageModel);
                        }
                        testModel.setPassageModels(passageModelArrayList);
                    }

                    //Question
                    else if (key.equalsIgnoreCase("questions")) {
                        HashMap<Integer,QuestionModel> queslist = new LinkedHashMap<Integer,QuestionModel>();
                        Iterable<DataSnapshot> questions = postSnapshot.getChildren();
                        for (DataSnapshot que : questions) {
                            QuestionModel q = new QuestionModel();
                            Integer ques_id = 0;
                            Integer passage_id = 0;
                            for (DataSnapshot quedata : que.getChildren()) {
                                String que_key = quedata.getKey();
                                if (que_key.equalsIgnoreCase("ques_id")) {
                                    q.setQues_id(Integer.parseInt(quedata.getValue().toString()));
                                    ques_id = Integer.parseInt(quedata.getValue().toString());
                                }
                                else if (que_key.equalsIgnoreCase("text")) {
                                    q.setQuestion(quedata.getValue().toString());
                                } else if (que_key.equalsIgnoreCase("has_mul_ans")) {
                                    if (quedata.getValue().toString().equalsIgnoreCase("0")) {
                                        q.setIs_ml_ans(false);
                                    } else {
                                        q.setIs_ml_ans(true);
                                    }
                                }
                                else if (que_key.equalsIgnoreCase("is_fill")) {
                                    if (quedata.getValue().toString().equalsIgnoreCase("0")) {
                                        q.setIsFillUp(false);
                                    } else {
                                        q.setIsFillUp(true);
                                    }
                                }else if(que_key.equalsIgnoreCase("passage_id")){
                                    q.setPassage_id(Integer.parseInt(quedata.getValue().toString()));
                                }
                                else if (que_key.equalsIgnoreCase("answers")) {
                                    Iterable<DataSnapshot> answers = quedata.getChildren();
                                    ArrayList<AnswerModel> anslists = new ArrayList<AnswerModel>();
                                    for (DataSnapshot ans : answers) {
                                        AnswerModel ansdata = new AnswerModel();
                                        for (DataSnapshot a : ans.getChildren()) {
                                            String akey = a.getKey();
                                            String aval = a.getValue().toString();
                                            if (akey.equalsIgnoreCase("answer_id")) {
                                                ansdata.setId(Integer.parseInt(aval));
                                            } else if (akey.equalsIgnoreCase("text")) {
                                                ansdata.setAnswer(aval);
                                            } else if (akey.equalsIgnoreCase("is_right")) {
                                                if (aval.equalsIgnoreCase("0")) {
                                                    ansdata.setIs_right(false);
                                                } else {
                                                    ansdata.setIs_right(true);
                                                }
                                            } else if (akey.equalsIgnoreCase("has_url")) {
                                                if (aval.equalsIgnoreCase("0")) {
                                                    ansdata.setIs_url(false);
                                                } else {
                                                    ansdata.setIs_url(true);
                                                }
                                            }
                                        }

                                        anslists.add(ansdata);
                                    }
                                    q.setAnswerList(anslists);
                                    //Toast.makeText(context,"question loaded "+q.getQues_id() ,Toast.LENGTH_SHORT).show();
                                }
                            }
                            queslist.put(ques_id,q);
                        }
                        testModel.setQues(queslist);
                    }
                }
                Toast.makeText(context,testModel.getTest_name().toString()+" is loaded",Toast.LENGTH_SHORT).show();
                testModel.setIs_active(0);
                setUpTestData(testModel,context);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });

    }

    private void readTestData(TestModel testModel,Context context){
        Set<Integer> questions = testModel.getQues().keySet();
        Iterator<Integer> ques_ids = questions.iterator();
        while(ques_ids.hasNext()) {
            QuestionModel  ques = testModel.getQues().get(ques_ids.next());
            if(ques.getAnswerList()!=null){
                Toast.makeText(context,ques.getAnswerList().size()+"",Toast.LENGTH_SHORT).show();
            }
            //Toast.makeText(context,ques.getAnswerList().size()+"",Toast.LENGTH_SHORT).show();
        }
    }
       private void setUpTestData(TestModel testModel,Context context){
        DBHandler dbHandler = new DBHandler(context);
        SQLiteDatabase db = dbHandler.getWritableDatabase();
        String sql = "delete from "+DBHandler.TABLE_QUESTIONS;
        dbHandler.deleteData(sql,db);
        sql = "delete from "+DBHandler.TABLE_PASSAGE;
        dbHandler.deleteData(sql,db);
        sql = "delete from "+DBHandler.TABLE_ANSWERS;
        dbHandler.deleteData(sql,db);
        sql = "delete from "+DBHandler.TABLE_TEST+" where "+DBHandler.COLUMN_TEST_ID+"='"+testModel.getTest_id()+"'";
        dbHandler.deleteData(sql,db);
        Log.d("SQL",sql);
        if(dbHandler.insertTest(testModel,db)){
        Set<Integer> questions = testModel.getQues().keySet();
        Iterator<Integer> ques_ids = questions.iterator();
        ArrayList<PassageModel> passage = testModel.getPassageModels();
            if(passage!=null) {
                for (PassageModel pas : passage) {
                    pas.setTestId(testModel.getTest_id());
                    dbHandler.insertPassage(pas, db);
                }
            }
        while(ques_ids.hasNext()) {
            QuestionModel qu = testModel.getQues().get(ques_ids.next());
            qu.setClass_id(testModel.getClass_id());
            qu.setTest_id(testModel.getTest_id());
            qu.setSubject_id(testModel.getSubject_id());
            if (dbHandler.insertQuestions(qu, db)) {
                ArrayList<AnswerModel> answers = qu.getAnswerList();
                if(answers!=null) {
                    for (AnswerModel ans : answers) {
                        ans.setQues_id(qu.getQues_id());
                        dbHandler.insertAnswers(ans, db);
                    }
                }
            }
        }
        }
        testModel.setIs_active(1);
        dbHandler.updateTestData(testModel);
        SharedPrefManager.getInstance(context).saveDataMesssage("no");
        checkStatus(context);
        db.close();
    }

    private void checkStatus(Context context) {
        String status = SharedPrefManager.getInstance(context).getDataMessage();
        if(status.equalsIgnoreCase("no")){
            Intent intent = new Intent(context,AlarmReceiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context,AlarmReceiver.REQUEST_CODE,intent,0);
            AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
            alarmManager.cancel(pendingIntent);
        }
    }

    public TestModel getTestObject(){
        return this.testModel;
    }
}
