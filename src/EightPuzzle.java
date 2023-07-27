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



public class EightPuzzle {
	
	
	
	
	//Find which method the user wanted to use and send the chosen board through the solution method
	public EightPuzzle(Board currentBoard, String input) {    
		System.out.println("Finding solution");
		Board solutionBoard = currentBoard;
		if (input.equals("1"))
		{
			solutionBoard=bfs(currentBoard);
			System.out.println("Ran BFS");
		}
		else if (input.equals("2"))
		{
			solutionBoard=dfs(currentBoard);
			System.out.println("Ran DFS");
		}
		else if (input.equals("3"))
		{
			solutionBoard=ucs(currentBoard);
			System.out.println("Ran UCS");
		}
		else if (input.equals("4"))
		{
			solutionBoard=gbf(currentBoard);
			System.out.println("Ran GBF");
		}
		else if (input.equals("5"))
		{
			solutionBoard=heuristic1(currentBoard);
			System.out.println("Ran H1");
		}
		else if (input.equals("6"))
		{
			solutionBoard=heuristic1(currentBoard);
			System.out.println("Ran H2");
		}
		
		String tempString = solutionBoard.boardString();
		System.out.println("Solution found");
		System.out.println(tempString);
		
		
		
		
		
		//Once a solution is found use the list of previous board states which keeps track of swapped values and show the sequence of events to obtain this solution
		System.out.println("The order of correct moves is as follows");
		System.out.println("Initial board:");
		System.out.println(solutionBoard.previousBoardStates.get(0).boardString());
		System.out.println("Amount of moves(Length):");
		System.out.println(solutionBoard.previousBoardStates.size());
		String empty = "";
		String prevEmpty = "";
		StringBuilder boardString = new StringBuilder();
		int i = 1;
		for (Board temp = solutionBoard.previousBoardStates.get(i); i < solutionBoard.previousBoardStates.size()-1; temp = solutionBoard.previousBoardStates.get(i)) {
			boardString.setLength(0);
			boardString.append("Swapping ");
			boardString.append(String.format("%2d ", temp.board[temp.emptyIndex]));
			boardString.append(" & ");
			if(temp.prevEmptyIndex!=30)
				boardString.append(String.format("%2d ", temp.board[temp.prevEmptyIndex]));
			if(temp.emptyIndex==temp.prevEmptyIndex)
				boardString.setLength(0);
			System.out.println(boardString);
			System.out.println("Board is now: ");
			System.out.println(temp.boardString());
			System.out.println("Score is now: ");
			System.out.println(temp.score);
			i++;
		}
		//second to last swap, I ran into issues listing everything in one loop so I had to seperate the second to last and last swaps
		Board tempBoard = solutionBoard.previousBoardStates.get(solutionBoard.previousBoardStates.size()-1);
		boardString.setLength(0);
		boardString.append("Swapping ");
		boardString.append(String.format("%2d ", tempBoard.board[tempBoard.emptyIndex]));
		boardString.append(" & ");
		boardString.append(String.format("%2d ", tempBoard.board[tempBoard.prevEmptyIndex]));
		System.out.println(boardString);
		System.out.println("Board is now: ");
		System.out.println(tempBoard.boardString());
		System.out.println("Score is now: ");
		System.out.println(tempBoard.score);
		
		//Last swap
		boardString.setLength(0);
		boardString.append("Swapping ");
		boardString.append(String.format("%2d ", solutionBoard.board[solutionBoard.emptyIndex]));
		boardString.append(" & ");
		boardString.append(String.format("%2d ", solutionBoard.board[solutionBoard.prevEmptyIndex]));
		System.out.println(boardString);
		System.out.println("Board is now: ");
		System.out.println(solutionBoard.boardString());
		System.out.println("Score(cost) is now: ");
		System.out.println(solutionBoard.score);
	}
	
	
	//Breadth first search method, takes the starting board as the first element examined and adds each child board to a queue,
	//repeating the process on each board in the queue until a solution is found, repeats are filtered in the board class
	public Board bfs(Board board) {
		System.out.println("Running BFS");
		Board result = board;
		int time = 0;
		int space = 0;
		Queue<Board> boardList = new LinkedList<Board>();
		
		while(result.check()!=9)
		{
			time++;
			//adds neighboring board to the queue
			for (Board temp : result.possibleSwaps()) {
				boardList.add(temp);
				if(space < boardList.size())
					space = boardList.size();
			}
			result=boardList.remove();
		}
		System.out.println("Time is ");
		System.out.println(time);
		System.out.println("Space is ");
		System.out.println(space);
		return result;
	}


	//Depth first search method, takes the starting board and adds child boards to a stack, then popping the top element off the stack 
	//checking for solution and then adding children to the stack. I limited the boards examined to be only ones with less then 50 moves
	//since the maximum amount of moves for the ideal solution is under this and if I don't limit it then the program takes forever
	public Board dfs(Board board) {
		Stack<Board> boardList = new Stack<Board>();
		int time = 0;
		int space = 0;
		while(board.check()!=9)
		{
			time++;
			for (Board temp : board.possibleSwaps()) {
				if(temp.previousBoardStates.size()<50)
				{
					boardList.push(temp);
					if(space < boardList.size())
						space = boardList.size();
				}
			}
			board=boardList.pop();
			
		}
		System.out.println("Time is ");
		System.out.println(time);
		System.out.println("Space is ");
		System.out.println(space);
		return board;
	}
	
	//Uniform cost search method, uses a special comparator class so that it can rank boards based off of lowest score, the priorirty queue
	//implements this comparator and ranks each frontier board in order of lowest score and expands the next node based off of this rank
	public Board ucs(Board board) {
		PriorityQueue<Board> costQueue = new PriorityQueue<Board>(500, new UCSComparator());
		int time = 0;
		int space = 0;
		while(board.check()!=9)
		{
			time++;
			for (Board temp : board.possibleSwaps()) {
				costQueue.add(temp);
				if(space < costQueue.size())
					space = costQueue.size();
			}
			board=costQueue.remove();
		}
		System.out.println("Time is ");
		System.out.println(time);
		System.out.println("Space is ");
		System.out.println(space);
		return board;
	}

	
	//Greedy best first method, examines frontier boards for highest check method values and adds boards with highest value to a queue
	//examines the next in queue and repeats, each time seeing if the check value is 9 whihc indicates a solution
	public Board gbf(Board board) {
		Queue<Board> boardList = new LinkedList<Board>();
		int count;
		int time = 0;
		int space = 0;
		while(board.check()!=9)
		{
			time++;
			count = 0;
			for (Board temp : board.possibleSwaps()) {
				if(count < temp.check())
					count = temp.check();
			}
			for (Board temp : board.possibleSwaps()) {
				if(count == temp.check())
				{
					boardList.add(temp);
					if(space < boardList.size())
						space = boardList.size();
				}
			}
			board=boardList.remove();
		}
		System.out.println("Time is ");
		System.out.println(time);
		System.out.println("Space is ");
		System.out.println(space);
		return board;
	}
	
	//A* search using H1, uses a special comparator class so that it can rank boards based off of highest value obtained from check method 
	//which checks amount of pieces in correct position, the priorirty queue implements this comparator and ranks each frontier board in 
	//order of highest check value and expands the next node based off of this rank
	public Board heuristic1(Board board) {
		PriorityQueue<Board> h1Queue = new PriorityQueue<Board>(500, new H1Comparator());
		int time = 0;
		int space = 0;
		while(board.check()!=9)
		{
			time++;
			for (Board temp : board.possibleSwaps()) {
				h1Queue.add(temp);
				if(space < h1Queue.size())
					space = h1Queue.size();
			}
			board=h1Queue.remove();
		}
		System.out.println("Time is ");
		System.out.println(time);
		System.out.println("Space is ");
		System.out.println(space);
		return board;
	}

	//A* search using H2, uses a special comparator class so that it can rank boards based off of lowest value obtained from manhattan distance method 
	//which scores the distance of pieces not in correct position, the priorirty queue implements this comparator and ranks each frontier board in 
	//order of lowest distance value and expands the next node based off of this rank
	public Board heuristic2(Board board) {
		PriorityQueue<Board> h2Queue = new PriorityQueue<Board>(500, new H2Comparator());
		int time = 0;
		int space = 0;
		while(board.check()!=9)
		{
			time++;
			for (Board temp : board.possibleSwaps()) {
				h2Queue.add(temp);
				if(space < h2Queue.size())
					space = h2Queue.size();
			}
			board=h2Queue.remove();
		}
		System.out.println("Time is ");
		System.out.println(time);
		System.out.println("Space is ");
		System.out.println(space);
		return board;
	}

    public static void main(String[] args) throws IOException {
		
		//Creates boards for testing which the user will choose from
		int[] testBoard1 = new int[]{1,3,4,8,6,2,7,0,5};
		int[] testBoard2 = new int[]{2,8,1,0,4,3,7,6,5};
		int[] testBoard3 = new int[]{5,6,7,4,0,8,3,2,1};

		Board board1 = new Board(testBoard1);
		Board board2 = new Board(testBoard2);
		Board board3 = new Board(testBoard3);
		
		//keeps track of board chosen by user
		Board currentBoard = board1;
		
		
		
		
		//This block of code is just for determining which algorithm and which board the user wants to use and sends it to the find solution method
        System.out.println("This is an AI that solves a modified version of the eight puzzle");
		System.out.println("The test boards are as follows");
		System.out.println("Board 1:");
		System.out.println(board1.boardString());
		System.out.println("Board 2:");
		System.out.println(board2.boardString());
		System.out.println("Board 3:");
		System.out.println(board3.boardString());
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in)); 
		String input = "";
		boolean loop = true;
		while(loop) {
			System.out.println("Choose a board by entering 1, 2, or 3");
			System.out.println("");
			System.out.flush();
			input = in.readLine();
			if (input.equals("1"))
			{
				currentBoard=board1;
				loop=false;
			}
			else if (input.equals("2"))
			{
				currentBoard=board2;
				loop=false;
			}
			else if (input.equals("3"))
			{
				currentBoard=board3;
				loop=false;
			}
			else
			{
				System.out.println("Improper input, try again");
			}
		}
		loop = true;
		while(loop) {
			System.out.println("Now choose an algorithm by typing 1 for BFS, 2 for DFS, 3 for UCS, 4 for GBF, 5 for h1, or 6 for h2");
			System.out.println("");
			System.out.flush();
			input = in.readLine();
			if (input.equals("1")||input.equals("2")||input.equals("3")||input.equals("4")||input.equals("5")||input.equals("6"))
			{
				loop=false;
			}
			else
			{
				System.out.println("Improper input, try again");
			}
		}
		System.out.println("Checking algorithm");
		System.out.println("Input is:");
		System.out.println(input);
		System.out.println("Your board is:");
		System.out.println(currentBoard.boardString());
		
		
		//Send user choices to proper algorithm
		EightPuzzle findSolution = new EightPuzzle(currentBoard, input);

    }

}