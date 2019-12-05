package monster;
import java.awt.image.BufferedImage;

public class MonsterOne extends Monster
{

    public MonsterOne(BufferedImage[] monsterTwo, int maxLives, int x, int y)
    {
        super(monsterTwo, maxLives/2, x, y);
        setMoveSpeed(5);
    }

    public void shot()
    {
        isDead = true;
    }
}
