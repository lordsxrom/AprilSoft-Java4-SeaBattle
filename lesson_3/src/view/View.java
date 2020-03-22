package view;

import presenter.ViewListener;
import model.Utils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

public class View implements IView {

    private ViewListener listener;

    private JPanel playerPanel;
    private JPanel aiPanel;

    private BufferedImage playerMap;
    private BufferedImage aiMap;

    public View() {
        initPlayerPanel();
        initAiPanel();
        initFrame();
    }

    private void initFrame() {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setTitle("Sea Battle");
        frame.setResizable(false);
        frame.setVisible(true);
        frame.setIconImage(new ImageIcon("img/icon.png").getImage());
        frame.setLocationRelativeTo(null);
        frame.setLayout(new BorderLayout());

        JButton button = new JButton("Restart");
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                listener.onStartButtonPressed();
            }
        });

        frame.add(button, BorderLayout.CENTER);
        frame.add(aiPanel, BorderLayout.WEST);
        frame.add(playerPanel, BorderLayout.EAST);
        frame.pack();
    }

    private void initAiPanel() {
        aiPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(aiMap, 0, 0, null);
            }
        };

        aiPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                int x = e.getX() / Utils.IMAGE_SIZE;
                int y = e.getY() / Utils.IMAGE_SIZE;
                listener.onMousePressed(x, y);
            }
        });

        aiPanel.setFocusable(true);
        aiPanel.setPreferredSize(new Dimension(Utils.COLS * Utils.IMAGE_SIZE,
                Utils.ROWS * Utils.IMAGE_SIZE));
    }

    private void initPlayerPanel() {
        playerPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(playerMap, 0, 0, null);
            }
        };

        playerPanel.setPreferredSize(new Dimension(Utils.COLS * Utils.IMAGE_SIZE,
                Utils.ROWS * Utils.IMAGE_SIZE));
    }

    @Override
    public void setListener(ViewListener listener) {
        this.listener = listener;
    }
    
    @Override
    public void updateState(int state) {
        String message;
        String title;

        if (state == 1) {
            message = "You lose. Do you want to try again";
            title = "Game Over";
        } else if (state == 2) {
            message = "You won. Congratulation! Do you want to try again?";
            title = "Win";
        } else {
            return;
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                int action = JOptionPane.showConfirmDialog(null,
                        message,
                        title,
                        JOptionPane.YES_NO_OPTION);

                if (action == JOptionPane.YES_OPTION) {
                    listener.onStartButtonPressed();
                } else {
                    System.exit(0);
                }
            }
        }).start();
    }

    @Override
    public void updatePlayerPanel(BufferedImage map) {
        this.playerMap = map;
        playerPanel.repaint();
    }

    @Override
    public void updateAiPanel(BufferedImage map) {
        this.aiMap = map;
        aiPanel.repaint();
    }
}
