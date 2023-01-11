/**
 *	Subject		: Java Programming
 *	Task		: Final
 * 	Class		: UserBoxReservation
 *	Objective	: Show the result of the reservation
 *	SID			: 2020315798
 *	Name		: Choi Jin Woo
 */

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JButton;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;
import util.SessionManager;
import javax.swing.SwingConstants;

public class UserBoxReservation extends JFrame {

	private JPanel contentPane;
	private JPanel panel_logo;
	private JLabel label_logo;
	private JPanel panel_body;
	private JLabel label_title;
	private JButton btnHome;
	private JLabel label_movie;
	private JLabel label_seat;
	private JLabel label_farewell;
	private JLabel label_movieName;
	private JLabel label_seatList;

	public UserBoxReservation(String movieName, List<Integer> list) {
		setTitle("SKKUBOX - User Box Reservation");
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
		
		label_title = new JLabel("RESERVATION CHECK");
		label_title.setFont(new Font("Courier", Font.BOLD, 40));
		label_title.setBackground(Color.decode("#342568"));
		label_title.setForeground(new Color(255, 255, 255));
		label_title.setBounds(94, 12, 408, 50);
		panel_body.add(label_title);
		
		label_movie = new JLabel("MOVIE :");
		label_movie.setForeground(Color.WHITE);
		label_movie.setFont(new Font("Arial", Font.BOLD, 25));
		label_movie.setBackground(new Color(52, 37, 104));
		label_movie.setBounds(116, 120, 103, 50);
		panel_body.add(label_movie);
		
		label_seat = new JLabel("SEAT :");
		label_seat.setForeground(Color.WHITE);
		label_seat.setFont(new Font("Arial", Font.BOLD, 25));
		label_seat.setBackground(new Color(52, 37, 104));
		label_seat.setBounds(116, 182, 103, 50);
		panel_body.add(label_seat);
		
		label_farewell = new JLabel(SessionManager.getSessionUserName()+", Enjoy the movie!");
		label_farewell.setHorizontalAlignment(SwingConstants.CENTER);
		label_farewell.setForeground(Color.WHITE);
		label_farewell.setFont(new Font("Arial", Font.BOLD, 20));
		label_farewell.setBackground(new Color(52, 37, 104));
		label_farewell.setBounds(14, 349, 572, 29);
		panel_body.add(label_farewell);
		
		btnHome = new JButton("HOME");
		btnHome.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				UserManager userManager = new UserManager();							// move to userManager page when clicking HOME
				userManager.setVisible(true);				
				dispose();					
			}
		});
		btnHome.setFont(new Font("Courier", Font.BOLD, 30));
		btnHome.setBounds(175, 390, 250, 55);
		panel_body.add(btnHome);
		
		label_movieName = new JLabel("");
		label_movieName.setForeground(Color.WHITE);
		label_movieName.setFont(new Font("Arial", Font.BOLD, 25));
		label_movieName.setBackground(new Color(52, 37, 104));
		label_movieName.setBounds(237, 120, 349, 50);
		panel_body.add(label_movieName);
		
		label_seatList = new JLabel("");
		label_seatList.setForeground(Color.WHITE);
		label_seatList.setFont(new Font("Arial", Font.BOLD, 25));
		label_seatList.setBackground(new Color(52, 37, 104));
		label_seatList.setBounds(237, 182, 349, 50);
		panel_body.add(label_seatList);
		
		setReservationInfo(movieName, list);
	}
	
	public void setReservationInfo(String movieName, List<Integer> list){
		label_movieName.setText(movieName);												// set movie name info to result
		
		String seatList = "";
		for(int i=0;i<list.size();i++){
			String row = "";
			String seat = "";
			int rowIdx = list.get(i) / 6;
			int colIdx = (list.get(i) % 6) + 1;
			
			if(rowIdx == 0){
				row = "A";
			}
			else if(rowIdx == 1){
				row = "B";
			}
			else if(rowIdx == 2){
				row = "C";
			}
			else if(rowIdx == 3){
				row = "D";
			}
			else if(rowIdx == 4){
				row = "E";
			}
			seat = row + colIdx;
			
			seatList += (i < list.size()-1) ? seat+"," : seat;							// calculate index to A1~E6 and display to result
		}
		label_seatList.setText(seatList);
	}
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					String movieName = "";
					List<Integer> list = new ArrayList<Integer>();
					UserBoxReservation frame = new UserBoxReservation(movieName, list);
					frame.setVisible(true);
				} 
				catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}