package com.sri.zoomcar.app;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.android.volley.RequestQueue;
import com.sri.zoomcar.app.http.ServerCalls;
import com.sri.zoomcar.app.utils.L;
import com.sri.zoomcar.app.utils.ViewUtils;

/**
 * Created by sridhar on 12/9/15.
 */
public class BaseActivity extends AppCompatActivity {

    protected final String TAG = this.getClass().getSimpleName();
    protected RequestQueue restClient;

    //Views
    protected LinearLayout rootLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_base);
        init();
    }

    private void init() {
        rootLayout = (LinearLayout) findViewById(R.id.root);
        ViewUtils.hideSoftKeyboard(this);

        restClient = ServerCalls.getInstance(this);
    }

    @Override
    public void setContentView(int layoutResID) {
        View v = View.inflate(this, layoutResID, null);
        rootLayout.addView(v);
    }

    @Override
    protected void onDestroy() {
        restClient.cancelAll(TAG);
        super.onDestroy();
        L.d(TAG, "Destroying Activity");
    }

    @Override
    protected void onResume() {
        super.onResume();
        L.d(TAG, "Resuming Activity");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
