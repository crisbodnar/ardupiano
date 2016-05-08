package hack.anvil.ardupiano.connections;

import android.util.Log;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class BluetoothThread implements Runnable {
    private final InputStream inputStream;
    private final BlockingQueue<Double> notesQueue;

    public BluetoothThread(InputStream input, BlockingQueue<Double> queue) {
        inputStream = input;
        notesQueue = queue;
    }

    @Override
    public void run() {
        byte[] bluetoothDataBuffer = new byte[1];

        DataInputStream dataInputStream = new DataInputStream(inputStream);

        Map<Integer, Double> bluetoothInputNoteMap = getNotesMap();

        while (true) {
            try {
                dataInputStream.readFully(bluetoothDataBuffer, 0, bluetoothDataBuffer.length);
                logDataToDebugConsole(bluetoothDataBuffer);
                Double currentNote = bluetoothInputNoteMap.get(bluetoothDataBuffer);
                notesQueue.put(currentNote);
            } catch (IOException e) {
                break;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void logDataToDebugConsole(byte[] dataBuffer) throws UnsupportedEncodingException {
        Log.d("BluetoothThread", "Received data: " + new String(dataBuffer, "UTF-8"));
    }

    private Map<Integer, Double> getNotesMap() {
        Map<Integer, Double> bluetoothInputNoteMap = new HashMap<>();

        bluetoothInputNoteMap.put(1, 1.0);
        bluetoothInputNoteMap.put(2, 1.0);
        bluetoothInputNoteMap.put(3, 1.0);
        bluetoothInputNoteMap.put(4, 1.0);
        bluetoothInputNoteMap.put(5, 1.0);
        bluetoothInputNoteMap.put(6, 1.0);
        bluetoothInputNoteMap.put(7, 1.0);

        return bluetoothInputNoteMap;
    }
}
