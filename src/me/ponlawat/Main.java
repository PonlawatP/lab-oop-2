package me.ponlawat;


public class Main
{
    public static void main(String[] args) {
    	int amount = 10;
    	for(String a : args) {
    		if(a.contains("amout=")) {
    			try {
    				amount = Integer.parseInt(a.split("=")[1]);
    				System.out.println("set meteor amount: " + amount);
    			} catch(Exception e) {
    				System.out.println("cannot set meteor amount... set default (5)");
    			}
    		}
    	}
        new Frame(amount);
    }
}