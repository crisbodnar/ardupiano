package hack.anvil.ardupiano.activities;

import android.media.AudioManager;
import android.media.audiofx.Visualizer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import hack.anvil.ardupiano.R;
import hack.anvil.ardupiano.views.VisualizerView;

/*
 * Copy test.mp3 to /res/raw/ folder
 *
 * needed in AndroidManifest.xml
 * android:minSdkVersion="9"
 * uses-permission of "android.permission.RECORD_AUDIO"
 *
 * reference: Android demo example -
 * ApiDemos > Media > AudioTx
 */

public class MainActivity extends AppCompatActivity {

    VisualizerView mVisualizerView;

    private Visualizer mVisualizer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mVisualizerView = (VisualizerView) findViewById(R.id.myvisualizerview);

        initAudio();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (isFinishing()) {
            mVisualizer.release();
        }
    }

    private void initAudio() {
        setVolumeControlStream(AudioManager.STREAM_MUSIC);

        setupVisualizerFxAndUI();
        // Make sure the visualizer is enabled only when you actually want to
        // receive data, and
        // when it makes sense to receive data.
        mVisualizer.setEnabled(true);
        // When the stream ends, we don't need to collect any more data. We
        // don't do this in
        // setupVisualizerFxAndUI because we likely want to have more,
        // non-Visualizer related code
        // in this callback.

    }

    private void setupVisualizerFxAndUI() {

        // Create the Visualizer object and attach it to our media player.
        mVisualizer = new Visualizer(0);
        mVisualizer.setCaptureSize(Visualizer.getCaptureSizeRange()[1]);
        mVisualizer.setDataCaptureListener(
                new Visualizer.OnDataCaptureListener() {
                    public void onWaveFormDataCapture(Visualizer visualizer,
                                                      byte[] bytes, int samplingRate) {
                        mVisualizerView.updateVisualizer(bytes);
                    }

                    public void onFftDataCapture(Visualizer visualizer,
                                                 byte[] bytes, int samplingRate) {
                    }
                }, Visualizer.getMaxCaptureRate() / 2, true, false);
    }

}
