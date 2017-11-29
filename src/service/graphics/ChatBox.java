package service.graphics;

import client.Client;
import service.RSA;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Chat box class that contains basic GUI for the chat application and functionality of buttons.
 */
public class ChatBox {

    /** Client object that will do receiving and sending functionality. */
    private Client client;

    /** RSA encryption. */
    private RSA encryption;

    /** Username for current client user. */
    private String username;

    /** Main frame of the application. */
    private JFrame frame;

    /** Main panel of the application. */
    private JPanel panel;

    /** South panel of the application. */
    private JPanel southPanel;

    /** Main text area of the chat display. */
    private JTextArea textArea;

    /** Text field for user input. */
    private JTextField textField;

    /** JButton for sending message. */
    private JButton send;

    /** Main menu bar of the application. */
    private JMenuBar menuBar;

    /** Main menu of the menu bar. */
    private JMenu menu;

    /** Menu item that connects to the server. */
    private JMenuItem connect;

    /** Menu item that exits the application. */
    private JMenuItem exit;

    /**
     * Initialize menu action listener for connect and exit.
     */
    private void menuAction() {
        connect.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame textbox = new JFrame("Connect to server");
                textbox.setSize(300, 130);
                JPanel controlPanel = new JPanel();
                controlPanel.setLayout(new FlowLayout());

                JLabel nickname = new JLabel("Nickname:       ");
                JLabel hostAddress = new JLabel("Host Address: ");
                JTextField userNickname = new JTextField(15);
                JTextField userInput = new JTextField(15);
                nickname.setLabelFor(userNickname);
                hostAddress.setLabelFor(userInput);

                JButton connect = new JButton("Connect");
                connect.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        username = userNickname.getText();

                        new Thread(() -> {
                            client = new Client(userInput.getText());
                            encryption = client.getRSA();
                            JOptionPane.showMessageDialog(null, "Connected");

                            new Thread(() -> {
                                while (true) {
                                    displayMessage();
                                }
                            }).start();
                        }).start();
                    }
                });

                controlPanel.add(nickname);
                controlPanel.add(userNickname);
                controlPanel.add(hostAddress);
                controlPanel.add(userInput);
                controlPanel.add(connect);
                textbox.add(controlPanel);
                textbox.setVisible(true);
                textbox.toFront();
            }
        });

        exit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(1);
            }
        });
    }

    /**
     * Initialize menu and menu items of the application.
     */
    private void menuInit() {
        connect = new JMenuItem("Connect");
        exit = new JMenuItem("Exit");
        menu = new JMenu("Menu");
        menuBar = new JMenuBar();

        menuAction();

        menu.add(connect);
        menu.add(exit);
        menuBar.add(menu);
    }

    /**
     * Initialize send button and added its action listener.
     */
    private void buttonInit() {
        send = new JButton("Send");
        send.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new Thread(() -> {
                    String userInput = textField.getText();
                    String message = new String(username + ": " + userInput + "\n");
                    client.sendMessage(encryption.encrypt(message.getBytes()));
                }).start();

                textField.setText("");
            }
        });
    }

    /**
     * Initialize the main panel and south panel for the application.
     */
    private void panelInit() {
        panel = new JPanel();
        southPanel = new JPanel();
        textArea = new JTextArea(38,58);
        textArea.setLineWrap(true);
        textArea.setEditable(false);
        textField = new JTextField(50);
        buttonInit();

        panel.add(textArea);
        southPanel.add(textField);
        southPanel.add(send);
    }

    /**
     * Initialize the main frame for the application.
     */
    private void frameInit() {
        frame = new JFrame("Chat Box");
        frame.setJMenuBar(menuBar);
        frame.add(panel);
        frame.add(southPanel, BorderLayout.SOUTH);
        frame.setSize(700, 700);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    /**
     * Method for listening to the server and decrypt message sent from the server.
     * The decrypted message will then be displayed onto the text area.
     */
    private void displayMessage() {
        try {
            byte[] encryptedMessage = client.receive();

            if (encryptedMessage != null) {
                byte[] decryptedMessage = encryption.decrypt(encryptedMessage);
                String decryptedString = new String(decryptedMessage);
                textArea.append(decryptedString);
            }
        } catch (NullPointerException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Initialize the chat service.
     */
    public void chatInit() {
        menuInit();
        panelInit();
        frameInit();
    }
}
