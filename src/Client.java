import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;

/**
 * This class serves as the client. It sends UDP packets with different requests and receives responses.
 * @author Aashna Narang
 *
 */
public class Client {
	private DatagramPacket sendPacket, receivePacket;
	private DatagramSocket sendReceiveSocket;
	private int maxRequests;
	private int requestNumber;

	/**
	 * Public constructor to initialize the instance variables
	 * @param maxRequests The maximum number of requests the client can make before closing its socket
	 */
	public Client(int maxRequests) {
		try {
			this.maxRequests = maxRequests;
			requestNumber = 0;
			sendReceiveSocket = new DatagramSocket();
			sendReceiveSocket.setSoTimeout(10000);
		} catch (SocketException se) {
			se.printStackTrace();
			System.exit(1);
		}
	}

	/**
	 * Send a packet to intermediate host and wait to receive a response
	 */
	public void sendAndReceive() {
		requestNumber++;
		byte msg[] = createMsg();

		try {
			sendPacket = new DatagramPacket(msg, msg.length, InetAddress.getLocalHost(), 23);
		} catch (UnknownHostException e) {
			e.printStackTrace();
			System.exit(1);
		}

		System.out.println("Client: Sending packet:");
		System.out.println("Request Number: " + requestNumber);
		PrintHelpers.printSendPacketInfo(sendPacket);

		try {
			sendReceiveSocket.send(sendPacket);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
		System.out.println("Client: Packet sent.\n");

		// Construct a DatagramPacket for receiving packets up
		// to 100 bytes long (the length of the byte array).

		byte data[] = new byte[100];
		receivePacket = new DatagramPacket(data, data.length);

		try {
			sendReceiveSocket.receive(receivePacket);
		} catch (SocketTimeoutException e1) {
			sendReceiveSocket.close();
			System.exit(1);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}

		System.out.println("Client: Packet received:");
		PrintHelpers.printReceivePacketInfo(receivePacket, data);

		if (requestNumber == maxRequests) {
			System.out.println("Client closing.");
			sendReceiveSocket.close();
		}
	}

	/**
	 * Create the data message for a packet depending on the current request number
	 * @return The byte array to be set as the data of the packet
	 */
	private byte[] createMsg() {
		if (requestNumber == maxRequests) {
			byte msg[] = new byte[20];
			for (int i = 0; i < msg.length; i++) {
				msg[i] = (byte) 0;
			}
			return msg;
		}
		String s = "test.txt";
		String mode = "ocTEt";
		ArrayList<Byte> bytes = new ArrayList<>();
		bytes.add((byte) 0);
		if (requestNumber % 2 == 0) {
			bytes.add((byte) 1);
		} else {
			bytes.add((byte) 2);
		}
		for (byte b : s.getBytes()) {
			bytes.add(b);
		}
		bytes.add((byte) 0);
		for (byte b : mode.getBytes()) {
			bytes.add(b);
		}
		bytes.add((byte) 0);

		byte msg[] = new byte[bytes.size()];
		for (int i = 0; i < bytes.size(); i++) {
			msg[i] = bytes.get(i);
		}
		return msg;
	}

	/**
	 * Initialize the client and call sendAndReceive 11 times
	 * @param args An array of command-line arguments for the application
	 */
	public static void main(String args[]) {
		Client c = new Client(11);
		for (int i = 0; i < 11; i++) {
			c.sendAndReceive();
		}

	}
}
