package com.appteq.ad.appteq;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import com.appteq.ad.appteq.actions.RequestAdapt;
import com.appteq.ad.appteq.actions.SharedPrefManager;
import com.appteq.ad.appteq.data.DBHandler;
import com.appteq.ad.appteq.model.UserModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class LoginActivity extends AppCompatActivity {
    TextInputLayout editTextphone,editTextpassword;
    Button buttonloginpost;
    FloatingActionButton btnReg;
    AlertDialog.Builder builder;
    String phone_no,passwd,phone_token;
    ProgressDialog progressDialog;
    String auth_token,current_time,auth_time,last_login,success;
    private DBHandler mDbHelper;
    App_Functions func;
    String otp_url = APP_CONSTANT.webservice_url + "appuserlogin";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        progressDialog = new ProgressDialog(this);
        setContentView(R.layout.activity_main2);
        String isSplashAppear = SharedPrefManager.getInstance(this).getSplashScreenStatus();
        if(isSplashAppear==null || !isSplashAppear.equalsIgnoreCase("yes")){
            Intent mainIntent = new Intent(LoginActivity.this,SplashSlideActivity.class);
            LoginActivity.this.startActivity(mainIntent);
            finish();
            SharedPrefManager.getInstance(this).saveSplashScreenStatus("yes");
        }
        mDbHelper = DBHandler.getHelper(this);
        editTextphone = findViewById(R.id.txt_login_phone);
        editTextpassword = findViewById(R.id.txt_login_password);
        buttonloginpost = findViewById(R.id.btn_login);
        btnReg = findViewById(R.id.btn_Registration);
        func = new App_Functions(this);
        boolean is_user_login = func.is_user_login(mDbHelper);
        if(is_user_login){
            startActivity(new Intent(LoginActivity.this,HomeActivity.class));
        }
        btnReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this,SignupActivity.class));
            }
        });



        buttonloginpost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonloginpost.setClickable(false);
                String message = "";
                boolean is_error = false;
                phone_no = editTextphone.getEditText().getText().toString().trim();
                passwd = editTextpassword.getEditText().getText().toString().trim();

                if(phone_no.equalsIgnoreCase("") || phone_no.length()!=10){
                    message = getResources().getString(R.string.phonevalidate);
                    is_error = true;
                }else if(passwd.equalsIgnoreCase("")){
                    message = getResources().getString(R.string.passwordvalidate);
                    is_error = true;
                }
                if(is_error){
                    func.dismissProgressDialog();
                    func.showErrors(LoginActivity.this,message,"Error message");
                    buttonloginpost.setClickable(true);
                }else{
                    func.setProgressMessage(getResources().getString(R.string.validateinfo));
                    func.showProgressDialog();
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, otp_url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            JSONArray jsonArray = null;
                            boolean is_success = false;
                            String auth_token = "";
                            String current_time = "";
                            String last_login = "";
                            int user_id = 0;
                            UserModel userModel = new UserModel();
                            try {

                                JSONObject jsonObject = new JSONObject(response);
                                Iterator<String> keys = jsonObject.keys();
                                Cursor userc = null;
                                Toast.makeText(getApplicationContext(),jsonObject.toString(),Toast.LENGTH_SHORT).show();
                                while(keys.hasNext()){
                                    String key = keys.next();
                                    if(key.equalsIgnoreCase("err") || key.equalsIgnoreCase("errs")){
                                        func.showErrors(LoginActivity.this,jsonObject.getString(key),"Error message");
                                        func.dismissProgressDialog();
                                        buttonloginpost.setClickable(true);
                                        break;
                                    }else if(key.equalsIgnoreCase("success")){
                                        is_success = true;
                                        /*String sql = "select "+DBHandler.USER_ID+" from "+DBHandler.TABLE_USER+" where "+DBHandler.USER_AUTH_NAME+"='user_"+phone_no+"' and "+DBHandler.USER_PASSWORD+"='"+passwd+"'";
                                        Log.d("Sql",sql);
                                        userc  =  mDbHelper.getAllData(sql);*/
                                    }else if(key.equalsIgnoreCase("auth_token")){
                                        auth_token = jsonObject.getString(key);
                                    }
                                    else if(key.equalsIgnoreCase("current_time")){
                                        current_time = jsonObject.getString(key);
                                    }
                                    else if(key.equalsIgnoreCase("last_login")){
                                        last_login = jsonObject.getString(key);
                                    }
                                    else if(key.equalsIgnoreCase("user_id")){
                                        user_id = jsonObject.getInt(key);
                                    }
                                }
                                func.dismissProgressDialog();
                                if(is_success){
                                    //userc.moveToNext();
                                    SQLiteDatabase db = mDbHelper.getWritableDatabase();
                                    //Integer id = userc.getInt(userc.getColumnIndex(DBHandler.USER_ID));
                                    userModel.setUserid(user_id);
                                    userModel.setLogin_token(auth_token);
                                    userModel.setUser_login_current_time(current_time);
                                    if(last_login!=null && last_login!=""){
                                        userModel.setUser_login_current_time(last_login);
                                    }
                                    String sql = "delete from "+DBHandler.TABLE_USER_AUTH;
                                    mDbHelper.deleteData(sql,db);
                                    mDbHelper.insertAuthdata(userModel,db);
                                    db.close();
                                    Toast.makeText(LoginActivity.this,getResources().getString(R.string.loginsuccess),Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(LoginActivity.this,HomeActivity.class));

                                }
                            } catch (JSONException e) {
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
                            String token = SharedPrefManager.getInstance(LoginActivity.this).getDeviceToken();

                            while(token==null) {
                                token = SharedPrefManager.getInstance(LoginActivity.this).getDeviceToken();
                            }
                            params.put("phone_no",phone_no);
                            params.put("passwd",passwd);
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
}
