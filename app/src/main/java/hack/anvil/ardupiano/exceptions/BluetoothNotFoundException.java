package hack.anvil.ardupiano.exceptions;

public class BluetoothNotFoundException extends Exception{
    public BluetoothNotFoundException() {
        super();
    }

    public BluetoothNotFoundException(String detailMessage) {
        super(detailMessage);
    }

    public BluetoothNotFoundException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public BluetoothNotFoundException(Throwable throwable) {
        super(throwable);
    }
}
