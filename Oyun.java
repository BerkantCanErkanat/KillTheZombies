
package zombi;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.imageio.stream.FileImageInputStream;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;


class Dusman{
    private double x;
    private double y;
    private double dirx;
    private double diry;

    public Dusman(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public double getDirx() {
        return dirx;
    }

    public void setDirx(double dirx) {
        this.dirx = dirx;
    }

    public double getDiry() {
        return diry;
    }

    public void setDiry(double diry) {
        this.diry = diry;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }
    
    
}
class Ates{
    private double atesX;
    private double atesY;
    private int mousex;
    private int mousey;
    private double dirx;
    private double diry;
    private int ilkx;
    private int ilky;
    public Ates(int atesX, int atesY, int mousex, int mousey) {
        this.atesX = atesX;
        this.atesY = atesY;
        this.mousex = mousex;
        this.mousey = mousey;
        ilkx=atesX;
        ilky=atesY;
        
            diry=(double)Math.abs(atesY-mousey)/Math.abs(atesX-mousex); 
            dirx=1;
            if(diry < 3){
                dirx*=6;
                diry*=6;
            }
            else if(diry < 6){
                dirx*=3;
                diry*=3;        
            }
            else if(diry < 10){
                dirx*=2;
                diry*=2;
            }

    }

    public double getDirx() {
        return dirx;
    }

    public double getDiry() {
        return diry;
    }

    public int getIlkx() {
        return ilkx;
    }

    public int getIlky() {
        return ilky;
    }

    public int getMousex() {
        return mousex;
    }

    public int getMousey() {
        return mousey;
    }

    public double getAtesX() {
        return atesX;
    }

    public void setAtesX(double atesX) {
        this.atesX = atesX;
    }

    public double getAtesY() {
        return atesY;
    }

    public void setAtesY(double atesY) {
        this.atesY = atesY;
    }

}


public class Oyun extends JPanel implements KeyListener,MouseListener,ActionListener{
  private int bizX=325;
  private int bizY=328;
   LinkedList<Dusman> dusmanlar = new LinkedList<>();
   LinkedList<Ates> atesler = new LinkedList<>();
   private int kill=0;
   Thread tr;
   Random rnd=new Random();
   Timer timer = new Timer(10,this) ;
   Object lock1 = new Object();
   Object lock2 = new Object();
    BufferedImage image;
    public Oyun() {
        setBackground(Color.white);
        setSize(800,600);
        tr = new Thread(new Runnable() {

            @Override
            public void run() {
                dusmanUret();
            }
        });
        tr.start();
        timer.start();
      try {
          image =ImageIO.read(new FileImageInputStream(new File("ates.jpg")));
      } catch (IOException ex) {
          Logger.getLogger(Oyun.class.getName()).log(Level.SEVERE, null, ex);
      }
    }
    public void olumkontrol(){
        Rectangle rect = new Rectangle();
        synchronized(lock1){
        for(Dusman dus : dusmanlar){
            synchronized(lock2){
            for(Ates at:atesler){
            rect=new Rectangle((int)dus.getX(),(int)dus.getY(),10,10);
            if(rect.intersects(new Rectangle((int)at.getAtesX(),(int)at.getAtesY(),10,10))){
                atesler.remove(at);
                dusmanlar.remove(dus);
                kill++;
            }}
        }}}
    }
    public void oyunkontrol(){
        synchronized(lock2){
        for(Dusman dus:dusmanlar){
            Rectangle rect = new Rectangle(bizX,bizY,10,10);
            if(rect.intersects(new Rectangle((int)dus.getX(),(int)dus.getY(),10,10))){
                timer.stop();
                JOptionPane.showMessageDialog(this,"ZOMBİ SENİ YAKALADI"+"\n Toplam Kill: "+kill);
                System.exit(0);
            }
        }
    }}
    @Override
    public void paint(Graphics g) {
        super.paint(g); //To change body of generated methods, choose Tools | Templates.
        g.setColor(Color.BLACK);
        g.fillOval(bizX, bizY,20,20);
        olumkontrol();
        oyunkontrol(); 
        g.setColor(Color.BLUE);
        for(Dusman dus:dusmanlar){
            g.fillRect((int)dus.getX(),(int)dus.getY(),15,15);
        }
         g.setColor(Color.red);
        for(Ates at:atesler){
            g.drawImage(image,(int)at.getAtesX(),(int)at.getAtesY(),image.getWidth()/30,image.getHeight()/30,this);
        }
        
    }
   public void dusmanUret(){
       while(true){
           try {
               Thread.sleep(2000);
           } catch (InterruptedException ex) {
               Logger.getLogger(Oyun.class.getName()).log(Level.SEVERE, null, ex);
           }
           int secim = rnd.nextInt(4);
           System.out.println(secim);
           int x,y;
           synchronized(lock1){
           switch(secim){
               
               case 0:
                    y = rnd.nextInt(10);
                   y = -y;
                    x = rnd.nextInt(700);
                    if(Math.abs(bizX-x) < 100){
                        x=0;
                        dusmanlar.add(new Dusman(x, y));
                    }else{
                   dusmanlar.add(new Dusman(x, y));
                    }
               break;
               case 1:
                    x= rnd.nextInt(10);
                   x = -x;
                    y = rnd.nextInt(500);
                    if(Math.abs(bizY-y) < 100){
                        y=400;
                         dusmanlar.add(new Dusman(x, y));
                    }else{
                        dusmanlar.add(new Dusman(x, y));
                    }
                   
               break;
               case 2:
                    x = rnd.nextInt(700);
                    y= rnd.nextInt(10)+550;
                    if(Math.abs(bizX-x) < 100){
                        x=700;
                        dusmanlar.add(new Dusman(x, y));
                    }else{
                   dusmanlar.add(new Dusman(x, y));
                    }
               break;
               case 3:
                    y = rnd.nextInt(500);
                    x = rnd.nextInt(10)+710;
                   if(Math.abs(bizY-y) < 100){
                        y=0;
                         dusmanlar.add(new Dusman(x, y));
                    }else{
                        dusmanlar.add(new Dusman(x, y));
                    }
               break;

           }
           
       }}
   }
    @Override
    public void repaint() {
        super.repaint(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int c =e.getKeyCode();
        if(c==KeyEvent.VK_A){
            if(bizX>10)
            bizX-=5;
            else
            bizX=10;
            }
       else  if(c==KeyEvent.VK_D){
            if(bizX<700)
            bizX+=5;
            else
            bizX=700;
        }
      else   if(c==KeyEvent.VK_W){
            if(bizY>10)
            bizY-=5;
            else
            bizY=10;
        }
       else if(c==KeyEvent.VK_S){
            if(bizY<500)
            bizY+=5;
            else
            bizY=500;     
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        synchronized(lock2){
        atesler.add(new Ates(bizX,bizY,e.getX(),e.getY()));
    }}

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

    @Override
    public void actionPerformed(ActionEvent e) {
        Thread t1=new Thread(new Runnable() {

            @Override
            public void run() {
                ateset();
            }
        });
        Thread t2 = new Thread(new Runnable() {

            @Override
            public void run() {
                dusmanyurut();
            }
        });
         t1.start();
         t2.start();
        repaint();
    }
    public void ateset(){
       for(Ates at:atesler){
           if(at.getIlkx() > at.getMousex() && at.getIlky() > at.getMousey()){
               at.setAtesX(at.getAtesX()-at.getDirx());
               at.setAtesY(at.getAtesY()-at.getDiry());
           }
           else if(at.getIlkx() < at.getMousex() && at.getIlky() > at.getMousey()){
                at.setAtesX(at.getAtesX()+at.getDirx());
                at.setAtesY(at.getAtesY()-at.getDiry());
           }
           else if(at.getIlkx() > at.getMousex() && at.getIlky() < at.getMousey()){
                at.setAtesX(at.getAtesX()-at.getDirx());
                at.setAtesY(at.getAtesY()+at.getDiry());
           }
           else if(at.getIlkx() < at.getMousex() && at.getIlky() < at.getMousey()){
                at.setAtesX(at.getAtesX()+at.getDirx());
                at.setAtesY(at.getAtesY()+at.getDiry());
           }
           
           
       }
 
    }
    public void dusmanyurut(){
        synchronized(lock1){
        for(Dusman dus:dusmanlar){
           if(bizX > dus.getX() && bizY > dus.getY()){
               dus.setDiry((double)Math.abs(dus.getY()-bizY)/Math.abs(dus.getX()-bizX));
               dus.setDirx(1);
               dus.setX(dus.getX()+dus.getDirx());
               dus.setY(dus.getY()+dus.getDiry());
           }
           else if(bizY > dus.getY() && bizX < dus.getX()){
               dus.setDiry((double)Math.abs(dus.getY()-bizY)/Math.abs(dus.getX()-bizX));
               dus.setDirx(1);
               dus.setX(dus.getX()-dus.getDirx());
               dus.setY(dus.getY()+dus.getDiry());
           }
           else if(bizY < dus.getY() && bizX > dus.getX()){
               dus.setDiry((double)Math.abs(dus.getY()-bizY)/Math.abs(dus.getX()-bizX));
               dus.setDirx(1);
               dus.setX(dus.getX()+dus.getDirx());
               dus.setY(dus.getY()-dus.getDiry());
           }
           else if(bizY < dus.getY() && bizX < dus.getX()){
               dus.setDiry((double)Math.abs(dus.getY()-bizY)/Math.abs(dus.getX()-bizX));
               dus.setDirx(1);
               dus.setX(dus.getX()-dus.getDirx());
               dus.setY(dus.getY()-dus.getDiry());
           }
       }}
    }
    
    
    
    
}
