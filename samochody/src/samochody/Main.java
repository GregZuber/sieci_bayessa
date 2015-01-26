package samochody;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;


public class Main {
	
	private static NetworkAdapter netAdapter;
	private static Map<String, String[]> evidences;
	
	public static void main(String args[]) throws NumberFormatException, IOException{
		netAdapter = new NetworkAdapter("networks/samochody.xdsl");
		evidences = netAdapter.getNodesWithEvidences();
		while(true) {
			Map<String, String> evidenceNodeToValue = gatherEvidences();
			updateNetwork(evidenceNodeToValue);
			presentResults();
			if(!repeat()) {
				break;
			}	
		}	
	}

	private static void updateNetwork(Map<String, String> evidenceNodeToValue) {
		netAdapter.setEvidenceNodesAndUpdateNetwork(evidenceNodeToValue);
	}

	private static boolean repeat() {
		return false;
	}

	private static void presentResults() {
		Map<String, Double> carToProbability = netAdapter.getCars();
		for (String carName : carToProbability.keySet()) {
			String output = String.format("Car: %s\t Probability: %f", carName, carToProbability.get(carName));
			System.out.println(output);
		}
	}

	private static Map<String, String> gatherEvidences() throws NumberFormatException, IOException {
		Map<String, String> result = new HashMap<String, String>();
		for (String node : evidences.keySet()) {
			System.out.println(node);
			int idx = 0;
			for (String evidence : evidences.get(node)) {
				System.out.println(String.format("[%d] %s", idx, evidence));
				++idx;
			}
			System.out.print("Your choice: ");
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			int choice = Integer.parseInt(br.readLine());
			System.out.println("Your choise is: " + choice);
			result.put(node, evidences.get(node)[choice]);
		}
		return result;
	}
	
}
