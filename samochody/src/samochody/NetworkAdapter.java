package samochody;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import smile.Network;
import smile.SMILEException;

public class NetworkAdapter {
	
	private static final Map<String, Double> CARS = new HashMap<>();
	
	static {
		CARS.put("Audi_5", null);
		CARS.put("Jeep_Renegade", null);
		CARS.put("Nissan_Navara", null);
		CARS.put("Peugeot_508", null);
		CARS.put("Volkswagen_Passat_B6", null);
		CARS.put("Opel_Astra_4", null);
		CARS.put("Citroen_C4", null);
		CARS.put("Skoda_Superb_2", null);
		CARS.put("Mercedes_Benz_C_class", null);
	}
	
	private static final List<String> EVIDENCE_NODES = new LinkedList<String>();
	
	static {
		EVIDENCE_NODES.add("Ciemnosc_koloru"); 
		EVIDENCE_NODES.add("Wielkosc_samochodu"); 
		EVIDENCE_NODES.add("Ilosc_miejsca_na_bagaz"); 
		EVIDENCE_NODES.add("Samochod_rodzinny"); 
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
		for (String carName : CARS.keySet()) {
			double value = getNodeStateValue(carName, "State0");
			CARS.put(carName, value);
		}
		return new HashMap<String, Double>(CARS);
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
				this.network.setEvidence(en, evidenceNodeToChosenValue.get(en));
			} catch(SMILEException ex) {
				System.out.println(String.format("[ERROR][SETTING_EVIDENCE] Node %s not exists", en));
			}
		}
		this.network.updateBeliefs();
	}
	
	private double getNodeStateValue(String nodeName, String stateName) {
		if(this.network == null) {
			return 0.3d;
		}
		String[] stateIds = getNodeStates(nodeName);
		int idx;
		for(idx = 0; idx < stateIds.length; idx++) {
			if(stateName.equals(stateIds[idx])) {
				break;
			}
		}
		return this.network.getNodeValue(nodeName)[idx];
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
