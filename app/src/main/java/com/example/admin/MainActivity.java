package com.example.admin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    EditText etn,err;
    TextView tvemail, tvnooc;
    EditText pttc;
    Button getDet,sendack,sendcost,jdone,errbtn;
    String email,tn;
    DatabaseReference reff;
    String item;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etn = (EditText) findViewById(R.id.etn);
        tvemail = (TextView) findViewById(R.id.tvemail);
        tvnooc = (TextView) findViewById(R.id.tvnooc);
        pttc = (EditText) findViewById(R.id.pttc);
        getDet = (Button) findViewById(R.id.getDet);
        //sendcost=(Button)findViewById(R.id.sendcost);
        //jdone=(Button)findViewById(R.id.jdone);
        //errbtn=(Button)findViewById(R.id.errbtn);
        err=(EditText)findViewById(R.id.err);
        sendack=(Button)findViewById(R.id.sendack);
       getDet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int flag = 1;

                if (TextUtils.isEmpty(etn.getText().toString().trim())) {
                    Toast.makeText(MainActivity.this, "Enter a valid Token number!", Toast.LENGTH_SHORT).show();
                    flag = 0;
                }
                if (flag == 1) {
                    tn = etn.getText().toString();

                    reff = FirebaseDatabase.getInstance().getReference().child("User").child(tn);
                    reff.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            email = dataSnapshot.child("email").getValue().toString();
                            String no_of_copies = dataSnapshot.child("no_of_copies").getValue().toString();
                            tvemail.setText("e-Mail: " + email);
                            tvnooc.setText("Number of Copies: " + no_of_copies);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            }
        });

        /*sendcost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_VIEW
                            , Uri.parse("mailto:" + email));
                    String sub="PrintingQueue Cost of Printing for token- "+tn;
                    String cost=pttc.getText().toString();
                    String msg="The total cost of printing, associated to the token number "+tn+" is Rs. "+cost+".Please pay the amount via GPay for further processing! :)";
                    intent.putExtra(Intent.EXTRA_SUBJECT,sub);
                    intent.putExtra(Intent.EXTRA_TEXT,msg);
                    startActivity(intent);
            }
        });

        jdone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //reff=FirebaseDatabase.getInstance().getReference().child("User").child(tn);
                //reff.setValue("NULL");
                Intent intent = new Intent(Intent.ACTION_VIEW
                        , Uri.parse("mailto:" + email));
                String sub="PrintingQueue Acknowledgement for token- "+tn;
                String mesg="The relevant files that are associated to the token number "+tn+" is(are) printed successfully. Please collect them. Hope you have spent you're time wisely! :)";
                intent.putExtra(Intent.EXTRA_SUBJECT,sub);
                intent.putExtra(Intent.EXTRA_TEXT,mesg);
                startActivity(intent);


            }
        });

        errbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW
                        , Uri.parse("mailto:" + email));
                String sub="PrintingQueue Negative Acknowledgement for token- "+tn;
                String error=err.getText().toString();
                String mesg="The relevant files that are associated to the token number "+tn+" is(are) NOT printed successfully, due to a certain issue ("+error+")! Sorry, please try again later!";
                intent.putExtra(Intent.EXTRA_SUBJECT,sub);
                intent.putExtra(Intent.EXTRA_TEXT,mesg);
                startActivity(intent);
            }
        });*/

        final Spinner spinner = (Spinner) findViewById(R.id.spinner);

        // Spinner click listener
        spinner.setOnItemSelectedListener(this);

        // Spinner Drop down elements
        List<String> categories = new ArrayList<String>();
        categories.add("Select Acknowledgement");
        categories.add("Send Cost");
        categories.add("Job Done!");
        categories.add("Error X");
        //categories.add("Personal");
       // categories.add("Travel");

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);

        sendack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                item=spinner.getSelectedItem().toString();
                if(item=="Select Acknowledgement"){
                    Toast.makeText(MainActivity.this,"Select Acknowledgement to be sent!",Toast.LENGTH_SHORT).show();
                }
                else if(item=="Send Cost") {
                    boolean amnt = false;
                    if (TextUtils.isEmpty(pttc.getText().toString().trim())) {
                        Toast.makeText(MainActivity.this, "Enter COST!", Toast.LENGTH_SHORT).show();
                        amnt = false;
                    } else amnt = true;
                    if (amnt) {
                        Intent intent = new Intent(Intent.ACTION_VIEW
                                , Uri.parse("mailto:" + email));
                        String sub = "PrintingQueue Cost of Printing for token- " + tn;
                        String cost = pttc.getText().toString();
                        String msg = "The total cost of printing, associated to the token number " + tn + " is Rs. " + cost + ".Please pay the amount via GPay for further processing! :)";
                        intent.putExtra(Intent.EXTRA_SUBJECT, sub);
                        intent.putExtra(Intent.EXTRA_TEXT, msg);
                        startActivity(intent);
                    }
                }
                else if(item=="Job Done!"){
                    Intent intent = new Intent(Intent.ACTION_VIEW
                            , Uri.parse("mailto:" + email));
                    String sub="PrintingQueue Acknowledgement for token- "+tn;
                    String mesg="The relevant files that are associated to the token number "+tn+" is(are) printed successfully. Please collect them. Hope you have spent you're time wisely! :)";
                    intent.putExtra(Intent.EXTRA_SUBJECT,sub);
                    intent.putExtra(Intent.EXTRA_TEXT,mesg);
                    startActivity(intent);
                }
                else if(item=="Error X") {
                    boolean errorR = false;
                    if (TextUtils.isEmpty(err.getText().toString().trim())) {
                        Toast.makeText(MainActivity.this, "Enter Reason behind error!", Toast.LENGTH_SHORT).show();
                        errorR=false;
                    }
                    else errorR=true;
                    if (errorR) {
                        Intent intent = new Intent(Intent.ACTION_VIEW
                                , Uri.parse("mailto:" + email));
                        String sub = "PrintingQueue Negative Acknowledgement for token- " + tn;
                        String error = err.getText().toString();
                        String mesg = "The relevant files that are associated to the token number " + tn + " is(are) NOT printed successfully, due to a certain issue (" + error + ")! Sorry, please try again later!";
                        intent.putExtra(Intent.EXTRA_SUBJECT, sub);
                        intent.putExtra(Intent.EXTRA_TEXT, mesg);
                        startActivity(intent);
                    }
                }
            }
        });
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        item = parent.getItemAtPosition(position).toString();
        if(item=="Select Acknowledgement"){

        }
        /*else if(item=="Send Cost"){
        Intent intent = new Intent(Intent.ACTION_VIEW
                , Uri.parse("mailto:" + email));
        String sub="PrintingQueue Cost of Printing for token- "+tn;
        String cost=pttc.getText().toString();
        String msg="The total cost of printing, associated to the token number "+tn+" is Rs. "+cost+".Please pay the amount via GPay for further processing! :)";
        intent.putExtra(Intent.EXTRA_SUBJECT,sub);
        intent.putExtra(Intent.EXTRA_TEXT,msg);
        startActivity(intent);
    }
        else if(item=="Job Done!"){
            Intent intent = new Intent(Intent.ACTION_VIEW
                    , Uri.parse("mailto:" + email));
            String sub="PrintingQueue Acknowledgement for token- "+tn;
            String mesg="The relevant files that are associated to the token number "+tn+" is(are) printed successfully. Please collect them. Hope you have spent you're time wisely! :)";
            intent.putExtra(Intent.EXTRA_SUBJECT,sub);
            intent.putExtra(Intent.EXTRA_TEXT,mesg);
            startActivity(intent);
        }
        else if(item=="Error!"){
            Intent intent = new Intent(Intent.ACTION_VIEW
                    , Uri.parse("mailto:" + email));
            String sub="PrintingQueue Negative Acknowledgement for token- "+tn;
            String error=err.getText().toString();
            String mesg="The relevant files that are associated to the token number "+tn+" is(are) NOT printed successfully, due to a certain issue ("+error+")! Sorry, please try again later!";
            intent.putExtra(Intent.EXTRA_SUBJECT,sub);
            intent.putExtra(Intent.EXTRA_TEXT,mesg);
            startActivity(intent);
        }*/
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}

