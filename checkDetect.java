    import java.util.List;
    import java.util.LinkedList;
    import java.util.HashSet;
    import java.util.LinkedHashMap;
    import java.util.HashMap;
    public class checkDetect{
        private board b;
        private LinkedList<Piece> wPieces;
        private LinkedList<Piece> bPieces;
        private King wK;
        private King bK;
        Square[][] board;
        //Maps each square to the pieces that can move to that square
        public LinkedHashMap<Square, List<Piece>> wMoves;
        public LinkedHashMap<Square, List<Piece>> bMoves;
        public checkDetect(board b, LinkedList<Piece> wPieces, LinkedList<Piece> bPieces, King wK, King bK) {
            this.b = b;
            this.wPieces = wPieces;
            this.bPieces = bPieces;
            this.wK = wK;
            this.bK = bK;
            wMoves = new LinkedHashMap<Square,List<Piece>>();
            bMoves = new LinkedHashMap<Square,List<Piece>>();
            board = b.getArray();
            for(int row =0;row<8;row++){
                for(int col = 0; col<8;col++){
                    wMoves.put(board[row][col], new LinkedList<Piece>());
                    bMoves.put(board[row][col], new LinkedList<Piece>());
                    
                }
            }
            upd();
        }
        public void upd(){
            for (List<Piece> pieces : wMoves.values()) {
                pieces.clear();
            }
            for (List<Piece> pieces : bMoves.values()) {
                pieces.clear();
            }
            for(Piece p: wPieces){
                HashSet<Square> moves = p.getLegalMoves(b);
                for(Square s : moves){
                    List<Piece> piece= wMoves.get(s);
                    piece.add(p);
                }
            }
            for(Piece p: bPieces){
                HashSet<Square> moves = p.getLegalMoves(b);
                for(Square s : moves){
                    List<Piece> piece= bMoves.get(s);
                    piece.add(p);
                }
            }
        }
        public List<Piece> getThreats(Square s,boolean color){
            List<Piece> threats = color == true ? bMoves.get(s):wMoves.get(s);
            
            return threats;
        }
        public boolean blackChecked(){
            return !getThreats(bK.getPos(),false).isEmpty();
        }
        public boolean whiteChecked(){
            return !getThreats(wK.getPos(),true).isEmpty();
        }
        public boolean testMove(Piece cur,Square moveTo){
            boolean moved = false;
            boolean passant = false;
            if(cur instanceof Rook||cur instanceof Pawn|| cur instanceof King){
                moved = cur.getMoved();   
            }
            if(cur instanceof Pawn) passant = ((Pawn) cur).getPassant();
            boolean out = true;
            Piece temp = moveTo.getOccupyingPiece();
            Square start = cur.getPos();
            cur.move(moveTo);
            upd();
            if(cur.getColor()==false&&blackChecked()){
                out=false;
            } else if(cur.getColor()==true&&whiteChecked()){
                out = false;
            }
            moveTo.removePiece();
            start.put(cur);
            if(temp!=null){
                moveTo.put(temp);
                if(cur.getColor()==false) b.whitePieces.add(temp);
                if(cur.getColor()==true) b.blackPieces.add(temp);
            }
            if(cur instanceof Rook||cur instanceof Pawn|| cur instanceof King){
                cur.setMoved(moved);
            }
            if(cur instanceof Pawn) ((Pawn) cur).setPassant(passant);
            upd();
            return out;
        }
        public boolean isStalemate(King k){
            upd();
            if(k.getColor() ? whiteChecked() : blackChecked()){
                return false;
            }
            List<Piece> pieces = k.getColor() ? wPieces : bPieces;
            for (Piece piece : pieces) {
                HashSet<Square> legalMoves = piece.getLegalMoves(b);
                for (Square moveTo : legalMoves) {
                    if (testMove(piece, moveTo)) {
                        return false;
                    }
                }
            }
            return true;
        }
        public boolean isMated(King k){
            upd();
            if(k.getColor()==true ? whiteChecked() : blackChecked()){
                List<Piece> pieces = k.getColor() ? wPieces : bPieces;
                for (Piece piece : pieces) {
                    HashSet<Square> legalMoves = piece.getLegalMoves(b);
                    for (Square moveTo : legalMoves) {
                        if (testMove(piece, moveTo)) {
                            return false;
                        }
                    }
                }
                return true;
            }
            return false;
        }
}
