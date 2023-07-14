package demo1;

public class S extends Tetromino {
    public S() {
        /*super(null,null,1000);
        cells=new cell[4];*/
        cells[0] = new cell(0, 4, Tetris.S);
        cells[1] = new cell(0, 5, Tetris.S);
        cells[2] = new cell(1,3, Tetris.S);
        cells[3] = new cell(1, 4, Tetris.S);

        //共计有两种旋转状态
        states = new State[2];
        //初始化两种状态相对坐标
        states[0] = new State(0, 0, 0, 1, 1, -1, 1, 0);
        states[1] = new State(0, 0, 1, 0, -1, -1, 0, -1);
    }
}
