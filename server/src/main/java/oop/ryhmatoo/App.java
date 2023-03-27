package oop.ryhmatoo;


import java.net.ServerSocket;
import java.net.Socket;

public class App
{
    public static final int TYPE_UPLOAD = 1;
    public static final int TYPE_GET_LEADERBOARD = 2;
    public static final int TYPE_OK = 3;
    public static final int TYPE_ERROR = 4;
    public static void main( String[] args ) throws Exception
    {
        int port = 1337;
        try (ServerSocket ss = new ServerSocket(port)) {
            System.out.println("Listening on :" + port);
            while (true) {
                Socket socket = ss.accept();
                RequestHandler handler = new RequestHandler(socket);
                new Thread(handler).start();
            }
        }
    }
}
