package com.example.projecttaxi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

//로그인
public class MainActivity extends AppCompatActivity {


    private static final String DRIVER = "oracle.jdbc.OracleDriver";
    private static final String URL = "jdbc:oracle:thin:@192.168.30.15:1521:xe";
    private static final String USERNAME = "HR";
    private static final String PASSWORD = "1234";
    private Connection connection;

    String D_Name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //보안정책
        StrictMode.ThreadPolicy threadPolicy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(threadPolicy);

    }

    public void OnClickedLogin(View view) {

        TextView id = findViewById(R.id.et_id);
        TextView pass = findViewById(R.id.et_pass);

        String getId = id.getText().toString();
        String getPass = pass.getText().toString();


        try {
            Class.forName(DRIVER);
            this.connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);

            String sql = "SELECT * FROM DRIVER WHERE ID = ? AND PASSWORD = ?";
            PreparedStatement stmt = connection.prepareStatement(sql);

            stmt.setString(1, getId);
            stmt.setString(2, getPass);
            ResultSet result = stmt.executeQuery();

            if (result.next() == true) {

                Toast mytoast = Toast.makeText(getApplicationContext(), "로그인 성공", Toast.LENGTH_LONG);
                mytoast.show();
                D_Name = result.getString(1);

                Intent nextMenu = new Intent(MainActivity.this, Menu.class);
                nextMenu.putExtra("이름", D_Name);
                nextMenu.putExtra("아이디", getId);
                startActivity(nextMenu);


            } else {
                Toast mytoast = Toast.makeText(getApplicationContext(), "계정이 없습니다.", Toast.LENGTH_LONG);
                mytoast.show();
            }

        } catch (Exception e) {
            Toast mytoast = Toast.makeText(getApplicationContext(), "로그인 실패", Toast.LENGTH_LONG);
            mytoast.show();




        }


    }


}