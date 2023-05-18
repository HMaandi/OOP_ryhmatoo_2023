package oop.ryhmatoo;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RequestHandler implements Runnable{

    final Socket client;
    final Connection connection;
    final String tulemusteFail = "server" + File.separatorChar + "src" + File.separatorChar + "main" + File.separatorChar + "resources" + File.separatorChar + "tulemused.txt";

    RequestHandler(Socket client, Connection connection) {
        this.connection = connection;
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
    private List<Sissekanne> loeSissekanded(Connection conn) throws IOException, SQLException {
        List<Sissekanne> sissekanded = new ArrayList<>();
        PreparedStatement ps = conn.prepareStatement("SELECT * FROM tulemused ORDER BY tulemus DESC");
        try (ResultSet resultSet = ps.executeQuery()) {
            while (resultSet.next()) {
                String isikukood = resultSet.getString("isikukood");
                String nimi = resultSet.getString("nimi");
                double tulemus = resultSet.getDouble("tulemus");
                sissekanded.add(new Sissekanne(isikukood, nimi, tulemus));
            }
        }
        return sissekanded;
    }
     synchronized private void salvestaTulemus(DataInputStream in, DataOutputStream out, Connection conn) throws IOException, SQLException {
        boolean onUus = true;
        List<Sissekanne> sissekanded = new ArrayList<>();
        try {
            sissekanded = loeSissekanded(conn);
            out.writeInt(App.TYPE_OK);
        }
        catch (Exception e) {
            out.writeInt(App.TYPE_ERROR);
        }
        String isikukood = in.readUTF();
        String nimi = in.readUTF();
        String meil = in.readUTF();
        double tulemus = in.readDouble();
        for (Sissekanne s : sissekanded) {
            if (s.getIsikukood().equals(isikukood)) {
                s.setTulemus(tulemus);
                onUus = false;
            }
        }
        if (onUus) {
            PreparedStatement ps = conn.prepareStatement("INSERT INTO tulemused (isikukood, nimi, tulemus) VALUES (?, ?, ?)");
            ps.setString(1, isikukood);
            ps.setString(2, nimi);
            ps.setDouble(3, tulemus);
            ps.executeUpdate();
        }
        else {
            PreparedStatement ps = conn.prepareStatement("UPDATE tulemused SET tulemus = ? WHERE isikukood = ?");
            ps.setDouble(1, tulemus);
            ps.setString(2, isikukood);
            ps.executeUpdate();
        }
        out.writeInt(App.TYPE_OK);

        }
    private void saadaEdeTabel(DataOutputStream out, Connection conn) throws IOException {
        List<Sissekanne> sissekanded = new ArrayList<>();
        try {
            sissekanded = loeSissekanded(conn);
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
                salvestaTulemus(dataIn, dataOut, connection);
            } else if (request == App.TYPE_GET_LEADERBOARD) {
                saadaEdeTabel(dataOut, connection);
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

