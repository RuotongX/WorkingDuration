package com.example.workingduration.controller.statistics;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.workingduration.model.RealmManager;
import com.example.workingduration.R;
import com.example.workingduration.model.WorkingHour;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class StatisticsAdapter extends RecyclerView.Adapter<StatisticsAdapter.MyViewHolder> {
    private ArrayList<WorkingHour> hourList;
    private final RealmManager rm;
    private recyclerCallback rcallback;

    public StatisticsAdapter(recyclerCallback c){
        rm = new RealmManager();
        hourList = rm.selectFromDB();
        this.rcallback = c;
    }
    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView date_tv;
        private TextView hour_tv;

        public MyViewHolder(final View view) {
            super(view);
            date_tv = view.findViewById(R.id.recycler_date);
            hour_tv = view.findViewById(R.id.recycler_hour);
            itemView.setOnClickListener(v -> {
                AlertDialog.Builder alert = new AlertDialog.Builder(view.getContext());
                alert.setTitle("Please input the working hour");

// Set an EditText view to get user input
                final EditText input = new EditText(view.getContext());
                alert.setView(input);

                alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        String value = input.getText().toString();
                        hour_tv.setText(value+" Hours");
                        WorkingHour wh = new WorkingHour();
                        wh.setDate((String)date_tv.getText());
                        wh.setDuration(Double.parseDouble(value));
                        rm.updateDB(wh);
                        rcallback.refresh();
                        return;
                    }
                });

                alert.setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // TODO Auto-generated method stub
                                return;
                            }
                        });
                alert.setNeutralButton("Delete",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        WorkingHour wh = new WorkingHour();
                        wh.setDate((String)date_tv.getText());
                        rm.deleteHourFromDB(wh);
                        hourList.remove(getAdapterPosition());
                        rcallback.refresh();
                        return;
                    }
                });
                alert.show();
            });
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.statistics_column, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        String date = hourList.get(position).getDate();
        double hour = hourList.get(position).getDuration();
        DecimalFormat df = new DecimalFormat("#.#");
        String duration = df.format(hour)+" Hours";
        holder.date_tv.setText(date);
        holder.hour_tv.setText(duration);
    }

    @Override
    public int getItemCount() {
        return hourList.size();
    }

}
