import java.util.Set;
import java.util.HashSet;
import java.util.LinkedHashSet;

public class Knight extends Piece{
    public Knight(Square curSquare,boolean color,String img){
        super(curSquare,color, img);
    }
    public LinkedHashSet<Square> getLegalMoves(board b) {
    LinkedHashSet<Square> moves = new LinkedHashSet();
    Square[][] board = b.getArray();
    
    int row = this.getRow();
    int col = this.getCol();

    int[] dx = {2, 2, -2, -2, 1, 1, -1, -1};
    int[] dy = {1, -1, 1, -1, 2, -2, 2, -2};

    for (int i = 0; i < 8; i++) {
        int newRow = row + dx[i];
        int newCol = col + dy[i];

        if (newRow < 8 && newCol <8 && newRow >=0 && newCol >= 0) {
            if(board[newRow][newCol].isOccupied()){
                if(board[newRow][newCol].getOccupyingPiece().getColor()!=color){
                    moves.add(board[newRow][newCol]);
                }
            } else{
                moves.add(board[newRow][newCol]);
            }
        }
    }
    
    return moves;
    }
    public String toString(){
        return color == true ? "N":"n";
    }
}