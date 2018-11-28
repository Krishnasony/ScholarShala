package com.appteq.ad.appteq;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.graphics.BitmapCompat;
import android.support.v4.view.GravityCompat;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.appteq.ad.appteq.actions.Utils;
import com.appteq.ad.appteq.model.ClassModel;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Iterator;

public class ProfileActivity extends NavigationActivity {

    //DECLARATION
    private ImageView mImageView;
    private TextView UserName;
    private TextView fatherName;
    private TextView contact;
    private TextView className;
    private TextView medium;
    private Button updateInfo;
    private Button saveInfo;
    private Uri mUri;
    private Intent mGalIntent, mCropIntent;
    final int RequestPermissionCode = 1;
    private ProgressDialog dialog = null;
    private JSONObject jsonObject;
    private RelativeLayout mRelativeLayout;

    private Context mContext;
    private Activity mActivity;
    private PopupWindow mPopupWindow;
    private ScrollView mScrollView;
    private Spinner classspin,medspin;
    private ArrayList<ClassModel> classes;

    //ONCREATE METHOD
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FrameLayout contentFrameLayout = (FrameLayout) findViewById(R.id.content_frame); //Remember this is the FrameLayout area within your activity_main.xml
        getLayoutInflater().inflate(R.layout.activity_profile, contentFrameLayout);
        final Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        CollapsingToolbarLayout collapsingToolbar = findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle("PROFILE");
        collapsingToolbar.setCollapsedTitleTypeface(ResourcesCompat.getFont(this,R.font.proximanova));
        collapsingToolbar.setExpandedTitleTypeface(ResourcesCompat.getFont(this,R.font.proximanova));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (drawerLayout.isDrawerVisible(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                } else {
                    drawerLayout.openDrawer(GravityCompat.START);
                }
            }
        });
        initViews();//ALL INTAIALIZATION
        allData();//ALL DATA FROM DB
        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GalleryOpen();

            }
        });//FOR PICKING IMAGE FROM GALLERY
        updateInformation();//UPDATE CLASS AND MEDIUM
        //saveInformation();//SAVE INFORMATION
    }
    private void allData() {
        UserName.setText(user.getUser_fullname());
        className.setText(user.getUser_class_name());
        medium.setText(user.getUser_class_med());
        fatherName.setText(user.getParent_name());
        contact.setText(user.getParent_phone_no());
    }
    private void GalleryOpen() {
        mGalIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(Intent.createChooser(mGalIntent, "Select Image From Gallery"), 2);
    }
    private void saveInformation() {
        saveInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SQLiteDatabase db = mDbhelper.getReadableDatabase();
                String strSQL = "UPDATE TABLE_USER SET USER_PARENT_PHONE = " + String.valueOf(contact.getText()) + " WHERE USER_ID = 1";
                db.execSQL(strSQL);
            }
        });
    }
    private void updateInformation() {
        updateInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(LAYOUT_INFLATER_SERVICE);
                View customView = inflater.inflate(R.layout.custom_layout,null);
                mPopupWindow = new PopupWindow(customView, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
                if(Build.VERSION.SDK_INT>=21){
                    mPopupWindow.setElevation(5.0f);
                }
                ImageButton closeButton = (ImageButton) customView.findViewById(R.id.ib_close);
                closeButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mPopupWindow.dismiss();
                    }
                });
                mPopupWindow.showAtLocation(mScrollView,Gravity.CENTER,0,0);*/
                startActivity(new Intent(getApplicationContext(),UpdateActivity.class));
            }
        });
    }
    private void uploadPic() {
        Bitmap image = ((BitmapDrawable) mImageView.getDrawable()).getBitmap();
        dialog.show();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 95, byteArrayOutputStream);
        byte[] imageInByte = byteArrayOutputStream.toByteArray();
        final int bitmapByteCount = BitmapCompat.getAllocationByteCount(image);
        String encodedImage = Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT);
        final int length = encodedImage.length();
        try {
            jsonObject.put("user_id", user.getUserid());
            jsonObject.put("logintoken", user.getLogin_token());
            jsonObject.put(Utils.image, encodedImage);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Utils.urlUpload, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                dialog.dismiss();
                boolean is_success = false;
                try {
                    Iterator<String> keys= jsonObject.keys();
                    while (keys.hasNext()){
                        String key = keys.next();
                        if(key.equalsIgnoreCase("err")){
                            func.showErrors(ProfileActivity.this,jsonObject.getString("message"),"Image upload Error");
                            mImageView.setImageDrawable(null);
                            break;
                        }else if(key.equalsIgnoreCase("success")){
                            is_success = true;
                        }else if(key.equalsIgnoreCase("fileurl")){
                            user.setUserimage(jsonObject.getString(key));
                        }
                    }
                    if(is_success){
                        mDbhelper.updateUserData(user);
                        Toast.makeText(ProfileActivity.this, "Image uploaded successfully", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    mImageView.setImageDrawable(null);
                }
                finally {
                    func.dismissProgressDialog();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                volleyError.printStackTrace();
                Log.e("Message from server", volleyError.toString());
                dialog.dismiss();
            }
        });
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        Volley.newRequestQueue(getApplicationContext()).add(jsonObjectRequest);
    }
    private void initViews() {
        classspin = (Spinner) findViewById(R.id.classlist);;
        mContext = getApplicationContext();
        mActivity = ProfileActivity.this;
        UserName = (TextView)findViewById(R.id.UserName);
        fatherName = (TextView) findViewById(R.id.fatherName);
        contact = (TextView) findViewById(R.id.contact);
        medium = (TextView)findViewById(R.id.medium);
        className = findViewById(R.id.className);

        jsonObject = new JSONObject();
        dialog = new ProgressDialog(this);
        dialog.setMessage("Uploading Image...");
        dialog.setCancelable(false);
        mImageView = (ImageView) findViewById(R.id.profile_image);
        updateInfo = (Button) findViewById(R.id.updateinfo);
        //saveInfo = (Button) findViewById(R.id.saveinfo);
        if(user.getUserimage()!=null && !user.getUserimage().equalsIgnoreCase("")){
            Picasso.with(this).load(user.getUserimage()).into(mImageView);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0 && resultCode == RESULT_OK)
            CropImage();
        else if (requestCode == 2) {
            if (data != null) {
                mUri = data.getData();
                CropImage();
            }
        } else if (requestCode == 1) {
            if (data != null) {
                Bundle bundle = data.getExtras();
                Bitmap bitmap = bundle.getParcelable("data");
                mImageView.setImageBitmap(bitmap);
                uploadPic();
            }
        }
        if (requestCode == Utils.REQCODE && resultCode == RESULT_OK && data != null) {
            Uri selectedImageUri = data.getData();
            mImageView.setImageURI(selectedImageUri);
            Toast.makeText(this, ""+selectedImageUri, Toast.LENGTH_SHORT).show();
        }
    }

    private void CropImage() {
        try {
            mCropIntent = new Intent("com.android.camera.action.CROP");
            mCropIntent.setDataAndType(mUri, "image/*");
            mCropIntent.putExtra("crop", "true");
            mCropIntent.putExtra("outputX", 100);
            mCropIntent.putExtra("outputY", 100);
            mCropIntent.putExtra("aspectX", 4);
            mCropIntent.putExtra("aspectY", 4);
            mCropIntent.putExtra("scaleUpIfNeeded", true);
            mCropIntent.putExtra("return-data", true);

            startActivityForResult(mCropIntent, 1);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(getApplicationContext(), "Your device doesn't support the crop action!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case RequestPermissionCode: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
