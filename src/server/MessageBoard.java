package server;

import shared.StringConsumer;
import shared.StringProducer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collections;
import java.util.Vector;

public class MessageBoard implements StringConsumer, StringProducer
{
    Vector<ConnectionProxy> allConnections;
    JFrame newFrame = new JFrame("My chat server v1.0");
    JButton exit;
    JTextField messageBox;
    JTextArea chatBox;
    JTextField usernameChooser;
    JTextField IPChooser;
    JButton enterServer;
    JTextField PortChooser;

    private String serverPort;

    public String getServerPort() {
        return serverPort;
    }

    public MessageBoard(ServerSocket _server){
        allConnections = new Vector<ConnectionProxy>();

    }


    @Override
    public void consume(String str) {
        chatBox.append(str);
        chatBox.append("\n");
        for (ConnectionProxy tempConnection: allConnections){

            System.out.println(str);




            try {
                tempConnection.dos.writeUTF(str);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void addConsumer(StringConsumer sc) {
        allConnections.add((ConnectionProxy) sc);
    }

    @Override
    public void removeConsumer(StringConsumer sc) {

    }

    public void disconnect(Socket my_socket) {
       for (int i = 0; i < allConnections.size();i++)
           if (allConnections.elementAt(i).my_socket == my_socket)
           {
               allConnections.remove(i);
           }

    }


    public void display(Thread t1) {
        newFrame.setVisible(true);
        JPanel southPanel = new JPanel();
        newFrame.add(BorderLayout.SOUTH, southPanel);
        southPanel.setBackground(Color.BLUE);
        southPanel.setLayout(new GridBagLayout());

        messageBox = new JTextField(30);
        exit = new JButton("Exit");
        exit.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                consume("Server was disconnected. Good bye!");
                for (int i = 0; i < allConnections.size() ; i++){
                    try {
                        allConnections.get(i).my_socket.close();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
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
        JLabel choosePortLabel = new JLabel("Pick a port for server to run on:");
        enterServer = new JButton("Start Server");
        enterServer.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                serverPort = PortChooser.getText();
                enterServer.setEnabled(false);
                t1.start();

            }
        });

        PortChooser = new JTextField();
        PortChooser.setColumns(5);
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



        southPanel.add(exit, right);

        chatBox.setFont(new Font("Serif", Font.PLAIN, 15));
        newFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        newFrame.setSize(1000, 800);





    }





}