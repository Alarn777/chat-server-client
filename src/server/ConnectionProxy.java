package server;
import java.time.*;
import java.text.*;
import shared.StringConsumer;
import shared.StringProducer;

import java.net.Socket;
import java.net.*;
import java.io.*;
import java.util.*;

public class ConnectionProxy extends Thread implements StringConsumer, StringProducer
{
    private Vector my_consumers;
    public Socket my_socket;
    ServerSocket server = null;
    InputStream is = null;
    public DataInputStream dis = null;
    OutputStream os = null;
    public DataOutputStream dos = null;
    public String userName;
    private volatile MessageBoard mb = null;

    public ConnectionProxy(Socket socket, MessageBoard _mb) throws IOException {
        my_socket = socket;
        mb = _mb;
        is = socket.getInputStream();
        dis = new DataInputStream(is);
        os = socket.getOutputStream();
        dos = new DataOutputStream(os);
        my_consumers = new Vector();
        userName = "";
    }


    public void start(){
        Thread th1 = null;
        try {
            th1 = new Thread(new ConnectionProxy(my_socket,mb), "listener");
            th1.start();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void produce(String str) {

    }


    @Override
    public void consume(String str) {

    }

    @Override
    public void addConsumer(StringConsumer sc) {
        my_consumers.add(sc);


    }

    @Override
    public void removeConsumer(StringConsumer sc) {

    }
    @Override
    public void run() {
        while (userName.equals("")){
            try {
                userName = dis.readUTF();
                System.out.println(userName);
                mb.consume(userName + " joined!");

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        while (true) {
            String userOutput = "";
            try {
                userOutput = dis.readUTF();
                if(userOutput.equals("disconnect"))
                {
                    mb.consume(userName + " left!");
                    mb.disconnect(my_socket);
                    my_socket.close();
                    continue;
                }
                String sendVal = userName + " At: " + getCurrentTimeUsingDate();
                sendVal += " " + userOutput;
                mb.consume(sendVal);


            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public void addMb(MessageBoard _mb) {
        mb = _mb;
    }

    public static String getCurrentTimeUsingDate() {
        Date date = new Date();
        String strDateFormat = "hh:mm:ss";
        DateFormat dateFormat = new SimpleDateFormat(strDateFormat);
        return dateFormat.format(date);
    }
}