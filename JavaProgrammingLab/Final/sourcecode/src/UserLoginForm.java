/**
 *	Subject		: Java Programming
 *	Task		: Final
 * 	Class		: UserLoginForm
 *	Objective	: Login View
 *	SID			: 2020315798
 *	Name		: Choi Jin Woo
 */

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.JRadioButton;
import javax.swing.JPasswordField;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import service.impl.*;

public class UserLoginForm extends JFrame {

	private JPanel contentPane;
	private JPanel panel_logo;
	private JLabel label_logo;
	private JPanel panel_body;
	private JLabel label_ID;
	private JLabel label_PW;
	private JTextField textField_ID;
	private JRadioButton radio_user;
	private JRadioButton radio_admin;
	private JButton button_login;
	private JButton button_signin;
	private JPasswordField textField_PW;

	public UserLoginForm() {
		setTitle("SKKUBOX - Login");
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
		label_ID.setBounds(80, 80, 50, 50);
		panel_body.add(label_ID);
		
		label_PW = new JLabel("PW");
		label_PW.setFont(new Font("Courier", Font.BOLD, 40));
		label_PW.setBackground(Color.decode("#342568"));
		label_PW.setForeground(new Color(255, 255, 255));
		label_PW.setBounds(80, 160, 50, 50);
		panel_body.add(label_PW);
		
		textField_ID = new JTextField();
		textField_ID.setFont(new Font("Arial", Font.PLAIN, 25));
		textField_ID.setBounds(165, 85, 250, 40);
		panel_body.add(textField_ID);
		textField_ID.setColumns(10);
		
		radio_user = new JRadioButton(" User");
		radio_user.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				radio_user.setSelected(true);												// toggle radio button
				radio_admin.setSelected(false);												// toggle radio button
			}
		});
		
		textField_PW = new JPasswordField();
		textField_PW.setFont(new Font("Arial", Font.PLAIN, 25));
		textField_PW.setBounds(165, 165, 250, 40);
		panel_body.add(textField_PW);
		radio_user.setSelected(true);
		radio_user.setFont(new Font("Arial", Font.PLAIN, 25));
		radio_user.setBackground(Color.decode("#342568"));
		radio_user.setForeground(new Color(255, 255, 255));
		radio_user.setBounds(160, 250, 103, 31);
		panel_body.add(radio_user);
		
		radio_admin = new JRadioButton(" Admin");
		radio_admin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				radio_admin.setSelected(true);												// toggle radio button
				radio_user.setSelected(false);												// toggle radio button
			}
		});
		radio_admin.setForeground(Color.WHITE);
		radio_admin.setFont(new Font("Arial", Font.PLAIN, 25));
		radio_admin.setBackground(new Color(52, 37, 104));
		radio_admin.setBounds(330, 250, 109, 31);
		panel_body.add(radio_admin);
		
		button_login = new JButton("LOGIN");
		button_login.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String inputID = textField_ID.getText();
				String inputPW = textField_PW.getText();
				String inputType = radio_user.isSelected() ? "user" : "admin";
				
				UserImpl userImpl = new UserImpl();
				String result = userImpl.executeLogin(inputID, inputPW, inputType);			// use UserImpl class to control DB
				
				if(result.equals("Login Success")){
					if(radio_user.isSelected()){
						UserManager userManager = new UserManager();
						userManager.setVisible(true);						
					}
					else if(radio_admin.isSelected()){
						AdminManager adminManager = new AdminManager();
						adminManager.setVisible(true);	
					}
					dispose();					
				}
				else if(result.equals("Login Fail")){
					JOptionPane.showMessageDialog(UserLoginForm.this, "Incorrect user name or password");
				}
			}
		});
		button_login.setFont(new Font("Courier", Font.BOLD, 30));
		button_login.setBounds(450, 85, 126, 120);
		panel_body.add(button_login);
		
		button_signin = new JButton("SIGN IN");
		button_signin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				UserRegisterForm registerForm = new UserRegisterForm();						// move to register page when clicking sign in
				registerForm.setVisible(true);				
				dispose();					
			}
		});
		button_signin.setFont(new Font("Courier", Font.BOLD, 30));
		button_signin.setBounds(175, 335, 250, 55);
		panel_body.add(button_signin);
	}
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					UserLoginForm frame = new UserLoginForm();
					frame.setVisible(true);
				} 
				catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}