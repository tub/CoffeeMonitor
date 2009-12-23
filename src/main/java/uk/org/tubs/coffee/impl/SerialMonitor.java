package uk.org.tubs.coffee.impl;

import gnu.io.CommPortIdentifier;
import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;
import gnu.io.UnsupportedCommOperationException;

import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.TooManyListenersException;

import uk.org.tubs.coffee.CoffeeListener;
import uk.org.tubs.coffee.CoffeeMonitor;
import uk.org.tubs.coffee.Constants;

import com.google.common.collect.Lists;

/**
 * gateway between serial port reading and coffee status events
 * 
 * @author Toby Cole
 */
public class SerialMonitor implements Runnable, SerialPortEventListener,
		CoffeeMonitor {

	static CommPortIdentifier portId;
	static Enumeration<CommPortIdentifier> portList;
	InputStream inputStream;
	SerialPort serialPort;
	Thread readThread;
	// Number of serial errors since last successful read.
	private int errs = 0;
	// Number of errors before we give up and throw an exception :)
	private int MAX_ERRS = 20;
	private List<CoffeeListener> coffeeListeners = Lists.newArrayList();

	@SuppressWarnings("unchecked")
	public SerialMonitor(String serialPortID) {
		try {
			portList = CommPortIdentifier.getPortIdentifiers();
			portId = CommPortIdentifier.getPortIdentifier(serialPortID);
			if (portId.getPortType() != CommPortIdentifier.PORT_SERIAL) {
				throw new RuntimeException(portId + " is not a Serial port");
			}
			serialPort = (SerialPort) portId.open("SimpleReadApp", 2000);
		} catch (PortInUseException e) {
			throw new RuntimeException(e);
		} catch (NoSuchPortException e) {
			throw new RuntimeException(e);
		}

		try {
			inputStream = serialPort.getInputStream();
		} catch (IOException e) {
		}

		try {
			serialPort.addEventListener(this);
		} catch (TooManyListenersException e) {
		}

		serialPort.notifyOnDataAvailable(true);

		try {
			serialPort.setSerialPortParams(9600, SerialPort.DATABITS_8,
					SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
		} catch (UnsupportedCommOperationException e) {
		}

		readThread = new Thread(this);

		readThread.start();
	}

	public void run() {
		try {
			Thread.sleep(20000);
		} catch (InterruptedException e) {
		}
	}

	public void serialEvent(SerialPortEvent event) {
		switch (event.getEventType()) {

		case SerialPortEvent.BI:

		case SerialPortEvent.OE:

		case SerialPortEvent.FE:

		case SerialPortEvent.PE:

		case SerialPortEvent.CD:

		case SerialPortEvent.CTS:

		case SerialPortEvent.DSR:

		case SerialPortEvent.RI:

		case SerialPortEvent.OUTPUT_BUFFER_EMPTY:
			break;

		case SerialPortEvent.DATA_AVAILABLE:
			byte[] readBuffer = new byte[20];

			try {
				while (inputStream.available() > 0) {
					inputStream.read(readBuffer);
				}

				updateCoffeeListeners(new String(readBuffer));
				errs = 0;
			} catch (IOException e) {
				if (errs >= MAX_ERRS) {
					throw new RuntimeException(e);
				}
				System.err.println("IOException reading serial port.");
				errs++;
			}

			break;
		}
	}

	private void updateCoffeeListeners(String string) {
		int serialVal = Integer.parseInt(string.trim());
		boolean brewing = (serialVal & Constants.BREWING_MASK) == Constants.BREWING_MASK;
		boolean hotplateOn = (serialVal & Constants.HOTPLATE_MASK) == Constants.HOTPLATE_MASK;

		CoffeeStatus status = new CoffeeStatus(brewing, hotplateOn);
		for (Iterator<CoffeeListener> iterator = coffeeListeners.iterator(); iterator
				.hasNext();) {
			CoffeeListener l = iterator.next();
			l.coffeStatusChanged(status);
		}
	}

	public void addCoffeeListener(CoffeeListener l) {
		coffeeListeners.add(l);
	}

	public boolean removeCoffeeListener(CoffeeListener l) {
		return coffeeListeners.remove(l);
	}

}
