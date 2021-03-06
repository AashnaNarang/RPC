# RPC

## Overview
Use UDP packets to allow a client to perform a remote procedure call. The client sends data to the intermediate host and the server sends a request for data to the intermediate host. The intermediate host sends an acknowledgement to the client and sends the client data to the server. 
The server sends data to the intermediate host and the client sends a request for data to the intermediate host. The host sends an acknowledgement to the server and sends the server's response to the client. The client receives the result of the remote procedure call. 
Note: There are two intermediate host threads creates to run the RPC. One to communicate with the client and one to communicate with the server. The host threads use a box class store shared data

## Setup Instructions
1. Open Eclipse and click `File->Import`
2. Select `Import from Git (with smart import)`
3. Copy and Paste `https://github.com/AashnaNarang/RPC.git` into the Clone URI field
4. Select `Next`
5. Make sure the `main` branch is selected and click `Next`
6. Chose a directory to save the project into
7. Leave all of the default options. Hit `Next` and leave all the deafult options again
8. Click `Finish`
9. Open the `src` folder and open up Main.java class and click `Run as -> Java Application`

Note: Because threads are used in this implementation, sometimes the print statements can print out of order of what is actually happening. Although, you can see that it worked by looking for the following print statements
- "Client request number: 11" --> If the last print statement like this has the number 11, then it becomes obvious that the requests were repeated 11 times
- "Received invalid data" --> Server received invaid data and shortly after there should be a print statement that says the "Server closed."
- Rest of the print statements should show you that the client/server/hosts are sending and receiving the correct data

## Classes
### Client.java
Creates a packet that includes a message with a specified byte array. Byte array is intialized to follow the assignment instructions. Packet is sent to the intermediate host on port 23. The main method can choose how many times to repeat the request. The last request will send an invalid message, even request #s will send read requests, and odd numbers will send write requests. Program ends after 11 iterations or if there is a wait timeout for the receiver socket (client didn't response because error was thrown).
The client receives an acknowledgement response and sends another packet to request for the RPC response. The client receives the RPC response and repeats.


### IntermediateHost.java
Listens to a specific port. IntermediateHost waits for a packet and sees if it includes data or a request for data. Stores data in the box or gets data from the box and sends either the data or a request acknowledgment.
- As mentioned earlier, an intermediate host thread is used for port 23 and port 69, one for client and one for the server. This is to reduce the amount of duplicate code and make sure each thread has a single responsibility. 

### Server.java
Sends a packet with a message to request for data to the IntermediateHost and is returned data. Server analyzes the data to see if a read request, a write request or if an invalid request came through. The server sends a packet to the Intermediate Host with the response and waits for an acknowledgement. If an invalid request was given, then and error is thrown and the program is ended.

### PrintHelpers.java
Static methods to reduce code duplication for print statements when receiving and sending packets.

### Main.java
Class used to test the RPC behaviour. Creates the necessary threads and starts them. Descriptive  statements are printed in the console to view behaviour.

### Box.java
Class used by 2 intermediate host threads to put/get data coming in/sending to to the client/server.


## Answers to question
1. It is a good idea to use more than one thread for the Intermediate host because if there was only 1, then one class would have a lot of duplicate code and be responsible for too many things. It will have to communicate with 2 different ports and you can't guarantee the client and server will send things in the order you want, so the intermediate host could get jumbled up if it was just one thread.

2. Not if you are using one thread for each port the intermediate host is communicate with. In my implementation, either only the client or server sends packets to 1 intermediate host thread. THe intermediate host also doesn't store information as instance variables. Instead it saves the data in a box class where if the box is full, you have to wait before putting a new item. Or if it is empty, you have to wait before getting an item. The box class is and needs to be synchronized to avoid overwriting data, but the IntermediateHost class doesn't need to be.
