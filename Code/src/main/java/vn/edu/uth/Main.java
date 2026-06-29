<<<<<<< HEAD
package vn.edu.uth;

public class Main {

    public static void main(String[] args) {

        System.out.println("Hello and welcome!");
    }
}
=======
package vn.edu.uth;

import Client.CaroClient;
import Server.CaroServer;
import javax.swing.SwingUtilities;

/**
 * Entry point tổng hợp.
 * Chạy Server: java -cp ... Server.CaroServer
 * Chạy Client: java -cp ... Client.CaroClient
 *             hoặc java -cp ... vn.edu.uth.Main --client
 */
public class Main {
    public static void main(String[] args) {
        if (args.length > 0 && args[0].equals("--server")) {
            CaroServer.main(args);
        } else {
            SwingUtilities.invokeLater(CaroClient::new);
        }
    }
}
>>>>>>> main
