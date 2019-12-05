package monster;

import engine.GameEngine;
import generator.BoardGenerator;
import generator.GunGenerator;
import generator.Some;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;

public class Monster extends JPanel implements MouseListener, Some
{
    int startMonster;
    private int deadMonster;
    private int currentMonster;
    int maxLives;
    boolean isDead;
    private int deadPosition;      //怪兽到这个位置的时候就GG了
    private int moveSpeed;
    private BufferedImage[] monsterImage;
    private Timer monsterTimer;
    private int x, y;         //怪兽前进过程中的坐标

    Monster(BufferedImage[] monster, int maxLives, int x, int y)
    {
        int width = monster[0].getWidth();
        int height = monster[0].getHeight();
        isDead = false;
        deadPosition = 100;
        setMonsterImage(monster, maxLives);
        setXY(x, y);
        setPreferredSize(new Dimension(width, height));
        setBounds(x, y, width, height);
        setOpaque(true);
        setBackground(new Color(0,0,0,0));
        addMouseListener(this);

        monsterTimer =  new Timer(GameEngine.RTIME, e -> {
            if(!BoardGenerator.isOver)
                updateGame();
        });
        monsterTimer.start();
    }

    private void setMonsterImage(BufferedImage[] monsterImage, int maxLives)
    {
        this.monsterImage = monsterImage;
        if(maxLives > 0 )
            this.maxLives = maxLives;
        this.deadMonster = monsterImage.length;
        this.startMonster = 0;
        this.currentMonster = startMonster;
    }

    void setMoveSpeed(int moveSpeed)
    {
        if(moveSpeed>=0)
            this.moveSpeed = moveSpeed;
    }


    public boolean isDead()
    {
        return currentMonster >= deadMonster;
    }

    private void setXY(int x, int y)
    {
        if(x>=0)
            this.x = x;
        if(y>=0)
            this.y = y;
    }

    public Timer getTimer()
    {
        return monsterTimer;
    }

    public void go()
    {
        if(!isDead)
        {
            if(x>deadPosition)
                x -= moveSpeed;
            else
            {
                x = deadPosition;
                BoardGenerator.isOver = true;
                monsterTimer.stop();
            }
            setLocation(x, y);
        }
    }

    public void shot() {

    }

    public int getScore()
    {
        return 100;
    }


    // Todo...  important
    public void updateGame()
    {
        go();
        if(++currentMonster == maxLives  && !isDead)
            currentMonster = startMonster;
        else if(isDead)
            currentMonster = (currentMonster < maxLives) ? maxLives : (currentMonster+1);
        repaint();
    }

    public void paint(Graphics g)
    {
        super.paint(g);
        Graphics2D graphics2D = (Graphics2D) g;
        if(!isDead())
            graphics2D.drawImage(monsterImage[currentMonster], 0, 0, null);
        else
            setForeground(new Color(0,0,0,0));
    }

    public void mouseClicked(MouseEvent e)
    {
        if(!BoardGenerator.isOver)
            GunGenerator.setGunFire(true);
    }
    public void mousePressed(MouseEvent e)
    {
        if(!BoardGenerator.isOver)
        {
            GunGenerator.setGunFire(true);
            shot();
            updateGame();
        }
    }

    public void mouseReleased(MouseEvent e)
    {
        if(!BoardGenerator.isOver)
            GunGenerator.setGunFire(false);
    }

    @Override
    public void mouseEntered(MouseEvent e)
    {
        setCursor(GameEngine.cursor1);
    }

    @Override
    public void mouseExited(MouseEvent e)
    {
        setCursor(GameEngine.cursor2);
    }

}
