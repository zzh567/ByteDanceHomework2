package com.zzh.loginandusers;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class UserpageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_userpage);

        TextView tvName = findViewById(R.id.tv_name);
        TextView tvSign = findViewById(R.id.tv_sign);

        View itemInfo = findViewById(R.id.item_info);
        View itemFavorite = findViewById(R.id.item_favorite);

        SharedPreferences sp = getSharedPreferences("user_profile", MODE_PRIVATE);

        String username = sp.getString("current_user", "未登录");
        String signature = sp.getString("signature", "这个人什么也没写~");

        tvName.setText(username);
        tvSign.setText(signature);

        if (tvSign != null) {
            tvSign.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showEditSignatureDialog(tvSign);
                }
            });
        }

        if (itemInfo != null) {
            itemInfo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(UserpageActivity.this, "点击了个人信息", Toast.LENGTH_SHORT).show();
                }
            });
        }

        if (itemFavorite != null) {
            itemFavorite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(UserpageActivity.this, "点击了我的收藏", Toast.LENGTH_SHORT).show();
                }
            });
        }

    }

    private void showEditSignatureDialog(TextView tvSign) {

        final EditText inputServer = new EditText(this);
        inputServer.setText(tvSign.getText());
        inputServer.setSelection(inputServer.getText().length());

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("修改个性签名")
                .setView(inputServer)
                .setNegativeButton("取消", null) // 取消按钮什么都不做
                .setPositiveButton("保存", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String newSign = inputServer.getText().toString();
                        saveSignInfo(newSign);
                        if (newSign.isEmpty()){
                            tvSign.setText("这个人什么也没写~");
                        }else{
                            tvSign.setText(newSign);
                        }
                        Toast.makeText(UserpageActivity.this, "签名修改成功", Toast.LENGTH_SHORT).show();
                    }
                });
        builder.show();
    }

    private void saveSignInfo(String sign) {
        SharedPreferences sp = getSharedPreferences("user_profile", MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("signature", sign);
        editor.apply();
    }
}