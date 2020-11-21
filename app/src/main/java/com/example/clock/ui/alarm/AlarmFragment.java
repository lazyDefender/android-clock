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

import com.example.clock.R;
import com.example.clock.databinding.FragmentAlarmBinding;
import com.example.clock.models.Alarm;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class AlarmFragment extends Fragment {

    private AlarmViewModel alarmViewModel;
    private FragmentAlarmBinding binding;
    private AlarmsListAdapter adapter;

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
        alarms.add(new Alarm("a"));
        alarms.add(new Alarm("b"));
        alarms.add(new Alarm("c"));
        alarms.add(new Alarm("d"));
        alarms.add(new Alarm("e"));
        alarms.add(new Alarm("f"));
        alarms.add(new Alarm("g"));
        alarms.add(new Alarm("h"));
        alarms.add(new Alarm("i"));
        alarms.add(new Alarm("j"));
        adapter = new AlarmsListAdapter(alarms);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity(), LinearLayoutManager.VERTICAL, false));
        binding.recyclerView.setAdapter(adapter);
        binding.recyclerView.setNestedScrollingEnabled(false);
        alarms.add(new Alarm("newwwwwwwwwwwwwwwwwwwwww"));
        System.out.println(binding.recyclerView);
    }
}