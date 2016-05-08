package hack.anvil.ardupiano.sounds;

import android.app.Activity;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.media.audiofx.Visualizer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import java.util.Arrays;
import java.util.concurrent.BlockingQueue;

import hack.anvil.ardupiano.R;
import hack.anvil.ardupiano.views.VisualizerView;

public class PlaySound implements Runnable {
    // originally from http://marblemice.blogspot.com/2010/04/generate-and-play-tone-in-android.html
    // and modified by Steve Pomeroy <steve@staticfree.info>
    private final int duration = 1; // seconds
    private final int sampleRate = 4000;
    private final int numSamples = duration * sampleRate;
    private final double sample[] = new double[numSamples];
    private double freqOfTone; // hz

    private final byte generatedSnd[] = new byte[2 * numSamples];

    public final BlockingQueue<Double> queue;

    public VisualizerView visualizerView;
    public Activity homeActivity;

    public PlaySound(BlockingQueue<Double> queue, Activity activity, VisualizerView vview) {
        this.queue = queue;
        homeActivity = activity;
        visualizerView = vview;
    }

    @Override
    public void run() {
        while (true) {
            if (!queue.isEmpty()) {
                freqOfTone = queue.remove();
                Log.d("PlaySound", "Deque value: " + freqOfTone);
            } else
                continue;

            genTone();
            playSound();
        }
    }

    private void genTone() {
        // fill out the array
        for (int i = 0; i < numSamples; ++i) {
            sample[i] = Math.sin(2 * Math.PI * i / (sampleRate / freqOfTone));
        }

        // convert to 16 bit pcm sound array
        // assumes the sample buffer is normalised.
        int idx = 0;
        for (final double dVal : sample) {
            // scale to maximum amplitude
            final short val = (short) ((dVal * 32767));
            // in 16 bit wav PCM, first byte is the low order byte
            generatedSnd[idx++] = (byte) (val & 0x00ff);
            generatedSnd[idx++] = (byte) ((val & 0xff00) >>> 8);

        }
    }

    public synchronized void playSound() {

        final AudioTrack audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC,
                sampleRate, AudioFormat.CHANNEL_OUT_SURROUND,
                AudioFormat.ENCODING_PCM_16BIT, generatedSnd.length,
                AudioTrack.MODE_STATIC);
        audioTrack.write(generatedSnd, 0, generatedSnd.length);
        if(audioTrack.getState() != AudioTrack.STATE_UNINITIALIZED) {
            homeActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    visualizerView.updateVisualizer(Arrays.copyOfRange(generatedSnd, 0, generatedSnd.length/16));
                }
            });
            audioTrack.play();
            try {
                this.wait(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            audioTrack.release();
        }
    }
}
