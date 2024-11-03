package com.example.xyz;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class OrganizationDashboard extends AppCompatActivity {

    String email , logintype , category , orgname;

    CardView emergency , donreq;
    private static final int REQUEST_PHONE_CALL = 1;
    private static final int REQUEST_PHONE_IMAGE = 1;
    FirebaseFirestore firestore;
    BottomNavigationView bottomNavigationView;
    FirebaseUser user ;
    FirebaseAuth auth;
    DocumentReference dref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_organization_dashboard);
        init();
        checkpermissionandmakecall();
        checkimagepermission();



        donreq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent ;
                intent = new Intent(OrganizationDashboard.this , donreqlist.class);
                intent.putExtra("Cat" , category);
                intent.putExtra("Org name" , orgname);
                Log.e("taggg" , ""+category+","+orgname);
                startActivity(intent);
            }
        });

        emergency.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent ;
                intent = new Intent(OrganizationDashboard.this , emergency.class);
                intent.putExtra("Cat" , category);
                intent.putExtra("Org name" , orgname);
                Log.e("taggg" , ""+category+","+orgname);
                startActivity(intent);
            }
        });

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                int id=item.getItemId();

                if(id==R.id.optorgprofile){
                    Intent iprofile=new Intent(OrganizationDashboard.this, Org_profile.class);
                    iprofile.putExtra("Email",email);
                    startActivity(iprofile);

                } else if (id==R.id.optorglogout) {
                    auth.signOut();
                    Intent ilogout=new Intent(OrganizationDashboard.this,LoginActivity.class);
                    ilogout.putExtra("Email",email);
                    startActivity(ilogout);
                    finish();

                }

                else {
                    Toast.makeText(OrganizationDashboard.this, "Already on Home Page", Toast.LENGTH_SHORT).show();
                }
                return false;
            }
        });
        bottomNavigationView.setSelectedItemId(R.id.optorghome);

    }
    private void checkpermissionandmakecall(){
        if(ActivityCompat.checkSelfPermission(OrganizationDashboard.this , android.Manifest.permission.CALL_PHONE)!= PackageManager.PERMISSION_GRANTED){
            Toast.makeText(this, "requesting permission", Toast.LENGTH_SHORT).show();
            ActivityCompat.requestPermissions(OrganizationDashboard.this , new String[]{Manifest.permission.CALL_PHONE} , REQUEST_PHONE_CALL);
        }
        else {
            //callperson();
            Toast.makeText(this, "granted already", Toast.LENGTH_SHORT).show();
        }
    }
    private void checkimagepermission() {
        if(ActivityCompat.checkSelfPermission(OrganizationDashboard.this , Manifest.permission.READ_MEDIA_IMAGES)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(OrganizationDashboard.this , new String[]{Manifest.permission.READ_MEDIA_IMAGES}, REQUEST_PHONE_IMAGE);
        }
        else {
            Toast.makeText(this, "Image permission already granted", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode==REQUEST_PHONE_CALL){

        }
        if (requestCode==REQUEST_PHONE_IMAGE){
            Toast.makeText(this, "granted permission", Toast.LENGTH_SHORT).show();
        }
    }


    void init(){
        emergency = findViewById(R.id.emergencyCard);
        donreq = findViewById(R.id.requestcard);
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        email = getIntent().getStringExtra("Email");
        logintype = getIntent().getStringExtra("Login Type");
        bottomNavigationView = findViewById(R.id.orgbottomnav);
        firestore = FirebaseFirestore.getInstance();
        dref = firestore.collection("Organization Details").document(user.getEmail());
        dref.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                category = task.getResult().getString("Organization Category");
                orgname = task.getResult().getString("Name");
            }
        });


    }
}