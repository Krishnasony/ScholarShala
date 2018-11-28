package com.appteq.ad.appteq.actions;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.View;
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

import com.appteq.ad.appteq.InstructionsActivity;
import com.appteq.ad.appteq.R;
import com.appteq.ad.appteq.data.DBHandler;
import com.appteq.ad.appteq.model.AnswerModel;
import com.appteq.ad.appteq.model.PassageModel;
import com.appteq.ad.appteq.model.QuestionModel;
import com.appteq.ad.appteq.model.TestModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class QuizActivity extends InstructionsActivity {


    public static final String MyPREFERENCES = "MyPreferences";
    public static final String Name = "savedState";
    public static HashMap<Integer, PassageModel> passageModelHashMap = new LinkedHashMap<Integer, PassageModel>();
    int checkedIndex;
    public static HashMap<Integer, QuestionModel> questionList = new LinkedHashMap<Integer, QuestionModel>();
    private TextView questions, q_id, ques_ml_option , fill_up_que;

    private GetTestData fbasedata;
    public static TestModel testModel;
    public static DBHandler dbhandler;
    /* private ArrayList<String> rbid = new ArrayList<String>(Arrays.asList("radioButton1","radioButton2","radioButton3",
                                                                               "radioButton4","radioButton5"));
   private ArrayList<String> cbid = new ArrayList<String>(Arrays.asList("checkBox1","checkBox2","checkBox3",
                                                                                   "checkBox4","checkBox5"));*/
    private Button previous;
    private Button next;
    private Button submit;
    private Button save;
    private RadioGroup radioGroup;
    private String manswer;
    private int mScore = 0;
    private int mcolumns;
    private TextView passageTextView;
    private LinearLayout passageScrollView;
    private ArrayList<Integer> pass_keys;
    private ArrayList<Integer> ques_keys;
    private ArrayList<RadioButton> rlist;
    private ArrayList<CheckBox> clist;
    private ArrayList<ImageView> iList;
    private ArrayList<TextView> tList;
    private ArrayList<EditText> eList;
    private AppCompatActivity apc;
    private int test_id;
    private int class_id;



    public QuizActivity(LinearLayout passageScrollView, TextView passageTextView, ArrayList<RadioButton> rlist, ArrayList<CheckBox> clist, ArrayList<ImageView> ilist, ArrayList<TextView> tlist, ArrayList<EditText> eList , TextView questions, TextView q_id, TextView ques_ml_option, TextView fill_up_que, AppCompatActivity
            apc, int test_id) {
        this.rlist = rlist;
        this.clist = clist;
        this.iList = ilist;
        this.tList = tlist;
        this.eList = eList;
        this.questions = questions;
        this.q_id = q_id;
        this.ques_ml_option = ques_ml_option;
        this.fill_up_que = fill_up_que;
        this.apc = apc;
        this.test_id = test_id;
        this.dbhandler = new DBHandler(apc);
        loadTestData();
        this.ques_keys = new ArrayList<Integer>(questionList.keySet());
        this.pass_keys = new ArrayList<Integer>(passageModelHashMap.keySet());
        this.passageTextView = passageTextView;
        this.passageScrollView = passageScrollView;

    }

    private void hideOption(QuestionModel tmp_q){
        for(int i=0;i<clist.size();i++){
            if(tmp_q.isIsFillUp()){
                rlist.get(i).setVisibility(View.GONE);
                clist.get(i).setVisibility(View.GONE);
                eList.get(i).setVisibility(View.VISIBLE);
                tList.get(i).setVisibility(View.GONE);
                iList.get(i).setVisibility(View.GONE);
            }else{
                //tList.get(i).setVisibility(View.VISIBLE);
                if(tmp_q.isIs_ml_ans()){
                    rlist.get(i).setVisibility(View.GONE);
                    clist.get(i).setVisibility(View.VISIBLE);
                }else{
                    clist.get(i).setVisibility(View.GONE);
                    rlist.get(i).setVisibility(View.VISIBLE);
                }
                eList.get(i).setVisibility(View.GONE);
            }
        }
    }



    public void updateQuestion(int mQuestionNo) {
        QuestionModel tmp_q = questionList.get(ques_keys.get(mQuestionNo));
        PassageModel passageModel = new PassageModel();
        boolean is_fillup = false;
        mcolumns = tmp_q.getAnswerList().size();
        questions.setText("Q"+(mQuestionNo+1)+". "+Html.fromHtml(tmp_q.getQuestion()));
      /*  if((mQuestionNo+1) == testModel.getQues().size()){
            InstructionsActivity.next.setVisibility(View.INVISIBLE);
        }else{
            InstructionsActivity.next.setVisibility(View.VISIBLE);
        }*/
        //Toast.makeText(apc,tmp_q.getIsFillUp()+"",Toast.LENGTH_SHORT).show();
        q_id.setText(Integer.toString(tmp_q.getQues_id()));
        if(QuizActivity.testModel.isHaspassage()) {
            if (tmp_q.getPassage_id() != 0) {
                PassageModel tmp_pmap = testModel.getPmap().get(tmp_q.getPassage_id());
                passageTextView.setText(Html.fromHtml(tmp_pmap.getPassageText()));
                passageScrollView.setVisibility(View.VISIBLE);
                passageTextView.setVisibility(View.VISIBLE);
            } else {
                passageScrollView.setVisibility(View.GONE);
                passageTextView.setText("");
                passageTextView.setVisibility(View.GONE);


            }
        }
        if(tmp_q.isIsFillUp()){
            hideOption(tmp_q);
            fill_up_que.setText("yes");
        }else{
            if (tmp_q.isIs_ml_ans()) {
                ques_ml_option.setText("yes");
                fill_up_que.setText("no");
                hideOption(tmp_q);
            } else{
                ques_ml_option.setText("no");
                fill_up_que.setText("no");
                hideOption(tmp_q);
            }
        }
        switch (mcolumns) {
            case 2:
                addOptions(mcolumns, rlist, tmp_q.getAnswerList(), tmp_q.isIs_ml_ans() ,  tmp_q.isIsFillUp());
                break;

            case 3:
                addOptions(mcolumns, rlist, tmp_q.getAnswerList(), tmp_q.isIs_ml_ans() , tmp_q.isIsFillUp());
                break;

            case 4:
                addOptions(mcolumns, rlist, tmp_q.getAnswerList(), tmp_q.isIs_ml_ans() , tmp_q.isIsFillUp());
                break;

            case 5:
                addOptions(mcolumns, rlist, tmp_q.getAnswerList(), tmp_q.isIs_ml_ans() , tmp_q.isIsFillUp());
                break;
        }
    }

    public void addOptions(int mcolumns, ArrayList<RadioButton> rlist, ArrayList<AnswerModel> answerList, boolean is_ml_ans , boolean isFillUp) {
        if (mcolumns > 0) {
            int i = 0;
            int img_c = 0;
            for (; i < mcolumns; i++) {
                ImageView im = iList.get(i);
                TextView tm = tList.get(i);
                AnswerModel am = answerList.get(i);
                if(isFillUp){
                    //CheckBox r = clist.get(i);
                }else{
                    if (is_ml_ans) {
                        CheckBox r = clist.get(i);
                        r.setText("");
                        r.setVisibility(View.VISIBLE);
                        if (am.isIs_url()) {
                            im.setVisibility(View.VISIBLE);
                            tm.setVisibility(View.GONE);
                            tm.setText("");
                            showImage(am, im);
                        } else {
                            tm.setVisibility(View.VISIBLE);
                            tm.setText(am.getAnswer());
                            tm.setLines(4);
                            im.setVisibility(View.GONE);
                        }
                    } else {
                        RadioButton r = rlist.get(i);
                        r.setText("");
                        r.setVisibility(View.VISIBLE);
                        if (am.isIs_url()) {
                            im.setVisibility(View.VISIBLE);
                            tm.setVisibility(View.GONE);
                            tm.setText("");
                            showImage(am, im);
                        } else {
                            tm.setVisibility(View.VISIBLE);
                            tm.setText(am.getAnswer());
                            im.setVisibility(View.GONE);
                        }
                    }
                }
                }

                if (rlist.size() > mcolumns) {
                for (; i < rlist.size(); i++) {
                    if(isFillUp){
                        eList.get(i).setVisibility(View.GONE);
                    }
                    else {
                        if (is_ml_ans) {
                            clist.get(i).setVisibility(View.GONE);
                            iList.get(i).setVisibility(View.GONE);
                            tList.get(i).setVisibility(View.GONE);
                        }
                        else{
                            rlist.get(i).setVisibility(View.GONE);
                            iList.get(i).setVisibility(View.GONE);
                            tList.get(i).setVisibility(View.GONE);
                        }
                    }
                }
            }
        }
    }

    private void showImage(AnswerModel am, ImageView im) {
        Picasso.with(apc)
                .load(am.getAnswer()).error(R.mipmap.ic_launcher)
                ./*resize*//*(200, 100)*//*.*/into(im, new com.squareup.picasso.Callback() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onError() {

            }
        });
    }

    public void SavePreferences(String key, String value) {
        SharedPreferences preferences = apc.getSharedPreferences("MY_SHARED_PREF",
                0);
        SharedPreferences.Editor editor = preferences.edit();
        //Log.d("Id "+ key,Integer.toString(value));
        if (!value.equalsIgnoreCase("-1")) {
            editor.putString(key, value);
            editor.commit();
        }
    }

    public void LoadPreferences(int mQuestionNo) {

        SharedPreferences getProgramPrefs = apc.getSharedPreferences(
                "MY_SHARED_PREF", 0);
        //Log.d("path",th)
        String qid = q_id.getText().toString();
        String savedRadioIndex = getProgramPrefs.getString(qid, "-1");
        getSelectedValue(savedRadioIndex);
    }
    private void getSelectedValue(String savedRadioIndex){
        String sval ="";
        if(fill_up_que.getText().toString() == "yes") {
            int index = savedRadioIndex.lastIndexOf(",");
            if (index != -1) {
                String indexes[] = savedRadioIndex.split(",");
                for(int j=0;j<indexes.length;j++){
                    eList.get(j).setText(indexes[j]);
                }
            } else {
                if(!savedRadioIndex.equalsIgnoreCase("-1")){
                    eList.get(0).setText(savedRadioIndex);
                }
                else{
                    eList.get(0).setText("");
                }
            }
        }else{
        if (!savedRadioIndex.equalsIgnoreCase("") && !savedRadioIndex.equalsIgnoreCase("-1")) {
            for (int i = 0; i < rlist.size(); i++) {
                if (ques_ml_option.getText() == "no") {
                    int qid_tmp = Integer.parseInt(savedRadioIndex);
                    if (i == qid_tmp) {
                        rlist.get(i).setChecked(true);
                    }
                } else {
                    int index = savedRadioIndex.lastIndexOf(",");
                    if (index != -1) {
                        String indexes[] = savedRadioIndex.split(",");
                        for (String j : indexes) {
                            int qid_tmp = Integer.parseInt(j);
                            if (qid_tmp == i) {
                                clist.get(i).setChecked(true);
                            }
                        }
                    } else {
                        int qid_tmp = Integer.parseInt(savedRadioIndex);
                        if (qid_tmp == i) {
                            clist.get(i).setChecked(true);
                        }
                    }
                }
            }
        }
        }
    }
    /*private void loadQuestionData(){
        testModel= fbasedata.fetchTestData(apc,this.getClass());
        questionList = testModel.getQues();

    }*/
    public void loadTestData() {
        boolean is_right = false;
        boolean is_ml_ans = false;
        boolean is_fillup = false;
        String table_sql = "select * from "+ DBHandler.TABLE_TEST+" where "+DBHandler.COLUMN_TEST_ID+"="+this.test_id+" and "+DBHandler.COLUMN_IS_ACTIVE+"=1";
        Cursor c = dbhandler.getAllData(table_sql);
        if(c!=null && c.moveToNext()){
            testModel = new TestModel();
            testModel.setTest_id(c.getInt(c.getColumnIndex(DBHandler.COLUMN_TEST_ID)));
            testModel.setTest_name(c.getString(c.getColumnIndex(DBHandler.COLUMN_TEST_NAME)));
            testModel.setTest_ins(c.getString(c.getColumnIndex(DBHandler.COLUMN_TEST_INS)));
            testModel.setClass_id(c.getInt(c.getColumnIndex(DBHandler.COLUMN_CLASS_ID)));
            testModel.setSubject_id(c.getInt(c.getColumnIndex(DBHandler.COLUMN_SUBJECT_ID)));
            testModel.setChapter_id(c.getInt(c.getColumnIndex(DBHandler.COLUMN_CHAPTER_ID)));
            testModel.setStart_time(c.getString(c.getColumnIndex(DBHandler.COLUMN_START_TIME)));
            testModel.setEnd_time(c.getString(c.getColumnIndex(DBHandler.COLUMN_END_TIME)));
            String sql = "select * from "+DBHandler.TABLE_PASSAGE+" where "+DBHandler.COLUMN_TEST_IDS+"="+this.test_id;
            Cursor pc = dbhandler.getAllData(sql);
            HashMap<Integer,PassageModel> pmap  = new HashMap<Integer, PassageModel>();
            if(pc!=null){
                while(pc.moveToNext()){
                    PassageModel passm = new PassageModel();
                    passm.setTestId(this.test_id);
                    int pid = pc.getInt(pc.getColumnIndex(DBHandler.PASSAGE_ID));
                    passm.setPassageId(pid);
                    passm.setPassageText(pc.getString(pc.getColumnIndex(DBHandler.PASSAGE_TEXT)));
                    pmap.put(pid,passm);
                }
                testModel.setHaspassage(true);
                testModel.setPmap(pmap);
            }
            sql = "select * from "+DBHandler.TABLE_QUESTIONS+" where "+DBHandler.QUESTION_TEST_ID+"="+this.test_id;
            Cursor qc = dbhandler.getAllData(sql);
            if(qc!=null){
                while(qc.moveToNext()){
                    QuestionModel questionModel = new QuestionModel();
                    questionModel.setQues_id(qc.getInt(qc.getColumnIndex(DBHandler.QUESTIONS_ID)));
                    questionModel.setQuestion(qc.getString(qc.getColumnIndex(DBHandler.QUESTIONS_TEXT)));
                    int ml_ans = qc.getInt(qc.getColumnIndex((DBHandler.QUESTIONS_HAS_MULTIPLE_ANS)));
                    int fill_ans = qc.getInt(qc.getColumnIndex((DBHandler.QUESTION_IS_FILL_UP)));
                    if(ml_ans == 1){
                        questionModel.setIs_ml_ans(true);
                    }else{
                        questionModel.setIs_ml_ans(false);
                    }
                    if(fill_ans == 1){
                        questionModel.setIsFillUp(true);
                    }else{
                        questionModel.setIsFillUp(false);
                    }
                    questionModel.setPassage_id(qc.getInt(qc.getColumnIndex(DBHandler.QUESTIONS_PASSAGE_ID)));
                    questionModel.setTest_id(qc.getInt(qc.getColumnIndex(DBHandler.QUESTION_TEST_ID)));
                    questionModel.setSubject_id(qc.getInt(qc.getColumnIndex(DBHandler.QUESTION_SUBJECT_ID)));
                    String asql= "select * from "+DBHandler.TABLE_ANSWERS+" where "+DBHandler.ANSWER_QUESTION_ID+"="+questionModel.getQues_id();
                    Cursor ac = dbhandler.getAllData(asql);
                    if(ac!=null){
                        ArrayList<AnswerModel> answers = new ArrayList<AnswerModel>();
                        while(ac.moveToNext()){
                            AnswerModel answerModel = new AnswerModel();
                            int is_url,is_ans_right;
                            is_url =  ac.getInt(ac.getColumnIndex(DBHandler.ANSWER_HAS_URL));
                            is_ans_right = ac.getInt(ac.getColumnIndex(DBHandler.ANSWER_IS_RIGHT));
                            if(is_ans_right==1){
                                answerModel.setIs_right(true);
                            }else{
                                answerModel.setIs_right(false);
                            }
                            if(is_url==1){
                                answerModel.setIs_url(true);
                            }else{
                                answerModel.setIs_url(false);
                            }
                            answerModel.setId(ac.getInt(ac.getColumnIndex(DBHandler.ANSWER_ID)));
                            answerModel.setAnswer(ac.getString(ac.getColumnIndex(DBHandler.ANSWER_TEXT)));
                            answerModel.setQues_id(ac.getInt(ac.getColumnIndex(DBHandler.ANSWER_QUESTION_ID)));
                            answers.add(answerModel);
                        }
                        questionModel.setAnswerList(answers);
                        ac.close();
                    }
                    questionList.put(questionModel.getQues_id(),questionModel);
                }
                testModel.setQues(questionList);
                qc.close();
            }else{
                testModel.setQues(questionList);
            }
            c.close();
        }
    }
    public void clearOptions(){
        for(int i=0;i<rlist.size();i++){
            if(fill_up_que.getText().equals("yes")){
                eList.get(i).setText("");
            }else{
                if(ques_ml_option.getText().equals("no")){
                    rlist.get(i).setChecked(false);
                }else{
                    clist.get(i).setChecked(false);
                }
            }
        }
    }

    public ArrayList<Integer> getQuestionKeys() {
        return ques_keys;
    }

    public void calculate_score() {
        int score = 0;
        SharedPreferences getProgramPrefs = apc.getSharedPreferences("MY_SHARED_PREF", 0);
        for(int i=0;i<ques_keys.size();i++) {
            QuestionModel questionModel = questionList.get(ques_keys.get(i));
            boolean is_right = false;
            String savedRadioIndex = getProgramPrefs.getString( Integer.toString(questionModel.getQues_id()), "-1");
            if (!savedRadioIndex.equalsIgnoreCase("") && !savedRadioIndex.equalsIgnoreCase("-1")) {
                ArrayList<AnswerModel> answers = questionModel.getAnswerList();
                if(questionModel.isIsFillUp()){
                    int index = savedRadioIndex.lastIndexOf(",");
                    if(index != -1){
                        String saveindex[] = savedRadioIndex.split(",");
                        int total_c = 0;
                        for (int j = 0; j < saveindex.length; j++) {
                            if(savedRadioIndex.equalsIgnoreCase(answers.get(j).getAnswer())){
                                is_right = true;
                                total_c++;
                            }
                        }
                        if(total_c == saveindex.length){
                            is_right = true;
                        }else{
                            is_right = false;
                        }
                    }else{
                        if(savedRadioIndex.equalsIgnoreCase(answers.get(0).getAnswer())){
                            is_right = true;
                        }
                    }
                }else{
                    if (questionModel.isIs_ml_ans()) {
                        int index = savedRadioIndex.lastIndexOf(",");
                        if (index != -1) {
                            String saveindex[] = savedRadioIndex.split(",");
                            int total_c = 0;
                            for (int j = 0; j < saveindex.length; j++) {
                                is_right = checkRight(answers.get(Integer.parseInt(saveindex[j])));
                                if(is_right){
                                    total_c++;
                                }
                            }
                            if(total_c == saveindex.length){
                                is_right = true;
                            }else{
                                is_right = false;
                            }
                        } else {
                            is_right = false;
                        }
                    }else{
                        int saveindex = Integer.parseInt(savedRadioIndex);
                        is_right = checkRight(answers.get(saveindex));
                    }
                }

            }
            if(is_right){
                score++;
            }

        }
        SharedPreferences.Editor editor =  getProgramPrefs.edit();
        editor.clear();
        editor.commit();
        testModel.setScore(score);
    }
    private boolean checkRight(AnswerModel ans){
        if(ans.isIs_right())
        {
            return true;
        }
        return false;
    }

}
