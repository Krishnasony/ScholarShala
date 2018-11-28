package com.appteq.ad.appteq.actions;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.appteq.ad.appteq.HomeActivity;
import com.appteq.ad.appteq.LoginActivity;
import com.appteq.ad.appteq.R;
import com.appteq.ad.appteq.SignupActivity;
import com.appteq.ad.appteq.data.DBHandler;
import com.appteq.ad.appteq.model.ClassModel;
import com.appteq.ad.appteq.model.UserModel;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class App_Functions {
    public static ArrayList<ClassModel> classeslist;
    public static ArrayList<String> model = null;
    private ProgressDialog progressDialog;
    static UserModel user = null;
    public App_Functions(Context context){
        progressDialog = new ProgressDialog(context);
        progressDialog.setCancelable(false);
    }
 public ArrayList<String> getMediumList(){
     ArrayList<String> med = new ArrayList<String>();
     med.add("Select class medium");
     med.add("English");
     med.add("Hindi");
    return med;
 }
    public static ArrayList<ClassModel> getClasses() {
        return classeslist;
    }
    public void setProgressMessage(String message){
        progressDialog.setMessage(message);
    }
    public void showProgressDialog(){
        progressDialog.show();
    }
    public void dismissProgressDialog(){
        progressDialog.dismiss();
    }
    public void showErrors(Context context, String message, String title){
        AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(context);

        dlgAlert.setMessage(message);
        dlgAlert.setTitle(title);
        dlgAlert.setPositiveButton("OK", null);
        dlgAlert.setCancelable(true);
        dlgAlert.create().show();

        dlgAlert.setPositiveButton("Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
    }
    public boolean is_user_login(DBHandler mDbHelper){
        String Query = "Select * from " + DBHandler.TABLE_USER_AUTH;
        Cursor cursor = mDbHelper.getAllData(Query);
        if(cursor!=null && cursor.moveToNext()){
            return true;
        }
        else{
            return false;
        }
    }
    public boolean is_user_payment_valid(DBHandler mDbHelper){
        String Query = "Select * from " + DBHandler.PAYMENT_TABLE+ " where "+DBHandler.PAYMENT_STATUS+"='TXN_SUCCESS' order by ID desc LIMIT 0,1";
        Cursor cursor = mDbHelper.getAllData(Query);
        if(cursor!=null && cursor.moveToNext()){
            return true;
        }
        else{
            return false;
        }
    }
    public void user_logout(DBHandler mDbHelper){
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        db.execSQL("DELETE FROM " + DBHandler.TABLE_USER_AUTH);
        db.close();
    }

    public UserModel get_user_id(DBHandler mdbhandler) {
        UserModel user = null;
        String sql = "select "+DBHandler.USER_AUTH_ID+","+DBHandler.USER_TOKEN+","+DBHandler.USER_NAME+","+DBHandler.USER_CLASS_NAME+","+DBHandler.USER_PIC_URL+","+DBHandler.USER_CLASS_NAME+","+DBHandler.USER_MEDIUM+","+DBHandler.USER_PARENT_NAME+","+DBHandler.USER_PARENT_PHONE+" from "+DBHandler.TABLE_USER_AUTH +" INNER JOIN "+DBHandler.TABLE_USER+" ON "+DBHandler.TABLE_USER_AUTH+"."+DBHandler.USER_AUTH_ID+" = "+DBHandler.TABLE_USER+"."+DBHandler.USER_ID+" LIMIT 0,1";
        Log.d("query", " query "+sql);
        Cursor cursor = mdbhandler.getAllData(sql);
        if(cursor!=null && cursor.moveToNext()){
            user = new UserModel();
            Log.d("User", "get_user_id: "+user);
            user.setUserid(cursor.getInt(cursor.getColumnIndex(DBHandler.USER_AUTH_ID)));
            user.setLogin_token(cursor.getString(cursor.getColumnIndex(DBHandler.USER_TOKEN)));
            user.setUser_fullname(cursor.getString(cursor.getColumnIndex(DBHandler.USER_NAME)));
            user.setUser_class_name(cursor.getString(cursor.getColumnIndex(DBHandler.USER_CLASS_NAME)));
            user.setUser_class_med(cursor.getString(cursor.getColumnIndex(DBHandler.USER_MEDIUM)));
            user.setParent_name(cursor.getString(cursor.getColumnIndex(DBHandler.USER_PARENT_NAME)));
            user.setParent_phone_no(cursor.getString(cursor.getColumnIndex(DBHandler.USER_PARENT_PHONE)));
            user.setUserimage(cursor.getString(cursor.getColumnIndex(DBHandler.USER_PIC_URL)));
            user.setUser_class_name(cursor.getString(cursor.getColumnIndex(DBHandler.USER_CLASS_NAME))); ;
        }
        return user;
    }
    public UserModel getloggedinuser(DBHandler mdbhandler){
            if(user == null){
                user = get_user_id(mdbhandler);
            }
            return user;
    }
}
