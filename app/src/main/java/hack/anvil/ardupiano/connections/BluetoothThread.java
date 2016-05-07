package hack.anvil.ardupiano.connections;

import android.util.Log;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class BluetoothThread extends Thread {
    private final InputStream mmInStream;
    private final OutputStream mmOutStream;

    public BluetoothThread(InputStream inp, OutputStream oup) {
        mmInStream = inp;
        mmOutStream = oup;
    }

    public void run() {
        byte[] buffer = new byte[1];  // buffer store for the stream
        int bytes; // bytes returned from read()

        DataInputStream dinput = new DataInputStream(mmInStream);

        // Keep listening to the InputStream until an exception occurs
        while (true) {
            try {
                // Read from the InputStream
                dinput.readFully(buffer, 0, buffer.length);
                // Send the obtained bytes to the UI activity
                    Log.d("BLUETOOTH", new String(buffer, "UTF-8"));
            } catch (IOException e) {
                break;
            }
        }
    }

    /* Call this from the main activity to send data to the remote device */
    public void write(byte[] bytes) {
        try {
            mmOutStream.write(bytes);
        } catch (IOException e) {
        }
    }
}
