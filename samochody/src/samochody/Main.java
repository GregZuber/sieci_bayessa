package samochody;

import smile.Network;

public class Main {
	public static void main(String args[]){
		Network net = new Network();
		net.readFile("networks/samochody.xdsl");
	}
	
}
