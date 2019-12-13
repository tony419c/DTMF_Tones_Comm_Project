package com.bignerdranch.android.comp9336_project;

import android.Manifest;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import be.tarsos.dsp.AudioDispatcher;
import be.tarsos.dsp.AudioEvent;
import be.tarsos.dsp.io.android.AudioDispatcherFactory;
import be.tarsos.dsp.pitch.Goertzel;
import be.tarsos.dsp.pitch.PitchDetectionHandler;
import be.tarsos.dsp.pitch.PitchDetectionResult;
import be.tarsos.dsp.pitch.PitchProcessor;

public class Task1_Receiver_Activity extends AppCompatActivity {

    TextView mTextView_Freq;
    AudioDispatcher dispatcher;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task1_receiver);

        mTextView_Freq = (TextView) findViewById(R.id.textView_Task1_Receiver_Freq);

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, 0);

        dispatcher =
                AudioDispatcherFactory.fromDefaultMicrophone(44100,8192,0);


        // buffer size originally: 1024
        dispatcher.addAudioProcessor(new PitchProcessor(PitchProcessor.PitchEstimationAlgorithm.FFT_YIN, 44100, 8192, new PitchDetectionHandler()
        {
            @Override
            public void handlePitch(PitchDetectionResult pitchDetectionResult,
                                    final AudioEvent audioEvent) {

                final float pitchInHz = pitchDetectionResult.getPitch();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mTextView_Freq.setText("" + pitchInHz);
                    }
                });
            }
        }));

        new Thread(dispatcher,"Audio Dispatcher").start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dispatcher.stop();
    }
}
