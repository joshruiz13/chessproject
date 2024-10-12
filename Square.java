import javax.swing.JComponent;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.Dimension;
import java.lang.Math;
import java.util.Random;

public class Square {
    private board b;
    private boolean color;
    private Piece occupyingPiece;
    private int row;
    private int col;
    private boolean displayPiece;
    private int sqSize;
    private long[] zobristKeys = new long[12];
    Random r;
    public Square(board b, boolean color, int row, int col){
        this.b=b;
        this.color=color;
        this.row=row;
        this.col=col;
        r = new Random();
        for(int i = 0; i < 12;i++){
            zobristKeys[i] = r.nextLong();
        }
    }
    public long getZobristKey(){
        Piece p = getOccupyingPiece();
        if(p instanceof Pawn){
            return (p.getColor()==true ? zobristKeys[0]:zobristKeys[1]);
        }
        if(p instanceof Knight){
            return (p.getColor()==true ? zobristKeys[2]:zobristKeys[3]);
        }
        if(p instanceof Bishop){
            return (p.getColor()==true ? zobristKeys[4]:zobristKeys[5]);
        }
        if(p instanceof Queen){
            return (p.getColor()==true ? zobristKeys[6]:zobristKeys[7]);
        }
        if(p instanceof Rook){
            return (p.getColor()==true ? zobristKeys[8]:zobristKeys[9]);
        }
        if(p instanceof King){
            return (p.getColor()==true ? zobristKeys[10]:zobristKeys[11]);
        }
        return 0;
    }
    public void draw(Graphics g, int x, int y, int size) {
        if(displayPiece && occupyingPiece!=null){
            occupyingPiece.draw(g,x,y);
        }
    }
    public board getBoard(){
        return b;
    }
    public boolean getDisp(){
        return displayPiece;
    }
    public boolean getColor(){
        return color;
    }
    public Piece getOccupyingPiece(){
        return occupyingPiece;
    }
    public boolean isOccupied(){
        return occupyingPiece!=null;
    }
    public int getRow(){
        return row;
    }
    public int getCol(){
        return col;
    }
    public void put(Piece p){
        occupyingPiece = p;
        p.setPos(this);
        displayPiece = true; 
    }
    public void setDisp(boolean b){
        displayPiece = b;
    }
    public Piece removePiece(){
        Piece cur = occupyingPiece;
        occupyingPiece = null;
        displayPiece = false;
        return cur;
    }
    public void delete(){
        Piece cur = occupyingPiece;
        if(cur.getColor() == true) b.whitePieces.remove(cur);
        if(cur.getColor() == false) b.blackPieces.remove(cur);
        occupyingPiece = null;
        displayPiece = false;
    }
    public void capture(Piece p){
        Piece cur = getOccupyingPiece();
        if(cur.getColor() == true) b.whitePieces.remove(cur);
        if(cur.getColor() == false) b.blackPieces.remove(cur);
        occupyingPiece = p;
    }
    @Override
    /**
    public String toString(){
        if(occupyingPiece!=null){
            return occupyingPiece.toString();
        } else{
            
            return "o";
        }
    }
    */
    public String toString(){
        return b.squaresInverse.get(this);
    }
}


