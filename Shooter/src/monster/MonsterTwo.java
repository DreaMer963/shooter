package monster;


import java.awt.image.BufferedImage;

public class MonsterTwo extends Monster
{
    private int maxShot, shotNum;
    private int live;

    public MonsterTwo(BufferedImage[] monsterTwo, int maxLives, int x, int y)
    {
        super(monsterTwo, maxLives/2, x, y);
        shotNum = 0;
        live = maxLives;
        setMoveSpeed(6);
        setMaxShot(2);
    }

    private void setMaxShot(int maxShot)
    {
        if(maxShot > 0)
            this.maxShot = maxShot;
    }

    public int getScore()
    {
        return 200;
    }

    public void shot()
    {
        shotNum++;
        if(shotNum < maxShot)
        {
            startMonster = 6;
            maxLives = live;
        }
        else
            isDead = true;
        repaint();
    }
}
