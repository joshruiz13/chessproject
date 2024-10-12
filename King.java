import java.util.Set;
import java.util.HashSet;
import java.lang.Math;
import java.util.LinkedHashSet;

public class King extends Piece{
    private boolean wasMoved;
    private boolean kCastle = false;
    private boolean qCastle = false;
    private board b;
    public King(Square curSquare,boolean color,String img, board b){
        super(curSquare,color,img);
        this.b = b;
    }
    @Override
    public boolean move(Square end){
        boolean output;
        output = super.move(end);
        setMoved(true);
        return output;
    }
    public boolean canCastle(Rook ro, board b){
        if(wasMoved) return false;
        if(ro.getMoved()) return false;
        int row = getRow();
        int k = getCol();
        int r = ro.getCol();
        int direction = r>k ? 1:-1;
        Square[][] board = b.getArray();
        int cur = k+direction;
        while(cur!=r){
            if(board[row][cur].getOccupyingPiece()!=null){
                return false;
            }
            cur+=direction;
        }
        return true;
    }
    public LinkedHashSet<Square> getLegalMoves(board b){
        LinkedHashSet<Square> moves = new LinkedHashSet();
        Square[][]board = b.getArray();
        int row = this.getRow();
        int col = this.getCol();
        for(int i = -1;i<2;i++){
            for(int j = -1;j<2;j++){
                int dRow = row +i;
                int dCol = col +j;
                dRow = Math.max(0,Math.min(7,dRow));
                dCol = Math.max(0,Math.min(7,dCol));
                if(!board[dRow][dCol].isOccupied()){
                    moves.add(board[dRow][dCol]);
                } else{
                    if(board[dRow][dCol].getOccupyingPiece().getColor()!=color){
                        moves.add(board[dRow][dCol]);    
                    }
                }
            }
        }
        if(board[row][0].getOccupyingPiece() instanceof Rook&&col>2){
            if(canCastle((Rook)board[row][0].getOccupyingPiece(), b)){
                qCastle = true;    
                moves.add(board[row][col-2]);
            }
        }
        if(board[row][7].getOccupyingPiece() instanceof Rook&&col<6){
            if(canCastle((Rook)board[row][7].getOccupyingPiece(), b)){
                kCastle = true; 
                moves.add(board[row][col+2]);   
            }
        }
        moves.remove(board[row][col]);
        return moves;
    }
    public String toString(){
        return color == true ? "K":"k";
    }
}