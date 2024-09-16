package com.ngb.colorflashscreen.base;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public abstract class BaseFragment<T extends ViewModel> extends Fragment {
    protected T mModel;
    protected Context mContext;
    protected View rootView;

    @Override
    public void onAttach( @NonNull Context context ) {
        super.onAttach(context);
        mContext = context;
    }

    @Nullable
    @Override
    public View onCreateView( @NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState ) {
        rootView = inflater.inflate(getLayoutID(), container, false);
        mModel = new ViewModelProvider(requireActivity()).get(getClassViewModel());
        initViews();
        return rootView;
    }

    protected abstract void initViews();

    protected abstract Class<T> getClassViewModel();

    protected abstract int getLayoutID();
}
