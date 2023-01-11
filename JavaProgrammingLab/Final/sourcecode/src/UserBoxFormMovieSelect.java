/**
 *	Subject		: Java Programming
 *	Task		: Final
 * 	Class		: UserBoxFormMovieSelect
 *	Objective	: UserBoxFormMovieSelect View
 *	SID			: 2020315798
 *	Name		: Choi Jin Woo
 */

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JComboBox;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

import service.Item.MovieItem;
import service.impl.*;
import util.SessionManager;

public class UserBoxFormMovieSelect extends JFrame {

	private JPanel contentPane;
	private JPanel panel_body;
	private JLabel label_MovieList;
	private JLabel label_Duration;
	private JButton button_nextstep;
	private JLabel label_title;
	private JLabel label_ageLimit;
	private JComboBox comboBox;
	private JTextField textField_duration;
	private JTextField textField_ageLimit;
	int selectedMovie = 0;
	List<MovieItem> movieList = new ArrayList<MovieItem>();
	private JButton button_Previous;
	
	public UserBoxFormMovieSelect() {
		setTitle("SKKUBOX - User Box Form Movie Select");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 897, 867);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		panel_body = new JPanel();
		panel_body.setBackground(Color.decode("#342568"));
		panel_body.setBounds(0, 0, 879, 820);
		contentPane.add(panel_body);
		panel_body.setLayout(null);
		
		label_title = new JLabel("Step1. Select Movie");
		label_title.setForeground(Color.WHITE);
		label_title.setFont(new Font("Courier", Font.BOLD, 40));
		label_title.setBackground(new Color(52, 37, 104));
		label_title.setBounds(24, 12, 509, 50);
		panel_body.add(label_title);
		
		label_MovieList = new JLabel("Movie list");
		label_MovieList.setFont(new Font("Arial", Font.PLAIN, 35));
		label_MovieList.setBackground(Color.decode("#342568"));
		label_MovieList.setForeground(new Color(255, 255, 255));
		label_MovieList.setBounds(80, 100, 234, 50);
		panel_body.add(label_MovieList);
		
		label_Duration = new JLabel("Duration");
		label_Duration.setFont(new Font("Arial", Font.PLAIN, 35));
		label_Duration.setBackground(Color.decode("#342568"));
		label_Duration.setForeground(new Color(255, 255, 255));
		label_Duration.setBounds(80, 415, 165, 50);
		panel_body.add(label_Duration);
		
		label_ageLimit = new JLabel("Age Limit");
		label_ageLimit.setForeground(Color.WHITE);
		label_ageLimit.setFont(new Font("Arial", Font.PLAIN, 35));
		label_ageLimit.setBackground(new Color(52, 37, 104));
		label_ageLimit.setBounds(80, 480, 165, 50);
		panel_body.add(label_ageLimit);
		
		comboBox = new JComboBox();
		comboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setMovieInfo();														// event handle when the value of combo box changes
			}
		});
		comboBox.setFont(new Font("Arial", Font.PLAIN, 25));
		comboBox.setBounds(300, 105, 400, 40);
		panel_body.add(comboBox);
		
		textField_duration = new JTextField();
		textField_duration.setFont(new Font("Arial", Font.PLAIN, 25));
		textField_duration.setBounds(300, 420, 400, 40);
		panel_body.add(textField_duration);
		textField_duration.setColumns(10);
		textField_duration.setEditable(false);
		
		textField_ageLimit = new JTextField();
		textField_ageLimit.setFont(new Font("Arial", Font.PLAIN, 25));
		textField_ageLimit.setEditable(false);
		textField_ageLimit.setColumns(10);
		textField_ageLimit.setBounds(300, 485, 400, 40);
		panel_body.add(textField_ageLimit);
		
		button_nextstep = new JButton("Next Step");
		button_nextstep.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				// check if there is no movie at all
				if(movieList.size() == 0){
					JOptionPane.showMessageDialog(UserBoxFormMovieSelect.this, "There is no movie.");
					return;
				}
				// check if user's age is lower than allowed movie age
				if(SessionManager.getSessionUserAge() < movieList.get(selectedMovie).getAge()){
					JOptionPane.showMessageDialog(UserBoxFormMovieSelect.this, "You should be age of " + movieList.get(selectedMovie).getAge() + " to watch this movie.");
					return;
				}
				
				UserBoxFormSeatSelect userBoxFormSeatSelect = new UserBoxFormSeatSelect(movieList.get(selectedMovie));
				userBoxFormSeatSelect.setVisible(true);		
				dispose();	
			}
		});
		button_nextstep.setFont(new Font("Courier", Font.BOLD, 30));
		button_nextstep.setBounds(125, 684, 250, 55);
		panel_body.add(button_nextstep);
		
		button_Previous = new JButton("Previous");
		button_Previous.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				UserManager userManager = new UserManager();
				userManager.setVisible(true);		
				dispose();	
			}
		});
		button_Previous.setFont(new Font("Courier", Font.BOLD, 30));
		button_Previous.setBounds(480, 684, 250, 55);
		panel_body.add(button_Previous);
		
		setMovieList();																// set initial movie list and information
	}	
	
	public void setMovieList(){
		MovieImpl movieImpl = new MovieImpl();
		movieList = movieImpl.getMovieList();
		
		for(int i=0;i<movieList.size();i++){
			comboBox.addItem(movieList.get(i).getName());
		}
	}	
	
	public void setMovieInfo(){
		selectedMovie = comboBox.getSelectedIndex();
		textField_duration.setText(movieList.get(selectedMovie).getDuration()+"min");
		textField_ageLimit.setText(movieList.get(selectedMovie).getAge()+"");
	}

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					UserBoxFormMovieSelect frame = new UserBoxFormMovieSelect();
					frame.setVisible(true);
				} 
				catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}