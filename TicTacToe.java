import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class TicTacToe {

    char grid[][];
    String winners[];

    int currentUser;
    int userInput;
    int currentNumberWinners;

    Scanner input = new Scanner(System.in);

    public TicTacToe(int winners) {

	// Initialise variables
	this.grid = new char[3][3];
	this.winners = new String[winners];
	this.currentUser = 0;
	this.userInput = 0;
	this.currentNumberWinners = 0;

	readResultsFile();

	do {

	    // Menu
	    System.out.println("-----------------------");
	    System.out.println("1. New Game");
	    System.out.println("2. Display results");
	    System.out.println("3. Exit");
	    System.out.println("-----------------------");

	    userInput = Integer.parseInt(input.nextLine());

	    // Menu Options
	    if (userInput == 1) {
		// Player names
		System.out.println("Player one name? ");
		String playerOne = input.nextLine();

		System.out.println("Player two name? ");
		String playerTwo = input.nextLine();

		displayGrid(playerOne, playerTwo);
	    } else if (userInput == 2) {
		displayWinners();

	    } else if (userInput == 3) {
		writeResultsToFile();
		System.out.println("Goodbye");
	    } else {
		System.out.println("Invalid entry\nSelect 1 -3!f");
	    }

	} while (userInput != 3);

    }

    public void newGame(String playerOne, String playerTwo) {

	String userInput;

	// Check which Players turn
	if (this.currentUser == 0) {
	    System.out.println("Player One (" + playerOne + ") Move:");
	    userInput = input.nextLine();

	} else {
	    System.out.println("Player Two (" + playerTwo + ") Move:");
	    userInput = input.nextLine();
	}

	// Convert Input to independent Char values
	boolean valid = true;
	int X = 0;
	int Y = 0;

	// Check valid input length
	if (userInput.length() == 2) {
	    X = Character.toLowerCase(userInput.charAt(0)) - 97;
	    Y = (char) userInput.charAt(1) - 49;

	} else {
	    System.out.println("Invalid Entry, Try again!\nValid answer example: “A1”\n");
	    valid = false;
	}

	// Check that the X / Y Values are valid
	if (X < 0 || X > 2 || Y < 0 || Y > 2) {
	    System.out.println("Invalid Entry, Try again!\nValid answer example: “A1”\n");
	    valid = false;
	} else {

	    // Check the spot is blank
	    if (Character.toLowerCase(this.grid[X][Y]) != 0) {
		System.out.println("Spot taken, Try again!\n");
		valid = false;
	    }
	}

	if (valid == false) {

	    newGame(playerOne, playerTwo);
	}

	// Display grid and check if winner
	if (valid == true && this.currentUser == 0) {
	    this.grid[X][Y] = 'X';
	    this.currentUser = 1;
	    displayGrid(playerOne, playerTwo);

	} else if (valid == true && this.currentUser == 1) {
	    this.grid[X][Y] = 'O';
	    this.currentUser = 0;
	    displayGrid(playerOne, playerTwo);

	}
    }

    public boolean checkWinner() {

	boolean winner = false;

	// Check horizontal
	for (int x = 0; x < 3; x++) {

	    if (Character.toLowerCase(this.grid[x][0]) != 0
		    && Character.toLowerCase(this.grid[x][0]) == Character.toLowerCase(this.grid[x][1])
		    && Character.toLowerCase(this.grid[x][0]) == Character.toLowerCase(this.grid[x][2])) {

		winner = true;
	    }
	}

	// Check vertical
	for (int y = 0; y < 3; y++) {

	    if (Character.toLowerCase(this.grid[0][y]) != 0
		    && Character.toLowerCase(this.grid[0][y]) == Character.toLowerCase(this.grid[1][y])
		    && Character.toLowerCase(this.grid[0][y]) == Character.toLowerCase(this.grid[2][y])) {

		winner = true;
	    }
	}

	// "/" diagonal
	if (Character.toLowerCase(this.grid[0][0]) != 0
		&& Character.toLowerCase(this.grid[0][0]) == Character.toLowerCase(this.grid[1][1])
		&& Character.toLowerCase(this.grid[0][0]) == Character.toLowerCase(this.grid[2][2])) {

	    winner = true;
	}

	// "\" diagonal
	if (Character.toLowerCase(this.grid[0][2]) != 0
		&& Character.toLowerCase(this.grid[0][2]) == Character.toLowerCase(this.grid[1][1])
		&& Character.toLowerCase(this.grid[0][2]) == Character.toLowerCase(this.grid[2][0])) {

	    winner = true;
	}

	return winner;

    }

    public void displayGrid(String playerOne, String playerTwo) {

	// Display Grid
	System.out.println("\n\n      1   2   3");

	System.out.printf("A:    %S | %S | %S \n", this.grid[0][0], this.grid[0][1], this.grid[0][2]);
	System.out.println("     ---+---+---");
	System.out.printf("B:    %S | %S | %S \n", this.grid[1][0], this.grid[1][1], this.grid[1][2]);
	System.out.println("     ---+---+---");
	System.out.printf("C:    %S | %S | %S \n\n", this.grid[2][0], this.grid[2][1], this.grid[2][2]);

	// Check winner
	boolean winner = checkWinner();
	if (winner == true && this.currentUser == 0) {
	    System.out.println("Congratulations, Player Two (" + playerTwo + ") wins!\n");

	    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
	    LocalDateTime now = LocalDateTime.now();

	    // Update winners array
	    this.winners[this.currentNumberWinners] = dtf.format(now) + "," + playerTwo;
	    this.currentNumberWinners += 1;
		
	    // Autosave
	    writeResultsToFile();

	} else if (winner == true && this.currentUser == 1) {
	    System.out.println("Congratulations, Player one (" + playerOne + ") wins!\n");

	    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
	    LocalDateTime now = LocalDateTime.now();

	    // Update winners array
	    this.winners[this.currentNumberWinners] = dtf.format(now) + "," + playerOne;
	    this.currentNumberWinners += 1;
		
	    // Autosave
	    writeResultsToFile();

	} else if (winner == false) {
	    newGame(playerOne, playerTwo);
	}

    }

    public void displayWinners() {

	if (this.currentNumberWinners != 0) {

	    System.out.println("----------------------------------");
	    System.out.println("  Date and time  | Winner ");
	    System.out.println("----------------------------------");

	    for (int i = 0; i < this.currentNumberWinners; i++) {

		String result[] = this.winners[i].split(",");
		String date = result[0];
		String winner = result[1];

		System.out.printf("%s | %-3s \n", date, winner);

	    }
	    System.out.println("----------------------------------\n");
	} else {

	    System.out.println("No past results, Try playing a new game!\n");
	}

    }

    public void extendArray() {

	String[] tempWinners = new String[+20];

	for (int i = 0; i < this.winners.length; i++) {
	    tempWinners[i] = this.winners[i];

	}

	this.winners = new String[tempWinners.length];

	for (int i = 0; i < this.winners.length; i++) {
	    this.winners[i] = tempWinners[i];

	}

    }

    public void readResultsFile() {

	try {
	    Scanner fileScanner = new Scanner(new FileReader("pastResults.txt"));

	    while (fileScanner.hasNextLine()) {

		// extend array if needed
		if (this.currentNumberWinners == this.winners.length) {
		    extendArray();

		}

		this.winners[this.currentNumberWinners] = fileScanner.nextLine();
		this.currentNumberWinners++;

	    }

	} catch (FileNotFoundException e) {
	    // TODO Auto-generated catch block

	}

    }

    public void writeResultsToFile() {

	// write array to data file
	try {
	    PrintWriter pw = new PrintWriter(new FileWriter("pastResults.txt"));

	    for (int i = 0; i < this.currentNumberWinners; i++) {

		pw.println(this.winners[i]);
	    }
	    pw.close();

	} catch (IOException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}

    };

    public static void main(String[] args) {
	TicTacToe TicTacToe;
	TicTacToe = new TicTacToe(20);
    }

}
