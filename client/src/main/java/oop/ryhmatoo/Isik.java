package oop.ryhmatoo;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class Isik {
    private String isikukood;
    private String eesnimi;
    private String perekonnanimi;
    private String meiliAadress;
    private List<Ülekanne> ülekanded;

    public Isik(String isikukood, String eesnimi, String perekonnanimi, String meiliAadress, List<Ülekanne> ülekanded) {
        this.isikukood = isikukood;
        this.eesnimi = eesnimi;
        this.perekonnanimi = perekonnanimi;
        this.meiliAadress = meiliAadress;
        this.ülekanded = ülekanded;
    }


    public String getMeiliAadress() {
        return meiliAadress;
    }

    public String getIsikukood() {
        return isikukood;
    }

    public String getEesnimi() {
        return eesnimi;
    }

    public String getPerekonnanimi() {
        return perekonnanimi;
    }

    @Override
    public String toString() {
        return "isikukood=" + isikukood +
                ", eesnimi='" + eesnimi + '\'' +
                ", perekonnanimi='" + perekonnanimi + '\'' +
                '}';
    }

    public double arvutaTulemus(){
        double sissetulek = arvutaKokkuSissetulekud();
        double kuuKulud = 0;
        for (Ülekanne ük: ülekanded) {
            if(ük.getOnVäljaminek()){
                kuuKulud += ük.getSumma();
            }
        }
        return Math.round(10*( sissetulek/ kuuKulud));
    }
    public double arvutaKokkuVäljaminekud() {
        double summa = 0;
        for (Ülekanne ülekanne : ülekanded) {
            if (ülekanne.getOnVäljaminek()) {
                summa += ülekanne.getSumma();
            }
        }
        return summa;
    }
    public double arvutaKokkuSissetulekud() {
        double summa = 0;
        for (Ülekanne ülekanne : ülekanded) {
            if (!ülekanne.getOnVäljaminek()) {
                summa += ülekanne.getSumma();
            }
        }
        return summa;
    }
    public String arvutaSuurimVäljaminek() {
        if (ülekanded.size() == 0) {return null;}
        Ülekanne suurim = ülekanded.get(0);
        for (Ülekanne ülekanne : ülekanded) {
            if (ülekanne.getSumma() > suurim.getSumma() && ülekanne.getOnVäljaminek()) {
                suurim = ülekanne;
            }
        }
        return suurim.getTeineOsapool() + " - " + suurim.getSumma() + "€";
    }

    public String arvutaSuurimVäljaminekuSumma(){
        Map<String, Double> väljaminekuteSihtkohad = new HashMap<>();
        String maxVõti = "";
        double maxVäärtus = 0.0;
        for (Ülekanne ülekanne : ülekanded) {
            if (ülekanne.getOnVäljaminek()) {
                if (väljaminekuteSihtkohad.containsKey(ülekanne.getTeineOsapool())) {
                    väljaminekuteSihtkohad.replace(ülekanne.getTeineOsapool(),
                            ülekanne.getSumma() + väljaminekuteSihtkohad.get(ülekanne.getTeineOsapool()));
                    if (maxVäärtus < ülekanne.getSumma() + väljaminekuteSihtkohad.get(ülekanne.getTeineOsapool())){
                        maxVõti = ülekanne.getTeineOsapool();
                        maxVäärtus = ülekanne.getSumma() + väljaminekuteSihtkohad.get(ülekanne.getTeineOsapool());
                    }
                } else {väljaminekuteSihtkohad.put(ülekanne.getTeineOsapool(),ülekanne.getSumma());
                    if (maxVäärtus < ülekanne.getSumma()){
                        maxVõti = ülekanne.getTeineOsapool();
                        maxVäärtus = ülekanne.getSumma();
                    }}
            }
        }
        return maxVõti + " " +maxVäärtus;
    }
}
