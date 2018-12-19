package client;

import shared.StringConsumer;
import shared.StringProducer;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;

public class ConnectionProxy extends Thread implements StringConsumer, StringProducer
{
    public Vector consumers;
    private ClientGUI gui;
    private Vector my_consumers;
    Socket my_socket;
    ServerSocket server = null;
    InputStream is = null;
    public DataInputStream dis = null;
    OutputStream os = null;
    public DataOutputStream dos = null;

    public ConnectionProxy(Socket socket, ClientGUI my_gui) throws IOException {
        consumers = new Vector();
        my_socket = socket;
        is = socket.getInputStream();
        dis = new DataInputStream(is);
        os = socket.getOutputStream();
        dos = new DataOutputStream(os);
        my_consumers = new Vector();
        gui = my_gui;
    }

    @Override
    public void consume(String str) {
        try {
            dos.writeUTF(str);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void addConsumer(StringConsumer sc) {
        gui= (ClientGUI)sc;
    }

    @Override
    public void removeConsumer(StringConsumer sc) {

    }
    @Override
    public void run() {

        while (true) {
            String serverOutput = "";
            try {
                serverOutput = dis.readUTF();
                System.out.println(serverOutput);                                                           //--------------
                if (!serverOutput.equals("")) {


                    gui.consume(serverOutput);

                }

            } catch (IOException e) {
                try {
                    my_socket.close();
                    return;
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                e.printStackTrace();
                return;
            }
        }
    }
    public void close(){
        try {
            my_socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}