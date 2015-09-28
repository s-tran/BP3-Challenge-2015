package bp3Challenge;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainParse {

	public static void main(String[] args) throws FileNotFoundException, JSONException {
		
		String jsonData = "";
		BufferedReader br = null;
		try {
			String line;
			br = new BufferedReader(new FileReader("/Users/Stephen/Documents/School/EE422C/EE422C Assignment WORKSPACE/bp3/task-2.json"));
			while ((line = br.readLine()) != null) {
				jsonData += line + "\n";
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null)
					br.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}

		JSONArray arr = new JSONArray(jsonData);

		ParseObj[] myArr = new ParseObj[arr.length()];

		for (int i = 0; i < arr.length(); i++) {
			myArr[i] = new ParseObj();
			if (arr.getJSONObject(i).isNull("closeDate") == true){
				myArr[i].closeDate = "9999999999";
				String crDate = arr.getJSONObject(i).getString("createDate");
				myArr[i].createDate = crDate.substring(0, 10);
				myArr[i].instanceId = arr.getJSONObject(i).getInt("instanceId");
				myArr[i].name = arr.getJSONObject(i).getString("name");
				myArr[i].assignee = arr.getJSONObject(i).getString("assignee");
				myArr[i].status = arr.getJSONObject(i).getString("status");
			} else {
				String clDate = arr.getJSONObject(i).getString("closeDate");
				myArr[i].closeDate = clDate.substring(0, 10);
				String crDate = arr.getJSONObject(i).getString("createDate");
				myArr[i].createDate = crDate.substring(0, 10);
				myArr[i].instanceId = arr.getJSONObject(i).getInt("instanceId");
				myArr[i].name = arr.getJSONObject(i).getString("name");
				myArr[i].assignee = arr.getJSONObject(i).getString("assignee");
				myArr[i].status = arr.getJSONObject(i).getString("status");
			}
		}
		
		String toClose = "close";
		Scanner scanner = new Scanner (System.in);
		System.out.print("Please enter your name: \n");
		String name = scanner.next(); // Get what the user types.
		System.out.println("\nHi, " + name + ". Please type \"close\" now or after a task has completed to end program."
				+ " To continue, type anything and hit enter.");
		while (toClose.compareTo(scanner.next()) != 0){
			System.out.println("\nWhich task would you like to do: (Please enter a number 1 to 5 and hit enter)"
				+ "\n 1) Given a specific date provide the current number of open and closed tasks."
				+ "\n 2) Given a specific start and end date, how many tasks were opened and how many were closed in that range."
				+ "\n 3) Given a particular instanceId, provide the name of the most recent task."
				+ "\n 4) Given a particular instanceId, provide the count of tasks."
				+ "\n 5) Given a particular assignee, provide the count of open and closed tasks for that assignee.");
			int choice = scanner.nextInt();
		
			if (choice == 1){
				System.out.println("\nPlease provide the specific date in YYYY-MM-DD format:");
				String date1 = scanner.next();
				currentOpenClose(date1, myArr, arr.length());
				System.out.println("\nYou may now type anything to continue or \"close\" to exit.");
			}
			else if (choice == 2){
				System.out.println("\nPlease provide the start date in YYYY-MM-DD format:");
				String dateS = scanner.next();
				System.out.println("\nPlease provide the end date in YYYY-MM-DD format:");
				String dateE = scanner.next();
				rangeOpenClose(dateS, dateE, myArr, arr.length());
				System.out.println("\nYou may now type anything to continue or \"close\" to exit.");
			}
			else if (choice == 3){
				System.out.println("\nPlease provide the instanceId:");
				int Id1 = scanner.nextInt();
				mostRecent(Id1, myArr, arr.length());
				System.out.println("\nYou may now type anything to continue or \"close\" to exit.");
			}
			else if (choice == 4){
				System.out.println("\nPlease provide the instanceId:");
				int Id2 = scanner.nextInt();
				countTasks(Id2, myArr, arr.length());
				System.out.println("\nYou may now type anything to continue or \"close\" to exit.");
			}
			if (choice == 5){
				System.out.println("\nPlease provide the assignee:");
				String Asgn = scanner.next();
				countAssigneeTasks(Asgn, myArr, arr.length());
				System.out.println("\nYou may now type anything to continue or \"close\" to exit.");
			}
		}
		
		System.out.println("\nThank you for using my program, " + name + "! Have a good day! :)");
		scanner.close();
		
		//rangeOpenClose(myArr[599].createDate, myArr[600].closeDate, myArr, arr.length());
		//mostRecent(myArr[1].instanceId, myArr, arr.length());
		//countTasks(myArr[0].instanceId, myArr, arr.length());
		//countAssigneeTasks(myArr[0].assignee, myArr, arr.length());
	}
	
//	Given a specific date provide the current number of open and closed tasks.
//	The date is inclusive so if we ask for midnight Oct 12, a task opened or closed on midnight would count
	// needs 10 chars from input and assumes nothing is opened and closed on the same date
	public static void currentOpenClose(String date, ParseObj[] myArr, int len) {
		int open = 0;
		int close = 0;
		for (int i = 0; i < len; i++) {
			if ((date.compareTo(myArr[i].createDate) == 0 || date.compareTo(myArr[i].createDate) > 0) && date.compareTo(myArr[i].closeDate) < 0){
				open++;
			}
			else if ((date.compareTo(myArr[i].closeDate) == 0 || date.compareTo(myArr[i].closeDate) > 0)){
				close++;
			}
		}
		System.out.println("\nOn the date: " + date + ", there were " + open + " open tasks and " + close + " closed tasks.");
		return;
	}
	
//	Given a specific start and end date, how many tasks were opened
//	and how many were closed in that range. The start date is inclusive, the end date is exclusive.
	public static void rangeOpenClose(String start, String end, ParseObj[] myArr, int len) {
		int open = 0;
		int close = 0;
		for (int i = 0; i < len; i++) {
			if ((start.compareTo(myArr[i].createDate) == 0 || start.compareTo(myArr[i].createDate) < 0) && end.compareTo(myArr[i].createDate) > 0){
				open++;
			}
			if ((start.compareTo(myArr[i].closeDate) == 0 || start.compareTo(myArr[i].closeDate) < 0) && end.compareTo(myArr[i].closeDate) > 0){
				close++;
			}
		}
		System.out.println("\nBetween the dates: " + start + " and " + end + ", there were " + open + " tasks that were opened and " + close + " tasks that were closed.");
		return;
	}
	
//	Given a particular instanceId, provide the name of the most recent task.
	// assumes no 2 tasks for a particular instanceId are not opened on the same date
	public static void mostRecent(int Id, ParseObj[] myArr, int len) {
		String recentDate = "0000000000";
		String recent = "0";
		for (int i = 0; i < len; i++) {
			if (myArr[i].instanceId == Id){
				if (recentDate.compareTo(myArr[i].createDate) < 0){
					recent = myArr[i].name;
					recentDate = myArr[i].createDate;
				}
			}
		}
		System.out.println("\nGiven the instanceId: " + Id + ", the most recently opened task associated with Id #" + Id + " is \"" + recent + "\".");
		return;
	}
	
//	Given a particular instanceId, provide the count of tasks.
	// assumes no unique task is listed twice in the json file
	// meaning every time the instanceId is listed, it is assumed to be a unique task
	public static void countTasks(int Id, ParseObj[] myArr, int len) {
		int count = 0;
		for (int i = 0; i < len; i++) {
			if (myArr[i].instanceId == Id){
				count++;
			}
		}
		System.out.println("\nThe instanceId " + Id + " has " + count + " associated tasks.");
		return;
	}
	
//	Given a particular assignee, provide the count of open and closed tasks for that assignee.
	// assumes received is considered open
	public static void countAssigneeTasks(String assign, ParseObj[] myArr, int len) {
		int open = 0;
		int close = 0;
		String closed = "Closed";
		String received = "Received";
		for (int i = 0; i < len; i++) {
			if (assign.compareTo(myArr[i].assignee) == 0){
				if (closed.compareTo(myArr[i].status) == 0){
					close++;
				}
				if (received.compareTo(myArr[i].status) == 0){
					open++;
				}
			}
		}
		System.out.println("\nThe assignee " + assign + " has " + open + " open tasks and " + close + " closed tasks.");
		return;
	}

}
