import java.util.List;
import java.lang.Math; 
import java.util.Set;
import java.util.LinkedHashSet; 
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.awt.Image;
import javax.imageio.ImageIO;
import java.io.InputStream;
import java.io.File;
abstract class Piece{
    private boolean hasMoved;
    private Square curSquare;
    public boolean color;
    private BufferedImage image;
    public Piece(Square curSquare, boolean color, String img) {
        this.curSquare = curSquare;
        this.color = color;
        try {
            image = ImageIO.read(new File("resources/" + img+".png"));
        } catch (IOException e) {
            System.out.println("Image not Found");
            e.printStackTrace();
        }
        hasMoved = false;
    }
    public BufferedImage getImage(){
        return image;
    }
    public void draw(Graphics g, int x, int y) {
        g.drawImage(image, x, y, null);
    }
    public boolean getMoved(){
        return hasMoved;
    }
    public void setMoved(boolean b){
        hasMoved = b;
    }
    public boolean getColor(){
        return color;
    }
    public void setPos(Square s){
        curSquare = s;
    }
    public Square getPos(){
        return curSquare;
    }
    public int getRow(){
        return curSquare.getRow();
    }
    public int getCol(){
        return curSquare.getCol();
    }
    
    public boolean move(Square end){
        boolean out = true;
        Piece occupying = end.getOccupyingPiece();
        if(occupying != null){
            if(occupying.getColor() == color){
                return false;
            } else {
                end.capture(this);
            }
        }
        curSquare.removePiece();
        curSquare = end;
        curSquare.put(this);
        return out;
    }
    public LinkedHashSet<Square> linearCheck(Square[][] b,int startRow, int startCol){
        LinkedHashSet<Square> lin = new LinkedHashSet();
        for(int i = startRow+1; i<8;i++){
            if(b[i][startCol].isOccupied()){
                if(b[i][startCol].getOccupyingPiece().getColor()!=color){
                    lin.add(b[i][startCol]);
                }
                break;
            } else{
                lin.add(b[i][startCol]);
            }
        }
        for(int i = startRow-1; i>-1;i--){
            if(b[i][startCol].isOccupied()){
                if(b[i][startCol].getOccupyingPiece().getColor()!=color){
                    lin.add(b[i][startCol]);
                }
                break;
            } else{
                lin.add(b[i][startCol]);
            }
        }
        for(int i = startCol+1; i<8;i++){
            if(b[startRow][i].isOccupied()){
                if(b[startRow][i].getOccupyingPiece().getColor()!=color){
                    lin.add(b[startRow][i]);
                }
                break;
            } else{
                lin.add(b[startRow][i]);
            }
        }
        for(int i = startCol-1; i>-1;i--){
            if(b[startRow][i].isOccupied()){
                if(b[startRow][i].getOccupyingPiece().getColor()!=color){
                    lin.add(b[startRow][i]);
                }
                break;
            } else{
                lin.add(b[startRow][i]);
            }
        }
        return lin;
    }
    public LinkedHashSet<Square> diagCheck(Square[][] b,int startRow, int startCol){
        LinkedHashSet<Square> diag = new LinkedHashSet();
        int row = startRow;
        int col = startCol;
        int i = Math.min(startRow,startCol);
        while(i>0){
            if(b[--row][--col].isOccupied()){
                if(b[row][col].getOccupyingPiece().getColor()!=color){
                    diag.add(b[row][col]);
                }
                break;
            } else{
                diag.add(b[row][col]);
            }
            i--;
        }
        i = Math.max(startRow,startCol);
        row = startRow;
        col = startCol;
        while(i<7){
            if(b[++row][++col].isOccupied()){
                if(b[row][col].getOccupyingPiece().getColor()!=color){
                    diag.add(b[row][col]);
                }
                break;
            } else{
                diag.add(b[row][col]);
            }
            i++;
        }
        
        row = startRow;
        col = startCol;
        i = Math.min(startRow,7-startCol);
        while(i>0){
            if(b[--row][++col].isOccupied()){
                if(b[row][col].getOccupyingPiece().getColor()!=color){
                    diag.add(b[row][col]);
                }
                break;
            } else{
                diag.add(b[row][col]);
            }
            i--;
        }
        
        row = startRow;
        col = startCol;
        i = Math.min(7-startRow,startCol);
        while(i>0){
            if(b[++row][+--col].isOccupied()){
                if(b[row][col].getOccupyingPiece().getColor()!=color){
                    diag.add(b[row][col]);
                }
                break;
            } else{
                diag.add(b[row][col]);
            }
            i--;
        }
        return diag;
    }
    abstract LinkedHashSet<Square> getLegalMoves(board b);
}