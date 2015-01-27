package samochody;

import java.io.IOException;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;


public class Main {
	
	private static NetworkAdapter netAdapter;
	private static Map<String, String[]> evidences;
	private static final Map<String, String> result = new HashMap<String, String>();
	
	public static void main(String args[]) throws NumberFormatException, IOException{
		netAdapter = new NetworkAdapter("networks/samochody.xdsl");
		evidences = netAdapter.getNodesWithEvidences();
		gatherEvidences();	
	}

	private static void updateNetwork() {
		printSelection();
		netAdapter.setEvidenceNodesAndUpdateNetwork(result);
	}

	private static void presentResults(Shell parent, Display display) {
		Map<String, Double> carToProbability = netAdapter.getCars();
		
		final Shell shell = new Shell(parent);
		shell.setLayout(new GridLayout(2, true));
	    shell.setSize(800, 300);
	    
	    
//	    ValueComparabtor bvc =  new ValueComparator(carToProbability);
//        TreeMap<String,Double> sorted_map = new TreeMap<String,Double>(bvc);


//        System.out.println("unsorted map: "+carToProbability);
//
//        sorted_map.putAll(carToProbability);
	    
	    for (String carName : carToProbability.keySet()) {
	    	Label carLabel = new Label(shell, SWT.FILL);
			carLabel.setText(carName);
			carLabel.setLayoutData(new GridData(SWT.FILL));
			Label probLabel = new Label(shell, SWT.FILL);
			probLabel.setLayoutData(new GridData(SWT.FILL));
			probLabel.setText(carToProbability.get(carName).toString());
		}
		
		
		shell.pack();
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}
	
	class ValueComparator implements Comparator<String> {

	    Map<String, Double> base;
	    public ValueComparator(Map<String, Double> base) {
	        this.base = base;
	    }

	    // Note: this comparator imposes orderings that are inconsistent with equals.    
	    public int compare(String a, String b) {
	        if (base.get(a) >= base.get(b)) {
	            return -1;
	        } else {
	            return 1;
	        } // returning 0 would merge keys
	    }
	}

	private static void gatherEvidences() throws NumberFormatException, IOException {
		final Display display = new Display();
	    final Shell shell = new Shell(display);

	    shell.setLayout(new GridLayout());
	    shell.setSize(500, 200);
	    
	    for (String node : evidences.keySet()) {
			Composite composite = new Composite(shell, SWT.NONE);
			composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
			composite.setLayout(new RowLayout());
			Label text = new Label(composite, SWT.SINGLE | SWT.LEAD | SWT.BORDER);
			text.setText(node);
			text.setLayoutData(new RowData());
			List<Button> radios = new LinkedList<Button>();
			for (String evidence : evidences.get(node)) {
				Button button = new Button(composite, SWT.RADIO);
				button.setText(evidence);
				button.setData(node);
				button.addSelectionListener(new SelectionListener() {
					
					@Override
					public void widgetSelected(SelectionEvent e) {
						Button radio = (Button)e.getSource();
						result.put((String)radio.getData(), radio.getText());
					}

					@Override
					public void widgetDefaultSelected(SelectionEvent e) {
					}
					
				});
				radios.add(button);
			}
		}
	    
		
	    Button compute = new Button(shell, SWT.PUSH);
	    
		compute.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		compute.setText("Compute");
		compute.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				updateNetwork();
				presentResults(shell, display);
				
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
			}
		});
		
	    
	    shell.pack();
	    shell.open();
	    while (!shell.isDisposed()) {
	      if (!display.readAndDispatch()) {
	        display.sleep();
	      }
	    }
	    display.dispose();

	}
	
	
	private static void printSelection() {
		System.out.println("[INFO] Evidences selection:");
		for (String nodeName : result.keySet()) {
			System.out.println(String.format("[INFO] Node: %s\tEvidence: %s", nodeName, result.get(nodeName)));
		}
	}
		
	
}
