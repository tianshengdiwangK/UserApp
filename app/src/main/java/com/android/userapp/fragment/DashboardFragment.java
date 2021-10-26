package com.android.userapp.fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.userapp.activity.AppUsageAnalysisActivity;
import com.android.userapp.activity.DevInfoActivity;
import com.android.userapp.activity.AppInfoShowActivity;
import com.software.ustc.superspy.R;
import com.android.userapp.activity.AppsRecomendActivity;

public class DashboardFragment extends Fragment implements View.OnClickListener {



    private static final String TAG = "DashboardFragment";

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);
        return root;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Button btnApps = (Button) getActivity().findViewById(R.id.btn_apps);
        btnApps.setOnClickListener(this);
        Button btnSys = (Button) getActivity().findViewById(R.id.btn_sys);
        btnSys.setOnClickListener(this);
        Button btnAppUsages = (Button) getActivity().findViewById(R.id.btn_app_usage_list);
        btnAppUsages.setOnClickListener(this);
        Button btnAuth = (Button) getActivity().findViewById(R.id.btn_authority);
        btnAuth.setOnClickListener(this);
        Button btnAppRecomend = (Button) getActivity().findViewById(R.id.btn_app_recommend_list);
        btnAppRecomend.setOnClickListener(this);




    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_apps:
                startActivity(new Intent(getActivity(), AppInfoShowActivity.class));
                break;
            case R.id.btn_sys:
                startActivity(new Intent(getActivity(), DevInfoActivity.class));
                break;
            case R.id.btn_app_usage_list:
                startActivity(new Intent(getActivity(), AppUsageAnalysisActivity.class));
                break;
            case R.id.btn_authority:
                Intent intent = new Intent(android.provider.Settings.ACTION_USAGE_ACCESS_SETTINGS);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getContext().startActivity(intent);
                break;
            case R.id.btn_app_recommend_list:
                startActivity(new Intent(getActivity(), AppsRecomendActivity.class));
                break;

            default:
                break;
        }
    }




}