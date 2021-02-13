# Datagrams

## Description
Practicing sending and receiving udp packets from various ports. Client sends a message to intermediate host, intermediate host forwards it to the server, server validates the message, server sends a response to intermediate host which is forwarded to the client. 

## Setup Instructions
1. Open Eclipse and click `File->Import`
2. Select `Import from Git (with smart import)`
3. Copy and Paste `https://github.com/AashnaNarang/Datagrams.git` into the Clone URI field
4. Select `Next`
5. Make sure the `main` branch is selected and click `Next`
6. Chose a directory to save the project into
7. Leave all of the default options. Hit `Next` and leave all the deafult options again
8. Click `Finish`
9. Open the `src` folder and open up SandwichProblem.java
10. Right click `Server.java` in the file editor and click `Run as -> Java Application`
11. Right click `IntermediateHost.java` in the file editor and click `Run as -> Java Application`
12. Right click `Client.java` in the file editor and click `Run as -> Java Application`

## Classes
### Client.java
Creates a packet that includes a message with a specified byte array. Byte array is intialized to follow the assignment instructions. Packet is sent to the intermediate host on port 23. The main method can choose how many times to repeat the request. The last request will send an invalid message, even request #s will send read requests, and odd numbers will send write requests. Program ends after 11 iterations or if there is a wait timeout for the receiver socket (client didn't response because error was thrown).

### IntermediateHost.java
Listens to port 23 for packets and sends them to port 69 (where server is listening). IntermediateHost waits for a response and forwards the response to the client (client's the port/address is stored to be able to send the message back). Program ends after there is a wait timeout for the receiver sockets.

### Server.java
Listens for packets coming in on port 69, grabs the message and validates the message in the fashion described in the assignment instructions. A specific message is sent if a read request came through, a different message for write requests, and an error is thrown when an invalid message comes through. Program ends once error is thrown.

### PrintHelpers.java
Static methods to reduce code duplication for print statements when receiving and sending packets.