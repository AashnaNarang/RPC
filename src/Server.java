import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

/**
 * Server class that will receive requests and send a response based on the type of request
 * @author Aashna Narang
 *
 */
public class Server {
	private DatagramPacket sendPacket, receivePacket;
	private DatagramSocket receiveSocket;
	private static final byte[] WRITE = new byte[] { 0, 4, 0, 0};
	private static final byte[] READ = new byte[] { 0, 3, 0, 1};
	
	/**
	 * Public constructor to initialize instance variables
	 */
	public Server() {
		try {
			receiveSocket = new DatagramSocket(69);
		} catch (SocketException se) {
			se.printStackTrace();
			System.exit(1);
		}
	}

	/**
	 * Receive UDP packets, validate them and send a response based on the incoming message
	 * @throws IOException If an invalid message is sent, close the socket and throw an error
	 */
	public void receiveAndSend() throws IOException {
		receivePacket();
		sendPacket();
	}

	/**
	 * Receive a packet from intermediate host and store and print appropriate values
	 * @throws IOException If an invalid message is sent, close the socket and throw an error
	 */
	private void receivePacket() throws IOException {
		byte data[] = new byte[100];
		receivePacket = new DatagramPacket(data, data.length);
		System.out.println("Server: Waiting for Packet.\n");

		try {
			System.out.println("Waiting...");
			receiveSocket.receive(receivePacket);
		} catch (IOException e) {
			System.out.print("IO Exception: likely:");
			System.out.println("Receive Socket Timed Out.\n" + e);
			e.printStackTrace();
			System.exit(1);
		}

		System.out.println("Server: Packet received:");
		PrintHelpers.printReceivePacketInfo(receivePacket, data);
		
		byte[] response = validateAndCreateResponse(data);
		if (response != null) {
			System.out.println("Received valid data\n");
		} else {
			System.out.println("Received invalid data\n");
			receiveSocket.close();
			throw new IOException();
		}

		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
			System.exit(1);
		}

		sendPacket = new DatagramPacket(response, response.length, receivePacket.getAddress(), receivePacket.getPort());
	}
	
	/**
	 * Send a packet to intermediate host 
	 */
	private void sendPacket() {
		System.out.println("Server: Sending packet:");
		PrintHelpers.printSendPacketInfo(sendPacket);

		try {
			DatagramSocket sendSocket = new DatagramSocket();
			sendSocket.send(sendPacket);
			sendSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}

		System.out.println("Server: packet sent");
	}
	
	/**
	 * Validate the bytes/format of the incoming message. If valid, send the appropriate response otherwise return null
	 * @param data Incoming data from the packet
	 * @return The message to send back or null if incoming message was invalid
	 */
	private byte[] validateAndCreateResponse(byte data[]) {
		if (data[0] != (byte)0) {
			return null;
		}
		if ((data[1] != (byte)1) && (data[1] != (byte)2)) {
			return null;
		}
		if (data[2] < (byte)32) {
			return null;
		}
		int i;
		for(i = 2; i < data.length; i++) {
			if (data[i] < 32) {
				break;
			}
		}
		if ((data[i] != (byte)0) || (data[i+1] < (byte)32)) {
			return null;
		}
		for(i = i + 1; i < data.length; i++) {
			if (data[i] < 32) {
				break;
			}
		}
		for(; i < data.length; i++) {
			if (data[i] != (byte)0) {
				return null;
			}
		}
		return data[1] == 1 ? READ : WRITE;
	}
	
	/**
	 * Create a server object and continuously call the receive and send method. Exit if an error is thrown
	 * @param args An array of command-line arguments for the application
	 */
	public static void main(String args[]) {
		Server c = new Server();
		while(true) {
			try {
				c.receiveAndSend();
			} catch (IOException e) {
				System.out.println("Server closed.");
				System.exit(1);
			}
		}
	}
}
