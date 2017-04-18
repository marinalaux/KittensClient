package kittensclient;

import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;

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
        getContentPane().setLayout(null);
        setSize(800, 600);
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
                    getContentPane().add(kittens.get(i));
                    repaint();
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
                            Thread.sleep(300);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        kittens.get(i).restoresPreviousIcon();
                    }).start();
                }
                if (type == MessageType.EXIT) {
                    int i = Integer.parseInt(data[1]);
                    getContentPane().remove(kittens.get(i));
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
    
}
