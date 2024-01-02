package TrabalhoPratico1;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ContainerListener;
import java.awt.event.InputMethodListener;
import java.util.Scanner;

public class ResourceMonitorGUI {
	private JFrame windowFrame;
	private JTextArea alertsArea;
	private JTextField numConsumersInput;
	private int numConsumers = -1;
	private boolean isRunning = false;

	public ResourceMonitorGUI() {
		windowFrame = new JFrame("Resource Monitor GUI");
		windowFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		windowFrame.setResizable(false);

		Panel pn0 = new Panel();
		pn0.setBounds(0, 0, 600, 50);

		JButton buttonStart = new JButton("Start program");
		buttonStart.setSize(600, 50);
		pn0.add(buttonStart);
		buttonStart.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				addAlert("Starting program");
				if(numConsumersInput.getText()!= null) {
					
					  // Prompt the user for an integer input
			        String userInput =  numConsumersInput.getText();
			        
			        // Validate and parse the input
			        try {
			            int inputNumber = Integer.parseInt(userInput);

			            if (inputNumber >= 0) {
			                // If the input is valid, update numConsumers
			                numConsumers = inputNumber;

			                // Update the GUI with the new number of consumers
			                addAlert("New consumer added: " + numConsumers);

			                // Start a new consumer thread
			                main.numConsumers = getNumConsumers();
			                
							main.isRunning = true;
							addAlert("new consumer added" + isRunning);	
			                
			            } else {
			                // Show an error message for invalid input
			                JOptionPane.showMessageDialog(null, "Please enter a non-negative integer.", "Error", JOptionPane.ERROR_MESSAGE);
			            }
			        } catch (NumberFormatException e1) {
			            // Show an error message for non-integer input
			            JOptionPane.showMessageDialog(null, "Please enter a valid integer.", "Error", JOptionPane.ERROR_MESSAGE);
			        }
			      
				}
				else {
					addAlert("number of consumers is illigal! Try again...");
				}
				
			}
		});

		JButton buttonStop = new JButton("Stop program");
		buttonStop.setSize(600, 50);
		pn0.add(buttonStop);
		buttonStop.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				addAlert("stopping program");
				main.isRunning = false;
				System.exit(0);
			}
		});

		// Added JTextField for user input
		numConsumersInput = new JTextField("0", 5);
		pn0.add(numConsumersInput);

		
		windowFrame.add(pn0);

		alertsArea = new JTextArea();
		alertsArea.setBounds(0, 100, 600, 100);
		alertsArea.setMargin(new Insets(10, 10, 10, 10));
		alertsArea.setLineWrap(true);
		alertsArea.setWrapStyleWord(true);
		alertsArea.setEditable(false);
		JScrollPane scrollPane = new JScrollPane(alertsArea);
		windowFrame.add(scrollPane);

		windowFrame.setSize(600, 600);
		windowFrame.setBackground(Color.WHITE);
		windowFrame.setVisible(true);
	}

	// our threads can all call this method
	public synchronized void addAlert(String alert) {
		if (!alert.endsWith("\n"))
			alert += "\n";
		alertsArea.append(alert);
	}


	public int getNumConsumers() {
		return numConsumers;
	}

	public void setNumConsumers(int numConsumers) {
		this.numConsumers = numConsumers;
	}

	public boolean isRunning() {
		return isRunning;
	}

	public void setRunning(boolean isRunning) {
		this.isRunning = isRunning;
	}

	public static ResourceMonitorGUI newInstance() {
		return new ResourceMonitorGUI();
	}

}
