package oop.ryhmatoo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import static oop.ryhmatoo.CSVLuger.loeCSV;

public class Main {
    private List<Isik> isikud = new ArrayList<>();
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
        //NB! Leida võimalus et saaks faili sisse tõsta või läbi file exploreri leida see
        while(true) {
            System.out.println("Sisestage failitee Teie pangakonto väljavõtte CSV failile: ");
            String failitee = sc.nextLine();
            ArrayList<Ülekanne> ülekanded = new ArrayList<>(loeCSV(failitee));
            if(ülekanded.isEmpty()){
                System.out.println("Tekkis viga. Kontrollige et tegemist on õige failiga ja õige failiteega.");
                continue;
            }
            isikud.add(new Isik(isikukood, eesnimi, perekonnanimi, sissetulek, ülekanded));
            System.out.println("Kasutaja " + isikud.get(isikud.size()-1).toString() + " lisatud");
            break;
        }
    }
    public static void main(String[] args) {
        ArrayList<Ülekanne> ülekanded = new ArrayList<> (loeCSV("C:\\Users\\August\\IdeaProjects\\statement.csv"));
        System.out.println(ülekanded);
    }
}