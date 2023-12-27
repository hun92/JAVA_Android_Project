package com.example.tavara;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ReservationActivity extends AppCompatActivity {
    private static final String DRIVER = "oracle.jdbc.OracleDriver";
    private static final String URL = "jdbc:oracle:thin:@192.168.30.15:1521:xe";
    private static final String USERNAME = "HR";
    private static final String PASSWORD = "1234";
    private Connection connection;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation);

        StrictMode.ThreadPolicy threadPolicy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(threadPolicy);

        String name = getIntent().getStringExtra("이름");
        String cardNum = getIntent().getStringExtra("복지카드번호");
        String cardAccount = getIntent().getStringExtra("복지카드잔액");
        String account = getIntent().getStringExtra("잔액");
        String phone = getIntent().getStringExtra("전화번호");

        try{
            Class.forName(DRIVER);
            this.connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            Spinner spinner = findViewById(R.id.spinner2);
            String sql = "select num, reservetime, departure from reservation where username = ? and approval = 0 or approval = 1 or approval = 2 order by reservetime asc";
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, name);
            ResultSet resultSet = pstmt.executeQuery();
            String spin = "";
            while(resultSet.next()){
                spin = spin+"(" +resultSet.getString(1) + ")"+resultSet.getString(2) +"  - "+resultSet.getString(3)+"," ;
            }
            String[] title = spin.toString().split(",");
            connection.close();
            ArrayAdapter<String> adapter = new ArrayAdapter<String>
                    (this, android.R.layout.simple_spinner_item,title);
            spinner.setAdapter(adapter);

        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public void deleteBtn(View view){
        String name = getIntent().getStringExtra("이름");
        String cardNum = getIntent().getStringExtra("복지카드번호");
        String cardAccount = getIntent().getStringExtra("복지카드잔액");
        String account = getIntent().getStringExtra("잔액");
        String phone = getIntent().getStringExtra("전화번호");
        String id = getIntent().getStringExtra("아이디");

        Spinner spinner = findViewById(R.id.spinner2);
        String[] number = spinner.getSelectedItem().toString().split("\\)");
        String num = number[0].replaceAll("\\(","");
        try {
            Class.forName(DRIVER);
            this.connection = DriverManager.getConnection(URL,USERNAME,PASSWORD);

            AlertDialog.Builder myAlertBuilder =
                    new AlertDialog.Builder(ReservationActivity.this);
            // alert의 title과 Messege 세팅
            myAlertBuilder.setTitle("Alert");
            myAlertBuilder.setMessage("예약을 삭제하시겠습니까?");
            // 버튼 추가 (Ok 버튼과 Cancle 버튼 )
            myAlertBuilder.setPositiveButton("Ok",new DialogInterface.OnClickListener(){
                public void onClick(DialogInterface dialog,int which){
                    // OK 버튼을 눌렸을 경우
                    try {
                        String sql = "delete from reservation where username = ? and num = ?";
                        PreparedStatement pstmt = connection.prepareStatement(sql);
                        pstmt.setString(1, name);
                        pstmt.setString(2, num);
                        pstmt.executeUpdate();
                        Toast.makeText(getApplicationContext(),"예약이 취소되었습니다.",
                                Toast.LENGTH_SHORT).show();
                        TextView tv_date = findViewById(R.id.tv_date);
                        TextView tv_Tname = findViewById(R.id.tv_Tname);
                        TextView tv_Chanum = findViewById(R.id.tv_Chanum);
                        TextView tv_phnum = findViewById(R.id.tv_phnum);
                        TextView tv_departure = findViewById(R.id.tv_departure);
                        TextView tv_Arrival = findViewById(R.id.tv_Arrival);
                        TextView tv_count = findViewById(R.id.tv_count);
                        TextView tv_Check = findViewById(R.id.tv_Check);
                        TextView tv_money = findViewById(R.id.tv_money);
                        TextView tv_receipt = findViewById(R.id.textView46);

                        tv_date.setText("");
                        tv_Tname.setText("");
                        tv_Chanum.setText("");
                        tv_phnum.setText("");
                        tv_departure.setText("");
                        tv_Arrival.setText("");
                        tv_count.setText("");
                        tv_Check.setText("");
                        tv_money.setText("");
                        tv_receipt.setText("");


                        Intent intent = new Intent(ReservationActivity.this, MenuActivity.class); //버튼을 눌렀을 때 회원가입 페이지로 이동
                        intent.putExtra("이름", name);
                        intent.putExtra("복지카드번호", cardNum);
                        intent.putExtra("복지카드잔액", cardAccount);
                        intent.putExtra("잔액", account);
                        intent.putExtra("전화번호",phone);
                        intent.putExtra("아이디",id);
                        startActivity(intent);

                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            });
            myAlertBuilder.setNegativeButton("Cancle", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // Cancle 버튼을 눌렸을 경우
                    Toast.makeText(getApplicationContext(),"Pressed Cancle",
                            Toast.LENGTH_SHORT).show();
                }
            });
            // Alert를 생성해주고 보여주는 메소드(show를 선언해야 Alert가 생성됨)
            myAlertBuilder.show();

            Toast myToast = Toast.makeText(this.getApplicationContext(),"예약삭제가 완료되었습니다.", Toast.LENGTH_SHORT);
            myToast.show();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void selectBtn(View view){

        String name = getIntent().getStringExtra("이름");
        String cardNum = getIntent().getStringExtra("복지카드번호");
        String cardAccount = getIntent().getStringExtra("복지카드잔액");
        String account = getIntent().getStringExtra("잔액");
        String phone = getIntent().getStringExtra("전화번호");

        TextView tv_date = findViewById(R.id.tv_date);
        TextView tv_Tname = findViewById(R.id.tv_Tname);
        TextView tv_Chanum = findViewById(R.id.tv_Chanum);
        TextView tv_phnum = findViewById(R.id.tv_phnum);
        TextView tv_departure = findViewById(R.id.tv_departure);
        TextView tv_Arrival = findViewById(R.id.tv_Arrival);
        TextView tv_count = findViewById(R.id.tv_count);
        TextView tv_Check = findViewById(R.id.tv_Check);
        TextView tv_money = findViewById(R.id.tv_money);
        TextView tv_receipt = findViewById(R.id.textView46);
        Spinner spinner = findViewById(R.id.spinner2);
        String[] number = spinner.getSelectedItem().toString().split("\\)");
        String num = number[0].replaceAll("\\(","");
        try{
            Class.forName(DRIVER);
            this.connection = DriverManager.getConnection(URL,USERNAME,PASSWORD);

            String sql = "select * from reservation where num = ? and username = ?";
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, num);
            pstmt.setString(2,name);
            ResultSet resultSet = pstmt.executeQuery();
            if(resultSet.next() == true){
                tv_date.setText(resultSet.getString(4));
                tv_Tname.setText(resultSet.getString(8));
                tv_Chanum.setText(resultSet.getString(7));
                tv_phnum.setText(resultSet.getString(11));
                tv_departure.setText(resultSet.getString(2));
                tv_Arrival.setText(resultSet.getString(3));
                tv_count.setText(resultSet.getString(6));
                if(resultSet.getString(5).equals("0")){
                    tv_Check.setText("없음");
                }else{
                    tv_Check.setText("있음");
                }
                tv_money.setText(resultSet.getString(9));
                if(resultSet.getString(12).equals("0")){
                    tv_receipt.setText("접수대기");
                }else if(resultSet.getString(12).equals("1")){
                    tv_receipt.setText("배차완료");
                }else if(resultSet.getString(12).equals("2")){
                    tv_receipt.setText("기사승인완료");
                }else if(resultSet.getString(12).equals("3")){
                    tv_receipt.setText("운행완료");
                }else {
                    tv_receipt.setText("운행거절");
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}