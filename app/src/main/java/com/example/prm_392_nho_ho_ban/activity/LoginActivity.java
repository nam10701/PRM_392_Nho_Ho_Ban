package com.example.prm_392_nho_ho_ban.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.prm_392_nho_ho_ban.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity {
    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?", Pattern.CASE_INSENSITIVE);

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    ProgressDialog progressDialog;
    EditText edtEmail;
    EditText edtPassword;
    TextView tvForgot;
    Button btnLogin;
    Button btnRegister;
    private void bindingUI(){
        edtEmail = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtPassword);
        btnLogin = findViewById(R.id.btnLogin);
        tvForgot = findViewById(R.id.tvForgot);
        btnRegister = findViewById(R.id.btnRegister);
        progressDialog = new ProgressDialog(this);
    }
    private void bindingAction(){
        btnLogin.setOnClickListener(this::login);
        btnRegister.setOnClickListener(this::startRegisterIntent);
        tvForgot.setOnClickListener(this::forgot);
    }

    private void forgot(View view) {
        startActivity(new Intent(this,ResetPasswordActivity.class));
    }

    private void startRegisterIntent(View view) {
        Intent registerIntent = new Intent(this, RegisterActivity.class);
        startActivity(registerIntent);
    }

    private void login(View view) {
       String email = edtEmail.getText().toString();
       String password = edtPassword.getText().toString();
        progressDialog.show();
       if(validate(email,password)){
       firebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
           @Override
           public void onComplete(@NonNull Task<AuthResult> task) {
               progressDialog.dismiss();
               if(task.isSuccessful()){
                  startActivity(new Intent(getApplicationContext(), WelcomeActivity.class));
               }else{
                   Toast.makeText(getApplicationContext(),"Login failed",Toast.LENGTH_LONG).show();
               }
           }
       });
       }else{
           progressDialog.dismiss();
       }
    }

    private boolean validate(String email, String password){

        if(TextUtils.isEmpty(email)){
            Toast.makeText(this,"Enter email?",Toast.LENGTH_LONG).show();
            return false;
        }else {
            Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(email);
            if(!matcher.find()){
            Toast.makeText(this,"Enter right email?",Toast.LENGTH_LONG).show();
            return false;}
        }
        if(TextUtils.isEmpty(password)){
            Toast.makeText(this,"Enter password?",Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        bindingUI();
        bindingAction();
    }

    @Override
    protected void onStart() {
        super.onStart();
        authorize();
    }

    private void authorize() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user!=null){
            Intent intent = new Intent(this, WelcomeActivity.class);
            startActivity(intent);
        }
    }
}