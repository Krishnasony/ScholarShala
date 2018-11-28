package com.appteq.ad.appteq.actions;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by Krishana Sony on 10-04-2018.
 */

public class RequestAdapt {
    private static  RequestAdapt minstance;
    private static Context context;
    private static RequestQueue requestQueue;

    private RequestAdapt(Context ctx){
        context = ctx;
        requestQueue = getRequestQue();
    }
    public RequestQueue getRequestQue(){
        if (requestQueue==null){
            requestQueue = Volley.newRequestQueue(context.getApplicationContext());
        }
        return requestQueue;

    }
    public  static synchronized RequestAdapt getInstance(Context context){
        if (minstance==null){
            minstance= new RequestAdapt(context);
        }
        return minstance;
    }
    public <T> void addToRequestQueue(Request<T> request){
        requestQueue.add(request);
    }

}

