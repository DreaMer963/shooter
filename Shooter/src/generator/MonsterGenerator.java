package generator;

import engine.GameEngine;
import monster.Monster;
import monster.MonsterOne;
import monster.MonsterTwo;

import java.awt.image.BufferedImage;
import java.util.Random;
class MonsterGenerator
{
    private BufferedImage[] greenMonster, blueMonster;
    private Random random;
    MonsterGenerator()
    {
        random = new Random(10021356);
        blueMonster = SpriteGenerator.generateSprite("D:\\githubPro\\code\\javaCoding\\Shooter\\src\\images\\creatures/blueMonster.png", 3, 6);
        greenMonster = SpriteGenerator.generateSprite("D:\\githubPro\\code\\javaCoding\\Shooter\\src\\images\\creatures/greenMonster.png", 2, 5);
    }

    Monster monsterGenerator()
    {
        if(random.nextInt(2) == 0)
        {
            return new MonsterOne(greenMonster, 6, (random.nextInt(100) + (GameEngine.WIDTH - 100)), random.nextInt(GameEngine.HEIGHT - 200) + 50);
        }
        else
            return new MonsterTwo(blueMonster, 12, (random.nextInt(100) + (GameEngine.WIDTH - 100)), random.nextInt(GameEngine.HEIGHT - 200) + 50);
    }
}
