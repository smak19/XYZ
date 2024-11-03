package com.example.xyz;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class UserProfile extends AppCompatActivity {

    ArrayList<String> list;
    Spinner spinner;
    ArrayAdapter<String> adapter;
    Toolbar toolbar;
    Button submit;
    String email;
    FloatingActionButton edit;
    EditText username, userphoneno, emailedittext;
    ImageView pic;
    FirebaseUser user;
    FirebaseAuth auth;
    BottomNavigationView bottomNavigationView;
    CollectionReference cref;
    private final  int GALLERY_REQ_CODE=100;
    DocumentReference documentReference;
    String encode;
    Uri newuri;
    String useremail , urii;

    FirebaseFirestore firestore;
    String logintype;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        init();
        setUser();
        seturi();
        bottomNavigationView.setSelectedItemId(R.id.optprofile);


        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                int id=item.getItemId();

                if(id==R.id.opthome){
                    Intent ihome=new Intent(UserProfile.this,HomeActivity.class);
                    startActivity(ihome);
                    finish();
                }

                else if (id==R.id.optlogout){
                    Intent ilogout=new Intent(UserProfile.this,LoginActivity.class);
                    ilogout.putExtra("Email",email);
                    auth.signOut();
                    startActivity(ilogout);
                    finish();

                }
                else {
                    Toast.makeText(UserProfile.this, "Already on User Profile", Toast.LENGTH_SHORT).show();
                }
                return false;
            }
        });


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                String item = adapterView.getItemAtPosition(position).toString();
                Log.e("heyyy", "" + item);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent igallery = new Intent(Intent.ACTION_PICK);
                igallery.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(igallery , GALLERY_REQ_CODE);

            }
        });

        submit.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View view) {
                if(username.getText().toString().isEmpty()){
                    Toast.makeText(UserProfile.this, "Please Enter Your Name", Toast.LENGTH_SHORT).show();
                }
                else if(emailedittext.getText().toString().isEmpty()){
                    Toast.makeText(UserProfile.this, "Please Enter Your Email ID", Toast.LENGTH_SHORT).show();
                }

                else if(userphoneno.getText().toString().isEmpty()){
                    Toast.makeText(UserProfile.this, "Please Enter Your Phone No", Toast.LENGTH_SHORT).show();
                }

                else if(userphoneno.getText().toString().length()<10 || userphoneno.getText().toString().length()>10){
                    Toast.makeText(UserProfile.this, "Please Enter Valid 10 Digit Phone Number", Toast.LENGTH_SHORT).show();
                }
                else {
                    updateUser();
                }
            }
        });



    }

    private void updateUser() {
        Map<String , Object> User = new HashMap<>();
        User.put("Blood Type" , spinner.getSelectedItem().toString());
        User.put("Email" , emailedittext.getText().toString());
        User.put("Name" , username.getText().toString());
        User.put("Phone No" , userphoneno.getText().toString());
        if(encode == null){
            // Toast.makeText(this, "inside null", Toast.LENGTH_SHORT).show();
            User.put("uri" , urii);
        }
        else{
            //Toast.makeText(this, "encode is not null", Toast.LENGTH_SHORT).show();
            User.put("uri" , encode);
        }

        Log.e("urii" ,""+encode);

        cref.document(useremail).update(User).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(UserProfile.this, "success", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(UserProfile.this , HomeActivity.class));
                finish();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(resultCode==RESULT_OK){
            if (requestCode==GALLERY_REQ_CODE){
                //Toast.makeText(this, "inside if", Toast.LENGTH_SHORT).show();
                Uri pp = (data.getData());
                Bitmap bitmap;
                pic.setImageURI(pp);
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), pp);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                encode = Base64.encodeToString(bitmapToByteArray(bitmap) , Base64.DEFAULT);
                Log.e("pp" , ""+pp);
                Intent intent = new Intent();
                intent.putExtra("uri" , pp);
                Log.e("uri" , ""+pp);
                newuri = pp;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);

    }

    private void seturi() {
        firestore.collection("Users Details").document(useremail.toString()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                // Log.e("tagggggg" , ""+task.getResult());
                String base64 = task.getResult().getString("uri");
                //Log.e("taggg" , ""+uri);
                byte[] decodedstr = Base64.decode(base64 , Base64.DEFAULT);
                Bitmap bitmap = byteArrayToBitmap(decodedstr);
                pic.setImageBitmap(bitmap);

            }
        });


    }
    public Bitmap byteArrayToBitmap(byte[] byteArray) {
        return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
    }
    void setUser() {
        Toast.makeText(this, "setuserrrr", Toast.LENGTH_LONG).show();
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                username.setText(task.getResult().getString("Name"));
                userphoneno.setText(task.getResult().getString("Phone No"));
                urii = task.getResult().getString("uri");
                Log.e("nameee" , ""+task.getResult().getString("Name"));
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }

    public byte[] bitmapToByteArray(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        try {
            stream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return byteArray;
    }



    void init () {
        toolbar = findViewById(R.id.userprofile);
        edit = findViewById(R.id.usereditpic);
        auth = FirebaseAuth.getInstance();
        username = findViewById(R.id.profileusername);
        bottomNavigationView=findViewById(R.id.bottomnavUser);
        pic = findViewById(R.id.profilepic1);
        user=auth.getCurrentUser();
        useremail = getIntent().getStringExtra("Email");
        emailedittext = findViewById(R.id.profileemailid);
        cref = FirebaseFirestore.getInstance().collection("Users Details");
        userphoneno = findViewById(R.id.profileuserphone);
        firestore = FirebaseFirestore.getInstance();
        spinner = findViewById(R.id.profilespinneruserbloodtype);
        logintype = getIntent().getStringExtra("Login Type");
        submit = findViewById(R.id.updatebutton);
        documentReference = cref.document(useremail);
        emailedittext.setText(useremail);

        firestore.collection("Users Details").document(useremail).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                String selected = task.getResult().getString("Blood Type");
                Log.e("heyyy", "" + selected);
                int pos = adapter.getPosition(selected);
                spinner.setSelection(pos);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
        list = new ArrayList<>();

        list.add("A+");
        list.add("A-");
        list.add("B+");
        list.add("B-");
        list.add("AB+");
        list.add("AB-");
        list.add("O+");
        list.add("O-");
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, list);
        adapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
        spinner.setAdapter(adapter);

    }
}