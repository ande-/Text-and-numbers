package machineProject;

import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;


public class Keypad {
	HashMap<Character, Integer> map;
	Dictionary dict = new Dictionary("./words.txt");
	
	/**
	 * Constructor creates the map of letters and numbers
	 */
	public Keypad() {
		map = new HashMap<Character, Integer>();
		
		int j = 2;
		for(int i=97; i<=122; i++) {
			if(i !=97 && i<113 && i%3==1) {
				j++;
			}
			else if(i==116 || i==119) {
				j++;
			}
			map.put((char) i, j);
		}
	}

	/**
	 * Converts a String of letters to their corresponding numbers
	 * @param input a String of letters
	 * @return a String of numbers
	 */
	public String letters2numbers(String input) {
		String letters = input.toLowerCase();
		String numbers = "";
		for(int i=0; i<letters.length(); i++) {
			if(map.containsKey(letters.charAt(i))) {
				numbers += map.get(letters.charAt(i));
			}
			else {
				numbers += letters.charAt(i);
			}
		}
		return numbers;
	}
	
	/**
	 * Calculates how many different Strings of letters can exist for a given String of numbers
	 * @param numbers a String of numbers
	 * @return the number of possible combinations
	 */
	public int numberOfCombinations(String numbers) {
		int last = 1;
		for(int n=0; n<numbers.length(); n++) {
			int entries = getMatches(numbers.charAt(n)).length();		
			if(entries>0) {
				last = last*entries;
			}
		}
		return last;
	}
	
	/**
	 * Uses the HashMap to find all the letter matches (3 or 4) for each number
	 * @param number a number
	 * @return a String of all the letters mapped to that number in the HashMap
	 */
	public String getMatches(char number) {
        	
            String matches = "";
            for(Character c : map.keySet()) {
                if(map.get(c).toString().charAt(0)==number) {  //converting the Integer to a char in order to check for equals
                matches += c;
                }
            }
            return matches;
	}
	
	/**
	 * Finds all the possible letter combinations for a String of numbers
	 * @param numbers a String of numbers
	 * @return an array of Strings, each a possible combination of letters
	 */
    public String[] numbers2letters(String numbers) {
        String[] allCombos = new String[numberOfCombinations(numbers)];
        
        //I have to fill the array list with strings so I can concatenate onto them
        for(int index=0; index<allCombos.length; index++) {
            allCombos[index] = "";
        }
        //will be successively dividing the array into clumps
        int clumpsize = allCombos.length; 
        for(int number=0; number<numbers.length(); number++) {
        	String matches = getMatches(numbers.charAt(number));
        	//if something other than 2-9 is entered, just put that character in
            if(matches.length()==0) { 
            	for(int index=0; index<allCombos.length; index++) {
                    allCombos[index] += numbers.charAt(number);
                }
            }
            else {
            	clumpsize = clumpsize/matches.length();
            	int numberOfClumps = allCombos.length/clumpsize;
            	int clumpstart = 0;
            	//this loop will specify how many times we need to cycle through appending letters
            	for(int clumpsDone = 0; clumpsDone<numberOfClumps; clumpsDone++) { 
            		//this loop will specify which letter match we are appending
            		for(int letter=0; letter<matches.length(); letter++) { 
            			//this loop will specify the region of the array to add to
            			for(int slot=clumpstart; slot<(clumpsize+clumpstart) && slot<allCombos.length; slot++) { 
            				allCombos[slot] += matches.charAt(letter);
            			}
            			clumpstart += clumpsize;
            		}
            	}
            }
        }
        return allCombos;
    }
    
    /**
     * Runs all the letter combinations from a String of numbers through a dictionary
     * and returns an array of those which are possible
     * @param numbers a String of numbers
     * @return an array of words
     */
	public String[] numbers2words(String numbers) {
		
		List<String> dictionaryResult = new ArrayList<String>();
		String[] stuff = numbers2letters(numbers);
		for(int letterCombo=0 ; letterCombo<stuff.length; letterCombo++) {
			if(!dict.lookup(stuff[letterCombo]).isEmpty()) {
				dictionaryResult.add(dict.lookup(stuff[letterCombo]).get(0));
			}
		}
		String[] wordsToReturn = new String[dictionaryResult.size()];
		for(int word=0; word<wordsToReturn.length; word++) {
			wordsToReturn[word] = dictionaryResult.get(word);
		}
		return wordsToReturn;
	}
	
	//Getters for measurements taken by the Dictionary
	public long getLastComparisons() {
		return dict.getLastComparisons();
	}
	public double getAverageComparisons() {
		return dict.getAverageComparisons();
	}
	public long getLastTiming() {
		return dict.getLastTiming();
	}
	public double getAverageTiming() {
		return dict.getAverageTiming();
	}
}

