package gilberthlam.com.calorietracker;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Gilbert Lam on 2017-07-17.
 */

public class AdapterFood extends ArrayAdapter<Foods> {
    private Activity activity;
    private ArrayList<Foods> listFoods;
    private static LayoutInflater inflater = null;

    public AdapterFood (Activity activity, int textViewResourceId,ArrayList<Foods> listFoods) {
        super(activity, textViewResourceId, listFoods);
        try {
            this.activity = activity;
            this.listFoods = listFoods;

            inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        } catch (Exception e) {
            Log.i("error", "e");
        }


    }
        public int getCount() {
            return listFoods.size();
        }

        public long getItemId(int position) {
            return position;
        }

        public static class ViewHolder {
            public TextView display_name;
            public TextView display_number;

        }

        public View getView(int position, View convertView, ViewGroup parent) {
            View vi = convertView;
            final ViewHolder holder;
            try {
                if (convertView == null) {
                    vi = inflater.inflate(R.layout.list_layout, null);
                    holder = new ViewHolder();

                    holder.display_name = (TextView) vi.findViewById(R.id.display_name);
                    holder.display_number = (TextView) vi.findViewById(R.id.display_number);


                    vi.setTag(holder);
                } else {
                    holder = (ViewHolder) vi.getTag();
                }



                holder.display_name.setText(listFoods.get(position).getName());
                holder.display_number.setText(""+listFoods.get(position).getCalorie());


            } catch (Exception e) {


            }
            return vi;
        }
    }



