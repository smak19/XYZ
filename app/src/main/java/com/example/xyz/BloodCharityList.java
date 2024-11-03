package com.example.xyz;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class BloodCharityList extends AppCompatActivity {
    RecyclerView recyclerView;
    Adapter adapter;
    Toolbar toolbar;
    ArrayList<CharityModel> arrayList;
    CollectionReference cref , cref2;
    FirebaseFirestore firestore;
    DocumentReference documentReference;
    String Category;
    Query query;
    String Email , orgname;
    String BloodGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blood_charity_list);
        init();
        setToolbar();
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        getDetails();
    }
    public void setToolbar(){
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    void getDetails(){

        query = cref;
        query.whereEqualTo("Organization Category" , "Blood").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        CharityModel cm = new CharityModel();
                        Log.e("tag", document.getId() + " => " + document.getData());
                        cm.setName(document.getString("Name"));
                        cm.setAddress(document.getString("Organization Address"));
                        cm.setBloodgroup(BloodGroup);

                        Log.e("org name" ,""+document.getString("Name"));
                        cm.setCategory(document.getString("Organization Category"));
                        arrayList.add(cm);

                        //setting up adapter


                    }
                    adapter=new Adapter(arrayList,getApplicationContext());
                    recyclerView.setLayoutManager(new LinearLayoutManager(BloodCharityList.this));
                    Log.e("array sizde" , ""+arrayList.size());
                    recyclerView.setAdapter(adapter);
                } else {
                    Log.e("tag", "Error getting documents: ", task.getException());
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(BloodCharityList.this, "failed", Toast.LENGTH_SHORT).show();
            }
        });

    }
    public void init(){
        toolbar = findViewById(R.id.bloodtoolbar);
        recyclerView = findViewById(R.id.bloodrecycleview);
        arrayList = new ArrayList<>();
        firestore = FirebaseFirestore.getInstance();
        cref = firestore.collection("Organization Details");
        cref2 = firestore.collection("Users Details");
        Intent intent = getIntent();
        Email = intent.getStringExtra("Email");
        Category = intent.getStringExtra("Category");
        Log.e("cat" , ""+Category);

        cref2.document(Email).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                BloodGroup  = task.getResult().getString("Blood Type");
            }
        });

    }

}