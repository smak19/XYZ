package com.example.xyz;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
public class LoginActivity extends AppCompatActivity {

    EditText email;
    FirebaseUser currentuser;
    EditText password;
    Button login;
    TextView sign;
    RadioGroup radioGroup;
    RadioButton radioButton1, radioButton2;
    SharedPreferences preferences ;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firestore;
    FirebaseUser user;
    int selected;
    String type;
    CollectionReference cref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //  SplashScreen();
        init();
        checklogin();

        sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, SigninActivity.class));
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String emal = email.getText().toString();
                String pass = password.getText().toString();


                if (emal.isEmpty() && pass.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Please Enter All The Credentials", Toast.LENGTH_SHORT).show();
                } else {
                    loginuser(emal, pass);
                }

            }
        });
    }


    void checklogin () {


        if (currentuser != null) {
            firestore.collection("Users Details").document(currentuser.getEmail().toString()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        String type = task.getResult().getString("Type of User");
                        if (type != null) {


                            if (type.equals("Donor")) {
                                Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                                intent.putExtra("Email", user.getEmail());

                                Log.e("Email", "" + email);
                                startActivity(intent);
                                finish();
                            } else if (type.equals("Organisation")){
                                startActivity(new Intent(LoginActivity.this, OrganizationDashboard.class));
                                finish();

                            }
                        }
                    }
                }
            });
        }
    }

    private void loginuser (String email2, String pass){
        firebaseAuth.signInWithEmailAndPassword(email2, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {

                    if (radioGroup.getCheckedRadioButtonId() == -1) {
                        Log.e("6969", "" + selected);
                        Toast.makeText(LoginActivity.this, "Please Select Login Type", Toast.LENGTH_SHORT).show();
                    } else if (radioButton1.isChecked()) {
                        type = radioButton1.getText().toString();

                        boolean check = preferences.getBoolean("isDetailsSubmitted?", false);
                        if (!check){

                            Intent intent;
                            intent = new Intent(LoginActivity.this, UserDetails.class);
                            intent.putExtra("Email", email2);
                            intent.putExtra("Login Type", "Donor");
                            startActivity(intent);
                            Toast.makeText(LoginActivity.this, type + " Login Successful", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                        else {



                            Intent intent;
                            intent = new Intent(LoginActivity.this, HomeActivity.class);
                            intent.putExtra("Email", email2);
                            intent.putExtra("Login Type", "Donor");
                            startActivity(intent);
                            Toast.makeText(LoginActivity.this, type + " Login Successful", Toast.LENGTH_SHORT).show();
                            firestore.collection("Users Details").document(email.getText().toString()).update("Type of User" , "Donor").addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Toast.makeText(LoginActivity.this, "Donor changed", Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.e("tggg" , "failedd");
                                }
                            });

                            finish();
                        }

                    } else if (radioButton2.isChecked()) {
                        boolean check2 = preferences.getBoolean("isorgsubmitted" , false);
                        if (!check2) {

                            type = radioButton2.getText().toString();
                            Intent intent = new Intent(LoginActivity.this, Organisationdetails.class);
                            intent.putExtra("Email", email2);
                            intent.putExtra("Login Type", type);
                            startActivity(intent);
                            Toast.makeText(LoginActivity.this, type + " Login Successful", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                        else {
                            type = radioButton2.getText().toString();
                            Intent intent = new Intent(LoginActivity.this, OrganizationDashboard.class);
                            intent.putExtra("Email", email2);
                            intent.putExtra("Login Type", type);
                            startActivity(intent);
                            Toast.makeText(LoginActivity.this, type + " Login Successful", Toast.LENGTH_SHORT).show();
                            firestore.collection("Users Details").document(email.getText().toString()).update("Type of User" , "Organisation").addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Toast.makeText(LoginActivity.this, "Organisaton changed", Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.e("tggg" , "failedd");
                                }
                            });
                            finish();
                        }
                    }

                } else {
                    Toast.makeText(LoginActivity.this, "Please Enter Valid Email And Password", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }


    void init () {

        firebaseAuth = FirebaseAuth.getInstance();
        email = findViewById(R.id.emailfield);
        password = findViewById(R.id.passwordfield);
        login = findViewById(R.id.loginButton);
        sign = findViewById(R.id.noacc);
        firestore = FirebaseFirestore.getInstance();
        radioGroup = findViewById(R.id.radioGroup);
        radioButton1 = findViewById(R.id.donorradio);
        radioButton2 = findViewById(R.id.orgradio);
        currentuser = firebaseAuth.getCurrentUser();
        selected = radioGroup.getCheckedRadioButtonId();
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        preferences = getSharedPreferences("state" , MODE_PRIVATE);


    }
//        void SplashScreen ()
//        {
//            if(!isSplashPlayed) {
//                Intent isplash = new Intent(Loginactivity.this, SplashScreen.class);
//                startActivity(isplash);
//            }
//        }

}