/*

*/
import java.util.Arrays;
import java.util.LinkedList; 
import java.util.Queue;
import java.util.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.Comparator;

public class H2Comparator implements Comparator<Board>
{
	//Used in A* H2 for implementing priority queue, compares based on lower manhattan distance method in Board class
	public int compare(Board a, Board b) {
		return a.manhattanDistanceBoard()-b.manhattanDistanceBoard();
	}
}