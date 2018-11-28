package com.appteq.ad.appteq.actions.payment;

import com.appteq.ad.appteq.actions.APP_CONSTANT;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public class API {
    public interface Api {
        String BASE_URL = APP_CONSTANT.webservice_url;

        @FormUrlEncoded
        @POST("paymentCheckSumChecker")
        Call<Checksum> getChecksum(
                @Field("MID") String mId,
                @Field("ORDER_ID") String orderId,
                @Field("CUST_ID") String custId,
                @Field("CHANNEL_ID") String channelId,
                @Field("TXN_AMOUNT") String txnAmount,
                @Field("WEBSITE") String website,
                @Field("CALLBACK_URL") String callbackUrl,
                @Field("INDUSTRY_TYPE_ID") String industryTypeId
        );
    }
}