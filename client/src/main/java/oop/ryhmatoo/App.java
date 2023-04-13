package oop.ryhmatoo;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

import static oop.ryhmatoo.CSVLuger.loeCSV;

public class App {
    public static final int TYPE_UPLOAD = 1;
    public static final int TYPE_GET_LEADERBOARD = 2;
    public static final int TYPE_OK = 3;
    public static final int TYPE_ERROR = 4;
    private int port = 1337;

    public void saadaIsik(Isik isik) throws IOException {
        System.out.println("ühendan serveriga");
        try (Socket socket = new Socket("localhost", port)) {
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            DataInputStream in = new DataInputStream(socket.getInputStream());
            out.writeInt(TYPE_UPLOAD);
            if (in.readInt() == TYPE_ERROR) {
                System.out.println("Serveril tekkis viga faili lugemisel");
            } else {
                out.writeInt(isik.getIsikukood());
                out.writeUTF(isik.getEesnimi() + " " + isik.getPerekonnanimi());
                out.writeDouble(isik.arvutaTulemus());
                if (in.readInt() == TYPE_OK) {
                    System.out.println("Isiku andmed on edukalt salvestatud serverisse");
                } else {
                    System.out.println("Serveril tekkis viga andmete salvestamisega");
                }
            }
        }
    }
    public void küsiEdetabel() throws IOException {
        System.out.println("ühendan serveriga");
        try (Socket socket = new Socket("localhost", port)) {
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            DataInputStream in = new DataInputStream(socket.getInputStream());
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
    public void looKasutaja() throws IOException {
        Scanner sc = new Scanner(System.in);
        System.out.println("Sisestage isikukood: ");
        int isikukood = Integer.parseInt(sc.nextLine());
        System.out.println("Sisestage eesnimi: ");
        String eesnimi = sc.nextLine();
        System.out.println("Sisestage perekonnanimi: ");
        String perekonnanimi = sc.nextLine();
        System.out.println("Sisestage igakuine sissetulek: ");
        Double sissetulek = Double.parseDouble(sc.nextLine());
        //tekib võimalus lisada csv fail
        //NB! Leida võimalus et saaks faili sisse tõsta või läbi file exploreri leida see
        while(true) {
            System.out.println("Sisestage failitee Teie pangakonto väljavõtte CSV failile: ");
            String failitee = sc.nextLine();
            ArrayList<Ülekanne> ülekanded = new ArrayList<>(loeCSV(failitee));
            if(ülekanded.isEmpty()){
                System.out.println("Tekkis viga. Kontrollige et tegemist on õige failiga ja õige failiteega.");
                continue;
            }
            Isik uusIsik = new Isik(isikukood, eesnimi, perekonnanimi, sissetulek, ülekanded);
                saadaIsik(uusIsik);
                System.out.println("Kasutaja " + uusIsik.toString() + " lisatud");
                break;
        }
    }
    public void main(String[] args) throws IOException{

    }
}

