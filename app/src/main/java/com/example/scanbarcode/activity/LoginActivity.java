package com.example.scanbarcode.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.scanbarcode.R;
import com.example.scanbarcode.model.ProductModel;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import org.mindrot.jbcrypt.BCrypt;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.SQLException;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private TextInputEditText edtUser;
    private TextInputEditText edtPassword;
    private MaterialButton btnLogin;
    private MaterialButton btnRegister;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        
        Mapping();
    }

    private void Mapping() {
        edtUser = findViewById(R.id.edt_user_login);
        edtPassword = findViewById(R.id.edt_pass_login);
        btnLogin = findViewById(R.id.btn_login_login);
        btnRegister = findViewById(R.id.btn_get_register);

        btnLogin.setOnClickListener(this::onClick);
        btnRegister.setOnClickListener(this::onClick);
    }

    @Override
    public void onClick(View view) {

        if (view == btnLogin){
            String passwordFromDB = null;
//           Lay password tu DB va so sanh
                try {
                    passwordFromDB = new ProductModel().SelectPassword(edtUser.getText().toString());
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
                Log.i("passwordFromDB", String.valueOf(passwordFromDB));
                if (BCrypt.checkpw(edtPassword.getText().toString(),passwordFromDB)){
                    Toast.makeText(this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                }
                else{
                    Toast.makeText(this, "Sai tài khoản hoặc mật khẩu", Toast.LENGTH_SHORT).show();
            }

        }

        if (view == btnRegister){
//            Bam mat khau
            String hashPassword = hashPassword(edtUser.getText().toString());
//            Luu len DB
            try {
                new ProductModel().InsertAccount(edtUser.getText().toString().trim(), hashPassword);
                Toast.makeText(this, "Đăng kí thành công", Toast.LENGTH_SHORT).show();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
                Toast.makeText(this, "Đăng kí thất bại", Toast.LENGTH_SHORT).show();
            }


        }

    }
    private String hashPassword(String plainTextPassword){
        return BCrypt.hashpw(plainTextPassword, BCrypt.gensalt());
    }
}