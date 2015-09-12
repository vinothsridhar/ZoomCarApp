package com.sri.zoomcar.app.gson;

/**
 * Created by sridhar on 12/9/15.
 */
public class Car implements Comparable<Car> {

    public enum CarSort {
        NONE, PRICE, RATING;
    };

    public String name;
    public String image;
    public String type;
    public int hourly_rate;
    public float rating;
    public int seater;
    public int ac;
    public CarLocation location;
    public static CarSort sortMethod;

    public static void setSort(CarSort sort) {
        sortMethod = sort;
    }

    @Override
    public int compareTo(Car another) {
        if (sortMethod == CarSort.RATING) {
            if (this.rating > another.rating) {
                return -1;
            }
            if (this.rating < another.rating) {
                return 1;
            }
            return 0;
        } else if (sortMethod == CarSort.PRICE) {
            if (this.hourly_rate > another.hourly_rate) {
                return -1;
            }
            if (this.hourly_rate < another.hourly_rate) {
                return 1;
            }
            return 0;
        } else {
            return 0;
        }
    }
}
