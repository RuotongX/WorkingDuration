package com.example.workingduration.controller.home;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.workingduration.model.RealmManager;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class HomeViewModel extends ViewModel {

    private final MutableLiveData<String> mText;
    private RealmManager rm  = new RealmManager();

    public HomeViewModel() {
        mText = new MutableLiveData<>();
        LocalDate date = LocalDate.now();
        LocalDate previous = date.minusDays(1);
        String result = date.format(DateTimeFormatter.ofPattern("MM-dd"));
        String resultP = previous.format(DateTimeFormatter.ofPattern("MM-dd"));
        LocalTime time = LocalTime.now();
        LocalTime start = LocalTime.parse("09:00");

        if(rm.contains(result)){
            mText.setValue(rm.getOneDay(result));
        } else if (rm.contains(resultP)&& time.isBefore(start)){
            mText.setValue(rm.getOneDay(resultP));
        }

    }

    public LiveData<String> getText() {
        return mText;
    }
}