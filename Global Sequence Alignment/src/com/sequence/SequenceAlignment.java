package com.sequence;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class SequenceAlignment {
	
	int rows;
	int columns;
	int gap = 2;
	int mismatch = 1; 
	
	char[] stringA;
	char[] stringB;
	
	int[][] opt;

	SequenceAlignment(String stringA, String stringB) {
		this.rows = stringA.length();
		this.columns = stringB.length();
		this.stringA = stringA.toCharArray();
		this.stringB = stringB.toCharArray();
		this.opt = new int[rows][columns];
	}
	
	int calculateEditDistance() {
		
		opt[rows-1][columns-1] = 0;
		
		for(int i=rows-2;i>=0;i--)
			opt[i][columns-1] = gap+opt[i+1][columns-1];

		for(int i=columns-2;i>=0;i--)
			opt[rows-1][i] = gap+opt[rows-1][i+1];

		for(int i=rows-2; i>=0; i--) {
			for(int j=columns-2; j>=0; j--) {
				int temp = findMatch(this.stringA[i], this.stringB[j]);
				opt[i][j] = minimum(opt[i+1][j]+gap, opt[i][j+1]+gap, opt[i+1][j+1]+temp);
			}
		}
		//printEditDistance();
		//findAlignment();
		return opt[0][0];
	}
	
	int findMatch(char c1, char c2) {
		if(c1 == c2)
			return 0;
		else
			return 1;
	}
	int minimum(int a,int b, int c) {
		if(a<b && a<c) 
			return a;
		else if (b<a && b<c)
			return b;
		else
			return c;
	}
	
	void findAlignment() {
		int i=0,j=0;
		
		List<Character> newStringA = new ArrayList<Character>();
		List<Character> newStringB = new ArrayList<Character>();
		
		while(i!=rows-1 && j!=columns-1) {
			if (opt[i][j] == opt[i+1][j]+gap) {
				//System.out.println(" stringB should contain Gap");
				newStringA.add(stringA[i]);
				newStringB.add(' ');
				i++;
			} else if (opt[i][j] == opt[i][j+1]+gap) {
				//System.out.println(" stringA should contain Gap");
				newStringA.add(' ');
				newStringB.add(stringB[j]);
				j++;	
			} else if (opt[i][j] == opt[i+1][j+1]) {
				//System.out.println(" characters match : "+stringA[i]+" "+stringB[j]);
				newStringA.add(stringA[i]);
				newStringB.add(stringB[j]);
				i++;
				j++;
			} else if (opt[i][j] == opt[i+1][j+1]+mismatch) {
				//System.out.println(" characters mismatch : "+stringA[i]+" "+stringB[j]);
				newStringA.add(stringA[i]);
				newStringB.add(stringB[j]);
				j++;
				i++;
			}
		}
		
		while(i!=rows-1) {
			//Add the remaining characters to stringA and gaps to stringB
			newStringA.add(stringA[i]);
			newStringB.add(' ');
			i++;
		}
		
		while(j!=columns-1) {
			//Add the remaining characters to stringB and gaps to stringA
			newStringA.add(' ');
			newStringB.add(stringB[j]);
			j++;
		}
		
		System.out.println("String A : "+newStringA);
		System.out.println("String B : "+newStringB);

	}
	
	void printEditDistance() {
		for(int i=0;i<rows;i++) {
			for(int j=0;j<columns;j++) {
				System.out.print(opt[i][j]+" ");
			}
			System.out.println();
		}
	}
	
	public static void main(String[] args) {
		String stringA="", stringB="";
		
		Scanner scan;
		//Reading data from command line
		
		/*scan = new Scanner(System.in);
		System.out.println("Enter the first string (without space):");
		stringA = scan.nextLine();
		System.out.println("Enter the second string (without space):");
		stringB = scan.nextLine();
		stringA+=" ";
		stringB+=" ";*/
		
		//Reading Data from file
		System.out.println("Enter the file name with extension");
		scan = new Scanner(System.in);
		File file = new File(scan.nextLine());
		scan.close();
		try {
			scan = new Scanner(file);
			while(scan.hasNextLine()) {
				stringA = scan.nextLine()+" ";
				stringB = scan.nextLine()+" ";
			}
		} catch(FileNotFoundException e) {
			System.out.println("File not Found !!");
		} finally {
			scan.close();
		}
		System.out.println("Distance between the 2 strings is : "+new SequenceAlignment(stringA+" ",stringB+" ").calculateEditDistance());
	}

}
