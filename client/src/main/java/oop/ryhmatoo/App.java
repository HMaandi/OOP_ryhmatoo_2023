package oop.ryhmatoo;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
public class App {
    public static final int TYPE_UPLOAD = 1;
    public static final int TYPE_GET_LEADERBOARD = 2;
    public static final int TYPE_OK = 3;
    public static final int TYPE_ERROR = 4;
    private int request;
    private Isik edastatavIsik;

    public App(int request) {
        this.request = request;
    }

    public void main(String[] args) throws IOException {
        int port = 1337;
        System.out.println("ühendan serveriga");
        try (Socket socket = new Socket("localhost", port)) {
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            DataInputStream in = new DataInputStream(socket.getInputStream());
            switch(request){
                case TYPE_UPLOAD:
                    out.writeInt(TYPE_UPLOAD);
                    if(in.readInt() == TYPE_ERROR){
                        System.out.println("Serveril tekkis viga faili lugemisel");
                    }else{
                        out.writeInt(edastatavIsik.getIsikukood());
                        out.writeUTF(edastatavIsik.getEesnimi() + " " + edastatavIsik.getPerekonnanimi());
                        out.writeDouble(edastatavIsik.arvutaTulemus());
                        if(in.readInt() == TYPE_OK){
                            System.out.println("Isiku andmed on edukalt salvestatud serverisse");
                        }
                        else{
                            System.out.println("Serveril tekkis viga andmete salvestamisega");
                        }
                    }
                case TYPE_GET_LEADERBOARD:
                    out.writeInt(TYPE_GET_LEADERBOARD);
                    int echo = in.readInt();
                    if(echo == TYPE_OK){
                        int ridadeArv = in.readInt();
                        for (int i = 1; i <= ridadeArv; i++) {
                            String nimi = in.readUTF();
                            String tulemus = in.readUTF();
                            System.out.println(i + ". " + nimi + ": " + tulemus);
                        }
                    }
                    else{
                        System.out.println("Serveril tekkis viga edetabeli sisselugemisel");
                    }
            }
        }
    }
}
