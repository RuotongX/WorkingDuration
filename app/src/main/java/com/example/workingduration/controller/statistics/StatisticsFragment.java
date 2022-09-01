package com.example.workingduration.controller.statistics;

import static io.realm.internal.SyncObjectServerFacade.getApplicationContext;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.location.GnssAntennaInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

public class StatisticsFragment extends Fragment {

    private FragmentStatisticsBinding binding;
    private RealmManager rm;



    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        StatisticsViewModel statisticsViewModel =
                new ViewModelProvider(this).get(StatisticsViewModel.class);

        binding = FragmentStatisticsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        rm = new RealmManager();

        final ImageButton add_bt = binding.addButton;
        final ImageButton delete_bt = binding.deleteButton;
        final RecyclerView recyclerView = binding.recyclerView;
        final TextView textView = binding.totalHour;
        statisticsViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        textView.setTypeface(Typeface.createFromAsset(getContext().getAssets(), "fonts/FOT-MatissePro-EB.otf"));
        setAdapter(recyclerView);
        delete_bt.setOnClickListener(v ->{
            AlertDialog ad = new AlertDialog.Builder(getContext())
                    .setTitle("Delete All")
                    .setMessage("Do you confirm to clear all logs")
                    .setIcon(R.mipmap.delete_button)
                    .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            rm.deleteAll();
                            textView.setText("0 Hours");
                           setAdapter(recyclerView);
                        }
                    })
                    .setNegativeButton("exit",null)
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
    private void setAdapter(RecyclerView recyclerView) {
        StatisticsAdapter adapter = new StatisticsAdapter();
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

    }
}