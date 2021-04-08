package me.pgb.JStachuraForeGroundRadio.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class HomeViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public HomeViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Welcome to the Radio App! Head to Configuration to control the radio, and Visualizer to see the playing audio visualized!");
    }

    public LiveData<String> getText() {
        return mText;
    }
}