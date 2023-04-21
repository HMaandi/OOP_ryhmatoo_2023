package oop.ryhmatoo;

import java.util.List;

public class Isik {
    private int isikukood;
    private String eesnimi;
    private String perekonnanimi;
    private String meiliAadress;
    private Double sissetulek;
    private List<Ülekanne> ülekanded;

    public Isik(int isikukood, String eesnimi, String perekonnanimi, String meiliAadress, Double sissetulek, List<Ülekanne> ülekanded) {
        this.isikukood = isikukood;
        this.eesnimi = eesnimi;
        this.perekonnanimi = perekonnanimi;
        this.meiliAadress = meiliAadress;
        this.sissetulek = sissetulek;
        this.ülekanded = ülekanded;
    }

    public String getMeiliAadress() {
        return meiliAadress;
    }

    public int getIsikukood() {
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
        double kuuKulud = 0;
        for (Ülekanne ük: ülekanded) {
            if(ük.getOnVäljaminek()){
                kuuKulud += ük.getSumma();
            }
        }
        return Math.round(10*(sissetulek / kuuKulud));
    }
}
