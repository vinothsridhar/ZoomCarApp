package com.sri.zoomcar.app;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.android.volley.Request;
import com.google.gson.Gson;
import com.sri.zoomcar.app.adapters.CarAdapter;
import com.sri.zoomcar.app.gson.ApiHits;
import com.sri.zoomcar.app.gson.Car;
import com.sri.zoomcar.app.gson.ListCars;
import com.sri.zoomcar.app.http.ServerCalls;
import com.sri.zoomcar.app.listeners.RequestHandler;
import com.sri.zoomcar.app.utils.LocalConfig;
import com.sri.zoomcar.app.views.TextAwesome;

import java.util.Collections;

/**
 * Created by sridhar on 12/9/15.
 */
public class MainActivity extends BaseActivity {

    private ListCars carsList;
    private boolean apiCalled = false;

    //views
    private ProgressBar progressBar;
    private LinearLayout networkErrorLayout;
    private RelativeLayout mainLayout;
    private ListView listView;
    private TextAwesome totalCars;
    private TextAwesome totalHits;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);

        //FIXFIX Searchview not working with min sdk 10;
        MenuItem searchItem = menu.findItem(R.id.search);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        final SearchView searchView = (SearchView) searchItem.getActionView();
        if (searchView != null) {
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    return false;
                }
            });

            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.HONEYCOMB_MR1) {
                View.OnAttachStateChangeListener onAttachStateChangeListener = new View.OnAttachStateChangeListener() {
                    @Override
                    public void onViewAttachedToWindow(View v) {
                        // Do Nothing
                    }

                    @Override
                    public void onViewDetachedFromWindow(View v) {
                        listView.setVisibility(View.GONE);
                        searchView.setQuery("", false);
//                        showProductList();
                    }
                };
                searchView.addOnAttachStateChangeListener(onAttachStateChangeListener);
            } else {
                // TODO: Handle this for older versions
            }
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (carsList == null) {
            return false;
        }

        switch (item.getItemId()) {
            case R.id.sort_by_price:
                Car.setSort(Car.CarSort.PRICE);
                Collections.sort(carsList.cars);
                ((CarAdapter) listView.getAdapter()).notifyDataSetChanged();
                break;
            case R.id.sort_by_rating:
                Car.setSort(Car.CarSort.RATING);
                Collections.sort(carsList.cars);
                ((CarAdapter) listView.getAdapter()).notifyDataSetChanged();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initUI();
        initComponents();
    }

    private void initUI() {
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        networkErrorLayout = (LinearLayout) findViewById(R.id.networkErrorLayout);
        mainLayout = (RelativeLayout) findViewById(R.id.main);
        listView = (ListView) findViewById(R.id.listView);
        totalCars = (TextAwesome) findViewById(R.id.total_cars);
        totalHits = (TextAwesome) findViewById(R.id.total_hits);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (carsList == null && !apiCalled) {
            getCarsList();
        } else {
            populateList();
        }
        getTotalHits();
    }

    private void initComponents() {

    }

    private void populateList() {
        if (carsList != null) {
            CarAdapter adapter = new CarAdapter(this, carsList.cars);
            listView.setAdapter(adapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    ZoomCarApp.setCurrentCar(carsList.cars.get(position));
                    Intent i = new Intent(MainActivity.this, BookingDetailActivity.class);
                    startActivity(i);
                }
            });
            mainLayout.setVisibility(View.VISIBLE);
            totalCars.setText(String.format(getString(R.string.total_cars), carsList.cars.size()));
        }
    }

    private void getCarsList() {
        if (LocalConfig.DEBUG) {
            String response = "{\"cars\":[{\"name\":\"Ciaz Hybrid\",\"image\":\"http:\\/\\/imgd1.aeplcdn.com\\/310x174\\/cw\\/ec\\/19824\\/Maruti-Suzuki-Ciaz-Hybrid-Right-Front-Three-Quarter-56021.jpg?wm=0\",\"type\":\"Sedan\",\"hourly_rate\":\"100\",\"rating\":\"4\",\"seater\":\"4\",\"ac\":\"1\",\"location\":{\"latitude\":\"18.520430\",\"longitude\":\"73.856744\"}},{\"name\":\"Ford Figo Aspire\",\"image\":\"http:\\/\\/imgd2.aeplcdn.com\\/310x174\\/cw\\/cars\\/ford\\/ford-figo-aspire.jpg\",\"type\":\"Sedan\",\"hourly_rate\":\"70\",\"rating\":\"3.8\",\"seater\":\"4\",\"ac\":\"1\",\"location\":{\"latitude\":\"19.075984\",\"longitude\":\"72.877656\"}},{\"name\":\"Honda Jazz\",\"image\":\"http:\\/\\/imgd2.aeplcdn.com\\/310x174\\/cw\\/cars\\/honda\\/jazz.jpg\",\"type\":\"Hatchback\",\"hourly_rate\":\"72\",\"rating\":\"3.0\",\"seater\":\"3\",\"ac\":\"1\",\"location\":{\"latitude\":\"17.385044\",\"longitude\":\"78.486671\"}},{\"name\":\"Elite i20\",\"image\":\"http:\\/\\/imgd2.aeplcdn.com\\/310x174\\/cw\\/cars\\/hyundai\\/elite-i20.jpg\",\"type\":\"Hatchback\",\"hourly_rate\":\"100\",\"rating\":\"4.2\",\"seater\":\"4\",\"ac\":\"1\",\"location\":{\"latitude\":\"13.082680\",\"longitude\":\"80.270718\"}},{\"name\":\"Suzuki Celerio\",\"image\":\"http:\\/\\/imgd2.aeplcdn.com\\/310x174\\/cw\\/cars\\/maruti-suzuki\\/celerio.jpg\",\"type\":\"Hatchback\",\"hourly_rate\":\"62\",\"rating\":\"3.9\",\"seater\":\"4\",\"ac\":\"1\",\"location\":{\"latitude\":\"12.914142\",\"longitude\":\"74.855957\"}},{\"name\":\"Tata Nano\",\"image\":\"http:\\/\\/imgd3.aeplcdn.com\\/310x174\\/cw\\/cars\\/tata\\/nano.jpg\",\"type\":\"Hatchback\",\"hourly_rate\":\"45\",\"rating\":\"3\",\"seater\":\"2\",\"ac\":\"0\",\"location\":{\"latitude\":\"25.594095\",\"longitude\":\"85.137565\"}},{\"name\":\"Swift Dzire\",\"image\":\"http:\\/\\/imgd2.aeplcdn.com\\/310x174\\/cw\\/cars\\/maruti-suzuki\\/swift-dzire.jpg\",\"type\":\"Sedan\",\"hourly_rate\":\"65\",\"rating\":\"3.7\",\"seater\":\"4\",\"ac\":\"1\",\"location\":{\"latitude\":\"24.663717\",\"longitude\":\"93.906269\"}},{\"name\":\"Mercedes E-Class\",\"image\":\"http:\\/\\/imgd3.aeplcdn.com\\/310x174\\/cw\\/cars\\/mercedese\\/e-classs.jpg\",\"type\":\"Executive\",\"hourly_rate\":\"1360\",\"rating\":\"5\",\"seater\":\"5\",\"ac\":\"1\",\"location\":{\"latitude\":\"23.022505\",\"longitude\":\"72.571362\"}},{\"name\":\"Honda Amaze\",\"image\":\"http:\\/\\/imgd1.aeplcdn.com\\/310x174\\/cw\\/cars\\/honda\\/amaze.jpg\",\"type\":\"Sedan\",\"hourly_rate\":\"70\",\"rating\":\"4\",\"seater\":\"4\",\"ac\":\"1\",\"location\":{\"latitude\":\"29.380304\",\"longitude\":\"79.463566\"}},{\"name\":\"Tata Safari\",\"image\":\"http:\\/\\/imgd5.aeplcdn.com\\/310x174\\/ec\\/C2\\/15\\/10757\\/img\\/m\\/Tata-Safari-Right-Front-Three-Quarter-48881_ol.jpg\",\"type\":\"SUV\",\"hourly_rate\":\"140\",\"rating\":\"4.2\",\"seater\":\"6\",\"ac\":\"1\",\"location\":{\"latitude\":\"22.572646\",\"longitude\":\"88.363895\"}},{\"name\":\"Toyota Fortuner\",\"image\":\"http:\\/\\/imgd2.aeplcdn.com\\/310x174\\/cw\\/cars\\/toyota\\/fortuner.jpg\",\"type\":\"SUV\",\"hourly_rate\":\"614\",\"rating\":\"5\",\"seater\":\"7\",\"ac\":\"1\",\"location\":{\"latitude\":\"12.971599\",\"longitude\":\"77.594563\"}}]}";
            carsList = new Gson().fromJson(response, ListCars.class);
            populateList();
            return;
        }
        apiCalled = true;
        progressBar.setVisibility(View.VISIBLE);
        networkErrorLayout.setVisibility(View.GONE);
        mainLayout.setVisibility(View.GONE);
        Request listCars = ServerCalls.getRequest(ServerCalls.API_LIST_CARS, new RequestHandler() {
            @Override
            public void onFinish() {
                apiCalled = false;
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onSuccess(Object response) {
                Log.d(TAG, "response: " + response.toString());
                carsList = new Gson().fromJson(response.toString(), ListCars.class);
                populateList();
                Log.d(TAG, "Total cars: " + carsList.cars.size());
            }

            @Override
            public void onFailure(Exception e) {
                apiCalled = false;
                networkErrorLayout.setVisibility(View.VISIBLE);
            }
        });
        listCars.setTag(TAG);
        restClient.add(listCars);
    }

    private void getTotalHits() {
        final Request totalHits = ServerCalls.getRequest(ServerCalls.API_API_HITS, new RequestHandler() {
            @Override
            public void onSuccess(Object response) {
                ApiHits hits = new Gson().fromJson(response.toString(), ApiHits.class);
                setApiHits(hits.api_hits);
            }

            @Override
            public void onFailure(Exception e) {
                //Do nothing
            }

            @Override
            public void onFinish() {

            }
        });
        totalHits.setTag(TAG);
        restClient.add(totalHits);
    }

    public void retryClick(View v) {
        Log.d(TAG, "retry clicked");
        getCarsList();
    }

    private void setApiHits(int hits) {
        totalHits.setText(String.format(getString(R.string.total_hits), hits));
    }

}
