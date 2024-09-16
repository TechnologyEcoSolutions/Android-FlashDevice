package com.ngb.colorflashscreen.base;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public abstract class BaseActivity<T extends ViewModel> extends AppCompatActivity {
    protected T mModel;

    @Override
    protected void onCreate( @Nullable Bundle savedInstanceState ) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutID());
        mModel = new ViewModelProvider(this).get(getClassViewModel());
        initViews();
    }

    protected abstract void initViews();

    protected abstract Class<T> getClassViewModel();

    protected abstract int getLayoutID();

    protected void showFragment( int layoutId, Fragment fragment, boolean addToBackStack) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(layoutId, fragment);
        if (addToBackStack) {
            transaction.addToBackStack("add");
        }
        transaction.commit();
    }

}
