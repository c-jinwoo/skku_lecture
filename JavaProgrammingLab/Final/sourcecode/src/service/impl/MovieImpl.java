/**
 *	Subject		: Java Programming
 *	Task		: Final
 * 	Class		: MovieImpl
 *	Objective	: Execute Movie(Service Implement)
 *	SID			: 2020315798
 *	Name		: Choi Jin Woo
 */

package service.impl;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import service.Movie;
import service.Item.MovieItem;
import util.SessionManager;

public class MovieImpl implements Movie {
	
	@Override
	public List<MovieItem> getMovieList(){
		List<MovieItem> list = new ArrayList<MovieItem>();
		
		try {
			FileInputStream fileObject = new FileInputStream("./db/MOVIE.txt");
			Scanner fileScanner = new Scanner(fileObject);
			
			while(fileScanner.hasNext()){
				String line = fileScanner.next();
				int index = Integer.parseInt(line.split(",")[0].split(":")[1]);
				String name = line.split(",")[1].split(":")[1];
				int age = Integer.parseInt(line.split(",")[2].split(":")[1]);
				int duration = Integer.parseInt(line.split(",")[3].split(":")[1]);
				
				MovieItem movieItem = new MovieItem(index, name, age, duration);					// get movie info from DB and add to MovieItem object
				list.add(movieItem);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		return list;		
	}	

	@Override
	public List<Integer> setMovieSeat(int movieIdx){
		List<Integer> seatList = new ArrayList<Integer>();
		
		try {
			FileInputStream fileObject = new FileInputStream("./db/RESERVATION.txt");
			Scanner fileScanner = new Scanner(fileObject);
			
			while(fileScanner.hasNext()){
				String line = fileScanner.nextLine();
				int idx = Integer.parseInt(line.split(",")[0].split(":")[1]);						// get movie index from DB
				if(idx == movieIdx){
					String[] seatArr = line.split(",")[2].split(":")[1].split("/");					// if movie index is equal to the selected movie, add seats to the list
					for(int i=0;i<seatArr.length;i++){
						seatList.add(Integer.parseInt(seatArr[i]));
					}
				}
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		return seatList;
	}
	
	@Override
	public void executeReserve(int movieIdx, List list){

		try {
			FileOutputStream fileObject = new FileOutputStream("./db/RESERVATION.txt",true);
			PrintWriter fileWriter = new PrintWriter(fileObject);
			
			String line = "";
			line += "MOVIEIDX:"+movieIdx+",";
			line += "USERIDX:"+SessionManager.getSessionUserIdx()+",";
			line += "SEAT:";
			for(int i=0;i<list.size();i++){
				line += (i < list.size()-1) ? list.get(i)+"/" : list.get(i)+""; 			// insert reservation information to DB
			}
			fileWriter.write(line+"\n");
			
			fileWriter.close();
			fileObject.close();
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public String executeEnroll(String name, String duration, String age){
		String result = "";
		
		try {
			FileOutputStream fileObject = new FileOutputStream("./db/MOVIE.txt",true);
			PrintWriter fileWriter = new PrintWriter(fileObject);

			int movieIdx = getMovieCnt();													// get last movie index
			
			String line = "";
			line += "IDX:"+movieIdx+",";
			line += "NAME:"+name+",";
			line += "AGE:"+age+",";
			line += "DURATION:"+duration+"";
			
			if(name.length() == 0){															// check if Name is empty
				result = "Please enter Name";				
			}
			else if(duration.length() == 0){												// check if Duration is empty
				result = "Please enter duration";					
			}
			else if(age.length() == 0){														// check if Age is empty
				result = "Please enter Age";	
			}
			else{
				result = "Success";
				fileWriter.write(line+"\n");
			}
			
			fileWriter.close();
			fileObject.close();
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
	@Override
	/* SELECT movie count from MOVIE DB */ 
	public Integer getMovieCnt(){
		int count = 0;		
		try {
			FileInputStream fileObject = new FileInputStream("./db/MOVIE.txt");
			Scanner fileScanner = new Scanner(fileObject);
			
			while(fileScanner.hasNext()){
				if(fileScanner.nextLine().length() > 0){
					count++;
				}
			}		
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		return count;
	}
}
