package generator;

import engine.GameEngine;
import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;


public class BoardGenerator extends JPanel
{

    private MonsterGenerator monsterGenerator;  //生成怪兽
    private GunGenerator gunGenerator;      //武器
    private Timer timer;
    private Font font;
    private JCheckBox musicOn;
    private JButton restart;
    private JButton stop;
    private JLabel labelScore;

    private int score;
    public static boolean isOver = false;
    private boolean isStoped ;

    public BoardGenerator() throws UnsupportedAudioFileException, IOException, LineUnavailableException
    {
        monsterGenerator = new MonsterGenerator();
        isStoped = false;
        isOver = false;
        score = 0;
        font = new Font(Font.MONOSPACED, Font.BOLD, 25);
        setMusicOn();
        setBounds(0, 0, GameEngine.WIDTH, GameEngine.HEIGHT);
        setLayout(null);
        setCursor(GameEngine.cursor1);
        setTopPanel();
        setGunGenerator();
        setbgMusic();
    }

    private void setGunshotOn()
    {
        Clip clip = null;
        File file = new File("D:\\githubPro\\code\\javaCoding\\Shooter\\src\\sound\\gunShoot.wav");
        try
        {
            AudioInputStream sound = AudioSystem.getAudioInputStream(file);
            clip = AudioSystem.getClip();
            clip.open(sound);
        }
        catch (UnsupportedAudioFileException | IOException | LineUnavailableException e)
        {
            e.printStackTrace();
        }
        assert clip != null;
        clip.setFramePosition(0);
        clip.start();
    }

    private void setbgMusic() throws IOException, UnsupportedAudioFileException, LineUnavailableException
    {
        Clip clip;
        File file = new File("D:\\githubPro\\code\\javaCoding\\Shooter\\src\\sound\\bgMusic.wav");

            AudioInputStream music = AudioSystem.getAudioInputStream(file);
            clip = AudioSystem.getClip();
            clip.open(music);
        clip.setFramePosition(0);
        clip.loop(1000000);
    }

    private JButton createButton(Icon icon1, Icon icon2, Icon icon3)
    {
        JButton button = new JButton(icon1);
        button.setBorderPainted(false);
        button.setFocusable(true);
        button.setFocusPainted(false);
        button.setRolloverEnabled(true);
        button.setContentAreaFilled(false);
        button.setRolloverIcon(icon2);
        button.setPressedIcon(icon3);
        button.setPreferredSize(new Dimension(icon1.getIconWidth(), icon1.getIconHeight()));
        button.addActionListener(this::actionPerformed);
        return button;
    }

    private void setTopPanel()
    {
        JLabel showScore = new JLabel("Score: ");
        showScore.setFont(font);
        showScore.setForeground(Color.BLACK);

        //顶部
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        topPanel.setBackground(new Color(0,0,0,0));
        topPanel.setBounds(0,0,GameEngine.WIDTH,50);
        topPanel.setOpaque(true);

        labelScore = new JLabel(Integer.toString(score));
        labelScore.setFont(font);
        labelScore.setForeground(Color.BLACK);

        topPanel.add(showScore);
        topPanel.add(labelScore);
        topPanel.add(musicOn);
        //添加重新开始和暂停按钮
        BufferedImage[] bufferedImages = SpriteGenerator.generateSprite("D:\\githubPro\\code\\javaCoding\\Shooter\\src\\images\\buttons\\inner.png", 2, 3);
        // System.out.println(Arrays.toString(bufferedImages));
        ImageIcon[] icons = new ImageIcon[bufferedImages.length];
        for(int i=0; i<icons.length; i++)
            icons[i] = new ImageIcon(bufferedImages[i]);
        stop = createButton(icons[3], icons[4], icons[5]);
        restart = createButton(icons[0], icons[1], icons[2]);
        topPanel.add(stop);
        topPanel.add(restart);
        add(topPanel);
        topPanel.setVisible(true);
    }

    //射击武器的行为,很重要
    //分两部分: 跟谁鼠标动+命中后点击发射
    private void setGunGenerator()
    {
        gunGenerator = new GunGenerator();
        add(gunGenerator);
        addMouseMotionListener(new MouseMotionListener()
        {
            @Override
            public void mouseDragged(MouseEvent e) { }
            @Override
            public void mouseMoved(MouseEvent e)
            {
                gunGenerator.setRotate(e.getX(), e.getY());
            }
        });

        addMouseListener(new MouseListener()
        {
            @Override
            public void mouseClicked(MouseEvent e)
            {
                GunGenerator.setGunFire(true);
            }

            @Override
            public void mousePressed(MouseEvent e)
            {
                GunGenerator.setGunFire(true);
            }

            @Override
            public void mouseReleased(MouseEvent e)
            {
                GunGenerator.setGunFire(false);
            }

            @Override
            public void mouseEntered(MouseEvent e)
            {
                GunGenerator.setGunFire(true);
            }

            @Override
            public void mouseExited(MouseEvent e)
            {
                GunGenerator.setGunFire(false);
            }
        });
    }

    private void setMusicOn()
    {
        musicOn = new JCheckBox("Music ", true);
        musicOn.setFont(font);
        musicOn.setBackground(new Color(0,0,0,0));
        musicOn.setFocusable(false);
    }

    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        //System.out.println("111");
        Image background = Toolkit.getDefaultToolkit().getImage("D:\\githubPro\\code\\javaCoding\\Shooter\\src\\images\\backgrounds/background.png");
        g.drawImage(background, 0, 0, null);
    }

    private Thread thread()
    {
        for(int i = 0; i<6; i++)
            add(monsterGenerator.monsterGenerator());
        return new Thread(() -> {
            int t = 1000;
            while (!isOver && !isStoped)
            {
                try
                {
                    Thread.sleep(t);
                } catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
                if(isStoped)
                    return;
                if(!isOver)
                    add(monsterGenerator.monsterGenerator());
                if(t > GameEngine.RTIME + 50)
                    t -= 4;
                else
                    t = GameEngine.RTIME + 50;
            }
        });
    }

    private void over()
    {
        timer.stop();
        for(Component c : getComponents())
        {
            if(c instanceof Some)
                ((Some) c).getTimer().stop();
            remove(c);
        }

        int option = JOptionPane.showConfirmDialog(this, ("Score：" + score + "\n Do you want to begin a new?"), "Over",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        GameEngine.setGlory(score);
        if(option == JOptionPane.YES_OPTION)
        {
            GameEngine.setStatus(GameEngine.GameStatus.CONTINUE);
        }
        else
        {
            GameEngine.setStatus(GameEngine.GameStatus.OVER);
        }
    }

    public void cycle()
    {
        thread().start();
        timer = new Timer(GameEngine.RTIME, e -> {
            repaint();
            if(isOver)
            {
                over();
                return;
            }
            for(Component eg : getComponents())
            {
                if(eg instanceof Some)
                {
                    Some i = (Some) eg;
                    if(i.isDead())
                    {
                        if(musicOn.isSelected())
                            setGunshotOn();
                        score += i.getScore();
                        labelScore.setText(Integer.toString(score));
                        remove(eg);
                    }
                }
            }
        });
        timer.start();
    }

    private void gameStop()
    {
        isStoped = true;
        timer.stop();
        for(Component c : getComponents())
        {
            if(c instanceof Some)
                ((Some) c).getTimer().stop();
            remove(c);
        }
    }

    private void actionPerformed(ActionEvent e)
    {
        if(e.getSource() == restart)
        {
            gameStop();
            GameEngine.setStatus(GameEngine.GameStatus.CONTINUE);
        }
        else if(e.getSource() == stop)
        {
            gameStop();
            GameEngine.setStatus(GameEngine.GameStatus.OVER);
        }
    }

}

