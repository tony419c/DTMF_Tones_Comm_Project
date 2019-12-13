package com.bignerdranch.android.comp9336_project;

import android.Manifest;
import android.arch.lifecycle.ComputableLiveData;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import be.tarsos.dsp.AudioDispatcher;
import be.tarsos.dsp.AudioEvent;
import be.tarsos.dsp.io.android.AudioDispatcherFactory;
import be.tarsos.dsp.pitch.Goertzel;
import be.tarsos.dsp.pitch.PitchDetectionHandler;
import be.tarsos.dsp.pitch.PitchDetectionResult;
import be.tarsos.dsp.pitch.PitchProcessor;

public class Task2_3_Detector_Activity extends AppCompatActivity
        implements Goertzel.FrequenciesDetectedHandler, View.OnClickListener
{
    TextView mTextView_DetectingMode;

    RadioButton mRadioButton_0;
    RadioButton mRadioButton_1;
    RadioButton mRadioButton_2;
    RadioButton mRadioButton_3;

    int mDetectingMode = 0;
    /*  0: Audible single frequency digits  1: Inaudible single frequency digits
        2: Audible DTMF digits              4: Inaudible DTMF digits        */

    int mGoetzelInaudibleBase1 = 14675;
    int mGoetzelInaudibleStep1 = 181;
    int mGoetzelInaudibleBase2 = 15000;
    int mGoetzelInaudibleStep2 = 300;

    AudioDispatcher dispatcher;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        // 1. Initialisation of the Activity
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task2_3_detector);

        mRadioButton_0 = (RadioButton) findViewById(R.id.radioButton_0);
        mRadioButton_0.setOnClickListener(this);
        mRadioButton_1 = (RadioButton) findViewById(R.id.radioButton_1);
        mRadioButton_1.setOnClickListener(this);
        mRadioButton_2 = (RadioButton) findViewById(R.id.radioButton_2);
        mRadioButton_2.setOnClickListener(this);
        mRadioButton_3 = (RadioButton) findViewById(R.id.radioButton_3);
        mRadioButton_3.setOnClickListener(this);

        mTextView_DetectingMode = (TextView) findViewById(R.id.textView_DetectingMode);


        // 2. Request for permission(s)
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, 0);

        // 3. Initialisation of FFT_YIN detector
        dispatcher =
                AudioDispatcherFactory.fromDefaultMicrophone(44100,8192,0);

        // 4. Intialisation of Goertzel detector
        double freq_array[] = {697, 770, 852, 941, 1209, 1336, 1477, 1633};
        final Goertzel m_Goetzel = new Goertzel(44100, 8192, freq_array, this, 15);

        /*
        double freq_array_inaudible[] = new double[6];
        for (int i = 0; i < 3 ; i++)
        {
            freq_array_inaudible[i] = mGoetzelInaudibleBase1 + i * mGoetzelInaudibleStep1;
            freq_array_inaudible[i + 3] = mGoetzelInaudibleBase2 + i * mGoetzelInaudibleStep2;
        }
        */
        double freq_array_inaudible[] = {11343, 11612, 11931, 30000, 15193, 15366, 15549, 30000};
        final Goertzel m_Goetzel_inaudible = new Goertzel(44100, 8192, freq_array_inaudible, this, (float)1.5 );


        // 5. Set up of the ever-running thread
        dispatcher.addAudioProcessor(new PitchProcessor(PitchProcessor.PitchEstimationAlgorithm.FFT_YIN, 44100, 8192, new PitchDetectionHandler()
        {
            // buffer size originally: 1024
            @Override
            public void handlePitch(PitchDetectionResult pitchDetectionResult,
                                    final AudioEvent audioEvent)
            {
                final float pitchInHz = pitchDetectionResult.getPitch();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        if (2 == mDetectingMode)
                        {
                            m_Goetzel.process(audioEvent);
                            return;
                        }
                        else if(3 == mDetectingMode)
                        {
                            m_Goetzel_inaudible.process(audioEvent);
                            return;
                        }


                        int mDigit = 0;

                        if (0 == mDetectingMode)        // Convert audible single frequencies to digits
                        {
                            mDigit = audibleSingleFreqToDigit(pitchInHz);

                        }
                        else if (1 == mDetectingMode)   // Convert audible single frequencies to digits
                        {
                            mDigit = inaudibleSingleFreqToDigit(pitchInHz);
                        }

                        TextView mTextView_detectedDigit = (TextView) findViewById(R.id.textView_detectedDigit);

                        if (mDigit != 0)
                        {
                            mTextView_detectedDigit.setText( String.format("Detected: %d", mDigit));
                        }
                        else
                        {
                            mTextView_detectedDigit.setText( "No digit detected" );
                        }

                        // Conversion of DTMF frequencies to digits is in the charge of handleDetectedFrequencies()
                    }
                });
            }
        }));

        new Thread(dispatcher,"Audio Dispatcher").start();

    }



    private int audibleSingleFreqToDigit(float pitchInHz) {
        int audibleFreqBase = 4000 + 100; // number 1 will use 4000 hz;
        int audibleFreqStep = 200; // number 2 will use 4200 hz;

        if (pitchInHz < audibleFreqBase - 100)
        {
            return 0;
        }

        else if (pitchInHz < audibleFreqBase)
        {
            return 1;
        }
        else if (pitchInHz < audibleFreqBase + 1 * audibleFreqStep)
        {
            return 2;
        }
        else if (pitchInHz < audibleFreqBase + 2 * audibleFreqStep)
        {
            return 3;
        }
        else if (pitchInHz < audibleFreqBase + 3 * audibleFreqStep)
        {
            return 4;
        }
        else if (pitchInHz < audibleFreqBase + 4 * audibleFreqStep)
        {
            return 5;
        }
        else if (pitchInHz < audibleFreqBase + 5 * audibleFreqStep)
        {
            return 6;
        }
        else if (pitchInHz < audibleFreqBase + 6 * audibleFreqStep)
        {
            return 7;
        }
        else if (pitchInHz < audibleFreqBase + 7 * audibleFreqStep)
        {
            return 8;
        }
        else if (pitchInHz < audibleFreqBase + 8 * audibleFreqStep)
        {
            return 9;
        }

        return 0;
    }

    private int inaudibleSingleFreqToDigit(float pitchInHz) {
        int inaudibleFreqBase = 14950 + 100;
        int inaudibleFreqStep = 90;

        if (pitchInHz < inaudibleFreqBase - 100)
        {
            return 0;
        }

        else if (pitchInHz < inaudibleFreqBase)
        {
            return 1;
        }
        else if (pitchInHz < inaudibleFreqBase + 1 * inaudibleFreqStep)
        {
            return 2;
        }
        else if (pitchInHz < inaudibleFreqBase + 2 * inaudibleFreqStep)
        {
            return 3;
        }
        else if (pitchInHz < inaudibleFreqBase + 3 * inaudibleFreqStep)
        {
            return 4;
        }
        else if (pitchInHz < inaudibleFreqBase + 4 * inaudibleFreqStep)
        {
            return 5;
        }
        else if (pitchInHz < inaudibleFreqBase + 5 * inaudibleFreqStep)
        {
            return 6;
        }
        else if (pitchInHz < inaudibleFreqBase + 6 * inaudibleFreqStep)
        {
            return 7;
        }
        else if (pitchInHz < inaudibleFreqBase + 7 * inaudibleFreqStep)
        {
            return 8;
        }
        else if (pitchInHz < inaudibleFreqBase + 8 * inaudibleFreqStep)
        {
            return 9;
        }

        return 0;
    }


    @Override
    public void handleDetectedFrequencies(double timestamp, double[] frequencies, double[] powers, double[] allFrequencies, double[] allPowers) {
        //TextView text = (TextView) findViewById(R.id.textView_PitchHz);
        //text.setText("" );

        // Conversion of single frequencies to digits is in the charge of PitchDetectionHandler()
        if (0 == mDetectingMode || 1 == mDetectingMode)
        {
            return;
        }

        /*
        if (3 == mDetectingMode)
        {
            return; // Temporarily disuse Goertzel to detect inaudible DTMF
        }
        */


        List<Integer> L_twoMaxIndices = new ArrayList<>();
        findTwoMax(allPowers, L_twoMaxIndices);

        TextView mTextView_detectedDigit = (TextView) findViewById(R.id.textView_detectedDigit);

        //if (L_twoMaxIndices.get(0) >= 0 && L_twoMaxIndices.get(0) <= 3 && L_twoMaxIndices.get(1) >= 4 && L_twoMaxIndices.get(1) <= 7)
        if (L_twoMaxIndices.get(0) >= 0 && L_twoMaxIndices.get(0) <= 2 && L_twoMaxIndices.get(1) >= 4 && L_twoMaxIndices.get(1) <= 6)
        {
            int m_digit = DTMF_FreqArrayToDigit(L_twoMaxIndices.get(0), L_twoMaxIndices.get(1));
            mTextView_detectedDigit.setText( String.format("Detected: %d", m_digit));
        }
        else
        {
            //mTextView_detectedDigit.setText( "No digit detected" );
        }
    }

    private int DTMF_FreqArrayToDigit(int i, int j)
    {
        int m_freq_array_to_digit[][] = { {-1, -1, -1, -1, 1, 2, 3}, {-1, -1, -1, -1, 4, 5, 6}, {-1, -1, -1, -1, 7, 8, 9} };
        return m_freq_array_to_digit[i][j];
    }

    private void findTwoMax(double [] allPower, List<Integer> l_two_max_indices)
    {
        List<Double> m_List_0_3 = new ArrayList<>();
        List<Double> m_List_4_7 = new ArrayList<>();
        List<Double> m_List_1 = new ArrayList<>();
        for (int i = 0; i < allPower.length; i++)
        {
            if (i <= 3)
            {
                m_List_0_3.add(allPower[i]);
            }
            else
            {
                m_List_4_7.add(allPower[i]);
            }

            m_List_1.add(allPower[i]);
        }

        Collections.sort(m_List_0_3);
        Collections.sort(m_List_4_7);

        double max1 = m_List_0_3.get(m_List_0_3.size() - 1);
        double max2 = m_List_4_7.get(m_List_4_7.size() - 1);
        int index_1 = m_List_1.indexOf( max1 );
        int index_2 = m_List_1.indexOf( max2 );

        if (index_1 < index_2)
        {
            l_two_max_indices.add(index_1);
            l_two_max_indices.add(index_2);
        }
        else
        {
            l_two_max_indices.add(index_2);
            l_two_max_indices.add(index_1);
        }
    }


    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.radioButton_0:
                mDetectingMode = 0;
                mTextView_DetectingMode.setText("Mode 0: Detecting audible single frequency digits");
                break;
            case R.id.radioButton_1:
                mDetectingMode = 1;
                mTextView_DetectingMode.setText("Mode 1: Detecting inaudible single frequency digits");
                break;
            case R.id.radioButton_2:
                mDetectingMode = 2;
                mTextView_DetectingMode.setText("Mode 2: Detecting audible DTMF digits");
                break;
            case R.id.radioButton_3:
                mDetectingMode = 3;
                mTextView_DetectingMode.setText("Mode 3: Detecting inaudible DTMF digits");
                break;

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dispatcher.stop();
    }
}
