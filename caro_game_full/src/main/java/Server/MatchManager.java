package Server;

import Game.Board;
import Game.GameLogic;
import Game.TurnManager;
import Game.WinChecker;
import Model.*;
import service.GameService;

public class MatchManager {
    private final String roomId;
    private final ClientHandler hostHandler;
    private final ClientHandler guestHandler;
    private final TurnManager turnManager;
    private final Board board;
    private boolean gameOver = false;
    private int moveCount = 0;

    private static final GameService gameService = new GameService();

    public MatchManager(String roomId, ClientHandler host, ClientHandler guest) {
        this.roomId = roomId;
        this.hostHandler = host;
        this.guestHandler = guest;
        this.turnManager = new TurnManager(host.getUsername(), guest.getUsername());
        this.board = new Board(20);
    }

    public synchronized void handleMove(ClientHandler sender, int x, int y) {
        if (gameOver) return;

        String username = sender.getUsername();
        if (!turnManager.isCurrentPlayer(username)) {
            Message invalid = new Message(MessageType.MOVE_INVALID);
            invalid.setContent("Chưa đến lượt của bạn!");
            sender.sendMessage(invalid);
            return;
        }

        String symbol = turnManager.getSymbol(username);
        if (!board.setMove(x, y, symbol)) {
            Message invalid = new Message(MessageType.MOVE_INVALID);
            invalid.setContent("Ô đã được đánh!");
            sender.sendMessage(invalid);
            return;
        }

        moveCount++;

        // Relay nước đi sang đối thủ
        Message moveMsg = new Message(MessageType.MOVE);
        moveMsg.setSender(username);
        moveMsg.setSymbol(symbol);
        moveMsg.setX(x);
        moveMsg.setY(y);
        getOpponent(sender).sendMessage(moveMsg);

        // Kiểm tra thắng
        if (GameLogic.checkWin(board, x, y, symbol)) {
            endGame(username);
            return;
        }

        // Kiểm tra hòa
        if (WinChecker.isBoardFull(board)) {
            endGame(null);
            return;
        }

        turnManager.switchTurn();
    }

    private void endGame(String winner) {
        gameOver = true;
        RoomManager.setFinished(roomId);

        Message gameOverMsg = new Message(MessageType.GAME_OVER);
        gameOverMsg.setContent(winner != null ? winner + " đã thắng!" : "Hòa!");
        gameOverMsg.setSender(winner != null ? winner : "DRAW");

        hostHandler.sendMessage(gameOverMsg);
        guestHandler.sendMessage(gameOverMsg);
        hostHandler.setMatchManager(null);
        guestHandler.setMatchManager(null);

        // Lưu kết quả vào DB (bất đồng bộ)
        final String w = winner;
        final int moves = moveCount;
        new Thread(() -> gameService.recordResult(
                hostHandler.getUsername(), guestHandler.getUsername(), w, moves
        )).start();

        System.out.println("[GAME OVER] " + roomId + " | Người thắng: " + (winner != null ? winner : "Hòa"));
    }

    public void handlePlayerDisconnect(ClientHandler disconnected) {
        if (gameOver) return;
        gameOver = true;
        ClientHandler remaining = getOpponent(disconnected);
        if (remaining != null) {
            Message quitMsg = new Message(MessageType.QUIT);
            quitMsg.setContent("Đối thủ mất kết nối. Bạn thắng!");
            remaining.sendMessage(quitMsg);
            gameService.recordResult(
                    hostHandler.getUsername(), guestHandler.getUsername(),
                    remaining.getUsername(), moveCount
            );
        }
        RoomManager.removeRoom(roomId);
    }

    private ClientHandler getOpponent(ClientHandler me) {
        return me == hostHandler ? guestHandler : hostHandler;
    }
}
