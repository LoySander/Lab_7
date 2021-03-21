package com.content7;

import javax.swing.*;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class Peer {
    private  String sender;
    private  String destinationAddress;

    public void SetSender(String sender){
        this.sender=sender;
    }
    public void SetdestinationAddress(String destinationAddress){
        this.destinationAddress = destinationAddress;
    }
    public String GetdestinationAddress(){
        return destinationAddress;
    }
    public String GetSender(){
        return sender;
    }
    /*public Peer(String sender,String destinationAddress){
     this.sender = sender;
     this.destinationAddress = destinationAddress;
    }*/

}
