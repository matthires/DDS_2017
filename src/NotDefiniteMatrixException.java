import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class NotDefiniteMatrixException extends Exception{
	
	public NotDefiniteMatrixException(JFrame frame){
		JOptionPane.showMessageDialog(frame , 
				"Matica nie je definitná!", "CHYBA!",JOptionPane.ERROR_MESSAGE);
	}
	

}
