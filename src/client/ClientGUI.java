package client;

import shared.StringConsumer;
import shared.StringProducer;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;

public class ClientGUI implements StringConsumer, StringProducer {
//    public static String serverName = "127.0.0.1";
//    public static int serverPortNumber = 1300;
    private static String username;

    private String ip = "";
    private int port = 0;
    private static boolean flag = false;
    public static ClientGUI my_GUI;
    JFrame newFrame = new JFrame("My chat v1.0");
    JButton sendMessage;
    JButton exit;
    JButton enterServer;
    JTextField messageBox;
    JTextArea chatBox;
    JTextField usernameChooser;
    JTextField IPChooser;
    JTextField PortChooser;
    JFrame preFrame;

    static ConnectionProxy connection = null;
    static Socket socket = null;


    public static String getUsername(){
        return username;
    }

    public static void main(String args[]) {
        my_GUI = new ClientGUI();

        my_GUI.display();



    }








    public ClientGUI(){
        username = "";
    }


    public void display() {
        newFrame.setVisible(true);
        JPanel southPanel = new JPanel();
        newFrame.add(BorderLayout.SOUTH, southPanel);
        southPanel.setBackground(Color.BLUE);
        southPanel.setLayout(new GridBagLayout());

        messageBox = new JTextField(30);
        messageBox.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode() == KeyEvent.VK_ENTER){
                    if (!messageBox.getText().equals("")) {
                        connection.consume(messageBox.getText());
                        messageBox.setText("");
                    }
                }
            }

        });
        sendMessage = new JButton("Send Message");
        exit = new JButton("Exit");
        exit.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                connection.consume("disconnect");
                connection.close();
                System.exit(0);
            }
        });
        chatBox = new JTextArea();
        chatBox.setEditable(false);
        newFrame.add(new JScrollPane(chatBox), BorderLayout.CENTER);

        //my code
        Panel pnl = new Panel();


        usernameChooser = new JTextField();

        usernameChooser.setColumns(5);
        JLabel chooseUsernameLabel = new JLabel("Pick a username:");
        IPChooser = new JTextField();
        IPChooser.setColumns(5);
        JLabel chooseIPLabel = new JLabel("Pick an IP:");
        PortChooser = new JTextField();
        PortChooser.setColumns(5);
        JLabel choosePortLabel = new JLabel("Pick a port:");
        enterServer = new JButton("Enter Server");
        enterServer.addActionListener(new enterServerButtonListener());
        pnl.add(chooseUsernameLabel);
        pnl.add(usernameChooser);

        pnl.add(chooseIPLabel);
        pnl.add(IPChooser);

        pnl.add(choosePortLabel);
        pnl.add(PortChooser);

        pnl.add(enterServer);
        newFrame.add(pnl, BorderLayout.NORTH);


        //his code
        chatBox.setLineWrap(true);

        GridBagConstraints left = new GridBagConstraints();
        left.anchor = GridBagConstraints.WEST;
        GridBagConstraints right = new GridBagConstraints();
        right.anchor = GridBagConstraints.EAST;
        right.weightx = 2.0;

        southPanel.add(messageBox, left);
        southPanel.add(sendMessage, right);
        southPanel.add(exit, right);

        chatBox.setFont(new Font("Serif", Font.PLAIN, 15));
        newFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        newFrame.setSize(1000, 800);


        sendMessage.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (!messageBox.getText().equals("")) {
                    connection.consume(messageBox.getText());
                    messageBox.setText("");
                }
            }
        });

    }



    @Override
    public void consume(String str) {
            if(!str.equals("")){
                chatBox.append(str);
                chatBox.append("\n");
            }
    }

    @Override
    public void addConsumer(StringConsumer sc) {

    }

    @Override
    public void removeConsumer(StringConsumer sc) {

    }

    class enterServerButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            username = usernameChooser.getText();
            ip = IPChooser.getText();
            if (ip.equals(""))
            {
                ip = "127.0.0.1";
            }
            try {
                port = Integer.parseInt(PortChooser.getText());
            }
            catch (NumberFormatException e) {
                System.out.println("Port not valid!");
            }
            if (port == 0)
            {
                port = 1300;
            }


            if (username.length() < 1) {
                System.out.println("Pick longer name!");
            } else {

                try {
                    socket = new Socket(ip, port);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                try {
                    connection = new ConnectionProxy(socket, my_GUI);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                connection.addConsumer(my_GUI);
                connection.consume(username);
                enterServer.setEnabled(false);

                flag = true;


                Thread th1 = null;
                try {
                    th1 = new Thread(new ConnectionProxy(socket,my_GUI), "server-data");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                th1.start();


            }
        }
    }
}

