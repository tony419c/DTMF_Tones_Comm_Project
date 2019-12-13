package com.bignerdranch.android.comp9336_project;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.bignerdranch.android.comp9336_project.databinding.ActivityTask2SenderBinding;

public class Task2_Sender_Activity extends AppCompatActivity
    implements View.OnClickListener
{

    ActivityTask2SenderBinding binding;

    int audibleFreqBase = 4000; // number 1 will use 4000 hz;
    int audibleFreqStep = 200; // number 2 will use 4200 hz;

    int inaudibleFreqBase = 14500;
    int inaudibleFreqStep = 200;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_task2_sender);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_task2_sender);

        binding.buttonAudible1.setOnClickListener(this);
        binding.buttonAudible2.setOnClickListener(this);
        binding.buttonAudible3.setOnClickListener(this);
        binding.buttonAudible4.setOnClickListener(this);
        binding.buttonAudible5.setOnClickListener(this);
        binding.buttonAudible6.setOnClickListener(this);
        binding.buttonAudible7.setOnClickListener(this);
        binding.buttonAudible8.setOnClickListener(this);
        binding.buttonAudible9.setOnClickListener(this);

        binding.buttonInaudible1.setOnClickListener(this);
        binding.buttonInaudible2.setOnClickListener(this);
        binding.buttonInaudible3.setOnClickListener(this);
        binding.buttonInaudible4.setOnClickListener(this);
        binding.buttonInaudible5.setOnClickListener(this);
        binding.buttonInaudible6.setOnClickListener(this);
        binding.buttonInaudible7.setOnClickListener(this);
        binding.buttonInaudible8.setOnClickListener(this);
        binding.buttonInaudible9.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        String m_str_Duration = binding.editTextAudibleDuration.getText().toString();
        //int m_Duration;
        float m_Duration;
        try
        {
            //m_Duration = Integer.parseInt(m_str_Duration);
            m_Duration = Float.parseFloat(m_str_Duration);
        }
        catch (RuntimeException e)
        {
            return;
        }

        switch (view.getId())
        {
            case R.id.button_Audible_1:
                SpecFreqToneGenerator.generateSingleFreqTone(audibleFreqBase, m_Duration);
                break;
            case R.id.button_Audible_2:
                SpecFreqToneGenerator.generateSingleFreqTone(audibleFreqBase + 1 * audibleFreqStep, m_Duration);
                break;
            case R.id.button_Audible_3:
                SpecFreqToneGenerator.generateSingleFreqTone(audibleFreqBase + 2 * audibleFreqStep, m_Duration);
                break;
            case R.id.button_Audible_4:
                SpecFreqToneGenerator.generateSingleFreqTone(audibleFreqBase + 3 * audibleFreqStep, m_Duration);
                break;
            case R.id.button_Audible_5:
                SpecFreqToneGenerator.generateSingleFreqTone(audibleFreqBase + 4 * audibleFreqStep, m_Duration);
                break;
            case R.id.button_Audible_6:
                SpecFreqToneGenerator.generateSingleFreqTone(audibleFreqBase + 5 * audibleFreqStep, m_Duration);
                break;
            case R.id.button_Audible_7:
                SpecFreqToneGenerator.generateSingleFreqTone(audibleFreqBase + 6 * audibleFreqStep, m_Duration);
                break;
            case R.id.button_Audible_8:
                SpecFreqToneGenerator.generateSingleFreqTone(audibleFreqBase + 7 * audibleFreqStep, m_Duration);
                break;
            case R.id.button_Audible_9:
                SpecFreqToneGenerator.generateSingleFreqTone(audibleFreqBase + 8 * audibleFreqStep, m_Duration);
                break;

            case R.id.button_Inaudible_1:
                SpecFreqToneGenerator.generateSingleFreqTone(inaudibleFreqBase, m_Duration);
                break;
            case R.id.button_Inaudible_2:
                SpecFreqToneGenerator.generateSingleFreqTone(inaudibleFreqBase + 1 * inaudibleFreqStep, m_Duration);
                break;
            case R.id.button_Inaudible_3:
                SpecFreqToneGenerator.generateSingleFreqTone(inaudibleFreqBase + 2 * inaudibleFreqStep, m_Duration);
                break;
            case R.id.button_Inaudible_4:
                SpecFreqToneGenerator.generateSingleFreqTone(inaudibleFreqBase + 3 * inaudibleFreqStep, m_Duration);
                break;
            case R.id.button_Inaudible_5:
                SpecFreqToneGenerator.generateSingleFreqTone(inaudibleFreqBase + 4 * inaudibleFreqStep, m_Duration);
                break;
            case R.id.button_Inaudible_6:
                SpecFreqToneGenerator.generateSingleFreqTone(inaudibleFreqBase + 5 * inaudibleFreqStep, m_Duration);
                break;
            case R.id.button_Inaudible_7:
                SpecFreqToneGenerator.generateSingleFreqTone(inaudibleFreqBase + 6 * inaudibleFreqStep, m_Duration);
                break;
            case R.id.button_Inaudible_8:
                SpecFreqToneGenerator.generateSingleFreqTone(inaudibleFreqBase + 7 * inaudibleFreqStep, m_Duration);
                break;
            case R.id.button_Inaudible_9:
                SpecFreqToneGenerator.generateSingleFreqTone(inaudibleFreqBase + 8 * inaudibleFreqStep, m_Duration);
                //SpecFreqToneGenerator.generateDoubleFreqTone(697, 1336, m_Duration);
                break;
        }
    }
}
