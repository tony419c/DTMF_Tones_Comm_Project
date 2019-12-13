package com.bignerdranch.android.comp9336_project;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;

public class SpecFreqToneGenerator {

    //private AudioTrack generateSingleFreqTone(double freqHz, int durationMs)

    public static void generateSingleFreqTone_1(double freqHz, /* int duration */ float duration_decimal )
    {
        int count = (int)(44100.0 * 2.0 * (duration_decimal / 1000.0)) & ~1;
        short[] samples = new short[count];
        for(int i = 0; i < count; i += 2){
            short sample = (short)(Math.sin(2 * Math.PI * i / (44100.0 / freqHz)) * 0x7FFF);
            samples[i + 0] = sample;
            samples[i + 1] = sample;
        }
        AudioTrack track = new AudioTrack(AudioManager.STREAM_MUSIC, 44100,
                AudioFormat.CHANNEL_OUT_STEREO, AudioFormat.ENCODING_PCM_16BIT,
                count * (Short.SIZE / 8), AudioTrack.MODE_STATIC);
        track.write(samples, 0, count);

        //track.write(mBuffer, 0, mSound.length);


        //track.setStereoVolume(AudioTrack.getMaxVolume(), AudioTrack.getMaxVolume());
        track.play();

        //track.write(samples, 0, count);
        //track.stop();
        //track.release();
    }

    public static void generateSingleFreqTone(double frequency, /* int duration */ float duration_decimal )
    {
        /*
        int count = (int)(44100.0 * 2.0 * (durationMs / 1000.0)) & ~1;
        short[] samples = new short[count];
        for(int i = 0; i < count; i += 2){
            short sample = (short)(Math.sin(2 * Math.PI * i / (44100.0 / freqHz)) * 0x7FFF);
            samples[i + 0] = sample;
            samples[i + 1] = sample;
        }
        AudioTrack track = new AudioTrack(AudioManager.STREAM_MUSIC, 44100,
                AudioFormat.CHANNEL_OUT_STEREO, AudioFormat.ENCODING_PCM_16BIT,
                count * (Short.SIZE / 8), AudioTrack.MODE_STATIC);
        track.write(samples, 0, count);
        return track;
        */

        int duration = (int) (duration_decimal * 44100) ;
        //duration *= 44100;

        int mBufferSize = AudioTrack.getMinBufferSize(44100,
                AudioFormat.CHANNEL_OUT_MONO,
                AudioFormat.ENCODING_PCM_8BIT);

        AudioTrack mAudioTrack = new AudioTrack(AudioManager.STREAM_MUSIC, 44100,
                AudioFormat.CHANNEL_OUT_MONO, AudioFormat.ENCODING_PCM_16BIT,
                mBufferSize, AudioTrack.MODE_STREAM);

        // Sine wave
        double[] mSound = new double[duration];
        short[] mBuffer = new short[duration];
        for (int i = 0; i < mSound.length; i++) {
            mSound[i] = Math.sin((2.0*Math.PI * i/(44100/frequency)));
            mBuffer[i] = (short) (mSound[i]*Short.MAX_VALUE);
        }

        mAudioTrack.write(mBuffer, 0, mSound.length);


        mAudioTrack.setStereoVolume(AudioTrack.getMaxVolume(), AudioTrack.getMaxVolume());
        mAudioTrack.play();

        mAudioTrack.write(mBuffer, 0, mSound.length);
        mAudioTrack.stop();
        mAudioTrack.release();
    }


    public static void generateDoubleFreqTone(double freq1, double freq2, float duration_decimal)
    {
        int duration = (int) (duration_decimal * 44100) ;

        int mBufferSize = AudioTrack.getMinBufferSize(44100,
                AudioFormat.CHANNEL_OUT_MONO,
                AudioFormat.ENCODING_PCM_8BIT);

        AudioTrack mAudioTrack = new AudioTrack(AudioManager.STREAM_MUSIC, 44100,
                AudioFormat.CHANNEL_OUT_MONO, AudioFormat.ENCODING_PCM_16BIT,
                mBufferSize, AudioTrack.MODE_STREAM);

        // Sine wave
        double[] mSound = new double[duration];
        short[] mBuffer = new short[duration];
        for (int i = 0; i < mSound.length; i++) {
            //mSound[i] = (  Math.sin( (2.0 * Math.PI * i/(44100/freq1))  )  + Math.sin( (2.0 * Math.PI * i/(44100/freq2)) )    )  / 2.0;
            mSound[i] =   0.5 * Math.sin( (2.0 * Math.PI * i/(44100/freq1))  )  + 0.5 * Math.sin( (2.0 * Math.PI * i/(44100/freq2)) );
            mBuffer[i] = (short) (mSound[i]*Short.MAX_VALUE);
        }

        mAudioTrack.write(mBuffer, 0, mSound.length);


        mAudioTrack.setStereoVolume(AudioTrack.getMaxVolume(), AudioTrack.getMaxVolume());
        mAudioTrack.play();

        mAudioTrack.write(mBuffer, 0, mSound.length);
        mAudioTrack.stop();
        mAudioTrack.release();




    }

}
