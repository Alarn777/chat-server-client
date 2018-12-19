package server;

import shared.StringConsumer;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Scanner;

public class ServerApplication  {

    public static  MessageBoard mb;
    private static int port = 0;
    private static ServerSocket server = null;

    public static void main(String args[])
    {

        mb = new MessageBoard(server);


        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {


                System.out.println(mb.getServerPort());
                try
                {

                    server = new ServerSocket(Integer.parseInt(mb.getServerPort()),5);
                }
                catch(IOException ex)
                {
                    System.out.println("Error creating server");
                    ex.printStackTrace();

                }

                System.out.println("Server created!");
                Socket socket = null;
                ClientDescriptor client = null;
                ConnectionProxy connection = null;
                mb.chatBox.append("Server started at port " + mb.getServerPort() + "\n");
                while(true)
                {
                    try {

                        socket = server.accept();
                        connection = new ConnectionProxy(socket,mb);
                        client = new ClientDescriptor();
                        connection.addConsumer(client);
                        connection.addMb(mb);

                        client.addConsumer(mb);


                        mb.addConsumer(connection);
                        connection.start();
                    }
                    catch(IOException e)
                    {

                    }
                }
            }
        });

        mb.display(t1);

    }
}
