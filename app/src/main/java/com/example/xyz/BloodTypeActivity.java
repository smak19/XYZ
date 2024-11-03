package com.example.xyz;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
public class BloodTypeActivity extends AppCompatActivity {
    ArrayList<String> list ;
    String usertype;
    Spinner spinner;
    String email;
    FirebaseFirestore firestore;
    CollectionReference cref;
    Query query;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blood_type);
        init();
        setSpinner();


    }

    private void setSpinner() {
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                String item = adapterView.getItemAtPosition(position).toString();
                userdetails(email , item , usertype);
                Log.e("6969" , ""+item);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }



    void userdetails(String emaill, String bloodtype, String typeofuser) {

        Map<String, Object> User = new HashMap<>();

        User.put("Name", emaill);
        User.put("Type of User" ,usertype );
        User.put("Blood Type" , bloodtype);

        firestore.collection("Users Details").document(emaill.toString())
                .set(User)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.e("6969", "USer Details Added");
                        Toast.makeText(BloodTypeActivity.this, "User Added", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("6969", "Error writing document", e);
                    }
                });
    }

    void init(){
        email = getIntent().getStringExtra("Email");
        usertype = getIntent().getStringExtra("Login Type");
        spinner = findViewById(R.id.spinnerbloodtype);
        firestore = FirebaseFirestore.getInstance();
        cref = firestore.collection("User Details");
        query = cref;

        list = new ArrayList<>();
        list.add("Please Select Your Blood Type");
        list.add("A+");
        list.add("A-");
        list.add("B+");
        list.add("B-");
        list.add("AB+");
        list.add("AB-");
        list.add("O+");
        list.add("O-");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this , android.R.layout.simple_spinner_item ,list);
        adapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
        spinner.setAdapter(adapter);

    }

}