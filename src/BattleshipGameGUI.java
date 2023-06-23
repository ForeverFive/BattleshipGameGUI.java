import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class BattleshipGameGUI {
    private JFrame frame;
    private JButton[][] buttons;
    private char[][] playerBoard;
    private char[][] computerBoard;
    private int shipCount;
    private GameState gameState;

    private enum GameState {
        PLAYER_SETUP,
        COMPUTER_SETUP,
        PLAYER_TURN,
        COMPUTER_TURN,
        GAME_OVER
    }

    public BattleshipGameGUI() {
        frame = new JFrame("Морський бій");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 500);
        frame.setLayout(new GridLayout(10, 10));

        buttons = new JButton[10][10];
        playerBoard = new char[10][10];
        computerBoard = new char[10][10];
        shipCount = 14;
        gameState = GameState.PLAYER_SETUP;

        initializeButtons();
        frame.setVisible(true);
    }

    private void initializeButtons() {
        for (int row = 0; row < 10; row++) {
            for (int col = 0; col < 10; col++) {
                JButton button = new JButton();
                button.setBackground(Color.BLUE);
                button.addActionListener(new ButtonListener(row, col));
                buttons[row][col] = button;
                frame.add(button);
            }
        }
    }

    private void placePlayerShips() {
        JOptionPane.showMessageDialog(null, "Гравець 1: Розмістіть свої кораблі.");
        while (shipCount > 0) {
            String input = JOptionPane.showInputDialog("Введіть координати корабля (наприклад, A1):");
            if (input != null) {
                if (input.length() == 2) {
                    char[] chars = input.toCharArray();
                    char rowChar = Character.toUpperCase(chars[0]);
                    int row = rowChar - 'A';
                    int col = Integer.parseInt(String.valueOf(chars[1])) - 1;
                    if (isValidCoordinate(row, col)) {
                        if (playerBoard[row][col] != 'S') {
                            playerBoard[row][col] = 'S';
                            buttons[row][col].setBackground(Color.GREEN);
                            shipCount--;
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "Неприпустимі координати!");
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Неправильний формат координат!");
                }
            } else {
                JOptionPane.showMessageDialog(null, "Розміщення кораблів гравцем 1 відмінено.");
                System.exit(0);
            }
        }

        gameState = GameState.COMPUTER_SETUP;
        placeComputerShips();
    }

    private void placeComputerShips() {
        JOptionPane.showMessageDialog(null, "Гравець 2 (комп'ютер): Розмістіть свої кораблі.");
        while (shipCount > 0) {
            int row = (int) (Math.random() * 10);
            int col = (int) (Math.random() * 10);
            if (isValidCoordinate(row, col)) {
                if (computerBoard[row][col] != 'S') {
                    computerBoard[row][col] = 'S';
                    shipCount--;
                }
            }
        }

        gameState = GameState.PLAYER_TURN;
        JOptionPane.showMessageDialog(null, "Починається гра!");
    }

    private boolean isValidCoordinate(int row, int col) {
        return row >= 0 && row < 10 && col >= 0 && col < 10;
    }

    private class ButtonListener implements ActionListener {
        private int row;
        private int col;

        public ButtonListener(int row, int col) {
            this.row = row;
            this.col = col;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (gameState == GameState.PLAYER_TURN) {
                if (computerBoard[row][col] == 'S') {
                    buttons[row][col].setBackground(Color.RED);
                    computerBoard[row][col] = 'H';
                    shipCount--;
                    if (shipCount == 0) {
                        gameState = GameState.GAME_OVER;
                        JOptionPane.showMessageDialog(null, "Гравець 1 переміг!");
                        System.exit(0);
                    }
                } else if (computerBoard[row][col] == '-') {
                    buttons[row][col].setBackground(Color.WHITE);
                    computerBoard[row][col] = 'M';
                    gameState = GameState.COMPUTER_TURN;
                    computerTurn();
                }
            } else if (gameState == GameState.COMPUTER_TURN) {
                JOptionPane.showMessageDialog(null, "Зачекайте, зараз хід комп'ютера.");
            }
        }

        private void computerTurn() {
            int row, col;
            do {
                row = (int) (Math.random() * 10);
                col = (int) (Math.random() * 10);
            } while (!isValidCoordinate(row, col) || playerBoard[row][col] == 'H' || playerBoard[row][col] == 'M');

            if (playerBoard[row][col] == 'S') {
                buttons[row][col].setBackground(Color.RED);
                playerBoard[row][col] = 'H';
                shipCount--;
                if (shipCount == 0) {
                    gameState = GameState.GAME_OVER;
                    JOptionPane.showMessageDialog(null, "Гравець 2 (комп'ютер) переміг!");
                    System.exit(0);
                }
            } else if (playerBoard[row][col] == '-') {
                buttons[row][col].setBackground(Color.WHITE);
                playerBoard[row][col] = 'M';
                gameState = GameState.PLAYER_TURN;
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                BattleshipGameGUI game = new BattleshipGameGUI();
                game.placePlayerShips();
            }
        });
    }
}
