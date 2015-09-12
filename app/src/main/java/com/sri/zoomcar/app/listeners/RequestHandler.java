package com.sri.zoomcar.app.listeners;

import org.json.JSONException;

/**
 * Created by sridhar on 9/8/15.
 */
public interface RequestHandler {
    public void onSuccess(Object response) throws JSONException;
    public void onFailure(Exception e);
    public void onFinish();
}
