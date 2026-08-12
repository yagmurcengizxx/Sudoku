import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Sudoku {

    class Tile extends JButton {
        int a;
        int b;

        public Tile(int a, int b) {
            this.a = a;
            this.b = b;
        }
    }

    String[] puzzle = {
            "-38---254",
            "--1--26-7",
            "-7--6-9-3",
            "7---1--3-",
            "5----41-2",
            "1236-----",
            "817-26---",
            "3948--7-6",
            "2-54-----"
    };

    String[] solution = {
            "638197254",
            "951342687",
            "472568913",
            "746219538",
            "589734162",
            "123685479",
            "817926345",
            "394851726",
            "265473891"
    };

    int boardWidth = 600;
    int boardHeight = 650;

    JFrame frame = new JFrame("Sudoku");
    JLabel textLabel = new JLabel();
    JPanel textPanel = new JPanel();
    JPanel boardPanel = new JPanel();
    JPanel buttonsPanel = new JPanel();
    JButton resetButton = new JButton("Reset");

    JButton numSelected = null;
    Tile[][] board = new Tile[9][9];

    int error = 0;

    public Sudoku() {
        frame.setSize(boardWidth, boardHeight);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new BorderLayout());

        textPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10));

        textLabel.setFont(new Font("Monospaced", Font.BOLD, 28));
        textLabel.setText("----  Sudoku  ----");
        textPanel.add(textLabel);

        resetButton.setFont(new Font("Monospaced", Font.BOLD, 16));
        resetButton.setFocusable(false);
        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                resetGame();
            }
        });
        textPanel.add(resetButton);

        frame.add(textPanel, BorderLayout.NORTH);

        boardPanel.setLayout(new GridLayout(9, 9));
        setupTiles();
        frame.add(boardPanel, BorderLayout.CENTER);

        buttonsPanel.setLayout(new GridLayout(1, 9));
        setupButtons();
        frame.add(buttonsPanel, BorderLayout.SOUTH);

        frame.setVisible(true);
    }

    public void setupTiles() {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                Tile tile = new Tile(i, j);
                board[i][j] = tile;

                char tileChar = puzzle[i].charAt(j);

                if (tileChar != '-') {
                    tile.setFont(new Font("Monospaced", Font.BOLD, 20));
                    tile.setText(String.valueOf(tileChar));
                    tile.setBackground(Color.orange);
                } else {
                    tile.setFont(new Font("Monospaced", Font.PLAIN, 20));
                    tile.setBackground(Color.lightGray);
                }

                if ((i == 2 && j == 2) || (i == 2 && j == 5) || (i == 5 && j == 2) || (i == 5 && j == 5)) {
                    tile.setBorder(BorderFactory.createMatteBorder(1, 1, 5, 5, Color.black));
                } else if (i == 2 || i == 5) {
                    tile.setBorder(BorderFactory.createMatteBorder(1, 1, 5, 1, Color.black));
                } else if (j == 2 || j == 5) {
                    tile.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 5, Color.black));
                } else {
                    tile.setBorder(BorderFactory.createLineBorder(Color.black));
                }
                tile.setFocusable(false);
                boardPanel.add(tile);

                tile.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        Tile clickedTile = (Tile) e.getSource();
                        int r = clickedTile.a;
                        int c = clickedTile.b;

                        if (numSelected != null) {
                            if (!clickedTile.getText().equals("")) {
                                return;
                            }

                            String numSelectedText = numSelected.getText();
                            String tileSolution = String.valueOf(solution[r].charAt(c));

                            if (tileSolution.equals(numSelectedText)) {
                                clickedTile.setText(numSelectedText);

                                if (checkWin()) {
                                    JOptionPane.showMessageDialog(
                                            frame,
                                            "Congratulations! You solwed the Sudoku with " + error + " errors!",
                                            "Game Over!",
                                            JOptionPane.INFORMATION_MESSAGE
                                    );
                                }
                            } else {
                                error++;
                                textLabel.setText("Error: " + error);
                            }
                        }
                    }
                });
            }
        }
    }

    public void setupButtons() {
        for (int i = 1; i < 10; i++) {
            JButton button = new JButton();
            button.setFont(new Font("Monospaced", Font.BOLD, 20));
            button.setText(String.valueOf(i));
            button.setFocusable(false);
            button.setBackground(Color.lightGray);
            buttonsPanel.add(button);

            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    JButton buttonClicked = (JButton) e.getSource();

                    if (numSelected != null) {
                        numSelected.setBackground(Color.lightGray);
                    }
                    numSelected = buttonClicked;
                    numSelected.setBackground(Color.gray);
                }
            });
        }
    }

    public boolean checkWin() {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                String currentText = board[i][j].getText();
                String solutionText = String.valueOf(solution[i].charAt(j));

                if (!currentText.equals(solutionText)) {
                    return false;
                }
            }
        }
        return true;
    }

    public void resetGame() {
        error = 0;
        textLabel.setText("Error: 0");

        if (numSelected != null) {
            numSelected.setBackground(Color.lightGray);
            numSelected = null;
        }

        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (puzzle[i].charAt(j) == '-') {
                    board[i][j].setText("");
                }
            }
        }
    }
}