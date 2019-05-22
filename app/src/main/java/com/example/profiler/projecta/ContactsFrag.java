package com.example.profiler.projecta;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static android.R.attr.data;
import static android.app.Activity.RESULT_OK;

/**
 * Created by PROFILER on 9/25/2017.
 */

public class ContactsFrag extends Fragment {
    private static final int RESULT_PICK = 85500;
    View myView;
    DatabaseReference mDatabase;
    FirebaseAuth fa;
    FirebaseUser fu;
    String flag;
    String name1="---";
    String name2="---";
    String name3="---";
    String phone1="---";
    String phone2="---";
    String phone3="---";
    TextView tv1;
    TextView tv2;
    TextView tv3;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        
        myView = inflater.inflate( R.layout.contacts_lay , container ,false);



        fa = FirebaseAuth.getInstance();
        fu = fa.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference();


        tv1 = myView.findViewById(R.id.textView4);

        tv2 = myView.findViewById(R.id.textView5);

        tv3 = myView.findViewById(R.id.textView6);

        mDatabase.child(fu.getUid()).child("contacts").child("value1").child("name").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                name1 = String.valueOf(dataSnapshot.getValue());
                upText();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        mDatabase.child(fu.getUid()).child("contacts").child("value2").child("name").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                name2 = String.valueOf(dataSnapshot.getValue());
                upText();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        mDatabase.child(fu.getUid()).child("contacts").child("value3").child("name").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                name3 = String.valueOf(dataSnapshot.getValue());
                upText();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        mDatabase.child(fu.getUid()).child("contacts").child("value1").child("number").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                phone1 = String.valueOf(dataSnapshot.getValue());
                upText();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        mDatabase.child(fu.getUid()).child("contacts").child("value2").child("number").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                phone2 = String.valueOf(dataSnapshot.getValue());
                upText();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        mDatabase.child(fu.getUid()).child("contacts").child("value3").child("number").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                phone3 = String.valueOf(dataSnapshot.getValue());
                upText();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        Button c1 = myView.findViewById(R.id.contact1But);
        Button c2 = myView.findViewById(R.id.contact2But);
        Button c3 = myView.findViewById(R.id.contact3But);

        c1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                flag = "1";
                Intent conTact = new Intent(Intent.ACTION_PICK, ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
                startActivityForResult(conTact,RESULT_PICK);

            }
        });

        c2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                flag = "2";
                Intent conTact = new Intent(Intent.ACTION_PICK, ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
                startActivityForResult(conTact,RESULT_PICK);

            }
        });

        c3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                flag="3";
                Intent conTact = new Intent(Intent.ACTION_PICK, ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
                startActivityForResult(conTact,RESULT_PICK);

            }
        });






        return myView;
    }


    public void upText(){
        if (name1.isEmpty() || phone1.isEmpty() ){

        }else {
            tv1.setText(name1+" "+phone1);
        }

        if (name2.isEmpty() || phone2.isEmpty() ){

        }else {
            tv2.setText(name2+" "+phone2);
        }

        if (name3.isEmpty() || phone3.isEmpty() ){

        }else {
            tv3.setText(name3+" "+phone3);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK) {

            switch (requestCode) {
                case RESULT_PICK:
                    contactPicked(data);
                    break;
            }
        } else {
            Log.e("MainActivity", "Failed to pick contact");
        }
    }

    private void contactPicked(Intent data) {
        Cursor cursor;
        try {
            Context context = getActivity();

            String phoneNo = null ;
            String name = null;
            Uri uri = data.getData();
            cursor = getActivity().getContentResolver().query(uri, null, null, null, null);
            cursor.moveToFirst();

            int  phoneIndex =cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);

            int  nameIndex =cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);

            phoneNo = cursor.getString(phoneIndex);
            name = cursor.getString(nameIndex);

            Toast.makeText(context,name+" "+phoneNo, Toast.LENGTH_SHORT).show();

            mDatabase.child(fu.getUid()).child("contacts").child("value"+flag).child("name").setValue(name.replaceAll("\\s+",""));
            mDatabase.child(fu.getUid()).child("contacts").child("value"+flag).child("number").setValue(phoneNo.replaceAll("\\s+",""));




        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
