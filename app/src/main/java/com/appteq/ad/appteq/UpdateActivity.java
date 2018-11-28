package com.appteq.ad.appteq;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.res.ResourcesCompat;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.appteq.ad.appteq.actions.APP_CONSTANT;
import com.appteq.ad.appteq.actions.App_Functions;
import com.appteq.ad.appteq.actions.RequestAdapt;
import com.appteq.ad.appteq.actions.SharedPrefManager;
import com.appteq.ad.appteq.data.DBHandler;
import com.appteq.ad.appteq.model.ChapterModel;
import com.appteq.ad.appteq.model.ClassModel;
import com.appteq.ad.appteq.model.SubjectModel;
import com.appteq.ad.appteq.model.UserModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class UpdateActivity extends NavigationActivity {

    private TextInputLayout nameEditText;
    private Spinner classspin,medspin;
    public static ArrayList<ClassModel> classeslist;
    public static ArrayList<String> model = null;
    private RequestQueue mQueue;
    private ArrayList<ClassModel> classes;
    private LinearLayout subjects;
    String selectedsubject;
    int classposition,medpos;
    ArrayList<String> med;
    ArrayList<SubjectModel> selected_subjects;

    private Context mContext;
    private Activity mActivity;
    private PopupWindow mPopupWindow;
    private Button done;
    private RelativeLayout mRelativeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        ImageButton closeButton = (ImageButton)findViewById(R.id.ib_close);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),ProfileActivity.class));
            }
        });

            nameEditText = findViewById(R.id.txt_reg_username);
            done = findViewById(R.id.saveinfo);
            final Typeface typeface = ResourcesCompat.getFont(this, R.font.proximanova);
            final App_Functions func = new App_Functions(this);
            subjects = (LinearLayout) findViewById(R.id.subjects);
            classspin = (Spinner) findViewById(R.id.classlist);
            medspin = (Spinner) findViewById(R.id.classmed);
            ///func.fetchClasses(this,this);
            done.setEnabled(false);
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
                        ArrayAdapter<String> classadapter = new ArrayAdapter<String>(UpdateActivity.this, android.R.layout.simple_spinner_item, smodel);
                        classadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        classspin.setAdapter(classadapter);
                        func.dismissProgressDialog();
                        done.setEnabled(true);
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
            done = findViewById(R.id.saveinfo);
            final String register_url = APP_CONSTANT.webservice_url+"userupdate";

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
                        CheckBox subcheck = new CheckBox(UpdateActivity.this);
                        subcheck.setId(i);
                        subcheck.setText(csubjects.get(i));
                        subcheck.setTextSize(TypedValue.COMPLEX_UNIT_SP,18);
                        subcheck.setTypeface(typeface,Typeface.NORMAL);
                        subcheck.setVisibility(View.VISIBLE);
                        subcheck.setTextColor(getResources().getColor(R.color.buttoncolor));
                        subcheck.setHighlightColor(getResources().getColor(R.color.buttoncolor));
                        subcheck.setLayoutParams(lparams);
                        subjects.addView(subcheck);
                    }
                }
                public void onNothingSelected(AdapterView arg0) {
                }
            });
            done.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    String message = "";
                    selected_subjects = new ArrayList<SubjectModel>();
                    done.setEnabled(false);
                    boolean is_error = false;

                    selectedsubject = "";
                    classposition = classspin.getSelectedItemPosition();
                    medpos = medspin.getSelectedItemPosition();
                    if(classposition == 0){
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
                        func.showErrors(UpdateActivity.this,message,"Error message");
                        done.setEnabled(true);
                        done.setClickable(true);
                    }else{
                        func.setProgressMessage(getResources().getString(R.string.validateinfo));
                        func.showProgressDialog();
                        StringRequest stringRequest = new StringRequest(Request.Method.POST, register_url, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                JSONArray jsonArray = null;
                                String username = "";
                                boolean is_success = false;
                                JSONArray jchapters = null;
                                ArrayList<ChapterModel> chapters = null;
                                try {
                                    ClassModel cm = classes.get(classposition-1);
                                    JSONObject jsonObject = new JSONObject(response);
                                    Toast.makeText(UpdateActivity.this, "try"+jsonObject, Toast.LENGTH_SHORT).show();
                                    Iterator<String> responsekeys = jsonObject.keys();
                                    while(responsekeys.hasNext()){
                                        String key = responsekeys.next();
                                        if(key.equalsIgnoreCase("err")){
                                            func.showErrors(UpdateActivity.this,jsonObject.getString("message"),"Error message");
                                            func.dismissProgressDialog();
                                            done.setEnabled(true);
                                            done.setClickable(true);
                                            break;
                                        }else if(key.equalsIgnoreCase("success")){
                                            is_success = true;
                                        }
                                        else if(key.equalsIgnoreCase("chapters")){
                                            jchapters = jsonObject.getJSONArray("chapters");
                                        }
                                    }
                                    if(is_success){
                                        SQLiteDatabase db = mDbhelper.getWritableDatabase();

                                        HashMap<Integer,String> su = cm.getSubjects();
                                        Set<Integer> ss = su.keySet();
                                        user.setUser_class(cm.getClass_id());
                                        user.setUser_class_name(cm.getClass_name());
                                        user.setUser_class_med(med.get(medpos));
                                        for (SubjectModel subjectModel:selected_subjects){
                                            subjectModel.setUser_id(user.getUserid());
                                        }
                                        user.setSubjects(selected_subjects);
                                        if(jchapters!=null) {
                                            chapters = new ArrayList<ChapterModel>();
                                            for (int i = 0; i < jchapters.length(); i++) {
                                                JSONObject jchapter = (JSONObject) jchapters.get(i);
                                                ChapterModel chapterModel = new ChapterModel();
                                                chapterModel.setChapter_id(jchapter.getInt("ID"));
                                                chapterModel.setChapter_name(jchapter.getString("chapter_name"));
                                                chapterModel.setSubject_id(jchapter.getInt("subject_id"));
                                                chapterModel.setClass_id(cm.getClass_id());
                                                chapterModel.setUser_id(user.getUserid());
                                                chapters.add(chapterModel);
                                            }
                                        }
                                        user.setChapters(chapters);
                                        String sql = "delete from "+DBHandler.TABLE_USER_SUBJECT;
                                        mDbhelper.deleteData(sql,db);
                                        sql = "delete from "+DBHandler.CHAPTER_TABLE;
                                        mDbhelper.deleteData(sql,db);
                                        mDbhelper.insertUserSubject(user,db);
                                        mDbhelper.insertUserChapter(user,db);
                                        mDbhelper.updateUserData(user);
                                        func.dismissProgressDialog();
                                        Toast.makeText(UpdateActivity.this,getResources().getString(R.string.updatesuccess),Toast.LENGTH_SHORT).show();
                                        db.close();
                                        startActivity(new Intent(UpdateActivity.this,ProfileActivity.class));
                                        done.setEnabled(true);
                                    }
                                } catch (JSONException e) {
                                    func.dismissProgressDialog();
                                    Toast.makeText(getApplicationContext(),e.toString(),Toast.LENGTH_SHORT).show();
                                    done.setEnabled(true);
                                    done.setClickable(true);
                                    e.printStackTrace();
                                }
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                func.dismissProgressDialog();
                                done.setEnabled(true);
                                done.setClickable(true);
                                Toast.makeText(UpdateActivity.this,error.toString(),Toast.LENGTH_SHORT).show();
                            }
                        }) {
                            @Override
                            protected Map<String, String> getParams() throws AuthFailureError {
                                String token = SharedPrefManager.getInstance(UpdateActivity.this).getDeviceToken();

                                while(token==null) {
                                    token = SharedPrefManager.getInstance(UpdateActivity.this).getDeviceToken();
                                }
                                Map<String, String> params = new HashMap<String, String>();
                                ClassModel cm = classes.get(classposition-1);
                                params.put("user_class",Integer.toString(cm.getClass_id()));
                                int index = selectedsubject.lastIndexOf(",");
                                selectedsubject = selectedsubject.substring(0,index);
                                params.put("user_subject",selectedsubject);
                                params.put("user_medium",med.get(medpos));
                                params.put("logintoken",user.getLogin_token());
                                params.put("user_id",user.getUserid()+"");
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
    }

