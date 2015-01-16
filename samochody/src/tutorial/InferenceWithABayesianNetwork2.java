package tutorial;

import smile.Network;

public class InferenceWithABayesianNetwork2 {
	public static void main(String[] args) {
		Network net = new Network();
		net.readFile("tutorial_a.xdsl");
		net.updateBeliefs();

		String[] aForecastOutcomeIds = net.getOutcomeIds("Forecast");
		int outcomeIndex;
		for (outcomeIndex = 0; outcomeIndex < aForecastOutcomeIds.length; outcomeIndex++)
			if ("Moderate".equals(aForecastOutcomeIds[outcomeIndex]))
				break;

		double[] aValues = net.getNodeValue("Forecast");
		double P_ForecastIsModerate = aValues[outcomeIndex];
		System.out.println("P(\"Forecast\" = Moderate) = "
				+ P_ForecastIsModerate);
		net.setEvidence("Forecast", "Good");

		net.updateBeliefs();

		// Getting the index of the "Failure" outcome:
		String[] aSuccessOutcomeIds = net.getOutcomeIds("Success");
		for (outcomeIndex = 0; outcomeIndex < aSuccessOutcomeIds.length; outcomeIndex++)
			if ("Failure".equals(aSuccessOutcomeIds[outcomeIndex]))
				break;

		// Getting the value of the probability:
		aValues = net.getNodeValue("Success");
		double P_SuccIsFailGivenForeIsGood = aValues[outcomeIndex];
		System.out.println("P(\"Success\" = Failure | \"Forecast\" = Good) = "
				+ P_SuccIsFailGivenForeIsGood);

		net.clearEvidence("Forecast");
		net.setEvidence("Forecast", "Poor");
		net.updateBeliefs();

		// Getting the index of the "Failure" outcome:
		aSuccessOutcomeIds = net.getOutcomeIds("Success");
		for (outcomeIndex = 0; outcomeIndex < aSuccessOutcomeIds.length; outcomeIndex++)
			if ("Success".equals(aSuccessOutcomeIds[outcomeIndex]))
				break;

		// Getting the value of the probability:
		aValues = net.getNodeValue("Success");
		double P_SuccIsSuccGivenForeIsPoor = aValues[outcomeIndex];

		System.out.println("P(\"Success\" = Success | \"Forecast\" = Poor) = "
				+ P_SuccIsSuccGivenForeIsPoor);
	}
}
