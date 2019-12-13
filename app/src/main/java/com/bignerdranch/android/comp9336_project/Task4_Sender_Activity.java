package com.bignerdranch.android.comp9336_project;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;

public class Task4_Sender_Activity extends AppCompatActivity
    implements View.OnClickListener, Runnable
{

    EditText mEditText_Duration;
    EditText mEditText_Content;
    Button mButton_Send;
    Button mButton_StopSending;

    int mLowFreq_0 = 1500;
    int mHighFreq_1 = 7000;
    int mMidFreq_Level;

    byte[] mPreamble = {(byte) 0xAA, (byte) 0xAA};

    String mContent;
    String mStrDuration;
    float mDuration;

    int m_Interval_ms = 150; //120

    byte[] mContentBytes;

    boolean isKeepSending = true;
    Thread mThread_Sending;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task4_sender);

        mEditText_Duration = (EditText) findViewById(R.id.editText_Task4_Duration);
        mEditText_Content = (EditText) findViewById(R.id.editText_Task4_Content);
        mButton_Send = (Button) findViewById(R.id.button_Task4_Send);
        mButton_Send.setOnClickListener(this);
        mButton_StopSending = (Button) findViewById(R.id.button_Task4_StopSending);
        mButton_StopSending.setOnClickListener(this);

    }


    private static int getBitofByte(byte m_single_Byte, int position)
    {
        if (position < 0 || position > 7)
        {
            return -1;
        }

        int result = 0;
        int test_byte = 1; // 1 in the least significant bit

        test_byte = test_byte << position;
        result = m_single_Byte & test_byte;

        return result == 0 ? 0 : 1;
    }

    @Override
    // This event-response function is only for the click of button_Task4_Send and button_Task4_StopSending
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.button_Task4_Send:
                if(!getInputfromEditText())
                {
                    break;
                }
                if(!getFloatDuration())
                {
                    break;
                }

                byte[] m_temp_content_bytes = mContent.getBytes(StandardCharsets.UTF_16LE);

                mContentBytes = new byte[mPreamble.length + m_temp_content_bytes.length];
                System.arraycopy(mPreamble, 0, mContentBytes, 0, mPreamble.length);
                System.arraycopy(m_temp_content_bytes, 0, mContentBytes, mPreamble.length, m_temp_content_bytes.length);

                mMidFreq_Level = (mLowFreq_0 + mHighFreq_1) / 2;

                mThread_Sending = new Thread(this);
                mThread_Sending.start();

                break;


            case R.id.button_Task4_StopSending:

                isKeepSending = false;
                mThread_Sending.interrupt();


                break;
        }
    }


    private boolean getInputfromEditText() {
        mContent = mEditText_Content.getText().toString();
        mContent = mContent.trim();
        if (mContent.isEmpty())
        {
            return false;
        }

        mStrDuration = mEditText_Duration.getText().toString();
        mStrDuration = mStrDuration.trim();
        if (mStrDuration.isEmpty())
        {
            return false;
        }

        return true;
    }

    private boolean getFloatDuration() {

        try
        {
            mDuration = Float.parseFloat(mStrDuration);
        }
        catch (RuntimeException e)
        {
            return false;
        }

        if (mDuration <= 0.0 )
        {
            return false;
        }

        return true;
    }


    @Override
    public void run() {
        for ( int i = 0; i < mContentBytes.length && isKeepSending; i++ )
        {
            sendSingleByte(mContentBytes[i]);
            if (i > 1 && 1 == i % 2) // One more Unicode character just finished being sent
            {
                int m_Parity = calcParity(mContentBytes[i - 1], mContentBytes[i]);
                switch (m_Parity)
                {
                    case 0:
                        SpecFreqToneGenerator.generateSingleFreqTone(mMidFreq_Level, mDuration);
                        interBitSleep();
                        SpecFreqToneGenerator.generateSingleFreqTone(mLowFreq_0, mDuration);
                        interBitSleep();
                        break;
                    case 1:
                        SpecFreqToneGenerator.generateSingleFreqTone(mMidFreq_Level, mDuration);
                        interBitSleep();
                        SpecFreqToneGenerator.generateSingleFreqTone(mHighFreq_1, mDuration);
                        interBitSleep();
                        break;
                }
            }
        }
    }

    public static int calcParity(byte m_UnicodeChar_Prev_2, byte m_UnicodeChar_Prev_1)
    {
        int m_num_of_1s = 0;
        for (int i = 0; i < 8 ;i++)
        {
            if ( getBitofByte(m_UnicodeChar_Prev_2, i) == 1 )
            {
                m_num_of_1s++;
            }
            if ( getBitofByte(m_UnicodeChar_Prev_2, i) == 1 )
            {
                m_num_of_1s++;
            }
        }

        return (m_num_of_1s % 2);
    }

    private void sendSingleByte(byte m_single_byte)
    {
        int mCurrBit = 0;
        for( int j = 0; j < 8 && isKeepSending ; j++ )
        {
            mCurrBit = getBitofByte(m_single_byte, j);
            if ( 0 == mCurrBit )
            {

                SpecFreqToneGenerator.generateSingleFreqTone(mMidFreq_Level, mDuration);
                interBitSleep();
                SpecFreqToneGenerator.generateSingleFreqTone(mLowFreq_0, mDuration);
                interBitSleep();
            }
            else if ( 1 == mCurrBit)
            {
                SpecFreqToneGenerator.generateSingleFreqTone(mMidFreq_Level, mDuration);
                interBitSleep();
                SpecFreqToneGenerator.generateSingleFreqTone(mHighFreq_1, mDuration);
                interBitSleep();
            }
        }
    }

    private void interBitSleep()
    {
        try
        {
            Thread.sleep(m_Interval_ms);
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
