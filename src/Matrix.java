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
		private double[][] mtx;
		private Gui gui;
		static final double EPS = -100000;
		
		/**
		 * Displays 2 matrices in form of 2D array of text fields on the main frame.
		 * 
		 * @param frame The main frame of the GUI
		 * @param dimension Dimension of the matrices
		 * @param panel Label to display the matrices
		 */
	public  Matrix(Gui gui, int dimension){   
		//the main frame -for updating the screen 
		this.gui = gui;
		dim = dimension; //dimension of the matrix
		mtx = new double[dimension][dimension];
		
	}
	
	/**
	 * Returns the matrix we are working with
	 * @return 2d matrix
	 */
	public double[][] getMatrix(){
		
		for(int i = 0;i < dim;i++){
        	for(int j = 0;j < dim;j++){
        		mtx[i][j] = gui.getMtxValue(i, j);
        	}
		}
		return mtx;
	}
	
	/**
	 * Floyd-Warshall algorithm.
	 * @param mtx the matrix to use the algorithm on
	 * @return the reordered matrix with the less weighted paths
	 */
	public double[][] floydWarshall(double[][] mtx){
		double[][] fwMatrix = mtx;
		double shrtPath = 0;
		
		for(int i = 0;i < dim;i++){
        	for(int j = 0;j < dim;j++){
        		for(int k = 0;k < dim;k++){
    				shrtPath = Math.min( getValueOf(mtx, i, k) 
    					+ getValueOf(mtx, k, j), getValueOf(mtx, i, j));
        			if(shrtPath < -10000){
            			fwMatrix[i][j] = fwMatrix[i][j];        				
        			}
        			else{
        				fwMatrix[i][j] = 0;
        			}
        		}
        	}
		}
		return fwMatrix;
	}
	
	/**
	 * Counts the Strongly Transitive Closure from the
	 * Weakly Transitive Closure
	 * @param wcc the Weakly Transitive Closure
	 * @return the Strongly Transitive Closure
	 */
	public double[][] getStrTC(double[][] wtc){
		double[][] stc = wtc;
		for(int i=0;i<dim;i++){
			stc[i][i] = 0;
		}
		return stc;
	}
	
	
	/**
	 * Returns any needed value of the edge directed from vertex1 to vertex2
	 * @param vertex1 start vertex
	 * @param vertex2 end vertex
	 * @return The weight of the edge from vertex1 to vertex2
	 */
	public double getValueOf(double[][] matrix, int vertex1, int vertex2) {
		return matrix[vertex1][vertex2];
	}
	 
	/**
     * Gets the minimal number from a set of numbers
     * @param list - list of numbers to get the minimum value of
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
	 * Checks whether the given matrix is a definite matrix or not.
	 * A matrix is definite, when its eigenvalue is equal to 0 and
	 * its digraph is strongly connected.
	 * @return true if the matrix is definite, else false
	 */
	public boolean isDefinite(double[][] matrix){
		double[][] wtc = floydWarshall(mtx); //weakly transitive closure
		double[][] stc = getStrTC(wtc); //strongly transitive closure
		ArrayList<Double> diag = new ArrayList<Double>();
		double lambda;
		
		boolean isDef = true;
		for(int i = 0;i < dim;i++){
        	for(int j = 0;j < dim;j++){
        		if(wtc[i][j] != stc[i][j]){
        			isDef = false;
        		}
        		diag.add(powerMatrix(matrix, dim)[i][i]);
        	}
    	}		
		lambda = getMin(diag);
		if(lambda != 0 || !isDef){
			isDef = false;
		}
		
		return isDef;
	}
	
	 /**
     * Gets the maximal number from a set of numbers .It ignores the INF value as it represents only the no-edge.
     * @param list  list of numbers to get the maximum value of
     * @return the maximal value from the set
     */
    public double getMax(ArrayList<Double> list) {
    	double max= 0; //variable to save the maximal value to 
   		 
	   	for(int i=0;i<list.size();i++){
	   		if (max < list.get(i) && list.get(i) < 10000) {
	   			max = list.get(i);
   			}    		  	 	 
	   	}
		return max;
	}
	
	
	public double[][] powerMatrix(double[][] matrix, int power){
		double[][] poweredMatrix = matrix;
		ArrayList<Double> temp = new ArrayList<Double>();
		
		for(int h=0;h<power;h++){
			for(int i = 0;i < dim;i++){
	        	for(int j = 0;j < dim;j++){
	        		for(int k = 0;k < dim;k++){
	        			temp.add(poweredMatrix[i][k] + matrix[k][j]);
	            	}
	            		poweredMatrix[i][j] = getMax(temp);
	        	}
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
	 * Returns the eigenspace of the matrix, based on its bases.
	 * @param bases list of bases as a nested list
	 * @return string representing the eigenspace
	 */
	public String getEigenSpace(ArrayList<ArrayList<Double>> bases){
		ArrayList<String> indepBases = getIndepBases(bases);
		String[] abc = {"α", "β", "γ", "δ", "ζ", "η", "θ"};
		String eigenSpace = "V(A)= { ";
		String ending = "";
		for(int i=0;i<bases.size();i++){
			if(i==0){
				eigenSpace += abc[i] + "⊗" + bases.get(i) + " ";
			}else{
				eigenSpace += "⊕" + bases.get(i) + " ";
			}	
		}		
		ending += ", ∈ ℝ* }";
		
		return eigenSpace + ending;
	}
	
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
	 * Return a list of independent bases.
	 * @return
	 */
	public ArrayList<String> getIndepBases(ArrayList<ArrayList<Double>> listOfBases){
		ArrayList<String> indepBases = null;
		
		/*
		 * IMPLEMENT 
		 */
		return indepBases;
	}
	/**
	 * Sets the value of the edge directed from vertex1 to vertex2
	 * @param vertex1 start vertex
	 * @param vertex2 end vertex
	 * @param value - value to be set.
	 */
	protected void setEdge(double[][] matrix, int vertex1, int vertex2, int value) {		
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
