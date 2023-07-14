package demo1;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class Tetris extends JPanel {
    private Timer timer;
    //声明正在下落的方块
    public Tetromino currentOne=Tetromino.randomOne();
    //将要下落的方块
    public Tetromino nextOne=Tetromino.randomOne();
    //暂存的方块
    public Tetromino holdOne;
    public Tetromino tempOne;
    //正在下落的方块的初始方块状态
    public Tetromino startcurrentOne;
    //游戏主区域
    public cell[][] wall=new cell[15][9];
    //单元格的值为48
    public  static final int CELL_SIZE=48;
    //游戏分数值
    int[] scores_pool={0,1,2,5,10};
    //获得游戏的分数
    public int totalScore=0;
    //当前已消除行数
    public int totaline=0;
    //游戏三个状态，游戏中，暂停，游戏结束
    public  static final int PLAYING=0;
    public static final int PAUSE=1;
    public static final int GAMEOVER=2;
    //暂存状态
    public int hold=0;
    public int startchance=1;
    //暂存机会
    public int holdchance=1;
    //存放状态
    public int game_state;
    //显示游戏状态
    String[] show_state={"P[pause]","C[continue]","S[replay]"};
    //载入方块图片
    public static BufferedImage I;
    public static BufferedImage J;
    public static BufferedImage L;
    public static BufferedImage O;
    public static BufferedImage S;
    public static BufferedImage T;
    public static BufferedImage Z;
    public static BufferedImage backImage;

    static {
        try {
            I = ImageIO.read(new File("images/I.png"));
            J = ImageIO.read(new File("images/J.png"));
            L = ImageIO.read(new File("images/L.png"));
            O = ImageIO.read(new File("images/O.png"));
            S = ImageIO.read(new File("images/S.png"));
            T = ImageIO.read(new File("images/T.png"));
            Z = ImageIO.read(new File("images/Z.png"));
            backImage =ImageIO.read(new File("images/background.png"));


        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void drawGame(Graphics g) {
        g.drawImage(backImage, 0, 0, null);
        // 平移坐标轴
        g.translate(300, 15);
        // 绘制游戏主区域
        paintwall(g);
        // 绘制正在下落四方格
        paintCurrentOne(g);
        // 下一个四方格
        paintNextOne(g);
        if (hold > 0) {
            // 暂存方块
            paintHoldOne(g);
        }
        // 绘制游戏得分
        paintScore(g);
        // 绘制游戏状态
        paintState(g);
    }
    @Override
    public void paint(Graphics g) {
        drawGame(g);
    }
    //键盘监听
    /*public void start()
    {
        game_state=PLAYING;
        KeyListener l=new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {

                int code=e.getKeyCode();
                switch (code){
                    case KeyEvent.VK_DOWN:
                        sortDropAction();//下移
                        break;
                    case KeyEvent.VK_LEFT:
                        moveLeftAction();//左移
                        break;
                    case KeyEvent.VK_RIGHT:
                        moveRightAction();//右移
                        break;
                    case KeyEvent.VK_UP:
                        rotateRightAction();//顺时针旋转
                        break;
                    case KeyEvent.VK_SPACE:
                        handDropAction();//瞬间下落
                        break;
                    case KeyEvent.VK_SHIFT://方块暂存
                        if((game_state==PLAYING)&&holdchance==1) {
                            holdcode();
                            holdchance=0;
                        }break;
                    case KeyEvent.VK_P:
                        if(game_state==PLAYING){
                            game_state=PAUSE;
                        }
                        break;
                    case KeyEvent.VK_C:
                        if(game_state==PAUSE){
                            game_state=PLAYING;
                        }
                        break;
                    case KeyEvent.VK_S:
                        game_state=PLAYING;
                        wall=new cell[15][9];
                        currentOne=Tetromino.randomOne();
                        nextOne=Tetromino.randomOne();
                        totalScore=0;
                        totaline=0;
                        hold=0;
                        break;
                }
            }
        };
        //将俄罗斯方块窗口设置为焦点
        this.addKeyListener(l);
        this.requestFocus();

        //方块自动下落
        while (true)
        {
            if(game_state==PLAYING)
            {
                try {
                    Thread.sleep(500);
                }catch (InterruptedException e){
                    e.printStackTrace();
                }
                if(canDrop()){
                    if(startchance==1)
                    {
                        try {
                            startcurrentOne = (Tetromino) currentOne.clone();
                        } catch (CloneNotSupportedException e) {
                            e.printStackTrace();
                        }
                        startchance=0;
                    }
                    currentOne.softDrop();
                }else{
                    landTowall();
                    destroyLine();
                    if(isGameOver()){
                        game_state=GAMEOVER;
                    }else {
                        currentOne=nextOne;
                        nextOne=Tetromino.randomOne();
                    }
                }
            }
            repaint();
        }
    }*/
    //顺时针旋转
    public void rotateRightAction(){
        currentOne.rotateRight();
        if(outOfBound()||coincide()){
            currentOne.rotateLeft();
        }
    }
    //瞬间下落
    public void handDropAction(){
        while(true){
            if(canDrop()){
                currentOne.softDrop();
            }else {
                break;
            }
        }
        //嵌入到墙中
        landTowall();
        //判断能不能消行
        destroyLine();
        //判断游戏是否结束
        if(isGameOver()){
            game_state=GAMEOVER;
        }else{
            //游戏没有结束，继续生成新的方块
            currentOne=nextOne;
            nextOne=Tetromino.randomOne();
        }
    }
    //判断四方格能否下落
    public boolean canDrop(){
        cell[] cells= currentOne.cells;
        for (cell cell : cells) {
            int row = cell.getRow();
            int col = cell.getCol();
            //判断是否到底
            if (row == wall.length - 1)
            {
                holdchance=1;
                startchance=1;
                return false;
            } else if (wall[row + 1][col] != null)
            {
                holdchance=1;
                startchance=1;
                return false;
            }
        }
        return true;
    }
    //按键四方格下落
    public void sortDropAction(){
        if(canDrop())
        {
            currentOne.softDrop();
        }else {
            //将四方格嵌入到墙中
            landTowall();
            //判断能否消行
            destroyLine();
            //判断游戏是否结束
            if(isGameOver()){
                game_state=GAMEOVER;
            }else {
                //当游戏未结束时继续生成
                currentOne=nextOne;
                nextOne=Tetromino.randomOne();
            }
        }
    }

    public void landTowall() {
        cell[] cells= currentOne.cells;
        for (cell cell : cells) {
            int row=cell.getRow();
            int col=cell.getCol();
            wall[row][col]=cell;
        }
    }

    //消行方法
    public void destroyLine(){
        //统计当前消除行数
        int line=0;
        cell[] cells= currentOne.cells;
        for (cell cell : cells) {
            int row=cell.getRow();
            //判断当前行是否满
            if(isFullLine(row)){
                line++;
                for(int i=row;i>0;i--)
                {
                    System.arraycopy(wall[i-1],0,wall[i],0,wall[0].length);
                }
                wall[0]=new cell[wall[0].length];
            }
        }
        //在分数池中获取分数
        totalScore+=scores_pool[line];
        //统计总行数
        totaline+=line;
    }

    //判断当前行是否已满
    public boolean isFullLine(int row){
        cell[] cells=wall[row];
        for (cell cell : cells) {
            if(cell==null){
                return false;
            }
        }
        return true;
    }
    //判断游戏是否结束
    public boolean isGameOver(){
        cell[] cells=nextOne.cells;
        for (cell cell : cells) {
            int row=cell.getRow();
            int col=cell.getCol();
            if(wall[row][col]!=null){
                return true;
            }
        }
        return false;
    }

    public void paintState(Graphics g) {
        if(game_state==PLAYING){
            g.drawString(show_state[PLAYING],-250,400);
        }
        else if(game_state==PAUSE){
            g.drawString(show_state[PAUSE],-250,400);
            g.setColor(Color.red);
            g.setFont(new Font(Font.SANS_SERIF,Font.BOLD,60));
            g.drawString("PAUSING",30,400);
            g.drawString("PRESS C TO CONTINUE",-100,500);

        }else if(game_state==GAMEOVER){
            g.drawString(show_state[GAMEOVER],-250,400);
            g.setColor(Color.red);
            g.setFont(new Font(Font.SANS_SERIF,Font.BOLD,60));
            g.drawString("GAMEOVER",30,400);
            g.drawString("PRESS S TO REPLAY",-100,500);

        }
    }

    public void paintScore(Graphics g) {
        g.setFont(new Font(Font.SANS_SERIF,Font.BOLD,30));
        g.drawString("SCOREC:"+totalScore,470,300);
        g.drawString("LINES:"+totaline,470,600);
    }

    public void paintNextOne(Graphics g) {
        g.setFont(new Font(Font.SANS_SERIF,Font.BOLD,30));
        g.drawString("下一个方块",470,25);
        g.drawString("按SHIFT暂存方块",-270,30);
        g.drawString("暂存的方块",-250,110);
        g.drawString("按方向键操作方块",-270,600);
        g.drawString("按空格方块瞬间下落",-290,700);
        cell[] cells=nextOne.cells;
        for (cell cell : cells) {
            int x=cell.getCol()*CELL_SIZE+330;
            int y=cell.getRow()*CELL_SIZE+50;
            g.drawImage(cell.getImage(),x,y,null);
        }
    }

    public void paintCurrentOne(Graphics g) {
        cell[] cells=currentOne.cells;
        for (cell cell : cells) {
            int x=cell.getCol()*CELL_SIZE;
            int y=cell.getRow()*CELL_SIZE;
            g.drawImage(cell.getImage(),x,y,null);
        }
    }
    public void paintHoldOne(Graphics g) {

        cell[] cells= holdOne.cells;
        for (cell cell : cells) {
            int x=cell.getCol()*CELL_SIZE-400;
            int y=cell.getRow()*CELL_SIZE+150;
            g.drawImage(cell.getImage(),x,y,null);
        }
    }

    public void paintwall(Graphics g) {
        for(int i=0;i< wall.length;i++){
            for(int j=0;j<wall[i].length;j++){
                int x=j*CELL_SIZE;
                int y=i*CELL_SIZE;
                cell cell=wall[i][j];
                //判断当前单元格是否有小方块
                if(cell==null){
                    g.drawRect(x,y,CELL_SIZE,CELL_SIZE);
                }else {
                    g.drawImage(cell.getImage(),x,y,null);
                }
            }
        }
    }
    //判断游戏是否出界
    public boolean outOfBound(){
        cell[] cells= currentOne.cells;
        for (cell cell : cells) {
            int col=cell.getCol();
            int row=cell.getRow();
            if(row<0||row>wall.length-1||col<0||col>wall[0].length-1){
                return true;
            }
        }
        return false;
    }
    //判断方块是否重合
    public boolean coincide(){


        cell[] cells= currentOne.cells;
        for (cell cell : cells) {
            int row=cell.getRow();
            int col=cell.getCol();
            if(wall[row][col]!=null){
                holdchance=1;
                startchance=1;
                return true;
            }
        }
        return false;
    }
    //按键四方格左移
    public void moveLeftAction(){
        currentOne.moveLeft();
        //判断越界或者重合
        if(outOfBound()||coincide()){
            currentOne.moveRight();
        }
    }
    //右移
    public void moveRightAction(){
        currentOne.moveRight();
        if(outOfBound()||coincide()){
            currentOne.moveLeft();
        }
    }
    //方块暂存
    public void holdcode(){
        //if(!coincide())
        if(hold==0)
        {
            holdOne=startcurrentOne;
            currentOne=nextOne;
            startcurrentOne=nextOne;
            nextOne=Tetromino.randomOne();
            hold++;
        }
        else
        {

            tempOne=startcurrentOne;
           currentOne=holdOne;
            holdOne=tempOne;
        }
    }


    public static void main(String[] args)
    {

        SwingUtilities.invokeLater(() -> new LoginRegisterGUI());
        //LoginRegisterGUI.opengame();

    }


    public  static void opengame()
    {
        //创建窗口
        JFrame frame=new JFrame("俄罗斯方块");
        //创建游戏界面
        Tetris panel=new Tetris();
        //面板嵌入窗口
        frame.setSize(1000,850);
        frame.add(panel);

        //设置可见
        frame.setVisible(true);
        //设置尺寸

        //窗口居中
        frame.setLocationRelativeTo(null);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //游戏主要逻辑
        panel.timer = new Timer(500, e ->
        {
            if (panel.game_state == PLAYING) {
                if (panel.canDrop()) {
                    if (panel.startchance == 1) {
                        try {
                            panel.startcurrentOne = (Tetromino) panel.currentOne.clone();
                        } catch (CloneNotSupportedException ex) {
                            ex.printStackTrace();
                        }
                        panel.startchance = 0;
                    }
                    panel.currentOne.softDrop();
                } else {
                    panel.landTowall();
                    panel.destroyLine();
                    if (panel.isGameOver()) {
                        panel.game_state = GAMEOVER;
                    } else {
                        panel.currentOne = panel.nextOne;
                        panel.nextOne = Tetromino.randomOne();
                    }
                }
            }
            panel.repaint(); // 触发重绘操作

        });

        // 添加键盘监听器
        panel.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                int code = e.getKeyCode();
                switch (code) {
                    case KeyEvent.VK_DOWN:
                        panel.sortDropAction();//下移
                        break;
                    case KeyEvent.VK_LEFT:
                        panel.moveLeftAction();//左移
                        break;
                    case KeyEvent.VK_RIGHT:
                        panel.moveRightAction();//右移
                        break;
                    case KeyEvent.VK_UP:
                        panel.rotateRightAction();//顺时针旋转
                        break;
                    case KeyEvent.VK_SPACE:
                        panel.handDropAction();//瞬间下落
                        break;
                    case KeyEvent.VK_SHIFT://方块暂存
                        if ((panel.game_state == PLAYING) && panel.holdchance == 1) {
                            panel.holdcode();
                            panel.holdchance = 0;
                        }
                        break;
                    case KeyEvent.VK_P:
                        if (panel.game_state == PLAYING) {
                            panel.game_state = PAUSE;
                        }
                        break;
                    case KeyEvent.VK_C:
                        if (panel.game_state == PAUSE) {
                            panel.game_state = PLAYING;
                        }
                        break;
                    case KeyEvent.VK_S:
                        panel.game_state = PLAYING;
                        panel.wall = new cell[15][9];
                        panel.currentOne = Tetromino.randomOne();
                        panel.nextOne = Tetromino.randomOne();
                        panel.totalScore = 0;
                        panel.totaline = 0;
                        panel.hold = 0;
                        break;
                }
            }
        });

        panel.requestFocus(); // 将面板设置为焦点
        panel.timer.start(); // 启动定时器
    }


}

