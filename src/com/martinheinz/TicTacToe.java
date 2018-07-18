package com.martinheinz;

import java.util.Arrays;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

public class TicTacToe {

    public static void main(String[] args) {
        TicTacToe t = new TicTacToe();
        if (args[0].equals("1")) {
            t.runNormalMode();
        }
        else if (args[0].equals("0")) {
            t.runBotMode();
        }
    }

    public enum STATE {BLANK, CROSS, CIRCLE}
    public enum TURN {X, Y}

    public STATE[][] getGameBoard() {
        return gameBoard;
    }

    private STATE[][] gameBoard = new STATE[3][3];
    private TURN playerOnTurn;

    public TicTacToe() {
        for (int x = 0; x < gameBoard.length; x++) {
            for (int y = 0; y < gameBoard[x].length; y++) {
                gameBoard[x][y] = STATE.BLANK;
            }
        }
        playerOnTurn = TURN.X;
    }

    public TURN getPlayerOnTurn() {
        return playerOnTurn;
    }

    private char getStateChar(STATE s) {
        if (s.equals(STATE.BLANK)) {
            return ' ';
        }
        else if (s.equals(STATE.CIRCLE)) {
            return 'O';
        }
        return 'X';
    }

    private String getCellDivider() {
        return "|";
    }

    private String getLineDivider() {
        return "-+-+-\n";
    }

    public String getBoardState() {
        StringBuilder boardState = new StringBuilder();
        boardState.append(getBoardLine(0));
        for (int x = 1; x < gameBoard.length; x++) {
            boardState.append(getLineDivider());
            boardState.append(getBoardLine(x));
        }
        return boardState.toString();
    }

    private String getBoardLine(int x) {
        return getStateChar(gameBoard[x][0]) + getCellDivider() +getStateChar(gameBoard[x][1]) + getCellDivider() + getStateChar(gameBoard[x][2]) + '\n';
    }

    private void printGameStart() {
        System.out.println("Game Board Creation...");
        System.out.println(getBoardState());
        System.out.println("Board Created.");
        System.out.println("The game will start with player X");
    }

    public void runNormalMode() {
        printGameStart();
        Scanner scanner = new Scanner(System.in);
        while (true) {
            String line = scanner.nextLine();
            String[] strArray = line.split("\\s+");
            int[] intArray = new int[strArray.length];
            try {
                for(int i = 0; i < strArray.length; i++) {
                    intArray[i] = Integer.parseInt(strArray[i]);
                }
                if (intArray.length == 2) {
                    int x = intArray[0];
                    int y = intArray[1];
                    if (x <= 2 && x >= 0 && y <= 2 && y >= 0 && getGameBoard()[x][y] == STATE.BLANK) {
                        setCell(x, y);
                        printPlayerTurn();
                        System.out.println(getBoardState());
                        if (checkWinner() == STATE.CROSS) {
                            System.out.println("PLAYER X WON!");
                            return;
                        }
                        else if (checkWinner() == STATE.CIRCLE) {
                            System.out.println("PLAYER O WON!");
                            return;
                        }
                        else if (checkForDraw()) {
                            System.out.println("GAME ENDS WITH A DRAW!");
                            return;
                        }
                    }
                    else {
                        if (x > 2 || x < 0 || y > 2 || y < 0) {
                            System.out.println("Input position out of bounds... \n Please try again");
                        }
                        else if (getGameBoard()[x][y] != STATE.BLANK) {
                            System.out.println("This cell is already filled... \n Please try again");
                        }

                    }
                }
                else {
                    System.out.println("Wrong input... \n Please try again");
                }
            }
            catch (NumberFormatException ex) {
                System.out.println("Wrong input... \n Please try again");
            }
        }
    }

    private void printPlayerTurn() {
        if (getPlayerOnTurn() == TURN.X) {
            System.out.println("Player X:");
        }
        else {
            System.out.println("Player O:");
        }
    }

    public void runBotMode() {
        printGameStart();
        while (checkWinner() == STATE.BLANK && !checkForDraw()) {
            pause();
            int randomX = ThreadLocalRandom.current().nextInt(0, 3);
            int randomY = ThreadLocalRandom.current().nextInt(0, 3);
            while (gameBoard[randomX][randomY] != STATE.BLANK) {
                randomX = ThreadLocalRandom.current().nextInt(0, 3);
                randomY = ThreadLocalRandom.current().nextInt(0, 3);
            }
            setCell(randomX, randomY);
            printPlayerTurn();
            System.out.println(getBoardState());
            if (checkWinner() == STATE.CROSS) {
                System.out.println("PLAYER X WON!");
                return;
            }
            else if (checkWinner() == STATE.CIRCLE) {
                System.out.println("PLAYER O WON!");
                return;
            }
            else if (checkForDraw()) {
                System.out.println("GAME ENDS WITH A DRAW!");
                return;
            }
        }
    }

    private void pause() {
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public boolean setCell(int x, int y) {
        if (x <= 2 && x >= 0 && y <= 2 && y >= 0 && getGameBoard()[x][y] == STATE.BLANK) {
            if (playerOnTurn == TURN.X) {
                getGameBoard()[x][y] = STATE.CROSS;
                playerOnTurn = TURN.Y;
            }
            else {
                getGameBoard()[x][y] = STATE.CIRCLE;
                playerOnTurn = TURN.X;
            }
            return true;
        }

        return false;
    }

    public STATE checkWinner() {
        STATE col = checkForThreeInCol();
        STATE row = checkForThreeInRow();
        STATE diag = checkForThreeInDiag();
        if (Arrays.asList(new STATE[] {col, row, diag}).contains(STATE.CROSS)) {
            return STATE.CROSS;
        }
        else if (Arrays.asList(new STATE[] {col, row, diag}).contains(STATE.CIRCLE)) {
            return STATE.CIRCLE;
        }
        return STATE.BLANK;
    }

    public boolean checkForDraw() {
        for (int x = 0; x < gameBoard.length; x++) {
            for (int y = 0; y < gameBoard[x].length; y++) {
                if (gameBoard[x][y] == STATE.BLANK) {
                    return false;
                }
            }
        }
        return true;
    }

    public STATE checkForThreeInCol() {
        for (int x = 0; x < gameBoard[0].length; x++) {
            STATE[] col = getColumn(x);
            boolean flag = true;
            for(int i = 1; i < col.length && flag; i++){
                if (col[i] != col[0] || col[i] == STATE.BLANK) {
                    flag = false;
                }
            }
            if (flag) {
                return col[0];
            }
        }
        return STATE.BLANK;
    }

    public STATE checkForThreeInRow() {
        for (int x = 0; x < gameBoard.length; x++) {
            boolean flag = true;
            for (int y = 1; y < gameBoard[x].length; y++) {
                if (gameBoard[x][y] != gameBoard[x][0] || gameBoard[x][y] == STATE.BLANK) {
                    flag = false;
                }
            }
            if (flag) {
                return gameBoard[x][0];
            }
        }
        return STATE.BLANK;
    }

    public STATE checkForThreeInDiag() {
        if (gameBoard[0][0] == gameBoard[1][1] && gameBoard[1][1] == gameBoard[2][2] && gameBoard[0][0] != STATE.BLANK) {
            return gameBoard[0][0];
        }
        if (gameBoard[0][2] == gameBoard[1][1] && gameBoard[1][1] == gameBoard[2][0] && gameBoard[0][2] != STATE.BLANK) {
            return gameBoard[0][2];
        }
        return STATE.BLANK;
    }

    private STATE[] getColumn(int index) {
        STATE[] column = new STATE[gameBoard.length];
        for(int i = 0; i < column.length; i++){
            column[i] = gameBoard[i][index];
        }
        return column;
    }
}
