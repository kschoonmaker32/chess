import chess.*;
import client.ServerFacade;
import client.WebSocketFacade;
import ui.PreLogin;

public class Main {
    public static void main(String[] args) {
        var newServerFacade =  new ServerFacade("localhost", "8080");
        var newWSFacade = new WebSocketFacade("ws://localhost:8080/ws"); // check here
        var preloginUI = new PreLogin(newServerFacade, newWSFacade);
        preloginUI.display();
        var piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        System.out.println("♕ 240 Chess Client: " + piece);
    }
}