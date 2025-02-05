/****************************************************************************
 *  Compilation:  javac PercolationVisualizer.java
 *  Execution:  java PercolationVisualizer < input.txt
 *  Dependencies: StdIn.java StdOut.java StdDraw.java Percolation.java
 *
 *  Visual testing client for Percolation. Reads in points from StdIn and 
 *  plots the result on canvas. Blue squares are full, white squares are open
 *  and black squares are closed
 * 
 *  N= number of cells
 *  (i,j) point to be opened
 *  input.txt format
 *  
 *  N
 *  i j
 *
 ****************************************************************************/


public class PercolationVisualizer {
    public static void main(String[] args) {
        
        //read in size of grid from StdIn
        int N = StdIn.readInt();
        Percolation perc = new Percolation(N);
        double sqrLength = 0.45; //length of 1/2 of square side, with room for grid
        
        StdDraw.setXscale(0, N);
        StdDraw.setYscale(0, N);
       
        
        
        
        while (!StdIn.isEmpty()) {
            int p = StdIn.readInt();
            int q = StdIn.readInt();
            
            //if not (p,q) not open, open it
            if (!perc.isOpen(p, q))
                perc.open(p, q);
            
            //draw grid
            for (int j = 1; j <= N; j++) {
                for (int i = 1; i <= N; i++) {
                    
                    //if (i,j) is full, draw in blue
                    if (perc.isFull(i, j)) {
                        StdDraw.setPenColor(StdDraw.BLUE);
                        
                        //-.5 shift to account for 10% border in setscale
                        //(N-i) transformation to shift origin to where
                        StdDraw.filledSquare(j - .5 , N- (i -.5) , sqrLength); }
                    
                    //if (i,j) is open, draw in white
                    else if (perc.isOpen(i, j)) {
                        StdDraw.setPenColor(StdDraw.WHITE);
                        StdDraw.filledSquare(j - .5, N- (i -.5), sqrLength); }
                    
                    //if (i,j) is closed, draw in black
                    else {
                        StdDraw.setPenColor(StdDraw.BLACK);
                        StdDraw.filledSquare(j - .5, N- (i -.5), sqrLength); }
                }
            }
        }
    }
}

