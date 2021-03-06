package com.example.rodolfotrevino.digitalgate;

/**
 * Created by Steven J on 2/17/2017.
 */

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.estimote.sdk.Beacon;
import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.EstimoteSDK;
import com.estimote.sdk.Nearable;
import com.estimote.sdk.Region;
import com.estimote.sdk.SecureRegion;
import com.estimote.sdk.SystemRequirementsChecker;
import com.estimote.sdk.cloud.model.Color;
import com.example.rodolfotrevino.digitalgate.estimote.BeaconID;
import com.example.rodolfotrevino.digitalgate.estimote.EstimoteCloudBeaconDetails;
import com.example.rodolfotrevino.digitalgate.estimote.EstimoteCloudBeaconDetailsFactory;
import com.example.rodolfotrevino.digitalgate.estimote.NearableID;
import com.example.rodolfotrevino.digitalgate.estimote.ProximityContentManager;
// import com.fasterxml.jackson.databind.ObjectMapper;
import android.os.RemoteException;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;


import static com.estimote.sdk.internal.utils.EstimoteBeacons.ESTIMOTE_PROXIMITY_UUID;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "Beacons";

    String name;
    private static String status = "inactive";
    private BeaconManager beaconManager;

    //TestingCarAdapter adapter;

    private SecureRegion secureRegion;
    private SecureRegion secureRegion2;
    private SecureRegion secureRegion3;
    private Region region;



    private static final Region ALL_ESTIMOTE_BEACONS = new Region("rid", ESTIMOTE_PROXIMITY_UUID, null, null);

    private static final Map<Color, Integer> BACKGROUND_COLORS = new HashMap<>();

    static {
        BACKGROUND_COLORS.put(Color.ICY_MARSHMALLOW, android.graphics.Color.rgb(109, 170, 199));
        BACKGROUND_COLORS.put(Color.BLUEBERRY_PIE, android.graphics.Color.rgb(98, 84, 158));
        BACKGROUND_COLORS.put(Color.MINT_COCKTAIL, android.graphics.Color.rgb(155, 186, 160));
    }

    private static final int BACKGROUND_COLOR_NEUTRAL = android.graphics.Color.rgb(160, 169, 172);

    private ProximityContentManager proximityContentManager;
    private static final Region ALL_ESTIMOTE_BEACONS_REGION = new Region("rid", null, null, null);


    public static Context getContext() {
        return context;
    }

    private static Context context = null;
    public TextView messageTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        messageTV = (TextView) findViewById(R.id.messageTV);

        EstimoteSDK.initialize(getApplicationContext(), "stevenjoy99-yahoo-com-s-yo-lyx", "0f4d0fa349ea5d6604f52b776a9653c8");

        context = this;


        // use for proximity Beacons STOP RANGING
        secureRegion = new SecureRegion("Secure region", UUID.fromString("B9407F30-F5F8-466E-AFF9-25556B57FE6D"), 20930, 14720);
        secureRegion2 = new SecureRegion("Secure region", UUID.fromString("B9407F30-F5F8-466E-AFF9-25556B57FE6D"), 26788, 12168);
        secureRegion3 = new SecureRegion("Secure region", UUID.fromString("B9407F30-F5F8-466E-AFF9-25556B57FE6D"), 20859, 25702);

        Log.d("Tag", "Beacons");
        proximityContentManager = new ProximityContentManager(this,
                Arrays.asList(
                        /** Proximity Beacons Identifier, minor and major*/
                        //new BeaconID("B9407F30-F5F8-466E-AFF9-25556B57FE6D", 32725, 55822), //
                        new BeaconID("B9407F30-F5F8-466E-AFF9-25556B57FE6D", 5323, 38267), //Candy
                        new BeaconID("B9407F30-F5F8-466E-AFF9-25556B57FE6D", 60973, 22221), //Beetroot
                        new BeaconID("B9407F30-F5F8-466E-AFF9-25556B57FE6D", 20930, 14720), //Ice
                        new BeaconID("B9407F30-F5F8-466E-AFF9-25556B57FE6D", 26788, 12168), //Mint
                        new BeaconID("B9407F30-F5F8-466E-AFF9-25556B57FE6D", 3138, 64033)), //Lemon

                new EstimoteCloudBeaconDetailsFactory());

//        /** listener used for nearable stickers*/
//        beaconManager = new BeaconManager(getApplicationContext());
//
//        beaconManager.connect(new BeaconManager.ServiceReadyCallback() {
//            @Override
//            public void onServiceReady() {
//                try {
//                    beaconManager.startRanging(ALL_ESTIMOTE_BEACONS);
//
//                } catch (Exception e) {
//                    Log.e("error", "Cannot start ranging", e);
//                }
//            }
//        });


//        beaconManager.setMonitoringListener(new BeaconManager.MonitoringListener() {
//
//            @Override
//            public void onEnteredRegion(Region region, List<Beacon> beacons) {
//                Log.d("Beacon Found", region.toString());
//
//                for (Beacon beacon : beacons) {
//                    String id = String.valueOf(beacon.getMajor()) + ":" + String.valueOf(beacon.getMinor());
//                    messageTV.setText(id);
//                }
//            }
//
//            @Override
//            public void onExitedRegion(Region region) {
//                // could add an "exit" notification too if you want (-:
//            }
//        });

//
//        beaconManager.setNearableListener(new BeaconManager.NearableListener() {
//            @Override
//            public void onNearablesDiscovered(List<Nearable> nearables) {
//
//                clearList();
//
//
//                /** loops through nearable ID's*/
//                for (Nearable nearable : nearables) {
//                    if (!nearableMap.containsValue(nearable.identifier)) {
//                        nearableId = nearable.identifier;
//                        Runnable runnable = new Runnable() {
//                            @Override
//                            public void run() {
//                                try {
//                                    beaconsInRange(nearableId);
//                                    //adapter.notifyDataSetChanged();
//
//
//                                } catch (IOException e) {
//                                    e.printStackTrace();
//                                } catch (JSONException e) {
//                                    e.printStackTrace();
//                                }
//                            }
//                        };
//
//                        Thread thread = new Thread(runnable);
//                        thread.start();
//
//                        try {
//                            thread.join();
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();
//                        }
//
//                    }
//                }
//
//                //setBeaconBackground();
//                adapter.notifyDataSetChanged();
//            }
//
//        });
//
//
//        /** Broadcast the nearable Beacons signal*/
//        beaconManager.connect(new BeaconManager.ServiceReadyCallback() {
//            @Override
//            public void onServiceReady() {
//
//                // Beacons ranging.
//                beaconManager.startRanging(ALL_ESTIMOTE_BEACONS);
//
//                // Nearable discovery.
//                beaconManager.startNearableDiscovery();
//
//            }
//        });

//        /** Broadcast the nearable Beacons signal*/
//        beaconManager.connect(new BeaconManager.ServiceReadyCallback() {
//            @Override
//            public void onServiceReady() {
//
//                // Beacons ranging.
//                beaconManager.startRanging(ALL_ESTIMOTE_BEACONS);
//
//            }
//        });

        beaconManager = new BeaconManager(this);
        beaconManager.setRangingListener(new BeaconManager.RangingListener() {
            @Override
            public void onBeaconsDiscovered(Region region, List<Beacon> list) {
                Log.d("Rudy", "Found Beacon");
                if (!list.isEmpty()) {
                    for (Beacon beacon : list) {
                        if (beacon.getMajor() == 60973) {
                            Log.d("Key", "Beetroot");
                            String text = "You're in Beetroot's range!";
                            messageTV.setText(text);

                            Runnable runnable = new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        name = exitPremise("60973");
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }

                            };
                            Thread thread = new Thread(runnable);
                            thread.start();

                            try {
                                thread.join();
                            } catch (Exception e) {
                                return;
                            }

                            if (status.equals("inactive")) {
                                messageTV.setText("Sorry Error");
                            } else {
                                messageTV.setText("Have a good trip " + name);
                            }

                        }
                    }

                }
            }
        });

//        proximityContentManager.setListener(new ProximityContentManager.Listener() {
//            @Override
//            public void onContentChanged(Object content) {
//                String text;
//
//                if (content != null) {
//                    EstimoteCloudBeaconDetails beaconDetails = (EstimoteCloudBeaconDetails) content;
//                    //BeaconID beaconID = (BeaconID) content;
//
//                    //String id = beaconID.getProximityUUID().toString() + beaconID.getMajor() + beaconID.getMinor();
//                    //Log.d("Beacon Found!!!!", id);
//
//                    //GATE Code
//                    if (beaconDetails.getBeaconName().equals("ice")) {
//                        Log.d("Key", "Ice Found");
//                        text = "You're in " + beaconDetails.getBeaconName() + "'s range!";
//                        messageTV.setText(text);
//
//                        Runnable runnable = new Runnable() {
//                            @Override
//                            public void run() {
//                                try {
//                                    name = exitPremise("Ice");
//                                }catch (IOException e) {
//                                    e.printStackTrace();
//                                } catch (JSONException e) {
//                                    e.printStackTrace();
//                                }
//                            }
//
//                        };
//                        Thread thread = new Thread(runnable);
//                        thread.start();
//
//                        try {
//                            thread.join();
//                        } catch (Exception e) {
//                            return;
//                        }
//
//                        if (status.equals("inactive")){
//                            messageTV.setText("Sorry Error");
//                        }else {
//                            messageTV.setText("Have a good trip " + name);
//                        }
//
//                    }
//
//                }
//            }
//        });

        region = new Region("ranged region", UUID.fromString("B9407F30-F5F8-466E-AFF9-25556B57FE6D"), null, null);

    }


    @Override
    protected void onResume() {
        super.onResume();

        if (!SystemRequirementsChecker.checkWithDefaultDialogs(this)) {
            Log.e(TAG, "Can't scan for Beacons, some pre-conditions were not met");
            Log.e(TAG, "Read more about what's required at: http://estimote.github.io/Android-SDK/JavaDocs/com/estimote/sdk/SystemRequirementsChecker.html");
            Log.e(TAG, "If this is fixable, you should see a popup on the app's screen right now, asking to enable what's necessary");
        } else {
            Log.d(TAG, "Starting ProximityContentManager content updates");
            proximityContentManager.startContentUpdates();
        }

        beaconManager.connect(new BeaconManager.ServiceReadyCallback() {
            @Override
            public void onServiceReady() {
                beaconManager.startRanging(region);
            }
        });
    }


    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "Stopping ProximityContentManager content updates");
        proximityContentManager.stopContentUpdates();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        proximityContentManager.destroy();
        //beaconManager.disconnect();
    }

    public static String exitPremise(String beacon) throws IOException, JSONException {
        URL url = null;
        try {
            url = new URL("http://34.205.184.198/gate.php?id=60973");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        HttpURLConnection urlConnection = null;

        urlConnection = (HttpURLConnection) url.openConnection();
        BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
        StringBuilder result = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            result.append(line);
        }

        String resultString = result.toString();

        //use case for car not reserved
        if (resultString.equals("No Results")) {
            //status = "inactive";
            return null;
        }

        JSONObject results = new JSONObject(resultString);

        //use car is not flagged to leave lot
        status = results.getString("status");

        return results.getString("name");
    }

}