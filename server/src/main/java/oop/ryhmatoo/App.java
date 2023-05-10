package oop.ryhmatoo;


import java.io.InputStreamReader;
import java.io.Reader;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import org.h2.tools.RunScript;

public class App
{
    public static final int TYPE_UPLOAD = 1;
    public static final int TYPE_GET_LEADERBOARD = 2;
    public static final int TYPE_OK = 3;
    public static final int TYPE_ERROR = 4;
    public static final int TYPE_GET_BIGGEST_SINGLE_PURCHASE = 5;
    public static final int TYPE_GET_BIGGEST_OVERALL_SPENDING = 6;
    public static void main( String[] args ) throws Exception {

        try (Connection connection = connectToDb()) {
            try (Reader setupSql = readFromClasspath("db-setup.sql")) {
                RunScript.execute(connection, setupSql);
            }
            int port = 1337;
            try (ServerSocket ss = new ServerSocket(port)) {
                System.out.println("Listening on :" + port);
                while (true) {
                    Socket socket = ss.accept();
                    RequestHandler handler = new RequestHandler(socket, connection);
                    new Thread(handler).start();
                }
            }
        }


    }
    private static Connection connectToDb() throws Exception {
        return DriverManager.getConnection("jdbc:h2:./server/db/tulemused");
    }

    private static InputStreamReader readFromClasspath(String name) throws Exception {
        ClassLoader cl = App.class.getClassLoader();
        return new InputStreamReader(cl.getResourceAsStream(name), "UTF-8");
    }
}
