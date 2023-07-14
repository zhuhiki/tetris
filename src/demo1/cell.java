package demo1;

import java.awt.image.BufferedImage;
import java.util.Objects;

//小方块类
public class cell
{
    public   int row;
    public  int col;
    public  BufferedImage image;

    public cell() {
    }

    public cell(int row, int col, BufferedImage image) {
        this.row = row;
        this.col = col;
        this.image = image;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public BufferedImage getImage() {
        return image;
    }

    public void setImage(BufferedImage image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return "cell{" +
                "row=" + row +
                ", col=" + col +
                ", image=" + image +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        cell cell = (cell) o;
        return row == cell.row && col == cell.col && Objects.equals(image, cell.image);
    }

    @Override
    public int hashCode() {
        return Objects.hash(row, col, image);
    }
    //编写左移一格方法
    public  void left(){
        col--;
    }
    //右移一格
    public  void right(){
        col++;
    }
    //编写下落一格方法
    public void drop(){
        row++;
    }
}
