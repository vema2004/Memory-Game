import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;

public class MemoryGame {
    private JFrame frame;
    private JPanel panel, topPanel, bottomPanel;
    private JLabel scoreLabel, timerLabel, bestScoreLabel;
    private JButton[] buttons;
    private JButton exitButton;
    private ArrayList<Integer> values;
    private int firstIndex = -1, secondIndex = -1, score = 0, timeLeft = 60, matchedPairs = 0;
    private int bestScore = 0;
    private Timer gameTimer, flipTimer;
    
    public MemoryGame() {
        frame = new JFrame("Memory Game");
        frame.setLayout(new BorderLayout());
        frame.getContentPane().setBackground(new Color(50, 50, 50));
        
        topPanel = new JPanel();
        topPanel.setLayout(new GridLayout(1, 3));
        topPanel.setBackground(new Color(30, 30, 30));
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        bestScoreLabel = new JLabel("Best Score: " + bestScore, SwingConstants.CENTER);
        bestScoreLabel.setForeground(Color.YELLOW);
        bestScoreLabel.setFont(new Font("Arial", Font.BOLD, 18));
        
        scoreLabel = new JLabel("Score: 0", SwingConstants.CENTER);
        scoreLabel.setForeground(Color.WHITE);
        scoreLabel.setFont(new Font("Arial", Font.BOLD, 18));
        
        timerLabel = new JLabel("Time: 60s", SwingConstants.CENTER);
        timerLabel.setForeground(Color.WHITE);
        timerLabel.setFont(new Font("Arial", Font.BOLD, 18));
        
        topPanel.add(bestScoreLabel);
        topPanel.add(scoreLabel);
        topPanel.add(timerLabel);
        
        panel = new JPanel(new GridLayout(4, 4, 10, 10));
        panel.setBackground(new Color(50, 50, 50));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        buttons = new JButton[16];
        values = new ArrayList<>();
        
        setupGame();
        
        bottomPanel = new JPanel();
        exitButton = new JButton("Exit");
        exitButton.setFont(new Font("Arial", Font.BOLD, 18));
        exitButton.setBackground(new Color(200, 50, 50));
        exitButton.setForeground(Color.WHITE);
        exitButton.addActionListener(e -> System.exit(0));
        bottomPanel.add(exitButton);
        
        frame.add(topPanel, BorderLayout.NORTH);
        frame.add(panel, BorderLayout.CENTER);
        frame.add(bottomPanel, BorderLayout.SOUTH);
        frame.setSize(450, 550);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
    
    private void setupGame() {
        panel.removeAll();
        values.clear();
        
        for (int i = 1; i <= 8; i++) {
            values.add(i);
            values.add(i);
        }
        Collections.shuffle(values);
        
        for (int i = 0; i < 16; i++) {
            buttons[i] = new JButton("?");
            buttons[i].setFont(new Font("Arial", Font.BOLD, 24));
            buttons[i].setFocusPainted(false);
            buttons[i].setBackground(new Color(70, 130, 180));
            buttons[i].setForeground(Color.WHITE);
            buttons[i].setPreferredSize(new Dimension(80, 80));
            buttons[i].addActionListener(new ButtonClickListener(i));
            panel.add(buttons[i]);
        }
        
        panel.revalidate();
        panel.repaint();
        
        firstIndex = -1;
        secondIndex = -1;
        score = 0;
        timeLeft = 60;
        matchedPairs = 0;
        scoreLabel.setText("Score: 0");
        timerLabel.setText("Time: 60s");
        
        flipTimer = new Timer(500, e -> checkMatch());
        flipTimer.setRepeats(false);
        
        if (gameTimer != null && gameTimer.isRunning()) {
            gameTimer.stop();
        }
        gameTimer = new Timer(1000, e -> updateTimer());
        gameTimer.start();
    }
    
    private class ButtonClickListener implements ActionListener {
        private int index;
        
        public ButtonClickListener(int index) {
            this.index = index;
        }
        
        @Override
        public void actionPerformed(ActionEvent e) {
            if (firstIndex == -1) {
                firstIndex = index;
                buttons[firstIndex].setText(String.valueOf(values.get(firstIndex)));
            } else if (secondIndex == -1 && index != firstIndex) {
                secondIndex = index;
                buttons[secondIndex].setText(String.valueOf(values.get(secondIndex)));
                flipTimer.start();
            }
        }
    }
    
    private void checkMatch() {
        if (!values.get(firstIndex).equals(values.get(secondIndex))) {
            buttons[firstIndex].setText("?");
            buttons[secondIndex].setText("?");
        } else {
            buttons[firstIndex].setEnabled(false);
            buttons[secondIndex].setEnabled(false);
            buttons[firstIndex].setBackground(new Color(34, 139, 34));
            buttons[secondIndex].setBackground(new Color(34, 139, 34));
            score += 10;
            matchedPairs++;
            scoreLabel.setText("Score: " + score);
            checkWin();
        }
        firstIndex = -1;
        secondIndex = -1;
    }
    
    private void updateTimer() {
        timeLeft--;
        timerLabel.setText("Time: " + timeLeft + "s");
        if (timeLeft == 0) {
            gameTimer.stop();
            JOptionPane.showMessageDialog(frame, "Time's up! Starting a new game.");
            setupGame();
        }
    }
    
    private void checkWin() {
        if (matchedPairs == 8) {
            gameTimer.stop();
            if (score > bestScore) {
                bestScore = score;
                bestScoreLabel.setText("Best Score: " + bestScore);
            }
            JOptionPane.showMessageDialog(frame, "Congratulations! You won! Your score: " + score);
            setupGame();
        }
    }
    
    public static void main(String[] args) {
        new MemoryGame();
    }
}
