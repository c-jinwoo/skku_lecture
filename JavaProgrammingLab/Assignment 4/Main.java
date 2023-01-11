import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.GridLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JProgressBar;
import javax.swing.SwingConstants;
import javax.swing.SwingWorker;

import java.io.PrintWriter;
import java.io.FileOutputStream;
import java.util.List;

public class Main extends JFrame {

	private JPanel contentPane;
	private JPanel control_pane;
	private JScrollPane scrollPane;
	private JPanel body_pane;
	private JPanel result_pane;
	private JTextArea textArea_output;
	private JLabel label_number;
	private JTextField txtField_input;
	private JButton btn_execute;
	private JButton btn_cancel;
	private JProgressBar progressBar;
	private JLabel label_sum;
	
	private Integer[] lucasArray;													// for storing lucas series values
	private Integer iSum = 0;														// for storing sum of the array
	private Integer iInput = 0;														// for storing user input
	private SwingWorker worker;														// for storing worker template
	
	/**
	 * Create the frame.
	 */
	public Main() {
		setTitle("Finding Lucas series sum");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 824, 617);
		
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		control_pane = new JPanel();
		contentPane.add(control_pane, BorderLayout.NORTH);
		control_pane.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		label_number = new JLabel("Number of row in Lucas series:");
		label_number.setFont(new Font("굴림", Font.BOLD, 15));
		label_number.setHorizontalAlignment(SwingConstants.RIGHT);
		control_pane.add(label_number);
		
		txtField_input = new JTextField();
		control_pane.add(txtField_input);
		txtField_input.setColumns(10);
		
		btn_execute = new JButton("Get Sum of Lucas series");
		btn_execute.setFont(new Font("굴림", Font.BOLD, 15));
		btn_execute.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {							// when started by execute button
				setThread();														// set thread				
				worker.execute();													// start thread
			}
		});
		control_pane.add(btn_execute);
		
		btn_cancel = new JButton("Cancel");
		btn_cancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {							// when interrupted by cancel button	
				worker.cancel(true);												// stop thread
				toggleButtons(true);												// switch buttons enable
			}			
		});
		btn_cancel.setFont(new Font("굴림", Font.BOLD, 15));
		btn_cancel.setEnabled(false);
		control_pane.add(btn_cancel);
		
		body_pane = new JPanel();
		contentPane.add(body_pane, BorderLayout.CENTER);
		body_pane.setLayout(new BorderLayout(0, 0));

		textArea_output = new JTextArea();						
		scrollPane = new JScrollPane(textArea_output);								// add scroll to the output textArea
		body_pane.add(scrollPane);
				
		result_pane = new JPanel();
		contentPane.add(result_pane, BorderLayout.SOUTH);
		result_pane.setLayout(new GridLayout(1, 2, 2, 0));

		progressBar = new JProgressBar();
		progressBar.setStringPainted(true);											// represent 100% value inside the progress bar
		result_pane.add(progressBar);
		
		label_sum = new JLabel("Sum = ");
		label_sum.setFont(new Font("굴림", Font.BOLD, 15));
		result_pane.add(label_sum);
	}
	
	private void setThread(){		
		/* Using SwingWorker Template */
		worker = new SwingWorker<Boolean, Integer>() {
			@Override
			protected Boolean doInBackground() throws Exception {				
				try{	
					initialize();													// initialize all objects
					toggleButtons(false);														// switch buttons enable
					
					/* print out result */
					for(int i=0;i<lucasArray.length;i++){	
						Thread.sleep(100);											// give 100ms delay with for loop
						
						publish(i);													// publish index number
					}
				}
				catch(IllegalArgumentException e){
					JOptionPane.showMessageDialog(null, "Enter valid input!", "Warning", JOptionPane.WARNING_MESSAGE);		// warining popup for invalid inputs(out of range, String, Float, etc)	
				}
				
				return true;
			}

			/* process() : update GUI in the middle */
			@Override
			protected void process(List<Integer> chunks) {				
				Integer index = chunks.get(chunks.size() - 1);						// current index
				
				textArea_output.append(lucasArray[index]+"\n");
				iSum += lucasArray[index];
				
				progressBar.setValue((index+1) * 100 / lucasArray.length);			// print out progress bar
				label_sum.setText("Sum = " + iSum);									// print out sum				
			}
			
			/* done() */
			@Override
			protected void done() {						
				try {
					/* File I/O - Print out result with 'data.txt' */
					FileOutputStream fileObj = new FileOutputStream("data.txt", false);
					PrintWriter file = new PrintWriter(fileObj);
					file.println(textArea_output.getText());
					file.close();
					
					toggleButtons(true);											// switch buttons enable when loop finishes
				}
				catch (Exception e) {
					JOptionPane.showMessageDialog(null, "File I/O Exception", "Warning", JOptionPane.WARNING_MESSAGE);		// warining popup for io exception	
				}
			}
		};
	}

	public void initialize(){
		iInput = Integer.parseInt(txtField_input.getText());						// get user input number
		if(iInput < 3 || iInput > 20){
			throw new IllegalArgumentException();									// throw illegal exception for out of range + any other String inputs
		}	
		iSum = 0;																	// initialize sum to 0
		textArea_output.setText("");												// reset text area	
		lucasArray = new Integer[iInput];											// assign the array size	
		
		/* set lucas array */
		lucasArray[0] = 2;															// set designated index [0]
		lucasArray[1] = 1;															// set designated index [1]			
		
		for(int i=2;i<iInput;i++){
			lucasArray[i] = lucasArray[i-1] + lucasArray[i-2];						// value of array at index = (index-1)+(index-2)
		}				
	}	
	
	public void toggleButtons(boolean onOff){
		btn_execute.setEnabled(onOff);												// enable btn_execute with parameter onOff
		btn_cancel.setEnabled(!onOff);												// enable btn_cancel with parameter !onOff
	}
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Main frame = new Main();
					frame.setVisible(true);
				} 
				catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}
