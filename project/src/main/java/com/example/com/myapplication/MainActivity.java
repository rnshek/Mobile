package com.example.com.myapplication;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;

public class MainActivity extends Activity {
    LinearLayout baseLayout;

    DatePicker dp;
    EditText edtDiary;
    Button btnSave;
    String fileName;
    TextView textView, textView2; //맴버 변수

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("모바일과제");

        baseLayout = (LinearLayout) findViewById(R.id.baseLayout);

        textView = (TextView) findViewById(R.id.day);
        textView2 = (TextView) findViewById(R.id.text);

        final View dialogView = (View) View.inflate(MainActivity.this,
                R.layout.dialog, null);  //dialog.xml 파일을 인플레이트 해서
        AlertDialog.Builder dlg = new AlertDialog.Builder(MainActivity.this);

        dp = (DatePicker) dialogView.findViewById(R.id.datePicker);
        edtDiary = (EditText) findViewById(R.id.diary);
        btnSave = (Button) findViewById(R.id.save);
        textView.setText(Integer.toString(dp.getYear()) + "년" + Integer.toString(dp.getMonth() + 1) + "월" + Integer.toString(dp.getDayOfMonth()) + "일");//오늘날짜를 get하여 TextView에 set함

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final View dialogView = (View) View.inflate(MainActivity.this,
                        R.layout.dialog, null);  //dialog.xml 파일을 인플레이트 해서
                AlertDialog.Builder dlg = new AlertDialog.Builder(MainActivity.this);
                dlg.setTitle("날짜 선택");
                dlg.setView(dialogView);
                dlg.setPositiveButton("확인",  //확인버튼 생성
                        new DialogInterface.OnClickListener() { //확인버튼 온클릭 리스너 (DatePicker의 정보를 get하여 TextView에 set함)
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dp = (DatePicker) dialogView.findViewById(R.id.datePicker);

                                textView.setText(Integer.toString(dp.getYear()) + "년" + Integer.toString(dp.getMonth() + 1) + "월" + Integer.toString(dp.getDayOfMonth()) + "일");//오늘날짜를 get하여 TextView에 set함
                            }
                        });

                dlg.setNegativeButton("닫기", null); //닫기버튼 생성
                dlg.show();
            }

        });


        Calendar cal = Calendar.getInstance();
        int cYear =dp.getYear();
        int cMonth = dp.getMonth();
        int cDay = dp.getDayOfMonth();

        dp.init(cYear, cMonth, cDay, new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                fileName = Integer.toString(year) + "_" + Integer.toString(monthOfYear + 1) + "_"
                        + Integer.toString(dayOfMonth) + ".txt";
                String str = readDiary(fileName);
                edtDiary.setText(str);
                btnSave.setEnabled(true);
            }
        });
        fileName = Integer.toString(dp.getYear()) + "_" + Integer.toString(dp.getMonth() + 1) + "_" + Integer.toString(dp.getDayOfMonth()) + ".txt";
        String str = readDiary(fileName);
        if(!str.equals("")) {
            edtDiary.setText(str);
            btnSave.setEnabled(true);
        }  //처음 실행하면 해당 날짜의 일기가 있어도 나오지 않는다. 처음 실행할 때부터 그날의 일기가 있으면 에디트텍스트에 일기를 보여주고


        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    FileOutputStream outFs = openFileOutput(fileName, Context.MODE_WORLD_WRITEABLE); //일기 파일(연_월_일.txt)을 쓰기 모드로 연다.
                    String str = edtDiary.getText().toString();
                    outFs.write(str.getBytes()); //에디트 텍스트의 대용을 일기 파일에 byte[]형으로 쓰고, 파일을 닫는다.
                    outFs.close();
                    Toast.makeText(getApplicationContext(), fileName + " 이 저장됨", 0).show(); //저장된 파일의 이름을 토스트 메시지로 출력한다.
                }catch (IOException e) {
                }

            }

        });
    }
    private String readDiary (String fName) {
        String diaryStr = null;
        FileInputStream inFs;
        try {
            inFs = openFileInput(fName);
            byte[] txt = new byte[500];
            inFs.read(txt);
            inFs.close();
            diaryStr = (new String(txt)).trim();
            btnSave.setText("수정하기");
        } catch (IOException e) {
            edtDiary.setHint("일기 없음");
            btnSave.setText ("새로 저장");
        }
        return  diaryStr;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater mInflater = getMenuInflater(); //메뉴 인플래이터를 생성하고
        mInflater.inflate(R.menu.menu1, menu); // munu1.xml 파일을 등록한다.
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) { //switch문 (어떤 항목을 선택했는지 구하여  case로 각 항목마다 실행할 내용 설정
            case R.id.big:
                edtDiary.setTextSize(30); //크게 메뉴버튼을 누르면 글씨크기를 30으로 지정
                return true;
            case R.id.nomal:
                edtDiary.setTextSize(20); //중간 메뉴버튼을 누르면 글씨크기를 20으로 지정
                return true;
            case R.id.small:
                edtDiary.setTextSize(10); //작게 메뉴버튼을 누르면 글씨크기를 20으로 지정
                return true;
        }
        return false;
    }
}