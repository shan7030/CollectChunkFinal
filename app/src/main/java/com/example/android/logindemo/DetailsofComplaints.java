package com.example.android.logindemo;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class DetailsofComplaints extends AppCompatActivity {

    private ImageView profilePic;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseAuth firebaseAuth;
    private FirebaseStorage firebaseStorage;

    private TextView t1,t2,t3,t4,t5;
    private Button b1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailsof_complaints);

        profilePic=(ImageView)findViewById(R.id.ivProfilePic) ;
        firebaseAuth=FirebaseAuth.getInstance();
        firebaseStorage=FirebaseStorage.getInstance();
        StorageReference storageReference = firebaseStorage.getReference();
        storageReference.child(firebaseAuth.getUid()).child("Images/"+Stringpasser.name).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).fit().centerCrop().into(profilePic);
            }
        });
        t1=(TextView)findViewById(R.id.Dateandtime);
        t2=(TextView)findViewById(R.id.Address);
        t3=(TextView)findViewById(R.id.amountofwaste);
        t4=(TextView)findViewById(R.id.Cash_back);
        t5=(TextView)findViewById(R.id.Status_of_collection);
        b1=(Button) findViewById(R.id.removebutton);


        Log.v("Complaints",""+Stringpasser.name);
        DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference().child("Description_of_Complaints/"+firebaseAuth.getUid()+"/"+Stringpasser.name);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                descofcomplaints dp=dataSnapshot.getValue(descofcomplaints.class);
                if(dp!=null) {
                    t1.setText("Date and Time :" + dp.datetime);
                    t2.setText( dp.Address);
                    t3.setText("Extra Information or Amount of Garbage :"+dp.subject);
                    t4.setText("Cash Back :" + dp.Cashback);
                    t5.setText("Status of Collection :" + dp.extra);

                    if(!dp.extra.equals("No"))
                    {
                        b1.setVisibility(View.GONE);
                    }
                }
                else
                {
                    Log.v("DetailsofComplaints",""+"Emptyytyt");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });




    }
    public void openaddress(View view)
    {

       DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference().child("Description_of_Complaints_Address/"+firebaseAuth.getUid()+"/"+Stringpasser.name);


       databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //String l=dataSnapshot.child("latitude").getValue(String.class);
               // String h=dataSnapshot.getKey();
               // Log.v("DetailsofComplaints",""+h);
               // Log.v("DetailsofComplaints",""+l);
             Double d=dataSnapshot.child("Latitude").getValue(Double.class);
             Log.v("DetailsofComplaints",""+d);
             Double d1=dataSnapshot.child("Longitude").getValue(Double.class);
             Log.v("DetailsofComplaints",""+d1);
             OnlyforLatlong.latLng=new LatLng(d,d1);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        Intent intent = new Intent (this,MapsActivity.class);
        startActivity(intent);
    }
    public void removeandbackto(View view)
    {

        DatabaseReference dr=FirebaseDatabase.getInstance().getReference().child("Description_of_Complaints/"+firebaseAuth.getUid()+"/"+Stringpasser.name);
        dr.removeValue();
        dr=FirebaseDatabase.getInstance().getReference().child("Description_of_Complaints_Address/"+firebaseAuth.getUid()+"/"+Stringpasser.name);
        dr.removeValue();
        FirebaseDatabase.getInstance().getReference().child("Detail_description_of_Complaints/"+Stringpasser.name).removeValue();
        Toast.makeText(DetailsofComplaints.this, "Entry removed succesfully !!", Toast.LENGTH_SHORT).show();
        firebaseStorage=FirebaseStorage.getInstance();
        StorageReference storageReference=firebaseStorage.getReference();
        storageReference.child(firebaseAuth.getUid()).child("Images/"+Stringpasser.name).delete();
        Intent intent = new Intent (this,PreviousComplaints.class);
        startActivity(intent);
    }
}
