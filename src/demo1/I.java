package demo1;
import demo1.Tetris;
import demo1.Tetromino;
import demo1.cell;

public class I extends Tetromino {
    public I(){
        /*super(null,null,1000);
        cells=new cell[4];*/
        cells[0]=new cell(0,4, Tetris.I);
        cells[1]=new cell(0,3,Tetris.I);
        cells[2]=new cell(0,5,Tetris.I);
        cells[3]=new cell(0,6,Tetris.I);
        //共计两种
        states=new State[2];
        //初始化两种状态相对位置
        states[0]=new State(0,0,0,-1,0,1,0,2);
        states[1]=new State(0,0,-1,0,1,0,2,0);
    }
}
