package com.ngb.colorflashscreen.ui.fragment.callscreen.viewmodel;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.ViewModel;

import com.ngb.colorflashscreen.datasource.model.ScreenModel;

import java.util.ArrayList;
import java.util.List;

public class ListScreenViewModel extends ViewModel {
    private List<ScreenModel> modelList;
    private Context mContext;

    public List<ScreenModel> initImage( Context context ) {
        mContext = context;
        modelList = new ArrayList<>();
        try {
            String[] listPath = mContext.getAssets().list("image");
            for (String fileName : listPath) {
                String iconName = "image/" + fileName;
                modelList.add(new ScreenModel("Lucy", iconName));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return modelList;
    }

    public List<ScreenModel> getModelList() {
        return modelList;
    }

    public void setModelList( List<ScreenModel> modelList ) {
        this.modelList = modelList;
    }
}
