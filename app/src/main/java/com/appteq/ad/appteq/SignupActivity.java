package com.appteq.ad.appteq;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.appteq.ad.appteq.actions.APP_CONSTANT;
import com.appteq.ad.appteq.actions.App_Functions;
import com.appteq.ad.appteq.actions.RequestAdapt;
import com.appteq.ad.appteq.actions.SharedPrefManager;
import com.appteq.ad.appteq.data.DBHandler;
import com.appteq.ad.appteq.model.ChapterModel;
import com.appteq.ad.appteq.model.ClassModel;
import com.appteq.ad.appteq.model.SubjectModel;
import com.appteq.ad.appteq.model.UserModel;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

public class SignupActivity extends AppCompatActivity{
    private TextInputLayout nameEditText,phoneEditText,paPhoneEditText,passwordEditText,dobEditText,paNameEditText;
    private Button signup;
    private TextView login;
    private Spinner classspin,medspin;
    private DBHandler mDbHelper;
    public static ArrayList<ClassModel> classeslist;
    public static ArrayList<String> model = null;
    private RequestQueue mQueue;
    private ArrayList<ClassModel> classes;
    private DatePickerDialog DatePickerDialog;
    private LinearLayout subjects;
    private SimpleDateFormat dateFormatter;
    String name, passwd,phone,dob,paname,paphone,selectedsubject;
    int classposition,medpos;
    ArrayList<String> med;
    ArrayList<SubjectModel> selected_subjects;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        signup = findViewById(R.id.register);
        final Typeface typeface = ResourcesCompat.getFont(this, R.font.proximanova);
        final App_Functions func = new App_Functions(this);
        subjects = (LinearLayout) findViewById(R.id.subjects);
        classspin = (Spinner) findViewById(R.id.classlist);
        medspin = (Spinner) findViewById(R.id.classmed);
        ///func.fetchClasses(this,this);
        signup.setEnabled(false);
        //String url = APP_CONSTANT.webservice_url+"getclass";
        String url = APP_CONSTANT.webservice_url+"getclass";
        func.setProgressMessage("Loading classes & subjects");
        func.showProgressDialog();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JSONArray jsonArray = null;

                try {
                    classeslist = new ArrayList<ClassModel>();
                    JSONObject jsonObject = new JSONObject(response);
                    ArrayList<String> smodel = new ArrayList<String>();
                    smodel.add("Select Class");
                    JSONObject classes = jsonObject.getJSONObject("classes");
                    Iterator<String> keys = classes.keys();
                    while(keys.hasNext()){
                        String key = keys.next();
                        ClassModel cmodel = new ClassModel();
                        cmodel.setClass_id(Integer.parseInt(key));
                        JSONObject tmp_class = classes.getJSONObject(key);
                        String classname = tmp_class.getString("class_name");
                        cmodel.setClass_name(classname);
                        smodel.add(classname);
                        JSONObject subjects = tmp_class.getJSONObject("subjects");
                        HashMap<Integer,String> subjectmap = new HashMap<Integer, String>();
                        Iterator<String> subjetkeys = subjects.keys();
                        while(subjetkeys.hasNext()){
                            String subkey = subjetkeys.next();
                            subjectmap.put(Integer.parseInt(subkey),subjects.getString(subkey));
                        }
                        cmodel.setSubjects(subjectmap);
                        classeslist.add(cmodel);
                        //sign.setClasses(model);
                    }
                    ArrayAdapter<String> classadapter = new ArrayAdapter<String>(SignupActivity.this, android.R.layout.simple_spinner_item, smodel);
                    classadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    classspin.setAdapter(classadapter);
                    func.dismissProgressDialog();
                    signup.setEnabled(true);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),error.toString(),Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("action","retrieve");
                return params;
            }


        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));
        RequestAdapt.getInstance(this).addToRequestQueue(stringRequest);
        med = func.getMediumList();
        ArrayAdapter<String> medadapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, med);
        medadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        medspin.setAdapter(medadapter);
        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
        mDbHelper = DBHandler.getHelper(this);
        dobEditText = findViewById(R.id.txt_reg_date);
        signup = findViewById(R.id.register);
        login = (TextView) findViewById(R.id.btn_login);
        nameEditText = findViewById(R.id.txt_reg_username);
        phoneEditText = findViewById(R.id.txt_reg_phone);
        paNameEditText = findViewById(R.id.txt_reg_parent_name);
        paPhoneEditText = findViewById(R.id.txt_reg_parent_phone);
        passwordEditText = findViewById(R.id.txt_reg_password);
        final String register_url = APP_CONSTANT.webservice_url+"userappregistration";
        Calendar newCalendar = Calendar.getInstance();
        DatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                dobEditText.getEditText().setText(dateFormatter.format(newDate.getTime()));

            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        dobEditText.getEditText().setOnClickListener(new View.OnClickListener(){
            public void onClick(View view) {
                DatePickerDialog.show();
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignupActivity.this,LoginActivity.class));
            }
        });
        classspin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            public void onItemSelected(AdapterView parent, View view, int position, long id) {
                if(position == 0){
                    return;
                }
                classes = classeslist;
                ClassModel classModel = classes.get(position-1);
                HashMap<Integer,String> csubjects = classModel.getSubjects();
                Set<Integer> keys = csubjects.keySet();
                LinearLayout.LayoutParams lparams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                subjects.removeAllViews();
                for (Integer i:keys) {
                    CheckBox subcheck = new CheckBox(SignupActivity.this);
                    subcheck.setId(i);
                    subcheck.setText(csubjects.get(i));
                    subcheck.setTextSize(TypedValue.COMPLEX_UNIT_SP,18);
                    subcheck.setTypeface(typeface,Typeface.NORMAL);
                    subcheck.setVisibility(View.VISIBLE);
                    subcheck.setTextColor(getResources().getColor(R.color.colorwhite));
                    subcheck.setHighlightColor(getResources().getColor(R.color.colorwhite));
                    subcheck.setLayoutParams(lparams);
                    subjects.addView(subcheck);
                }
            }
            public void onNothingSelected(AdapterView arg0) {
            }
        });
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String message = "";
                selected_subjects = new ArrayList<SubjectModel>();
                signup.setEnabled(false);
                boolean is_error = false;
                name = nameEditText.getEditText().getText().toString().trim();
                name = name.trim();
                passwd = passwordEditText.getEditText().getText().toString();
                passwd = passwd.trim();
                phone = phoneEditText.getEditText().getText().toString();
                phone = phone.trim();
                paname = paNameEditText.getEditText().getText().toString();
                paname = paname.trim();
                paphone = paPhoneEditText.getEditText().getText().toString();
                paphone = paphone.trim();
                dob = dobEditText.getEditText().getText().toString();
                dob = dob.trim();
                selectedsubject = "";
                classposition = classspin.getSelectedItemPosition();
                medpos = medspin.getSelectedItemPosition();
                if(name.equalsIgnoreCase("")){
                    message = getResources().getString(R.string.nameerr);
                    is_error = true;
                }else if(passwd.equalsIgnoreCase("")){
                    message = getResources().getString(R.string.passwordvalidate);
                    is_error = true;
                }else if(phone.equalsIgnoreCase("") || phone.length()!=10){
                    message = getResources().getString(R.string.phonevalidate);
                    is_error = true;
                }else if(dob.equalsIgnoreCase("")){
                    message = getResources().getString(R.string.dateerr);
                    is_error = true;
                }else if(paname.equalsIgnoreCase("")){
                    message = getResources().getString(R.string.parentnameerr);
                    is_error = true;
                }else if(paphone.equalsIgnoreCase("") || paphone.length()!=10){
                    message = getResources().getString(R.string.phonevalidate);
                    is_error = true;
                }else if(classposition == 0){
                    message = getResources().getString(R.string.classserr);
                    is_error = true;
                }else if(medpos == 0){
                    message = getResources().getString(R.string.subjecterr);
                    is_error = true;
                }
                if(!is_error && classposition > 0){
                    int boxes = subjects.getChildCount();
                    int checkedcount = 0;
                    for(int i=0;i<boxes;i++){
                        CheckBox checkBox = (CheckBox) subjects.getChildAt(i);
                        if(checkBox.isChecked()){
                            selectedsubject += checkBox.getId()+",";
                            SubjectModel submodel = new SubjectModel();
                            submodel.setSubject_id(checkBox.getId());
                            submodel.setSubject_name(checkBox.getText().toString());
                            selected_subjects.add(submodel);
                            checkedcount++;
                        }
                    }
                    if(checkedcount==0){
                        message = getResources().getString(R.string.subjecterr);
                        is_error = true;
                    }
                }
                if(is_error){
                    func.dismissProgressDialog();
                    func.showErrors(SignupActivity.this,message,"Error message");
                    signup.setEnabled(true);
                    signup.setClickable(true);
                }else{
                    func.setProgressMessage(getResources().getString(R.string.validateinfo));
                    func.showProgressDialog();
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, register_url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            JSONArray jsonArray = null;
                            int user_id = 0;
                            String username = "";
                            boolean is_success = false;
                            JSONArray jchapters = null;
                            ArrayList<ChapterModel> chapters = null;
                            try {
                                ClassModel cm = classes.get(classposition-1);
                                JSONObject jsonObject = new JSONObject(response);
                                Iterator<String> responsekeys = jsonObject.keys();
                                while(responsekeys.hasNext()){
                                    String key = responsekeys.next();
                                    if(key.equalsIgnoreCase("errs")){
                                        func.showErrors(SignupActivity.this,jsonObject.getString(key),"Error message");
                                        func.dismissProgressDialog();
                                        signup.setEnabled(true);
                                        signup.setClickable(true);
                                        break;
                                    }else if(key.equalsIgnoreCase("user_id")){
                                        user_id = Integer.parseInt(jsonObject.getString(key));

                                    }else if(key.equalsIgnoreCase("username")){
                                        username = jsonObject.getString(key);
                                    }else if(key.equalsIgnoreCase("success")){
                                        is_success = true;
                                    }
                                    else if(key.equalsIgnoreCase("chapters")){
                                        jchapters = jsonObject.getJSONArray("chapters");
                                    }

                                }
                                if(is_success){
                                    SQLiteDatabase db = mDbHelper.getWritableDatabase();
                                    UserModel model = new UserModel();
                                    model.setUserid(user_id);
                                    model.setUsername(username);
                                    model.setUser_fullname(name);
                                    model.setDob(dob);
                                    model.setPhone_no(phone);
                                    model.setParent_name(paname);
                                    model.setPasswd(passwd);
                                    model.setParent_phone_no(paphone);
                                    HashMap<Integer,String> su = cm.getSubjects();
                                    Set<Integer> ss = su.keySet();
                                    model.setUser_class(cm.getClass_id());
                                    model.setUser_class_name(cm.getClass_name());
                                    model.setUser_class_med(med.get(medpos));
                                    for (SubjectModel subjectModel:selected_subjects){
                                        subjectModel.setUser_id(user_id);
                                    }
                                    model.setSubjects(selected_subjects);
                                    if(jchapters!=null) {
                                        chapters = new ArrayList<ChapterModel>();
                                        for (int i = 0; i < jchapters.length(); i++) {
                                            JSONObject jchapter = (JSONObject) jchapters.get(i);
                                            ChapterModel chapterModel = new ChapterModel();
                                            chapterModel.setChapter_id(jchapter.getInt("ID"));
                                            chapterModel.setChapter_name(jchapter.getString("chapter_name"));
                                            chapterModel.setSubject_id(jchapter.getInt("subject_id"));
                                            chapterModel.setClass_id(cm.getClass_id());
                                            chapterModel.setUser_id(user_id);
                                            chapters.add(chapterModel);
                                        }
                                    }
                                    model.setChapters(chapters);
                                    mDbHelper.insertUserData(model,db);
                                    mDbHelper.insertUserSubject(model,db);
                                    mDbHelper.insertUserChapter(model,db);
                                    func.dismissProgressDialog();
                                    Toast.makeText(SignupActivity.this,getResources().getString(R.string.registersuccess),Toast.LENGTH_SHORT).show();
                                    db.close();
                                    mDbHelper.close();
                                    startActivity(new Intent(SignupActivity.this,LoginActivity.class));
                                    signup.setEnabled(true);


                                }
                            } catch (JSONException e) {
                                signup.setEnabled(true);
                                signup.setClickable(true);
                                e.printStackTrace();
                                mDbHelper.close();
                            }


                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            func.dismissProgressDialog();
                            signup.setEnabled(true);
                            signup.setClickable(true);
                            mDbHelper.close();
                            Toast.makeText(SignupActivity.this,error.toString(),Toast.LENGTH_SHORT).show();
                        }
                    }) {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            String token = SharedPrefManager.getInstance(SignupActivity.this).getDeviceToken();

                            while(token==null) {
                                token = SharedPrefManager.getInstance(SignupActivity.this).getDeviceToken();
                            }
                            Map<String, String> params = new HashMap<String, String>();
                            params.put("user_fullname",name);
                            params.put("passwd",passwd);
                            params.put("phone_no",phone);
                            params.put("dob",dob);
                            params.put("parent_name",paname);
                            params.put("parent_phone_no",paphone);
                            ClassModel cm = classes.get(classposition-1);
                            params.put("user_class",Integer.toString(cm.getClass_id()));
                            int index = selectedsubject.lastIndexOf(",");
                            selectedsubject = selectedsubject.substring(0,index);
                            params.put("user_subject",selectedsubject);
                            params.put("user_medium",med.get(medpos));
                            params.put("phone_token",token);
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
        });
    }
    public void setClasses(ArrayList<String> classes){
        ArrayAdapter<String> classadapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, classes);
        classadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        classspin.setAdapter(classadapter);
    }
}