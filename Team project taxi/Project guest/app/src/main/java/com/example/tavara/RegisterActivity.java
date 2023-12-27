package com.example.tavara;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

public class RegisterActivity extends AppCompatActivity {

    private static final String DRIVER = "oracle.jdbc.OracleDriver";
    private static final String URL = "jdbc:oracle:thin:@192.168.30.15:1521:xe";
    private static final String USERNAME = "HR";
    private static final String PASSWORD = "1234";
    private Connection connection;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //보안정책
        StrictMode.ThreadPolicy threadPolicy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(threadPolicy);
    }

    public void registerBtn(View view){
        TextView et_name = findViewById(R.id.et_userId); //사용자에게 입력받을 이름 객체 생성
        TextView et_password = findViewById(R.id.et_pass); //사용자에게 입력받을 비밀번호 객체 생성
        TextView et_date = findViewById(R.id.et_date); //사용자에게 입력받을 생년월일 객체 생성
        TextView et_cardNum = findViewById(R.id.et_cardnum); //사용자에게 입력받을 복지카드번호 객체 생성
        TextView et_phone = findViewById(R.id.et_phnum); //사용자에게 입력받을 전화번호 객체 생성
        TextView et_id = findViewById(R.id.et_id);

        String name = et_name.getText().toString().replaceAll(" ",""); //이름을 입력받은 후 변수에 저장
        String password = et_password.getText().toString().replaceAll(" ","");//비밀번호를 입력받은 후 변수에 저장
        String date = et_date.getText().toString().replaceAll(" ","");//생년월일을 입력받은 후 변수에 저장
        String cardNum = et_cardNum.getText().toString().replaceAll(" ","");//복지카드번호를 입력받은 후 변수에 저장
        String phone = et_phone.getText().toString().replaceAll(" ","");//전화번호를 입력받은 후 변수에 저장
        String id = et_id.getText().toString().replaceAll(" ","");

        try{
            //DB접속
            Class.forName(DRIVER);
            this.connection = DriverManager.getConnection(URL,USERNAME,PASSWORD);
            String sql = "INSERT INTO users VALUES (?, ?, ?, TO_DATE(?, 'yy/MM/dd'), 0, '0', ?, ?, '500000')";
            //String sql = "insert into users values(?, ?, ?, TO_DATE(?'YY/MM/DD'), 0, '0', ?, ?, '500000')";//sql insert문 생성
            PreparedStatement pstmt = connection.prepareStatement(sql); //sqlite 생성


            //sql문에 세팅
            pstmt.setString(1,name);
            pstmt.setString(2,password);
            pstmt.setString(3, cardNum);
            pstmt.setString(4,date);
            pstmt.setString(5, phone);
            pstmt.setString(6, id);
            pstmt.executeUpdate();//sql 실행


            Intent intent = new Intent(RegisterActivity.this, MainActivity.class); //버튼을 눌렀을 때 회원가입 페이지로 이동
            startActivity(intent);
            Toast myToast = Toast.makeText(this.getApplicationContext(),"회원가입이 완료되었습니다.", Toast.LENGTH_SHORT);
            myToast.show();



        }catch (Exception e){
            Toast myToast = Toast.makeText(this.getApplicationContext(),"회원가입 에러.", Toast.LENGTH_SHORT);
            myToast.show();
            e.printStackTrace();


        }

    }

    public void cancleBtn(View view){
        Button cancle = findViewById(R.id.bt_cancel); // 로그인 페이지로 이동하는 버튼 객체 생성
        cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegisterActivity.this, MainActivity.class); //버튼을 눌렀을 때 로그인 페이지로 이동
                startActivity(intent);
            }
        });
    }
}