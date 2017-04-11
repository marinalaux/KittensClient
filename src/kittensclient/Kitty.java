package kittensclient;

import java.awt.Image;
import java.awt.Point;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

/**
 * Gatinho
 * 
 * @author Marina
 */
public class Kitty extends JLabel {
    
    /** Posição do jogador na tela */
    private Point position;
    /** Imagem ao andar para direita */
    private final ImageIcon walkRight;
    /** Imagem ao andar para esquerda */
    private final ImageIcon walkLeft;
    /** Imagem ao andar para cima */
    private final ImageIcon walkUp;
    /** Imagem ao andar para baixo */
    private final ImageIcon walkDown;
    /** Imagem ao receber carinho */
    private final ImageIcon petting;

    /**
     * Construtor
     */
    public Kitty() {
        walkRight = new ImageIcon(new ImageIcon(getClass().getResource("/right.gif")).
                getImage().getScaledInstance(64, 64, Image.SCALE_DEFAULT));
        walkLeft = new ImageIcon(new ImageIcon(getClass().getResource("/left.gif")).
                getImage().getScaledInstance(64, 64, Image.SCALE_DEFAULT));
        walkUp = new ImageIcon(new ImageIcon(getClass().getResource("/up.gif")).
                getImage().getScaledInstance(64, 64, Image.SCALE_DEFAULT));
        walkDown = new ImageIcon(new ImageIcon(getClass().getResource("/down.gif")).
                getImage().getScaledInstance(64, 64, Image.SCALE_DEFAULT));
        petting = new ImageIcon(new ImageIcon(getClass().getResource("/heart.gif")).
                getImage().getScaledInstance(64, 64, Image.SCALE_DEFAULT));
        setIconRight();
        position = new Point(0, 0);
        setBounds(position.x, position.y, 64, 64);
    }

    /**
     * Move o jogador
     */
    public void move() {
        setBounds(position.x, position.y, 64, 64);
    }
    
    /**
     * Retorna a linha onde o jogador está
     * 
     * @return Linha
     */
    public int getX() {
        return position.x;
    }
    
    /**
     * Retorna a coluna onde o jogador está
     * 
     * @return Coluna
     */
    public int getY() {
        return position.y;
    }
    
    /**
     * Define a posição do jogador
     * 
     * @param p 
     */
    public void setPosition(Point p) {
        position.x = p.x;
        position.y = p.y;
    }
    
    /**
     * Define o ícone do jogador quando anda para direita
     */
    public void setIconRight() {
        setIcon(walkRight);
    }
    
    /**
     * Define o ícone do jogador quando anda para esquerda
     */
    public void setIconLeft() {
        setIcon(walkLeft);
    }
    
    /**
     * Define o ícone do jogador quando anda para cima
     */
    public void setIconUp() {
        setIcon(walkUp);
    }
    
    /**
     * Define o ícone do jogador quando anda para baixo
     */
    public void setIconDown() {
        setIcon(walkDown);
    }
}
