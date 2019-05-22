package com.example.profiler.projecta;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MenuActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String CHANNEL_ID ="baseNotif" ;
    FusedLocationProviderClient fsdc;
    double latitude;
    double longi;
    DatabaseReference mDatabase;
    FirebaseAuth fa= FirebaseAuth.getInstance();
    FirebaseUser  fu = fa.getCurrentUser();
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);


        mDatabase = FirebaseDatabase.getInstance().getReference();


        mDatabase.child("events").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {


                DataModel dm = dataSnapshot.getValue(DataModel.class);

                if(dm.uid!=fu.getEmail()) {

                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                        NotificationChannel bch = new NotificationChannel(CHANNEL_ID, "Normal", NotificationManager.IMPORTANCE_DEFAULT);
                        NotificationManager nm;
                        nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                        nm.createNotificationChannel(bch);
                        Notification nf = new Notification.Builder(MenuActivity.this)
                                .setContentTitle("Help!!")
                                .setContentText(dm.getUid() + " " + dm.getSec() + " " + dm.getFirst())
                                .setSmallIcon(R.drawable.red_icon)
                                .setChannelId(CHANNEL_ID)
                                .build();
                        nm.notify(1145, nf);
                    }
                    if(android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.O) {
                        Notification nf = new Notification.Builder(MenuActivity.this)
                                .setContentTitle("Help!!")
                                .setContentText(dm.getUid() + " " + dm.getSec() + " " + dm.getFirst())
                                .setSmallIcon(R.drawable.red_icon)
                                .build();
                        NotificationManager m = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                        m.notify(123, nf);
                    }
                }

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        mDatabase.child("events").orderByKey().equalTo("fire").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });





        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        android.app.FragmentManager frag1 = getFragmentManager();
        frag1.beginTransaction()
                .replace(R.id.content_frame, new HomeFrag())
                .commit();





        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View header=navigationView.getHeaderView(0);

        TextView tvName = header.findViewById(R.id.textView);
        TextView tvemail = header.findViewById(R.id.textView3);
        if (fu!= null) {
            tvName.setText(fu.getDisplayName());
            tvemail.setText(fu.getEmail());
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

         if(id == R.id.logMen) {
             AuthUI.getInstance()
                     .signOut(this)
                     .addOnCompleteListener(new OnCompleteListener<Void>() {
                         @Override
                         public void onComplete(@NonNull Task<Void> task) {
                             Toast.makeText(MenuActivity.this, "Logged out", Toast.LENGTH_SHORT).show();

                             startActivity(new Intent(MenuActivity.this,MainActivity.class));
                         }
                     });
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        android.app.FragmentManager fragmentManager = getFragmentManager();

        if (id == R.id.home123){
            fragmentManager.beginTransaction()
                    .replace(R.id.content_frame,new HomeFrag())
                    .commit();

        } else if (id == R.id.contactsMen){
            fragmentManager.beginTransaction()
                    .replace(R.id.content_frame,new ContactsFrag())
                    .commit();
        }else if(id  == R.id.logout){
            AuthUI.getInstance()
                    .signOut(this)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(MenuActivity.this, "Logged out", Toast.LENGTH_SHORT).show();

                            startActivity(new Intent(MenuActivity.this,MainActivity.class));
                        }
                    });
        }else if(id  == R.id.editFrag){
            fragmentManager.beginTransaction()
                    .replace(R.id.content_frame,new EditProf())
                    .commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(fa==null|| fu==null){
            Intent i = new Intent(this,MainActivity.class);
            startActivity(i);
            finish();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(fa==null|| fu==null){
            Intent i = new Intent(this,MainActivity.class);
            startActivity(i);
            finish();
        }
    }
}
