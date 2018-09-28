package com.example.android.logindemo;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;


class descofcomplaints
{
    public String subject;
    public String Address;

    public String datetime;
    public String extra;
    public int Cashback;

    descofcomplaints()
    {

    }
}
class detaileddescofcomplaints
{
    public String subject;
    public String Address;
    public String informeruid;
    public Double Latitude;
    public Double Longitude;
    public String datetime;
    public String extra;
    public int Cashback;

    detaileddescofcomplaints()
    {

    }
}
public class Complaints extends AppCompatActivity {

    int PLACE_PICKER_REQUEST = 1;
    TextView t1;
    LatLng latlong;
    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;
    private FirebaseStorage storageReference;
    private ImageView userProfilePic;
    Uri imagePath;
    private static int PICK_IMAGE = 123;
    private static final int CAMERA_REQUEST_CODE=2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complaints);

        firebaseAuth=FirebaseAuth.getInstance();
        storageReference= FirebaseStorage.getInstance();

        userProfilePic= (ImageView)findViewById(R.id.Wastephoto);
        userProfilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("images/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Image"), PICK_IMAGE);
            }
        });


        Calendar c = Calendar.getInstance();
        SimpleDateFormat dateformat = new SimpleDateFormat("dd-MMM-yyyy ");
        String datetime = dateformat.format(c.getTime());
        TextView t=(TextView)findViewById(R.id.datetime);
        t.setText("Date :"+datetime);
       t1=(TextView) findViewById(R.id.address);
    }
    public void gotomaps(View view)
    {
      /*  Intent intent = new Intent (this,MapsActivity.class);
        startActivity(intent);*/
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


            if(requestCode == PICK_IMAGE && resultCode == RESULT_OK && data.getData() != null){
                imagePath = data.getData();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imagePath);
                    userProfilePic.setImageBitmap(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(data, this);

                latlong= place.getLatLng();
                t1.setText("Address Selected :"  +place.getAddress());
            }

        }









    }


    public void savedata(View view)
    {

        Calendar c = Calendar.getInstance();
        SimpleDateFormat dateformat = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss");
        String datetime = dateformat.format(c.getTime());
        descofcomplaints d=new descofcomplaints();


        String e=((EditText)findViewById(R.id.weigthofwaste)).getText().toString().trim();


        if(e.isEmpty())
        {
            String toastMsg = String.format("Please enter a valid information for amount of Garbage!");
            Toast.makeText(this, toastMsg, Toast.LENGTH_LONG).show();
            return;
        }

        if(imagePath==null)
        {
            String toastMsg = String.format("Please select proper Image of Garbage!");
            Toast.makeText(this, toastMsg, Toast.LENGTH_LONG).show();
            return;
        }
        String t=((TextView)findViewById(R.id.address)).getText().toString().trim();
        if(t.isEmpty())
        {
            String toastMsg = String.format("Please select proper Address from the map!");
            Toast.makeText(this, toastMsg, Toast.LENGTH_LONG).show();
            return;
        }

        d.Address=((TextView)findViewById(R.id.address)).getText().toString().trim();
        d.datetime=datetime;
        d.subject=((EditText)findViewById(R.id.weigthofwaste)).getText().toString().trim();

        d.extra="No";
        d.Cashback=0;
        databaseReference= FirebaseDatabase.getInstance().getReference();
        databaseReference.child("Description_of_Complaints/"+firebaseAuth.getUid()+"/"+datetime).setValue(d);
        databaseReference.child("Description_of_Complaints_Address/"+firebaseAuth.getUid()+"/"+datetime+"/Latitude").setValue(latlong.latitude);
        databaseReference.child("Description_of_Complaints_Address/"+firebaseAuth.getUid()+"/"+datetime+"/Longitude").setValue(latlong.longitude);
        String toastMsg = String.format("We will clear your Complaints Soon");
        Toast.makeText(this, toastMsg, Toast.LENGTH_LONG).show();
        detaileddescofcomplaints dr=new detaileddescofcomplaints();
        dr.Address=d.Address;
        dr.Cashback=0;
        dr.datetime=d.datetime;
        dr.extra="No";
        dr.informeruid=firebaseAuth.getUid();
        dr.Latitude=latlong.latitude;
        dr.Longitude=latlong.longitude;
        dr.subject=d.subject;
    databaseReference.child("Detail_description_of_Complaints/"+datetime).setValue(dr);
        StorageReference imageReference = storageReference.getReference().child(firebaseAuth.getUid()).child("Images").child(datetime);  //User id/Images/Profile Pic.jpg
        UploadTask uploadTask = imageReference.putFile(imagePath);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Complaints.this, "Upload failed!", Toast.LENGTH_SHORT).show();
            }
        }).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                Toast.makeText(Complaints.this, "Upload successful!", Toast.LENGTH_SHORT).show();
            }
        });
        Intent intent = new Intent (this,PreviousComplaints.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menusforcomplaints, menu);
        return true;
    }

    private void Logout(){
        firebaseAuth.signOut();
        finish();
        startActivity(new Intent(Complaints.this, MainActivity.class));
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()){
            case R.id.logoutMenu:{
                Logout();
                break;
            }
            case R.id.profileMenu: {
                startActivity(new Intent(Complaints.this, ProfileActivity.class));
                break;
            }
            case R.id.previouscomplaints:{
                startActivity(new Intent(Complaints.this, PreviousComplaints.class));
                break;

            }

        }
        return super.onOptionsItemSelected(item);
    }

}
