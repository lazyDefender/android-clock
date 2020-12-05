package com.example.clock.ui.alarm;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.clock.AlarmRepo;
import com.example.clock.R;
import com.example.clock.databinding.AlarmsListItemBinding;
import com.example.clock.databinding.FragmentAlarmBinding;
import com.example.clock.handlers.AlarmHandler;
import com.example.clock.models.Alarm;

import java.util.ArrayList;
import java.util.List;

public class AlarmFragment extends Fragment {

    private AlarmViewModel alarmViewModel;
    private FragmentAlarmBinding binding;
    private AlarmsListAdapter adapter;
    private AlarmHandler handler;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        alarmViewModel =
                new ViewModelProvider(this).get(AlarmViewModel.class);
//        View root = inflater.inflate(R.layout.fragment_alarm, container, false);
        binding = FragmentAlarmBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        List<Alarm> alarms = new ArrayList<>();
        adapter = new AlarmsListAdapter(alarms);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity(), LinearLayoutManager.VERTICAL, false));
        binding.recyclerView.setAdapter(adapter);
        binding.recyclerView.setNestedScrollingEnabled(false);

        List<Alarm> alarmsList = AlarmRepo.findAll(this.getContext());
        AlarmsListAdapter adapter = new AlarmsListAdapter(alarmsList);
        binding.recyclerView.setAdapter(adapter);

        handler = new AlarmHandler();
        binding.setAlarmHandler(handler);
    }
}