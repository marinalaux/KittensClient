package kittensclient;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

/**
 * Cliente Kitty
 * 
 * @author Marina
 */
public class KittensClient extends JFrame implements Runnable {

    /** Jogadores */
    private ArrayList<Kitty> kittens;
    /** Socket de comunicação */
    private Socket s;
    /** Retornos do servidor */
    private BufferedReader in;
    /** Requisições para o servidor */
    private PrintWriter out;
    /** Painel do jogo */
    private JPanel panel;
    /** Painel de informações do jogo */
    private JPanel infoPanel;
    
    public static void main(String[] args) {
        
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new KittensClient().setVisible(true);
            }
        });
        
    }
    
    /**
     * Construtor do jogo
     */
    public KittensClient() {
        super();
        initGui();
        kittens = new ArrayList<>();
        connect();
        Thread imAliveThread = new Thread(this::imAliveMessageSender);
        Thread rendererThread = new Thread(this);
        imAliveThread.start();
        rendererThread.start();
    }

    /**
     * Inicializa os componentes da interface
     */
    private void initGui() {
        setTitle("Kittens");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                out.println("exiting");
            }
        });
        addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {}

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                    out.println("press_right");
                }
                if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                    out.println("press_left");
                }
                if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                    out.println("press_down");
                }
                if (e.getKeyCode() == KeyEvent.VK_UP) {
                    out.println("press_up");
                }
                if (e.getKeyCode() == KeyEvent.VK_CONTROL) {
                    out.println("pet");
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                    out.println("release_right");
                }
                if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                    out.println("release_left");
                }
                if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                    out.println("release_down");
                }
                if (e.getKeyCode() == KeyEvent.VK_UP) {
                    out.println("release_up");
                }
                if (e.getKeyCode() == KeyEvent.VK_CONTROL) {
                    out.println("stop_petting");
                }
            }
        });
        getContentPane().setLayout(new BorderLayout());
        panel = new JPanel(null);
        getContentPane().add(panel);
        JScrollPane scroll = new JScrollPane(buildInfoPanel());
        scroll.setPreferredSize(new Dimension(200, 600));
        getContentPane().add(scroll, BorderLayout.EAST);
        setSize(1000, 600);
        try {
            setIconImage(ImageIO.read(KittensClient.class.getResourceAsStream("/icon.png")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Conecta com o servidor
     */
    private void connect() {
        try {
            s = new Socket("localhost", 8880);
            in = new BufferedReader(new InputStreamReader(s.getInputStream()));
            out = new PrintWriter(s.getOutputStream(), true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Envia mensagem de conexão ativa ao servidor
     */
    private void imAliveMessageSender() {
        while (true) {
            try {
                out.println("imAlive");
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    
    /**
     * Renderiza o jogo
     */
    private void renderer() {
        String command;
        try {
            while (true) {
                command = in.readLine();
                String data[] = command.split("\\_");
                MessageType type = MessageType.valueOf(data[0]);
                if (type == MessageType.NEW) {
                    kittens.add(new Kitty());
                    int i = kittens.size() - 1;
                    kittens.get(i).setColor(Integer.parseInt(data[1]));
                    kittens.get(i).setMe(Boolean.parseBoolean(data[2]));
                    kittens.get(i).setPetScore(Integer.parseInt(data[3]));
                    kittens.get(i).setScorePanel(createScorePanel(kittens.get(i)));
                    SwingUtilities.invokeLater(() -> {
                        panel.add(kittens.get(i));
                        infoPanel.add(kittens.get(i).getScorePanel());
                        revalidate();
                        repaint();
                    });
                }
                if (type == MessageType.MOV) {
                    int i = Integer.parseInt(data[1]);
                    if (data[4].equals("right")) {
                        kittens.get(i).setIconRight();
                    }
                    if (data[4].equals("left")) {
                        kittens.get(i).setIconLeft();
                    }
                    if (data[4].equals("down")) {
                        kittens.get(i).setIconDown();
                    }
                    if (data[4].equals("up")) {
                        kittens.get(i).setIconUp();
                    }
                    kittens.get(i).setPosition(new Point(Integer.parseInt(data[2]), Integer.parseInt(data[3])));
                    kittens.get(i).move();
                }
                if (type == MessageType.PET) {
                    int i = Integer.parseInt(data[1]);
                    new Thread(() -> {
                        kittens.get(i).setIconPet();
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        kittens.get(i).restoresPreviousIcon();
                    }).start();
                    kittens.get(i).setPetScore(Integer.parseInt(data[2]));
                    kittens.get(i).getPetScoreLabel().setText(String.valueOf(kittens.get(i).getPetScore()));
                    repaint();
                }
                if (type == MessageType.EXIT) {
                    int i = Integer.parseInt(data[1]);
                    panel.remove(kittens.get(i));
                    infoPanel.remove(kittens.get(i).getScorePanel());
                    revalidate();
                    repaint();
                    kittens.remove(i);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public void run() {
        renderer();
    }

    /**
     * Cria painel de informações do jogo
     * 
     * @return Painel de informações
     */
    private JPanel buildInfoPanel() {
        infoPanel = new JPanel();
        JLabel title = new JLabel("Pet Score");
        title.setAlignmentX(JLabel.CENTER_ALIGNMENT);
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.add(title);
        return infoPanel;
    }

    /**
     * Cria painel de pontuação do jogador
     * 
     * @return Painel de pontuação
     */
    private JPanel createScorePanel(Kitty k) {
        JPanel score = new JPanel(null);
        JLabel player = new JLabel();
        JLabel playerScore = new JLabel();
        if (k.isMe()) {
            player.setForeground(new Color(k.getColor()));
            player.setText("You");
        } else {
            player.setBackground(new Color(k.getColor()));
            player.setOpaque(true);
        }
        player.setBounds(10, 10, 25, 10);
        player.setPreferredSize(new Dimension(10, 10));
        playerScore.setBounds(40, 10, 50, 10);
        playerScore.setPreferredSize(new Dimension(10, 10));
        playerScore.setText(String.valueOf(k.getPetScore()));
        k.setPetScoreLabel(playerScore);
        score.add(player);
        score.add(playerScore);
        score.setPreferredSize(new Dimension(170, 20));
        score.setMaximumSize(new Dimension(170, 20));
        return score;
    }
    
}
