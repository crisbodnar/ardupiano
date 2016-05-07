package hack.anvil.ardupiano.connections;

import android.util.Log;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

public class BluetoothThread extends Thread {
    private final InputStream inputStream;

    public BluetoothThread(InputStream input) {
        inputStream = input;
    }

    @Override
    public void run() {
        byte[] bluetoothDataBuffer = new byte[1];

        DataInputStream dataInputStream = new DataInputStream(inputStream);

        while (true) {
            try {
                dataInputStream.readFully(bluetoothDataBuffer, 0, bluetoothDataBuffer.length);
                logDataToDebugConsole(bluetoothDataBuffer);
            } catch (IOException e) {
                break;
            }
        }
    }

    private void logDataToDebugConsole(byte[] dataBuffer) throws UnsupportedEncodingException {
        Log.d("BluetoothThread", "Received data: " + new String(dataBuffer, "UTF-8"));
    }
}
