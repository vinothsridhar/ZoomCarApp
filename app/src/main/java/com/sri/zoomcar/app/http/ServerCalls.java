package com.sri.zoomcar.app.http;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.okhttp.OkHttpClient;
import com.sri.zoomcar.app.listeners.RequestHandler;
import com.sri.zoomcar.app.utils.L;
import com.sri.zoomcar.app.utils.LocalConfig;

import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by sridhar on 12/9/15.
 */
public class ServerCalls {

    //Server configs
    public static final String API_ENDPOINT = "http://zoomcar.0x10.info/api/zoomcar?type=json";
    public static final String QUERY_LIST_CARS = "query=list_cars";
    public static final String QUERY_API_HITS = "query=api_hits";

    //Apis
    public static final String API_LIST_CARS = API_ENDPOINT + "&" + QUERY_LIST_CARS;
    public static final String API_API_HITS = API_ENDPOINT + "&" + QUERY_API_HITS;

    private static RequestQueue restClient = null;

    private ServerCalls() {}

    public static RequestQueue getInstance(Context c) {
        if (restClient == null) {
            OkHttpClient client = new OkHttpClient();
            client.setConnectTimeout(LocalConfig.TIMEOUT, TimeUnit.MILLISECONDS);
            restClient = Volley.newRequestQueue(c.getApplicationContext(), new OkHttpStack(client));
        }
        return restClient;
    }

    public static Request getRequest(String url, RequestHandler handler) {
        return request(Request.Method.GET, url, handler, null);
    }

    public static Request postRequest(String url, RequestHandler handler, Map<String, String> params) {
        return request(Request.Method.POST, url, handler, params);
    }

    public static Request request(int method, final String url, final RequestHandler handler, final Map<String, String> params) {
        return new StringRequest(method, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    handler.onSuccess(response);
                    handler.onFinish();
                } catch (Exception e) {
                    L.d("Response Error | url = " + url, e);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                L.d("Response Error | url = " + url, error);
                handler.onFailure(error);
                handler.onFinish();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return params;
            }
        };
    }

}
