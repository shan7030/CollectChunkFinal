package com.example.android.logindemo;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DailyActivity extends AppCompatActivity {
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily);
        Date c = Calendar.getInstance().getTime();
        //System.out.println("Current time => " + c);
        firebaseAuth=FirebaseAuth.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        final String formattedDate = df.format(c);
        TextView todaydate=(TextView)findViewById(R.id.Todaysdate);
        todaydate.setText("Date :"+formattedDate);
        Calendar calendar = Calendar.getInstance();

// we can set time by open date and time picker dialog
//Calender and Notification daily


        TextView ty=(TextView)findViewById(R.id.hideact);
        ty.setVisibility(View.GONE);
    databaseReference=FirebaseDatabase.getInstance().getReference().child("Daily_activity/");
    databaseReference.addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            int ispresent=0;
            for(DataSnapshot ds:dataSnapshot.getChildren())
            {
                if(ds.getKey().equals(firebaseAuth.getUid()))
                {
                    ispresent=1;
                    DatabaseReference dref=FirebaseDatabase.getInstance().getReference().child("Daily_activity/"+firebaseAuth.getUid());
                    dref.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot3) {
                            for(DataSnapshot dsr:dataSnapshot3.getChildren())
                            {
                                if(dsr.getKey().equals(formattedDate))
                                {
                                    TextView ty=(TextView)findViewById(R.id.hideact);
                                    ty.setVisibility(View.VISIBLE);
                                    CheckBox c1=(CheckBox)findViewById(R.id.checkbox123);
                                    c1.setVisibility(View.GONE);
                                    Button b1=(Button)findViewById(R.id.buttontosavechanges);
                                    b1.setVisibility(View.GONE);

                                }
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
                Log.v("DailyActivity",""+ispresent);
            }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    });




    }
    private void Logout(){
        firebaseAuth.signOut();
        finish();
        startActivity(new Intent(DailyActivity.this, MainActivity.class));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menuofdaily, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()){
            case R.id.logoutMenu:{
                Logout();
                break;
            }
            case R.id.profileMenu: {
                startActivity(new Intent(DailyActivity.this, ProfileActivity.class));
                break;
            }
            case R.id.previousactivity:{
                startActivity(new Intent(DailyActivity.this, PreviousVisits.class));
                break;
            }
            case R.id.adddailynotification:{
                addnotification();
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }
    public void savetodata(View view)
    {
        staticcounter.isvisited++;
        Date c = Calendar.getInstance().getTime();
        //System.out.println("Current time => " + c);

        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        String formattedDate = df.format(c);
        TextView todaydate=(TextView)findViewById(R.id.Todaysdate);
        todaydate.setText("Date :"+formattedDate);
        databaseReference= FirebaseDatabase.getInstance().getReference();

        CheckBox c2=(CheckBox)findViewById(R.id.checkbox123);
        if(c2.isChecked())
        {
            databaseReference.child("Daily_activity/"+firebaseAuth.getUid()+"/"+formattedDate).setValue("Visited");
            Toast.makeText(DailyActivity.this, "Thanks for your feeddack!!", Toast.LENGTH_SHORT).show();

        }
        else
        {
            databaseReference.child("Daily_activity/"+firebaseAuth.getUid()+"/"+formattedDate).setValue("Not Visited");
            Toast.makeText(DailyActivity.this, "Thanks for your feedback!!We will send the E-Truck Soon!", Toast.LENGTH_SHORT).show();

        }
        TextView ty=(TextView)findViewById(R.id.hideact);
        ty.setVisibility(View.VISIBLE);
        CheckBox c1=(CheckBox)findViewById(R.id.checkbox123);
        c1.setVisibility(View.GONE);
        Button b1=(Button)findViewById(R.id.buttontosavechanges);
        b1.setVisibility(View.GONE);
       // Toast.makeText(DailyActivity.this, "Thanks for your feedback!!We will send the E-Truck Soon!", Toast.LENGTH_SHORT).show();
    }
    public void gotocomplaints(View view)
    {
        Intent intent = new Intent (this,Complaints.class);
        startActivity(intent);
    }
    public void addnotification()
    {
       Calendar calendar=Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY,21);
        calendar.set(Calendar.MINUTE,54);
        calendar.set(Calendar.SECOND,30);

        Intent intent=new Intent(getApplicationContext(),Notification_Reciever.class);
        intent.setAction("MY_NOTIFICATION_MESSAGE");
        PendingIntent pendingIntent=PendingIntent.getBroadcast(getApplicationContext(),100,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager=(AlarmManager)getSystemService(ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),AlarmManager.INTERVAL_DAY,pendingIntent);


     /*   Calendar calendar=Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 20);
        calendar.set(Calendar.MINUTE, 40);
        calendar.set(Calendar.SECOND, 30);

        Intent intent1 = new Intent(DailyActivity.this, MyReceiver.class);
        intent1.setAction("MY_NOTIFICATION_MESSAGE");
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                DailyActivity.this, 0, intent1,
                PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager am = (AlarmManager) DailyActivity.this
                .getSystemService(DailyActivity.this.ALARM_SERVICE);
        am.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY, pendingIntent);
                */

    }
}
