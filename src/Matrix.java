import java.util.ArrayList;

/**
 * Represents an interval matrix and operations with matrices..
 * When the constructor is called, it displays
 * a 2D array (of chosen dimension) of text fields on the main frame.
 * To initialize the matrix or matrices, the class also creates a 
 * new 2D array of integers where it stores the values filled in the text fields.
 * 
 * 
 * @author matthires
 *
 */
public class Matrix {	
		private int dim;		
		static final double EPS = -100000;
		private double[][] matrix;
		private double eigVal;
		
		/**
		 * Creates a matrix as a 2d array
		 * @param dimension Dimension of the matrices
		 */
	public Matrix(int dimension){   
		dim = dimension; //dimension of the matrix
		matrix = new double[dim][dim];
	}
	
	/**
	 * Returns the matrix we are working with
	 * @return  matrix as a 2d array
	 */
	public double[][] getMatrix(){		
		return matrix;
	}
	
	/**
	 * Floyd-Warshall algorithm. Finds the less weighted paths from-to every 
	 * vertex. Returns the ordered matrix.
	 * It counts the weights from the definite matrix
	 * (subtracted lambda from the original).
	 *
	 * @return the reordered matrix with the less weighted paths
	 */
	public double[][] getFWMatrix(){
		double[][] fwMatrix = new double[dim][dim];
		double shortPath = 0;
		
		KarpAlgorithm kA = new KarpAlgorithm(this);
		eigVal = kA.getEigenValue();
		double[][] defMatrix = getDefMatrix(eigVal);

		/*saving the definite matrix,
		changing the non infinite values to 0*/
		for(int i = 0;i < dim;i++){
			for(int j = 0;j < dim;j++){
				if(defMatrix[i][j] > -10000){
        			fwMatrix[i][j] = 0;
        		}else{
        			fwMatrix[i][j] = EPS;
        		}
			}
		}
		
		for(int i = 0;i < dim;i++){
        	for(int j = 0;j < dim;j++){        		
        		for(int k = 0;k < dim;k++){
    				shortPath = Math.min( fwMatrix[k][j], 
    						fwMatrix[k][i] + fwMatrix[i][j] );
    				if(shortPath < -10000){
    						shortPath = Math.max( fwMatrix[k][j],
    								fwMatrix[k][i] + fwMatrix[i][j]);
					}
        			if(k == i || j == i){
        				fwMatrix[k][j] = fwMatrix[k][j];        
        			}else{
						fwMatrix[k][j] = shortPath;    					
    				}    					
        		}
        	}
		}
		return fwMatrix;
	}
	
	/**
	 * Counts the Strongly Transitive Closure as 
	 * (E + D)^n-1 [the D is the matrix we are working with]
	 *
	 * @return the Strongly Transitive Closure
	 */
	public double[][] getStrTC(){
		double[][] mtx = new double[dim][dim];
		double[][] defMatrix = getDefMatrix(eigVal);
		
		for(int i=0;i<dim;i++){
			for(int j=0;j<dim;j++){
				if(i == j ||  defMatrix[i][j] > -10000){
					mtx[i][i] = 0;
				}else{
					mtx[i][j] = EPS;
				}
			}
		}
		return powerMatrix(mtx, dim-1);
	}
	
	/**
     * Multiplies 2 matrices from the input.
     * @param m1 the multiplicand matrix 
     * @param2 m2 the multiplier matrix
     * @return Matrix  m1*m2
     */
	public double[][] multiplyMatrix(double[][] m1, double[][] m2){
		double[][] mtx = new double[dim][dim];
    	// temporary list to save the set of numbers we need the maximum of
		ArrayList<Double> temp = new ArrayList<Double>();
		
    	 for (int i=0;i<dim;i++){
             for (int j=0;j<dim;j++){   
                for (int k=0;k<dim;k++){
                    temp.add(m1[i][k] + m2[k][j]);
                }
                if(getMax(temp) < -10000){
                	mtx[i][j] = EPS;
                }else{
                	mtx[i][j] = getMax(temp);
                }              
                temp.clear(); //resetting the list
             }
          }
		return mtx; 
		
	}
	
	
	/**
	 * Returns any needed value of the edge directed from vertex1 to vertex2
	 * @param vertex1 start vertex
	 * @param vertex2 end vertex
	 * @return The weight of the edge from vertex1 to vertex2
	 */
	public double getValueOf(int vertex1, int vertex2) {
		return matrix[vertex1][vertex2];
	}
	 
	/**
     * Gets the minimal number from a set of numbers
     * @param list - list of numbers to get the minimum value from
     * @return the minimal value from the set
     */
    public double getMin(ArrayList<Double> list) {
	   	double min; //variable to save the minimal value to 
		min = list.get(0);
		for(int i=1;i<list.size();i++){
	   		if (min > list.get(i)) {
	   			min = list.get(i);
	   		}
	   	 }    
		 return min;
	 }
	
    /**
     * Counts the definite matrix by subtracting the eigenvalue.
     * @param eigVal the eigenvalue of the matrix
     * @return the definite matrix
     */
    public double[][] getDefMatrix(double eigVal){
    	double[][] defMatrix = new double[dim][dim];
    	
    	for (int i=0;i<dim;i++){
            for (int j=0;j<dim;j++){   
            	defMatrix[i][j] = matrix[i][j] - eigVal;
            }
    	}
    	return defMatrix;
    }
    
    /**
     * A help method for debugging.
     * Prints out the matrix from the input to the console.
     * @param matrix as a 2d array to print out
     */
    public void printMatrix(double[][] matrix){
    	System.out.println("--------------------------------");
    	for(int i=0;i<dim;i++){
			for(int j=0;j<dim;j++){
				System.out.print(matrix[i][j] + "  ");
			}
			System.out.println("\n");
		}
    	System.out.println("--------------------------------");
    }
    
    /**
     * Method for debugging..
     */
    public void debug(){
    	/*double[][] stc = getStrTC();//strongly transitive closure
		double[][] wtc = multiplyMatrix(matrix, stc); //weakly transitive closure	

		double[][] a2 = multiplyMatrix(matrix, matrix); 
		double[][] a3 = multiplyMatrix(a2, matrix); 
		double[][] a4 = multiplyMatrix(a3, matrix); 		
		
		System.out.println("A");
		printMatrix(matrix);
		System.out.println("A^2");
		printMatrix(a2);
		System.out.println("A^3");
		printMatrix(a3);
		System.out.println("A^4");
		printMatrix(a4);   
		System.out.println("stc");
		printMatrix(stc);    	
		System.out.println("wtc");
		printMatrix(wtc);    */
		KarpAlgorithm ka = new KarpAlgorithm(this);
		
		System.out.println(ka.getEigenValue());
		
    }
    
    
	/**
	 * Checks whether the given matrix is a definite matrix or not.
	 * A matrix is definite, when its eigenvalue is equal to 0 and
	 * its digraph is strongly connected. 
	 * A matrix is strongly connected if the Strongly 
	 * and the Weakly transitive closures are equal.
	 * @return true if the matrix is definite, else false
	 */
	public boolean isDefinite(){
		double[][] stc = getStrTC();//strongly transitive closure
		double[][] wtc = multiplyMatrix(matrix, stc); //weakly transitive closure	
		
		ArrayList<Double> diag = new ArrayList<Double>();
		double lambda = 0;
		
		boolean isDef = true;
		for(int i = 0;i < dim;i++){
        	for(int j = 0;j < dim;j++){
        		if(wtc[i][j] != stc[i][j]){
        			isDef = false;
        		}
        		diag.add(powerMatrix(matrix, dim)[i][i]/(i+1));
        	}
    	}		
		lambda = getMax(diag);
		if(lambda != 0 || !isDef){
			isDef = false;
		}
				
		return isDef;
	}
	
	 /**
     * Gets the maximal number from a set of numbers. 
     * @param list  list of numbers to get the maximum value of
     * @return the maximal value from the set
     */
    public double getMax(ArrayList<Double> list) {
    	double max = list.get(0); //variable to save the maximal value to 
   		 
	   	for(int i=0;i<list.size();i++){
	   		if (max < list.get(i)) {
	   			max = list.get(i);
   			}    		  	 	 
	   	}
		return max;
	}
	
	/**
	 * Counts the given power of the matrix from the input.
	 * @param matrix 2d array to count the power of
	 * @param power the power to count 
	 * @return the powered matrix
	 */
	public double[][] powerMatrix(double[][] matrix, int power){
		double[][] poweredMatrix = new double[dim][dim];
		
		for(int h=0;h<power-1;h++){
			if( h == 0){
				poweredMatrix = multiplyMatrix(matrix, matrix);
			}else{
				poweredMatrix = multiplyMatrix(poweredMatrix, matrix);
			}
		}
		
		return poweredMatrix;
	}
	
	/**
	 * Saves all the bases of the given matrix as a list to a list.
	 * A column is a base, when the diagonal value is 0.
	 * @param mtx matrix to search the bases in 
	 * @return list of bases as a nested list
	 */
	public ArrayList<ArrayList<Double>> getBases(double[][] mtx){
		ArrayList<ArrayList<Double>> bases = 
				new ArrayList<ArrayList<Double>>();
		int cnt=0;
		
		for(int i = 0;i < dim;i++){
			if(mtx[i][i] == 0){
				bases.add(new ArrayList<Double>());
	        	for(int j = 0;j < dim;j++){
					bases.get(cnt).add(mtx[i][j]);
				}
				cnt++;
			}
		}
		return bases;
	}
	
	/**
	 * Returns the eigenspace of the matrix as a String, based on its bases.
	 * @param bases list of bases as a nested list
	 * @return String representing the eigenspace
	 */
	public String getEigenSpace(ArrayList<ArrayList<Double>> bases){
		ArrayList<String> indepBases = getIndepBases(bases);
		String[] abc = {"α", "β", "γ", "δ", "ζ", "η", "θ"};
		String eigenSpace = "V(A)= { ";
		String ending = "";
		for(int i=0;i<bases.size();i++){
			if(i==0){
				eigenSpace += abc[i] + "⊗" + indepBases.get(i) + " ";
			}else{
				eigenSpace += "⊕" + abc[i] + "⊗" + indepBases.get(i) + " ";
			}	
			ending += ", " + abc[i];
		}		
		ending += " ∈ ℝ* }";
		
		return eigenSpace + ending;
	}
	
	/**
	 * Checks whether two bases are independent or not.
	 * 
	 * @param d1 base1
	 * @param d2 base2
	 * @return true if they are independent, else false
	 */
	public boolean areIndependent(ArrayList<Double> d1, ArrayList<Double> d2){
		boolean areIndep = true;
		double dif;
		
		if(d1.size() > 1){
			dif = d1.get(0) - d2.get(0);
			for(int i=1;i<dim;i++){			
				if(d1.get(i) - d2.get(i) == dif){
					return false;
				}
			}
		}else{
			return false;
		}
		
		return areIndep;
	}
	
	/**
	 * Checks which bases are independent.
	 * Returns a list of independent bases.
	 * @return list of independent bases
	 */
	public ArrayList<String> getIndepBases(ArrayList<ArrayList<Double>> listOfBases){
		ArrayList<String> indepBases = new ArrayList<String>();
		String base = "Δ";
		int size = listOfBases.size();
	
		/*OPRAVIT
		 * for(int i=0;i<size;i++){
			for(int j=0;j<dim;j++){
				if(i != j && areIndependent(listOfBases.get(i), listOfBases.get(j))){
					if(!indepBases.contains(base+i) && !indepBases.contains(base+j)){
						indepBases.add(base + i);
						indepBases.add(base + j);
					}
				}else{
						indepBases.add(base + i);
					}
			}  
		}*/
		
		return indepBases;
	}
	
	/**
	 * Sets the value of the edge directed from vertex1 to vertex2
	 * @param vertex1 start vertex
	 * @param vertex2 end vertex
	 * @param value - value to be set.
	 */
	protected void setValue(int vertex1, int vertex2, double value) {		
		matrix[vertex1][vertex2] = value;			
	}
	
		
	/**
	 * Returns the set up dimension of the matrix.
	 * @return dimension of the matrix
	 */
	public int getDimension() {		
		return this.dim;
	}
	

}
