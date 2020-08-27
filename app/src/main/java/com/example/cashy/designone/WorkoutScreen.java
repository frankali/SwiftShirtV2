package com.example.cashy.designone;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import static java.lang.Thread.sleep;


public class WorkoutScreen extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    Button startWorkout;
    Button stopWorkout;
    int workoutID = -1;
    boolean reading;
    boolean workingOut = false;
    int delay = 100;
    int repcount = 0;
    int restcount = 0;
    int bigRep = 0;
    boolean resting = false;
    int sets = 0;
    ArrayList<Integer> workout = new ArrayList<>();
    ArrayList<String> muscleGroups = new ArrayList<>();

    RelativeLayout stopWatchContainer;
    ImageView heartIcon;
    AlphaAnimation heartAlpha;
    TextView timer;
    long MillisecondTime, StartTime, TimeBuff, UpdateTime = 0L ;
    Handler handler;
    int Seconds, Minutes, MilliSeconds ;

    LinearLayout connectingLayout;
    LinearLayout heartContainer;
    LinearLayout wifiContainer;
    ProgressBar progressBar;

    Button endButton;
    Button pauseButton;
    Button resumeButton;



    public WorkoutScreen() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_workout_screen, container, false);
        startWorkout = view.findViewById(R.id.startWorkout);
        heartIcon = view.findViewById(R.id.heartIcon);
      //  stopWorkout = view.findViewById(R.id.stopWorkout);


        //WIFI SYMBOL
        //transmitting -- in and out
        // connecting symbol for shirt connection
        //heart beat in and out

        stopWatchContainer = view.findViewById(R.id.stopWatchLayout);
        heartContainer = view.findViewById(R.id.heartBeatContainer);
        connectingLayout = view.findViewById(R.id.connectingLayout);
        progressBar = view.findViewById(R.id.progressBar);
        wifiContainer = view.findViewById(R.id.wifiContainer);

        pauseButton = view.findViewById(R.id.btPause);
        endButton =  view.findViewById(R.id.btStart);
        resumeButton = view.findViewById(R.id.btResume);

        onStartWorkout();
        onStopWorkout();
        addMuscles();



        workoutBuilder(3, 10, 10);
        System.out.println(workout);


        heartAlpha = new AlphaAnimation(0, 1);
        heartAlpha.setDuration(2000);






        getActivity().runOnUiThread(new Runnable() {

            @Override
            public void run() {

                heartAlpha.setRepeatMode(2);
                heartAlpha.setRepeatCount(Animation.INFINITE);
                heartIcon.startAnimation(heartAlpha);


            }
        });

        handler = new Handler();
        timer = view.findViewById(R.id.tvTimer);

        pauseHandler();
        resumehandler();
        endHandler();

        return view;
    }

    public void onStartWorkout() {








        startWorkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Handler handler = new Handler();
                startWorkout.setVisibility(View.GONE);
                connectingLayout.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.VISIBLE);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        connectingLayout.setVisibility(View.GONE);
                        progressBar.setVisibility(View.GONE);
                        stopWatchContainer.setVisibility(View.VISIBLE);
                        heartContainer.setVisibility(View.VISIBLE);
                        wifiContainer.setVisibility(View.VISIBLE);
                        Toast.makeText(getContext(), "Connected to Swift Shirt!", Toast.LENGTH_SHORT)
                        .show();
                    }
                }, 3500);
                clockStart();


                startWorkoutPost((TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis())));


                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        // simulateFullBodyWorkout();
                        simulateCustomWorkout("abWorkout");
                    }
                }).start();



            }
        });

    }

    public void onStopWorkout() {


    }

    public void startWorkoutPost(final long time) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    InputStream inputStream;
                    String ResponseData;
                    URL url = new URL("http://api.swiftshirt.io/api/v1/workout/start");
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
                    conn.setRequestProperty("Accept", "application/json");
                    conn.setDoOutput(true);
                    conn.setDoInput(true);

                    JSONObject jsonParam = new JSONObject();
                    jsonParam.put("created_at", time);
                    jsonParam.put("user_id", 6);


                    Log.i("STARTING: ", "WORKOUT");
                    Log.i("JSON SENT:", jsonParam.toString());
                    DataOutputStream os = new DataOutputStream(conn.getOutputStream());
                    //os.writeBytes(URLEncoder.encode(jsonParam.toString(), "UTF-8"));
                    os.writeBytes(jsonParam.toString());

                    os.flush();
                    os.close();

                    Log.i("STATUS", String.valueOf(conn.getResponseCode()));
                    Log.i("MSG", conn.getResponseMessage());

                    inputStream = new BufferedInputStream(conn.getInputStream());

                    // GET THE JSON STRING RESPONSE
                    ResponseData = convertStreamToString(inputStream);

                    //CREATE JSON OBJECT FROM STRING
                    JSONObject jsonObject = new JSONObject(ResponseData);
                    Log.i("JSON RECIEVED:", ResponseData);

                    //GET INT FROM JSON OBJECT
                    workoutID = jsonObject.getInt("workout_id");

                    conn.disconnect();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


        thread.start();

    }

    public String convertStreamToString(InputStream is) {

        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append((line + "\n"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }

    public void sendPost(final String charName, final int charValue, final long time, final int workoutID) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL("http://api.swiftshirt.io/api/v1/raw");
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
                    conn.setRequestProperty("Accept", "application/json");
                    conn.setDoOutput(true);
                    conn.setDoInput(true);

                    JSONObject jsonParam = new JSONObject();
                    jsonParam.put("created_at", time);
                    jsonParam.put("name", charName);
                    jsonParam.put("value", charValue);
                    jsonParam.put("workout_id", workoutID);


                    Log.i("JSON", jsonParam.toString());
                    DataOutputStream os = new DataOutputStream(conn.getOutputStream());
                    //os.writeBytes(URLEncoder.encode(jsonParam.toString(), "UTF-8"));
                    os.writeBytes(jsonParam.toString());

                    os.flush();
                    os.close();

                    Log.i("STATUS", String.valueOf(conn.getResponseCode()));
                    Log.i("MSG", conn.getResponseMessage());

                    conn.disconnect();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();
    }

    public void endWorkoutPost(final long time) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    InputStream inputStream;
                    String ResponseData;
                    URL url = new URL("http://api.swiftshirt.io/api/v1/workout/end");
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
                    conn.setRequestProperty("Accept", "application/json");
                    conn.setDoOutput(true);
                    conn.setDoInput(true);

                    JSONObject jsonParam = new JSONObject();
                    jsonParam.put("workout_id", workoutID);
                    jsonParam.put("finished_at", time);


                    Log.i("ENDING: ", "WORKOUT");
                    Log.i("JSON SENT:", jsonParam.toString());
                    DataOutputStream os = new DataOutputStream(conn.getOutputStream());
                    //os.writeBytes(URLEncoder.encode(jsonParam.toString(), "UTF-8"));
                    os.writeBytes(jsonParam.toString());

                    os.flush();
                    os.close();

                    Log.i("STATUS", String.valueOf(conn.getResponseCode()));
                    Log.i("MSG", conn.getResponseMessage());

                    inputStream = new BufferedInputStream(conn.getInputStream());

                    // GET THE JSON STRING RESPONSE
                    ResponseData = convertStreamToString(inputStream);

                    Log.i("JSON RECIEVED:", ResponseData);


                    conn.disconnect();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();


    }

    private void addMuscles() {
        // muscleGroups.add("HeartRate");

        muscleGroups.add("bicep");
        muscleGroups.add("tricep");
        muscleGroups.add("back");
        muscleGroups.add("forearm");
        muscleGroups.add("chest");
        muscleGroups.add("shoulder");
        muscleGroups.add("ab");

    }

    private void workoutBuilder(int sets, int reps, int rest) {

        workout.add(0);
        workout.add(0);
        workout.add(0);
        workout.add(0);
        workout.add(0);


        //Each integer is separated by a 200ms delay

        for (int numsets = 0; numsets < sets; numsets++) {
            if (numsets > 0) {
                for (int i = 0; i < rest * 5; i++) {
                    workout.add(0);
                }
            }
            for (int numreps = 0; numreps < reps; numreps++) {
                workout.add(4000);
                workout.add(4000);
                workout.add(4000);
                workout.add(4000);
                workout.add(4000);
                workout.add(0);
                workout.add(0);
                workout.add(0);
                workout.add(0);
                workout.add(0);
            }


        }

    }

    //data with no stop, free flow workout
    private void simulateDat2a() {

        Thread myThread = new Thread() {
            @Override
            public void run() {
                try {

                    sleep(1000);
                    System.out.println(workoutID);
                    while (reading) {
                        if (workingOut) {

                            if (repcount == 10) {
                                System.out.println(4000);
                                sendPost("Forearm", 4000,
                                        TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis()),
                                        workoutID);
                                repcount = 0;
                                workingOut = false;
                                resting = true;
                                System.out.println("Rep");
                            } else {
                                System.out.println(4000);
                                sendPost("Forearm", 4000,
                                        TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis()),
                                        workoutID);
                                repcount++;
                            }
                            sleep(100);

                        }

                        if (resting) {
                            if (restcount == 10) {
                                System.out.println(0);
                                sendPost("Forearm", 0,
                                        TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis()),
                                        workoutID);
                                restcount = 0;
                                workingOut = true;
                                resting = false;
                            } else {
                                System.out.println(0);

                                sendPost("Forearm", 0,
                                        TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis()),
                                        workoutID);
                                restcount++;
                            }
                            sleep(100);
                        }
                    }

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        myThread.start();


    }

    private void simulateFullBodyWorkout() {
        Thread myThread = new Thread() {
            @Override
            public void run() {
                try {

                    System.out.println("WORKOUT BEGINNING");
                   /* for (int i : workout) {
                        for(String muscle: muscleGroups) {
                            sendPost(muscle, i,
                                    TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis()),
                                    workoutID);
                        }

                        sleep(250);

                    } */

                    for (String muscle : muscleGroups) {
                        for (int i : workout) {
                            sendPost("left_" + muscle, i,
                                    TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis()),
                                    workoutID);
                            sendPost("right_" + muscle, i,
                                    TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis()),
                                    workoutID);
                            sleep(250);
                        }
                    }
                    System.out.println("WORKOUT ENDING");
                    endWorkoutPost(TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis()));


                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        myThread.start();
    }


    /*

    ONLY SENDING LEFT OR RIGHT!!!!!!
    ONLY SENDING LEFT OR RIGHT!!!!!!
    ONLY SENDING LEFT OR RIGHT!!!!!!ONLY SENDING LEFT OR RIGHT!!!!!!
    ONLY SENDING LEFT OR RIGHT!!!!!!

     */
    private void simulateCustomWorkout(final String workoutChosen) {

        final ArrayList<String> armWorkout = new ArrayList<>();
        armWorkout.add("bicep");
        armWorkout.add("tricep");
        armWorkout.add("forearm");
        armWorkout.add("shoulder");

        final ArrayList<String> chestWorkout = new ArrayList<>();
        chestWorkout.add("chest");
        chestWorkout.add("shoulder");
        chestWorkout.add("tricep");

        final ArrayList<String> backWorkout = new ArrayList<>();
        backWorkout.add("back");
        backWorkout.add("bicep");
        backWorkout.add("forearm");

        final ArrayList<String> abWorkout = new ArrayList<>();
        abWorkout.add("ab");

        final ArrayList<String> fullbodyWorkout = new ArrayList<>();
        fullbodyWorkout.add("chest");
        fullbodyWorkout.add("back");
        fullbodyWorkout.add("bicep");
        fullbodyWorkout.add("tricep");
        fullbodyWorkout.add("forearm");
        fullbodyWorkout.add("shoulder");
        fullbodyWorkout.add("ab");


        final HashMap<String, ArrayList> workoutNametoMuscleList = new HashMap<>();
        workoutNametoMuscleList.put("chestWorkout", chestWorkout);
        workoutNametoMuscleList.put("backWorkout", backWorkout);
        workoutNametoMuscleList.put("armWorkout", armWorkout);
        workoutNametoMuscleList.put("abWorkout", abWorkout);
        workoutNametoMuscleList.put("fullbodyWorkout", fullbodyWorkout);


        Thread myThread = new Thread() {
            @Override
            public void run() {
                try {

                    ArrayList<String> workoutSelected = workoutNametoMuscleList.get(workoutChosen);
                    System.out.println("WORKOUT BEGINNING");


                    for (String muscle : workoutSelected) {
                        for (int i : workout) {
                            sendDualMuscleData(i, muscle);
                            sleep(250);
                        }

                    }

                    System.out.println("WORKOUT ENDING");
                    endWorkoutPost(TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis()));
                    Looper.prepare();
                    endDialog();



                } catch (
                        InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        myThread.start();

    }

    private void sendDualMuscleData(int value, String muscle) {

        sendPost("left_" + muscle, value,
                TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis()),
                workoutID);
        sendPost("right_" + muscle, value,
                TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis()),
                workoutID);

    }

    private void sendRightData(int value, String muscle) {

        sendPost("right_" + muscle, value,
                TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis()),
                workoutID);
    }

    private void sendLeftData(int value, String muscle) {

        sendPost("left_" + muscle, value,
                TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis()),
                workoutID);
    }



    public Runnable runnable = new Runnable() {

        public void run() {

            MillisecondTime = SystemClock.uptimeMillis() - StartTime;

            UpdateTime = TimeBuff + MillisecondTime;

            Seconds = (int) (UpdateTime / 1000);

            Minutes = Seconds / 60;

            Seconds = Seconds % 60;

            MilliSeconds = (int) (UpdateTime % 1000);

            timer.setText("" + Minutes + ":"
                    + String.format("%02d", Seconds) + ":"
                    + String.format("%03d", MilliSeconds));

            handler.postDelayed(this, 0);
        }

    };

    private void clockStart() {
        StartTime = SystemClock.uptimeMillis();
        handler.postDelayed(runnable, 0);

    }

    private void pauseHandler() {
        pauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                pauseButton.setVisibility(View.GONE);
                resumeButton.setVisibility(View.VISIBLE);

                TimeBuff += MillisecondTime;

                handler.removeCallbacks(runnable);


            }
        });


    }

    private void endHandler() {

        endButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                TimeBuff += MillisecondTime;

                handler.removeCallbacks(runnable);

                pauseButton.setEnabled(false);
                resumeButton.setEnabled(false);
                endDialog();


            }
        });
    }

    private void endDialog() {
        AlertDialog alertDialog = new AlertDialog.Builder(getContext())
//set icon
                .setIcon(android.R.drawable.ic_dialog_alert)
//set title
                .setTitle("Workout Complete!")
//set message
                .setMessage("Great Workout! Your data is currently being processed. " +
                        "An in-depth analysis will " +
                        "be available very soon at swiftshirt.io")
//set positive button
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        FragmentTransaction fragmentTransaction = getActivity().
                                getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.replace(R.id.HomePageContainer, new Profile());
                        fragmentTransaction.commit();

                    }
                })

                .show();


    }
    private void resumehandler() {
        resumeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pauseButton.setVisibility(View.VISIBLE);
                resumeButton.setVisibility(View.GONE);
                StartTime = SystemClock.uptimeMillis();
                handler.postDelayed(runnable, 0);

            }
        });
    }






}




