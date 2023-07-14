package demo1;

public class Z extends Tetromino {
    public Z() {
        /*super(null,null,1000);
        cells=new cell[4];*/
        cells[0] = new cell(1, 4, Tetris.Z);
        cells[1] = new cell(0, 3, Tetris.Z);
        cells[2] = new cell(0, 4, Tetris.Z);
        cells[3] = new cell(1, 5, Tetris.Z);

        //共计有两种旋转状态
        states = new State[2];
        states[0] = new State(0, 0, -1, -1, -1, 0, 0, 1);
        states[1] = new State(0, 0, -1, 1, 0, 1, 1, 0);
    }
}
