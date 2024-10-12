import java.util.Set;
import java.util.HashSet;
import java.util.LinkedHashSet;

public class Queen extends Piece{
    public Queen(Square curSquare,boolean color,String img){
        super(curSquare,color,img);
    }
    public LinkedHashSet<Square> getLegalMoves(board b){
        LinkedHashSet<Square> moves = new LinkedHashSet();
        moves = linearCheck(b.getArray(),this.getRow(),this.getCol());
        moves.addAll(diagCheck(b.getArray(),this.getRow(),this.getCol()));
        return moves;
    }
    public String toString(){
        return color == true ? "Q":"q";
    }
}