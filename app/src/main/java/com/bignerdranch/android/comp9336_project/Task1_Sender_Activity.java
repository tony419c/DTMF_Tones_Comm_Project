package com.bignerdranch.android.comp9336_project;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class Task1_Sender_Activity extends AppCompatActivity {

    EditText mEditText_Freq;
    EditText mEditText_Duration;
    Button mButton_Generate;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task1_sender);

        mEditText_Freq = (EditText) findViewById(R.id.editText_Task1_Freq);
        mEditText_Duration = (EditText) findViewById(R.id.editText_Task1_Duration);

        mButton_Generate = (Button) findViewById(R.id.button_Task1_Generate);
        mButton_Generate.setOnClickListener
                (
                        new View.OnClickListener()
                        {
                            @Override
                            public void onClick(View view) {

                                String m_str_Freq = mEditText_Freq.getText().toString();
                                final int m_Freq;
                                try
                                {
                                    m_Freq = Integer.parseInt(m_str_Freq);
                                }
                                catch (RuntimeException e)
                                {
                                    return;
                                }

                                String m_str_Duration = mEditText_Duration.getText().toString();
                                final float m_Duration;
                                try
                                {
                                    m_Duration = Float.parseFloat(m_str_Duration);
                                }
                                catch (RuntimeException e)
                                {
                                    return;
                                }

                                runOnUiThread
                                        (
                                                new Runnable()
                                                {
                                                    @Override
                                                    public void run()
                                                    {
                                                        SpecFreqToneGenerator.generateSingleFreqTone(m_Freq, m_Duration);
                                                        //SpecFreqToneGenerator.generateSingleFreqTone_1(m_Freq, m_Duration);
                                                    }
                                                }
                                        );
                            }
                        }
                );
    }
}
