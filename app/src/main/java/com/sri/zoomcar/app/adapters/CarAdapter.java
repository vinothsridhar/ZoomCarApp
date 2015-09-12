package com.sri.zoomcar.app.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.sri.zoomcar.app.R;
import com.sri.zoomcar.app.ZoomCarApp;
import com.sri.zoomcar.app.gson.Car;
import com.sri.zoomcar.app.views.ColoredRatingBar;
import com.sri.zoomcar.app.views.TextAwesome;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sridhar on 12/9/15.
 */
public class CarAdapter extends BaseAdapter {

    private List<Car> carList = new ArrayList<Car>();
    private Context context;

    public CarAdapter(Context c, List<Car> carList) {
        this.carList = carList;
        this.context = c;
    }

    @Override
    public int getCount() {
        return carList.size();
    }

    @Override
    public Car getItem(int position) {
        return carList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.list_item_car, null);
            holder = new ViewHolder();
            holder.carName = (TextAwesome) convertView.findViewById(R.id.car_name);
            holder.ratingBar = (ColoredRatingBar) convertView.findViewById(R.id.car_rating);
            holder.ratingText = (TextAwesome) convertView.findViewById(R.id.car_rating_text);
            holder.carAmount = (TextAwesome) convertView.findViewById(R.id.car_amount);
            holder.carImage = (ImageView) convertView.findViewById(R.id.car_image);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Car carItem = getItem(position);
        holder.carName.setText(carItem.name);
        holder.ratingBar.setRating(carItem.rating);
        holder.ratingText.setText(carItem.rating + "");
        holder.carAmount.setText(carItem.hourly_rate + "/hr");
        ZoomCarApp.getImageLoader(context).DisplayImage(carItem.image, holder.carImage, true);

        return convertView;
    }

    static class ViewHolder {
        public TextAwesome carName;
        public ColoredRatingBar ratingBar;
        public TextAwesome carAmount;
        public TextAwesome ratingText;
        public ImageView carImage;
    }
}
