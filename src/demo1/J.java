package demo1;

import demo1.Tetris;
import demo1.Tetromino;
import demo1.cell;

public class J extends Tetromino {
    public J() {
        /*super(null,null,1000);
        cells=new cell[4];*/
        cells[0] = new cell(0, 4, Tetris.J);
        cells[1] = new cell(0, 3, Tetris.J);
        cells[2] = new cell(0, 5, Tetris.J);
        cells[3] = new cell(1, 5, Tetris.J);

        //共计有四种旋转状态
        states = new State[4];
        //初始化四种状态的相对坐标
        states[0] = new State(0, 0, 0, -1, 0, 1, 1, 1);
        states[1] = new State(0, 0, -1, 0, 1, 0, 1, -1);
        states[2] = new State(0, 0, 0, 1, 0, -1, -1, -1);
        states[3] = new State(0, 0, 1, 0, -1, 0, -1, 1);
    }
}
