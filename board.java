import java.util.LinkedList;
import java.util.Scanner;
import java.util.HashMap;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.Point;
import java.util.HashSet;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.JFrame;

public class board extends JPanel implements MouseListener, MouseMotionListener{
    public LinkedList<Piece> blackPieces;
    public LinkedList<Piece> whitePieces;
    private boolean whiteTurn;
    private RepititionTable reps = new RepititionTable();
    private Square[][] board;
    public Piece curPiece;
    public int curX;
    public int curY;
    public checkDetect cD;
    private ChessBoardPanel cGUI;
    public HashMap<String, Square> squares = new HashMap();
    public HashMap<Square, String> squaresInverse = new HashMap();
    private boolean drag = false;
    private King bK;
    private King wK;
    private Piece lastMoved = null;
    private int sqSize;
    public board(int sqSize){
        board = new Square[8][8];
        blackPieces = new LinkedList<Piece>();
        whitePieces = new LinkedList<Piece>();
        whiteTurn = true;
        this.sqSize = sqSize;
        initializeBoard();
        SwingUtilities.invokeLater(this::initializeGUI);
    }
    public void printBoard(){
        for(int i = 0;i<8;i++){
            for(int j =0;j<8;j++){
                if(board[i][j].getOccupyingPiece()!=null){
                    System.out.print(board[i][j].getOccupyingPiece());
                } else{
                    System.out.print(" ");
                }
            }
            System.out.println();
        }
        System.out.println();
    }
    public boolean isDragging(){
        return drag;
    }
    public void initializeBoard(){
        for(int i =0; i<8;i++){
            for(int j =0;j<8;j++){
                if((i+j)%2==0){
                    board[i][j] = new Square(this,true,i,j);
                } else {
                    board[i][j] = new Square(this,false,i,j);
                }
            }
        }
      
        //pawns
        for(int i = 0; i<8;i++){
            board[1][i].put(new Pawn(board[1][i], false,"black_pawn"));
            board[6][i].put(new Pawn(board[6][i], true,"white_pawn"));
        }
        //rooks
        board[0][0].put(new Rook(board[0][0], false,"black_rook"));
        board[0][7].put(new Rook(board[0][7], false,"black_rook"));
        board[7][0].put(new Rook(board[7][0], true,"white_rook"));
        board[7][7].put(new Rook(board[7][7], true,"white_rook"));
        //Knights
        board[0][1].put(new Knight(board[0][1], false,"black_knight"));
        board[0][6].put(new Knight(board[0][6], false,"black_knight"));
        board[7][1].put(new Knight(board[7][1], true,"white_knight"));
        board[7][6].put(new Knight(board[7][6], true,"white_knight"));
        //bishops
        board[0][2].put(new Bishop(board[0][2], false,"black_bishop"));
        board[0][5].put(new Bishop(board[0][5], false,"black_bishop"));
        board[7][2].put(new Bishop(board[7][2], true,"white_bishop"));
        board[7][5].put(new Bishop(board[7][5], true,"white_bishop"));
        //kings
        bK = new King(board[0][4], false,"black_king", this);
        wK = new King(board[7][4], true,"white_king", this);
        
        board[0][4].put(bK);
        board[7][4].put(wK);
        //queens
        board[0][3].put(new Queen(board[0][3], false,"black_queen"));
        board[7][3].put(new Queen(board[7][3], true,"white_queen"));
        
        
        
        
        for(int i =0; i<8;i++){
            
            whitePieces.add(board[6][i].getOccupyingPiece());
            whitePieces.add(board[7][i].getOccupyingPiece());
            blackPieces.add(board[0][i].getOccupyingPiece());
            blackPieces.add(board[1][i].getOccupyingPiece());
        }

    
        cD = new checkDetect(this, whitePieces,blackPieces,wK,bK);
        for(int i = 0;i<8;i++){
            squares.put("a"+(i+1), board[7-i][0]);
            squares.put("b"+(i+1), board[7-i][1]);
            squares.put("c"+(i+1), board[7-i][2]);
            squares.put("d"+(i+1), board[7-i][3]);
            squares.put("e"+(i+1), board[7-i][4]);
            squares.put("f"+(i+1), board[7-i][5]);
            squares.put("g"+(i+1), board[7-i][6]);
            squares.put("h"+(i+1), board[7-i][7]);
        }
        for(String key:squares.keySet()){
            squaresInverse.put(squares.get(key),key);
        }
    }
    public void nextTurn(){
        if(reps.add(hashBoard())){
            if(whiteTurn){
                if(cD.isStalemate(bK)){
                    cGUI.declareWinner("Draw! Black is in Stalemate");
                }
                if(cD.isMated(bK)){
                    cGUI.declareWinner("White wins by Checkmate");
                }
            } else {
                if(cD.isStalemate(wK)){
                    cGUI.declareWinner("Draw! White is in Stalemate");
                }
                if(cD.isMated(wK)){
                    cGUI.declareWinner("Black wins by Checkmate");
                }
            }
            if(lastMoved!=null){
                if(lastMoved instanceof Pawn) ((Pawn) lastMoved).setPassant(false);
            }
            whiteTurn = whiteTurn==true? false:true;
        } else {
            cGUI.declareWinner("Draw by repitition");
        }
        lastMoved = curPiece;
    }
    private void initializeGUI() {
        JFrame frame = new JFrame("Chess Game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        cGUI = new ChessBoardPanel(this,sqSize);
        frame.add(cGUI);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setVisible(true);
    }
    @Override
    public void mouseClicked(MouseEvent e) {
    }
    public void updateGraphics() {
        if (cGUI != null) {
            cGUI.refresh();
        }
    }
    public Square[][] getArray(){
        return board;
    }
    public void mouseDragged(MouseEvent e) {
        if(drag== true) updateGraphics();
    }
    @Override
    public void mousePressed(MouseEvent e) {
        updateGraphics();
        int col = e.getX();
        int row = e.getY();

        col = col / cGUI.sqSize();
        row = row / cGUI.sqSize();
        
        if (col >= 0 && col < 8 && row >= 0 && row < 8) {
            Square sq = board[row][col];
            if(sq.getOccupyingPiece()==null) return;
            if(sq.getOccupyingPiece().getColor()==true&&!whiteTurn){
                return;
            }
            if(sq.getOccupyingPiece().getColor()==false&&whiteTurn){
                return;
            }
            curPiece = sq.getOccupyingPiece();
            sq.setDisp(false);
            if (curPiece != null) {
                updateGraphics();
            }
            
        }
        drag = true;
    }
    public boolean testMove(Piece curPiece, Square target){
        return cD.testMove(curPiece, target);
    }
    @Override
    public void mouseReleased(MouseEvent e) {  
        int sqSize=cGUI.sqSize();
        if(curPiece!=null){
            curPiece.getPos().setDisp(true);
            int row = e.getY();
            int col = e.getX();
            row = row/sqSize;
            col = col/sqSize;
            if(board[row][col]==curPiece.getPos()){
                curPiece = null;
                drag = false; 
                updateGraphics();
                return;
            }
            Square target = board[row][col];
            HashSet<Square> m = curPiece.getLegalMoves(this);
            boolean t = testMove(curPiece,target);
            Square target2 = null;
            int direction = 0;
            int x = 0;
            boolean castle = false;
            if(curPiece instanceof King){
                if(Math.abs(target.getCol()-curPiece.getCol())==2){
                    if(curPiece.getColor() == true ? cD.whiteChecked():cD.blackChecked()){
                        curPiece = null;
                        drag = false; 
                        updateGraphics();
                        return;
                    }
                    if(t){
                        direction = (target.getCol()-curPiece.getCol())/2;  
                        t = testMove(curPiece,board[row][curPiece.getCol()+direction]);
                        if(t){
                            x = direction > 0 ? 7:0;
                            castle = true;
                        }
                    }
                }
            }
        
            if(curPiece instanceof Pawn){
                target2 = ((Pawn) curPiece).getPassantSquare();
                ((Pawn) curPiece).setPassantSquare(null);
                if(target2!=null){ 
                    Pawn p = (Pawn) target2.getOccupyingPiece();
                    target2.removePiece();
                    if(!testMove(curPiece,target)){
                        t=false;
                    }
                    target2.put(p);
                }
            }
            if(t&&m.contains(target)){
                curPiece.move(target);
                if(target2!=null&&target2.getCol()==target.getCol()) target2.delete();
                if(castle) board[row][curPiece.getCol()-direction].put(board[row][x].removePiece());
                //promotion
                if(curPiece instanceof Pawn && (curPiece.getRow()==7 || curPiece.getRow()==0)){
                    boolean color = curPiece.getColor();
                    target.delete();
                    if(color){
                        target.put(new Queen(board[target.getRow()][target.getCol()], true,"white_queen"));
                        whitePieces.add(target.getOccupyingPiece());
                    }
                    if(!color){ 
                        target.put(new Queen(board[target.getRow()][target.getCol()], false,"black_queen"));
                        blackPieces.add(target.getOccupyingPiece());
                    }
                    cD.upd();
                }
                nextTurn();
            }
            curPiece = null;
            
        }
        drag = false; 
        updateGraphics();
    }

    @Override
    public void mouseEntered(MouseEvent e) { }

    @Override
    public void mouseExited(MouseEvent e) { }
    
    @Override
    public void mouseMoved(MouseEvent e){ }
    
    public long hashBoard(){
        long seed = 1234567890987654321L;
        for(Piece p: whitePieces){
            seed = seed ^ p.getPos().getZobristKey();
        }
        for(Piece p: blackPieces){
            seed = seed ^ p.getPos().getZobristKey();
        }
        return seed;
    }
}