package com.example.workingduration.controller.home;

import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.workingduration.databinding.FragmentHomeBinding;
import com.example.workingduration.model.RealmManager;
import com.example.workingduration.model.WorkingHour;

import java.text.DecimalFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private RealmManager rm;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
            HomeViewModel homeViewModel =
                    new ViewModelProvider(this).get(HomeViewModel.class);

            binding = FragmentHomeBinding.inflate(inflater, container, false);
            View root = binding.getRoot();
            rm = new RealmManager();

            final TextView textView = binding.textHome;
            final Button finishButton = binding.finishButton;

            finishButton.setOnClickListener(v ->{
                    textView.setText(CountingHour());
                }
            );
            homeViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
            textView.setTypeface(Typeface.createFromAsset(getContext().getAssets(), "fonts/FOT-MatissePro-EB.otf"));
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
    private String CountingHour(){
        LocalDate date = LocalDate.now();
        LocalTime start = LocalTime.parse("09:00");
        LocalTime end = LocalTime.now();
        double workingtime = 0;
        if (end.isAfter(start)) {
            Duration duration = Duration.between(start,end);
            workingtime = duration.toMinutes()/60.0;
        } else {
            LocalTime midnight = LocalTime.parse("00:00");
            Duration duration = Duration.between(midnight,end);
            workingtime = duration.toMinutes()/60.0;
            workingtime = 15 + workingtime;
            date = date.minusDays(1);
        }
        WorkingHour wh = new WorkingHour();
        wh.setDate(date.format(DateTimeFormatter.ofPattern("MM-dd")));
        wh.setDuration(workingtime);
        if(rm.contains(wh.getDate())){
            rm.updateDB(wh);
        } else{
            rm.saveDatabase(wh);
        }
        DecimalFormat df = new DecimalFormat("#.#");
        return df.format(workingtime)+" Hours";
    }
}