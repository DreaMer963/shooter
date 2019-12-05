package engine;

import generator.BoardGenerator;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

public class GameEngine extends JFrame implements ActionListener
{

    public enum GameStatus {NEW, CONTINUE, OVER, WAIT, QUIT, GLORY}
    public final static int WIDTH = 900, HEIGHT = 500;   //设置窗口尺寸
    public final static int RTIME = 150;
    public static Cursor cursor1;
    public static Cursor cursor2;

    private Font font;                                  //字体
    private JPanel gloryPanel;                           //展示最高分的界面
    private JPanel startPanel;                           //开始界面
    private JButton playButton;                          //play 按钮
    private JButton gloryButton;                         //展示最高分的按钮
    private JButton quitButton;                          //退出游戏
    private JButton backButton;                          //返回按钮
    private JTextField gloryContent;
    private Cursor cursor0;
    private static int glory = 0;                         //最高分
    private static GameStatus status;


    @Override
    public Cursor getCursor()
    {
        return cursor0;
    }

    public GameEngine() throws IOException
    {
        this("a small game");
    }
    private GameEngine(String s) throws IOException
    {
        super(s);                  //Todo
        font = new Font(Font.MONOSPACED, Font.BOLD, 26);    //设置字体格式和大小
        loadCursor();
        showGlory();
        setBackButtons();
        setStartPanel();
        setGloryPanel();

        setLayout(null);
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(Color.white);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        setVisible(true);
        pack();

    }

    private void loadCursor()
    {
        Image cursor0Image = Toolkit.getDefaultToolkit().getImage("D:\\githubPro\\code\\javaCoding\\Shooter\\src\\images\\cursors\\cursor1.png");
        cursor0 = Toolkit.getDefaultToolkit().createCustomCursor(cursor0Image, new Point(15, 15), "cursor0");

        Image cursor1Image = Toolkit.getDefaultToolkit().getImage("D:\\githubPro\\code\\javaCoding\\Shooter\\src\\images\\cursors\\cursor2.png");
        cursor1 = Toolkit.getDefaultToolkit().createCustomCursor(cursor1Image, new Point(19, 19), "cursor1");

        Image cursor2Image = Toolkit.getDefaultToolkit().getImage("D:\\githubPro\\code\\javaCoding\\Shooter\\src\\images\\cursors\\cursor3.png");
        cursor2 = Toolkit.getDefaultToolkit().createCustomCursor(cursor2Image, new Point(19, 19), "cursor2");

    }
    //四部曲: set，get, (show)， save（供后续使用）
    //设置最高分
    public static void setGlory(int score)
    {
        if(score >= glory)
            glory = score;
    }

    //获取最高分
    private int getGlory()
    {
        return glory;
    }
    //展示最高分
    private void showGlory() throws IOException
    {
        File file = new File("D:\\githubPro\\code\\javaCoding\\Shooter\\src\\glory.txt");
        if(file.canRead())
        {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String score = reader.readLine();
            if(score == null)
                setGlory(0);
            else
                setGlory(Integer.parseInt(score));        // 字符串转化为整型
            reader.close();
        }
        else
        {
            file.createNewFile();
            setGlory(0);
        }
    }

    //保存最高分
    private void saveGlory() throws IOException
    {
        File file = new File("D:\\githubPro\\code\\javaCoding\\Shooter\\src\\glory.txt");
        if(!file.canWrite())
        {
           file.createNewFile();
        }
        else
        {
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            writer.write(Integer.toString(getGlory()));
            writer.close();
        }
    }

    //status set
    public static void setStatus(GameStatus status)
    {
        GameEngine.status = status;
    }


    //创建按钮
    private JButton createButton(String[] fileOfButton)
    {
        int len = fileOfButton.length;
        ImageIcon[] icons = new ImageIcon[len];
        for(int i=0; i<len; ++i)
        {
            icons[i] = new ImageIcon(fileOfButton[i]);
        }

        JButton button = new JButton(icons[0]);
        button.setBorderPainted(false);
        button.setFocusable(true);
        button.setFocusPainted(false);
        button.setRolloverEnabled(true);
        button.setRolloverIcon(icons[1]);        //Todo...
        button.setPressedIcon(icons[2]);
        button.addActionListener(this);
        return button;
    }

    //设置返回键
    private void setBackButtons()
    {
        backButton = createButton(new String[] {"D:\\githubPro\\code\\javaCoding\\Shooter\\src\\images/buttons/back0.png",
                "D:\\githubPro\\code\\javaCoding\\Shooter\\src\\images/buttons/back1.png", "D:\\githubPro\\code\\javaCoding\\Shooter\\src\\images/buttons/back2.png"});
    }

    //设置开始界面
    private void setStartPanel()
    {
        startPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));  //位置居中
        startPanel.setBackground(Color.BLACK);          //背景颜色为黑色
        startPanel.setCursor(cursor0);
        startPanel.setBounds(0, 0, WIDTH, HEIGHT);

        JPanel smallPanel = new JPanel(new GridLayout(4,1,5,5));
        smallPanel.setPreferredSize(new Dimension(310, 440));
        smallPanel.setBackground(Color.gray);

        playButton = createButton(new String[] {"D:\\githubPro\\code\\javaCoding\\Shooter\\src\\images/buttons/play0.png",
                "D:\\githubPro\\code\\javaCoding\\Shooter\\src\\images/buttons/play1.png", "D:\\githubPro\\code\\javaCoding\\Shooter\\src\\images/buttons/play0.png"});
        gloryButton = createButton(new String[] {"D:\\githubPro\\code\\javaCoding\\Shooter\\src\\images/buttons/glory0.png",
                "D:\\githubPro\\code\\javaCoding\\Shooter\\src\\images/buttons/glory1.png", "D:\\githubPro\\code\\javaCoding\\Shooter\\src\\glory2.png"});
        quitButton = createButton(new String[] {"D:\\githubPro\\code\\javaCoding\\Shooter\\src\\images/buttons/quit0.png",
                "D:\\githubPro\\code\\javaCoding\\Shooter\\src\\images/buttons/quit1.png", "D:\\githubPro\\code\\javaCoding\\Shooter\\src\\images/buttons/quit2.png"});

        smallPanel.add(playButton);
        smallPanel.add(gloryButton);
        smallPanel.add(quitButton);
        startPanel.add(smallPanel);
    }

    //展示最高分界面
    private void setGloryPanel()
    {
        gloryPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        gloryPanel.setCursor(cursor0);
        gloryPanel.setBackground(Color.black);
        gloryPanel.setBounds(0,0,WIDTH,HEIGHT);

        JPanel smalllPanel = new JPanel(new GridLayout(2,1,5,5));
        smalllPanel.setPreferredSize(new Dimension(800, 220));
        smalllPanel.setBackground(Color.gray);

        JLabel label = new JLabel("Your Glory", JLabel.CENTER);
        label.setFont(font);
        label.setForeground(Color.BLACK);
        gloryContent = new JTextField(Integer.toString(glory), 6);
        gloryContent.setEditable(false);
        gloryContent.setFont(font);

        smalllPanel.add(gloryContent, BorderLayout.CENTER);
        smalllPanel.add(label, BorderLayout.NORTH);

        gloryPanel.add(smalllPanel);
        gloryPanel.add(backButton);

    }

    public void readyGo()
    {
        Thread thread = new Thread(
                () ->
                {
                    setStatus(GameStatus.WAIT);
                    add(startPanel);
                    BoardGenerator boardGenerator = null;
                    while (!status.equals(GameStatus.QUIT))
                    {
                        if(status == GameStatus.WAIT)
                        {
                            try
                            {
                                Thread.sleep(200);
                            }
                            catch (InterruptedException e)
                            {
                                e.printStackTrace();
                            }
                        }
                        if(status == GameStatus.NEW)
                        {
                            remove(startPanel);
                            repaint();
                            try {
                                boardGenerator = new BoardGenerator();
                            } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
                                e.printStackTrace();
                            }
                            add(boardGenerator);
                            pack();
                            assert boardGenerator != null;
                            boardGenerator.cycle();
                            setStatus(GameStatus.WAIT);
                        }
                        if(status == GameStatus.CONTINUE) {
                            try
                            {
                                saveGlory();
                            } catch (IOException e)
                            {
                                e.printStackTrace();
                            }
                            remove(boardGenerator);
                            repaint();
                            try {
                                boardGenerator = new BoardGenerator();
                            } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
                                e.printStackTrace();
                            }
                            add(boardGenerator);
                            repaint();
                            pack();
                            assert boardGenerator != null;
                            boardGenerator.cycle();
                            setStatus(GameStatus.WAIT);
                        }
                        if(status == GameStatus.OVER)
                        {
                            try
                            {
                                saveGlory();
                            } catch (IOException e)
                            {
                                e.printStackTrace();
                            }
                            remove(boardGenerator);
                            repaint();
                            add(startPanel);
                            repaint();
                            pack();
                            setStatus(GameStatus.WAIT);
                        }
                        if(status == GameStatus.GLORY)
                        {
                            gloryContent.setText(Integer.toString(getGlory()));
                            remove(startPanel);
                            repaint();
                            add(gloryPanel);
                            repaint();
                            pack();
                            setStatus(GameStatus.WAIT);
                        }
                        if(status == GameStatus.QUIT)
                            break;
                    }
                    dispose();
                    System.exit(0);
                }
        );
        thread.start();
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        if(e.getSource().equals(playButton))
            setStatus(GameStatus.NEW);

        else if(e.getSource().equals(gloryButton))
            setStatus(GameStatus.GLORY);
        else if(e.getSource().equals(quitButton))
            setStatus(GameStatus.QUIT);
        else if(e.getSource().equals(backButton))
        {
            remove(((JButton) e.getSource()).getParent());
            repaint();
            add(startPanel);
            pack();
            setStatus(GameStatus.WAIT);
        }
    }
}

