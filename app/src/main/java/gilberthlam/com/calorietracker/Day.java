package gilberthlam.com.calorietracker;

import android.content.Context;
import android.util.Log;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.SimpleTimeZone;

/**
 * Created by Gilbert Lam on 2017-07-16.
 */

public class Day implements Serializable{
    Date currentDate;
    SimpleDateFormat format;
    ArrayList<Foods> listOfFoods = new ArrayList<>();

    public Day() {
        currentDate = new Date();
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(currentDate);
        cal1.set(Calendar.HOUR_OF_DAY, 0);
        cal1.set(Calendar.MINUTE, 0);
        cal1.set(Calendar.SECOND, 0);
        cal1.set(Calendar.MILLISECOND, 0);
        currentDate = cal1.getTime();
        format = new SimpleDateFormat("MMMM dd yyyy");
        listOfFoods = new ArrayList<>();
    }
    public Day(Date setDay) {
        currentDate = setDay;
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(currentDate);
        cal1.set(Calendar.HOUR_OF_DAY, 0);
        cal1.set(Calendar.MINUTE, 0);
        cal1.set(Calendar.SECOND, 0);
        cal1.set(Calendar.MILLISECOND, 0);
        currentDate = cal1.getTime();
        format = new SimpleDateFormat("MMMM dd yyyy");
        listOfFoods = new ArrayList<>();
    }
    public String getDate(){
        return(format.format(currentDate));
    }

    public int calculateTotalCalories(){
        int totalCal = 0;
        for(int i = 0; i < listOfFoods.size(); i++){
            totalCal += listOfFoods.get(i).getCalorie();
        }
        return totalCal;
    }
    public  ArrayList<Foods> getListOfFoods(){
        return listOfFoods;
    }

    public void addFood(Foods item){
        listOfFoods.add(item);
    }

    public boolean equals(Object otherDay){
        return((Day)otherDay).getDate().equals(getDate());
    }










}
