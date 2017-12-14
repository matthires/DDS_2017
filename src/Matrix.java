import java.util.ArrayList;

/**
 * Represents a matrix and operations with matrices..
 * The operations are in the max-plus algebra. 
 * Matrices are represented as 2d arrays.
 * 
 * @author Hires, Gazda
 */
public class Matrix {	
		private int dim;		
		static final double EPS = -100000;
		private double[][] matrix;
		private double eigVal;
		
		/**
		 * Creates a matrix as a 2d array
		 * @param dimension Dimension of the matrix
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
		double shortPath = 0;
		
		KarpAlgorithm kA = new KarpAlgorithm(this);
		eigVal = kA.getEigenValue();
		double[][] fwMatrix = getDefMatrix(eigVal);

		
		for(int i = 0;i < dim;i++){
        	for(int j = 0;j < dim;j++){        		
        		for(int k = 0;k < dim;k++){
    				shortPath = Math.max( fwMatrix[k][j], 
    						fwMatrix[k][i] + fwMatrix[i][j] );
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
				if(i == j){ 
					mtx[i][i] = 0;
				}else if(defMatrix[i][j] > -10000){
					mtx[i][j] = defMatrix[i][j];
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
		ArrayList<Double> temp = new ArrayList<>();
		
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
     * Counts the definite matrix by subtracting the eigenvalue
     * from the given matrix.
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
	 * Saves all the fundamental vectors of the given matrix as a list to a list.
	 * A column is a fundamental vector, when the diagonal value is 0.
	 * @param weakly transitive closure
	 * @return list of fundamental vectors as a nested list
	 */
	public ArrayList<ArrayList<Double>> getFundVectors(double[][] wtc){
		ArrayList<ArrayList<Double>> funVectors = new ArrayList<>();
		int cnt = 0;
		
		for(int i=0;i<dim;i++){
			if(wtc[i][i] == 0){
				ArrayList<Double> vector = new ArrayList<>();
				funVectors.add(vector);
	        	for(int j=0;j<dim;j++){
					funVectors.get(cnt).add(wtc[j][i]);
				}
				cnt++;
			}
		}
		return funVectors;
	}
	
	/**
	 * Returns the eigenspace of the matrix as a String, based on its bases.
	 * @param bases list of bases as a nested list
	 * @return String representing the eigenspace
	 */
	public String getEigenSpace(ArrayList<ArrayList<Double>> bases){
		ArrayList<String> indepBases = getBases(bases);
		String[] abc = {"α", "β", "γ", "δ", "ζ", "η", "θ"};
		String eigenSpace = "V(A) = { ";
		String ending = "";
		for(int i=0;i<indepBases.size();i++){
			if(i==0){
				eigenSpace += abc[i] + "⊗" + indepBases.get(i) + " ";
			}else{
				eigenSpace += "⊕ " + abc[i] + "⊗" + indepBases.get(i) + " ";
			}	
			ending += ", " + abc[i];
		}		
		ending += " ∈ ℝ* }";
		
		return eigenSpace + ending;
	}
	
	/**
	 * Checks whether two vectors are independent or not.
	 * 
	 * @param d1 vector1 as a list of numbers
	 * @param d2 vector2 as a list of numbers
	 * @return true if they are independent, else false
	 */
	public boolean areIndependent(ArrayList<Double> d1, ArrayList<Double> d2){
		boolean areIndep = false;
		double dif;
		
		if(d1.size() > 1){
			dif = d1.get(0) - d2.get(0);
			for(int i=1;i<dim;i++){			
				if(d1.get(i) - d2.get(i) != dif){
					return true;
				}
			}
		}
		
		return areIndep;
	}
	
	/**
	 * Checks which fundamental vectors are independent.
	 * @param listOfFundamentalEigenVectors the list of 
	 * all the fundamental eigenvectors got from the definite matrix.
	 * @return list of independent bases
	 */
	public ArrayList<String> getBases(ArrayList<ArrayList<Double>> listOfFundamentalEigenVectors){
		ArrayList<String> independentBases = new ArrayList<>();
		String base = "Δ";
		int size = listOfFundamentalEigenVectors.size();
		boolean independency = false;
		
		ArrayList<ArrayList<Double>> indBases = new ArrayList<>();
		indBases.add(listOfFundamentalEigenVectors.get(0));
		independentBases.add(base + (1));
		for(int i = 1; i < size; i++) {
			independency = true;
			for(int j = 0; j < indBases.size(); j++) {
				if(!areIndependent(indBases.get(j), listOfFundamentalEigenVectors.get(i))) {
					independency = false;
				}
			}
			if(independency){
				indBases.add(listOfFundamentalEigenVectors.get(i));
				independentBases.add(base + (i+1));
			}
		}
		
		return independentBases;
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
