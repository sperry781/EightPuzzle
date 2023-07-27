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


public class Board{
	//array representation of the board
	public int[] board;
	//list of previous board states, initially empty unless included in creation
	List<Board> previousBoardStates = new ArrayList<Board>();
	//score, initially 0 unless included in creation
	public int score;
	//index showing where the 0 is on the board, used for listing swaps in final sequence of boards
	public int emptyIndex;
	//index showing where previous 0 was, used for listing swaps in final sequence of boards
	public int prevEmptyIndex;
	//used for checking win conditions in check method and manhattan distance
	public int[] winBoard = new int[]{1,2,3,8,0,4,7,6,5};
	
	//Create a board using just a list of numbers
	public Board(int[] boardArray) {
		board = new int[9];
		for(int i=0; i<9; i++)
		{
			board[i] = boardArray[i];
			if(boardArray[i]==0)
			{
				emptyIndex=i;
			}
		}
		score = 0;
		prevEmptyIndex = 30;
	}
	
	//Create a board using list of numbers and remember previous board states as well as other information
	public Board(int[] boardArray, List<Board> inputBoardStates, int inputScore, int inputEmpty, int inputPrevEmpty) {
		board = new int[9];
		for(int i=0; i<9; i++)
		{
			board[i] = boardArray[i];

		}
		score = inputScore;
		emptyIndex = inputEmpty;
		prevEmptyIndex = inputPrevEmpty;
		previousBoardStates = inputBoardStates;
	}
	
	
	//Check how many tiles are in the correct position, if all 9 are then the board is won
	public int check()
	{
		int count=0;
		for(int i=0; i<9; i++)
		{
			if(board[i]==winBoard[i])
			{
				count++;
			}
		}
		return count;
	}
	
	//Check the manhattan distance of the board by checking each tile
	public int manhattanDistanceBoard() {
        int sum = 0;
        for (int i = 0; i < 9; i++)
		{
			//if the tile is not in the correct position then  find its distance
            if (board[i] != winBoard[i] && board[i] != 0)
			{
                sum += manhattanDistanceTile(i);
			}
		}
        return sum;
    }
	
	
	//Check manhattan distance of a tile
	public int manhattanDistanceTile(int tile)
	{
		int correctPosition=20;
		//find the correct position for the number at the tile in question
		for(int i=0; i<9; i++)
		{
			if(winBoard[i]==board[tile])
			{
				correctPosition=i;
			}
		}
		//find the distance between the current position and correct position
		int colDistance = Math.abs(tile-correctPosition)%3;
		int rowDistance = Math.abs(tile/3-correctPosition/3);
		return colDistance+rowDistance;
	}
	
	//Create a new board with the empty tile and the input tile swapped and store old board state
	public Board swap(int tileIndex) {
		//Add the current board to the list of previous board states before swapping
		List<Board> oldStates = new ArrayList<Board>();
		for (int i = 0; i < previousBoardStates.size(); i++)
		{
            Board temp = previousBoardStates.get(i);
			oldStates.add(temp);
		}
		Board thisBoard = new Board(board, previousBoardStates, score, emptyIndex, prevEmptyIndex);
		oldStates.add(thisBoard);
		
		//Create an array with the numbers swapped
		int[] newBoard = new int[9];
		for(int i=0; i<9; i++)
		{
			newBoard[i] = board[i];
		}
		int temp = newBoard[emptyIndex];
		newBoard[emptyIndex]=board[tileIndex];
		newBoard[tileIndex]=temp;
		
		//Create the new board and keep track of the new score as well as new empty tiles
		Board swappedBoard = new Board(newBoard, oldStates, this.score+this.board[tileIndex], tileIndex, emptyIndex);
        return swappedBoard;
    }
	
	//Used to see if the board has previously been in the state it is currently in, used to make sure that possible swaps doesn't add repeat boards, returns false if there is a repeat
	public boolean checkRepeat()
	{
		int[] currentState = board;
		for(int i = 0; i < previousBoardStates.size(); i++)
		{
			if(Arrays.equals(previousBoardStates.get(i).board, currentState))
				return false;
		}
		return true;
	}
	
	//List of possible swaps that could be made under the rules of the puzzle, checks if the possible swap is a state the board has already been in and if so does not return it
	public Iterable<Board> possibleSwaps() { 

        Board possibleBoard;
        Queue<Board> boardList = new LinkedList<Board>();

		//If in second/third row
        if (emptyIndex > 2) {
            possibleBoard = swap(emptyIndex-3);
			if(possibleBoard.checkRepeat())
			{
				boardList.add(possibleBoard);
			}
        }
		possibleBoard=null;
		//If in first/second row
        if (emptyIndex < 6) {
			possibleBoard = swap(emptyIndex+3);
			if(possibleBoard.checkRepeat())
			{
				boardList.add(possibleBoard);
			}
        }
		possibleBoard=null;
		//If in second/third collumn
        if (emptyIndex % 3 != 0) {
			possibleBoard = swap(emptyIndex-1);
			if(possibleBoard.checkRepeat())
			{
				boardList.add(possibleBoard);
			}
        }
		possibleBoard=null;
		//If in first/second collumn
        if (emptyIndex % 3 != 2) {
			possibleBoard = swap(emptyIndex+1);
			if(possibleBoard.checkRepeat())
			{
				boardList.add(possibleBoard);
			}
        }
		possibleBoard=null;
        return boardList;
    }
	
	//Convert the board into a string representation for printing to console
	public String boardString()
	{
		StringBuilder boardString = new StringBuilder();
		for(int i=0; i<9; i++)
		{
			boardString.append(String.format("%2d ", board[i]));
			if (i%3==2)
			{
                boardString.append("\n");
			}
		}
		return boardString.toString();
	}
	
	
}