package ar.edu.uade.programacion3.tateti;

import java.util.Scanner;

public class CmdLineTateti {

    public static void main(String[] args) {
        System.out.println("Bienvenido al juego Ta-Te-Ti");
        Scanner scanner = new Scanner(System.in).useDelimiter("[,\\s+]");

        Tateti tateti = Tateti.inicializar();
        tateti.turno(getPlayerFromStdIn(scanner));

        boolean ended = false;
        while (!ended) {
            System.out.print("Ingrese fila y columna: ");
            Move input = new Move(scanner.nextInt(), scanner.nextInt());
            ended = tateti.jugar(input);
        }
        scanner.close();
        System.out.println("Gracias por jugar");
    }

    private static Player getPlayerFromStdIn(Scanner scanner) {
        Player player = null;
        while (player == null) {
            System.out.print("Por favor elegir jugador para el primer turno j (jugador) c (computadora): ");
            String nextLine = scanner.nextLine();
            if (nextLine.equals("j")) {
                player = Player.HUMAN;
            }
            if (nextLine.equals("c")) {
                player = Player.COMPUTER;
            }
            System.out.println();
        }
        return player;
    }
}