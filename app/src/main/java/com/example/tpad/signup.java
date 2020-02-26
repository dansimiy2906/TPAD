package com.example.tpad;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class signup extends AppCompatActivity {
    EditText EditText_tsc_number, EditText_id_number, EditText_phone, EditText_email, EditText_password, EditText_c_Password;
    ProgressBar progressBar;
    Button signup_btn;
    String userID;
    FirebaseAuth mFirebaseAuth;
    FirebaseFirestore fstore;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);

        EditText_tsc_number = findViewById(R.id.tsc_number);
        EditText_id_number = findViewById(R.id.id_no);
        EditText_phone = findViewById(R.id.phone);
        EditText_email = findViewById(R.id.email_address);
        EditText_password = findViewById(R.id.password);
        EditText_c_Password = findViewById(R.id.confirm_password);
        signup_btn = findViewById(R.id.register_btn);
        progressBar = findViewById(R.id.progressBar2);

        mFirebaseAuth = FirebaseAuth.getInstance();
        fstore = FirebaseFirestore.getInstance();

        if (mFirebaseAuth.getCurrentUser() != null){
            startActivity(new Intent(getApplicationContext(), HomeActivity.class));
            finish();

        }

        signup_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String tsc_no = EditText_tsc_number.getText().toString();
                final String id_number = EditText_id_number.getText().toString();
                final String phone_id = EditText_phone.getText().toString();
                final String email = EditText_email.getText().toString();
                String pwd = EditText_password.getText().toString();
                String cpwd = EditText_c_Password.getText().toString();

                if (TextUtils.isEmpty(tsc_no)){
                    EditText_tsc_number.setError("Email is Required!!");
                    return;
                }
               else if (TextUtils.isEmpty(id_number)){
                    EditText_id_number.setError("Id Number is required!!");
                    return;
                }
                else if (TextUtils.isEmpty(phone_id)){
                    EditText_phone.setError("Phone Number is Required!!");
                    return;
                }
               else if (TextUtils.isEmpty(email)){
                    EditText_email.setError("Email is Required!!");
                    return;
                }
                else if (TextUtils.isEmpty(pwd)){
                    EditText_password.setError("Password is Required!!");
                    return;
                }
                else if (TextUtils.isEmpty(cpwd)){
                    EditText_c_Password.setError("Password Has Not Been Conformed!!");
                    return;
                }
               else if (pwd == cpwd){
                    Toast.makeText(signup.this,"Passwords have Matched!!",Toast.LENGTH_LONG).show();
                }



                progressBar.setVisibility(View.VISIBLE);

                mFirebaseAuth.createUserWithEmailAndPassword(email,pwd).addOnCompleteListener( new OnCompleteListener<AuthResult>() {

                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    FirebaseUser fuser = mFirebaseAuth.getCurrentUser();
                                    fuser.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Toast.makeText(signup.this,"Verification Mail Has Been Sent to Your Email",Toast.LENGTH_LONG).show();

                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(signup.this ,"Email Has Not Been Sent "+ e.getMessage(),Toast.LENGTH_LONG ).show();

                                        }
                                    });



                                    Toast.makeText(signup.this, "User Created", Toast.LENGTH_LONG).show();
                                    userID = mFirebaseAuth.getCurrentUser().getUid();
                                    DocumentReference documentReference = fstore.collection("users").document(userID);
                                    Map<String,Object> user = new HashMap<>();
                                    user.put("tsc_num",tsc_no);
                                    user.put("id",id_number);
                                    user.put("phone",phone_id);
                                    user.put("email",email);
                                    documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {

                                        @Override
                                        public void onSuccess(Void aVoid) {

                                        }
                                    });
                                    startActivity(new Intent(getApplicationContext(), HomeActivity.class));

                                }
                                else {
                                    Toast.makeText(signup.this,"Signup Failed,Please Try Again!!"+task.getException().getMessage(),Toast.LENGTH_LONG).show();
                                    progressBar.setVisibility(View.GONE);
                                }
                            }
                        });
            }
        });

    }
}





