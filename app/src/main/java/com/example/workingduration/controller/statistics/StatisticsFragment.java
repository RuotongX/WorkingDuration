package com.example.workingduration.controller.statistics;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.workingduration.databinding.FragmentStatisticsBinding;

public class StatisticsFragment extends Fragment {

    private FragmentStatisticsBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        StatisticsViewModel statisticsViewModel =
                new ViewModelProvider(this).get(StatisticsViewModel.class);

        binding = FragmentStatisticsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.totalHour;
        statisticsViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        textView.setTypeface(Typeface.createFromAsset(getContext().getAssets(), "fonts/FOT-MatissePro-EB.otf"));
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}