/**
 *	Subject		: Java Programming
 *	Task		: Final
 * 	Interface	: User
 *	Objective	: Execute Login(Service)
 *	SID			: 2020315798
 *	Name		: Choi Jin Woo
 */

package service;

import java.util.List;

import service.Item.MovieItem;

public interface Movie{
	public List<MovieItem> getMovieList();
	
	public List<Integer> setMovieSeat(int movieIdx);
	
	public void executeReserve(int movieIdx, List list);
	
	public String executeEnroll(String name, String duration, String age);
	
	public Integer getMovieCnt();
}
