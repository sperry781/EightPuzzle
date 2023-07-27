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

public class UCSComparator implements Comparator<Board>
{
	//Used in UCS for implementing priority queue, compares based on lower score
	public int compare(Board a, Board b) {
		return a.score-b.score;
	}
}