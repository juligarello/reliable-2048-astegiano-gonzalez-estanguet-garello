package ar.edu.unrc.game2048;

import java.util.Scanner;

/**
 * Command-line interface for the 2048 game.
 * 
 * This class handles user input and displays the board state.
 * It demonstrates how to use the Board class and its methods.
 */
public class MainCLI {
    private final Board board;
    private final Scanner scanner;

    public MainCLI() {
        this.board = new Board();
        this.scanner = new Scanner(System.in);
    }
    public MainCLI(String args){

        this.scanner = new Scanner(System.in);
        String linea = scanner.nextLine().trim();
        if(linea.startsWith("[") && linea.endsWith("]")){
            linea = linea.substring(1, linea.length() - 1).trim();
        }
        if(linea.isEmpty()){
            throw new IllegalArgumentException("El tablero no puede estar vacío");
        }
        if(linea.startsWith(",") || linea.endsWith(",") || linea.matches(".*,\\s*,.*")){
            throw new IllegalArgumentException("No se permiten celdas vacías");
        }

        String[] partes = linea.split("[,\\s]+");
        int cantidad = partes.length;
        int size = (int) Math.sqrt(cantidad);
        if(size * size != cantidad){
            throw new IllegalArgumentException(
                    "La cantidad de valores debe ser un cuadrado perfecto: " + cantidad);
        }
        if(size <= 0 || size >= 20){
            throw new IllegalArgumentException("Tamaño de tablero inválido: " + size);
        }

        this.board = new Board(size);

        for(int i = 0; i < cantidad; i++){
            String parte = partes[i];
            if(parte.isEmpty()){
                throw new IllegalArgumentException("No se permiten celdas vacías");
            }
            for(int j = 0; j < parte.length(); j++){
                char caracter = parte.charAt(j);
                if(caracter < '0' || caracter > '9'){
                    throw new IllegalArgumentException(
                            "El input solo puede contener números: " + caracter);
                }
            }
            int valor = Integer.parseInt(parte);
            if(!(valor >= 2 && (valor & (valor - 1)) == 0) && valor != 0){
                throw new IllegalArgumentException("Valor inválido para una celda: " + valor);
            }
            int fila = i / size;
            int columna = i % size;
            board.setCell(fila, columna, new Cell(valor));
        }

    }
    /**
     * Main game loop.
     */
    public void play() {
        System.out.println("=== 2048 Game ===");
        System.out.println("Controls: W(up), S(down), A(left), D(right), Q(quit)");
        System.out.println();

        while (true) {
            displayBoard();

            if (board.isWinningBoard()) {
                System.out.println("🎉 Congratulations! You reached 2048! 🎉");
                System.out.println("You can continue playing or quit.");
            }
            if (board.isLosingBoard()) {
                System.out.println("💀 Game Over! No more moves available. 💀");
                System.out.println("Final score: " + board.getScore());
                break;
            }

            System.out.print("Enter move: ");
            String input = scanner.nextLine().trim().toUpperCase();

            if (input.equals("Q")) {
                System.out.println("Thanks for playing! Final score: " + board.getScore());
                break;
            }

            boolean moved = false;
            switch (input) {
                case "W":
                    moved = board.moveUp();
                    break;
                case "S":
                    moved = board.moveDown();
                    break;
                case "A":
                    moved = board.moveLeft();
                    break;
                case "D":
                    moved = board.moveRight();
                    break;
                default:
                    System.out.println("Invalid input! Use W, A, S, D, or Q.");
                    continue;
            }

            assert board.repOk();

            if (moved) {
                System.out.println("Tile moved!");
            } else {
                System.out.println("No tiles moved. Try a different direction.");
            }

            System.out.println();
        }

        scanner.close();
    }

    /**
     * Displays the current board state.
     */
    private void displayBoard() {
        System.out.println(board);
    }

    public static void main(String[] args) {
        if (args.length > 0 && args[0].equalsIgnoreCase("s")) {
            MainCLI game = new MainCLI("");
            game.play();
        } else {
            MainCLI game = new MainCLI();
            game.play();
        }
    }
}
