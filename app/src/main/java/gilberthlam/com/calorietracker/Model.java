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
        if(newDay(currentDate)){
            listOfDays.add(new Day());
        }
        user = new User();
    }



    public boolean newDay(Date currentDate){
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

    public Day getSelectedDay(Date targetDate){
        for(int i = 0; i < listOfDays.size(); i++){
            if(listOfDays.get(i).currentDate.equals(targetDate))
                return  listOfDays.get(i);
        }
        return null;
    }

    public Day getToday(){
        return listOfDays.get(listOfDays.size()-1);
    }


}
