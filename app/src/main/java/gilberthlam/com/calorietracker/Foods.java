package gilberthlam.com.calorietracker;

import java.io.Serializable;

/**
 * Created by Gilbert Lam on 2017-07-16.
 */

public class Foods implements Serializable{
    private int calorie;
    private String name;
    public Foods(String name, int calorie) {
        this.name = name;
        this.calorie = calorie;
    }
    public String getName(){
        return name;
    }
    public int getCalorie(){
        return calorie;
    }
    public void setName(String name){
        this.name = name;
    }
    public void setCalorie(int calorie){
        this.calorie = calorie;
    }
    public String toString(){
        return name;
    }
}
