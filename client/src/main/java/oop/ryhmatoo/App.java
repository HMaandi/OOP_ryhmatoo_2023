package oop.ryhmatoo;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * Hello world!
 *
 */
public class App {
    private Isik edastatavIsik;

    public App(Isik edastatavIsik) {
        this.edastatavIsik = edastatavIsik;
    }
    public String isikReaks(Isik isik){
        return isik.getIsikukood() + "\\|" + isik.getEesnimi() + " " + isik.getPerekonnanimi() + "\\|" + isik.arvutaTulemus();
    }

    public void main(String[] args) throws IOException {
        int port = 1337;
        System.out.println("ühendan serveriga");
        try (Socket socket = new Socket("localhost", port)) {
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            out.writeUTF(isikReaks(edastatavIsik));
        }
    }
}
