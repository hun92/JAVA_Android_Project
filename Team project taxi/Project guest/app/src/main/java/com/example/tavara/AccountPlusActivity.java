package com.example.tavara;

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

public class AccountPlusActivity extends AppCompatActivity {
    private static final String DRIVER = "oracle.jdbc.OracleDriver";
    private static final String URL = "jdbc:oracle:thin:@192.168.30.15:1521:xe";
    private static final String USERNAME = "HR";
    private static final String PASSWORD = "1234";
    private Connection connection;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_plus);

        StrictMode.ThreadPolicy threadPolicy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(threadPolicy);

        String name = getIntent().getStringExtra("이름");
        String cardNum = getIntent().getStringExtra("복지카드번호");
        String cardAccount = getIntent().getStringExtra("복지카드잔액");
        String account = getIntent().getStringExtra("잔액");
        String phone = getIntent().getStringExtra("전화번호");
        String id = getIntent().getStringExtra("아이디");
        TextView editet_Cardnum = findViewById(R.id.editet_Cardnum);
        editet_Cardnum.setText(cardNum);
    }

    public void plusBtn(View view){
        String name = getIntent().getStringExtra("이름");
        String cardNum = getIntent().getStringExtra("복지카드번호");
        String cardAccount = getIntent().getStringExtra("복지카드잔액");
        String account = getIntent().getStringExtra("잔액");
        String phone = getIntent().getStringExtra("전화번호");
        String id = getIntent().getStringExtra("아이디");
        TextView et_money = findViewById(R.id.et_money);
        String strMoney = et_money.getText().toString();
        int money = Integer.parseInt(strMoney); //충전 할 잔액
        int getAccount = Integer.parseInt(account); // 현재 잔액
        int getCardAccount = Integer.parseInt(cardAccount); //복지카드 잔액
        if(money > getCardAccount){
            Toast myToast = Toast.makeText(this.getApplicationContext(),"복지카드에 잔액이 부족합니다.", Toast.LENGTH_SHORT);
            myToast.show();
        }else {
            getAccount += money;
            getCardAccount -=money;
            try{
                Class.forName(DRIVER);
                this.connection = DriverManager.getConnection(URL,USERNAME,PASSWORD);

                String sql = "update users set account = ?, cardaccount = ? where name = ? and id = ?";
                PreparedStatement pstmt = connection.prepareStatement(sql);
                pstmt.setString(1,String.valueOf(getAccount));
                pstmt.setString(2,String.valueOf(getCardAccount));
                pstmt.setString(3,name);
                pstmt.setString(4,id);
                pstmt.executeUpdate();

                Toast myToast = Toast.makeText(this.getApplicationContext(),"충전이 완료되었습니다.", Toast.LENGTH_SHORT);
                myToast.show();

                Intent intent = new Intent(AccountPlusActivity.this, MenuActivity.class);
                intent.putExtra("이름", name);
                intent.putExtra("복지카드번호", cardNum);
                intent.putExtra("복지카드잔액", cardAccount);
                intent.putExtra("잔액", String.valueOf(getAccount));
                intent.putExtra("전화번호",phone);
                intent.putExtra("아이디",id);
                startActivity(intent);

            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}