# Proj5

Multi-threaded online chat service for CS 342 Project 5. Application uses RSA encryption/decryption for message security. Chat service allow users to enter chat room with self assigned username to chat with all members in the chat room. Message updates in real time. Application written in Java.

### How to use chat service

First, start up the server side application.

```sh
$ javac StartingServer.java
$ java StartingServer
```

Then, once you receive the host address from terminal, you can begin the client side application.

```sh
$ javac StartingClient.java
$ java StartingClient
```

After the application for the client side has launched, click the `menu` on the menu bar and then click `connect` to connect to the host. Enter your preferred username and the address of the host of the server. Then click connect when you are ready to enter the chat room. A message with `"Connected"` should show up once you are successfully connected with the server.

Once you are connected to the server, you will be able to receive message from others in the chat room marked with their preferred username and you can be able to chat with them by typing your message down at the bottom and click `send` button once you are ready to send the message.

### RSA Encryption

This application used RSA encryption/decryption for message security. Details on implementation see CS 342 [Project 5 Description][cs342].

[cs342]: <https://www.cs.uic.edu/pub/CS342/AssignmentsF17/CS_342_proj5_f17.pdf>