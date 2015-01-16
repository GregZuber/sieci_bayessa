package tutorial;

import java.awt.Color;

import smile.Network;

public class CreateBayessianNetwork1 {
	public static void main(String args[]) {
		System.loadLibrary("jsmile"); 
		Network net = new Network();
		net.addNode(Network.NodeType.Cpt, "Success");
		net.setOutcomeId("Success", 0, "Success");
		net.setOutcomeId("Success", 1, "Failure");

		net.addNode(Network.NodeType.Cpt, "Forecast");
		net.addOutcome("Forecast", "Good");
		net.addOutcome("Forecast", "Moderate");
		net.addOutcome("Forecast", "Poor");
		net.deleteOutcome("Forecast", 0);
		net.deleteOutcome("Forecast", 0);

		net.addArc("Success", "Forecast");

		double[] aSuccessDef = { 0.2, 0.8 };
		net.setNodeDefinition("Success", aSuccessDef);

		double[] aForecastDef = { 0.4, 0.4, 0.2, 0.1, 0.3, 0.6 };
		net.setNodeDefinition("Forecast", aForecastDef);

		// Changing the nodes' spacial and visual attributes:
		net.setNodePosition("Success", 20, 20, 80, 30);
		net.setNodeBgColor("Success", Color.red);
		net.setNodeTextColor("Success", Color.white);
		net.setNodeBorderColor("Success", Color.black);
		net.setNodeBorderWidth("Success", 2);
		net.setNodePosition("Forecast", 30, 100, 60, 30);

		net.writeFile("tutorial_a.xdsl");

	}
}
