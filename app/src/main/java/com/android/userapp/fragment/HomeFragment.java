package com.android.userapp.fragment;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.android.userapp.kits.AppUsageUtil;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.software.ustc.superspy.R;
import com.android.userapp.db.sqllite.AppUsageDao;
import com.android.userapp.kits.AppUsageInfo;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class HomeFragment extends Fragment {

    private AppUsageDao pdao;
    List<AppUsageInfo> appUsageInfoList;
    private final int MAX_CNT = 50;
    Float[] pieValue = new Float[MAX_CNT];
    String[] pieLable = new String[MAX_CNT];
    private PieChart chart;
    private TextView tv;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        repareData();
        showPie();
        showTv();

    }


    void repareData() {
        //????????????
        AppUsageUtil.checkUsageStateAccessPermission(getContext());
        Calendar beginCal = Calendar.getInstance();
        beginCal.add(Calendar.DATE, -1);
        Calendar endCal = Calendar.getInstance();
        long start_time = beginCal.getTimeInMillis();
        long end_time = endCal.getTimeInMillis();
        //???????????????
        AppUsageUtil.getAppUsageInfo(getContext(), start_time, end_time);
        pdao = new AppUsageDao(getContext());//?????????
        appUsageInfoList = pdao.queryAppUsageList();
        for (int i = 0; i < MAX_CNT; ++i) {
            pieValue[i] = Float.parseFloat(appUsageInfoList.get(i).getForeground_time()) / 60;
            pieLable[i] = appUsageInfoList.get(i).getApp_name();
        }
    }

    private void showPie() {
        getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        chart = getActivity().findViewById(R.id.chart);
        chart.setBackgroundColor(Color.WHITE);

        moveOffScreen();

        chart.setUsePercentValues(true);
        chart.getDescription().setEnabled(false);

        chart.setCenterText(generateCenterSpannableText());

        chart.setDrawHoleEnabled(true);
        chart.setHoleColor(Color.WHITE);

        chart.setTransparentCircleColor(Color.WHITE);
        chart.setTransparentCircleAlpha(110);

        chart.setHoleRadius(58f);
        chart.setTransparentCircleRadius(61f);

        chart.setDrawCenterText(true);

        chart.setRotationEnabled(false);
        chart.setHighlightPerTapEnabled(true);

        chart.setMaxAngle(180f); // HALF CHART
        chart.setRotationAngle(180f);
        chart.setCenterTextOffset(0, -20);

        setPieData(6, 100);

        Legend l = chart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);
        l.setXEntrySpace(7f);
        l.setYEntrySpace(0f);
        l.setYOffset(0f);

        // entry label styling
        chart.setEntryLabelColor(Color.WHITE);
        chart.setEntryLabelTextSize(12f);
    }

    private void setPieData(int count, float range) {

        ArrayList<PieEntry> values = new ArrayList<>();

        for (int i = 0; i < count-1; i++) {
            values.add(new PieEntry(pieValue[i], pieLable[i]));
        }
        float sum=0;
        for(int i = count-1;i<MAX_CNT;i++)
        {
            sum+=pieValue[i];
        }
        values.add(new PieEntry(sum, "??????"));

        PieDataSet dataSet = new PieDataSet(values, "Election Results");
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);
        //????????????
        List<Integer> colors = new ArrayList<>();
        colors.add(Color.parseColor("#4A92FC"));
        colors.add(Color.parseColor("#7adfb8"));
        colors.add(Color.parseColor("#2ecc71"));
        colors.add(Color.parseColor("#f1c40f"));
        colors.add(Color.parseColor("#e74c3c"));
        colors.add(Color.parseColor("#3498db"));
        dataSet.setColors(colors);

        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.WHITE);
        chart.setData(data);

        chart.invalidate();
    }

    private SpannableString generateCenterSpannableText() {
        SpannableString s = new SpannableString("SuperSpy\n\n???????????????App??????????????????");
        //????????????
        s.setSpan(new RelativeSizeSpan(1.7f), 0, 8, 0);
        s.setSpan(new StyleSpan(Typeface.NORMAL), 0, 8, 0);
        s.setSpan(new ForegroundColorSpan(Color.GRAY), 0, 8, 0);
        s.setSpan(new RelativeSizeSpan(.8f), 0, 8, 0);
        s.setSpan(new StyleSpan(Typeface.ITALIC), 9, s.length(), 0);
        s.setSpan(new ForegroundColorSpan(ColorTemplate.getHoloBlue()), 9, s.length(), 0);
        return s;
    }

    private void moveOffScreen() {

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        int height = displayMetrics.heightPixels;

        int offset = (int) (height * 0.65); /* percent to move */

        RelativeLayout.LayoutParams rlParams =
                (RelativeLayout.LayoutParams) chart.getLayoutParams();
        rlParams.setMargins(0, 0, 0, -offset);
        chart.setLayoutParams(rlParams);
    }

    private void showTv()
    {
        tv = getActivity().findViewById(R.id.tv);
        java.text.DecimalFormat myformat = new java.text.DecimalFormat("0.00");
        tv.setText("??????,??????????????????: [ "+myformat.format(pieValue[0]+pieValue[1]+pieValue[2]+pieValue[3]+pieValue[4]+pieValue[5])+" ]??????\n\n"+
                "??????????????????App???:\n\n"+
                pieLable[0] +"\t [ "+myformat.format(pieValue[0])+" ] ??????\n"+
                pieLable[1] +"\t [ "+myformat.format(pieValue[1])+" ] ??????\n"+
                pieLable[2] +"\t [ "+myformat.format(pieValue[2])+" ] ??????\n"+
                pieLable[3] +"\t [ "+myformat.format(pieValue[3])+" ] ??????\n"+
                pieLable[4] +"\t [ "+myformat.format(pieValue[4])+" ] ??????\n"
        );
    }
}
