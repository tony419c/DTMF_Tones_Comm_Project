package com.bignerdranch.android.comp9336_project;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button mButton_Task1_Sender;
    Button mButton_Task1_Receiver;
    Button mButton_Task2_Sender;
    Button mButton_Task3_DTMF_Sender;
    Button mButton_Task2_3_Detector;
    Button mButton_Task4_Sender;
    Button mButton_Task4_Receiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mButton_Task1_Sender = (Button) findViewById(R.id.button_Task1_Sender);
        mButton_Task1_Sender.setOnClickListener
                (
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent i = new Intent(MainActivity.this, Task1_Sender_Activity.class);
                                startActivity(i);
                            }
                        }
                );

        mButton_Task1_Receiver = (Button) findViewById(R.id.button_Task1_Receiver);
        mButton_Task1_Receiver.setOnClickListener
                (
                        new View.OnClickListener()
                        {
                            @Override
                            public void onClick(View view) {
                                Intent i = new Intent(MainActivity.this, Task1_Receiver_Activity.class);
                                startActivity(i);
                            }
                        }
                );

        mButton_Task2_Sender = (Button) findViewById(R.id.button_Task2_Sender);
        mButton_Task2_Sender.setOnClickListener
                (
                        new View.OnClickListener()
                        {
                            @Override
                            public void onClick(View view) {
                                Intent i = new Intent(MainActivity.this, Task2_Sender_Activity.class);
                                startActivity(i);
                            }
                        }
                );

        mButton_Task3_DTMF_Sender = (Button) findViewById(R.id.button_Task3_DTMF_Sender);
        mButton_Task3_DTMF_Sender.setOnClickListener
                (
                        new View.OnClickListener()
                        {
                            @Override
                            public void onClick(View view) {
                                Intent i = new Intent(MainActivity.this, Task3_DTMF_Sender_Activity.class);
                                startActivity(i);
                            }
                        }
                );

        mButton_Task2_3_Detector = (Button) findViewById(R.id.button_Task2_3_Detector);
        mButton_Task2_3_Detector.setOnClickListener
                (
                        new View.OnClickListener()
                        {
                            @Override
                            public void onClick(View view) {
                                Intent i = new Intent(MainActivity.this, Task2_3_Detector_Activity.class);
                                startActivity(i);
                            }
                        }
                );

        mButton_Task4_Sender = (Button) findViewById(R.id.button_Task4_String_Sender);
        mButton_Task4_Sender.setOnClickListener
                (
                        new View.OnClickListener()
                        {
                            @Override
                            public void onClick(View view) {
                                Intent i = new Intent(MainActivity.this, Task4_Sender_Activity.class);
                                startActivity(i);
                            }
                        }
                );

        mButton_Task4_Receiver = (Button) findViewById(R.id.button_Task4_String_Receiver);
        mButton_Task4_Receiver.setOnClickListener
                (
                        new View.OnClickListener()
                        {
                            @Override
                            public void onClick(View view) {
                                Intent i = new Intent(MainActivity.this, Task4_Receiver_Activity.class);
                                startActivity(i);
                            }
                        }
                );

    }
}
