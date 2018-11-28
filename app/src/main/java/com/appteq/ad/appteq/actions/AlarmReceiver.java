package com.appteq.ad.appteq.actions;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

/**
 * Created by sonu on 09/04/17.
 */

public class AlarmReceiver extends BroadcastReceiver {
    public static final int REQUEST_CODE = 12345;
    private GetTestData getdata = new GetTestData();
    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context,"Loading test data",Toast.LENGTH_SHORT).show();
        getdata.fetchTestData(context);
    }

}




