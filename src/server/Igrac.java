package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Igrac extends Thread{
	int igraMark;
	String playerName;
	Igrac opponent;
	public void setOpponent(Igrac opponent) {
		this.opponent = opponent;
	}
	Socket socket;
	BufferedReader input;
	PrintWriter output;
	public Igrac(String playerName,Socket socket)
	{
		this.playerName=playerName;
		this.socket=socket;
		try {
			input=new BufferedReader(new InputStreamReader(socket.getInputStream()));
			output=new PrintWriter(socket.getOutputStream(),true);
			output.println("WELCOME "+playerName);
			output.println("MSG Waiting for opponent");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("Igrac riknuo");
		}
	}
	public void run() {
        try {
            output.println("MSG All players connected");
            if (playerName.equals("crveni")) {
                output.println("MSG Your move");
            }

            //Repeatedly get commands from the client and process them.
            while (true) {
                String command = input.readLine();
                if (command == null) {
                    // Handle the case where the input stream is closed (e.g., the opponent disconnected).
                    return;
                }
                if (command.startsWith("MOV")) {
                    int location = Integer.parseInt(command.substring(4));
                    int validlocation = Server.games.get(igraMark).legalMove(location, this);
                    if (validlocation!= -1) {
                        output.println("VALMOV"+validlocation);
                        output.println(Server.games.get(igraMark).isWinner() ? "VICTORY"
                                     : Server.games.get(igraMark).boardFilledUp() ? "TIE"
                                     : "");
                    } else {
                        output.println("MSG ?");
                    }
                } else if (command.startsWith("QUIT")) {
                    return;
                }
            }
        } catch (IOException e) {
            System.out.println("Player died: " + e);
        } finally {
            try {socket.close();} catch (IOException e) {}
        }
    }
	public void otherPlayerMoved(int location) {
            output.println("OPMOV " + location);
            output.println(
                Server.games.get(igraMark).isWinner() ? "DEFEAT" : Server.games.get(igraMark).boardFilledUp() ? "TIE" : "");
        }
	public int getIgraMark() {
		return igraMark;
	}
	public void setIgraMark(int igraMark) {
		this.igraMark = igraMark;
	}
	
}
