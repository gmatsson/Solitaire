package com.cards;
import javafx.event.EventHandler;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import javax.swing.*;
import java.awt.event.KeyEvent;
import javax.swing.JFrame;



//by Gustaf Matsson
//2018-08-01
public class PlayingCardGameDisplay extends JComponent implements MouseListener{

    private static final int CARD_WIDTH = 120;
    private static final int CARD_HEIGHT = 200;
    private static final int SPACING = 5;
    private static final int FACE_UP_OFFSET = 15;
    private static final int FACE_DOWN_OFFSET = 5;

    private JFrame frame;
    private int selectedRow = -1;
    private int selectedCol = -1;
    private PlayingCardGame game;





    public PlayingCardGameDisplay(PlayingCardGame game) {
        this.game = game;
        frame = new JFrame("Solitaire");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.getContentPane().add(this);
        this.setPreferredSize(new Dimension(CARD_WIDTH * 7 + SPACING * 8, CARD_HEIGHT * 2 + SPACING * 3 + FACE_DOWN_OFFSET * 7 + 13 * FACE_UP_OFFSET));
        this.addMouseListener(this);
        createMenu();
        frame.pack();
        frame.setVisible(true);
    }
    public void createMenu() {

        JMenuBar menubar = new JMenuBar();
        JMenu menu = new JMenu("Menu");
        JMenuItem newGame = new JMenuItem("New game");
        JMenuItem rules = new JMenuItem("Game rules");
        JMenuItem highscore = new JMenuItem("HIghscore");
        menu.add(newGame);
        menu.add(rules);
        menu.add(highscore);
        menubar.add(menu);
        frame.setJMenuBar(menubar);
        
        newGame.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
               game = new PlayingCardGame();

            }
        });
        rules.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                JOptionPane.showMessageDialog(frame, "Solitaire typically involve dealing cards from a " +
                        "shuffled deck into a prescribed arrangement on a tabletop, \n" +
                        "from which the player attempts to reorder the deck by suit and rank through \n" +
                        "a series of moves transferring cards from one place to another under prescribed restrictions. \n" +
                        "Some games allow for the reshuffling of the deck(s), and/or the placement of cards into new or \"empty\" locations.\n" +
                        " In the most familiar, general form of Patience, the object of the game is to build up four blocks of cards \n" +
                        "going from ace to king in each suit, taking cards from the layout if they appear on the table.\n" +
                        "Source: Wikipedia");

            }
        });
        highscore.addActionListener (new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                BufferedReader br = null;
                try {
                    br = new BufferedReader(new FileReader("highscore.txt"));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                String aLineFromFile = null;
                String input = "";
                try {
                    while ((aLineFromFile = br.readLine()) != null) {
                        input += aLineFromFile + "\n";
                    }
                    JOptionPane.showMessageDialog(frame, input, "Highscore",1);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void paintComponent(Graphics g) {
        g.setColor(new Color(0, 128, 0));
        g.fillRect(0, 0, getWidth(), getHeight());

        drawCard(g, game.getStockCard(), SPACING, SPACING);

        drawCard(g, game.getWasteCard(), SPACING * 2 + CARD_WIDTH, SPACING);
        if (selectedRow == 0 && selectedCol == 1)
            drawBorder(g, SPACING * 2 + CARD_WIDTH, SPACING);

        for (int i = 0; i < 4; i++)
            drawCard(g, game.getFoundationCard(i), SPACING * (4 + i) + CARD_WIDTH * (3 + i), SPACING);

        for (int i = 0; i < 7; i++) {
            Stack<PlayingCard> pile = game.getPile(i);
            int offset = 0;
            for (int j = 0; j < pile.size(); j++) {
                drawCard(g, pile.get(j), SPACING + (CARD_WIDTH + SPACING) * i, CARD_HEIGHT + 2 * SPACING + offset);
                if (selectedRow == 1 && selectedCol == i && j == pile.size() - 1)
                    drawBorder(g, SPACING + (CARD_WIDTH + SPACING) * i, CARD_HEIGHT + 2 * SPACING + offset);

                if (pile.get(j).isFaceUp())
                    offset += FACE_UP_OFFSET;
                else
                    offset += FACE_DOWN_OFFSET;
            }
        }
    }

    private void drawBorder(Graphics g, int x, int y) {
        g.setColor(Color.YELLOW);
        g.drawRect(x, y, CARD_WIDTH, CARD_HEIGHT);
        g.drawRect(x + 1, y + 1, CARD_WIDTH - 2, CARD_HEIGHT - 2);
        g.drawRect(x + 2, y + 2, CARD_WIDTH - 4, CARD_HEIGHT - 4);
    }

    private void drawCard(Graphics g, PlayingCard playingCard, int x, int y) {
        if (playingCard == null) {
            g.setColor(Color.BLACK);
            g.drawRect(x, y, CARD_WIDTH, CARD_HEIGHT);
        }
        else {
            String fileName = playingCard.getFileName();
            if (!new File(fileName).exists())
                throw new IllegalArgumentException("bad file name:  " + fileName);
            Image image = new ImageIcon(fileName).getImage();
            g.drawImage(image, x, y, CARD_WIDTH, CARD_HEIGHT, null);
        }
    }


    @Override
    public void mouseClicked(MouseEvent e) {
        if(game.winCheck()) {
            displayWin();
        }
        int col = e.getX() / (SPACING + CARD_WIDTH);
        int row = e.getY() / (SPACING + CARD_HEIGHT);
        if (row > 1)
            row = 1;
        if (col > 6)
            col = 6;

        if (row == 0 && col == 0)
            game.stockClicked();
        else if (row == 0 && col == 1)
            game.wasteClicked();
        else if (row == 0 && col >= 3)
            game.foundationClicked(col - 3);
        else if (row == 1)
            game.pileClicked(col);
        repaint();

    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    public void unselect() {
        selectedRow = -1;
        selectedCol = -1;
    }

    public boolean isWasteSelected() {
        return selectedRow == 0 && selectedCol == 1;
    }

    public void selectWaste() {
        selectedRow = 0;
        selectedCol = 1;
    }

    public boolean isPileSelected() {
        return selectedRow == 1;
    }

    public int selectedPile() {
        if (selectedRow == 1)
            return selectedCol;
        else
            return -1;
    }

    public void selectPile(int index) {
        selectedRow = 1;
        selectedCol = index;
    }
    private void displayWin()  {
        PrintWriter highscoreFile = null;
        try {
            highscoreFile = new PrintWriter("highscore.txt");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        String winner = JOptionPane.showInputDialog("You Win!!! \n Enter your name: ");
        highscoreFile.println(winner + " " + game.score);
        highscoreFile.close();
    }

}
