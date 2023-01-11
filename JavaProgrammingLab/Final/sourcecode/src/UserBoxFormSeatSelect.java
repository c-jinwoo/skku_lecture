/**
 *	Subject		: Java Programming
 *	Task		: Final
 * 	Class		: UserBoxFormSeatSelect
 *	Objective	: Select seats
 *	SID			: 2020315798
 *	Name		: Choi Jin Woo
 */

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.SwingConstants;

import java.awt.Font;
import java.awt.EventQueue;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import service.Item.MovieItem;
import service.impl.MovieImpl;

public class UserBoxFormSeatSelect extends JFrame {

	private JPanel contentPane;
	private JPanel panel_body;
	private JButton button_RESERVE;
	private JLabel label_title;
	private JLabel label_screen;
	private JLabel lable_rowA;
	private JLabel lable_rowB;
	private JLabel lable_rowC;
	private JLabel lable_rowD;
	private JLabel lable_rowE;
	private JLabel label_col1;
	private JLabel label_col2;
	private JLabel label_col3;
	private JLabel label_col4;
	private JLabel label_col5;
	private JLabel label_col6;
	private JButton[] btnArr = new JButton[30];
	private JButton button_Previous;
	
	public UserBoxFormSeatSelect(MovieItem movieItem) {
		setTitle("SKKUBOX - User Box Form Seat Select");
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
		
		label_title = new JLabel("Step2. Select Seat");
		label_title.setForeground(Color.WHITE);
		label_title.setFont(new Font("Courier", Font.BOLD, 40));
		label_title.setBackground(new Color(52, 37, 104));
		label_title.setBounds(24, 12, 440, 50);
		panel_body.add(label_title);
		
		label_screen = new JLabel("Screen");
		label_screen.setForeground(Color.WHITE);
		label_screen.setBackground(Color.WHITE);
		label_screen.setFont(new Font("Arial", Font.PLAIN, 25));
		label_screen.setHorizontalAlignment(SwingConstants.CENTER);
		label_screen.setBounds(190, 100, 500, 50);		
		panel_body.add(label_screen);

		lable_rowA = new JLabel("A");
		lable_rowA.setForeground(Color.WHITE);
		lable_rowA.setFont(new Font("Arial", Font.BOLD, 40));
		lable_rowA.setBackground(new Color(52, 37, 104));
		lable_rowA.setBounds(50, 250, 40, 50);
		panel_body.add(lable_rowA);
		
		lable_rowB = new JLabel("B");
		lable_rowB.setForeground(Color.WHITE);
		lable_rowB.setFont(new Font("Arial", Font.BOLD, 40));
		lable_rowB.setBackground(new Color(52, 37, 104));
		lable_rowB.setBounds(50, 350, 40, 50);
		panel_body.add(lable_rowB);
		
		lable_rowC = new JLabel("C");
		lable_rowC.setForeground(Color.WHITE);
		lable_rowC.setFont(new Font("Arial", Font.BOLD, 40));
		lable_rowC.setBackground(new Color(52, 37, 104));
		lable_rowC.setBounds(50, 450, 40, 50);
		panel_body.add(lable_rowC);
		
		lable_rowD = new JLabel("D");
		lable_rowD.setForeground(Color.WHITE);
		lable_rowD.setFont(new Font("Arial", Font.BOLD, 40));
		lable_rowD.setBackground(new Color(52, 37, 104));
		lable_rowD.setBounds(50, 550, 40, 50);
		panel_body.add(lable_rowD);
		
		lable_rowE = new JLabel("E");
		lable_rowE.setForeground(Color.WHITE);
		lable_rowE.setFont(new Font("Arial", Font.BOLD, 40));
		lable_rowE.setBackground(new Color(52, 37, 104));
		lable_rowE.setBounds(50, 650, 40, 50);
		panel_body.add(lable_rowE);
		
		label_col1 = new JLabel("1");
		label_col1.setForeground(Color.WHITE);
		label_col1.setFont(new Font("Arial", Font.BOLD, 40));
		label_col1.setBackground(new Color(52, 37, 104));
		label_col1.setBounds(150, 175, 40, 50);
		panel_body.add(label_col1);
		
		label_col2 = new JLabel("2");
		label_col2.setForeground(Color.WHITE);
		label_col2.setFont(new Font("Arial", Font.BOLD, 40));
		label_col2.setBackground(new Color(52, 37, 104));
		label_col2.setBounds(250, 175, 40, 50);
		panel_body.add(label_col2);
		
		label_col3 = new JLabel("3");
		label_col3.setForeground(Color.WHITE);
		label_col3.setFont(new Font("Arial", Font.BOLD, 40));
		label_col3.setBackground(new Color(52, 37, 104));
		label_col3.setBounds(400, 175, 40, 50);
		panel_body.add(label_col3);
		
		label_col4 = new JLabel("4");
		label_col4.setForeground(Color.WHITE);
		label_col4.setFont(new Font("Arial", Font.BOLD, 40));
		label_col4.setBackground(new Color(52, 37, 104));
		label_col4.setBounds(500, 175, 40, 50);
		panel_body.add(label_col4);
		
		label_col5 = new JLabel("5");
		label_col5.setForeground(Color.WHITE);
		label_col5.setFont(new Font("Arial", Font.BOLD, 40));
		label_col5.setBackground(new Color(52, 37, 104));
		label_col5.setBounds(650, 175, 40, 50);
		panel_body.add(label_col5);
		
		label_col6 = new JLabel("6");
		label_col6.setForeground(Color.WHITE);
		label_col6.setFont(new Font("Arial", Font.BOLD, 40));
		label_col6.setBackground(new Color(52, 37, 104));
		label_col6.setBounds(750, 175, 40, 50);
		panel_body.add(label_col6);
				
		for(int i=0;i<btnArr.length;i++){
			btnArr[i] = new JButton(i+"");
			btnArr[i].setForeground(Color.WHITE);
			btnArr[i].setBackground(Color.WHITE);
			btnArr[i].setBounds(140 + 100 * (i % 6) + 50 * ((i % 6) / 2), 250 + 100 * (i / 6), 50, 50);
			btnArr[i].addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {											// for each seat button, toggle color and selected attribute for state change
					int index = Integer.parseInt(e.getActionCommand());
					if(btnArr[index].isSelected()){
						btnArr[index].setForeground(Color.WHITE);
						btnArr[index].setBackground(Color.WHITE);
						btnArr[index].setSelected(false);					
					}
					else{
						btnArr[index].setForeground(Color.MAGENTA);
						btnArr[index].setBackground(Color.MAGENTA);
						btnArr[index].setSelected(true);
					}
				}
			});
			panel_body.add(btnArr[i]);
		}
		
		button_RESERVE = new JButton("RESERVE");
		button_RESERVE.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				executeReserve(movieItem);
			}
		});
		button_RESERVE.setFont(new Font("Courier", Font.BOLD, 30));
		button_RESERVE.setBounds(126, 730, 250, 55);
		panel_body.add(button_RESERVE);
		
		button_Previous = new JButton("Previous");
		button_Previous.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				UserBoxFormMovieSelect userBoxFormMovieSelect = new UserBoxFormMovieSelect();
				userBoxFormMovieSelect.setVisible(true);		
				dispose();	
			}
		});
		button_Previous.setFont(new Font("Courier", Font.BOLD, 30));
		button_Previous.setBounds(486, 730, 250, 55);
		panel_body.add(button_Previous);
		
		setMovieSeat(movieItem.getIndex());
	}
		
	public void setMovieSeat(int movieIdx){
		MovieImpl movieImpl = new MovieImpl();
		List<Integer> seatList = movieImpl.setMovieSeat(movieIdx);										// get reserved seats
		
		for(int i=0;i<seatList.size();i++){
			btnArr[seatList.get(i)].setForeground(Color.DARK_GRAY);
			btnArr[seatList.get(i)].setBackground(Color.DARK_GRAY);
			btnArr[seatList.get(i)].setEnabled(false);													// if seats were already booked, make them disabled
		}
	}
	
	public void executeReserve(MovieItem movieItem){
		List<Integer> seatList = new ArrayList<Integer>();
		int movieIdx = movieItem.getIndex();
		int count = 0;
		
		for(int i=0;i<btnArr.length;i++){
			if(btnArr[i].isSelected()){
				seatList.add(i);																		// add seats to the list
				count++;
			}
		}
		
		if(count == 0){
			JOptionPane.showMessageDialog(UserBoxFormSeatSelect.this, "No seats are selected.");		// if no seats are selected, return
			return;
		}

		MovieImpl movieImpl = new MovieImpl();	
		movieImpl.executeReserve(movieIdx, seatList);													// add seats and user to DB
		
		UserBoxReservation userBoxReservation = new UserBoxReservation(movieItem.getName(), seatList);	// go to result page
		userBoxReservation.setVisible(true);		
		dispose();
	}

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MovieItem movieItem = new MovieItem(0,"",0,0);
					UserBoxFormSeatSelect frame = new UserBoxFormSeatSelect(movieItem);
					frame.setVisible(true);
				} 
				catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}