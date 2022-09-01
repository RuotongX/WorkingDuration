package com.example.workingduration.controller.statistics;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.workingduration.model.RealmManager;
import com.example.workingduration.model.WorkingHour;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class StatisticsViewModel extends ViewModel {

    private final MutableLiveData<String> mText;
    private RealmManager rm  = new RealmManager();

    public StatisticsViewModel() {
        mText = new MutableLiveData<>();
        ArrayList<WorkingHour> hourList = rm.selectFromDB();
        double total = hourList.stream().mapToDouble(WorkingHour::getDuration).sum();
        DecimalFormat df = new DecimalFormat("#.#");
        mText.setValue(df.format(total)+" Hours");
    }

    public LiveData<String> getText() {
        return mText;
    }
}