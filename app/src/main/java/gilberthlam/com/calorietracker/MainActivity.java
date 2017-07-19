package gilberthlam.com.calorietracker;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.ByteArrayOutputStream;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static android.provider.ContactsContract.CommonDataKinds.Website.URL;

public class MainActivity extends FragmentActivity {
    protected static final int RESULT_SPEECH = 1;
    static Context context;
    TextToSpeech tts;
    Model model;
    Day currentDay;
    ArrayAdapter<String> adapter;
    ListView listView;
    String foodName;
    TextView date;
    TextView calories;
    DialogEdit d = new DialogEdit();
    Foods selectedFood;
    AdapterFood adbFood;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MainActivity.context = getApplicationContext();
        loadData();
        currentDay = model.getToday();
        ArrayAdapter<String> adapter;
        listView = (ListView) findViewById(R.id.listView);
        date = (TextView) findViewById(R.id.dateLabel);
        calories = (TextView) findViewById(R.id.totalCalLabel);

        date.setText(currentDay.getDate());

        ArrayList<Foods> myListItems = currentDay.listOfFoods;
        adbFood = new AdapterFood(this, 0, myListItems);
        listView.setAdapter(adbFood);

        calories.setText("" + currentDay.calculateTotalCalories());


        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() { //list is my listView

            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1, final int pos, long id) {
                Log.i("selected",pos+"");
                selectedFood = currentDay.getListOfFoods().get(pos);
                Log.i("food",selectedFood.getName());

                d.show(getFragmentManager(),"");
                saveData();


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
                                        addFood(model.getToday(), new Foods(tempFood, caloriesTemp));

                                        listView.setAdapter(adbFood);
                                        calories.setText("" + currentDay.calculateTotalCalories());
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
                                        saveData();
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

    public void saveData() {
        try {
            FileOutputStream fos = MainActivity.context.openFileOutput("data.ct", Context.MODE_PRIVATE);
            ObjectOutputStream os = new ObjectOutputStream(fos);
            os.writeObject(model);
            os.close();
            fos.close();
            loadData();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadData(){
        try {
            FileInputStream fis = MainActivity.context.openFileInput("data.ct");
            ObjectInputStream is = new ObjectInputStream(fis);
            model = (Model) is.readObject();
            is.close();
            fis.close();
            this.recreate();
            Log.i("Load","LOAD SUCCESSFUL");

        }catch(IOException e){
            e.printStackTrace();
        }catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    public void loadedDialog(){
        d.setSelectedDay(currentDay);
        d.setFood(selectedFood);
    }



}
