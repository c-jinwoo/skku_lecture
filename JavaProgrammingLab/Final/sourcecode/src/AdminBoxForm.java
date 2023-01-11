/**
 *	Subject		: Java Programming
 *	Task		: Final
 * 	Class		: UserLoginForm
 *	Objective	: Login GUI(View)
 *	SID			: 2020315798
 *	Name		: Choi Jin Woo
 */


import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JButton;

import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.GridLayout;
import java.awt.Color;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import java.awt.Font;

import javax.swing.JTextField;
import javax.swing.JRadioButton;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.JPasswordField;

import service.impl.MovieImpl;

public class AdminBoxForm extends JFrame {

	private JPanel contentPane;
	private JPanel panel_logo;
	private JLabel label_logo;
	private JPanel panel_body;
	private JPanel panle_body_inner;
	private JLabel lable_Name;
	private JLabel label_Duration;
	private JTextField textField_Name;
	private JButton button_submit;
	private JLabel label_title;
	private JTextField textField_Duration;
	private JLabel label_Age;
	private JTextField textField_Age;
	private JButton button_Previous;

	public AdminBoxForm() {
		setTitle("SKKUBOX - Admin Box Select");
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
		
		panle_body_inner = new JPanel();
		panle_body_inner.setBackground(Color.WHITE);
		panle_body_inner.setBounds(650, 40, 500, 400);
		panel_body.add(panle_body_inner);
		panle_body_inner.setLayout(null);
		
		label_title = new JLabel("Movie Register");
		label_title.setForeground(Color.WHITE);
		label_title.setFont(new Font("Courier", Font.BOLD, 40));
		label_title.setBackground(new Color(52, 37, 104));
		label_title.setBounds(130, 40, 346, 50);
		panel_body.add(label_title);
		
		lable_Name = new JLabel("Name");
		lable_Name.setFont(new Font("Arial", Font.BOLD, 35));
		lable_Name.setBackground(Color.decode("#342568"));
		lable_Name.setForeground(new Color(255, 255, 255));
		lable_Name.setBounds(69, 124, 150, 50);
		panel_body.add(lable_Name);
		
		label_Duration = new JLabel("Duration");
		label_Duration.setFont(new Font("Arial", Font.BOLD, 35));
		label_Duration.setBackground(Color.decode("#342568"));
		label_Duration.setForeground(new Color(255, 255, 255));
		label_Duration.setBounds(69, 199, 150, 50);
		panel_body.add(label_Duration);
		
		label_Age = new JLabel("Age");
		label_Age.setForeground(Color.WHITE);
		label_Age.setFont(new Font("Arial", Font.BOLD, 35));
		label_Age.setBackground(new Color(52, 37, 104));
		label_Age.setBounds(69, 275, 150, 50);
		panel_body.add(label_Age);
		
		textField_Name = new JTextField();
		textField_Name.setFont(new Font("Arial", Font.PLAIN, 25));
		textField_Name.setBounds(252, 129, 285, 40);
		panel_body.add(textField_Name);
		textField_Name.setColumns(10);
		
		textField_Duration = new JTextField();
		textField_Duration.setFont(new Font("Arial", Font.PLAIN, 25));
		textField_Duration.setColumns(10);
		textField_Duration.setBounds(252, 204, 283, 40);
		panel_body.add(textField_Duration);
		
		textField_Age = new JTextField();
		textField_Age.setFont(new Font("Arial", Font.PLAIN, 25));
		textField_Age.setColumns(10);
		textField_Age.setBounds(252, 280, 283, 40);
		panel_body.add(textField_Age);
		
		button_submit = new JButton("Submit");
		button_submit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String name = textField_Name.getText();
				String duration = textField_Duration.getText();
				String age = textField_Age.getText();
				
				MovieImpl movieImpl = new MovieImpl();
				String result = movieImpl.executeEnroll(name, duration, age);

				if(result.equals("Success")){
					AdminManager adminManager = new AdminManager();
					adminManager.setVisible(true);
					dispose();				
				}
				else{
					JOptionPane.showMessageDialog(AdminBoxForm.this, result);							// show dialog with result string of failure
				}
			}
		});
		button_submit.setFont(new Font("Courier", Font.BOLD, 30));
		button_submit.setBounds(35, 385, 250, 55);
		panel_body.add(button_submit);
		
		button_Previous = new JButton("Previous");
		button_Previous.setFont(new Font("Courier", Font.BOLD, 30));
		button_Previous.setBounds(321, 385, 250, 55);
		button_Previous.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				AdminManager adminManager = new AdminManager();
				adminManager.setVisible(true);		
				dispose();				
			}
		});
		panel_body.add(button_Previous);
	}
	

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					AdminBoxForm frame = new AdminBoxForm();
					frame.setVisible(true);
				} 
				catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}