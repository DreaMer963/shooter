package generator;

import engine.GameEngine;

import javax.swing.*;
import java.awt.*;

public class GunGenerator extends JPanel
{
    private Image gunImage;
    private Image gunFireImage;
    private final int HEIGHT = 150;
    private static boolean isGunFired;
    private int panelY;
    private int gunY;
    private double gunAngle;


    GunGenerator()
    {
        panelY = (GameEngine.HEIGHT /2 ) - (HEIGHT / 2) + 50;
        setBackground(Color.gray);
        int WIDTH = 100;
        setBounds(0, panelY, WIDTH, HEIGHT);
        setGunFire(false);
        setGunImage();
    }

    void setRotate(int x, int y)
    {
        setGunFire(false);
        gunAngle = Math.atan2(y-(panelY+gunY), x);
        repaint();
    }

    //是否射击
    public static void setGunFire(boolean fire)
    {
        isGunFired = fire;
    }

    private void setGunImage()
    {
        // 加载枪和枪射击时的图片
        gunImage = Toolkit.getDefaultToolkit().getImage("D:\\githubPro\\code\\javaCoding\\Shooter\\src\\images\\guns/gun0.png");
        gunFireImage = Toolkit.getDefaultToolkit().getImage("D:\\githubPro\\code\\javaCoding\\Shooter\\src\\images\\guns/gun1.png");

        gunY = (HEIGHT / 2 ) - (gunImage.getHeight(null) / 2) - 15;
        gunAngle = 0;
    }

    public void paint(Graphics g)
    {
        super.paint(g);
        Graphics2D graphics2D = (Graphics2D) g;
        graphics2D.rotate(gunAngle, 13, gunY+66);
        if(isGunFired)
            graphics2D.drawImage(gunFireImage, 0, gunY, null);
        else
            graphics2D.drawImage(gunImage, 0, gunY, null);
    }

    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        g.drawImage(Toolkit.getDefaultToolkit().getImage("D:\\githubPro\\code\\javaCoding\\Shooter\\src\\images\\backgrounds/gunbg.png"), 0, 0, null);
    }

}
