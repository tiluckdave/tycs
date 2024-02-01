import java.util.*;

public class Main {
    private int[] board = new int[9];
    private int[] magicSquare = { 2, 7, 6, 9, 5, 1, 4, 3, 8 };
    private Random random = new Random();

    public void playGame() {
        for (int i = 0; i < 9; i++) {
            if (i % 2 == 0) {
                computerMove();
            } else {
                humanMove();
            }
            printBoard();
            if (checkWin()) {
                System.out.println("Game Over. Winner: " + (i % 2 == 0 ? "Computer" : "Human"));
                return;
            }
        }
        System.out.println("Game Over. It's a draw.");
    }

    private void computerMove() {
        if (Arrays.stream(board).filter(i -> i != 0).count() < 4) {
            int move;
            do {
                move = random.nextInt(9);
            } while (board[move] != 0);
            board[move] = 1;
        } else {
            int move = findBestMove(1);
            if (move == -1) {
                move = findBestMove(-1);
                if (move == -1) {
                    do {
                        move = random.nextInt(9);
                    } while (board[move] != 0);
                }
            }
            board[move] = 1;
        }
    }

    private int findBestMove(int player) {
        for (int i = 0; i < 9; i++) {
            if (board[i] == 0) {
                int sum = 0;
                for (int j = 0; j < 9; j++) {
                    if (board[j] == player)
                        sum += magicSquare[j];
                }
                if (15 - sum == magicSquare[i])
                    return i;
            }
        }
        return -1;
    }

    private void humanMove() {
        Scanner scanner = new Scanner(System.in);
        int move;
        do {
            System.out.println("Enter your move (1-9): ");
            move = scanner.nextInt() - 1;
        } while (move < 0 || move >= 9 || board[move] != 0);
        board[move] = -1;
    }

    private boolean checkWin() {
        int[][] lines = {
                { 0, 1, 2 }, { 3, 4, 5 }, { 6, 7, 8 }, // rows
                { 0, 3, 6 }, { 1, 4, 7 }, { 2, 5, 8 }, // columns
                { 0, 4, 8 }, { 2, 4, 6 } // diagonals
        };

        for (int[] line : lines) {
            if (board[line[0]] != 0 &&
                    board[line[0]] == board[line[1]] &&
                    board[line[0]] == board[line[2]]) {
                return true;
            }
        }
        return false;
    }

    private void printBoard() {
        for (int i = 0; i < 9; i++) {
            System.out.print(board[i] == 1 ? "X " : board[i] == -1 ? "O " : i + 1 + " ");
            if (i % 3 == 2)
                System.out.println();
        }
    }

    public static void main(String[] args) {
        new Main().playGame();
    }
}
