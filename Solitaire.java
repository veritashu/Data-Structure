package solitaire;

import java.io.IOException;
import java.util.Scanner;
import java.util.Random;

/**
 * This class implements a simplified version of Bruce Schneier's Solitaire Encryption algorithm.
 * 
 * @author RU NB CS112
 */
public class Solitaire {
	
	/**
	 * Circular linked list that is the deck of cards for encryption
	 */
	CardNode deckRear;
	
	/**
	 * Makes a shuffled deck of cards for encryption. The deck is stored in a circular
	 * linked list, whose last node is pointed to by the field deckRear
	 */
	public void makeDeck() {
		// start with an array of 1..28 for easy shuffling
		int[] cardValues = new int[28];
		// assign values from 1 to 28
		for (int i=0; i < cardValues.length; i++) {
			cardValues[i] = i+1;
		}
		
		// shuffle the cards
		Random randgen = new Random();
 	        for (int i = 0; i < cardValues.length; i++) {
	            int other = randgen.nextInt(28);
	            int temp = cardValues[i];
	            cardValues[i] = cardValues[other];
	            cardValues[other] = temp;
	        }
	     
	    // create a circular linked list from this deck and make deckRear point to its last node
	    CardNode cn = new CardNode();
	    cn.cardValue = cardValues[0];
	    cn.next = cn;
	    deckRear = cn;
	    for (int i=1; i < cardValues.length; i++) {
	    	cn = new CardNode();
	    	cn.cardValue = cardValues[i];
	    	cn.next = deckRear.next;
	    	deckRear.next = cn;
	    	deckRear = cn;
	    }
	}
	
	/**
	 * Makes a circular linked list deck out of values read from scanner.
	 */
	public void makeDeck(Scanner scanner) 
	throws IOException {
		CardNode cn = null;
		if (scanner.hasNextInt()) {
			cn = new CardNode();
		    cn.cardValue = scanner.nextInt();
		    cn.next = cn;
		    deckRear = cn;
		}
		while (scanner.hasNextInt()) {
			cn = new CardNode();
	    	cn.cardValue = scanner.nextInt();
	    	cn.next = deckRear.next;
	    	deckRear.next = cn;
	    	deckRear = cn;
		}
	}
	
	/**
	 * Implements Step 1 - Joker A - on the deck.
	 */
	void jokerA() {
		// COMPLETE THIS METHOD

		int temp;
		CardNode prev = deckRear;
		CardNode curr = deckRear.next;
		
		do {
			if (prev.cardValue == 27){
				temp = prev.cardValue;
				prev.cardValue = curr.cardValue;
				curr.cardValue = temp;
				
				return;
			}
			
			else if (curr.cardValue == 27){
				temp= curr.cardValue;
				curr.cardValue = curr.next.cardValue;
				curr.next.cardValue = temp;
				
				return;
			}
			
			else{
				prev = prev.next;
				curr = curr.next;
			}
			
		} while (prev != deckRear);
	}
	
	/**
	 * Implements Step 2 - Joker B - on the deck.
	 */
	void jokerB() {
	    // COMPLETE THIS METHOD

		int temp;
		CardNode prev = deckRear;
		CardNode curr = deckRear.next;
		
		do{
			if (prev.cardValue == 28 && prev == deckRear){
				temp = prev.cardValue;
				prev.cardValue = curr.cardValue;
				curr.cardValue = curr.next.cardValue;
				curr.next.cardValue = temp;
				
				return;
			}
			
			else if(prev.cardValue == 28){
				temp = prev.cardValue;
				prev.cardValue = curr.cardValue;
				curr.cardValue = curr.next.cardValue;
				curr.next.cardValue = temp;
				
				return;
			}
		
			else {
				prev = prev.next;
				curr = curr.next;
			}

		}while(prev != deckRear);

		
	}
	
	/**
	 * Implements Step 3 - Triple Cut - on the deck.
	 */
	void tripleCut() {
		// COMPLETE THIS METHOD

		CardNode temp = new CardNode ();
		CardNode prev = deckRear;
		CardNode curr = deckRear.next;
		CardNode firstJoker = deckRear;
		CardNode secondJoker = deckRear;
		CardNode firstCard = deckRear.next;
		CardNode beforeFirstJoker = new CardNode();
		int secondCardNumber = 0;
		
		//if two jokers are at both ends
		if ((prev.cardValue == 27 && curr.cardValue == 28) || (prev.cardValue == 28 && curr.cardValue == 27)){
			
			return;
		}
		
		//find the first joker
		do {
			firstJoker = firstJoker.next;
			if (firstJoker.cardValue == 27 || firstJoker.cardValue == 28){
				break;
			}
		
		}while (firstJoker != deckRear);
		
		//find the node before first joker
		do {
			if (curr.cardValue == firstJoker.cardValue){
				beforeFirstJoker = prev;
				break;
			}
			else {
				prev = prev.next;
				curr = curr.next;
			}

		}while (prev != deckRear);
		
		//find the second joker
		if (firstJoker.cardValue == 27){
			secondCardNumber = 28;
		}
		else{
			secondCardNumber = 27;
		}		
		
		do {
			if (secondJoker.cardValue == secondCardNumber){
				break;
			}
			else{
				secondJoker = secondJoker.next;
			}
			
		}while (secondJoker != deckRear);
		
		//if the first joker is at the beginning
		if (deckRear.next.cardValue == 27 || deckRear.next.cardValue == 28){
			deckRear = secondJoker;
			
			return;
		}
		
		//if the second joker is at the end

		if (deckRear.cardValue == 27 || deckRear.cardValue == 28){
			deckRear = beforeFirstJoker;
			
			return;
		}
	
		//swap
		deckRear.next = firstJoker;
		temp = secondJoker.next;
		beforeFirstJoker.next = temp;
		secondJoker.next = firstCard;
		deckRear = beforeFirstJoker;	

	}
	
	/**
	 * Implements Step 4 - Count Cut - on the deck.
	 */
	void countCut() {		
		// COMPLETE THIS METHOD
		

		CardNode temp = new CardNode ();
		CardNode prev = deckRear;
		CardNode curr = deckRear.next;
		CardNode beforeLast = new CardNode();
		CardNode lastCard = deckRear;
		int cardNumber = deckRear.cardValue;
		
		if (deckRear.cardValue == 27 || deckRear.cardValue == 28){
			
			return;
		}
		
		//find the node before last node
		do{
			prev = prev.next;
			curr = curr.next;
		}while (curr != deckRear);
		
		beforeLast = prev;
		prev = prev.next;
		curr = curr.next;

		int i = 0;
	
		do{
			prev = prev.next;
			curr = curr.next;
			i++;
		}while(i<cardNumber);
		
		temp = lastCard.next;
		prev.next = lastCard;
		beforeLast.next = temp;
		lastCard.next = curr;
		
	}
	
	/**
	 * Gets a key. Calls the four steps - Joker A, Joker B, Triple Cut, Count Cut, then
	 * counts down based on the value of the first card and extracts the next card value 
	 * as key. But if that value is 27 or 28, repeats the whole process (Joker A through Count Cut)
	 * on the latest (current) deck, until a value less than or equal to 26 is found, which is then returned.
	 * 
	 * @return Key between 1 and 26
	 */
	int getKey() {
		// COMPLETE THIS METHOD
		
				int firstCardNumber;
				int j;
				int keyNumber;
				
				CardNode key = new CardNode();	

				do{
					jokerA();
					jokerB();
					tripleCut();
					countCut();
					
					key = deckRear;
					j = 0;
					
					if (deckRear.next.cardValue == 28){
						firstCardNumber = 27;
					}
					else {
						firstCardNumber = deckRear.next.cardValue;
					}
					
					do {
						key = key.next;
						j++;
					}while(j < firstCardNumber);
					
					keyNumber = key.next.cardValue;
			
				}while (keyNumber == 28 || keyNumber == 27);
				
				return keyNumber;
	}
	
	/**
	 * Utility method that prints a circular linked list, given its rear pointer
	 * 
	 * @param rear Rear pointer
	 */
	private static void printList(CardNode rear) {
		if (rear == null) { 
			return;
		}
		System.out.print(rear.next.cardValue);
		CardNode ptr = rear.next;
		do {
			ptr = ptr.next;
			System.out.print("," + ptr.cardValue);
		} while (ptr != rear);
		System.out.println("\n");
	}

	/**
	 * Encrypts a message, ignores all characters except upper case letters
	 * 
	 * @param message Message to be encrypted
	 * @return Encrypted message, a sequence of upper case letters only
	 */
	public String encrypt(String message) {	
		// COMPLETE THIS METHOD

		String newMessage = message.replaceAll("[^A-Z]","");
		
		String encrypt;
		int newMessageNumber;
		char c;
		char newMessageArray[] = new char[newMessage.length()];
		
		for(int i = 0; i < newMessage.length(); i++){
			
			c = newMessage.charAt(i);
			
			newMessageNumber = 0;
			newMessageNumber = getKey() + (int)c;
			
			if (newMessageNumber > 90){
				newMessageNumber = newMessageNumber - 26;
			}

			newMessageArray[i] = (char) (newMessageNumber);
		}
		
		encrypt = String.valueOf(newMessageArray);

		return encrypt;
	}
	
	/**
	 * Decrypts a message, which consists of upper case letters only
	 * 
	 * @param message Message to be decrypted
	 * @return Decrypted message, a sequence of upper case letters only
	 */
	public String decrypt(String message) {	
		// COMPLETE THIS METHOD

		String decrypt;
		
		int messageNumber;
		char c;
		char messageArray[] = new char[message.length()];
		
		for(int i = 0; i < message.length(); i++){
			
			c = message.charAt(i);
			
			messageNumber = 0;
			messageNumber = (int)c - getKey();
			
			if (messageNumber < 64){
				messageNumber = messageNumber + 26;
			}

			messageArray[i] = (char) (messageNumber);
		}
		
		decrypt = String.valueOf(messageArray);

		return decrypt;
	}
}
