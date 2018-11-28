package com.appteq.ad.appteq;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.FrameLayout;
import com.appteq.ad.appteq.data.DBHandler;
import com.appteq.ad.appteq.model.PaymentModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import android.support.v7.widget.Toolbar;

public class PaymentHistory extends NavigationActivity {
    private static ExpandableListView expandableListView;
    private static ExpandableListAdapter adapter;
    private HashMap<String,List<String>> payments = null;
    private ArrayList<String> headers;
    private String headername;
    private Button button;
    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FrameLayout contentFrameLayout = (FrameLayout) findViewById(R.id.content_frame); //Remember this is the FrameLayout area within your activity_main.xml
        getLayoutInflater().inflate(R.layout.activity_payment_history, contentFrameLayout);
        final Toolbar toolbar = (android.support.v7.widget.Toolbar)findViewById(R.id.payment_history_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setTitle("Payment History");
        expandableListView = (ExpandableListView) findViewById(R.id.simple_expandable_listview);
        // Setting group indicator null for custom indicator
        expandableListView.setGroupIndicator(null);
        button = findViewById(R.id.morepay);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(PaymentHistory.this,PaymentActivity.class));
            }
        });
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
        setItems();
    }

    // Setting headers and childs to expandable listview
    void setItems() {
        String sql = "select * from "+DBHandler.PAYMENT_TABLE;
        payments = new LinkedHashMap<String,List<String>>();
        headers = new ArrayList<String>();
        Cursor c = mDbhelper.getAllData(sql);
        int tra_c = 0;
        try {
            Date paymentdate = null;
            String validate = "";
            if (c != null && c.getCount() > 0) {
                while (c.moveToNext()) {
                    ArrayList<String> child = new ArrayList<String>();
                    PaymentModel model = new PaymentModel();
                    model.setTransactionname("Transaction " + (tra_c + 1));
                    paymentdate = format.parse(c.getString(c.getColumnIndex(DBHandler.PAYMENT_DATE)));
                    validate = c.getString(c.getColumnIndex(DBHandler.NEXT_PAYMENT_DATE));
                    if(validate!=null && !validate.equalsIgnoreCase("") ){
                        validate = format.parse(c.getString(c.getColumnIndex(DBHandler.NEXT_PAYMENT_DATE))).toLocaleString();
                    }
                    child.add("Transaction Id: " + c.getString(c.getColumnIndex(DBHandler.PAYMENT_TRANSACTION)));
                    child.add("Bank transaction Id: " + c.getString(c.getColumnIndex(DBHandler.PAYMENT_BANK_TRANSACTION)));
                    child.add("Amount: " + c.getString(c.getColumnIndex(DBHandler.PAYMENT_AMOUNT)) + " " + c.getString(c.getColumnIndex(DBHandler.PAYMENT_CURRENEY)));
                    child.add("Payment Package: " + c.getString(c.getColumnIndex(DBHandler.PAYMENT_PACKAGE)));
                    child.add("Paid on: " + paymentdate.toLocaleString());
                    child.add("Valid upto : " + validate);
                    child.add("Payment mode : " + c.getString(c.getColumnIndex(DBHandler.PAYMENT_MODE)));
                    String paymentstatus = c.getString(c.getColumnIndex(DBHandler.PAYMENT_STATUS));
                    if (paymentstatus.equalsIgnoreCase("TXN_SUCCESS")) {
                        paymentstatus = "Successful";
                    } else {
                        paymentstatus = "Pending";
                    }
                    child.add("Payment status: " + paymentstatus);
                    headername = "Transaction " + (tra_c + 1);
                    payments.put(headername, child);
                    headers.add(headername);
                    tra_c++;
                }
            } else {
                ArrayList<String> child = new ArrayList<String>();
                child.add("No transaction");
                headername = "No payment";
                payments.put(headername, child);
                headers.add(headername);
            }


            adapter = new PaymentExpandableListAdapter(getApplicationContext(), headers, payments);

            // Setting adpater over expandablelistview
            expandableListView.setAdapter(adapter);
        }catch (Exception e){

        }
    }


}

