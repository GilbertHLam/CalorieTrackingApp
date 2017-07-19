package gilberthlam.com.calorietracker;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

/**
 * Created by Gilbert Lam on 2017-07-18.
 */

public class DialogEdit extends DialogFragment {
    Day selectedDay;
    Foods selectedFood;
    EditText name, cals;
    Activity main;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();
        main = getActivity();
        final View editFood = inflater.inflate(R.layout.edit_layout, null);
        name = (EditText) editFood.findViewById(R.id.username);
        cals = (EditText) editFood.findViewById(R.id.password);
        ((MainActivity) main).loadedDialog();
        Log.i("foodinedit","initialized");
        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(editFood);

                builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        selectedFood.setName(name.getText().toString());
                        selectedFood.setCalorie(Integer.parseInt(cals.getText().toString()));
                        ((MainActivity) main).listView.setAdapter(((MainActivity) main).adbFood);
                        ((MainActivity) main).calories.setText("" + ((MainActivity) main).currentDay.calculateTotalCalories());
                    }
                });
                builder.setNegativeButton("Delete", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        selectedDay.listOfFoods.remove(selectedFood);
                        ((MainActivity) main).adbFood.notifyDataSetChanged();
                        ((MainActivity) main).listView.setAdapter(((MainActivity) main).adbFood);
                        ((MainActivity) main).calories.setText("" + ((MainActivity) main).currentDay.calculateTotalCalories());
                        DialogEdit.this.getDialog().cancel();
                    }
                });

        return builder.create();
    }
    public void setFood(Foods selectedFood){

        this.selectedFood = selectedFood;
        Log.i("foodinedit",selectedFood.getName());
        name.setText(selectedFood.getName());
        cals.setText(""+selectedFood.getCalorie());

    }
    public void setSelectedDay(Day selectedDay){
        this.selectedDay = selectedDay;

    }
}
