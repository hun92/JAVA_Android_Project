package com.example.tavara;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class MenuActivity extends AppCompatActivity {
    private static final String DRIVER = "oracle.jdbc.OracleDriver";
    private static final String URL = "jdbc:oracle:thin:@192.168.30.15:1521:xe";
    private static final String USERNAME = "HR";
    private static final String PASSWORD = "1234";
    private Connection connection;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        StrictMode.ThreadPolicy threadPolicy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(threadPolicy);

        String name = getIntent().getStringExtra("이름");
        String cardNum = getIntent().getStringExtra("복지카드번호");
        String account = getIntent().getStringExtra("잔액");
        String phone = getIntent().getStringExtra("전화번호");
        String id = getIntent().getStringExtra("아이디");
        String getcardNum = getIntent().getStringExtra("복지카드번호");
        TextView approval = findViewById(R.id.approval);
        TextView getName = findViewById(R.id.userName);
        TextView getAccount = findViewById(R.id.account);
        getName.setText(name);

        try{
            Class.forName(DRIVER);
            this.connection = DriverManager.getConnection(URL,USERNAME,PASSWORD);

            String sql = "select approval from users where name = ?";
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setString(1,name);
            ResultSet resultSet = pstmt.executeQuery();
            if(resultSet.next()==true){
                String getApproval = resultSet.getString(1).toString();
                if(getApproval.equals("1")){
                    approval.setText("고객 등록완료");
                    getAccount.setText("잔액 : " + account + " 원");
                }else{
                    approval.setText("고객 미등록");
                    ImageView receipt = findViewById(R.id.receipt);
                    receipt.setClickable(false);
                    ImageView reservation = findViewById(R.id.reservationBtn);
                    reservation.setClickable(false);
                    ImageView plusAccount = findViewById(R.id.plusAccount);
                    plusAccount.setClickable(false);
                    ImageView history = findViewById(R.id.history);
                    history.setClickable(false);
                    ImageView center = findViewById(R.id.imageView);
                    center.setClickable(false);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void receiptBtn(View view){
        String name = getIntent().getStringExtra("이름");
        String cardNum = getIntent().getStringExtra("복지카드번호");
        String account = getIntent().getStringExtra("잔액");
        String phone = getIntent().getStringExtra("전화번호");
        String id = getIntent().getStringExtra("아이디");
        String cardAccount = getIntent().getStringExtra("복지카드잔액");
        ImageView receipt = findViewById(R.id.receipt); // 회원가입 페이지로 이동하는 버튼 객체 생성
        receipt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MenuActivity.this, ReceiptActivity.class); //버튼을 눌렀을 때 회원가입 페이지로 이동
                intent.putExtra("이름", name);
                intent.putExtra("복지카드번호", cardNum);
                intent.putExtra("잔액", account);
                intent.putExtra("전화번호", phone);
                intent.putExtra("복지카드잔액", cardAccount);
                intent.putExtra("아이디", id);
                startActivity(intent);
            }
        });
    }

    public void reservationtBtn(View view){
        String name = getIntent().getStringExtra("이름");
        String cardNum = getIntent().getStringExtra("복지카드번호");
        String account = getIntent().getStringExtra("잔액");
        String phone = getIntent().getStringExtra("전화번호");
        String id = getIntent().getStringExtra("아이디");
        String cardAccount = getIntent().getStringExtra("복지카드잔액");
        ImageView Btn = findViewById(R.id.reservationBtn); // 회원가입 페이지로 이동하는 버튼 객체 생성
        Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MenuActivity.this, ReservationActivity.class); //버튼을 눌렀을 때 회원가입 페이지로 이동
                intent.putExtra("이름", name);
                intent.putExtra("복지카드번호", cardNum);
                intent.putExtra("잔액", account);
                intent.putExtra("전화번호",phone);
                intent.putExtra("복지카드잔액", cardAccount);
                intent.putExtra("아이디", id);
                startActivity(intent);
            }
        });
    }

    public void historyBtn(View view){
        String name = getIntent().getStringExtra("이름");
        String cardNum = getIntent().getStringExtra("복지카드번호");
        String account = getIntent().getStringExtra("잔액");
        String phone = getIntent().getStringExtra("전화번호");
        String id = getIntent().getStringExtra("아이디");
        String cardAccount = getIntent().getStringExtra("복지카드잔액");
        ImageView Btn = findViewById(R.id.history); // 회원가입 페이지로 이동하는 버튼 객체 생성
        Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MenuActivity.this, HistoryActivity.class); //버튼을 눌렀을 때 회원가입 페이지로 이동
                intent.putExtra("이름", name);
                intent.putExtra("복지카드번호", cardNum);
                intent.putExtra("잔액", account);
                intent.putExtra("전화번호",phone);
                intent.putExtra("아이디", id);
                intent.putExtra("복지카드잔액", cardAccount);
                startActivity(intent);
            }
        });
    }

    public void accountPlusBtn(View view){
        String name = getIntent().getStringExtra("이름");
        String cardNum = getIntent().getStringExtra("복지카드번호");
        String cardAccount = getIntent().getStringExtra("복지카드잔액");
        String account = getIntent().getStringExtra("잔액");
        String phone = getIntent().getStringExtra("전화번호");
        String id = getIntent().getStringExtra("아이디");
        ImageView Btn = findViewById(R.id.plusAccount); // 회원가입 페이지로 이동하는 버튼 객체 생성
        Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MenuActivity.this, AccountPlusActivity.class); //버튼을 눌렀을 때 회원가입 페이지로 이동
                intent.putExtra("이름", name);
                intent.putExtra("복지카드번호", cardNum);
                intent.putExtra("복지카드잔액", cardAccount);
                intent.putExtra("잔액", account);
                intent.putExtra("전화번호",phone);
                intent.putExtra("아이디", id);
                startActivity(intent);
            }
        });
    }

    public void userInfoBtn(View view){
        String name = getIntent().getStringExtra("이름");
        String cardNum = getIntent().getStringExtra("복지카드번호");
        String cardAccount = getIntent().getStringExtra("복지카드잔액");
        String account = getIntent().getStringExtra("잔액");
        String phone = getIntent().getStringExtra("전화번호");
        String id = getIntent().getStringExtra("아이디");
        ImageView Btn = findViewById(R.id.imageView); // 회원가입 페이지로 이동하는 버튼 객체 생성
        Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MenuActivity.this, UserInfoActivity.class); //버튼을 눌렀을 때 회원가입 페이지로 이동
                intent.putExtra("이름", name);
                intent.putExtra("복지카드번호", cardNum);
                intent.putExtra("복지카드잔액", cardAccount);
                intent.putExtra("잔액", account);
                intent.putExtra("전화번호",phone);
                intent.putExtra("아이디",id);
                startActivity(intent);
            }
        });
    }
}