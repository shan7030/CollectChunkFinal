package com.example.android.logindemo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Vector;

public class PreviousVisits extends AppCompatActivity {

    private DatabaseReference firebaseDatabase;
    FirebaseAuth firebaseAuth;
    Vector<String> v=new Vector<String>();
    ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_previous_visits);
        firebaseAuth=FirebaseAuth.getInstance();
        firebaseDatabase=FirebaseDatabase.getInstance().getReference().child("Daily_activity/"+firebaseAuth.getUid());
       firebaseDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot uniqueKeySnapshot : dataSnapshot.getChildren()) {

                    String date = uniqueKeySnapshot.getKey();
                    String visited = (String) uniqueKeySnapshot.getValue();
                    v.add("Date :" + date + "           " + visited);
                }
                listView = (ListView) findViewById(R.id.listofvisits);

                ArrayAdapter adapter = new ArrayAdapter<String>(PreviousVisits.this, R.layout.listofvisits, v);

                listView.setAdapter(adapter);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



    }
    private void Logout(){
        firebaseAuth.signOut();
        finish();
        startActivity(new Intent(PreviousVisits.this, MainActivity.class));
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
                startActivity(new Intent(PreviousVisits.this, ProfileActivity.class));
                break;

        }
        return super.onOptionsItemSelected(item);
    }
}
