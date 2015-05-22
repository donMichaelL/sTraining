package com.example.michalis.straining.ListAdapter;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.michalis.straining.DataModel.DayTraining;
import com.example.michalis.straining.R;

import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import static com.example.michalis.straining.R.color.material_blue_grey_800;

/**
 * Created by Michalis on 4/30/2015.
 */
public class TrainingListAdapter extends ArrayAdapter<DayTraining> {
    private static int DEFAUAL_COLOR = 0x00000000;
    private static String TOTAL_TRAINING = "Number of Trainings: ";
    private Context context;

    public TrainingListAdapter(Context context, int resource, ArrayList<DayTraining> list) {
        super(context, resource, list);
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View cell = convertView;
        ViewHolder viewHolder;
        DayTraining dayTraining = getItem(position);

        if (cell == null){
            cell = inflater.inflate(R.layout.training_cell_layout, parent, false);
            viewHolder = new ViewHolder(cell);
            cell.setTag(viewHolder);
        }

        viewHolder = (ViewHolder) cell.getTag();

        if (position == 0){
            cell.setBackgroundColor(context.getResources().getColor(android.R.color.holo_blue_dark));
            viewHolder.txtNumberTraining.setText(String.valueOf(TOTAL_TRAINING +dayTraining.getNumber()));
        }else {
            cell.setBackgroundColor(DEFAUAL_COLOR);
            viewHolder.txtNumberTraining.setText(context.getResources().getStringArray(R.array.days)[dayTraining.getDay()-1]);
        }
        viewHolder.txtDescription.setText(dayTraining.getDescription());
        viewHolder.txtTotalKm.setText(String.valueOf(dayTraining.getTotalKm()));
        viewHolder.txtTotalTime.setText(String.valueOf(dayTraining.getTime()));

        return cell;
    }

    class ViewHolder {
        TextView txtDescription;
        TextView txtNumberTraining;
        TextView txtTotalKm;
        TextView txtTotalTime;

        ViewHolder(View cell){
            txtDescription = (TextView) cell.findViewById(R.id.txt_desciption);
            txtNumberTraining = (TextView) cell.findViewById(R.id.training_number);
            txtTotalKm = (TextView) cell.findViewById(R.id.total_km);
            txtTotalTime = (TextView) cell.findViewById(R.id.total_time);
        }
    }
}
