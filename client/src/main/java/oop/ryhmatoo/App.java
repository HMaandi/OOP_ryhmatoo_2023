package oop.ryhmatoo;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static oop.ryhmatoo.CSVLuger.loeCSV;

public class App {
    public static final int TYPE_UPLOAD = 1;
    public static final int TYPE_GET_LEADERBOARD = 2;
    public static final int TYPE_OK = 3;
    public static final int TYPE_ERROR = 4;
    public static final int TYPE_GET_BIGGEST_SINGLE_PURCHASE = 5;
    public static final int TYPE_GET_BIGGEST_OVERALL_SPENDING = 6;
    private static final int port = 1337;

    public static void saadaIsik(Isik isik) throws IOException {
        System.out.println("ühendan serveriga");
        try (Socket socket = new Socket("localhost", port)) {
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            DataInputStream in = new DataInputStream(socket.getInputStream());
            out.writeInt(TYPE_UPLOAD);
            if (in.readInt() == TYPE_ERROR) {
                System.out.println("Serveril tekkis viga faili lugemisel");
            } else {
                out.writeUTF(isik.getIsikukood());
                out.writeUTF(isik.getEesnimi() + " " + isik.getPerekonnanimi());
                out.writeDouble(isik.arvutaTulemus());
                out.writeUTF(isik.getMeiliAadress());
                out.writeUTF(isik.arvutaSuurimVäljaminek());
                out.writeUTF(isik.arvutaSuurimVäljaminekuSumma());
                if (in.readInt() == TYPE_OK) {
                    System.out.println("Isiku andmed on edukalt salvestatud serverisse");
                } else {
                    System.out.println("Serveril tekkis viga andmete salvestamisega");
                }
            }
        }
    }
    public static void küsiEdetabel() throws IOException {
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

    public static String valiFail(){
        JFileChooser jfc = new JFileChooser();
        jfc.setFileFilter(new FileNameExtensionFilter("CSV", ".csv"));
        int tulemus = jfc.showOpenDialog(null);
        if(tulemus == JFileChooser.APPROVE_OPTION){
            return jfc.getSelectedFile().getAbsolutePath();
        }
        return null;
    }
    public static void looKasutaja() throws IOException {
        Scanner sc = new Scanner(System.in);
        System.out.println("Sisestage oma isikukood: ");
        String isikukood = sc.nextLine();
        System.out.println("Sisestage oma eesnimi: ");
        String eesnimi = sc.nextLine();
        System.out.println("Sisestage oma perekonnanimi: ");
        String perenimi = sc.nextLine();
        System.out.println("Valige pangakonto väljavõtte .csv fail.");
        String failitee = valiFail();
        CSVLuger csvLuger = new CSVLuger();
        List<Ülekanne> ülekanded = csvLuger.loeCSV(valiFail());
        System.out.println("Sisestage oma e-posti aadress: ");
        String meil = sc.nextLine();
        Isik uusIsik = new Isik(isikukood, eesnimi, perenimi, meil, ülekanded);
        saadaIsik(uusIsik);
        System.out.println("Kasutaja " + uusIsik + " lisatud");
        }
    public static void küsiSuurimKulutus() throws IOException {
        System.out.println("ühendan serveriga");
        try (Socket socket = new Socket("localhost", port)) {
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            DataInputStream in = new DataInputStream(socket.getInputStream());
            out.writeInt(TYPE_GET_BIGGEST_SINGLE_PURCHASE);
            int echo = in.readInt();
            if(echo == TYPE_OK){
                String suurimKulutus = in.readUTF();
                System.out.println(suurimKulutus);
            }
            else{
                System.out.println("Serveril tekkis viga edetabeli sisselugemisel");
            }
        }
    }
    public static void küsiSuurimKuludeSumma() throws IOException {
        System.out.println("ühendan serveriga");
        try (Socket socket = new Socket("localhost", port)) {
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            DataInputStream in = new DataInputStream(socket.getInputStream());
            out.writeInt(TYPE_GET_BIGGEST_OVERALL_SPENDING);
            int echo = in.readInt();
            if(echo == TYPE_OK){
                String suurimKulutusSumma = in.readUTF();
                System.out.println(suurimKulutusSumma);
            }
            else{
                System.out.println("Serveril tekkis viga edetabeli sisselugemisel");
            }
        }
    }
    public static void main(String[] args) throws IOException{
        Scanner sc = new Scanner(System.in);
        boolean programmKäib = true;
        while(programmKäib){
            System.out.println("1 - Loo uus kasutaja");
            System.out.println("2 - Kuva edetabel");
            System.out.println("3 - Kuva suurim kulutus");
            System.out.println("4 - Kuva asutuse nimi ja summa, kuhu läks kõige rohkem raha");
            System.out.println("5 - Sulge programm");
            int valik = sc.nextInt();
            switch (valik){
                case 1:
                    looKasutaja();
                    break;
                case 2:
                    küsiEdetabel();
                    break;
                case 3:
                    küsiSuurimKulutus();
                    break;
                case 4:
                    küsiSuurimKuludeSumma();
                    break;
                case 5:
                    programmKäib = false;
                    break;
            }
        }
    }
}

