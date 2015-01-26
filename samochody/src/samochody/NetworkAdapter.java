package samochody;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import smile.Network;
import smile.SMILEException;

public class NetworkAdapter {
	
	private static final List<String> EVIDENCE_NODES = new LinkedList<String>();
	
	static {
		EVIDENCE_NODES.add("Ciemnosc_koloru"); 
		EVIDENCE_NODES.add("Wielkosc_samochodu"); 
		EVIDENCE_NODES.add("Ilosc_miejsca_na_bagaz"); 
		EVIDENCE_NODES.add("Samochod_rodzinny_2"); 
		EVIDENCE_NODES.add("Wyprodukowany_w_europie"); 
		EVIDENCE_NODES.add("Wysokosc_ceny"); 
		EVIDENCE_NODES.add("Pojemnosc"); 
	}
	
	private Network network = null;
	
	public NetworkAdapter(String filename) {
		if(!filename.equals("TEST")) {
			this.network = new Network();
			this.network.readFile(filename);
		}
	}
	
	public Map<String, Double> getCars() {
		String[] carsNames = this.network.getOutcomeIds("Model_samochodu");
		double[] carsProbabilities = this.network.getNodeValue("Model_samochodu");
		Map<String, Double> carsWithProbs = new HashMap<String, Double>();
		for(int i = 0; i < carsNames.length; ++i) {
			carsWithProbs.put(carsNames[i], carsProbabilities[i]);
		}
		return carsWithProbs;
	}
	
	public Map<String, String[]> getNodesWithEvidences() {
		Map<String, String[]> evidenceNodeToValues = new HashMap<String, String[]>();
		for (String en : EVIDENCE_NODES) {
			evidenceNodeToValues.put(en, getNodeStates(en));
		}
		return evidenceNodeToValues;
	}
	
	public void setEvidenceNodesAndUpdateNetwork(Map<String, String> evidenceNodeToChosenValue) {
		
		for (String en : evidenceNodeToChosenValue.keySet()) {
			try {
				String evidence = evidenceNodeToChosenValue.get(en);
				System.out.println(String.format("[INFO] Setting evidence %s on node %s", evidence, en));
				this.network.setEvidence(en, evidence);
			} catch(SMILEException ex) {
				System.out.println(String.format("[ERROR][SETTING_EVIDENCE] Node %s. Ex: %s", en, ex.getMessage()));
			}
		}
		this.network.updateBeliefs();
	}

	private String[] getNodeStates(String nodeName) {
		try {
			return this.network.getOutcomeIds(nodeName);
		} catch(SMILEException ex) {
			System.out.println(String.format("[ERROR][GETTING_STATES] Node %s not exists", nodeName));
			return new String[]{"unknown state1", "unknown state2"};
		}
	}
	
}
