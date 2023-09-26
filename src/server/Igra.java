package server;

public class Igra {
	public Igrac playerA;
	public Igrac playerB;
	Igrac currentPlayer;
	private Igrac[] board= {
			null,null,null,null,null,null,null,
			null,null,null,null,null,null,null,
			null,null,null,null,null,null,null,
			null,null,null,null,null,null,null,
			null,null,null,null,null,null,null,
			null,null,null,null,null,null,null
			,null,null,null,null,null,null};
	public synchronized int legalMove(int location,Igrac player)
	{
		int minlocation = (location % 8)+8*5;
        for(int i = minlocation ; i >= location ; i-= 8)
            if (player == currentPlayer && board[i] == null) {
                board[i] = currentPlayer;
                currentPlayer = currentPlayer.opponent;
                currentPlayer.otherPlayerMoved(i);
                return i;
            }
        return -1;
	}
	 public boolean boardFilledUp() {
	        for (int i = 0; i < board.length; i++) {
	            if (board[i] == null) {
	                return false;
	            }
	        }
	        return true;
	    }
	 public  boolean isWinner() {
         // horizontalCheck 
        for (int j = 0 ; j< 9-4 ; j++){//column
            for (int i = 0 ; i < 48 ; i+=8){//row
                if (    board[i + j]!= null && board[i +j]== board[i +j+1] && board[i +j] == board[i+j+2] && board[i +j] ==  board[i+j+3]){
                return true;
                }
            }
        }
        // verticalCheck
        for (int i = 0 ; i< 24 ; i+=8){
            for (int j = 0 ; j < 9 ; j++){
                if (    board[i  + j]!= null && board[i +j]== board[i+8 +j] && board[i +j] == board[i+(16) +j] && board[i +j] ==  board[i+(24) +j]){
                return true;
                }
            }
        }
        // ascendingDiagonalCheck 
        for (int i = 24 ; i< 48 ; i+=8){
            for (int j = 0 ; j <4 ; j++){
                if (    board[i + j]!= null && board[i +j]== board[(i-8) +j+1] && board[(i-8) +j+1] == board[i-16 +j+2] && board[(i-16) +j+2] ==  board[(i-24) +j+3]){
                return true;
                }
            }
        }
        // descendingDiagonalCheck
        for (int i = 24 ; i< 48 ; i+=8){
            for (int j = 3 ; j < 8; j++){
                if (    board[i  + j]!= null && board[i +j]== board[(i-8) +j-1 ] && board[(i-8) +j-1 ] == board[(i-16) +j-2] && board[(i-16) +j-2] ==  board[(i-24) +j-3]){
                return true;
                }
            }
        }
        
        return false;
    }

}
