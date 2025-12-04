package com.zzh.loginandusers;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        EditText etUsername = findViewById(R.id.et_username);
        EditText etPassword = findViewById(R.id.et_password);
        Button btnLogin = findViewById(R.id.btn_login);
        Button btnWechat = findViewById(R.id.btn_wechat);
        Button btnApple = findViewById(R.id.btn_apple);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String inputUser = etUsername.getText().toString().trim();
                String inputPass = etPassword.getText().toString().trim();

                if (inputUser.isEmpty() || inputPass.isEmpty()) {
                    Toast.makeText(MainActivity.this, "账号或密码不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (checkUserFromDB(inputUser, inputPass)) {
                    Toast.makeText(MainActivity.this, "登录成功！", Toast.LENGTH_SHORT).show();

                    saveLoginInfo(inputUser);

                    Intent intent = new Intent(MainActivity.this, UserpageActivity.class);
                    startActivity(intent);

                    finish();
                } else {
                    Toast.makeText(MainActivity.this, "账号或密码错误", Toast.LENGTH_SHORT).show();
                }
            }
        });

        View.OnClickListener thirdPartyListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "暂未开放第三方登录", Toast.LENGTH_SHORT).show();
            }
        };
        if (btnWechat != null) btnWechat.setOnClickListener(thirdPartyListener);
        if (btnApple != null) btnApple.setOnClickListener(thirdPartyListener);
    }

    private boolean checkUserFromDB(String username, String password) {
        UserDatabaseHelper dbHelper = new UserDatabaseHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(
                UserDatabaseHelper.TABLE_USERS,
                null,
                "username=? AND password=?",
                new String[]{username, password},
                null, null, null);
        boolean isExist = false;
        if (cursor.moveToFirst()) {
            isExist = true;
        }
        cursor.close();
        return isExist;
    }
    private void saveLoginInfo(String username) {
        SharedPreferences sp = getSharedPreferences("user_profile", MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        String lastUser = sp.getString("current_user", "");
        if (!username.equals(lastUser)) {
            editor.putString("signature", "这个人什么也没写~");
        }
        editor.putString("current_user", username);
        editor.apply();
    }
}