package hack.anvil.ardupiano.activities;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Set;
import java.util.UUID;

import hack.anvil.ardupiano.R;
import hack.anvil.ardupiano.connections.BluetoothThread;
import hack.anvil.ardupiano.exceptions.BluetoothNotFoundException;

public class HomeActivity extends AppCompatActivity {

    private OutputStream bluetoothOutputStream = null;
    private InputStream bluetoothInputStream = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        initializeBluetooth();

        new BluetoothThread(bluetoothInputStream, bluetoothOutputStream).run();
    }

    private void initializeBluetooth() {
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        try {
            checkForBluetoothPresence(bluetoothAdapter);
            enableBluetooth(bluetoothAdapter);
            checkForBoundDevices(bluetoothAdapter);
        } catch (BluetoothNotFoundException e) {
            displayToast(e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void checkForBluetoothPresence(BluetoothAdapter bluetoothAdapter)
            throws BluetoothNotFoundException {
        if (!isBluetoothAdapterPresent(bluetoothAdapter)) {
            throw new BluetoothNotFoundException("Bluetooth adapter has not been found");
        }
    }

    private void enableBluetooth(BluetoothAdapter bluetoothAdapter) {
        if (!bluetoothAdapter.isEnabled()) {
            askUserToTurnOnBluetooth();
        }
    }

    private void checkForBoundDevices(BluetoothAdapter bluetoothAdapter) throws IOException {
        Set<BluetoothDevice> bondedDevices = bluetoothAdapter.getBondedDevices();

        if (bondedDevices.isEmpty())
            displayToast("Please Pair the Device first");
        else
            bindDevice(bondedDevices);
    }

    private void bindDevice(Set<BluetoothDevice> bondedDevices) throws IOException {
        final String DEVICE_ADDRESS = "20:15:07:27:86:55";

        for (BluetoothDevice device : bondedDevices)
            if (device.getAddress().equals(DEVICE_ADDRESS))
                connectDeviceToSocket(device);
    }

    private void askUserToTurnOnBluetooth() {
        Intent enableAdapter = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        startActivityForResult(enableAdapter, 0);
    }

    private void connectDeviceToSocket(BluetoothDevice device) throws IOException {
        final UUID portUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

        BluetoothSocket socket = device.createRfcommSocketToServiceRecord(portUUID);
        socket.connect();

        bluetoothOutputStream = socket.getOutputStream();
        bluetoothInputStream = socket.getInputStream();
    }

    private boolean isBluetoothAdapterPresent(BluetoothAdapter bluetoothAdapter) {
        return bluetoothAdapter != null;
    }

    private void displayToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }
}
