package com.example.clock.ui.alarm;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.clock.handlers.AlarmFragmentHandler;
import com.example.clock.repos.AlarmRepo;
import com.example.clock.adapters.AlarmsListAdapter;
import com.example.clock.databinding.FragmentAlarmBinding;
import com.example.clock.models.Alarm;
import com.example.clock.utils.RequestCodes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AlarmFragment extends Fragment {

    private AlarmViewModel alarmViewModel;
    private FragmentAlarmBinding binding;
    private AlarmsListAdapter adapter;
    private AlarmFragmentHandler handler;
    private List<Alarm> alarms;

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
        alarms = new ArrayList<>();

        binding.recyclerView.setNestedScrollingEnabled(false);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity(), LinearLayoutManager.VERTICAL, false));


        try {
            alarms = AlarmRepo.findAll(this.getContext());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        finally {
            adapter = new AlarmsListAdapter(alarms, this);
            binding.recyclerView.setAdapter(adapter);
        }

        handler = new AlarmFragmentHandler();
        binding.setAlarmFragmentHandler(handler);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if(requestCode == RequestCodes.SHOW_ALARM_DELETE) {
            if(resultCode == Activity.RESULT_OK) {
                Bundle extras = intent.getExtras();
                List<Integer> deletedIds = extras.getIntegerArrayList("deletedIds");
                for(int id : deletedIds) {
                    int index = alarms.indexOf(new Alarm(id));
                    alarms.remove(index);
                    adapter.notifyItemRemoved(index);
                }
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Alarm newAlarm = AlarmRepo.getNewAlarm();
        int deletedDuringUpdateId = AlarmRepo.getDeletedDuringUpdateId();
        if(deletedDuringUpdateId > 0) {
            int index = alarms.indexOf(new Alarm(deletedDuringUpdateId));
            alarms.remove(index);
            adapter.notifyItemRemoved(index);
            AlarmRepo.setDeletedDuringUpdateId(0);
        }
        if(newAlarm != null) {
            alarms.add(newAlarm);
            adapter.notifyItemInserted(alarms.size() - 1);
            AlarmRepo.setNewAlarm(null);
        }

        Alarm dismissedAlarm = AlarmRepo.getDismissedAlarm();
        if(dismissedAlarm != null) {
            int index = alarms.indexOf(dismissedAlarm);
            alarms.set(index, dismissedAlarm);
            adapter.notifyItemChanged(index);
            AlarmRepo.setDismissedAlarm(null);
        }
    }
}