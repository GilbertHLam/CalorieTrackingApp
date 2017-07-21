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
        format = new SimpleDateFormat("MMMM dd yyyy");
        listOfFoods = new ArrayList<>();
        Log.d("done", "shit");
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
