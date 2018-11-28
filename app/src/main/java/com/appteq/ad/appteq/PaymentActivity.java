package com.appteq.ad.appteq;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.GravityCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.appteq.ad.appteq.actions.APP_CONSTANT;
import com.appteq.ad.appteq.actions.RequestAdapt;
import com.appteq.ad.appteq.actions.payment.API;
import com.appteq.ad.appteq.actions.payment.Checksum;
import com.appteq.ad.appteq.actions.payment.Paytm;
import com.appteq.ad.appteq.data.DBHandler;
import com.appteq.ad.appteq.model.ClassModel;
import com.appteq.ad.appteq.model.Constants;
import com.appteq.ad.appteq.model.PaymentModel;
import com.appteq.ad.appteq.model.PaymentPackage;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.paytm.pgsdk.Log;
import com.paytm.pgsdk.PaytmOrder;
import com.paytm.pgsdk.PaytmPGService;
import com.paytm.pgsdk.PaytmPaymentTransactionCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

//implementing PaytmPaymentTransactionCallback to track the payment result.
public class PaymentActivity extends NavigationActivity implements PaytmPaymentTransactionCallback {

    private static final String TAG = "Main";
    private HashMap<Integer,PaymentPackage> packages = new LinkedHashMap<Integer, PaymentPackage>();
    private ArrayList<Integer> package_id = new ArrayList<Integer>();
    private ArrayList<String> packagename = new ArrayList<String>();
    private PaymentPackage selected_package;
    //the textview in the interface where we have the price
    TextView textprice;
    public static EditText editTextprice;
    private int user_id;
    private String validdate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String url = APP_CONSTANT.webservice_url+"getpaymentpackage";
        FrameLayout contentFrameLayout = (FrameLayout) findViewById(R.id.content_frame); //Remember this is the FrameLayout area within your activity_main.xml
        getLayoutInflater().inflate(R.layout.activity_main, contentFrameLayout);
        final Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        CollapsingToolbarLayout collapsingToolbar = findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle("MAKE PAYMENT");
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
        packagename.add("Select package");
        func.setProgressMessage("Loading payment packages");
        user_id = user.getUserid();
        func.showProgressDialog();
        editTextprice = findViewById(R.id.amount);
        editTextprice.setClickable(false);
        editTextprice.setEnabled(false);
        final MaterialSpinner spinner = (MaterialSpinner) findViewById(R.id.spinner);
        final Button paybutton = findViewById(R.id.buttonBuy);
        paybutton.setVisibility(View.INVISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JSONArray jsonArray = null;
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    Iterator<String> keys = jsonObject.keys();
                    while(keys.hasNext()){
                        String key = keys.next();
                        if(key.equalsIgnoreCase("err")){
                            func.showErrors(PaymentActivity.this,jsonObject.getString(key),"Error message");
                            func.dismissProgressDialog();
                            break;
                        }else{
                            if(key.equalsIgnoreCase("packages")){
                                JSONArray tpackages = jsonObject.getJSONArray("packages");
                                for(int i=0;i<tpackages.length();i++){
                                    JSONObject jpackage = (JSONObject) tpackages.get(i);
                                    PaymentPackage ppackage = new PaymentPackage();
                                    int tpackage_id = jpackage.getInt("ID");
                                    String package_name = jpackage.getString("package_name");
                                    ppackage.setPackage_id(tpackage_id);
                                    ppackage.setPackage_name(package_name);
                                    ppackage.setAmount(jpackage.getDouble("amount"));
                                    ppackage.setDuration(jpackage.getInt("package_duration"));
                                    packages.put(tpackage_id,ppackage);
                                    package_id.add(tpackage_id);
                                    Toast.makeText(PaymentActivity.this,package_name,Toast.LENGTH_SHORT).show();
                                    packagename.add(package_name);
                                }
                            }
                        }

                    }
                    spinner.setItems(packagename);
                    paybutton.setVisibility(View.VISIBLE);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                finally {
                    func.dismissProgressDialog();
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                func.dismissProgressDialog();
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
       spinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {

            @Override public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                if(position == 0){
                    paybutton.setEnabled(false);
                }else{
                    paybutton.setEnabled(true);
                    int tpackage_id = package_id.get(position-1);
                    PaymentPackage model = packages.get(tpackage_id);
                    editTextprice.setText(model.getAmount()+"");
                    selected_package = model;
                }
            }
        });
        findViewById(R.id.buttonBuy).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                generateCheckSum();
            }
        });

    }

    private void generateCheckSum() {
        String txnAmount = editTextprice.getText().toString().trim();
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(100, TimeUnit.SECONDS)
                .readTimeout(100,TimeUnit.SECONDS).build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API.Api.BASE_URL).client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        API.Api apiService = retrofit.create(API.Api.class);

        final Paytm paytm = new Paytm(
                Constants.M_ID,
                Constants.CHANNEL_ID,
                txnAmount,
                Constants.WEBSITE,
                Constants.CALLBACK_URL,
                Constants.INDUSTRY_TYPE_ID
        );

        //creating a call object from the apiService
        Call<Checksum> call = apiService.getChecksum(
                paytm.getmId(),
                paytm.getOrderId(),
                paytm.getCustId(),
                paytm.getChannelId(),
                paytm.getTxnAmount(),
                paytm.getWebsite(),
                paytm.getCallBackUrl(),
                paytm.getIndustryTypeId()
        );

        //making the call to generate checksum
        call.enqueue(new Callback<Checksum>() {
            @Override
            public void onResponse(Call<Checksum> call, Response<Checksum> response) {
                Toast.makeText(PaymentActivity.this,response.message(), Toast.LENGTH_SHORT).show();
                Toast.makeText(PaymentActivity.this, "no error", Toast.LENGTH_SHORT).show();

                //once we get the checksum we will initiailize the payment.
                //the method is taking the checksum we got and the paytm object as the parameter
                initializePaytmPayment(response.body().getChecksumHash(), paytm);
            }

            @Override
            public void onFailure(Call<Checksum> call, Throwable t) {
                Toast.makeText(PaymentActivity.this, "error", Toast.LENGTH_SHORT).show();
                Toast.makeText(PaymentActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d("I'm getting error",t.getMessage()
                );

            }
        });
    }

    private void initializePaytmPayment(String checksumHash, Paytm paytm) {

        //getting paytm service
        PaytmPGService Service = PaytmPGService.getStagingService();

        //use this when using for production
        //PaytmPGService Service = PaytmPGService.getProductionService();

        //creating a hashmap and adding all the values required
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("MID", Constants.M_ID);
        paramMap.put("ORDER_ID", paytm.getOrderId());
        paramMap.put("CUST_ID", paytm.getCustId());
        paramMap.put("CHANNEL_ID", paytm.getChannelId());
        paramMap.put("TXN_AMOUNT", paytm.getTxnAmount());
        paramMap.put("WEBSITE", paytm.getWebsite());
        paramMap.put("CALLBACK_URL", paytm.getCallBackUrl());
        paramMap.put("CHECKSUMHASH", checksumHash);
        paramMap.put("INDUSTRY_TYPE_ID", paytm.getIndustryTypeId());


        //creating a paytm order object using the hashmap
        PaytmOrder order = new PaytmOrder(paramMap);

        //intializing the paytm service
        Service.initialize(order, null);

        //finally starting the payment transaction
        Service.startPaymentTransaction(this, true, true, this);

    }

    //all these overriden method is to detect the payment result accordingly
    @Override
    public void onTransactionResponse(Bundle bundle) {
        try{
            final String paymentstatus = bundle.getString("STATUS");
            if(paymentstatus.equalsIgnoreCase("TXN_SUCCESS") || paymentstatus.equalsIgnoreCase("PENDING")){
                func.setProgressMessage("We are validating the your payment");
                func.showProgressDialog();
                final PaymentModel model = new PaymentModel();
                model.setUser_id(user_id);
                final String txndate = bundle.getString("TXNDATE");
                final String txnid = bundle.getString("TXNID");
                final String banktxnid = bundle.getString("BANKTXNID");
                final String amount = bundle.getString("TXNAMOUNT","0.0");
                final String currency = bundle.getString("CURRENCY");
                final String mode = bundle.getString("GATEWAYNAME");
                model.setPayment_date(txndate);
                model.setPayment_txn(txnid);
                model.setPayment_bank_txn(banktxnid);
                model.setAmount((amount));
                model.setCurrency(currency);
                model.setPayment_mode(mode);
                model.setStatus(paymentstatus);
                model.setPayment_package(selected_package.getPackage_name());
                SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                if(paymentstatus.equalsIgnoreCase("TXN_SUCCESS")){
                    Calendar calendar = Calendar.getInstance();
                    calendar.add(Calendar.DAY_OF_YEAR, selected_package.getDuration());
                    validdate = dateformat.format(calendar.getTime());
                    model.setPayment_valid_date(validdate);
                }
                String serverurl = APP_CONSTANT.webservice_url+"setuserpaymentdata";
                StringRequest stringRequest = new StringRequest(Request.Method.POST, serverurl, new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            Iterator<String> keys = jsonObject.keys();
                            while(keys.hasNext()){
                                String key = keys.next();
                                if(key.equalsIgnoreCase("err")){
                                    func.showErrors(PaymentActivity.this,jsonObject.getString("message"), getResources().getString(R.string.paymenterr));
                                    break;
                                }else if(key.equalsIgnoreCase("success")){
                                    mDbhelper.insertPayments(model);
                                    Toast.makeText(PaymentActivity.this,getResources().getString(R.string.paymentsuccess),Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(PaymentActivity.this,PaymentHistory.class));
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        finally {
                            func.dismissProgressDialog();
                            if(paymentstatus.equalsIgnoreCase("pending")){
                                func.showErrors(PaymentActivity.this,getResources().getString(R.string.paymentpending),getResources().getString(R.string.paymenterr));
                            }
                        }
                    }
                }, new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        func.dismissProgressDialog();
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("user_id",user_id+"");
                        params.put("txnid",txnid);
                        params.put("paymentdate",txndate);
                        params.put("banktxn",banktxnid);
                        params.put("amount",amount+"");
                        params.put("currency",currency);
                        params.put("mode",mode);
                        params.put("package_id",selected_package.getPackage_id()+"");
                        params.put("status",paymentstatus);
                        params.put("logintoken",user.getLogin_token());
                        if(validdate!="null"){
                            params.put("validdate",validdate);
                        }
                        return params;
                    }
                };
                stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                        50000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                ));
                RequestAdapt.getInstance(PaymentActivity.this).addToRequestQueue(stringRequest);

            }else{
                Toast.makeText(PaymentActivity.this,"It seems that your payment is failed",Toast.LENGTH_SHORT).show();
            }
        }
        catch (Exception e){
          Toast.makeText(PaymentActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
          e.printStackTrace();
        }
        //startActivity(new Intent(PaymentActivity.this,HomeActivity.class));
    }

    @Override
    public void networkNotAvailable() {
        Toast.makeText(this, "Network error", Toast.LENGTH_LONG).show();
    }

    @Override
    public void clientAuthenticationFailed(String s) {
        Toast.makeText(this, s, Toast.LENGTH_LONG).show();
    }

    @Override
    public void someUIErrorOccurred(String s) {
        Toast.makeText(this, s, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onErrorLoadingWebPage(int i, String s, String s1) {
        Toast.makeText(this, s, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onBackPressedCancelTransaction() {
        Toast.makeText(this, "Back Pressed", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onTransactionCancel(String s, Bundle bundle) {
        Toast.makeText(this, s + bundle.toString(), Toast.LENGTH_LONG).show();
    }
}
