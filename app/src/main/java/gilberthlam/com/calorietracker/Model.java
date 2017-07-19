package gilberthlam.com.calorietracker;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Gilbert Lam on 2017-07-17.
 */

public class Model {
    ArrayList<Day> listOfDays;
    Date currentDate;
    public Model(){
        listOfDays = new ArrayList<>();
        currentDate = new Date();
        if(newDay(currentDate)){
            listOfDays.add(new Day());
        }
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