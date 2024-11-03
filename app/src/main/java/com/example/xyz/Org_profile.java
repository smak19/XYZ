package com.example.xyz;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Org_profile extends AppCompatActivity {
    EditText orgname , orgmbno , orgemail, orgupi , orgaddress , orgpincode;
    String email;
    FirebaseFirestore firestore;
    BottomNavigationView b1;
    ArrayList<String> list ;
    FirebaseUser user ;
    FirebaseAuth auth;
    Spinner spinner;
    CollectionReference cref;
    Button update;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_org_profile);
        init();
        setDetails();

        b1.setSelectedItemId(R.id.optorgprofile);
        b1.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                int id=item.getItemId();

                if(id==R.id.optorghome){
                    Intent ihome=new Intent(Org_profile.this,OrganizationDashboard.class);
                    ihome.putExtra("Email",email);
                    startActivity(ihome);
                    finish();

                }
                else if (id==R.id.optorglogout){
                    Intent ilogout=new Intent(Org_profile.this,LoginActivity.class);
                    auth.signOut();
                    startActivity(ilogout);
                    finish();

                }
                else {
                    Toast.makeText(Org_profile.this, "Already on Profile Page", Toast.LENGTH_SHORT).show();
                }
                return false;
            }
        });

        update.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View view) {
                if(orgname.getText().toString().isEmpty()){
                    Toast.makeText(Org_profile.this, "Please Enter Your Name", Toast.LENGTH_SHORT).show();
                }
                else if(orgemail.getText().toString().isEmpty()){
                    Toast.makeText(Org_profile.this, "Please Enter Your Email ID", Toast.LENGTH_SHORT).show();
                }

                else if(orgmbno.getText().toString().isEmpty()){
                    Toast.makeText(Org_profile.this, "Please Enter Your Phone No", Toast.LENGTH_SHORT).show();
                }

                else if(orgmbno.getText().toString().length()<10 || orgmbno.getText().toString().length()>10){
                    Toast.makeText(Org_profile.this, "Please Enter Valid 10 Digit Phone Number", Toast.LENGTH_SHORT).show();
                } else if (orgaddress.getText().toString().isEmpty()) {
                    Toast.makeText(Org_profile.this, "Please Enter Organization Address", Toast.LENGTH_SHORT).show();
                } else if (orgpincode.getText().toString().isEmpty() || orgpincode.getText().toString().length()>6 || orgpincode.getText().toString().length()<6) {
                    Toast.makeText(Org_profile.this, "Please Enter Valid ^ digit pin code", Toast.LENGTH_SHORT).show();
                } else if (orgupi.getText().toString().isEmpty()) {
                    Toast.makeText(Org_profile.this, "Please enter Organization Upi ID", Toast.LENGTH_SHORT).show();
                } else {
                    updateUser();
                }
            }
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                String item = adapterView.getItemAtPosition(position).toString();
                Log.e("6969" , ""+item);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void setDetails() {
        cref.document(user.getEmail()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                orgname.setText(task.getResult().getString("Name"));
                orgupi.setText(task.getResult().getString("Organisation UPI ID"));
                orgmbno.setText(task.getResult().getString("Organization Number"));
                orgemail.setText(task.getResult().getString("Organization Email"));
                orgpincode.setText(task.getResult().getString("Organization Pin Code"));
                orgaddress.setText(task.getResult().getString("Organization Address"));
            }
        });
    }

    private void updateUser() {
        Map<String , Object> User = new HashMap<>();
        User.put("Name" , orgname.getText().toString());
        User.put("Organization Email" , orgemail.getText().toString());
        User.put("Organization Address" , orgaddress.getText().toString());
        User.put("Organization Number" , orgmbno.getText().toString());
        User.put("Organisation UPI ID" , orgupi.getText().toString());
        User.put("Organization Pin Code" , orgpincode.getText().toString());
        User.put("Organization Category" , spinner.getSelectedItem().toString());


        cref.document(user.getEmail()).update(User).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(Org_profile.this, "Details Updated Successfully", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(Org_profile.this , OrganizationDashboard.class));
                finish();
            }
        });
    }

    public  void  init(){
        orgname = findViewById(R.id.orgprofileusername);
        orgmbno = findViewById(R.id.orgprofilephoneno);
        orgemail = findViewById(R.id.orgprofileemailid);
        update = findViewById(R.id.orgupdatebutton);
        firestore = FirebaseFirestore.getInstance();
        orgupi = findViewById(R.id.orgprofileupiid);
        spinner = findViewById(R.id.profilespinnerorgcategory);
        orgaddress = findViewById(R.id.orgprofileaddress);
        orgpincode = findViewById(R.id.orgprofilepincode);
        b1=findViewById(R.id.bottomnavprofile);
        user = FirebaseAuth.getInstance().getCurrentUser();
        cref = firestore.collection("Organization Details");
        list = new ArrayList<>();
        list.add("Food");
        list.add("Grocery");
        list.add("Blood");
        list.add("Books");
        list.add("Clothes");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this , android.R.layout.simple_spinner_item ,list);
        adapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
        spinner.setAdapter(adapter);

        firestore.collection("Organization Details").document(user.getEmail()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                String selected = task.getResult().getString("Organization Category");
                Log.e("heyyy", "" + selected);
                int pos = adapter.getPosition(selected);
                spinner.setSelection(pos);

            }
        });

    }
}