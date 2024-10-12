import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

class ChessBoardPanel extends JPanel{
    private static final int SIZE = 8; // 8x8 Chessboard
    private static int SQUARE_SIZE; // Size of each square
    private board chessBoard;
    private String endMessage;
    private boolean gameOver;
    public ChessBoardPanel(board chessBoard, int a) {
        SQUARE_SIZE = a;
        this.chessBoard = chessBoard;
        setPreferredSize(new Dimension(SIZE * SQUARE_SIZE, SIZE * SQUARE_SIZE));
        setBackground(Color.WHITE);
        addMouseListener(chessBoard);
        addMouseMotionListener(chessBoard);
        gameOver = false;
    }
    public void endGame(){
        removeMouseListener(chessBoard);
        removeMouseMotionListener(chessBoard);
    }
    public int sqSize(){
        return SQUARE_SIZE;
    }
    private void drawSquare(Graphics g,int row, int col){
        Square[][] squares = chessBoard.getArray();
        if (squares[row][col].getColor()) {
            g.setColor(Color.WHITE);
        } else {
            g.setColor(new Color(196, 164, 132));
        }
        g.fillRect(col * SQUARE_SIZE, row * SQUARE_SIZE, SQUARE_SIZE, SQUARE_SIZE);        
        // Draw pieces
        Piece piece = squares[row][col].getOccupyingPiece();
        if (piece != null && squares[row][col].getDisp()) {
            drawPiece(g, piece, col, row, false);
        }
    }
    @Override
    protected void paintComponent(Graphics g) {
        if(!chessBoard.isDragging()){
            super.paintComponent(g);
            for (int row = 0; row < 8; row++) {
                for (int col = 0; col < 8; col++) {
                    drawSquare(g,row,col);
                }
            }
        }
        if(chessBoard.isDragging()){
            Point mousePos = getMousePosition();
            if (mousePos != null) {
                int x = mousePos.x - SQUARE_SIZE/2;
                int y = mousePos.y - SQUARE_SIZE/2;
                int row = mousePos.y/SQUARE_SIZE;
                int col = mousePos.x/SQUARE_SIZE;
                row = Math.max(1,Math.min(6,row));
                col = Math.max(1,Math.min(6,col));
                for(int i = -1;i<2;i++){
                    for(int j = -1;j<2;j++){
                        drawSquare(g,row+i,col+j);
                    }
                }
                drawPiece(g,chessBoard.curPiece,x,y,true);
                repaint();
            }
        }
        if (gameOver){
            int outlineWidth = 1;
            g.setColor(Color.BLACK);
            g.setFont(new Font("Serif", Font.BOLD, ((SQUARE_SIZE*2)/3)));
            FontMetrics fm = g.getFontMetrics();
            int x = (getWidth() - fm.stringWidth(endMessage)) / 2;
            int y = (getHeight() / 2) + fm.getAscent();
            g.drawString(endMessage, x-outlineWidth, y-outlineWidth);
            g.drawString(endMessage, x+outlineWidth, y+outlineWidth);
            g.drawString(endMessage, x+outlineWidth, y-outlineWidth);
            g.drawString(endMessage, x-outlineWidth, y+outlineWidth);
            g.setColor(Color.WHITE);
            g.drawString(endMessage, x, y);
        }
    }

    private void drawPiece(Graphics g, Piece piece, int x, int y, boolean drag) {
        BufferedImage image = piece.getImage();
        if(drag){
            g.drawImage(image, x, y, SQUARE_SIZE, SQUARE_SIZE, null);
        } else{
            g.drawImage(image, x * SQUARE_SIZE, y * SQUARE_SIZE, SQUARE_SIZE, SQUARE_SIZE, null);
        }
    }
    public void declareWinner(String message) {
        this.gameOver = true;
        this.endMessage = message;
        endGame();
    }
    
    public void refresh() {
        revalidate();
        repaint();
    }
}