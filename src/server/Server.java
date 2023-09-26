package server;

import java.net.InetAddress;
import java.net.ServerSocket;
import java.util.ArrayList;


public class Server {
	public static ArrayList<Igra> games=new ArrayList<>();
	public static void main(String[] args) throws Exception {
        int port = 8901 , backlog = 5;
        String ip = "127.0.0.1";
        ServerSocket listener = new ServerSocket(port,3,InetAddress.getLocalHost());
        //listener.bind(SocketAddress);
        System.out.println("Connect Four Server is Running");
        //listener.getInetAddress();
		System.out.println("Adress: "+InetAddress.getLocalHost());
        System.out.println("port: "+listener.getLocalSocketAddress());
        try {
            while (true) {
                Igra game = new Igra();
                Igrac playerX = new Igrac("crveni", listener.accept());
                Igrac playerO = new Igrac("zuti", listener.accept());
                playerX.setOpponent(playerO);
                playerO.setOpponent(playerX);
                playerX.setIgraMark(games.size());
                playerO.setIgraMark(games.size());
                game.currentPlayer = playerX;
                game.playerA=playerX;
                game.playerB=playerO;
                games.add(game);
                playerX.start();
                playerO.start();
            }
        } finally {
            listener.close();
        }
    }
}

