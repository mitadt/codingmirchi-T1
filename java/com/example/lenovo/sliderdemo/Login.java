package com.example.lenovo.sliderdemo;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class Login extends AppCompatActivity {
    int counter = 5;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        final RelativeLayout layout = (RelativeLayout)findViewById(R.id.layout);
        layout.setBackgroundColor(Color.BLACK);
        final Button login = (Button)findViewById(R.id.login);
        final EditText user = (EditText)findViewById(R.id.user);
        final EditText pass = (EditText)findViewById(R.id.password);
        final TextView hint = (TextView)findViewById(R.id.hint);
        hint.setText(Integer.toString(counter));
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(user.getText().toString().equals("admin") &&
                        pass.getText().toString().equals("admin")) {
                    Toast.makeText(getApplicationContext(),
                            "Redirecting...",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Login.this, MainActivity.class);
                    startActivity(intent);

                }else{
                    Toast.makeText(getApplicationContext(), "Wrong Credentials",Toast.LENGTH_SHORT).show();

                            user.setVisibility(View.VISIBLE);
                    user.setBackgroundColor(Color.RED);
                    pass.setBackgroundColor(Color.RED);
                    counter--;
                    //String Counter = Integer.toString(counter);
                    hint.setText(Integer.toString(counter));

                    if (counter == 0) {
                        login.setEnabled(false);
                    }
                }
            }
        });

    }
}
