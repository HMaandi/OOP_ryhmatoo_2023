package oop.ryhmatoo;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
    import java.util.List;
import java.util.stream.Collectors;

public class CSVLuger {


    public static List<Ülekanne> loeCSV(String failiNimi) {
        ArrayList<Ülekanne> CSVSisu = new ArrayList<>();

        try {
            List<String> failiRead = Files.readAllLines(Paths.get(failiNimi), StandardCharsets.UTF_8);
            failiRead = failiRead.stream()
                    .map(s -> s.replaceAll("\"", ""))
                    .collect(Collectors.toList());
            for (String rida : failiRead) {
                String[] sisu = rida.split(";");
                switch (sisu[1]) {
                    case "10", "82", "86", "Reatüüp":
                        break;

                    case "20":
                        //Vaatab kas tegemist on rahakoguja tehinguga, kuna see kasutajat ei mõjuta siis see ignoreeritakse
                        boolean onRahakogujaTehing =
                                sisu[4].equals("Kaardimaksete abil Rahakogujasse kogutud summa:")
                                || sisu[4].equals("Väljamakse Rahakogujast kontole EE672200221046975169");
                        if (onRahakogujaTehing) {break;}

                        //Vaatab kas tegemist on kulutusega(K) või mittekulutusega(MK)
                        boolean onVäljaminek = sisu[9].equals("K");
                        //Eemaldab liigsed tühikud
                        String teineOsapool = sisu[3].trim().replaceAll(" +", " ");
                        //Muudab komakohaga arvu punktiga arvuks
                        double summa = Double.parseDouble(sisu[5].replaceAll(",", "."));
                        Ülekanne ülekanne = new Ülekanne(sisu[2], teineOsapool, summa, onVäljaminek);
                        CSVSisu.add(ülekanne);
                        break;
                }
            }
        } catch (IOException ex) {
            System.out.format("I/O error: %s%n", ex);
        }
        return CSVSisu.stream().toList();
    }

}
