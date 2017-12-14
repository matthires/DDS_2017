import java.util.ArrayList;

/**
 * Karps algorithm to count the eigenvalue of a matrix.
 * It uses the column principle.
 * @author metti
 *
 */
public class KarpAlgorithm {
	private Matrix mtx;
	private double[][] matrix;
	int dim;
	
	public KarpAlgorithm(Matrix mtx){
		this.mtx = mtx;
		this.matrix = mtx.getMatrix();
		this.dim = mtx.getDimension();
	}
	
	 /**
     * This function returns a list of first columns of the matrices A, A^2, A^3, ... A^dim+1;
     * @return list of the first columns of the powered matrices.
     */
    public ArrayList<ArrayList<Double>> getCols(){
        // list to save the first columns of the powered matrices
    	ArrayList<ArrayList<Double>> listOfCols = new ArrayList<ArrayList<Double>>();  
        for(int i=0;i<=dim;i++){
    		listOfCols.add(new ArrayList<Double>());
        	for(int j=0;j<dim;j++){
        		double value = mtx.powerMatrix(matrix, i+1)[j][0];
        		listOfCols.get(i).add(value);
        	}
        }        
        return listOfCols;
    }   

      
     /**
      * Calculates the eigenvalue of the matrix via column principle 
      * @return the eigenvalue of the matrix from the input
      */
     public double getEigenValue(){
    	 ArrayList<Double> listOfMins = new ArrayList<Double>(); //list to save the minimum values 
    	 // temporary list to save the values to get the minimum of
    	 ArrayList<Double> temp = new ArrayList<Double>(); 
    	 //adds the needed values(column principle) to the list to get the max of them 
    	 for(int i=0;i<dim;i++){
    		 temp.clear(); 
			 int k = dim;    		 
    		 for(int j=0;j<dim;j++){
				 temp.add((getCols().get(dim).get(i) - getCols().get(j).get(i)) / (k));  
				 k--;
     		 }
    		 listOfMins.add(mtx.getMin(temp));    		 
    	 } 	    	 
		 return mtx.getMax(listOfMins);    	 
     }
     

}
