package com.example.profiler.projecta;

import android.Manifest;
import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.SupportActivity.ExtraData;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.menu.MenuAdapter;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationServices;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by PROFILER on 9/25/2017.
 */

public class HomeFrag extends Fragment implements View.OnClickListener, LocationListener {

    View myView;
    ImageView emergency;
    ImageView fire;
    ImageView ambulance;
    ImageView police;
    ImageView info;
    ImageView setting;
    ImageView back;
    FusedLocationProviderClient fp;
    double lat;
    double longitude;
    LocationManager locationManager;
    public Criteria criteria;
    public String bestProvider;
    String lati = null;
    String longi = null;
    DatabaseReference mDatabase;
    FirebaseUser fu;
    String p1="";
    String p2="";
    String p3="";
    public final int REQUEST_CODE= 777;
    ProgressDialog progressdialog;
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.def_lay, container, false);

        progressdialog = new ProgressDialog(getActivity());
        progressdialog.setMessage("Please Wait....");
        progressdialog.show();
        ////////////////////////////////////////////////////////////////////////////////////////////
        emergency = myView.findViewById(R.id.emerButton);
        fire = myView.findViewById(R.id.fireImgBut);
        ambulance = myView.findViewById(R.id.ambulanceBut);
        police = myView.findViewById(R.id.policeImgBut);
        info = myView.findViewById(R.id.info_butImg);
        setting = myView.findViewById(R.id.settingsImgbut);
        back = myView.findViewById(R.id.backImgView);

        FirebaseDatabase db = FirebaseDatabase.getInstance();

        mDatabase = db.getReference();

        final FirebaseAuth fa = FirebaseAuth.getInstance();

        fu = fa.getCurrentUser();

        if(fu!= null) {
            progressdialog.dismiss();

            getLoc();
            mDatabase.child(fu.getUid()).child("contacts").child("value1").child("number").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    p1 = String.valueOf(dataSnapshot.getValue());
                    p1.replaceAll("\\s+", "");

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
            mDatabase.child(fu.getUid()).child("contacts").child("value2").child("number").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    p2 = String.valueOf(dataSnapshot.getValue());
                    p2.replaceAll("\\s+", "");

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
            mDatabase.child(fu.getUid()).child("contacts").child("value3").child("number").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    p3 = String.valueOf(dataSnapshot.getValue());
                    p3.replaceAll("\\s+", "");

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            fire.setVisibility(View.INVISIBLE);
            ambulance.setVisibility(View.INVISIBLE);
            police.setVisibility(View.INVISIBLE);

            emergency.setEnabled(true);

            emergency.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    getLoc();
                    fire.setVisibility(View.VISIBLE);
                    ambulance.setVisibility(View.VISIBLE);
                    police.setVisibility(View.VISIBLE);

                    emergency.setEnabled(false);

                }
            });


            back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    getLoc();
                    fire.setVisibility(View.INVISIBLE);
                    ambulance.setVisibility(View.INVISIBLE);
                    police.setVisibility(View.INVISIBLE);

                    emergency.setEnabled(true);
                }
            });


            fire.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    getLoc();
                    sendSMS("+919944539967", " " + lati + " " + longi + " fire");
                    if (p1.equalsIgnoreCase("") && p2.equalsIgnoreCase("") && p3.equalsIgnoreCase("")) {

                    } else {
                        sendSMS(p1, " " + lati + " " + longi + " Emergency");
                        sendSMS(p2, " " + lati + " " + longi + " Emergency");
                        sendSMS(p3, " " + lati + " " + longi + " Emergency");
                    }
                    DataModel dm = new DataModel("fire", "lat:" + lati + " long:" + longi, fu.getEmail());
                    mDatabase.child("events").push().setValue(dm);

                    Toast.makeText(getActivity(), "Fire", Toast.LENGTH_SHORT).show();
                }
            });

            ambulance.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    getLoc();
                    sendSMS("+919944539967", " " + lati + " " + longi + " ambulance");
                    if (p1.equalsIgnoreCase("") && p2.equalsIgnoreCase("") && p3.equalsIgnoreCase("")) {

                    } else {
                        sendSMS(p1, " " + lati + " " + longi + " Emergency");
                        sendSMS(p2, " " + lati + " " + longi + " Emergency");
                        sendSMS(p3, " " + lati + " " + longi + " Emergency");
                    }
                    DataModel dm = new DataModel("ambulance", "lat:" + lati + " long:" + longi, fu.getEmail());
                    mDatabase.child("events").push().setValue(dm);
                }
            });

            police.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    getLoc();
                    sendSMS("+919944539967", " " + lati + " " + longi + " police");
                    if (p1.equalsIgnoreCase("") && p2.equalsIgnoreCase("") && p3.equalsIgnoreCase("")) {

                    } else {
                        sendSMS(p1, " " + lati + " " + longi + " Emergency");
                        sendSMS(p2, " " + lati + " " + longi + " Emergency");
                        sendSMS(p3, " " + lati + " " + longi + " Emergency");
                    }
                    DataModel dm = new DataModel("police", "lat:" + lati + " long:" + longi, fu.getEmail());
                    mDatabase.child("events").push().setValue(dm);
                    Toast.makeText(getActivity(), "Police", Toast.LENGTH_SHORT).show();
                }
            });

            info.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(getActivity(), "Info", Toast.LENGTH_SHORT).show();
                }
            });

            setting.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(getActivity(), "Setting", Toast.LENGTH_SHORT).show();
                }
            });
        }

        return myView;

    }




    @RequiresApi(api = Build.VERSION_CODES.M)
    public void getLoc() {

        fp = LocationServices.getFusedLocationProviderClient(getActivity());

        try {
            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                return;

            }
            fp.getLastLocation().addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {


                    if (location != null) {
                        lat = location.getLatitude();
                        longitude = location.getLongitude();

                        lati = String.valueOf(location.getLatitude());
                        longi = String.valueOf(location.getLongitude());

                    }


                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            });
        } catch (Exception e) {
            Toast.makeText(getActivity(), "Location cannot be resolved", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }







    public void sendSMS(String phoneNo, String msg) {
        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNo, null, msg, null, null);
            Toast.makeText(getActivity(), "Message Sent",
                    Toast.LENGTH_LONG).show();
        } catch (Exception ex) {
            Toast.makeText(getActivity(),ex.getMessage().toString(),
                    Toast.LENGTH_LONG).show();
            ex.printStackTrace();
        }
    }


    @Override
    public void onClick(View view) {





    }
    @Override
    public void onLocationChanged(Location location) {

        lat = location.getLatitude();
        longitude = location.getLongitude();

        lati = String.valueOf(location.getLatitude());
        longi = String.valueOf(location.getLongitude());

    }



}
