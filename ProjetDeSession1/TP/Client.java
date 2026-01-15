import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) throws IOException {
        // Demande à l'utilisateur l'adresse IP et le port du serveur
        Scanner sc = new Scanner(System.in);

        System.out.print("Entrez l'adresse IP du serveur : ");
        String serverIP = sc.nextLine();

        System.out.print("Entrez le port du serveur : ");
        int serverPort = sc.nextInt();
        sc.nextLine();  // Consommer la nouvelle ligne restante après nextInt()

        // Création d'un socket pour se connecter au serveur
        Socket client = new Socket(serverIP, serverPort);

        // Demande à l'utilisateur son nom
        System.out.println("Nom client:");
        String nomClient = sc.nextLine();

        // Envoi du nom du client au serveur
        DataOutputStream out = new DataOutputStream(client.getOutputStream());
        out.writeUTF(nomClient);

        // Réception du message du serveur
        DataInputStream in = new DataInputStream(client.getInputStream());
        String s1 = in.readUTF();
        System.out.println(s1);

        // Interaction continue avec le serveur
        while (true) {
            // Demande d'un message à l'utilisateur
            System.out.println("Entrez un message à envoyer au serveur (ou 'exit' pour quitter) : ");
            String message = sc.nextLine();
            if (message.equalsIgnoreCase("exit")) {
                break;
            }

            // Envoi du message au serveur
            out.writeUTF(message);

            // Réception de la réponse du serveur
            String response = in.readUTF();
            System.out.println("Réponse du serveur: " + response);
        }

        // Fermeture des flux et du socket
        sc.close();
        in.close();
        out.close();
        client.close();
    }
}
