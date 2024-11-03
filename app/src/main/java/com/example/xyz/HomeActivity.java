package com.example.xyz;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;

import android.app.DownloadManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class HomeActivity extends AppCompatActivity {

    CardView clothing , food , grocery , blood , money , books;
    String email;
    Intent intent;
    Toolbar toolbar;
    private static final int REQUEST_PHONE_CALL = 1;
    private static final int REQUEST_EXTERNAL_STORAGE = 1;

    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    BottomNavigationView bottomNavigationView;
    FirebaseFirestore firestore;
    String uri ;
    DownloadManager.Query query;
    CollectionReference collectionReference , collectionReference2;
    DocumentReference documentReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        init();
        setToolbar();
        checkpermissions();

        bottomNavigationView.setSelectedItemId(R.id.opthome);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                int id=item.getItemId();

                if(id==R.id.optprofile){
                    Intent iprofile=new Intent(HomeActivity.this,UserProfile.class);
                    iprofile.putExtra("Email",email);
                    startActivity(iprofile);
                } else if (id==R.id.optlogout) {
                    firebaseAuth.signOut();
                    Intent ilogout=new Intent(HomeActivity.this,LoginActivity.class);
                    ilogout.putExtra("Email",email);
                    startActivity(ilogout);
                    finish();
                }

                else {
                    Toast.makeText(HomeActivity.this, "Already on Home Page", Toast.LENGTH_SHORT).show();
                }
                return false;
            }
        });

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        books.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(HomeActivity.this , CharityList.class);
                intent1.putExtra("Email" , email);
                startActivity(intent1);
            }
        });

        clothing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent2 = new Intent(HomeActivity.this , ClothingCharityList.class);
                intent2.putExtra("Email" , email);
                startActivity(intent2);
            }
        });

        blood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent3 = new Intent(HomeActivity.this , BloodCharityList.class);
                intent3.putExtra("Email" , email);
                intent3.putExtra("Category" , "Blood");
                startActivity(intent3);
            }
        });

        food.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent4 = new Intent(HomeActivity.this , FoodCharityList.class);
                intent4.putExtra("Email" , email);
                startActivity(intent4);
            }
        });

        grocery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent5 = new Intent(HomeActivity.this , GroceryCharityList.class);
                intent5.putExtra("Email" , email);
                startActivity(intent5);
            }
        });

        money.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent6 = new Intent(HomeActivity.this , PaymentGateway.class);
                intent6.putExtra("Email" , email);
                startActivity(intent6);
            }
        });


    }
    private void checkpermissions(){
        if(ActivityCompat.checkSelfPermission(HomeActivity.this , android.Manifest.permission.CALL_PHONE)!= PackageManager.PERMISSION_GRANTED){
            Toast.makeText(this, "requesting call permission", Toast.LENGTH_SHORT).show();
            ActivityCompat.requestPermissions(HomeActivity.this , new String[]{Manifest.permission.CALL_PHONE} , REQUEST_PHONE_CALL);
        } else if (ActivityCompat.checkSelfPermission(HomeActivity.this , Manifest.permission.READ_MEDIA_IMAGES)!= PackageManager.PERMISSION_GRANTED){
            Toast.makeText(this, "requesting STORAGE permission", Toast.LENGTH_SHORT).show();
            ActivityCompat.requestPermissions(HomeActivity.this , new String[]{Manifest.permission.READ_MEDIA_IMAGES} , REQUEST_EXTERNAL_STORAGE);
        }

        else {
            //callperson();
            Toast.makeText(this, "granted already", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode==REQUEST_PHONE_CALL){

        }
        if (requestCode==REQUEST_EXTERNAL_STORAGE){

        }
    }

    public void setToolbar(){
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }


    void init(){
        toolbar = findViewById(R.id.hometoolbar);
        clothing = findViewById(R.id.clothingCard);
        food = findViewById(R.id.foodcard);
        grocery = findViewById(R.id.grocerycard);
        blood = findViewById(R.id.bloodcard);
        money = findViewById(R.id.moneycard);
        books = findViewById(R.id.bookcard);
        bottomNavigationView=findViewById(R.id.bottomnav);
        firebaseAuth=FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        intent = getIntent();
        email = user.getEmail();
        Log.e("Email" , ""+email);
        uri = getIntent().getStringExtra("uri");
        Log.e("uri" , ""+uri);

        firestore= FirebaseFirestore.getInstance();
        collectionReference = firestore.collection("Users Details");
        documentReference = collectionReference.document(email);


    }
}