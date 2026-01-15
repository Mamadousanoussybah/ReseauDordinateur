import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.InetAddress;

public class Server {
    public static void main(String[] args) throws IOException {
        // Créer un ServerSocket sur un port libre (0 signifie un port choisi automatiquement)
        ServerSocket serverSocket = new ServerSocket(0);
        
        // Récupérer l'adresse IP locale et le port du ServerSocket
        InetAddress localAddress = InetAddress.getLocalHost();
        int port = serverSocket.getLocalPort();
        
        // Afficher l'adresse IP et le port pour que le client sache comment se connecter
        System.out.println("Serveur en attente de connexions...");
        System.out.println("Adresse IP : " + localAddress.getHostAddress());
        System.out.println("Port : " + port);

        while (true) {
            // Accepter une connexion client
            Socket clientSocket = serverSocket.accept();
            System.out.println("Connexion établie avec un client.");

            // Créer un thread pour gérer le client
            ClientHandler clientHandler = new ClientHandler(clientSocket);
            new Thread(clientHandler).start();
        }
    }
}

// Classe pour gérer les clients dans un thread séparé
class ClientHandler implements Runnable {
    private final Socket clientSocket;

    public ClientHandler(Socket socket) {
        this.clientSocket = socket;
    }

    @Override
    public void run() {
        try {
            // Création des flux d'entrée et de sortie
            DataInputStream in = new DataInputStream(clientSocket.getInputStream());
            DataOutputStream out = new DataOutputStream(clientSocket.getOutputStream());

            // Communication avec le client
            while (true) {
                // Lire le message du client
                String clientMessage = in.readUTF();

                // Si le message est "exit", fermer la connexion
                if (clientMessage.equalsIgnoreCase("exit")) {
                    System.out.println("Un client a quitté.");
                    break;
                }

                // Réponse du serveur
                String response = "Hello " + clientMessage;
                System.out.println("Message reçu du client: " + clientMessage);

                // Envoyer la réponse au client
                out.writeUTF(response);
            }

            // Fermer les flux et le socket
            in.close();
            out.close();
            clientSocket.close();
        } catch (IOException e) {
            System.out.println("Erreur avec le client : " + e.getMessage());
        }
    }
}
