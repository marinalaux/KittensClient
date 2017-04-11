package kittensclient;

import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
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
        Thread rendererThread = new Thread(this);
        rendererThread.start();
    }

    /**
     * Inicializa os componentes da interface
     */
    private void initGui() {
        setTitle("Kittens");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
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
     * Renderiza o jogo
     */
    private void renderer() {
        String command;
        try {
            while (true) {
                command = in.readLine();
                String data[] = command.split("\\_");
                int i = Integer.parseInt(data[0]);
                if (i >= kittens.size()) {
                    kittens.add(new Kitty());
                    getContentPane().add(kittens.get(kittens.size() - 1));
                    repaint();
                }
                if (Integer.parseInt(data[1]) < kittens.get(i).getX()) {
                    kittens.get(i).setIconLeft();
                }
                if (Integer.parseInt(data[1]) > kittens.get(i).getX()) {
                    kittens.get(i).setIconRight();
                }
                if (Integer.parseInt(data[2]) < kittens.get(i).getY()) {
                    kittens.get(i).setIconUp();
                }
                if (Integer.parseInt(data[2]) > kittens.get(i).getY()) {
                    kittens.get(i).setIconDown();
                }
                kittens.get(i).setPosition(new Point(Integer.parseInt(data[1]), Integer.parseInt(data[2])));
                kittens.get(i).move();
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
