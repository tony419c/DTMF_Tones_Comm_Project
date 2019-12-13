package com.bignerdranch.android.comp9336_project;

import android.databinding.DataBindingUtil;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.bignerdranch.android.comp9336_project.databinding.ActivityTask3DtmfSenderBinding;

public class Task3_DTMF_Sender_Activity extends AppCompatActivity
        implements View.OnClickListener
{
    ActivityTask3DtmfSenderBinding binding;

    //int audibleFreqBase = 4000; // number 1 will use 4000 hz;
    //int audibleFreqStep = 200; // number 2 will use 4200 hz;

    int inaudibleDTMFFreqBase1 = 11200;
    int inaudibleDTMFFreqStep1 = 400;

    int inaudibleDTMFFreqBase2 = 15000;
    int inaudibleDTMFFreqStep2 = 400;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_task2_sender);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_task3_dtmf_sender);

        binding.buttonAudibleDTMF1.setOnClickListener(this);
        binding.buttonAudibleDTMF2.setOnClickListener(this);
        binding.buttonAudibleDTMF3.setOnClickListener(this);
        binding.buttonAudibleDTMF4.setOnClickListener(this);
        binding.buttonAudibleDTMF5.setOnClickListener(this);
        binding.buttonAudibleDTMF6.setOnClickListener(this);
        binding.buttonAudibleDTMF7.setOnClickListener(this);
        binding.buttonAudibleDTMF8.setOnClickListener(this);
        binding.buttonAudibleDTMF9.setOnClickListener(this);

        binding.buttonInaudibleDTMF1.setOnClickListener(this);
        binding.buttonInaudibleDTMF2.setOnClickListener(this);
        binding.buttonInaudibleDTMF3.setOnClickListener(this);
        binding.buttonInaudibleDTMF4.setOnClickListener(this);
        binding.buttonInaudibleDTMF5.setOnClickListener(this);
        binding.buttonInaudibleDTMF6.setOnClickListener(this);
        binding.buttonInaudibleDTMF7.setOnClickListener(this);
        binding.buttonInaudibleDTMF8.setOnClickListener(this);
        binding.buttonInaudibleDTMF9.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        String m_str_Duration = binding.editTextAudibleDuration.getText().toString();
        float m_Duration;
        try
        {
            m_Duration = Float.parseFloat(m_str_Duration);
        }
        catch (RuntimeException e)
        {
            return;
        }

        int m_Duration_ms = (int) (m_Duration * 1000);
        ToneGenerator toneGenerator;

        switch (view.getId())
        {
            case R.id.button_AudibleDTMF_1:
                toneGenerator= new ToneGenerator(AudioManager.STREAM_DTMF,ToneGenerator.MAX_VOLUME);
                toneGenerator.startTone(ToneGenerator.TONE_DTMF_1, m_Duration_ms);
                break;
            case R.id.button_AudibleDTMF_2:
                toneGenerator= new ToneGenerator(AudioManager.STREAM_DTMF,ToneGenerator.MAX_VOLUME);
                toneGenerator.startTone(ToneGenerator.TONE_DTMF_2, m_Duration_ms);
                break;
            case R.id.button_AudibleDTMF_3:
                toneGenerator= new ToneGenerator(AudioManager.STREAM_DTMF,ToneGenerator.MAX_VOLUME);
                toneGenerator.startTone(ToneGenerator.TONE_DTMF_3, m_Duration_ms);
                break;
            case R.id.button_AudibleDTMF_4:
                toneGenerator= new ToneGenerator(AudioManager.STREAM_DTMF,ToneGenerator.MAX_VOLUME);
                toneGenerator.startTone(ToneGenerator.TONE_DTMF_4, m_Duration_ms);
                break;
            case R.id.button_AudibleDTMF_5:
                toneGenerator= new ToneGenerator(AudioManager.STREAM_DTMF,ToneGenerator.MAX_VOLUME);
                toneGenerator.startTone(ToneGenerator.TONE_DTMF_5, m_Duration_ms);
                break;
            case R.id.button_AudibleDTMF_6:
                toneGenerator= new ToneGenerator(AudioManager.STREAM_DTMF,ToneGenerator.MAX_VOLUME);
                toneGenerator.startTone(ToneGenerator.TONE_DTMF_6, m_Duration_ms);
                break;
            case R.id.button_AudibleDTMF_7:
                toneGenerator= new ToneGenerator(AudioManager.STREAM_DTMF,ToneGenerator.MAX_VOLUME);
                toneGenerator.startTone(ToneGenerator.TONE_DTMF_7, m_Duration_ms);
                break;
            case R.id.button_AudibleDTMF_8:
                toneGenerator= new ToneGenerator(AudioManager.STREAM_DTMF,ToneGenerator.MAX_VOLUME);
                toneGenerator.startTone(ToneGenerator.TONE_DTMF_8, m_Duration_ms);
                break;
            case R.id.button_AudibleDTMF_9:
                toneGenerator= new ToneGenerator(AudioManager.STREAM_DTMF,ToneGenerator.MAX_VOLUME);
                toneGenerator.startTone(ToneGenerator.TONE_DTMF_9, m_Duration_ms);
                break;

            case R.id.button_InaudibleDTMF_1:
                SpecFreqToneGenerator.generateDoubleFreqTone(inaudibleDTMFFreqBase1, inaudibleDTMFFreqBase2, m_Duration);
                break;
            case R.id.button_InaudibleDTMF_2:
                SpecFreqToneGenerator.generateDoubleFreqTone(inaudibleDTMFFreqBase1, inaudibleDTMFFreqBase2 + 1 * inaudibleDTMFFreqStep2, m_Duration);
                break;
            case R.id.button_InaudibleDTMF_3:
                SpecFreqToneGenerator.generateDoubleFreqTone(inaudibleDTMFFreqBase1, inaudibleDTMFFreqBase2 + 2 * inaudibleDTMFFreqStep2, m_Duration);
                break;
            case R.id.button_InaudibleDTMF_4:
                SpecFreqToneGenerator.generateDoubleFreqTone(inaudibleDTMFFreqBase1 + 1 * inaudibleDTMFFreqStep1, inaudibleDTMFFreqBase2, m_Duration);
                break;
            case R.id.button_InaudibleDTMF_5:
                SpecFreqToneGenerator.generateDoubleFreqTone(inaudibleDTMFFreqBase1 + 1 * inaudibleDTMFFreqStep1, inaudibleDTMFFreqBase2 + 1 * inaudibleDTMFFreqStep2, m_Duration);
                break;
            case R.id.button_InaudibleDTMF_6:
                SpecFreqToneGenerator.generateDoubleFreqTone(inaudibleDTMFFreqBase1 + 1 * inaudibleDTMFFreqStep1, inaudibleDTMFFreqBase2 + 2 * inaudibleDTMFFreqStep2, m_Duration);
                break;
            case R.id.button_InaudibleDTMF_7:
                SpecFreqToneGenerator.generateDoubleFreqTone(inaudibleDTMFFreqBase1 + 2 * inaudibleDTMFFreqStep1, inaudibleDTMFFreqBase2, m_Duration);
                break;
            case R.id.button_InaudibleDTMF_8:
                SpecFreqToneGenerator.generateDoubleFreqTone(inaudibleDTMFFreqBase1 + 2 * inaudibleDTMFFreqStep1, inaudibleDTMFFreqBase2 + 1 * inaudibleDTMFFreqStep2, m_Duration);
                break;
            case R.id.button_InaudibleDTMF_9:
                SpecFreqToneGenerator.generateDoubleFreqTone(inaudibleDTMFFreqBase1 + 2 * inaudibleDTMFFreqStep1, inaudibleDTMFFreqBase2 + 2 * inaudibleDTMFFreqStep2, m_Duration);
                break;
        }
    }
}
