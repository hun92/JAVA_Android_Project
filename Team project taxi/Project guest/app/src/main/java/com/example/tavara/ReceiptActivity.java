package com.example.tavara;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Calendar;

public class ReceiptActivity extends AppCompatActivity{
    private static final String DRIVER = "oracle.jdbc.OracleDriver";
    private static final String URL = "jdbc:oracle:thin:@192.168.30.15:1521:xe";
    private static final String USERNAME = "HR";
    private static final String PASSWORD = "1234";
    private Connection connection;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receipt);

        StrictMode.ThreadPolicy threadPolicy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(threadPolicy);
    }

    public void showDateTimePickerDialog(View view) {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        TextView reserveTime = findViewById(R.id.reserveTime);
        reserveTime.setVisibility(View.VISIBLE);
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {
                final int selectedYearFinal = selectedYear;
                final int selectedMonthFinal = selectedMonth;
                final int selectedDayFinal = selectedDay;

                TimePickerDialog timePickerDialog = new TimePickerDialog(ReceiptActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int selectedHour, int selectedMinute) {
                        // 사용자가 선택한 날짜와 시간을 텍스트뷰에 설정
                        String selectedDateTime = (selectedYearFinal % 100) + "-" + (selectedMonthFinal + 1) + "-" + selectedDayFinal + " " + selectedHour + ":" + selectedMinute;
                        reserveTime.setText(selectedDateTime);
                    }
                }, hour, minute, false);

                timePickerDialog.show();
            }
        }, year, month, day);

        datePickerDialog.show();
    }

    public void checkBtn2(View view){
        String name = getIntent().getStringExtra("이름");
        String cardNum = getIntent().getStringExtra("복지카드번호");
        String cardAccount = getIntent().getStringExtra("복지카드잔액");
        String account = getIntent().getStringExtra("잔액");
        String phone = getIntent().getStringExtra("전화번호");
        String id = getIntent().getStringExtra("아이디");

        TextView et_departure = findViewById(R.id.et_departure);
        TextView et_arrival = findViewById(R.id.et_arrival);
        TextView reserveTime = findViewById(R.id.reserveTime);
        TextView peoples = findViewById(R.id.peoples);
        if(peoples.getText().toString().equals("0")){
            Toast myToast = Toast.makeText(this.getApplicationContext(),"인원을 선택해주세요.", Toast.LENGTH_SHORT);
            myToast.show();
        }

        CheckBox wheelChair = findViewById(R.id.wheelchair);
        int wheel;
        if(wheelChair.isChecked()){
            wheel = 1;
        }else {
            wheel = 0;
        }
        CheckBox reservation = findViewById(R.id.reservation);
        try{
            Class.forName(DRIVER);
            this.connection = DriverManager.getConnection(URL,USERNAME,PASSWORD);
            CheckBox receipt = findViewById(R.id.ch_receipt);
            CheckBox cb_reservation = findViewById(R.id.reservation);
            TextView amount = findViewById(R.id.amount);
            String sql2 = "select account from users where name = ?";
            PreparedStatement pstmt2 = connection.prepareStatement(sql2);
            pstmt2.setString(1,name);
            ResultSet resultSet = pstmt2.executeQuery();
            if(resultSet.next()==true){
                String getAccount = resultSet.getString(1).toString();
                int getAmount = Integer.parseInt(amount.getText().toString());
                if(Integer.parseInt(getAccount) < getAmount){
                    Toast myToast = Toast.makeText(this.getApplicationContext(),"잔액이 부족합니다.", Toast.LENGTH_SHORT);
                    myToast.show();
                }else if(receipt.isChecked()){
                    String sql = "INSERT INTO RESERVATION VALUES(?, ?, ?, to_char(sysdate, 'yy-mm-dd hh:mi'), ?, ?, '미정', '미정', ?, reserve_seq.nextval,'미정', 0, ?)";
                    PreparedStatement pstmt = connection.prepareStatement(sql);
                    pstmt.setString(1,name);
                    pstmt.setString(2, et_departure.getText().toString());
                    pstmt.setString(3,et_arrival.getText().toString());
                    pstmt.setInt(4,wheel);
                    pstmt.setInt(5,Integer.parseInt(peoples.getText().toString()));
                    pstmt.setString(6, amount.getText().toString());
                    pstmt.setString(7,phone);
                    pstmt.executeUpdate();
                    Toast myToast = Toast.makeText(this.getApplicationContext(),"접수가 완료되었습니다.", Toast.LENGTH_SHORT);
                    myToast.show();
                    Intent intent = new Intent(ReceiptActivity.this, MenuActivity.class); //버튼을 눌렀을 때 회원가입 페이지로 이동
                    intent.putExtra("이름", name);
                    intent.putExtra("복지카드번호", cardNum);
                    intent.putExtra("복지카드잔액", cardAccount);
                    intent.putExtra("잔액", account);
                    intent.putExtra("전화번호",phone);
                    intent.putExtra("아이디", id);
                    startActivity(intent);
                }else if(receipt.isChecked()==false && reservation.isChecked()==false){
                    Toast myToast = Toast.makeText(this.getApplicationContext(),"날짜와 시간을 선택해주세요ㅂㅈ.", Toast.LENGTH_SHORT);
                    myToast.show();
                }else if(cb_reservation.isChecked()){
                    String sql = "INSERT INTO RESERVATION VALUES(?, ?, ?, ?, ?, ?, '미정', '미정', ?,reserve_seq.nextval,'미정', 0,?)";
                    PreparedStatement pstmt = connection.prepareStatement(sql);
                    pstmt.setString(1,name);
                    pstmt.setString(2, et_departure.getText().toString());
                    pstmt.setString(3,et_arrival.getText().toString());
                    pstmt.setString(4,reserveTime.getText().toString());
                    pstmt.setInt(5,wheel);
                    pstmt.setInt(6,Integer.parseInt(peoples.getText().toString()));
                    pstmt.setString(7, amount.getText().toString());
                    pstmt.setString(8, phone);
                    pstmt.executeUpdate();
                    Toast myToast = Toast.makeText(this.getApplicationContext(),"예약이 완료되었습니다.", Toast.LENGTH_SHORT);
                    myToast.show();
                    Intent intent = new Intent(ReceiptActivity.this, MenuActivity.class); //버튼을 눌렀을 때 회원가입 페이지로 이동
                    intent.putExtra("이름", name);
                    intent.putExtra("복지카드번호", cardNum);
                    intent.putExtra("복지카드잔액", cardAccount);
                    intent.putExtra("잔액", account);
                    intent.putExtra("전화번호",phone);
                    intent.putExtra("아이디", id);
                    startActivity(intent);
                }
            }

        }catch (Exception e){
            Toast myToast = Toast.makeText(this.getApplicationContext(),"예약에 실패하였습니다.", Toast.LENGTH_SHORT);
            myToast.show();
            e.printStackTrace();
        }
    }

    public void peoplesUp(View view){
        TextView peoples = findViewById(R.id.peoples);
        int num = Integer.parseInt(peoples.getText().toString());
        num++;
        if(num<=5){
            peoples.setText(String.valueOf(num));
        }else{
            Toast myToast = Toast.makeText(this.getApplicationContext(),"인원제한은 5명입니다.", Toast.LENGTH_SHORT);
            myToast.show();
        }
    }

    public void peoplesDown(View view){
        TextView peoples = findViewById(R.id.peoples);
        int num = Integer.parseInt(peoples.getText().toString());
        num--;
        if(num<=5 && num>=0){
            peoples.setText(String.valueOf(num));
        }else if(num<1){
            Toast myToast = Toast.makeText(this.getApplicationContext(),"최소1명이상 선택해주세요.", Toast.LENGTH_SHORT);
            myToast.show();
        }
    }
}