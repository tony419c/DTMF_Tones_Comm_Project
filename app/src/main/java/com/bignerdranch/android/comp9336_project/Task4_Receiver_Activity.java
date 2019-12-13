package com.bignerdranch.android.comp9336_project;

import android.Manifest;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

import be.tarsos.dsp.AudioDispatcher;
import be.tarsos.dsp.AudioEvent;
import be.tarsos.dsp.io.android.AudioDispatcherFactory;
import be.tarsos.dsp.pitch.PitchDetectionHandler;
import be.tarsos.dsp.pitch.PitchDetectionResult;
import be.tarsos.dsp.pitch.PitchProcessor;

public class Task4_Receiver_Activity extends AppCompatActivity {

    EditText mEditText_mLowFreq_0;
    EditText mEditText_mHighFreq_1;
    EditText mEditText_Content;
    Button mButton_StartListening;
    TextView mTextView_Status;
    TextView mTextView_CurrToneLevel;
    TextView mTextView_BitsAccum;
    TextView mTextView_CharAccum;
    TextView mTextView_ParityCheck;
    TextView mTextView_CharDropped;

    boolean isAlreadyInPreambleStage = false;

    boolean isKeepListening = false;
    int mLowFreq_0;
    int mHighFreq_1;
    int mMidFreq_Level;
    //int mFreqDemar; // Freq Demarcation
    int mFreqRangeRadius = 500;
    float mDuration;

    boolean isNowInMidFreq = true;

    int mCurrBit = -1;

    int mListeningStage = 0;
    int mBitsAccum = 0; // the number of bits accumulated both in the listening stage of preamble and data
                        // because both the preamble or any one UTF-16 character are exactly 16bits
    int mCharAccum = 0;
    int mCharDropped = 0;
    byte[] mBytes_CurrUTF16LE_Char;

    String mReceivedContent;

    int mParityCheckTextColour;

    Thread mThread_Listening;
    AudioDispatcher dispatcher;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task4_receiver);

        mEditText_mLowFreq_0 = (EditText) findViewById(R.id.editText_Task4_Receiver_Low_Freq_0);
        mEditText_mHighFreq_1 = (EditText) findViewById(R.id.editText_Task4_Receiver_High_Freq_1);
        mEditText_Content = (EditText) findViewById(R.id.editText_Task4_Receiver_Content);
        mTextView_Status = (TextView) findViewById(R.id.textView_Task4_Receiver_Status);
        mTextView_CurrToneLevel = (TextView) findViewById(R.id.textView_Task4_Receiver_CurrToneLevel);
        mTextView_BitsAccum = (TextView) findViewById(R.id.textView_Task4_Receiver_BitsAccum);
        mTextView_CharAccum = (TextView) findViewById(R.id.textView_Task4_Receiver_CharAccum);
        mTextView_ParityCheck = (TextView) findViewById(R.id.textView_Task4_Receiver_ParityCheck);
        mTextView_CharDropped = (TextView) findViewById(R.id.textView_Task4_Receiver_CharDropped);


        mButton_StartListening = (Button) findViewById(R.id.button_Task4_StartListening);
        mButton_StartListening.setOnClickListener
                (
                        new View.OnClickListener()
                        {
                            @Override
                            public void onClick(View view) {
                                try
                                {
                                    mLowFreq_0 = Integer.parseInt(mEditText_mLowFreq_0.getText().toString());
                                    mHighFreq_1 = Integer.parseInt(mEditText_mHighFreq_1.getText().toString());
                                    //mDuration = Float.parseFloat(mEditText_Duration.getText().toString());
                                }
                                catch (RuntimeException e)
                                {
                                    mTextView_Status.setText("Invalid settings!");
                                    return;
                                }

                                //mFreqDemar = (mLowFreq_0 + mHighFreq_1) / 2;
                                mMidFreq_Level = (mLowFreq_0 + mHighFreq_1) / 2;
                                mListeningStage = 0;
                                isAlreadyInPreambleStage = false;
                                mBitsAccum = 0;
                                mCharAccum = 0;
                                mCharDropped = 0;
                                mBytes_CurrUTF16LE_Char = new byte[2];
                                mBytes_CurrUTF16LE_Char[0] = 0;
                                mBytes_CurrUTF16LE_Char[1] = 0;
                                mReceivedContent = "";
                                isKeepListening = true;
                                mTextView_Status.setText("Listening...");
                                mTextView_CurrToneLevel.setTextColor(getResources().getColor(android.R.color.black));
                            }
                        }
                );



        // 2. Request for permission(s)
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, 0);

        // 3. Initialisation of FFT_YIN detector
        dispatcher =
                AudioDispatcherFactory.fromDefaultMicrophone(44100,8192,0);

        dispatcher.addAudioProcessor(new PitchProcessor(PitchProcessor.PitchEstimationAlgorithm.FFT_YIN, 44100, 8192, new PitchDetectionHandler()
        {
            @Override
            public void handlePitch(PitchDetectionResult pitchDetectionResult,
                                    final AudioEvent audioEvent) {


                final float pitchInHz = pitchDetectionResult.getPitch();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //EditText mEditText_Content = (EditText) findViewById(R.id.editText_Task4_Content);
                        //mEditText_Content.setText("" + pitchInHz);

                        if (pitchInHz > mMidFreq_Level - mFreqRangeRadius && pitchInHz < mMidFreq_Level + mFreqRangeRadius)
                        {
                            isNowInMidFreq = true;
                            if (isKeepListening)
                            {
                                mTextView_CurrToneLevel.setText("~ Mid ~");
                            }
                            else
                            {
                                mTextView_CurrToneLevel.setText("Mic status OK.");
                                mTextView_CurrToneLevel.setTextColor(getResources().getColor(android.R.color.holo_green_light));
                            }
                        }
                        else if (isNowInMidFreq && pitchInHz > mLowFreq_0 - mFreqRangeRadius && pitchInHz < mLowFreq_0 + mFreqRangeRadius)
                        {
                            // A bit 0 detected
                            isNowInMidFreq = false;
                            mTextView_CurrToneLevel.setText("- Low -");
                            handleDetectedBit(0);
                        }
                        else if ( isNowInMidFreq && pitchInHz > mHighFreq_1 - mFreqRangeRadius && pitchInHz < mHighFreq_1 + mFreqRangeRadius)
                        {
                            // A bit 1 detected
                            isNowInMidFreq = false;
                            mTextView_CurrToneLevel.setText("+ High +");
                            handleDetectedBit(1);
                        }

                    }
                });
            }
        }));

        mThread_Listening = new Thread(dispatcher,"Audio Dispatcher");
        mThread_Listening.start();

    }

    private boolean handleDetectedBit(int mDetectedBit)
    {
        if (0 == mListeningStage) // In the beginning of listening or still not yet received a full preamble
        {
            if(!isAlreadyInPreambleStage)
            {
                mTextView_Status.setText("0: Preamble");
                mTextView_ParityCheck.setText("No parity for preamble.");
                isAlreadyInPreambleStage = true;
            }

            if ( 0 == mBitsAccum % 2 ) // currently yearning for and just got a bit at an even number of position,
                                        // so a bit 1 is expected
            {
                if (1 == mDetectedBit) // An error bit received. Stop receiving all at once.
                {
                    return false;
                }
                else
                {
                    mBitsAccum++;
                }
            }
            else // 1 == mBitsAccum % 2
            {
                if (0 == mDetectedBit) // An error bit received. Stop receiving all at once.
                {
                    return false;
                }
                else
                {
                    mBitsAccum++;
                    mTextView_BitsAccum.setText( String.format("%d", mBitsAccum) );
                }
            }
            mTextView_BitsAccum.setText( String.format("%d", mBitsAccum) );

            if (16 == mBitsAccum) // An complete preamble has been received. Advance the listening stage to character mode
            {
                mListeningStage = 1;
                mTextView_Status.setText("1: Data characters");
                mBitsAccum = 0;
            }

        } //if (0 == mListeningStage)

        else if (1 == mListeningStage) // A full preamble received, now in character listening stage
        {
            if (0 == mBitsAccum)
            {
                mBytes_CurrUTF16LE_Char[0] = 0;
                mBytes_CurrUTF16LE_Char[1] = 0;
                mTextView_ParityCheck.setTextColor(getResources().getColor(android.R.color.darker_gray));
                mTextView_ParityCheck.setText("Yet to come.");
            }

            if (1 == mDetectedBit)
            {
                int m_temp_byte_1 = 0;

                if (mBitsAccum <= 7)
                {
                    m_temp_byte_1 = 1 << mBitsAccum;
                    mBytes_CurrUTF16LE_Char[0] = (byte) (mBytes_CurrUTF16LE_Char[0] | m_temp_byte_1);
                }
                else
                {
                    m_temp_byte_1 = 1 << (mBitsAccum - 8);
                    mBytes_CurrUTF16LE_Char[1] = (byte) (mBytes_CurrUTF16LE_Char[1] | m_temp_byte_1);
                }
            }

            mBitsAccum++;
            mTextView_BitsAccum.setText( String.format("%d", mBitsAccum) );

            if (16 == mBitsAccum)
            {
                /*
                receivedOneMoreChar();

                mBytes_CurrUTF16LE_Char[0] = 0;
                mBytes_CurrUTF16LE_Char[1] = 0;
                mBitsAccum = 0;
                */
                mTextView_ParityCheck.setText("Ready for parity bit.");
                mTextView_ParityCheck.setTextColor(getResources().getColor(android.R.color.holo_purple));
                mListeningStage = 2;
            }
        }

        else if (2 == mListeningStage) // Waiting for the parity bit
        {
            int m_received_parity = mDetectedBit;
            int m_calculated_parity = Task4_Sender_Activity.calcParity(mBytes_CurrUTF16LE_Char[0], mBytes_CurrUTF16LE_Char[1]);

            if (m_received_parity == m_calculated_parity)
            {
                mTextView_ParityCheck.setText("Success! Curr char saved.");
                mTextView_ParityCheck.setTextColor(getResources().getColor(android.R.color.holo_green_light));
                receivedOneMoreChar();
            }
            else
            {
                mTextView_ParityCheck.setText("Failed! Curr char dropped.");
                mTextView_ParityCheck.setTextColor(getResources().getColor(android.R.color.holo_red_light));
                mCharDropped++;
                mTextView_CharDropped.setText(String.format("%d", mCharDropped));
            }

            mBytes_CurrUTF16LE_Char[0] = 0;
            mBytes_CurrUTF16LE_Char[1] = 0;
            mBitsAccum = 0;
            mListeningStage = 1;
        }

        return true;
    }

    private void receivedOneMoreChar() {
        String mStrCurrChar = new String(mBytes_CurrUTF16LE_Char, StandardCharsets.UTF_16LE);
        mReceivedContent += mStrCurrChar;
        mEditText_Content.setText( mReceivedContent );

        mCharAccum++;
        mTextView_CharAccum.setText(String.format("%d", mCharAccum));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dispatcher.stop();
        mThread_Listening.interrupt();
    }
}
