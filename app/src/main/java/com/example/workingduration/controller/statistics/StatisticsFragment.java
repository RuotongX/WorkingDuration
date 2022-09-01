package com.example.workingduration.controller.statistics;

import static io.realm.internal.SyncObjectServerFacade.getApplicationContext;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.workingduration.databinding.FragmentStatisticsBinding;
import com.example.workingduration.R;
import com.example.workingduration.model.RealmManager;
import com.example.workingduration.model.WorkingHour;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.ArrayList;

public class StatisticsFragment extends Fragment implements recyclerCallback {

    private FragmentStatisticsBinding binding;
    private RealmManager rm;
    private RecyclerView recyclerView;
    private TextView textView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        StatisticsViewModel statisticsViewModel =
                new ViewModelProvider(this).get(StatisticsViewModel.class);

        binding = FragmentStatisticsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        rm = new RealmManager();

        final ImageButton add_bt = binding.addButton;
        final ImageButton delete_bt = binding.deleteButton;
        recyclerView = binding.recyclerView;
        textView = binding.totalHour;
        statisticsViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        textView.setTypeface(Typeface.createFromAsset(getContext().getAssets(), "fonts/FOT-MatissePro-EB.otf"));
        setAdapter();
        add_bt.setOnClickListener(v -> {
                    int mYear = LocalDate.now().getYear();
                    int mMonth = LocalDate.now().getMonthValue();
                    int mDay = LocalDate.now().getDayOfMonth();
                    WorkingHour wh = new WorkingHour();
                    wh.setDate("00-00");
                    DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                            new DatePickerDialog.OnDateSetListener() {
                                @Override
                                public void onDateSet(DatePicker view, int year,
                                                      int monthOfYear, int dayOfMonth) {
                                    String month = String.format("%02d", monthOfYear+1);
                                    String day = String.format("%02d", dayOfMonth);
                                    Log.d("message", month+"-"+day);
                                    wh.setDate(month + "-" + day);
                                    wh.setDuration(0);
                                    if(rm.contains(wh.getDate())||wh.getDate().equals("00-00")){

                                    } else {
                                        rm.saveDatabase(wh);
                                        setAdapter();
                                    }
                                }
                            }, mYear, mMonth-1, mDay);

                    datePickerDialog.show();

                }
        );

        delete_bt.setOnClickListener(v -> {
            AlertDialog ad = new AlertDialog.Builder(getContext())
                    .setTitle("Delete All")
                    .setMessage("Do you confirm to clear all logs")
                    .setIcon(R.mipmap.delete_button)
                    .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            rm.deleteAll();
                            textView.setText("0 Hours");
                            setAdapter();
                        }
                    })
                    .setNegativeButton("exit", null)
                    .create();
            ad.show();
        });
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void setAdapter() {
        StatisticsAdapter adapter = new StatisticsAdapter(StatisticsFragment.this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

    }

    @Override
    public void refresh() {
        ArrayList<WorkingHour> hourList = rm.selectFromDB();
        double total = hourList.stream().mapToDouble(WorkingHour::getDuration).sum();
        DecimalFormat df = new DecimalFormat("#.#");
        textView.setText(df.format(total)+" Hours");
        setAdapter();

    }
}