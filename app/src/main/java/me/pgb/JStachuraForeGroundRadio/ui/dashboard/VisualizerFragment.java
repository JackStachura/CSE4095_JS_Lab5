package me.pgb.JStachuraForeGroundRadio.ui.dashboard;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.gauravk.audiovisualizer.visualizer.BarVisualizer;

import me.pgb.JStachuraForeGroundRadio.MainActivity;
import me.pgb.JStachuraForeGroundRadio.R;
import me.pgb.JStachuraForeGroundRadio.service.ServiceContainer;

import static android.Manifest.permission.RECORD_AUDIO;

public class VisualizerFragment extends Fragment {

    private VisualizerViewModel visualizerViewModel;
    public static boolean audio_set  = false;
    private BarVisualizer mVisualizer;
    public static int session_id = 0;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        visualizerViewModel =
                new ViewModelProvider(this).get(VisualizerViewModel.class);
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);

        visualizerViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {

            }
        });
        MainActivity main = (MainActivity) getActivity();
        int saidYes = ContextCompat.checkSelfPermission(main.getApplicationContext(), RECORD_AUDIO);
        if (saidYes == PackageManager.PERMISSION_GRANTED) {


            Log.i("VIS", "" + String.valueOf(audio_set));
            if (mVisualizer != null) {
                mVisualizer.release();
            }


            if (ServiceContainer.radioService.isPlaying()) {
                TextView tv = root.findViewById(R.id.textViewNoVis);
                tv.setText(""); // no need for the text if the visualizer is on.
                mVisualizer = root.findViewById(R.id.blast);
                if (ServiceContainer.radioService.getAudioSessionId() != -1) {
                    mVisualizer.setAudioSessionId(ServiceContainer.radioService.getAudioSessionId());
                }
                session_id = ServiceContainer.radioService.getAudioSessionId();

            }

        }
        return root;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mVisualizer != null) {
            mVisualizer.release();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if(mVisualizer != null) {
            mVisualizer.release();
        }
    }
}