package oop.ryhmatoo;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RequestHandler implements Runnable{

    final Socket client;
    final String tulemusteFail = "server" + File.separatorChar + "src" + File.separatorChar + "main" + File.separatorChar + "resources" + File.separatorChar + "tulemused.txt";

    RequestHandler(Socket client) {
        this.client = client;
    }

    private static Sissekanne ridaSissekandeks(String rida) {
        String[] split = rida.split("\\|");
        Sissekanne sissekanne;
        sissekanne = new Sissekanne(split[0],
                                    split[1],
                                    Double.parseDouble(split[2]));
        return sissekanne;
    }
    private List<Sissekanne> loeSissekanded(String fail) throws IOException {
        return new ArrayList<>(Files.readAllLines(Path.of(fail))
                .stream()
                .map(RequestHandler::ridaSissekandeks)
                .toList());
    }
     synchronized private void salvestaTulemus(DataInputStream in, DataOutputStream out) throws IOException {
        boolean onUus = true;
        File fail = new File(tulemusteFail);
        List<Sissekanne> sissekanded = new ArrayList<>();
        try {
            sissekanded = loeSissekanded(tulemusteFail);
        }
        catch (Exception e) {

            out.writeInt(App.TYPE_ERROR);
        }
        String isikukood = in.readUTF();
        String nimi = in.readUTF();
        double tulemus = in.readDouble();
        for (Sissekanne s : sissekanded) {
            if (s.getIsikukood().equals(isikukood)) {
                s.setTulemus(tulemus);
                onUus = false;
            }
        }
        if (onUus) {
            sissekanded.add(new Sissekanne(isikukood, nimi, tulemus));
        }
        Collections.sort(sissekanded);
        try (   FileWriter fw = new FileWriter(fail);
                BufferedWriter bw = new BufferedWriter(fw)
                ) {
            for (Sissekanne s: sissekanded) {
                bw.write(s.getIsikukood() + "|" + s.getNimi() + "|" + s.getTulemus());
                bw.newLine();
            }
            out.writeInt(App.TYPE_OK);
        }
    }
    private void saadaEdeTabel(DataOutputStream out) throws IOException {
        List<Sissekanne> sissekanded = new ArrayList<>();
        try {
            sissekanded = loeSissekanded(tulemusteFail);
        }
        catch (Exception e) {
            out.writeInt(App.TYPE_ERROR);
        }
        out.writeInt(App.TYPE_OK);
        out.writeInt(sissekanded.size());
        for (Sissekanne s : sissekanded) {
            out.writeUTF(s.getNimi());
            out.writeDouble(s.getTulemus());
        }
    }

    private void handleRequests() throws Exception {
        try (client;
             OutputStream out = client.getOutputStream();
             DataOutputStream dataOut = new DataOutputStream(out);
             InputStream in = client.getInputStream();
             DataInputStream dataIn = new DataInputStream(in)) {
            System.out.println("Client connected");
            int request = dataIn.readInt();
            if (request == App.TYPE_UPLOAD) {
                salvestaTulemus(dataIn, dataOut);
            } else if (request == App.TYPE_GET_LEADERBOARD) {
                saadaEdeTabel(dataOut);
            } else {
                dataOut.writeInt(App.TYPE_ERROR);
            }

        }
    }
    @Override
    public void run() {
        try {
            handleRequests();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

