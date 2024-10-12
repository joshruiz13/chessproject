import java.util.Set;
import java.util.HashSet;
import java.util.LinkedHashSet;

public class Bishop extends Piece{
    public Bishop(Square curSquare,boolean color,String img){
        super(curSquare,color, img);
    }
    public LinkedHashSet<Square> getLegalMoves(board b){
        return diagCheck(b.getArray(),this.getRow(),this.getCol());
    }
    public String toString(){
        return color == true ? "B":"b";
    }
}