import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;


/**
 * MAX-PLUS ALGEBRA.
 * This program is made for computations in max-plus algebra.
 * Its purpose is to find eigenspace of adefinite matrix using 
 * Floyd-Warshall algorithm.
 * This class stands for the GUI part of the program.
 * 
 * @author Hires, Gazda
 *
 */
public class Gui {
	 private static JLabel title, author, selectDimension, mtxLabel;
	 private static JFrame frame;
	 private static JLabel panel, matrixLayout, eigenSpace, eigenVal;
	 private static JButton  resetBtn, fwButton, stcButton, 
	 			basesButton, eigenSpButton, eigenValButton;
	 private String[] dims = {" ", "1", "2", "3", "4", "5", "6", "7"};
	 private JComboBox<?> dimensions;
	 private Matrix mtx = null;
	 private JTextField[][] mtxField;
	 private int dim;
	 /**
	  * The constructor of the GUI.
	  */
	public Gui(){		
	}
	
	public static void main(String[] args) {
		Gui gui = new Gui();
		gui.startGui();			
	}
	
	/**
	 * Starts the GUI - displays a new window with the given components.
	 * It uses 2 labels, one transparent for the matrices and the 
	 * other one for all the other components like buttons,labels etc.
	 */
	public void startGui(){
		frame = new JFrame("Nájdenie vlastného priestoru definitnej matice pomocou Floydovho-Warshallovho algoritmu");
		frame.pack();
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		panel = new JLabel(new ImageIcon(
				this.getClass().getResource("/resources/background.jpg")));	
        panel.setOpaque(true);
        panel.setLayout(null);
        
        matrixLayout = new JLabel();
        matrixLayout.setOpaque(false);
        matrixLayout.setLayout(null);
        matrixLayout.setSize(1200,1000);
                
		title = new JLabel("NÁJDENIE VLASTNÉHO PRIESTORU DEFINITNEJ MATICE"
				+ " POMOCOU F-W ALGORITMU");
		title.setFont(new Font("San-Serif", Font.BOLD, 22));		
		title.setForeground(Color.white);
		title.setSize(1000, 30);
		title.setLocation(120, 40);
		
		author = new JLabel("Gazda, Hireš, Marková, 2017");
		author.setForeground(Color.white);
		author.setFont(new Font("San-Serif", Font.BOLD, 16));
		author.setSize(300, 30);
		author.setLocation(950, 930);		
		
		selectDimension = new JLabel("Veľkosť matice:");
		selectDimension.setForeground(Color.white);
		selectDimension.setFont(new Font("San-Serif", Font.BOLD, 16));
		selectDimension.setSize(200, 30);
		selectDimension.setLocation(50, 100);
		
		dimensions  = new JComboBox<Object>(dims);
	    dimensions.setSize(50, 30);
	    dimensions.setLocation(200, 100);
	    
		mtxLabel = new JLabel("A =");
		mtxLabel.setFont(new Font("San-Serif", Font.BOLD, 20));
		mtxLabel.setForeground(Color.WHITE);
		mtxLabel.setSize(50,50);
		mtxLabel.setVisible(false);
				
	    resetBtn = new JButton("Vynulovať");
	    resetBtn.setSize(120, 30);
	    resetBtn.setLocation(300, 100);
	    resetBtn.setVisible(false);
	    
	    fwButton = new JButton("[F-W] => ");
	    fwButton.setSize(120, 30);
	    fwButton.setVisible(false);
	    
	    stcButton = new JButton("Γ(D) => ");
	    stcButton.setSize(120, 30);
	    stcButton.setVisible(false);	    		      
	    
	    basesButton = new JButton("Fundamentálne vektory");
	    basesButton.setSize(180, 30);
	    basesButton.setVisible(false);
	    
	    eigenSpButton = new JButton("Vlastný priestor");
	    eigenSpButton.setSize(180, 30);
		eigenSpButton.setLocation(50,850);
	    eigenSpButton.setVisible(false);
	    
	    eigenSpace = new JLabel("");
	    eigenSpace.setForeground(Color.white);
	    eigenSpace.setFont(new Font("San-Serif", Font.BOLD, 20));
	    eigenSpace.setSize(850, 30);
	    eigenSpace.setLocation(250, 850);	
	    
	    eigenValButton = new JButton("Vlastná hodnota");
	    eigenValButton.setSize(130, 30);
	    eigenValButton.setLocation(90,200);
	    eigenValButton.setVisible(false);
	    
	    eigenVal = new JLabel("");
	    eigenVal.setForeground(Color.white);
	    eigenVal.setFont(new Font("San-Serif", Font.BOLD, 20));
	    eigenVal.setSize(50, 30);
	    eigenVal.setLocation(250, 200);	

	    setPanelContents();
		
		frame.setSize(1180, 1000);
		frame.setVisible(true);
		frame.setContentPane(panel);

		
		dimensions.addActionListener (new ActionListener () {
		    public void actionPerformed(ActionEvent e) {
		    	try{
		    		String selectedDim = dimensions.getSelectedItem().toString();
		    		dim = Integer.parseInt(selectedDim);
					reset();					
					showMatrix();	
		    	}catch(NumberFormatException nfe){
		    		reset();
		    		mtxLabel.setVisible(false);
		    	}				
		    }
		});
				
		resetBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				reset();
				dimensions.setSelectedIndex(0);
			}
		});
		
		eigenValButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if(saveMatrix()){
					getEigenVal();
				}
			}
		});
		
		fwButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if(saveMatrix()){						
					showFwMatrix();	
				}
			}
		});
		
		stcButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				showSTClosure();
			}
		});
		
		basesButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				showFundVectors();
			}			
		});
		
		eigenSpButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				showEigenSpace();
				
			}			
		});
		
	}

	/**
	 * Resets all the fields, deletes all the text.
	 */
	public void reset(){
		mtx = new Matrix(dim);
		matrixLayout.removeAll();
		frame.repaint();	
		fwButton.setVisible(false);
		stcButton.setVisible(false);
		basesButton.setVisible(false);
		eigenSpButton.setVisible(false);
		eigenSpace.setVisible(false);
		eigenVal.setVisible(false);
		eigenValButton.setVisible(false);
		mtxLabel.setVisible(false);
	}
	
	/**
	 * Printing out the textfields for the matrix
	 * @param dim dimension of the matrix
	 */
	public void showMatrix(){	
		mtxLabel.setLocation(50, 230 + (dim-1)*35/2);
		mtxLabel.setVisible(true);	

	    fwButton.setLocation(92+(dim*35),  240 + (dim-1)*35/2);
	    fwButton.setVisible(true);
	    
	    resetBtn.setVisible(true);
	    
	    eigenValButton.setVisible(true);
	    
	    mtxField = new JTextField[dim][dim];
        for(int i = 0;i < dim;i++){
        	for(int j = 0;j < dim;j++){	
	            mtxField[i][j] = new JTextField("0", 10);
	            mtxField[i][j].setBounds(90 + j * 35, 240 + i * 35, 30, 30);
	            mtxField[i][j].setHorizontalAlignment(SwingConstants.CENTER);
	            mtxField[i][j].setFont(new Font("San-Serif", Font.BOLD, 16));  
	            final int i1 = i; final int i2 = j;
	            mtxField[i][j].addMouseListener(new MouseListener() {
									
					@Override
					public void mouseReleased(MouseEvent e) {
						
					}
					
					@Override
					public void mousePressed(MouseEvent e) {
						
					}
					
					@Override
					public void mouseExited(MouseEvent e) {
						
					}
					
					@Override
					public void mouseEntered(MouseEvent e) {
						
					}
					
					@Override
					public void mouseClicked(MouseEvent e) {
						mtxField[i1][i2].setText("ε");
					}
				});

            	matrixLayout.add(mtxField[i][j]);
        	}
		}

	}

	/**
	 * Saves the specified values to the matrix
	 * @return true if there is no error, else false
	 */
	public boolean saveMatrix(){
		double value = 0;
		
		for(int i = 0;i < dim;i++){
        	for(int j = 0;j < dim;j++){         		
        		String val = mtxField[i][j].getText();
        		try{
        			value = Double.parseDouble(val);
        			mtx.setValue(i, j, value);   
        		}catch(NumberFormatException nfe){
        			if(val.equals("ε")){
        				mtx.setValue(i, j, Matrix.EPS);
        			}else{
        				JOptionPane.showMessageDialog(frame , 
        					"Matica nie je správne definovaná!",
        						"CHYBA!",JOptionPane.ERROR_MESSAGE);
        				return false;
        			}
        		}
			}     		
		}
		return true;
	}
	
	public void getEigenVal(){
		KarpAlgorithm kA = new KarpAlgorithm(mtx);
		String eVal = Double.toString(kA.getEigenValue());
		
		eigenVal.setVisible(true);
		eigenVal.setText(eVal);
	}
	
	
	/**
	 * Prints out the ordered matrix after using the F-W algorithm.
	 */
	public void showFwMatrix(){
		double[][] matrix = mtx.getFWMatrix();
		String value;
		JTextField fwTextField;
		
		stcButton.setLocation(222 + (dim*70),  240 + (dim-1)*35/2);
	    stcButton.setVisible(true);
		
		for(int i = 0;i < dim;i++){
        	for(int j = 0;j < dim;j++){  
        		if(matrix[i][j] < -10000){
        			fwTextField = new JTextField("ε", 10);
        		}else{
        			value = Double.toString(matrix[i][j]);
        			fwTextField = new JTextField(value, 10);
        		}
        		fwTextField.setBounds(220+(dim*35) + j * 35, 240 + i * 35, 30, 30);
        		fwTextField.setHorizontalAlignment(SwingConstants.CENTER);
        		fwTextField.setFont(new Font("San-Serif", Font.BOLD, 16));
        		fwTextField.setEditable(false);
        		matrixLayout.add(fwTextField);
        	}
		}		
	}
	

	/**
	 * Prints out the Strongly Transitive Closure.
	 */
	public void showSTClosure(){
		//double[][] wtc = mtx.floydWarshall(mtx.getMatrix());
		double[][] stc = mtx.getStrTC();
		String value;
		JTextField stcTextField;
		
		
		for(int i=0;i<dim;i++){
        	for(int j=0;j<dim;j++){  
        		if(stc[i][j] < -10000){
        			stcTextField = new JTextField("ε", 10);
        		}else{
        			value = Double.toString(stc[i][j]);
        			stcTextField = new JTextField(value, 10);
        		}
        		stcTextField.setBounds(350+(dim*70) + j * 35, 240 + i * 35, 30, 30);
        		stcTextField.setHorizontalAlignment(SwingConstants.CENTER);
        		stcTextField.setFont(new Font("San-Serif", Font.BOLD, 16));
        		stcTextField.setEditable(false);
        		matrixLayout.add(stcTextField);
        	}
		}		
	    basesButton.setLocation((315+(dim*97))/2, 505);
		basesButton.setVisible(true);
	}
	
	/**
	 * Prints out all the fundamental vectors from the weakly transitive closure.
	 */
	public void showFundVectors() {
		double[][] wtc = mtx.getFWMatrix();
		ArrayList<ArrayList<Double>> funVectors = mtx.getFundVectors(wtc);
		String value, baseStr = "Δ";
		JLabel base;
		JTextField baseTextField;
		
		for(int i=0;i<funVectors.size();i++){
			base = new JLabel(baseStr + (i+1));
			base.setFont(new Font("San-Serif", Font.BOLD, 16));
			base.setSize(50, 50);
			base.setForeground(Color.white);
			base.setLocation(92 + i * 150, 530);
			matrixLayout.add(base);
			
			for(int j=0;j<funVectors.get(i).size();j++){
				if(funVectors.get(i).get(j) < -10000){
        			baseTextField = new JTextField("ε", 10);
        		}else{
        			value = Double.toString(funVectors.get(i).get(j));
        			baseTextField = new JTextField(value, 10);
        		}
        		baseTextField.setBounds(90 + i * 150, 580 + j * 35, 30, 30);
        		baseTextField.setHorizontalAlignment(SwingConstants.CENTER);
        		baseTextField.setFont(new Font("San-Serif", Font.BOLD, 16));
        		baseTextField.setEditable(false);        		
        		matrixLayout.add(baseTextField);
			}
		}
		eigenSpButton.setVisible(true);
		
	}
	
	/**
	 * Prints out the eigenspace of the given matrix.
	 * (all the linearly independent bases.) 
	 */
	public void showEigenSpace(){
		double[][] wtc = mtx.getFWMatrix();
		ArrayList<ArrayList<Double>> bases = mtx.getFundVectors(wtc);
		eigenSpace.setText(mtx.getEigenSpace(bases));
		eigenSpace.setVisible(true);
	}
	
	/**
	 * Adds the labels and buttons to the main label.
	 */
	public void setPanelContents(){		
	    panel.add(title);
	    panel.add(author);
	    panel.add(matrixLayout);
		panel.add(selectDimension);
		panel.add(dimensions);
		panel.add(resetBtn);
		panel.add(fwButton);
		panel.add(mtxLabel);		
		panel.add(stcButton);
		panel.add(basesButton);
		panel.add(eigenSpButton);
		panel.add(eigenSpace);
		panel.add(eigenVal);
		panel.add(eigenValButton);
	}
		
}
