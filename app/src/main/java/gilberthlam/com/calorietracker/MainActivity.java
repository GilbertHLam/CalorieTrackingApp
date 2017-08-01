package gilberthlam.com.calorietracker;

import android.app.DatePickerDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends FragmentActivity {
    private GestureDetector gestureDetector;
    View.OnTouchListener gestureListener;
    Intent go;
    protected static final int RESULT_SPEECH = 1;
    static Context context;
    TextToSpeech tts;
    static Model model;
    Day currentDay;
    ArrayAdapter<String> adapter;
    ListView listView;
    String foodName;
    TextView date;
    TextView calories;
    DialogEdit d = new DialogEdit();
    Button dateButton;
    Foods selectedFood;
    AdapterFood adbFood;
    ArrayList<Foods> myListItems;
    Day todaysDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MainActivity.context = getApplicationContext();
        todaysDate = new Day();
        ArrayAdapter<String> adapter;
        listView = (ListView) findViewById(R.id.listView);
        date = (TextView) findViewById(R.id.dateLabel);
        calories = (TextView) findViewById(R.id.totalCalLabel);
        loadData();
        currentDay = model.getToday();
        Log.d("ONLOAD", "" + currentDay.currentDate.toString());


        dateButton = (Button) findViewById(R.id.dateButton);
        if(!todaysDate.getDate().equals(currentDay.getDate())){
            model.addDay();
            saveData();
            editInfo();
        }
        verifyButtonVisibility();
        final Calendar myCalendar = Calendar.getInstance();

        final DatePickerDialog.OnDateSetListener date1 = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                myCalendar.set(Calendar.HOUR_OF_DAY, 0);
                myCalendar.set(Calendar.MINUTE, 0);
                myCalendar.set(Calendar.SECOND, 0);
                myCalendar.set(Calendar.MILLISECOND, 0);
                Log.i("set",""+myCalendar.getTime());
                Log.i("set",""+currentDay.currentDate.toString());
                if(model.getSelectedDay(myCalendar.getTime()) == null)
                    model.addSetDay(myCalendar.getTime());
                currentDay = model.getSelectedDay(myCalendar.getTime());
                saveData();
                editInfo();
            }

        };

        dateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(MainActivity.this, date1, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        editInfo();


        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() { //list is my listView

            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1, final int pos, long id) {
                d.editMode = true;
                Log.i("selected",pos+"");
                selectedFood = currentDay.getListOfFoods().get(pos);
                Log.i("food",selectedFood.getName());
                d.show(getFragmentManager(),"");
                return true;
            }
        });

        Button addButton = (Button) findViewById(R.id.addButton);
        addButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(
                        RecognizerIntent.ACTION_RECOGNIZE_SPEECH);

                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, "en-US");

                try {
                    startActivityForResult(intent, RESULT_SPEECH);
                } catch (ActivityNotFoundException a) {
                    Toast t = Toast.makeText(getApplicationContext(),
                            "Sorry! Your device doesn't have speech to text. Try to add it manually instead.",
                            Toast.LENGTH_SHORT);
                    t.show();
                }
            }
        });

        addButton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                addFood(currentDay, new Foods("",0));
                selectedFood = currentDay.getListOfFoods().get(currentDay.getListOfFoods().size()-1);
                d.editMode = false;
                d.show(getFragmentManager(),"");
                return true;
            }
        });
        gestureDetector = new GestureDetector(new SwipeGestureDetector());
        gestureListener = new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                return gestureDetector.onTouchEvent(event);
            }
        };
        listView.setOnTouchListener(gestureListener);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case RESULT_SPEECH: {
                if (resultCode == RESULT_OK && null != data) {
                    ArrayList<String> text = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    foodName = text.get(0).replaceAll(" ", "+");
                    String keyword = foodName + "+calories";
                    RequestQueue queue = Volley.newRequestQueue(this);
                    String url = "http://www.google.ca/search?q=" + keyword;
                    StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    // Display the first 500 characters of the response string.
                                    String[] cals;
                                    try {
                                        cals = response.split("<div class=\"_XWk an_fna\" aria-live=\"polite\">");
                                        response = cals[1].split(" calories</div>")[0];
                                        Log.i("i", "Response is: " + response);
                                        response = response.replaceAll(",", "");
                                        foodName = foodName.replaceAll("\\+", " ");
                                        foodName = foodName.toUpperCase();
                                        int caloriesTemp = Integer.parseInt(response);
                                        String tempFood = foodName;
                                        //Adding food today!
                                        addFood(currentDay, new Foods(tempFood, caloriesTemp));

                                        editInfo();
                                        saveData();
                                    } catch (ArrayIndexOutOfBoundsException e) {
                                        Toast t = Toast.makeText(getApplicationContext(),
                                                "Sorry! We couldn't find the calorie information! Try to add it manually instead.",
                                                Toast.LENGTH_SHORT);
                                        t.show();
                                        currentDay.getListOfFoods().add(new Foods("",0));
                                        selectedFood = currentDay.getListOfFoods().get(currentDay.getListOfFoods().size()-1);
                                        Log.i("food",selectedFood.getName());

                                        d.show(getFragmentManager(),"");

                                    }
                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d("Connection", "That didn't work!");
                        }
                    });
                    queue.add(stringRequest);
                }
                break;
            }

        }
    }

    public void addFood(Day selectedDay, Foods selectedFood) {
        selectedDay.addFood(selectedFood);
    }

    public static void saveData() {
        try {
            FileOutputStream fos = MainActivity.context.openFileOutput("data.ct", Context.MODE_PRIVATE);
            ObjectOutputStream os = new ObjectOutputStream(fos);
            os.writeObject(model);
            os.close();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadData(){
        FileInputStream fis = null;
        try {
            fis = MainActivity.context.openFileInput("data.ct");
            try {
                ObjectInputStream is = new ObjectInputStream(fis);
                model = (Model) is.readObject();
                is.close();
                fis.close();


            }catch(IOException e) {
                model = new Model();
            }catch (ClassNotFoundException e) {
                model = new Model();
            }
        }catch (FileNotFoundException e) {
            model = new Model();
            Log.i("Load","NEW");
        }

    }
    public void loadedDialog(){
        d.setSelectedDay(currentDay);
        d.setFood(selectedFood);
    }

    public void editInfo(){
        myListItems = currentDay.listOfFoods;
        adbFood = new AdapterFood(this, 0, myListItems);
        listView.setAdapter(adbFood);
        date.setText(currentDay.getDate());
        calories.setText("" + currentDay.calculateTotalCalories() + "/" + model.user.goal);
    }

    public void verifyButtonVisibility(){
        dateButton.setVisibility(View.VISIBLE);
    }

    private void onLeftSwipe() {
        Toast t = Toast.makeText(MainActivity.this, "Left swipe", Toast.LENGTH_LONG);
        t.show();
        go = new Intent(this, Progress.class);
        go.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(go);
        finish();
    }

    private void onRightSwipe() {
        Toast t = Toast.makeText(MainActivity.this, "Right swipe", Toast.LENGTH_LONG);
        t.show();
        go = new Intent(this, Profile.class);
        go.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(go);
        finish();
    }

    private class SwipeGestureDetector extends GestureDetector.SimpleOnGestureListener {
        private static final int SWIPE_MIN_DISTANCE = 50;
        private static final int SWIPE_MAX_OFF_PATH = 200;
        private static final int SWIPE_THRESHOLD_VELOCITY = 200;

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                               float velocityY) {
            try {
                float diffAbs = Math.abs(e1.getY() - e2.getY());
                float diff = e1.getX() - e2.getX();

                if (diffAbs > SWIPE_MAX_OFF_PATH)
                    return false;

                // Left swipe
                if (diff > SWIPE_MIN_DISTANCE
                        && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                    MainActivity.this.onLeftSwipe();
                }
                // Right swipe
                else if (-diff > SWIPE_MIN_DISTANCE
                        && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                    MainActivity.this.onRightSwipe();
                }
            } catch (Exception e) {
                Log.e("Home", "Error on gestures");
            }
            return false;
        }

    }

}

