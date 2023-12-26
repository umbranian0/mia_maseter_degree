package TrabalhoPratico1;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ResourceMonitorGUI {
	private JFrame windowFrame;
	private JTextArea alertsArea;

	public ResourceMonitorGUI() {
		windowFrame = new JFrame("Resource Monitor GUI");
		windowFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		windowFrame.setResizable(false);
		
		Panel pnl = new Panel();
		pnl.setBounds(0, 0, 500, 50);
		windowFrame.add(pnl);
		
		JButton buttonStart = new JButton("Start program");
		buttonStart.setSize(500, 100);
		pnl.add(buttonStart);

		buttonStart.addActionListener((ActionListener) new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				addAlert("starting program");
				Main.main(null);
			}
		});

		JButton buttonStop = new JButton("Stop program");
		buttonStop.setSize(500, 100);
		pnl.add(buttonStop);
		buttonStop.addActionListener((ActionListener) new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				addAlert("stopping program");
				Main.stopProgram();
				
			}
		});

		alertsArea = new JTextArea();
		alertsArea.setBounds(0, 0, 500, 500);
		alertsArea.setMargin(new Insets(10, 10, 10, 10));
		alertsArea.setLineWrap(true);
		alertsArea.setWrapStyleWord(true);
		alertsArea.setEditable(false);
		JScrollPane scrollPane = new JScrollPane(alertsArea);
		windowFrame.add(scrollPane);
		windowFrame.setSize(500, 500);
		windowFrame.setBackground(Color.WHITE);
		windowFrame.setVisible(true);

	}

	// our threads can all call this method
	public synchronized void addAlert(String alert) {
		if (!alert.endsWith("\n"))
			alert += "\n";
		alertsArea.append(alert);
	}

	public static ResourceMonitorGUI newInstance() {
		return new ResourceMonitorGUI();
	}

}
