package demo1;

public class L extends Tetromino {
    public L() {
        /*super(null,null,1000);
        cells=new cell[4];*/
        cells[0] = new cell(0, 4, Tetris.L);
        cells[1] = new cell(0, 3, Tetris.L);
        cells[2] = new cell(0, 5, Tetris.L);
        cells[3] = new cell(1, 3, Tetris.L);

        //共计有四种旋转状态
        states = new State[4];
        //初始化四种状态的相对坐标
        states[0] = new State(0, 0, 0, -1, 0, 1, 1, -1);
        states[1] = new State(0, 0, -1, 0, 1, 0, -1, -1);
        states[2] = new State(0, 0, 0, 1, 0, -1, -1, 1);
        states[3] = new State(0, 0, 1, 0, -1, 0, 1, 1);
    }
}
