package com.example.clock.fragments;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.clock.adapters.ZonesListAdapter;
import com.example.clock.databinding.FragmentTimeBinding;
import com.example.clock.models.Zone;
import com.example.clock.repos.ZoneRepo;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.TimeZone;

public class TimeFragment extends Fragment {

    private FragmentTimeBinding binding;
    private ZonesListAdapter adapter;
    private Zone[] zones;
    private Zone[] filteredZones;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentTimeBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Zone[] zones = ZoneRepo.findAll();
        for(Zone zone : zones) {
            TimeZone timezone = TimeZone.getTimeZone(zone.getId());
            Calendar calendar = Calendar.getInstance(timezone);
            DateFormat formatter = new SimpleDateFormat("dd.MM HH:mm");
            formatter.setCalendar(calendar);
            formatter.setTimeZone(timezone);
            String s = formatter.format(calendar.getTime());
            zone.setDateStr(s);
        }

        filteredZones = Arrays.copyOf(zones, zones.length);

        binding.recyclerView.setNestedScrollingEnabled(false);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity(), LinearLayoutManager.VERTICAL, false));
        adapter = new ZonesListAdapter(filteredZones);
        binding.recyclerView.setAdapter(adapter);

        binding.cityFilter.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String search = s.toString();
                filteredZones = Arrays.stream(zones)
                        .filter(zone -> zone.getName().contains(search))
                        .toArray(Zone[]::new);
                adapter.setZones(filteredZones);
            }
        });
    }
}