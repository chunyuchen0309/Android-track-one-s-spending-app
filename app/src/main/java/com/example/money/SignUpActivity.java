package com.example.money;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

public class SignUpActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private String account;
    private String password;
    private String name;
    private EditText accountEdit,input_name;
    private EditText passwordEdit;
    private Button signUpBtn;
    private FirebaseDatabase db;
    private DatabaseReference myRef;
    private FirebaseUser firebaseUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        initView(); //呼叫 initView()
    }

    private void initView() {
        mAuth = FirebaseAuth.getInstance();
        myRef=FirebaseDatabase.getInstance().getReference();
        accountEdit = (EditText) findViewById(R.id.account_edit);
        input_name = (EditText) findViewById(R.id.input_name);
        passwordEdit = (EditText) findViewById(R.id.password_edit);
        signUpBtn = (Button) findViewById(R.id.signup_button);
        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                account = accountEdit.getText().toString();
                password = passwordEdit.getText().toString();
                name=input_name.getText().toString();
                if(TextUtils.isEmpty(account)){
                    Toast.makeText(SignUpActivity.this,"Account is Empty",Toast.LENGTH_LONG).show();
                    return;
                }
                if(TextUtils.isEmpty(password)){
                    Toast.makeText(SignUpActivity.this,"Password is Empty",Toast.LENGTH_LONG).show();
                    return;
                }
                if(TextUtils.isEmpty(name)){
                    Toast.makeText(SignUpActivity.this,"Name is Empty",Toast.LENGTH_LONG).show();
                    return;
                }
                mAuth.createUserWithEmailAndPassword(account, password)//將帳號密碼傳至Firebase
                .addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(SignUpActivity.this, R.string.register_success, Toast.LENGTH_SHORT).show();
                            firebaseUser=mAuth.getCurrentUser();
                            UserProfileChangeRequest profileChangeRequest=new UserProfileChangeRequest.Builder().setDisplayName(name).build();
                            firebaseUser.updateProfile(profileChangeRequest);
                            myRef.child(mAuth.getCurrentUser().getUid()).setValue(name);
                            Intent intent = new Intent();
                            intent.setClass(SignUpActivity.this, LoginActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(SignUpActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
        addUserIF();
    }
    private void addUserIF() {
        db= FirebaseDatabase.getInstance();
        myRef=db.getReference("userIFO");
    }
}
