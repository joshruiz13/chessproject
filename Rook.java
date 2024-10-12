import java.util.Set;
import java.util.HashSet;
import java.util.LinkedHashSet;

public class Rook extends Piece{
    public Rook(Square curSquare,boolean color,String img){
        super(curSquare,color, img);
    }
    @Override
    public boolean move(Square end){
        boolean output = super.move(end);
        setMoved(true);
        return output;
    }
    public LinkedHashSet<Square> getLegalMoves(board b){
        return linearCheck(b.getArray(),this.getRow(),this.getCol());
    }
    public String toString(){
        return color == true ? "R":"r";
    }
}