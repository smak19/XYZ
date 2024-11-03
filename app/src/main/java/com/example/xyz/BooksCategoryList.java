package com.example.xyz;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class BooksCategoryList extends AppCompatActivity {
    FirebaseFirestore firestore;
    FirebaseUser firebaseUser;

    Toolbar toolbar;
    String orgname;
    EditText booktype , noofbooks;
    CollectionReference cref;
    DocumentReference dref;
    FirebaseAuth auth;
    String  donorname , donoremail , donorphoneno , sdate;
    Button donate , date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_books_category_list);
        //setToolbar();
        init();

//        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//            }
//        });
        donate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (booktype.getText().toString().isEmpty()){
                    Toast.makeText(BooksCategoryList.this, "Please enter the type of book", Toast.LENGTH_SHORT).show();
                }
                else if (noofbooks.getText().toString().isEmpty()){
                    Toast.makeText(BooksCategoryList.this, "Please the number of books", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    setDonationDetails();
                }
            }
        });
    }

    private void setDonationDetails() {
        Map<String , Object> Donation = new HashMap<>();
        Donation.put("Donor Name" , donorname);
        Donation.put("Donor Phone No" , donorphoneno);
        Donation.put("Donor Email" , donoremail);
        Donation.put("Book Type" , booktype.getText().toString());
        Donation.put("No of Books Donor wants to Donate" , noofbooks.getText().toString());
        Donation.put("Donated to" , orgname);

        firestore.collection("Book Donation Details").document(donorname).set(Donation).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(BooksCategoryList.this, "Donation Registered Successfully", Toast.LENGTH_SHORT).show();

                AlertDialog.Builder builder = new AlertDialog.Builder(BooksCategoryList.this);
                builder.setMessage( "\n1. The Organization Will Contact For Further Details.\n" +
                        "\n2. Please ensure that the books are in good condition and are in a readable condition");
                builder.setTitle("Important Points to remember");
                builder.setCancelable(false);
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(BooksCategoryList.this , DonationSuccessful.class);
                        startActivity(intent);
                        finish();
                    }
                });

                AlertDialog alertDialog = builder.create();
                alertDialog.show();

            }
        });

    }
//    public void setToolbar(){
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//
//    }

    public  void init(){
        toolbar=findViewById(R.id.bookscategorytoolbar);
        booktype = findViewById(R.id.booktype);
        noofbooks = findViewById(R.id.noofbooks);
        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        firebaseUser = auth.getCurrentUser();
        cref = firestore.collection("Users Details");
        dref = cref.document(firebaseUser.getEmail());
        orgname = getIntent().getStringExtra("Org Name");
        donate = findViewById(R.id.booksdonate);

        dref.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                donorname  = task.getResult().getString("Name");
                donoremail = task.getResult().getString("Email");
                donorphoneno = task.getResult().getString("Phone No");
            }
        });
    }
}