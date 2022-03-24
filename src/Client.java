import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.util.Arrays;
import java.util.Objects;


public class Client {
    // Génère un objet KeyPair contenant une clé publique et une clé privée pour un cryptage RSA
    public static KeyPair generecleRSA(){
        KeyPairGenerator keyGen = null;
        try {   keyGen = KeyPairGenerator.getInstance("RSA");
        } catch (NoSuchAlgorithmException e) {   e.printStackTrace(); }
        assert keyGen != null;
        keyGen.initialize(1024);
        return keyGen.generateKeyPair();
    }

    // Cryptage RSA
    public static byte[] crypteRSA(byte[] message, PublicKey clePublic) throws Exception{

        Cipher cipher = null;
        byte[] messageCode = null;
        try { cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, clePublic);
            messageCode = cipher.doFinal(message);
        } catch (NoSuchAlgorithmException e) {e.printStackTrace();}
        return messageCode;

    }

    // Décryptage RSA
    public static byte[] decrypteRSA(byte[] message, PrivateKey clePrive) throws Exception {
        Cipher cipher = null;
        byte[] messageCode = null;
        try { cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.DECRYPT_MODE, clePrive);
            messageCode = cipher.doFinal(message);
        } catch (NoSuchAlgorithmException e) { e.printStackTrace(); }
        return messageCode;
    }
    public static void main(String[] args) throws Exception {
        Socket socketclient = new Socket("localhost", 6020);
        BufferedReader in = new BufferedReader(
                new InputStreamReader(socketclient.getInputStream()));
        OutputStream out = socketclient.getOutputStream();
        PrintWriter writer = new PrintWriter(out, true);
        while (true){
            char[] mess = new char[1024];
            // Création d'une clé DSE provenant normalement du serveur
            KeyGenerator keyGen = KeyGenerator.getInstance("DES");
            keyGen.init(56);
            Key key = keyGen.generateKey();
            // Décryptage du DSE provenant du serveur
            byte[] messDSE = Arrays.toString(mess).getBytes(StandardCharsets.UTF_8);
            Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] Texte = cipher.doFinal(messDSE);
            out.write(Arrays.toString(mess).getBytes(StandardCharsets.UTF_8),0,1024);
            int lg=in.read(mess,0,1024);
            // Les clés RSA à générés
            KeyPair keyRSA = generecleRSA();
            String TexteV2 = "Message a crypter" + Arrays.toString(Texte);
            // Le message à coder
            System.out.println("\nLa message a coder : "+ TexteV2);
            // Codage du RSA public a envoyé au serveur pour qu'il génère en codeDSE
            byte[] code = crypteRSA(TexteV2.getBytes(StandardCharsets.UTF_8), keyRSA.getPublic());
            System.out.println("\nLe message code : "+ TexteV2);
            // Décodage du RSA public avec la clé RSA privée et affichage du message
            byte[] decode = decrypteRSA(code, keyRSA.getPrivate());
            writer.println("message "+ Arrays.toString(decode) + " de taille : " + lg);
            if (Objects.equals(writer.toString(), "stop")) {
                writer.println("stop");
                break;

            }
        }
        socketclient.close();
        out.close();
        writer.close();
    }

}
