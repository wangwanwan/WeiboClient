package com.bricechou.weiboclient.fragement;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bricechou.weiboclient.BaseFragment;
import com.bricechou.weiboclient.R;

/**
 * Created by sdduser on 5/28/16.
 */
public class HomeFragment extends BaseFragment {

    private View mView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = View.inflate(mMainActivity, R.layout.frag_home, null);
        return mView;
    }

    public void getWeibo() {

    }
}
