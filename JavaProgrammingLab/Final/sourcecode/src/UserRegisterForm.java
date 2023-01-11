/**
 *	Subject		: Java Programming
 *	Task		: Final
 * 	Class		: UserRegisterForm
 *	Objective	: Register View
 *	SID			: 2020315798
 *	Name		: Choi Jin Woo
 */

import java.awt.EventQueue;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.JPasswordField;

import service.impl.*;

public class UserRegisterForm extends JFrame {

	private JPanel contentPane;
	private JPanel panel_logo;
	private JLabel label_logo;
	private JPanel panel_body;
	private JLabel label_ID;
	private JLabel label_PW;
	private JTextField textField_ID;
	private JButton btnSubmit;
	private JLabel label_PWCK;
	private JPasswordField textField_PW;
	private JPasswordField textField_PWCK;
	private JLabel lable_AGE;
	private JTextField textField_AGE;
	private JLabel label_NAME;
	private JTextField textField_NAME;

	public UserRegisterForm() {
		setTitle("SKKUBOX - Register");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 619, 831);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		panel_logo = new JPanel();
		panel_logo.setBounds(0, 0, 600, 299);
		contentPane.add(panel_logo);
		
		label_logo = new JLabel("");
		panel_logo.add(label_logo);
		label_logo.setIcon(new ImageIcon(getClass().getResource("./img/logo.jpg")));
		
		panel_body = new JPanel();
		panel_body.setBackground(Color.decode("#342568"));
		panel_body.setBounds(0, 299, 600, 485);
		contentPane.add(panel_body);
		panel_body.setLayout(null);
		
		label_ID = new JLabel("ID");
		label_ID.setFont(new Font("Courier", Font.BOLD, 40));
		label_ID.setBackground(Color.decode("#342568"));
		label_ID.setForeground(new Color(255, 255, 255));
		label_ID.setBounds(100, 30, 50, 50);
		panel_body.add(label_ID);
		
		label_PW = new JLabel("PW");
		label_PW.setFont(new Font("Courier", Font.BOLD, 40));
		label_PW.setBackground(Color.decode("#342568"));
		label_PW.setForeground(new Color(255, 255, 255));
		label_PW.setBounds(100, 105, 50, 50);
		panel_body.add(label_PW);
		
		label_PWCK = new JLabel("PW CK");
		label_PWCK.setForeground(Color.WHITE);
		label_PWCK.setFont(new Font("Courier", Font.BOLD, 40));
		label_PWCK.setBackground(new Color(52, 37, 104));
		label_PWCK.setBounds(100, 180, 132, 50);
		panel_body.add(label_PWCK);
		
		label_NAME = new JLabel("NAME");
		label_NAME.setForeground(Color.WHITE);
		label_NAME.setFont(new Font("Courier", Font.BOLD, 40));
		label_NAME.setBackground(new Color(52, 37, 104));
		label_NAME.setBounds(100, 249, 120, 50);
		panel_body.add(label_NAME);
		
		lable_AGE = new JLabel("AGE");
		lable_AGE.setForeground(Color.WHITE);
		lable_AGE.setFont(new Font("Courier", Font.BOLD, 40));
		lable_AGE.setBackground(new Color(52, 37, 104));
		lable_AGE.setBounds(100, 318, 132, 50);
		panel_body.add(lable_AGE);
		
		textField_ID = new JTextField();
		textField_ID.setFont(new Font("Arial", Font.PLAIN, 25));
		textField_ID.setBounds(250, 35, 250, 40);
		panel_body.add(textField_ID);
		textField_ID.setColumns(10);
		
		textField_PW = new JPasswordField();
		textField_PW.setFont(new Font("Arial", Font.PLAIN, 25));
		textField_PW.setBounds(250, 110, 250, 40);
		panel_body.add(textField_PW);
		
		textField_PWCK = new JPasswordField();
		textField_PWCK.setFont(new Font("Arial", Font.PLAIN, 25));
		textField_PWCK.setBounds(250, 185, 250, 40);
		panel_body.add(textField_PWCK);
		
		textField_NAME = new JTextField();
		textField_NAME.setFont(new Font("Arial", Font.PLAIN, 25));
		textField_NAME.setColumns(10);
		textField_NAME.setBounds(250, 254, 250, 40);
		panel_body.add(textField_NAME);
		
		textField_AGE = new JTextField();
		textField_AGE.setFont(new Font("Arial", Font.PLAIN, 25));
		textField_AGE.setColumns(10);
		textField_AGE.setBounds(250, 326, 250, 40);
		panel_body.add(textField_AGE);
		
		btnSubmit = new JButton("SUBMIT");
		btnSubmit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String inputID = textField_ID.getText();
				String inputPW = textField_PW.getText();
				String inputPWCK = textField_PWCK.getText();
				String inputNM = textField_NAME.getText();
				String inputAGE = textField_AGE.getText();
				
				UserImpl userImpl = new UserImpl();
				String result = userImpl.executeSubmit(inputID, inputPW, inputPWCK, inputNM, inputAGE);		// use UserImpl class to control DB
				
				if(result.equals("Success")){
					UserLoginForm loginForm = new UserLoginForm();
					loginForm.setVisible(true);		
					dispose();	
				}
				else{
					JOptionPane.showMessageDialog(UserRegisterForm.this, result);							// show dialog with result string of failure
				}
			}
		});
		btnSubmit.setFont(new Font("Courier", Font.BOLD, 30));
		btnSubmit.setBounds(174, 405, 250, 55);
		panel_body.add(btnSubmit);
	}	

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					UserRegisterForm frame = new UserRegisterForm();
					frame.setVisible(true);
				} 
				catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}