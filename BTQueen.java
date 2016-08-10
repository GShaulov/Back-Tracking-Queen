import java.awt.Color;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.util.Vector;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class BTQueen extends JPanel{
	/*****************************************
	 * Program:	Back Tracking Queen	         *
	 * Author:	Grigory Shaulov 317572386    *
	 *****************************************/
	
	//version
	private static final long serialVersionUID = 151010L;
	
	//variables
	private Vector<Integer> board;
	private int n;
	private static int option=-1;
	private static int solution=0;
	private static ImageIcon qIcon2 = new ImageIcon("res/qIcon2.png");	
	private static ImageIcon qIcon = new ImageIcon("res/qIcon.png");	
	private static Image qImage = new ImageIcon("res/qImage.png").getImage();
	
	//Constructor
	public BTQueen(Vector<Integer> b){
		this.board=b;
		this.n=b.size();
	}
	
	//Main  
	public static void main(String[] args){
		
		//Build User Interface
        JPanel p = new JPanel(new GridLayout(10, 1));
        p.add(new JLabel(qIcon2,JLabel.CENTER));
        p.add(new JLabel(" ",JLabel.CENTER));
        p.add(new JLabel("\"BACK TRACKING QUEEN\"", JLabel.CENTER),JLabel.TOP);
        p.add(new JLabel(" ",JLabel.CENTER));
        p.add(new JLabel("Fill in the form, then click \"OK\" to continue.",JLabel.CENTER));
        
        //input number of queens
        p.add(new JLabel("Number of Queens:"));
        JTextField queens = new JTextField("");
        p.add(queens);
        
        //choose solution option
        p.add(new JLabel("Solution Option:"));      
        String[] options = {"All Valid Solutions","First Valid Solution"};
        JComboBox<String> op = new JComboBox<>(options);
        p.add(op);

        //Show Interface
		int result = JOptionPane.showConfirmDialog(null, p, "BTQueen", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        
        //check if OK clicked 
        if (result == JOptionPane.OK_OPTION){
        	
        	//While is no number of Queens entered
        	while(result == JOptionPane.OK_OPTION && (queens.getText().equals("") || isUserInputValid(queens.getText())==false)){
  
        		//Ask for user input VALID number of Queens
            	JOptionPane.showMessageDialog(p, "Enter Valid Number of Queens!", "BTQueen Error", JOptionPane.ERROR_MESSAGE,qIcon);
        		result = JOptionPane.showConfirmDialog(null, p, "BTQueen", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        	}//while
        	option = op.getSelectedIndex();
			Vector<Integer> board = construct();
        	//After it is VALID data inputed check click OK
            if (result == JOptionPane.OK_OPTION){
    			int num = Integer.parseInt(queens.getText());
    			backQ(board, num,0);  
                if(solution==0){
                	System.out.println("Threre Is No Solutions With "+ num +" Queens!");
                	JOptionPane.showMessageDialog(p, "Threre Is No Solutions With "+ num +" Queens!", "BTQueen Error", JOptionPane.ERROR_MESSAGE,qIcon);
                }
            }//if

        }
        
        //if user click cancel
        if (result == JOptionPane.CANCEL_OPTION)
           	JOptionPane.showMessageDialog(p, "Canceled!", "BTQueen Message", JOptionPane.INFORMATION_MESSAGE,qIcon);

	}//main
	
	//construct() create new Vector
	private static Vector<Integer> construct() {
		return new Vector<Integer>();
	}

	//isUserInputValid() check input user data
	private static boolean isUserInputValid(String text) {
		if(text==null || text=="")
			return false;
		for(int i=0; i<text.length(); i++)
			if(text.charAt(i)<'0' || text.charAt(i)>'9')
				return false;
		return true;
	}//isUserInputValid
	
	//BACK TRACKING QUEEN 
	public static boolean backQ(Vector<Integer> board, int n, int nextLine){
		
		//IF ALL QUEENS ALRAIDY ON BOARD
		if(goal(nextLine, n)){			
			//COPY VALID SOLUTION TO VECTOR 'b' 
			Vector<Integer> b = new Vector<Integer>();
			for(int i=0; i<n; i++){
				b.add((Integer) board.get(i));
			}
			// add 1 to valid solutions
			++solution;
			//PRINT ON CONSOLE
			printConsol(b, n);
			//PRINT gui when in "all Solutions" n between 1 and 8 or in "first solution" when n less than 30
			if(n>8 && option==0 || n>30 && option==1){
			}
			else
				printGui(b);
			return true;
		}//if

		//takes nextLine and check if is valid column for queen
		int next = getNext(board, n, nextLine);

		while(next!=-1){
			push(board, nextLine, next);
			next = getNext(board, n, nextLine+1);
		}//while
		if(solution>0)
			return true;
		else
			return false;
	}//backQ
	
	//getNext() takes nextLine and check if is valid column for queen
	private static int getNext(Vector<Integer> board, int n, int nextLine) {
		int col=0;
		while(col<n){
			//if the queen place is valid 
			if(isValid(board, n, nextLine, col)) {
				//put queen on nextLine to next column  
				push(board, nextLine, col);
				//find queen place on nextLine
				if(backQ(board, n, nextLine + 1)==true){
					if(option==1)
						return -1;
				}
				else
					//delete queen from this place
					pop(board, nextLine);
			}
			col++;
		}//while
		return -1;
	}

	//goal() checking if board is complete
	private static boolean goal(int nextLine, int n) {
		if(nextLine== n)
			return true;
		else
			return false;
	}

	//pop() removes queen from line
	private static void pop(Vector<Integer> board, int line) 			{	
		board.remove(line);		
		}

	//push() put queen in specific place
	private static void push(Vector<Integer> board, int line, int col) 	{	
		
		board.add(line, col);	
		
	}

	//isValid() checks if new queen is valid with any other queen on board 
	public static boolean isValid(Vector<Integer> board, int n, int line , int col)	{
		for(int l = 0; l < line; l++) {
			int c = (int)board.get(l);
			if( (c == col) || ( Math.abs(line - l) == Math.abs(col - c) ) )
				return false;
		}
		return true;
	}
	
	//paint() Paints board with queens 
	public void paint(Graphics g){
		//paint board
		for(int i = 0; i <= ((n-1)*50); i+=100){
			for(int j = 0; j <= ((n-1)*50); j+=100){ 
				g.clearRect(i, j, 50, 50);
			}
		}
		//paint board
		for(int i = 50; i <= ((n-1)*50); i+=100){ 
			for(int j = 50; j <= ((n-1)*50); j+=100){ 
				g.clearRect(i, j, 50, 50);
			}
		}
		//paint all queens on specific places
		for(int x=0; x<n; x++)
				g.drawImage( qImage, (int)board.get(x)*50, x*50, null);	
	}
	
	//printConsol()  prints board with queens on console   
	public static void printConsol(Vector<Integer> board, int n){
		System.out.printf("Solution number %d:\n", solution);
		for (int i = 0; i < n; i++){
			System.out.printf("%d\t ", ((int)board.get(i)+1));
			for(int j=0; j<n; j++)
				if(j==(int)board.get(i))
					System.out.print("[*]");
				else
					System.out.print("[ ]");
			System.out.println();
		}
		System.out.printf("\n");
	}
		
	//printGui() prints board with queens by GUI swing 
	private static void printGui(Vector<Integer> v) {
		JFrame b = new JFrame("Solution "+solution);
		b.setSize(50+(50*v.size()),(50+(50*v.size()))); 
		BTQueen board = new BTQueen(v);
		b.add(board);
		b.setLocation(200, 200);
		b.setLocationRelativeTo(null); 
		b.setBackground(Color.LIGHT_GRAY);
		b.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
		b.setVisible(true);
	}
	
}
