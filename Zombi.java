
package zombi;

import java.awt.HeadlessException;
import javax.swing.JFrame;


public class Zombi extends JFrame {

    public Zombi(String title) throws HeadlessException {
        super(title);
    }

    
    public static void main(String[] args) {
        Zombi zombi=new Zombi("Zombileri vur");
        zombi.setFocusable(false);
        Oyun oyun = new Oyun();
        oyun.requestFocus();
        oyun.addKeyListener(oyun);
        oyun.setFocusable(true);
        oyun.setFocusTraversalKeysEnabled(false);
        oyun.addMouseListener(oyun);
        zombi.add(oyun);
        zombi.setSize(800,600);
        zombi.setResizable(false);
        zombi.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        zombi.setVisible(true);
    }
    
}
