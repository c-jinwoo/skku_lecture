/**
 *	Subject		: Java Programming
 *	Task		: Final
 * 	Class		: AdminManager
 *	Objective	: Admin Manager view
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
import javax.swing.SwingConstants;

public class AdminManager extends JFrame {

	private JPanel contentPane;
	private JPanel panel_logo;
	private JLabel label_logo;
	private JPanel panel_body;
	private JButton btn_box;
	private JButton btn_logout;

	public AdminManager() {
		setTitle("SKKUBOX - Admin Manager");
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
		
		btn_box = new JButton(" BOX");
		btn_box.setHorizontalAlignment(SwingConstants.LEFT);
		btn_box.setForeground(Color.decode("#342568"));
		btn_box.setFont(new Font("Courier", Font.BOLD, 40));
		btn_box.setBounds(70, 80, 460, 130);
		btn_box.setBackground(Color.WHITE);
		btn_box.setIcon(new ImageIcon(getClass().getResource("./img/ticket.jpg")));
		btn_box.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				AdminBoxForm adminBoxForm = new AdminBoxForm();
				adminBoxForm.setVisible(true);		
				dispose();							
			}
		});
		
		panel_body.add(btn_box);
		
		btn_logout = new JButton(" Logout");
		btn_logout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				UserLoginForm loginForm = new UserLoginForm();
				loginForm.setVisible(true);		
				dispose();							
			}
		});
		btn_logout.setHorizontalAlignment(SwingConstants.LEFT);
		btn_logout.setForeground(Color.decode("#342568"));
		btn_logout.setFont(new Font("Courier", Font.BOLD, 40));
		btn_logout.setBackground(Color.WHITE);
		btn_logout.setBounds(70, 260, 460, 130);
		btn_logout.setIcon(new ImageIcon(getClass().getResource("./img/setting.jpg")));
		
		panel_body.add(btn_logout);
	}
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					AdminManager frame = new AdminManager();
					frame.setVisible(true);
				} 
				catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}