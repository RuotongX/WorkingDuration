package com.example.workingduration.model;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import io.realm.Realm;
import io.realm.RealmResults;

public class RealmManager {
    Realm realm;
    RealmResults<WorkingHour> workHourDatabase;

    public RealmManager(){
        realm = Realm.getDefaultInstance();
    }

    public void saveDatabase(WorkingHour wh){
        realm.executeTransactionAsync(realm -> {
            WorkingHour hour = realm.createObject(WorkingHour.class,wh.getDate());
            hour.setDuration(wh.getDuration());
        });
    }
    public ArrayList<WorkingHour> selectFromDB(){
        workHourDatabase = realm.where(WorkingHour.class).findAll();
        ArrayList<WorkingHour> hoursList = new ArrayList<>();
        for(WorkingHour wh : workHourDatabase){
            hoursList.add(wh);
        }
        return hoursList;
    }
    public String getOneDay(String date){
        WorkingHour hourInDB = realm.where(WorkingHour.class).equalTo("date",date).findFirst();
        DecimalFormat df = new DecimalFormat("#.#");
        return df.format(hourInDB.getDuration())+" Hours";
    }
    public void updateDB(WorkingHour wh){
        realm.executeTransactionAsync(realm -> {
            WorkingHour hourInDB = realm.where(WorkingHour.class).equalTo("date",wh.getDate()).findFirst();
            hourInDB.setDuration(wh.getDuration());
        });
    }

    public void deleteHourFromDB(WorkingHour wh){
        realm.executeTransactionAsync(realm -> {
            WorkingHour hourInDB = realm.where(WorkingHour.class).equalTo("date",wh.getDate()).findFirst();
            hourInDB.deleteFromRealm();
        });
    }
    public void deleteAll(){
        realm.beginTransaction();
        realm.deleteAll();
        realm.commitTransaction();
    }
    public boolean contains(String date){
        workHourDatabase = realm.where(WorkingHour.class).findAll();
        for (WorkingHour wh : workHourDatabase) {
            if(wh.getDate().equals(date)){
                return true;
            }
        }
        return false;
    }

}
