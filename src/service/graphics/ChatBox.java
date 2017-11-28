package service.graphics;

import client.Client;
import service.RSA;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ChatBox {

    private Client client;
    private RSA encryption;
    private String username;
    private JFrame frame;
    private JPanel panel;
    private JPanel southPanel;
    private JTextArea textArea;
    private JTextField textField;
    private JButton send;
    private JMenuBar menuBar;
    private JMenu menu;
    private JMenuItem connect;
    private JMenuItem exit;

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

    private void buttonInit() {
        send = new JButton("Send");
        send.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new Thread(() -> {
                    String userInput = textField.getText();
                    System.out.println(userInput);
                    String message = new String(username + ": " + userInput + "\n");
                    client.sendMessage(bytesToString(encryption.encrypt(message.getBytes())));

                    byte[] ok = encryption.encrypt(message.getBytes());
                    byte[] ok1 = encryption.decrypt(ok);
                    String ok2 = new String(ok1);
                    System.out.println(ok2);
                }).start();
            }
        });
    }

    private void panelInit() {
        panel = new JPanel();
        southPanel = new JPanel();
        textArea = new JTextArea(38,58);
        textArea.setLineWrap(true);
        textField = new JTextField(50);
        buttonInit();

        panel.add(textArea);
        southPanel.add(textField);
        southPanel.add(send);
    }

    private void frameInit() {
        frame = new JFrame("Chat Box");
        frame.setJMenuBar(menuBar);
        frame.add(panel);
        frame.add(southPanel, BorderLayout.SOUTH);
        frame.setSize(700, 700);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    private void displayMessage() {
        try {
            String encryptedMessage = client.receive();

            if (encryptedMessage != null) {
                byte[] decryptedMessage = encryption.decrypt(encryptedMessage.getBytes());
                String decryptedString = new String(decryptedMessage);
                System.out.println(bytesToString(decryptedString.getBytes()));
                System.out.println(decryptedString);
                textArea.append(decryptedString);
            }
        } catch (NullPointerException ex) {
            ex.printStackTrace();
        }
    }

    public void chatInit() {
        menuInit();
        panelInit();
        frameInit();
    }

    private String bytesToString(byte[] encrypted)

    {

        String test = "";

        for (byte b : encrypted)

        {

            test += Byte.toString(b);

        }

        return test;

    }
}
