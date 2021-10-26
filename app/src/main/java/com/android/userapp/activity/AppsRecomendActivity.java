package com.android.userapp.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;
import com.android.userapp.db.sqllite.AppUsageDao;
import com.android.userapp.kits.AppTagsMap;
import com.android.userapp.kits.AppUsageUtil;
import com.android.userapp.kits.BaseActivity;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.RadarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.*;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.software.ustc.superspy.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class AppsRecomendActivity extends BaseActivity {
    private AppUsageDao pdao;
    private AppTagsMap appTagsMap;

    private RadarChart chart;
    private final List<String>chartX=new ArrayList<String>();
    private final List<String>chartY=new ArrayList<String>();

    private BarChart bar;
    private List<BarEntry> barEntryList = new ArrayList<BarEntry>();
    private final List<String>barX=new ArrayList<String>();
    private final List<String>barY=new ArrayList<String>();

    private TextView tv_app_tag,tv_man_tag,tv_app_recomend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apps_recomend);

        repareData();
        showBar();
        showChart();
        showTextView();
    }

    void repareData()
    {
        appTagsMap=new AppTagsMap();
        //权限检查
        AppUsageUtil.checkUsageStateAccessPermission(this);
        Calendar beginCal = Calendar.getInstance();
        beginCal.add(Calendar.YEAR, -1);
        Calendar endCal = Calendar.getInstance();
        long start_time = beginCal.getTimeInMillis();
        long end_time = endCal.getTimeInMillis();
        //数据库刷新
        AppUsageUtil.getAppUsageInfo(getApplicationContext(),start_time,end_time);
        pdao = new AppUsageDao(this);//数据层
        long[] tagChart=new long[10];
        long[] tagBar=new long[10];
        for(int i=0;i<appTagsMap.getAppTag().length-1;++i)
        {
            tagChart[i]=pdao.queryAppTagUsage(appTagsMap.getAppTag()[i]);
        }
        for(int i=0;i<appTagsMap.getAppTag().length;++i)
        {
            tagBar[i]=pdao.queryAppTagUsage(appTagsMap.getAppTag()[i]);
        }
        //寻找使用最多的五种类型的app,构建Chart数据
        long[] temp=tagChart;
        for(int i=0;i<5;++i)
        {
            int max=0;
            //不统计其他类
            for(int j=0;j<appTagsMap.getAppTag().length-1;++j)
            {
                if(temp[j]>temp[max])
                {
                    max=j;
                }
            }
            java.text.DecimalFormat myformat = new java.text.DecimalFormat("0.00");
            String hour = myformat.format(temp[max]/60.0/60.0);
            chartX.add(appTagsMap.getPeopleTag()[max]);
            chartY.add(hour);
            temp[max]=-1;
        }
        //排序,构建Bar数据
        temp=tagBar;
        for(int i=0;i<10;++i)
        {
            int max=0;
            for(int j=0;j<appTagsMap.getAppTag().length;++j)
            {
                if(temp[j]>temp[max])
                {
                    max=j;
                }
            }
            java.text.DecimalFormat myformat = new java.text.DecimalFormat("0.00");
            String hour = myformat.format(temp[max]/60.0/60.0);
            barX.add(appTagsMap.getAppTag()[max]);
            barY.add(hour);
            temp[max]=-1;
        }
    }

    void showBar()
    {
        bar = (BarChart) findViewById(R.id.bar);
        //添加数据
        for(int i=1;i<=10;++i)
        {
            barEntryList.add(new BarEntry(i,new Float(barY.get(i-1))/100));
        }

        BarDataSet barDataSet=new BarDataSet(barEntryList,"应用类型");   //list是你这条线的数据  "语文" 是你对这条线的描述
        BarData barData=new BarData(barDataSet);
        bar.setData(barData);
        bar.getXAxis().setDrawGridLines(false);  //是否绘制X轴上的网格线（背景里面的竖线）
        bar.getAxisLeft().setDrawGridLines(false);  //是否绘制Y轴上的网格线（背景里面的横线）
        bar.getDescription().setEnabled(false);                  //是否显示右下角描述

        //X轴
        XAxis xAxis=bar.getXAxis();
        xAxis.setDrawGridLines(false);  //是否绘制X轴上的网格线（背景里面的竖线）
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);        //X轴所在位置,默认为上面
        xAxis.setValueFormatter(new IAxisValueFormatter() {   //X轴自定义坐标
            @Override
            public String getFormattedValue(float v, AxisBase axisBase) {
                if (v==1){
                    return barX.get(0);
                }
                if (v==2){
                    return barX.get(1);
                }
                if (v==3){
                    return barX.get(2);
                }
                if (v==4){
                    return barX.get(3);
                }
                if (v==5){
                    return barX.get(4);
                }
                if (v==6){
                    return barX.get(5);
                }
                if (v==7){
                    return barX.get(6);
                }
                if (v==8){
                    return barX.get(7);
                }
                if (v==9){
                    return barX.get(8);
                }
                if (v==10){
                    return barX.get(9);
                }
                return "";//注意这里需要改成 ""
            }
        });
        xAxis.setAxisMaximum(11);   //X轴最大数值
        xAxis.setAxisMinimum(0);   //X轴最小数值
        //X轴坐标的个数    第二个参数一般填false     true表示强制设置标签数 可能会导致X轴坐标显示不全等问题
        xAxis.setLabelCount(6,false);
        //Y轴
        YAxis AxisLeft=bar.getAxisLeft();
        AxisLeft.setDrawGridLines(false);  //是否绘制Y轴上的网格线（背景里面的横线）
        int maxY= (int)(Float.parseFloat(barY.get(0))+1);
        maxY/=100;
        maxY+=2;
        final int finalMaxY = maxY;
        AxisLeft.setValueFormatter(new IAxisValueFormatter() {  //Y轴自定义坐标
            @Override
            public String getFormattedValue(float v, AxisBase axisBase) {
                for (int a = 0; a< finalMaxY; a++){     //用个for循环方便
                    if (a==v){
                        return a*100+"h";
                    }
                }

                return "";
            }
        });
        AxisLeft.setAxisMaximum(finalMaxY);   //Y轴最大数值
        AxisLeft.setAxisMinimum(0);   //Y轴最小数值
        //是否隐藏右边的Y轴（不设置的话有两条Y轴 同理可以隐藏左边的Y轴）
        bar.getAxisRight().setEnabled(false);

        //柱子
        barDataSet.setColors(Color.CYAN,Color.GREEN,Color.BLUE,Color.YELLOW);//设置柱子多种颜色  循环使用
        barDataSet.setHighlightEnabled(false);//选中柱子是否高亮显示  默认为true
        //定义柱子上的数据显示    可以实现加单位    以及显示整数（默认是显示小数）
        barDataSet.setValueFormatter(new IValueFormatter() {
            @Override
            public String getFormattedValue(float v, Entry entry, int i, ViewPortHandler viewPortHandler) {
                if (entry.getY()==v){
                    return v*100+"h";
                }
                return "";
            }
        });
        //数据更新
        bar.notifyDataSetChanged();
        bar.invalidate();
    }

    /**
     * 设置数据
     */
    private void showChart() {
        chart = findViewById(R.id.chart);
        //设置web线的颜色(即就是外面包着的那个颜色)
        chart.setWebColorInner(Color.BLACK);
        //设置中心线颜色(也就是竖着的线条)
        chart.setWebColor(Color.BLACK);
        chart.setWebAlpha(50);
        XAxis xAxis = chart.getXAxis();
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float v, AxisBase axisBase) {
                if (v==0){
                    return chartX.get(0);
                }
                if (v==1){
                    return chartX.get(1);
                }
                if (v==2){
                    return chartX.get(2);
                }
                if (v==3){
                    return chartX.get(3);
                }
                if (v==4){
                    return chartX.get(4);
                }
                return "";
            }
        });
        xAxis.setAxisMaximum(4f);
        xAxis.setAxisMinimum(0f);
        xAxis.setTextSize(10f);
        YAxis yAxis = chart.getYAxis();
        //设置y轴的标签个数
//        yAxis.setLabelCount(6, true);
        //设置y轴从0f开始
        yAxis.setAxisMinimum(0f);
        /*启用绘制Y轴顶点标签，这个是最新添加的功能
         * */
        yAxis.setDrawTopYLabelEntry(false);

        //启用线条，如果禁用，则无任何线条
        chart.setDrawWeb(true);

        //禁用图例和图表描述
        chart.getDescription().setEnabled(false);
        chart.getLegend().setEnabled(false);
        List<RadarEntry> list = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            list.add(new RadarEntry(Float.parseFloat(chartY.get(i))));
        }
        RadarDataSet set = new RadarDataSet(list, "Petterp");

        //禁用标签
        set.setDrawValues(false);
        //设置填充颜色
        set.setFillColor(Color.BLUE);
        //设置填充透明度
        set.setFillAlpha(40);
        //设置启用填充
        set.setDrawFilled(true);

        RadarData data = new RadarData(set);
        chart.setData(data);
        chart.invalidate();
    }

    void showTextView()
    {
        tv_app_tag = findViewById(R.id.tv_app_tag);
        tv_man_tag = findViewById(R.id.tv_man_tag);
        tv_app_recomend = findViewById(R.id.tv_app_recomend);
        tv_app_tag.setText("/t过去一年您使用频率 最高(最低) 的是 [ "+barX.get(0)+"("+barX.get(9)+")"+
                " ] 类App,使用时长为 [ "+barY.get(0)+"("+barY.get(9)+")"+" ] 小时\n" +
                "/t具体的各类App的使用频率如下:");
        tv_man_tag.setText("\t您是一个:" +
                "\n\t狂热的 [ "+chartX.get(0)+" ], [ "+ chartX.get(1)+" ] "+
                "\n\t优秀的 [ "+chartX.get(2)+" ] , [ "+ chartX.get(3)+" ] , [ "+chartX.get(4)+"]\n");
        //共推荐三个,Tag1推荐2个,Tag2推荐1个
        String Tag1;
        String Tag2;
        if(!barX.get(0).equals("其他"))
        {
            Tag1=barX.get(0);
            if(!barX.get(1).equals("其他"))
            {
                Tag2=barX.get(1);
            }
            else
            {
                Tag2=barX.get(2);
            }
        }
        else
        {
            Tag1=barX.get(1);
            Tag2=barX.get(2);
        }

        List<String> rstRecomend=new ArrayList<String>();
        List<String> appNames = appTagsMap.getAllAppsInThisTag(Tag1);
        int i = 0;
        for(String appName : appNames)
        {
            if(null == pdao.querySignalAppUsage(appName))
                rstRecomend.add(appName);
            if(i++>2)
                break;
        }
        appNames = appTagsMap.getAllAppsInThisTag(Tag2);
        for(String appName : appNames)
        {
            if(null == pdao.querySignalAppUsage(appName)) {
                rstRecomend.add(appName);
                break;
            }
        }
        tv_app_recomend.setText("\n根据您的人物画像,我们向您推荐使用以下App:\n"+
                " [ "+rstRecomend.get(0)+" ] , [ "+ rstRecomend.get(1)+" ] , [ "+rstRecomend.get(2)+"]\n");
    }
}