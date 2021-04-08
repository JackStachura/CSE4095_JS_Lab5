package me.pgb.JStachuraForeGroundRadio.ui.background_radio;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import java.util.Arrays;
import java.util.List;

import me.pgb.JStachuraForeGroundRadio.R;
import me.pgb.JStachuraForeGroundRadio.models.RadioStation;
import me.pgb.JStachuraForeGroundRadio.models.RadioStationArray;
import me.pgb.JStachuraForeGroundRadio.service.RadioService;
import me.pgb.JStachuraForeGroundRadio.service.ServiceContainer;

public class RadioFragment extends Fragment {

    private RadioViewModel radioViewModel;
    private Button radioToggleButton;
    private Button stopBackgroundThread;
    public static TextView statusView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_radio, container, false);
        
        radioToggleButton = root.findViewById(R.id.radio_toggle_button);
        if(!ServiceContainer.radioService.isPlaying()){
            radioToggleButton.setText("Turn Radio ON");
        }
        else{
            radioToggleButton.setText("Turn Radio OFF");
        }
        radioToggleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {/*
                int num = ServiceContainer.radioService.getCounter();
                Toast.makeText(getActivity().getApplicationContext(), "number: " + String.valueOf(num).toString(), Toast.LENGTH_SHORT).show();*/

                if (!ServiceContainer.radioService.isPlaying()){
                    ServiceContainer.radioService.radioOn();
                    radioToggleButton.setText("Turn Radio OFF");
                }
                else {
                    ServiceContainer.radioService.radioOff();
                    radioToggleButton.setText("Turn Radio ON");
                }

            }
        });
        //get String array of Radio Station names for Spinner view
        String[] stationNames = RadioStationArray.getArrayOfRadioNames();
        //get String array of Radio Station links for Spinner activation
        String[] stationLinks = RadioStationArray.getArrayOfRadioLinks();
        List<String> names = Arrays.asList(stationNames);
        List<String> links = Arrays.asList(stationLinks);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this.getContext(), android.R.layout.simple_spinner_item, stationNames);
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        Spinner spinner_stations = root.findViewById(R.id.spinner_stations);
        spinner_stations.setAdapter(adapter);
        int setPosition = -1;
        for(int i = 0; i < stationLinks.length; i++){
            if (stationLinks[i].equals(ServiceContainer.radioService.getURL())) setPosition = i;
        }
        if (setPosition >= 0) spinner_stations.setSelection(setPosition);
        spinner_stations.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                statusView = root.findViewById(R.id.statusView);
                statusView.setText("Changing...");
                ServiceContainer.radioService.changeURL(links.get(position));


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        SeekBar volumeBar = root.findViewById(R.id.seekBarVolume);
        volumeBar.setProgress(ServiceContainer.radioService.getProgress());
        volumeBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                ServiceContainer.radioService.setVolume(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        return root;
    }

}