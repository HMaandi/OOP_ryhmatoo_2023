package oop.ryhmatoo;

import java.util.Scanner;

public class Main {
    public void looKasutaja(){
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
        //loeCSV, mis tagastab List<Ülekanne>
        //Isik isik = new Isik(isiskukood, eesnimi, perekonnanimi, sissetulek, tagastatudList);
    }
    public static void main(String[] args) {

    }
}