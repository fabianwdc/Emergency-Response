package com.example.profiler.projecta;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Arrays;

import static com.firebase.ui.auth.AuthUI.TAG;
import static com.firebase.ui.auth.AuthUI.getApplicationContext;
import static com.firebase.ui.auth.AuthUI.getInstance;

public class EditProf extends Fragment {
    DatabaseReference mDatabase;
    FirebaseAuth fa;
    FirebaseUser fu;
    private static final int RC_SIGN_IN = 123;
    View cusView;

    @SuppressLint("ResourceAsColor")
    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        cusView = inflater.inflate(R.layout.edit_prof,container,false);
        fa = FirebaseAuth.getInstance();
        fu = fa.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference();



        final TextView tv = cusView.findViewById(R.id.emailTv);
        tv.setText(fu.getEmail());



        final TextView tv2 = cusView.findViewById(R.id.nameTv);
        tv2.setText(fu.getDisplayName());

        tv2.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View v) {
                final View view = inflater.inflate(R.layout.layout_alert, null);
                final AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
                alertDialog.setTitle("Enter New Name");

                alertDialog.setCancelable(true);



                final EditText etComments = (EditText) view.findViewById(R.id.etComments);
                etComments.setHint("Enter Name");

                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE ,"OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                .setDisplayName(etComments.getText().toString())
                                .build();
                        fu.updateProfile(profileUpdates)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        Toast.makeText(getActivity(), "Name Updated!!!", Toast.LENGTH_SHORT).show();
                                        fa=FirebaseAuth.getInstance();
                                        fu=fa.getCurrentUser();

                                        tv2.setText(fu.getDisplayName());


                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getActivity(), "Name update failed!!!!!!!", Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                });


                alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        alertDialog.dismiss();
                    }
                });

                alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface dialog) {
                        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(R.color.colorPrimaryDark);
                        alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(R.color.colorPrimaryDark);
                    }
                });
                alertDialog.setView(view);
                alertDialog.show();
            }

        });

        final TextView tv3 = cusView.findViewById(R.id.aadharTv);

        mDatabase.child(fu.getUid()).child("aaad").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(String.valueOf(dataSnapshot.getValue()).isEmpty()) {
                    tv3.setText("Aahaar No: Enter");
                }else {

                    tv3.setText("Aahaar No:" + String.valueOf(dataSnapshot.getValue()));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        //tv3.setText(fu.getDisplayName());
        tv3.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View v) {
                final View view = inflater.inflate(R.layout.layout_alert, null);
                final AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
                alertDialog.setTitle("Enter Aadhar No:");

                alertDialog.setCancelable(true);



                final EditText etComments = (EditText) view.findViewById(R.id.etComments);
                etComments.setHint("Enter Aadhaar No");
                etComments.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_NUMBER_FLAG_SIGNED);
                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE ,"OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {


                        mDatabase.child(fu.getUid()).child("aaad").setValue(etComments.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(getActivity(), "Aadhar Updated!!!", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getActivity(), "Aadhar Update Failed!!", Toast.LENGTH_SHORT).show();
                            }
                        });


                    }
                });


                alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        alertDialog.dismiss();
                    }
                });

                alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface dialog) {
                        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(R.color.colorPrimaryDark);
                        alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(R.color.colorPrimaryDark);
                    }
                });
                alertDialog.setView(view);
                alertDialog.show();
            }

        });

        TextView tv4 = cusView.findViewById(R.id.passReset);

        tv4.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View v) {
                final View view = inflater.inflate(R.layout.layout_alert, null);
                final AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
                alertDialog.setTitle("Sure To Reset Password?");
                alertDialog.setMessage("A Mail Will Be Sent With A Link To Reset Account Password ");
                alertDialog.setCancelable(true);




                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE ,"OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        fa=  FirebaseAuth.getInstance();
                        fu = fa.getCurrentUser();

                        fa.sendPasswordResetEmail(fu.getEmail())
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(getActivity(), "Email Sent To Reset Password!", Toast.LENGTH_SHORT).show();
                                            AuthUI.getInstance().signOut(getActivity());
                                            fu=null;
                                            fa=null;
                                            getActivity().finish();
                                            startActivityForResult(
                                                    AuthUI.getInstance()
                                                            .createSignInIntentBuilder()
                                                            .setAvailableProviders(Arrays.asList(
                                                                    new AuthUI.IdpConfig.EmailBuilder().build()
                                                            ))
                                                            .build(),
                                                     RC_SIGN_IN );



                                        }
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getActivity(), "Faileeddd!!", Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                });


                alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        alertDialog.dismiss();
                    }
                });

                alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface dialog) {
                        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(R.color.colorPrimaryDark);
                        alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(R.color.colorPrimaryDark);
                    }
                });

                alertDialog.show();
            }

        });

        TextView tv5 = cusView.findViewById(R.id.deleteAccTv);

        tv5.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View v) {
                final View view = inflater.inflate(R.layout.layout_alert, null);
                final AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
                alertDialog.setTitle("Sure To Delete Account?");

                alertDialog.setCancelable(true);


                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE ,"OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        fu.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(getActivity(), "Successfully Deleted!!", Toast.LENGTH_SHORT).show();
                                fu=null;
                                fa=null;
                                getActivity().finish();
                                startActivityForResult(
                                        AuthUI.getInstance()
                                                .createSignInIntentBuilder()
                                                .setAvailableProviders(Arrays.asList(
                                                        new AuthUI.IdpConfig.EmailBuilder().build()
                                                ))
                                                .build(),
                                        RC_SIGN_IN );
                            }
                        });

                    }
                });


                alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        alertDialog.dismiss();
                    }
                });

                alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface dialog) {
                        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(R.color.colorPrimaryDark);
                        alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(R.color.colorPrimaryDark);
                    }
                });

                alertDialog.show();
            }

        });



        return cusView;
    }
}
