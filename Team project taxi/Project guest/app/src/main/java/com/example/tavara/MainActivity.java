package com.example.tavara;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;


public class MainActivity extends AppCompatActivity {
    private static final String DRIVER = "oracle.jdbc.OracleDriver";
    private static final String URL = "jdbc:oracle:thin:@192.168.30.15:1521:xe";
    private static final String USERNAME = "HR";
    private static final String PASSWORD = "1234";
    private Connection connection;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //보안정책
        StrictMode.ThreadPolicy threadPolicy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(threadPolicy);
    }

    public void loginBtn(View view){
        TextView et_id = findViewById(R.id.et_userId); //사용자 이름을 받는 객체생성
        TextView et_pass = findViewById(R.id.et_date); //비밀번호를 받는 객체생성
        String id = et_id.getText().toString().replaceAll(" ",""); //입력받은 이름을 공백제거 후 변수에 저장
        String password = et_pass.getText().toString().replaceAll(" ",""); //입력받은  비밀번호를 공백제거 후 변수에 저장

        try{
            //DB접속
            Class.forName(DRIVER);
            this.connection = DriverManager.getConnection(URL,USERNAME,PASSWORD);

            String sql = "select * from users where id = ? and password = ?"; //sql 쿼리문 생성
            PreparedStatement pstmt = connection.prepareStatement(sql); //sqlite 생성
            pstmt.setString(1,id); // sql문의 ? 순서대로 값 세팅
            pstmt.setString(2,password); // sql문의 ? 순서대로 값 세팅
            ResultSet resultSet = pstmt.executeQuery();//쿼리문 실행 후 resultSet에 저장

            //sql 문 실행 후 튜플값이 있으면 실행
            if(resultSet.next() == true){
                String getName = resultSet.getString(1); //resultSet의 첫 번째 열의 값인 name을 저장
                String getPassword = resultSet.getString(2); //resultSet의 두 번째 열의 값인 password을 저장
                String getCardNum = resultSet.getString(3);
                String getAccount = resultSet.getString(6);
                String getId = resultSet.getString(8);
                String phone = resultSet.getString(7);
                String cardAccount = resultSet.getString(9);
                //사용자가 입력한 이름과 비밀번호가 일치하면 실행
                if(getId.equals(id) && getPassword.equals(password)){
                    Intent intent = new Intent(MainActivity.this, MenuActivity.class);
                    intent.putExtra("이름", getName);
                    intent.putExtra("복지카드번호", getCardNum);
                    intent.putExtra("잔액", getAccount);
                    intent.putExtra("전화번호", phone);
                    intent.putExtra("복지카드잔액", cardAccount);
                    intent.putExtra("아이디", getId);
                    startActivity(intent);
                    Toast myToast = Toast.makeText(this.getApplicationContext(),"로그인이 성공하였습니다.", Toast.LENGTH_SHORT);
                    myToast.show();
                }else{
                    Toast myToast = Toast.makeText(this.getApplicationContext(),"비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT);
                    myToast.show();
                }
            }else{ //sql 문 실행 후 튜플 값이 없으면 실행
                Toast myToast = Toast.makeText(this.getApplicationContext(),"계정확인이 불가합니다.", Toast.LENGTH_SHORT);
                myToast.show();
            }
        }catch (Exception e){
            Toast myToast = Toast.makeText(this.getApplicationContext(),"로그인이 에러.", Toast.LENGTH_SHORT);
            myToast.show();
        }
    }


    public void registerBtn(View view){
        Button insert = findViewById(R.id.bt_join); // 회원가입 페이지로 이동하는 버튼 객체 생성
        insert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, RegisterActivity.class); //버튼을 눌렀을 때 회원가입 페이지로 이동
                startActivity(intent);
            }
        });
    }
}