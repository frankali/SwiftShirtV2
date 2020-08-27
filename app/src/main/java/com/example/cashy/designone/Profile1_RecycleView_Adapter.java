package com.example.cashy.designone;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;


import modalclass.Profile1_ModalClass;


public class Profile1_RecycleView_Adapter extends RecyclerView.Adapter<Profile1_RecycleView_Adapter.MyViewHolder> {

    Context context;
    AlphaAnimation alpha;
    private List<Profile1_ModalClass> albumListModalClasses;
    TextView createNew;


    public Profile1_RecycleView_Adapter(Context mainActivityContacts, List<Profile1_ModalClass> listModalClassList) {
        this.albumListModalClasses = listModalClassList;
        this.context = mainActivityContacts;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        final View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.workout_card, parent, false);

        PieChart pieChart = (PieChart) itemView.findViewById(R.id.piechart);


        alpha = new AlphaAnimation(0, 1);
        alpha.setDuration(2000);
        pieChart.setAnimation(alpha);



        pieChart.setUsePercentValues(true);


        // IMPORTANT: In a PieChart, no values (Entry) should have the same
        // xIndex (even if from different DataSets), since no values can be
        // drawn above each other.
        ArrayList<Entry> yvalues = new ArrayList<Entry>();
        yvalues.add(new Entry(8f, 0));
        yvalues.add(new Entry(15f, 1));
        yvalues.add(new Entry(12f, 2));
        yvalues.add(new Entry(25f, 3));
        yvalues.add(new Entry(23f, 4));
        yvalues.add(new Entry(17f, 5));

        PieDataSet dataSet = new PieDataSet(yvalues, "Election Results");

        ArrayList<String> xVals = new ArrayList<String>();

        xVals.add("Biceps");
        xVals.add("Triceps");
        xVals.add("Arms");
        xVals.add("Back");
        xVals.add("Chest");
        xVals.add("Abs");

        PieData data = new PieData(xVals, dataSet);

        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(9.5f);
        data.setValueTextColor(Color.DKGRAY);

        pieChart.setData(data);
        pieChart.setDescription("Muscle Chart");


        // set custom pastel colors on wheel, fine tune
        //mess with borders
        // work on Active Workout!

        pieChart.setDrawHoleEnabled(true);
        pieChart.setTransparentCircleRadius(30f);
        pieChart.setHoleRadius(30f);
        int[] colors = new int[]{Color.rgb(255,204,249), Color.rgb(213,170,255),
                Color.rgb(181, 185, 255), Color.rgb(133,227,255),
                Color.rgb(243,255,227), Color.rgb(255,190,188)};
        dataSet.setColors(ColorTemplate.createColors(colors));



        pieChart.getLegend().setEnabled(false);
        pieChart.animateXY(1400, 1400);




        return new MyViewHolder(itemView);




    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        Profile1_ModalClass modalClass = albumListModalClasses.get(position);
        holder.txtlike.setText(modalClass.getLike());
        holder.txtcomment.setText(modalClass.getComment());
        holder.txttime.setText(modalClass.getTime());
    }

    @Override
    public int getItemCount() {
        return albumListModalClasses.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {


        TextView txtlike, txtcomment, txttime;


        public MyViewHolder(View view) {
            super(view);


            txtlike = (TextView) view.findViewById(R.id.txtlike1);
            txtcomment = (TextView) view.findViewById(R.id.txtcomment1);
            txttime = (TextView) view.findViewById(R.id.txttime1);


        }


    }




}
