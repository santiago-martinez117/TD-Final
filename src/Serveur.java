import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Arrays;

public class Serveur {

    public static void main(String[] args) throws Exception {

        System.out.println("\nGénération clé");
        KeyGenerator keyGen = KeyGenerator.getInstance("DES");
        keyGen.init(56);
        Key key = keyGen.generateKey();
        System.out.println("Clé générée");
        Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
        System.out.println("\n" + cipher.getProvider().getInfo());

        ServerSocket socketserv = new ServerSocket(6020);
        // boucle du serveur
        while (true){
            char[] code = new char[1024];
            String Texte = Arrays.toString(code);
            //Attend une connexion
            Socket socketclient = socketserv.accept();
            //Lit un message transmis en paramètre
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(socketclient.getInputStream()));
            //Permet d'écrire sous forme de string dans un fichier
            PrintWriter out = new PrintWriter(
                    new BufferedWriter(new OutputStreamWriter(socketclient.getOutputStream())), true);
            Texte = in.readLine();
            //Debut du cryptage
            System.out.println("Le message a coder : " + Texte);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] cipherTexte = cipher.doFinal(Texte.getBytes(StandardCharsets.UTF_8));
            System.out.println("Le message codé : " + new String(cipherTexte, StandardCharsets.UTF_8));
            //Fin du cryptage et début du décryptage
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] TexteV2 = cipher.doFinal(cipherTexte);
            System.out.println("Le message décodé : ");
            System.out.println(new String(TexteV2, StandardCharsets.UTF_8));
            //Fin du décryptage et envoie du code
            System.out.println("Start");
            out.write(Arrays.toString(TexteV2));
            System.out.println("Reçu");
            socketclient.close();
        }
    }
}