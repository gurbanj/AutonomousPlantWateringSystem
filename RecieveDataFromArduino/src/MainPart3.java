import com.fazecast.jSerialComm.SerialPort;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Timer;

public class MainPart3 {
    public static final byte TIMER_DURATION = 10;

    public static void main(String[] args) {
        var sp = SerialPort.getCommPort("/dev/cu.usbserial-0001");   // Students: update this string!

        sp.setComPortParameters(9600, Byte.SIZE, SerialPort.ONE_STOP_BIT, SerialPort.NO_PARITY);
        sp.setComPortTimeouts(SerialPort.TIMEOUT_WRITE_BLOCKING, 0, 0);

        var hasOpened = sp.openPort();
        if (!hasOpened) {
            throw new IllegalStateException("Failed to open port.");
        }

        var outputStream = sp.getOutputStream();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            sp.closePort();
        }));

        var timer = new Timer();
        var countdown = new CountdownHandlerPart3(TIMER_DURATION, outputStream);

        sp.addDataListener(countdown);

        System.out.println("listen: " + countdown.getListeningEvents());
        timer.schedule(countdown, 0, 1000);
    }
}
