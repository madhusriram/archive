/****************************************************************************
 *  Compilation:  javac PercolationStats.java
 *  Execution:  java PercolationStats N T
 *  Dependencies: StdIn.java StdOut.java StdRandom.java
 *                StdStats.java Percolation.java
 *
 *  Calculations percolalation threshold by running a Monte Carlo simlulation
 *  on T different N*N grids. Calculates Standard Deviation and 95% conf
 *  intervals. Calculates total time, average time per trial and Standard
 *  Deviation of times as well
 *
 ****************************************************************************/


public class PercolationStats {
    public static void main(String[] args) {
        
        int N = Integer.parseInt(args[0]); //number of rows/columns
        int T = Integer.parseInt(args[1]); //number of trials to run
        int numCells = N*N; //total number of cells in grid
        
        //array to store calculated threshold for each trial
        double[] threshold = new double[T]; 
        //array to store calculated time for each trial
        double[] time = new double[T];
        
        
        
        //loop through T trials
        for (int i = 0; i < T; i++) {
            
            int count = 0;
            Stopwatch watch = new Stopwatch();
            Percolation perc = new Percolation(N);
            
            //open cells at random until perc percolates
            while (!perc.percolates()) {
                
                double q = StdRandom.random(); 
                int rand1 = (int) (q * N) +1;
                double r = StdRandom.random(); 
                int rand2 = (int) (r * N) + 1;
                
                //check to see if random cell is already open
                if (!perc.isOpen(rand1, rand2)) {
                    
                    //if not, open cell and increase count
                    perc.open(rand1, rand2);
                    count++;
                    
                }
            }
            
            //calc proportion of open cells and store
            time[i] = watch.elapsedTime();
            double percent = (double) count / numCells;
            threshold[i] = percent;
            
        }
        
        //print various statistics
        
        System.out.print("mean percolation threshold = ");
        double calcThresh = StdStats.mean(threshold);
        System.out.println(calcThresh);
        
        System.out.print("stddev                     = ");
        double calcDev = StdStats.stddev(threshold);
        System.out.println(calcDev);
        
        System.out.print("95% confidence interval    = ");
        
        double k = 1.96;
        double calcMin = calcThresh - (k*calcDev)/(Math.sqrt(T));
        double calcMax = calcThresh + (k*calcDev)/(Math.sqrt(T));
        
        System.out.println("[" + calcMin + "," + calcMax + "]");
        System.out.println();
        System.out.print("total time                 = ");
        System.out.println(StdStats.sum(time));
        System.out.print("mean time per experiment   = ");
        System.out.println(StdStats.sum(time)/T);
        System.out.print("stddev                     = ");
        System.out.println(StdStats.stddev(time));
    }
}