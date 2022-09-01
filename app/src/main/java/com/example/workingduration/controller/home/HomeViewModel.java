package com.example.workingduration.controller.home;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.workingduration.model.RealmManager;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class HomeViewModel extends ViewModel {

    private final MutableLiveData<String> mText;
    private RealmManager rm  = new RealmManager();

    public HomeViewModel() {
        mText = new MutableLiveData<>();
        LocalDate date = LocalDate.now();
        String result = date.format(DateTimeFormatter.ofPattern("MM-dd"));
        if(rm.contains(result)){
            mText.setValue(rm.getOneDay(result));
        }

    }

    public LiveData<String> getText() {
        return mText;
    }
}