package com.example.android.logindemo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SecondActivity extends AppCompatActivity {

    LatLng latLong;
    int PLACE_PICKER_REQUEST = 1;
    private FirebaseAuth firebaseAuth;
    private Button logout;
    TextView t;
    String address;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        firebaseAuth = FirebaseAuth.getInstance();
        t=(TextView)findViewById(R.id.text1);
        t.setText("Please Select the address by clicking from above button!!");

    }

    private void Logout(){
        firebaseAuth.signOut();
        finish();
        startActivity(new Intent(SecondActivity.this, MainActivity.class));
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
                startActivity(new Intent(SecondActivity.this, ProfileActivity.class));
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    public void opendailyactivity(View view)
    {

        DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference().child("Address_of_citizens/"+firebaseAuth.getUid());
        databaseReference.child("Latitude/").setValue(latLong.latitude);
        databaseReference.child("Longitude/").setValue(latLong.longitude);
        databaseReference.child("Address/").setValue(address);
        Toast.makeText(SecondActivity.this, "Address saved Succesfully!", Toast.LENGTH_SHORT).show();
        Intent intenter = new Intent (SecondActivity.this, DailyActivity.class);
        startActivity(intenter);
    }

    public void selectaddress(View view)
    {
        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
        try
        {
            startActivityForResult(builder.build(this), PLACE_PICKER_REQUEST);

        }
        catch(GooglePlayServicesRepairableException e)
        {
            e.printStackTrace();
        }
        catch (GooglePlayServicesNotAvailableException e)
        {
            e.printStackTrace();
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(data, this);
                address=(String) place.getAddress();
                latLong= place.getLatLng();
                t.setText(place.getAddress());
            }
        }
    }

}
