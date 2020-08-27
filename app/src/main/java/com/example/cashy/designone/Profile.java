package com.example.cashy.designone;

import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

import modalclass.Profile1_ModalClass;


public class Profile extends Fragment {

    RecyclerView profile1_recycleview;

    Profile1_RecycleView_Adapter mProfile1_Adapter;


    AlphaAnimation alpha;
    Handler handler;
    ImageView splashView;
    PieChart pieChart;

    TextView createNew;


    private ArrayList<Profile1_ModalClass> profile1ModalClassArrayList;

    public Profile() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        profile1_recycleview = view.findViewById(R.id.collection_recycleview);


        // Set Profile 1 Adapter
        profile1ModalClassArrayList = new ArrayList<>();
        profile1ModalClassArrayList.add(new Profile1_ModalClass("97 bpm","320 kcal","5h ago"));
        profile1ModalClassArrayList.add(new Profile1_ModalClass("112 bpm","480 kcal","3h ago"));
        profile1ModalClassArrayList.add(new Profile1_ModalClass("145 bpm","500 kcal","9h ago"));
        profile1ModalClassArrayList.add(new Profile1_ModalClass("120 bpm","230 kcal","12h ago"));
        profile1ModalClassArrayList.add(new Profile1_ModalClass("101 bpm","450 kcal","20h ago"));
        profile1ModalClassArrayList.add(new Profile1_ModalClass("100 bpm","120 kcal","11h ago"));


        mProfile1_Adapter = new Profile1_RecycleView_Adapter(getActivity(),profile1ModalClassArrayList);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        LinearLayoutManager layoutManager1 = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        profile1_recycleview.setLayoutManager(layoutManager1);
        profile1_recycleview.setAdapter(mProfile1_Adapter);


        createNew = view.findViewById(R.id.createNew);
        createNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.replace(R.id.HomePageContainer, new WorkoutScreen());
                fragmentTransaction.commit();
            }
        });



        // View otherView = inflater.inflate(R.layout.workout_card, container, false);
       // piChart(otherView);
        return view;
    }




}
