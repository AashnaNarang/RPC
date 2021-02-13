import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.net.SocketTimeoutException;

/**
 * 
 * @author Aashna Narang
 *
 */
public class IntermediateHost {
	// use separate packet objects from client and server to be able to access address/port of client later on
	private DatagramPacket sendPacket, receivePacket, sendPacket2, receivePacket2;
	private DatagramSocket receiveSocket2, receiveSocket;

	public IntermediateHost() {
		try {
			receiveSocket = new DatagramSocket(23);
			receiveSocket2 = new DatagramSocket();

			receiveSocket.setSoTimeout(5000);
			receiveSocket2.setSoTimeout(5000);
		} catch (SocketException se) {
			se.printStackTrace();
			System.exit(1);
		}
	}

	/**
	 * Receive a packet form client and pass it on to server
	 */
	public void receiveAndSendToServer() {
		receiveFromClient();
		sendToServer();
	}
	
	/**
	 * Receive a packet from client and send to server
	 */
	public void receiveAndSendToClient() {
		receiveFromServer();
		sendToClient();
	}
	
	/**
	 * Wait to receive a packet from client and print and store information as needed
	 */
	private void receiveFromClient() {
		byte data[] = new byte[100];
		receivePacket = new DatagramPacket(data, data.length);
		System.out.println("IntermediateHost: Waiting for Packet.\n");

		try {
			System.out.println("Waiting...");
			receiveSocket.receive(receivePacket);
		} catch (SocketTimeoutException e1) {
			receiveSocket.close();
			System.exit(1);
		} catch (IOException e) {
			System.out.print("IO Exception: likely:");
			System.out.println("Receive Socket Timed Out.\n" + e);
			e.printStackTrace();
			System.exit(1);
		}

		System.out.println("IntermediateHost: Packet received:");
		PrintHelpers.printReceivePacketInfo(receivePacket, data);

		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
			System.exit(1);
		}
		sendPacket = new DatagramPacket(data, receivePacket.getLength(), receivePacket.getAddress(), 69);
	}
	
	/**
	 * Send a packet to the server, print appropriate messages
	 */
	private void sendToServer() {
		System.out.println("IntermediateHost: Sending packet:");
		PrintHelpers.printSendPacketInfo(sendPacket);

		try {
			receiveSocket2.send(sendPacket);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}

		System.out.println("IntermediateHost: packet sent\n");
	}
	
	/**
	 * Receive packets from server and store and print approriate values
	 */
	private void receiveFromServer() {
		byte data[] = new byte[100];
		receivePacket2 = new DatagramPacket(data, data.length);
		System.out.println("IntermediateHost: Waiting for Packet.\n");

		try {
			System.out.println("Waiting...");
			receiveSocket2.receive(receivePacket2);
		} catch (SocketTimeoutException e1) {
			receiveSocket2.close();
			System.exit(1);
		} catch (IOException e) {
			System.out.print("IO Exception: likely:");
			System.out.println("Receive Socket Timed Out.\n" + e);
			e.printStackTrace();
			System.exit(1);
		}

		System.out.println("IntermediateHost: Packet received:");
		PrintHelpers.printReceivePacketInfo(receivePacket, data);

		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
			System.exit(1);
		}
		sendPacket2 = new DatagramPacket(data, receivePacket2.getLength(), receivePacket.getAddress(),
				receivePacket.getPort());
	}
	
	/**
	 * Send a packet to the client
	 */
	private void sendToClient() {
		System.out.println("IntermediateHost: Sending packet:");
		PrintHelpers.printSendPacketInfo(sendPacket2);

		try {
			DatagramSocket sendSocket = new DatagramSocket();
			sendSocket.send(sendPacket2);
			sendSocket.close();
		} catch (SocketException se) {
			se.printStackTrace();
			System.exit(1);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}

		System.out.println("IntermediateHost: packet sent\n");
	}

	/**
	 * Create an intermediate host object and continuously call the receive and send methods.
	 * @param args An array of command-line arguments for the application
	 */
	public static void main(String args[]) {
		IntermediateHost c = new IntermediateHost();
		while (true) {
			c.receiveAndSendToServer();
			c.receiveAndSendToClient();
		}

	}
}
