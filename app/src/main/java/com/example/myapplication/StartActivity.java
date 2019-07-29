package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.myapplication.util.IpCorect;

public class StartActivity extends AppCompatActivity {
    ImageButton imageButton;
    String IP;
    EditText editText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageButton = findViewById(R.id.btnStart);
        editText=findViewById(R.id.edit);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IP="";
                String edit=editText.getText().toString();
                IP+="192.168."+edit;
                Log.e("IP Start",IP);
                if(!IP.equals("192.168.")){
                    if(IpCorect.ipIsCorect(IP)){

                    Toast.makeText(getApplicationContext(),"Подключение к роботу",Toast.LENGTH_LONG).show();
                    goMainActivity(IP);
                }
                    else{
                        Toast.makeText(getApplicationContext(),"Неверный IP, попробуйте еще раз",Toast.LENGTH_LONG).show();
                    }
            }
                else{
                    Toast.makeText(getApplicationContext(),"Неверный IP, попробуйте еще раз",Toast.LENGTH_LONG).show();
                }
            }
        });

    }
    private void goMainActivity(String ip){
        Intent intent = new Intent(StartActivity.this, MainActivity.class);
        intent.putExtra("IP",ip);
        startActivity(intent);
    }
}
