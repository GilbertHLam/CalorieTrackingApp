package gilberthlam.com.calorietracker;

import android.content.Intent;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

public class Profile extends FragmentActivity {
    User user = MainActivity.model.user;
    int age, weight, height,goal,delta;
    float exercise;
    boolean isMale;
    EditText ageInput, weightInput, heightInput, goalInput;
    RadioButton maleInput, femaleInput, gainButton, loseButton, stayButton;
    Spinner exerciseInput;
    Button saveButton, suggestButton;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        heightInput = (EditText) findViewById(R.id.heightInput);
        ageInput = (EditText) findViewById(R.id.ageInput);
        weightInput = (EditText) findViewById(R.id.weightInput);
        goalInput = (EditText) findViewById(R.id.calInput);
        maleInput = (RadioButton) findViewById(R.id.maleButton);
        femaleInput = (RadioButton) findViewById(R.id.femaleButton);
        exerciseInput = (Spinner) findViewById(R.id.exerciseInput);
        saveButton = (Button) findViewById(R.id.saveButton);
        suggestButton = (Button) findViewById(R.id.suggestButton);
        gainButton = (RadioButton) findViewById(R.id.gainButton);
        stayButton = (RadioButton) findViewById(R.id.stayButton);
        loseButton = (RadioButton) findViewById(R.id.loseButton);
        load();
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                save();
            }
        });

        suggestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                suggest();
            }
        });


    }

    public void suggest(){
        try {
            age = Integer.parseInt(ageInput.getText().toString());
            weight = Integer.parseInt(weightInput.getText().toString());
            height = Integer.parseInt(heightInput.getText().toString());
            if (exerciseInput.getSelectedItemPosition() == 0) {
                exercise = 1.2f;
            } else if (exerciseInput.getSelectedItemPosition() == 1) {
                exercise = 1.375f;
            } else if (exerciseInput.getSelectedItemPosition() == 2) {
                exercise = 1.55f;
            } else if (exerciseInput.getSelectedItemPosition() == 3) {
                exercise = 1.725f;
            } else {
                exercise = 1.9f;
            }
            if (maleInput.isChecked() && !femaleInput.isChecked()) {
                isMale = true;
            } else {
                isMale = false;
            }
            if(gainButton.isChecked()){
                delta = 500;
            }
            else if(stayButton.isChecked()){
                delta = 0;
            }
            else {
                delta = -500;
            }
            if(isMale) {
                goal = (int) Math.round(((13.397 * weight) + (4.799 * height) - (5.677 * age) + 88.362 + delta)*exercise);
            }
            else {
                goal = (int) Math.round(((9.247 * weight) + (3.098 * height) - (4.330 * age) + 447.593 + delta)*exercise);
            }
            goalInput.setText(""+goal);
        } catch(NumberFormatException e) {
            Toast t = Toast.makeText(getApplicationContext(),
                    "Sorry! Please make sure all fields are filled out properly!",
                    Toast.LENGTH_SHORT);
            t.show();
        }
    }

    public void save(){
        try {
            age = Integer.parseInt(ageInput.getText().toString());
            weight = Integer.parseInt(weightInput.getText().toString());
            goal = Integer.parseInt(goalInput.getText().toString());
            height = Integer.parseInt(heightInput.getText().toString());
            if (exerciseInput.getSelectedItemPosition() == 0) {
                exercise = 1.2f;
            } else if (exerciseInput.getSelectedItemPosition() == 1) {
                exercise = 1.375f;
            } else if (exerciseInput.getSelectedItemPosition() == 2) {
                exercise = 1.55f;
            } else if (exerciseInput.getSelectedItemPosition() == 3) {
                exercise = 1.725f;
            } else {
                exercise = 1.9f;
            }
            if (maleInput.isChecked() && !femaleInput.isChecked()) {
                isMale = true;
            } else {
                isMale = false;
            }
            if(gainButton.isChecked()){
                delta = 500;
            }
            else if(stayButton.isChecked()){
                delta = 0;
            }
            else {
                delta = -500;
            }
            user.exercise = exercise;
            user.delta = delta;
            user.isMale = isMale;
            user.age = age;
            user.goal = goal;
            user.weight = weight;
            user.height = height;
            MainActivity.saveData();
            Intent go = new Intent(this, MainActivity.class);
            go.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(go);
            finish();
        } catch(NumberFormatException e) {
            Toast t = Toast.makeText(getApplicationContext(),
                    "Sorry! Please make sure all fields are filled out properly!",
                    Toast.LENGTH_SHORT);
            t.show();
        }
    }

    public void load(){
        if(user.age == 0){
            ageInput.setText("");
        }
        else {
            ageInput.setText(""+user.age);
        }
        if(user.weight == 0){
            weightInput.setText("");
        }
        else {
            weightInput.setText(""+user.weight);
        }
        if(user.height == 0){
            heightInput.setText("");
        }
        else {
            heightInput.setText(""+user.height);
        }
        if(user.goal == 0){
            goalInput.setText("");
        }
        else {
            goalInput.setText(""+user.goal);
        }
        if(user.delta == 0){
            stayButton.setChecked(true);
            loseButton.setChecked(false);
            gainButton.setChecked(false);
        }
        else if(user.delta == 500){
            stayButton.setChecked(false);
            loseButton.setChecked(false);
            gainButton.setChecked(true);
        }
        else {
            stayButton.setChecked(false);
            loseButton.setChecked(true);
            gainButton.setChecked(false);
        }
        if(user.exercise == 1.2f || user.exercise == 0.0f){
            exerciseInput.setSelection(0);
        }
        else if(user.exercise == 1.375f){
            exerciseInput.setSelection(1);
        }
        else if(user.exercise == 1.55f){
            exerciseInput.setSelection(2);
        }
        else if(user.exercise == 1.725f){
            exerciseInput.setSelection(3);
        }
        else{
            exerciseInput.setSelection(4);
        }
        if(user.isMale){
            maleInput.setChecked(true);
            femaleInput.setChecked(false);
        }
        else{
            femaleInput.setChecked(true);
            maleInput.setChecked(false);
        }

    }
}
