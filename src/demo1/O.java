package demo1;

public class O extends Tetromino {
    public O() {
        /*super(null,null,1000);
        cells=new cell[4];*/
        cells[0] = new cell(0, 4, Tetris.O);
        cells[1] = new cell(0, 5, Tetris.O);
        cells[2] = new cell(1, 4, Tetris.O);
        cells[3] = new cell(1, 5, Tetris.O);

        //共计有零种旋转状态
        states = new State[0];
    }
}
