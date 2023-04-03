package oop.ryhmatoo;

public class Ülekanne {
    private String kuupäev;
    private String teineOsapool;
    private Double summa;
    private Boolean onVäljaminek;

    public Ülekanne(String kuupäev, String teineOsapool, Double summa, Boolean onVäljaminek) {
        this.kuupäev = kuupäev;
        this.teineOsapool = teineOsapool;
        this.summa = summa;
        this.onVäljaminek = onVäljaminek;
    }

    public Double getSumma() {
        return summa;
    }

    public Boolean getOnVäljaminek() {
        return onVäljaminek;
    }

    @Override
    public String toString() {
        return "Ülekanne{" +
                "kuupäev='" + kuupäev + '\'' +
                ", teineOsapool='" + teineOsapool + '\'' +
                ", summa=" + summa +
                ", onVäljaminek=" + onVäljaminek +
                '}';
    }
}
