import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdRandom;

import java.util.Arrays;

public class Board {
    private final int n;
    private final char[] blocks;
    private int blankpos;

    private int row(int p) {
        return (int) Math.ceil((double) p / (double) n);
    }

    private int col(int p) {
        if (p % n == 0) return n;
        return p % n;
    }

    public Board(int[][] blocks) {
        n = blocks.length;
        this.blocks = new char[n * n];
        int k = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                this.blocks[k] = (char) blocks[i][j];
                if (blocks[i][j] == 0) blankpos = k;
                k++;
            }
        }
    }

    public int dimension() {
        return n;
    }

    public int hamming() {
        int hamming = 0;
        for (int k = 0; k < n * n; k++) {
            if (blocks[k] == 0) continue;
            if (blocks[k] != k + 1) hamming++;
        }
        return hamming;
    }

    public int manhattan() {
        int manhattan = 0;
        for (int k = 0; k < n * n; k++) {
            if (blocks[k] == 0) continue;
            int rowdiff = Math.abs(row(blocks[k]) - row(k + 1));
            int coldiff = Math.abs(col(blocks[k]) - col(k + 1));
            manhattan = manhattan + rowdiff + coldiff;
        }
        return manhattan;
    }

    public boolean isGoal() {
        for (int k = 0; k < n * n - 2; k++) {
            if (blocks[k] > blocks[k + 1]) return false;
        }
        return true;
    }

    public Board twin() {
        boolean swapSuccess = false;
        char[] twin = blocks.clone();
        int k = 0;
        do {
            k = StdRandom.uniform(n * n);
        } while (blocks[k] == 0);

        while (swapSuccess == false) {
            int choice = StdRandom.uniform(4);
            switch (choice) {
                case 0:
                    if (row(k + 1) == 1) swapSuccess = false;
                    else if (twin[k - n] == 0) swapSuccess = false;
                    else {
                        swapAbove(twin, k);
                        swapSuccess = true;
                    }
                    break;
                case 1:
                    if (row(k + 1) == n) swapSuccess = false;
                    else if (twin[k + n] == 0) swapSuccess = false;
                    else {
                        swapBelow(twin, k);
                        swapSuccess = true;
                    }
                    break;
                case 2:
                    if (col(k + 1) == 1) swapSuccess = false;
                    else if (twin[k - 1] == 0) swapSuccess = false;
                    else {
                        swapLeft(twin, k);
                        swapSuccess = true;
                    }
                    break;
                case 3:
                    if (col(k + 1) == n) swapSuccess = false;
                    else if (twin[k + 1] == 0) swapSuccess = false;
                    else {
                        swapRight(twin, k);
                        swapSuccess = true;
                    }
                    break;
            }
        }
        Board twinBoard = new Board(toTwoDArray(twin));
        return twinBoard;
    }

    public boolean equals(Object y) {
        if (y == this) return true;
        if (y == null) return false;
        if (y.getClass() != this.getClass()) return false;
        Board that = (Board) y;
        if (!Arrays.equals(this.blocks, that.blocks)) return false;
        return true;
    }

    public Iterable<Board> neighbors() {
        Stack<Board> stackNeighbors = new Stack<Board>();
        char[] neighbor;
        if (row(blankpos + 1) != 1) {
            neighbor = blocks.clone();
            swapAbove(neighbor, blankpos);
            Board neighborBoard = new Board(toTwoDArray(neighbor));
            stackNeighbors.push(neighborBoard);
        }
        if (row(blankpos + 1) != n) {
            neighbor = blocks.clone();
            swapBelow(neighbor, blankpos);
            Board neighborBoard = new Board(toTwoDArray(neighbor));
            stackNeighbors.push(neighborBoard);
        }
        if (col(blankpos + 1) != 1) {
            neighbor = blocks.clone();
            swapLeft(neighbor, blankpos);
            Board neighborBoard = new Board(toTwoDArray(neighbor));
            stackNeighbors.push(neighborBoard);
        }
        if (col(blankpos + 1) != n) {
            neighbor = blocks.clone();
            swapRight(neighbor, blankpos);
            Board neighborBoard = new Board(toTwoDArray(neighbor));
            stackNeighbors.push(neighborBoard);
        }
        return stackNeighbors;
    }

    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(n + "\n");
        int k = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                s.append(String.format("%2d ", (int) blocks[k]));
                k++;
            }
            s.append("\n");
        }
        return s.toString();
    }

    private void swapAbove(char[] oneDArray, int k) {
        char temp = oneDArray[k];
        oneDArray[k] = oneDArray[k - n];
        oneDArray[k - n] = temp;
    }

    private void swapBelow(char[] oneDArray, int k) {
        char temp = oneDArray[k];
        oneDArray[k] = oneDArray[k + n];
        oneDArray[k + n] = temp;
    }

    private void swapLeft(char[] oneDArray, int k) {
        char temp = oneDArray[k];
        oneDArray[k] = oneDArray[k - 1];
        oneDArray[k - 1] = temp;
    }

    private void swapRight(char[] oneDArray, int k) {
        char temp = oneDArray[k];
        oneDArray[k] = oneDArray[k + 1];
        oneDArray[k + 1] = temp;
    }

    private int[][] toTwoDArray(char[] oneDArray) {
        int k = 0;
        int[][] blocks = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                blocks[i][j] = oneDArray[k];
                k++;
            }
        }
        return blocks;
    }

    public static void main(String[] args) {
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] blocks = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                blocks[i][j] = in.readInt();
            }
        }
        Board initial = new Board(blocks);
    }
}
