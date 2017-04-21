package kittensclient;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

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
    /** Imagem anterior */
    private Icon previousIcon;
    /** Contador de carinhos */
    private int petScore;
    /** Cor do jogador */
    private int color;
    /** Jogador da própria sessão */
    private boolean me;
    /** Painel de pontuação do jogador */
    private JPanel scorePanel;
    /** Label de exibição do contador */
    private JLabel petScoreLabel;

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
     * Retorna a coluna onde o jogador está
     * 
     * @return Coluna
     */
    public int getX() {
        return position.x;
    }
    
    /**
     * Retorna a linha onde o jogador está
     * 
     * @return Linha
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
    
    /**
     * Define o ícone do jogador quando é acariciado
     */
    public void setIconPet() {
        if (getIcon() != petting) {
            savePreviousIcon();
        }
        setIcon(petting);
    }
    
    /**
     * Restaura o ícone anterior
     */
    public void restoresPreviousIcon() {
        setIcon(previousIcon);
    }
    
    /**
     * Salva o ícone atual do jogador
     */
    private void savePreviousIcon() {
        previousIcon = getIcon();
    }

    /**
     * Retorna o contador de carinho
     * 
     * @return Contador
     */
    public int getPetScore() {
        return petScore;
    }

    /**
     * Define o contador de carinho
     * 
     * @param petScore 
     */
    public void setPetScore(int petScore) {
        this.petScore = petScore;
    }

    /**
     * Retorna a cor do jogador
     * 
     * @return Cor
     */
    public int getColor() {
        return color;
    }

    /**
     * Define a cor do jogador
     * 
     * @param color 
     */
    public void setColor(int color) {
        this.color = color;
    }

    /**
     * Indica se é o jogador da sessão
     * 
     * @return Jogador da sessão
     */
    public boolean isMe() {
        return me;
    }
    
    /**
     * Define se é o próprio jogador da sessão
     * 
     * @param me 
     */
    public void setMe(boolean me) {
        this.me = me;
    }

    /**
     * Retorna o painel de pontuação do jogador
     * 
     * @return Painel de pontuação
     */
    public JPanel getScorePanel() {
        return scorePanel;
    }

    /**
     * Define o painel de pontuação do jogador
     * 
     * @param scorePanel 
     */
    public void setScorePanel(JPanel scorePanel) {
        this.scorePanel = scorePanel;
    }

    /**
     * Retorna o label do contador
     * 
     * @return Label contador
     */
    public JLabel getPetScoreLabel() {
        return petScoreLabel;
    }

    /**
     * Define o label do contador
     * 
     * @param petScoreLabel 
     */
    public void setPetScoreLabel(JLabel petScoreLabel) {
        this.petScoreLabel = petScoreLabel;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setColor(new Color(color));
        g2d.fillPolygon(new int []{24, 36, 30}, new int []{15, 15, 25}, 3);
        if (me) {
            g2d.setFont(new Font("Arial", Font.BOLD, 12));
            g2d.drawString("You", 20, 10);
        }
        g2d.dispose();
    }
    
}
