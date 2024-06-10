import chess.*;
import client.ServerFacade;
import ui.PreLogin;

public class Main {
    public static void main(String[] args) {
        var newFacade =  new ServerFacade("localhost", "8080");
        var preloginUI = new PreLogin(newFacade);
        preloginUI.display();
        var piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        System.out.println("♕ 240 Chess Client: " + piece);
    }
}