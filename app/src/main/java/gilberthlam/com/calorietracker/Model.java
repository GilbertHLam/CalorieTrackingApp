package gilberthlam.com.calorietracker;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Gilbert Lam on 2017-07-17.
 */

public class Model implements Serializable{
    ArrayList<Day> listOfDays;
    Date currentDate;
    User user;
    public Model(){
        listOfDays = new ArrayList<>();
        currentDate = new Date();
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(currentDate);
        cal1.set(Calendar.HOUR_OF_DAY, 0);
        cal1.set(Calendar.MINUTE, 0);
        cal1.set(Calendar.SECOND, 0);
        cal1.set(Calendar.MILLISECOND, 0);
        currentDate = cal1.getTime();
        if(newDay()){
            listOfDays.add(new Day());
        }
        user = new User();
    }



    public boolean newDay(){
        if (listOfDays.size() == 0){
            listOfDays.add(new Day());
        }
        else if(!listOfDays.get(listOfDays.size()-1).currentDate.equals(this.currentDate)){
            return true;
        }
        return false;
    }

    public void addDay(){
        listOfDays.add(new Day());
    }

    public void addSetDay(Date setDay){
        listOfDays.add(new Day(setDay));
    }

    public Day getSelectedDay(Date targetDate){
        Log.d("compare",""+targetDate.toString());
        for(int i = 0; i < listOfDays.size(); i++){
            Log.d("day",""+listOfDays.get(i).currentDate);
            if(listOfDays.get(i).currentDate.compareTo(targetDate) == 0) {
                Log.d("FOUND", "" + listOfDays.get(i).currentDate);
                return listOfDays.get(i);
            }
        }
        return null;
    }


    public Day getToday(){
        return listOfDays.get(listOfDays.size()-1);
    }


}
