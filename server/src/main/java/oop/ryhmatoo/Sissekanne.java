package oop.ryhmatoo;

public class Sissekanne implements Comparable<Sissekanne>{
    private final String isikukood;
    private final String nimi;
    private double tulemus;

    public Sissekanne(String isikukood, String nimi, double tulemus) {
        this.isikukood = isikukood;
        this.nimi = nimi;
        this.tulemus = tulemus;
    }

    public String getIsikukood() {
        return isikukood;
    }

    public String getNimi() {
        return nimi;
    }

    public double getTulemus() {
        return tulemus;
    }

    public void setTulemus(double tulemus) {
        this.tulemus = tulemus;
    }

    @Override
    public int compareTo(Sissekanne o) {
        return Double.compare(this.tulemus, o.getTulemus());
    }
}
