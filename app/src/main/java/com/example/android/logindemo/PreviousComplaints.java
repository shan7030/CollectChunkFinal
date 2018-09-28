package com.example.android.logindemo;

import android.content.Intent;
import android.location.Address;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Vector;


class descofcomplaints2
{
    public String subject;
    public String Address;

    public String datetime;
    public String extra;
    public int Cashback;

    descofcomplaints2()
    {

    }
}
public class PreviousComplaints extends AppCompatActivity {

    Vector<String> v=new Vector<String>();
    DatabaseReference databaseReference;
    FirebaseAuth firebaseAuth;
    descofcomplaints ds;
    ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_previous_complaints);
        firebaseAuth=FirebaseAuth.getInstance();
        databaseReference= FirebaseDatabase.getInstance().getReference().child("Description_of_Complaints/"+firebaseAuth.getUid());


         databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot uniqueKeySnapshot :dataSnapshot.getChildren())
                {
                    String date=uniqueKeySnapshot.getKey();
                   descofcomplaints c=uniqueKeySnapshot.getValue(descofcomplaints.class);
                    v.add(date);
                }

                listView = (ListView) findViewById(R.id.listofcomplaints);

                ArrayAdapter adapter = new ArrayAdapter<String>(PreviousComplaints.this, R.layout.listofvisits, v);

                listView.setAdapter(adapter);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                                    @Override
                                                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                                        String selected=(String)adapterView.getItemAtPosition(i);

                                                        Stringpasser.name=selected;

                                                        Intent appInfo = new Intent(PreviousComplaints.this, DetailsofComplaints.class);
                                                        startActivity(appInfo);

                                                    }
                                                }
                );
                }



            @Override
            public void onCancelled(DatabaseError databaseError) {

            }


        });


    }
    private void Logout(){
        firebaseAuth.signOut();
        finish();
        startActivity(new Intent(PreviousComplaints.this, MainActivity.class));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()){
            case R.id.logoutMenu:{
                Logout();
                break;
            }
            case R.id.profileMenu:
                startActivity(new Intent(PreviousComplaints.this, ProfileActivity.class));
                break;

        }
        return super.onOptionsItemSelected(item);
    }
}
