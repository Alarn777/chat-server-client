package server;

import shared.StringConsumer;
import shared.StringProducer;

import java.util.Vector;

public class ClientDescriptor implements StringConsumer, StringProducer {

    int descriptor;
    String name;
    Vector consumers;

    public ClientDescriptor(){
        descriptor = 0;
        name = "";
        consumers = new Vector();
    }

    void setName(String _name){
        name = _name;
    }

    void setDescriptor(int _descriptor){
        descriptor = _descriptor;
    }



    @Override
    public void consume(String str) {

    }

    @Override
    public void addConsumer(StringConsumer sc) {
        consumers.add(sc);
    }

    @Override
    public void removeConsumer(StringConsumer sc) {

    }
}


