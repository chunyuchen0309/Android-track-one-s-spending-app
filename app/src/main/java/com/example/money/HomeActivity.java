package com.example.money;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
public class HomeActivity extends AppCompatActivity {
    private Button login;
    private Button signUp;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();//獲取當前用戶
        if(user == null){ //判斷是否有使用者
            setContentView(R.layout.activity_home);
            login = (Button) findViewById(R.id.login);
            signUp = (Button) findViewById(R.id.sign_up);
            //傳送至登陸頁面
            login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.setClass(HomeActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            });
            //傳送至註冊畫面
            signUp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.setClass(HomeActivity.this, SignUpActivity.class);
                    startActivity(intent);
                    finish();
                }
            });
        } else{
            //如有登入直接傳至主頁面
            Intent intent = new Intent();
            intent.setClass(HomeActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }
}
