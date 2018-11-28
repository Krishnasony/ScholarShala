package com.appteq.ad.appteq;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.appteq.ad.appteq.actions.APP_CONSTANT;
import com.appteq.ad.appteq.actions.App_Functions;
import com.appteq.ad.appteq.actions.QuizActivity;
import com.appteq.ad.appteq.actions.RequestAdapt;
import com.appteq.ad.appteq.actions.SharedPrefManager;
import com.appteq.ad.appteq.model.AnswerModel;
import com.appteq.ad.appteq.model.PassageModel;
import com.appteq.ad.appteq.model.QuestionModel;
import com.appteq.ad.appteq.model.UserModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

public class InstructionsActivity extends AppCompatActivity {
    TextView time;
    Handler mHandler = new Handler();
    long timeLeft;
    boolean run = true;
    Thread t1 = null;
    int checkedIndex;
    int test_id=0;
    int class_id = 0;
    String testremaintime,testtimestr;
    private String testsubmitserverurl = APP_CONSTANT.webservice_url+"submittestinfo";
    private HashMap<Integer, QuestionModel> questionList = new LinkedHashMap<Integer, QuestionModel>();
    private Toolbar test_toolbar;
    long timeLeftInMillis;
    private TextView questions, q_id, ques_ml_option,testtitle,testtime , fill_up_que,subjectname,marks,totalquestions,duration;
    private RadioButton radioButton1;
    private RadioButton radioButton2;
    private RadioButton radioButton3;
    private RadioButton radioButton4;
    private RadioButton radioButton5;
    private RadioButton radioButton6;
    private TextView testname;
    private TextView instructiontextview;
    private LinearLayout bottomnavi;
    private App_Functions func;
    private ImageView subject_logo;
    public TextView passageTextView;
    private LinearLayout passageScrollView;
    private CardView instcard,fcard;
    private CheckBox checkbox1;
    private CheckBox checkbox2;
    private CheckBox checkbox3;
    private CheckBox checkbox4;
    private CheckBox checkbox5;
    private CheckBox checkbox6;
    private TextView textView1;
    private TextView textView2;
    private TextView textView3;
    private TextView textView4;
    private TextView textView6;
    private TextView textView5;
    private EditText edittext1;
    private EditText edittext2;
    private EditText edittext3;
    private EditText edittext4;
    private EditText edittext5;
    private EditText edittext6;
    private ImageView imageView1;
    private ImageView imageView2;
    private ImageView imageView3;
    private ImageView imageView4;
    private ImageView imageView5;
    private ImageView imageView6;
    private Toolbar toolbar;
    private Button previous;
    public static Button next;
    private Button submit;
    private volatile ScrollView testview;
    private Button save;
    private RadioGroup radioGroup;
    private volatile LinearLayout instructionview;
    private String manswer;
    private int mQuestionNo = 0;
    private int mScore = 0;
    private int mcolumns;
    private ArrayList<Integer> ques_keys;
    private ArrayList<RadioButton> rlist = new ArrayList<RadioButton>();
    private ArrayList<CheckBox> clist = new ArrayList<CheckBox>();
    private ArrayList<ImageView> iList = new ArrayList<ImageView>();
    private ArrayList<TextView> tList = new ArrayList<TextView>();
    private ArrayList<EditText> eList = new ArrayList<EditText>();
    private QuizActivity qa;
    private boolean isCanceled = false;
    private CountDownTimer countDownTimer = null;
    private SharedPreferences prefs;
    private static final String SHARED_PREF_NAME = "Testdatafile";
    private String user_id;
    private String user_token,subject_name;
    private TextView timeRemaining;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instructions);
        instructionview = (LinearLayout) findViewById(R.id.instruction);
        func = new App_Functions(this);
        testview = (ScrollView) findViewById(R.id.testview);
        questions = (TextView) findViewById(R.id.question_text_view);
        q_id = (TextView) findViewById(R.id.question_id);
        ques_ml_option = (TextView) findViewById(R.id.ques_ml_option);
        fill_up_que = (TextView) findViewById(R.id.fill_up_que);
        //testtitle= (TextView) findViewById(R.id.testname);
        testtime = (TextView) findViewById(R.id.testtime);
        testname = (TextView) findViewById(R.id.itextn);
        instructiontextview = (TextView) findViewById(R.id.itextw);
        bottomnavi = (LinearLayout) findViewById(R.id.bottomnavi);
        bottomnavi.setVisibility(View.GONE);
        passageTextView = (TextView)findViewById(R.id.passage_text_view);
        subject_logo = (ImageView) findViewById(R.id.subject_logo);
        subjectname = (TextView)findViewById(R.id.subjectname);
        Bundle bundle = getIntent().getBundleExtra("userdata");
        int test_id = Integer.parseInt(bundle.getString("test_id"));
        duration = findViewById(R.id.testduration);
        totalquestions = findViewById(R.id.totalquestions);
        marks = findViewById(R.id.totalmarks);
        instcard = findViewById(R.id.desccard);
        fcard = findViewById(R.id.testfboard);
        toolbar = (Toolbar) findViewById(R.id.test_tool_bar);
        toolbar.setVisibility(View.GONE);
        user_id = bundle.getString("user_id");
        user_token = bundle.getString("usertoken");
        subject_name = bundle.getString("subject_name");

        toolbar.setTitleTextColor(Color.WHITE);
        if (subject_name.equalsIgnoreCase("mathematics")){
            subject_logo.setImageResource(R.drawable.maths_icon);
        }
        if (subject_name.equalsIgnoreCase("social science")){
            subject_logo.setImageResource(R.drawable.social_icon);
        }
        if (subject_name.equalsIgnoreCase("science")){
            subject_logo.setImageResource(R.drawable.science_icon);
        }
        if (subject_name.equalsIgnoreCase("english")){
            subject_logo.setImageResource(R.drawable.english_icon);
        }
        if (subject_name.equalsIgnoreCase("hindi")){
            subject_logo.setImageResource(R.drawable.hindi_icon);
        }
        if (subject_name.equalsIgnoreCase("physics")){
            subject_logo.setImageResource(R.drawable.physics_icon);
        }
        if (subject_name.equalsIgnoreCase("chemistry")){
            subject_logo.setImageResource(R.drawable.chemistry_icon);
        }
        if (subject_name.equalsIgnoreCase("biology")){
            subject_logo.setImageResource(R.drawable.biology_icon);
        }
        /********************* Radio ***************************/
        radioButton1 = (RadioButton) findViewById(R.id.radioButton1);
        rlist.add(radioButton1);
        radioButton2 = (RadioButton) findViewById(R.id.radioButton2);
        rlist.add(radioButton2);
        radioButton3 = (RadioButton) findViewById(R.id.radioButton3);
        rlist.add(radioButton3);
        radioButton4 = (RadioButton) findViewById(R.id.radioButton4);
        rlist.add(radioButton4);
        radioButton5 = (RadioButton) findViewById(R.id.radioButton5);
        rlist.add(radioButton5);
        /*radioButton6 = (RadioButton) findViewById(R.id.radioButton5);
        rlist.add(radioButton6);*/

        /*************************** Checkbox **************************/
        checkbox1 = (CheckBox) findViewById(R.id.checkbox1);
        clist.add(checkbox1);
        checkbox2 = (CheckBox) findViewById(R.id.checkbox2);
        clist.add(checkbox2);
        checkbox3 = (CheckBox) findViewById(R.id.checkbox3);
        clist.add(checkbox3);
        checkbox4 = (CheckBox) findViewById(R.id.checkbox4);
        clist.add(checkbox4);
        checkbox5 = (CheckBox) findViewById(R.id.checkbox5);
        clist.add(checkbox5);
        /*checkbox6 = (CheckBox) findViewById(R.id.checkbox5);
        clist.add(checkbox6);*/

        /*************************** Textview **************************/
        textView1 = (TextView) findViewById(R.id.textView1);
        tList.add(textView1);
        textView2 = (TextView) findViewById(R.id.textView2);
        tList.add(textView2);
        textView3 = (TextView) findViewById(R.id.textView3);
        tList.add(textView3);
        textView4 = (TextView) findViewById(R.id.textView4);
        tList.add(textView4);
        textView5 = (TextView) findViewById(R.id.textView5);
        tList.add(textView5);
        /*textView6 = (TextView) findViewById(R.id.textView5);
        tList.add(textView6);*/

        /************* Edit Test ******************************/
        edittext1 = (EditText) findViewById(R.id.fillUp1);
        eList.add(edittext1);
        edittext2 = (EditText) findViewById(R.id.fillUp2);
        eList.add(edittext2);
        edittext3 = (EditText) findViewById(R.id.fillUp3);
        eList.add(edittext3);
        edittext4 = (EditText) findViewById(R.id.fillUp4);
        eList.add(edittext4);
        edittext5 = (EditText) findViewById(R.id.fillUp5);
        eList.add(edittext5);
        /*edittext6 = (EditText) findViewById(R.id.fillUp5);
        eList.add(edittext6);*/
     /*   edittext1 = (EditText) findViewById(R.id.fillUp1);
        eList.add(edittext1);*/

        /************************Images*********************************/
        imageView1 = (ImageView) findViewById(R.id.imageView1);
        iList.add(imageView1);
        imageView2 = (ImageView) findViewById(R.id.imageView2);
        iList.add(imageView2);
        imageView3 = (ImageView) findViewById(R.id.imageView3);
        iList.add(imageView3);
        imageView4 = (ImageView) findViewById(R.id.imageView4);
        iList.add(imageView4);
        imageView5 = (ImageView) findViewById(R.id.imageView5);
        iList.add(imageView5);
        /*imageView6 = (ImageView) findViewById(R.id.imageView5);
        iList.add(imageView6)*/;
        passageScrollView = (LinearLayout)findViewById(R.id.passagelayout);

        previous = (Button) findViewById(R.id.previous);
        submit = (Button) findViewById(R.id.submit);
        next = (Button) findViewById(R.id.next);
        if(test_id==0){
            startCountDown(false);
        }else{
            qa = new QuizActivity(passageScrollView,passageTextView,rlist, clist, iList, tList,eList,questions, q_id, ques_ml_option,fill_up_que,this,test_id);
            testremaintime = QuizActivity.testModel.getStart_time();
            totalquestions.setText(QuizActivity.testModel.getQues().size()+"");
            marks.setText(QuizActivity.testModel.getQues().size()+"");
            testname.setText(QuizActivity.testModel.getTest_name());
            testtimestr = QuizActivity.testModel.getEnd_time();
            ques_keys = qa.getQuestionKeys();
            for(int i=0;i<rlist.size();i++){
                final int k = i;
                rlist.get(i).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        for(int j=0;j<rlist.size();j++){
                            if(k==j){
                                rlist.get(j).setChecked(true);
                            }else{
                                rlist.get(j).setChecked(false);
                            }
                        }
                    }
                });
            }
            qa.updateQuestion(mQuestionNo);

            previous.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mQuestionNo != 0) {
                        mQuestionNo--;
                        if(fill_up_que.getText() == "yes"){
                            qa.clearOptions();
                        }else{
                            if(ques_ml_option.getText() == "no") {
                                qa.clearOptions();
                            } else {
                                qa.clearOptions();
                            }
                        }
                        qa.updateQuestion(mQuestionNo);
                        qa.LoadPreferences(mQuestionNo);
                    }


                }
            });

            next.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String sval = "";
                    if (mQuestionNo < (ques_keys.size() - 1)) {
                        ArrayList<AnswerModel> answers = QuizActivity.questionList.get(ques_keys.get(mQuestionNo)).getAnswerList();
                        Toast.makeText(InstructionsActivity.this,fill_up_que.getText()+"",Toast.LENGTH_SHORT).show();
                        if(fill_up_que.getText()== "yes"){
                            for (int i = 0; i < answers.size(); i++){
                                EditText tmp_c = eList.get(i);
                                if(tmp_c.getText().toString()!=""){
                                    sval += tmp_c.getText().toString()+",";
                                }else{
                                    sval += ""+",";
                                }
                            }
                            int index = sval.lastIndexOf(",");
                            if (index > -1) {
                                sval = sval.substring(0, index);
                            }
                        }
                        else{
                            if (ques_ml_option.getText() == "no" ) {
                                for (int i = 0; i < answers.size(); i++) {
                                    RadioButton tmp_c = rlist.get(i);
                                    if (tmp_c.isChecked()) {
                                        sval = Integer.toString(i);
                                        break;
                                    }
                                }
                            } else {
                                for (int i = 0; i < answers.size(); i++) {
                                    CheckBox tmp_c = (CheckBox) clist.get(i);
                                    if (tmp_c.isChecked()) {
                                        sval += i + ",";
                                    }
                                }
                                int index = sval.lastIndexOf(",");
                                if (index > -1) {
                                    sval = sval.substring(0, index);
                                }
                            }
                        }

                        qa.SavePreferences(q_id.getText().toString(), sval);
                        mQuestionNo++;
                        if (ques_ml_option.getText() == "no") {
                            qa.clearOptions();
                        } else {
                            qa.clearOptions();
                        }
                        if(fill_up_que.getText() == "yes"){
                            qa.clearOptions();
                        }
                        qa.updateQuestion(mQuestionNo);
                        qa.LoadPreferences(mQuestionNo);
                    }
                }
            });

            submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String sval = "";
                    qa.SavePreferences(q_id.getText().toString(), sval);
                    stopTest();

                }
            });
            //startTest();
            startCountDown(false);
        }

    }

    private void clearSharedPrefrences() {
        //prefs.edit().clear().commit();

    }

    private void startTest() {
        instructionview.setVisibility(View.GONE);
        fcard.setVisibility(View.GONE);
        instcard.setVisibility(View.GONE);
        bottomnavi.setVisibility(View.VISIBLE);
        toolbar.setVisibility(View.VISIBLE);
        testview.setVisibility(View.VISIBLE);
        startCountDown(true);
    }
    private void stopTest(){
         processTestData();
       /* Intent intent = new Intent(InstructionsActivity.this, ScoreActivity.class);
        startActivity(intent);*/
    }
    private void startCountDown(final boolean quiz_started) {

        SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        Date d1 = null;
        Date d2 = new Date();
        try {
            if (quiz_started) {
                d1 = format.parse(testtimestr);
                Toast.makeText(this,testtimestr,Toast.LENGTH_SHORT).show();
            } else {
                d1 = format.parse(testremaintime);
            }
            if (d1.getTime() > d2.getTime()) {
                long COUNTDOWN_IN_MILLIS = d1.getTime() - d2.getTime();
                Toast.makeText(this,COUNTDOWN_IN_MILLIS+"",Toast.LENGTH_SHORT).show();
                countDownTimer = new CountDownTimer(COUNTDOWN_IN_MILLIS, 1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        timeLeftInMillis = millisUntilFinished;
                        updateCountDownText(quiz_started);
                    }

                    @Override
                    public void onFinish() {
                        timeLeftInMillis = 0;
                        updateCountDownText(quiz_started);

                    }
                }.start();
            }else{
                if(quiz_started){
                    testview.setVisibility(View.GONE);
                    toolbar.setVisibility(View.GONE);
                    instructionview.setVisibility(View.VISIBLE);
                    testtime = (TextView)findViewById(R.id.test_remain_time);
                    testtime.setTextColor(getResources().getColor(R.color.colorwhite));
                    ((TextView)findViewById(R.id.testtimienoti)).setText("");
                    testname.setVisibility(View.GONE);
                    testname.setText("");
                    instructiontextview.setText(getResources().getString(R.string.testtimeerr));
                    instructiontextview.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                    bottomnavi.setVisibility(View.GONE);
                    testtime.setText("");
                }else{
                    startTest();
                }
            }

        } catch (Exception e) {

        }
    }

    private void updateCountDownText(boolean quiz_started) {
        int hour = (int) (timeLeftInMillis / 1000) /60 / 60;
        int minutes = (int) ((timeLeftInMillis / 1000) / 60) % 60;
        int seconds = (int) (timeLeftInMillis / 1000) % 60;

        String timeFormatted = String.format(Locale.getDefault(), "%02d:%02d:%02d",hour, minutes, seconds);
        if(quiz_started){
            testtime = (TextView)findViewById(R.id.testtime);
        }else{
            testtime = (TextView)findViewById(R.id.test_remain_time);
            //testname.setText(QuizActivity.testModel.getTest_name());
            instructiontextview.setText(Html.fromHtml(QuizActivity.testModel.getTest_ins()));
        }

        testtime.setText(timeFormatted);

        if (timeLeftInMillis < 10000) {
            testtime.setTextColor(Color.RED);
        } else {
            testtime.setTextColor(Color.BLACK);
        }
        if(timeLeftInMillis==0) {
            if (quiz_started) {
                stopTest();
            } else {
                startTest();
            }
        }
    }
    private void processTestData(){
        toolbar.setVisibility(View.GONE);
        testview.setVisibility(View.GONE);
        instructionview.setVisibility(View.VISIBLE);
        if(countDownTimer!=null){
            countDownTimer.cancel();
        }
        qa.calculate_score();
        func.setProgressMessage(getResources().getString(R.string.submittest));
        func.showProgressDialog();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, testsubmitserverurl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                boolean is_success = false;
                try {
                    Toast.makeText(InstructionsActivity.this,response,Toast.LENGTH_LONG).show();
                    JSONObject jsonObject = new JSONObject(response);
                    Iterator<String> keys = jsonObject.keys();
                    Cursor userc = null;
                    Toast.makeText(getApplicationContext(),jsonObject.toString(),Toast.LENGTH_SHORT).show();
                    while(keys.hasNext()){
                        String key = keys.next();
                        if(key.equalsIgnoreCase("err") || key.equalsIgnoreCase("errs")){
                            func.showErrors(InstructionsActivity.this,jsonObject.getString(key),"Error message");
                            func.dismissProgressDialog();
                            break;
                        }else if(key.equalsIgnoreCase("success")){
                            is_success = true;
                        }
                    }
                    func.dismissProgressDialog();
                    if(is_success){
                        func.dismissProgressDialog();
                        QuizActivity.dbhandler.insertScore(QuizActivity.testModel);
                        QuizActivity.testModel.setIs_active(1);
                        QuizActivity.dbhandler.updateTestData(QuizActivity.testModel);
                        testtime = (TextView)findViewById(R.id.test_remain_time);
                        ((TextView)findViewById(R.id.testtimienoti)).setText("");
                        testname.setText("");
                        testname.setVisibility(View.GONE);
                        func.showErrors(InstructionsActivity.this,getResources().getString(R.string.successtest),"Test Successful");
                        instructiontextview.setText("");
                        instructiontextview.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                        bottomnavi.setVisibility(View.GONE);
                        testtime.setText("");

                        startActivity(new Intent(InstructionsActivity.this,SubjectActivity.class));

                    }
                } catch (JSONException e) {
                    func.dismissProgressDialog();
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                func.dismissProgressDialog();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("test_id",QuizActivity.testModel.getTest_id()+"");
                params.put("test_score",QuizActivity.testModel.getScore()+"");
                params.put("user_id",user_id+"");
                params.put("chapter_id",QuizActivity.testModel.getChapter_id()+"");
                params.put("logintoken",user_token);
                params.put("subject_id",QuizActivity.testModel.getSubject_id()+"");
                params.put("totalquestion",QuizActivity.testModel.getQues().size()+"");
                params.put("testdate",QuizActivity.testModel.getStart_time());
                params.put("classid",QuizActivity.testModel.getClass_id()+"");
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));
        RequestAdapt.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }
}
