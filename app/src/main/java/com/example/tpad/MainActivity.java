package com.example.tpad;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {
    private EditText emailEditText;
    private EditText passEditText;
    TextView forgot_password;
    Button login_btn;
    ProgressBar progressBar;
    FirebaseAuth mFirebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);

        emailEditText =(EditText) findViewById(R.id.username);
        passEditText = (EditText) findViewById(R.id.password);
        login_btn = findViewById(R.id.login_button);
        forgot_password = findViewById(R.id.forgot_pass);
        progressBar = findViewById(R.id.progressBar);
        mFirebaseAuth = FirebaseAuth.getInstance();

        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String email = emailEditText.getText().toString();
             if (TextUtils.isEmpty(email)){
                    emailEditText.setError("Email is Required");
                    return;
                }
                String password = passEditText.getText().toString();
                if (TextUtils.isEmpty(password)){
                    passEditText.setError("Password is Required!!");
                    return;
                }
                progressBar.setVisibility(View.VISIBLE);
                mFirebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(MainActivity.this, "Login Succefull,Welcome :(", Toast.LENGTH_LONG).show();
                            startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                        } else {
                            Toast.makeText(MainActivity.this, "Login Failed,Please Try Again!!" + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });



            }
        });
        forgot_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText resetMail = new EditText(v.getContext());
                AlertDialog.Builder passwordResetDialog = new AlertDialog.Builder(v.getContext());
                passwordResetDialog.setTitle("Reset your Password?");
                passwordResetDialog.setMessage("Enter Your Email to Recieve Reset Link.");
                passwordResetDialog.setView(resetMail);

                passwordResetDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //extract mall and send reset link
                        String mail = resetMail.getText().toString();
                        mFirebaseAuth.sendPasswordResetEmail(mail).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(MainActivity.this,"Reset Link Has been Sent To Your Email",Toast.LENGTH_LONG).show();

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(MainActivity.this, "Error! Reset Link Is Not Sent"+e.getMessage(), Toast.LENGTH_SHORT).show();

                            }
                        });

                    }
                });
                passwordResetDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                passwordResetDialog.create().show();


            }
        });
    }

    public void go_to_register(View view) {
        Intent intent = new Intent(MainActivity.this,signup.class);
        startActivity(intent);
    }
}
