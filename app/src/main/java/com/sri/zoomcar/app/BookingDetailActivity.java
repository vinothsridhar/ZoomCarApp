package com.sri.zoomcar.app;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.sri.zoomcar.app.gson.Car;
import com.sri.zoomcar.app.views.ColoredRatingBar;
import com.sri.zoomcar.app.views.TextAwesome;

/**
 * Created by sridhar on 12/9/15.
 */
public class BookingDetailActivity extends BaseActivity {

    private Car currentCar = null;

    //Views
    private TextAwesome carName;
    private TextAwesome carAmount;
    private TextAwesome carRatingText;
    private TextAwesome carSeater;
    private TextAwesome carAC;
    private ColoredRatingBar carRating;
    private ImageView carImage;
    private GoogleMap googleMap;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.details_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.share_app:
                shareAppClicked();
                break;
            case R.id.share_sms:
                shareSmsClicked();
                break;
            case R.id.share_location:
                shareLocationClicked();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_detail);
        initUI();
        initComponents();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (ZoomCarApp.getCurrentCar() == null) {
            finish();
            return;
        }

        currentCar = ZoomCarApp.getCurrentCar();
        setCarDetail();
    }

    private void initUI() {
        carName = (TextAwesome) findViewById(R.id.car_details_name);
        carAmount = (TextAwesome) findViewById(R.id.car_amount);
        carRatingText = (TextAwesome) findViewById(R.id.car_rating_text);
        carRating = (ColoredRatingBar) findViewById(R.id.car_rating);
        carImage = (ImageView) findViewById(R.id.car_image);
        carSeater = (TextAwesome) findViewById(R.id.car_seater);
        carAC = (TextAwesome) findViewById(R.id.car_ac);
    }

    private void initComponents() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ZoomCarApp.setCurrentCar(null);
    }

    private void setCarDetail() {
        if (currentCar != null) {
            carName.setText(currentCar.name);
            carAmount.setText(currentCar.hourly_rate + "/hr");
            carRatingText.setText(currentCar.rating + "");
            carRating.setRating(currentCar.rating);
            carSeater.setText("Seater: " + (currentCar.seater - 1) + " + " + "1");
            boolean ac = currentCar.ac == 1;
            carAC.setText("AC: " + (ac ? "Yes" : "No"));
            ZoomCarApp.getImageLoader(this).DisplayImage(currentCar.image, carImage, true);
        }
    }

    private void initializeMap() {
        if (googleMap == null) {
            googleMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
            if (googleMap == null) {
                Toast.makeText(getApplicationContext(), "Sorry! unable to create maps", Toast.LENGTH_SHORT).show();
            } else {
                LatLng carLocation = new LatLng(currentCar.location.latitude, currentCar.location.longitude);
                Marker location = googleMap.addMarker(new MarkerOptions().position(carLocation).title("Car Location"));
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(carLocation, 15));
                googleMap.animateCamera(CameraUpdateFactory.zoomTo(10), 2000, null);
            }
        }
    }

    private void shareAppClicked() {
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, currentCar.name + " | Booking");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, getShareBody());
        startActivity(Intent.createChooser(sharingIntent, "Share via"));
    }

    private void shareSmsClicked() {
        Intent sendIntent = new Intent(Intent.ACTION_VIEW);
        sendIntent.setData(Uri.parse("sms:"));
        sendIntent.putExtra("sms_body", getShareBody());
        startActivity(sendIntent);
    }

    private void shareLocationClicked() {
        String uri = "geo:" + currentCar.location.latitude + "," + currentCar.location.longitude
                    + "?q=" + currentCar.location.latitude + "," + currentCar.location.longitude;
        startActivity(new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(uri)));
    }

    private String getShareBody() {
        return "Hey Buddy, I have shared car " + currentCar.name + " to you. " +
                " Price: " + currentCar.hourly_rate + "/hr" +
                " | Seater: " + currentCar.seater +
                " | AC: " + currentCar.ac;
    }
}
