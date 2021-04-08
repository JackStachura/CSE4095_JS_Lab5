package me.pgb.JStachuraForeGroundRadio.ui.background_radio;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class RadioViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public RadioViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is notifications fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}