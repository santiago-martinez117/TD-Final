import java.net.Socket;

/*
        Travail à faire et à rendre :
        1 – Un schéma de l’architecture du chat
        2 – Des explications sur les choix et justifications des algorithmes de chiffrage que vous allez
        mettre en œuvre
        3 – Le code commenté de vos programmes
        Bon travail.
        Remarque : En principe vous avez tous les éléments techniques, dans les TD réseaux et Prog
        répartie, pour traiter sans difficulté ce TD
*/

/*Explication du schéma du chat.
        Le chat sera composé de deux programmes indépendants qui vont s’échanger des messages
        saisis au clavier.
        Les programmes communiquent via internet, il faudra donc utiliser l’interface socket pour les classes Chat, Serveur et Client
        Les messages échangés devront être chiffrés, pour que l’on ne puisse pas comprendre leur
        contenu en cas d’écoute.
        Le premier programme qui sera lancé sera considéré comme le serveur et devra se mettre à
        l’écoute sur le port 22222.
        Tout d'abord la classe Client générera une clé 'DES' publique et une privée.
        Ensuite la classe Serveur utilisera une clé 'DES' chiffrée qui prendra en entrée une clé publique et renverra une clé 'DES' codée
        Enfin le client pourra décrypter le message grace a la clé privée
 */
public class Chat implements Runnable {
    private Socket socket;

    public Chat(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        
        // Code de la méthode à compléter

    }
}
