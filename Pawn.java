import java.util.Set;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.lang.Math;
public class Pawn extends Piece{
    private boolean enpassant;
    private Square passantSquare;
    public Pawn(Square curSquare,boolean color,String img){
        super(curSquare,color, img);
    }
    public void setPassant(boolean b){
        enpassant = b;
    }
    public boolean getPassant(){
        return enpassant;
    }
    public Square getPassantSquare(){
        return passantSquare;
    }
    public void setPassantSquare(Square s){
        passantSquare = s;
    }
    @Override
    public boolean move(Square end){
        if(Math.abs(end.getRow()-this.getPos().getRow())==2) enpassant = true;
        boolean output = super.move(end);
        setMoved(true);
        return output;
    }
    public LinkedHashSet<Square> getLegalMoves(board b){
        LinkedHashSet<Square> moves = new LinkedHashSet();
        Square[][] board = b.getArray();
        int row = this.getRow();
        if(row==0||row==7){
            return moves;
        }
        int col = this.getCol();
        int direction = this.getColor() == true ? -1:1;
        if(board[row+direction*(1)][col].isOccupied()==false){
            moves.add(board[row+direction*(1)][col]);
            if((row==6&&this.getColor())||(row==1&&!this.getColor())){
                if(board[row+(direction*(2))][col].isOccupied()==false){
                    moves.add(board[row+(direction*(2))][col]);
                }
            }
        }
        //captures
        if(col<7){
            if(board[row+direction*(1)][col+1].isOccupied()){
                if(board[row+direction*(1)][col+1].getOccupyingPiece().getColor()!=color){
                    moves.add(board[row+direction*(1)][col+1]);
                }
            }
            if(board[row][col+1].isOccupied()){
                Piece p = board[row][col+1].getOccupyingPiece();
                if(p.getColor()!= color && p instanceof Pawn){
                    if(((Pawn) p).getPassant()){
                       moves.add(board[row+direction*(1)][col+1]);
                       passantSquare = board[row][col+1];
                    }
                }
            }
            if(col>0){
                if(board[row+direction*(1)][col-1].isOccupied()){
                    if(board[row+direction*(1)][col-1].getOccupyingPiece().getColor()!=color){
                        moves.add(board[row+direction*(1)][col-1]);
                    }
                }
                if(board[row][col-1].isOccupied()){
                    Piece p = board[row][col-1].getOccupyingPiece();
                    if(p.getColor()!= color && p instanceof Pawn ){
                        if(((Pawn) p).getPassant()){
                            moves.add(board[row+direction*(1)][col-1]);
                            passantSquare = board[row][col-1];
                        }
                    }
                }
            }
        }
        return moves;
    }
    @Override
    public String toString(){
        return color == true ? "P":"p";
    }
}