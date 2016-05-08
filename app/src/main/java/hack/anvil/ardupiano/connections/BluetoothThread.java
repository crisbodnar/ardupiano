package hack.anvil.ardupiano.connections;

import android.util.Log;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
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

        Map<String, Double> bluetoothInputNoteMap = getNotesMap();

        while (true) {
            try {
                dataInputStream.readFully(bluetoothDataBuffer, 0, bluetoothDataBuffer.length);
                logDataToDebugConsole(bluetoothDataBuffer);
                Double currentNote = bluetoothInputNoteMap.get(new String(bluetoothDataBuffer, "UTF-8"));
                if(notesQueue.remainingCapacity() != 0)
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

    private Map<String, Double> getNotesMap() {
        Map<String, Double> bluetoothInputNoteMap = new HashMap<>();

        bluetoothInputNoteMap.put("1", 220.0);
        bluetoothInputNoteMap.put("2", 237.0);
        bluetoothInputNoteMap.put("3", 262.0);
        bluetoothInputNoteMap.put("4", 294.0);
        bluetoothInputNoteMap.put("5", 330.0);
        bluetoothInputNoteMap.put("6", 349.0);
        bluetoothInputNoteMap.put("7", 392.0);

        return bluetoothInputNoteMap;
    }
}
