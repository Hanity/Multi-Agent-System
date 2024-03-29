

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import jade.gui.GuiEvent;
import java.awt.SystemColor;
import java.awt.Font;

public class ArcGUI extends JFrame {

	private ArcCoordinatorAgent ArcCoordinatorAgent;

	String title = "";
	String messageType = "start";

	JTextField serverName;
	JTextField serverPort;
	JTextField tickerDuration;
	JTextField numberAgents;
	JTextArea messageViewer;
	JScrollPane scrollPane;

	JFrame mainFrame;

	JLabel headerLabel;
	JLabel statusLabel;

	JPanel controlPanel;

	JLabel serverLabel, portLabel, tickerLabel, agentNumberLabel;

	ArrayList<String> receiversList;

	JButton startAttackBtn;

	public ArcGUI(ArcCoordinatorAgent c) {

		super(c.getLocalName());

		
		ArcCoordinatorAgent = c;

		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				ArcCoordinatorAgent.doDelete();
			}
		});

		receiversList = new ArrayList<String>();

		// Remove this agents name from the drop-down list of recipients
		// No point of sending message to thyself :P

		for (String agentName : ArcCoordinatorAgent.agentList) {
			if (ArcCoordinatorAgent.getLocalName().equals(agentName) || receiversList.contains(agentName))
				continue;
			receiversList.add(agentName);
		}

		updateReceiverCombo();

		// all the GUI is instantiated here, so that it can be passed
		// as a parameter to the Agent class grid layout is used
		serverName = new JTextField();
		serverName.setPreferredSize(new Dimension(400, 30));

		tickerDuration = new JTextField();
		tickerDuration.setPreferredSize(new Dimension(400, 30));

		messageViewer = new JTextArea(13, 34);
		messageViewer.setBackground(new Color(224, 255, 255));
		messageViewer.setWrapStyleWord(true);
		messageViewer.setLineWrap(true);
		messageViewer.setEditable(false);
		scrollPane = new JScrollPane();
		scrollPane.setBorder(BorderFactory.createTitledBorder("Status Messages"));
		scrollPane.setViewportView(messageViewer);

		serverLabel = new JLabel("SERVER NAME (IP or Domain): ");
		serverLabel.setFont(new Font("Times New Roman", Font.ITALIC, 12));
		serverLabel.setForeground(Color.BLUE);
		serverLabel.setPreferredSize(new Dimension(400, 20));
		tickerLabel = new JLabel("DURATION BETWEEN ATTACKS: ");
		tickerLabel.setFont(new Font("Times New Roman", Font.ITALIC, 12));
		tickerLabel.setForeground(Color.BLUE);
		tickerLabel.setPreferredSize(new Dimension(400, 20));

		startAttackBtn = new JButton("START ATTACK");
		startAttackBtn.setFont(new Font("Times New Roman", Font.ITALIC, 12));
		startAttackBtn.setBackground(new Color(250, 128, 114));
		startAttackBtn.setPreferredSize(new Dimension(200, 50));

		startAttackBtn.addActionListener(new ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent ae) {
				try {
					String name = serverName.getText().trim();
					String port = serverPort.getText().trim();
					String duration = tickerDuration.getText().trim();
					String number = numberAgents.getText().trim();

					ArcCoordinatorAgent.getFromGui(messageType, name, port, duration, number);

					GuiEvent guiEvent = new GuiEvent(this, 1);
					ArcCoordinatorAgent.postGuiEvent(guiEvent); // this postGuiEvent triggers onGuiEvent method in
																// MessageAgent which in turn calls the sendMessage

					if (messageType.equals("start")) {
						startAttackBtn.setBackground(Color.red);
						startAttackBtn.setText("Stop Attack");
						messageType = "stop";
					} else {
						startAttackBtn.setText("Start Attack");
						startAttackBtn.setBackground(Color.green);
						messageType = "start";
					}

				} catch (Exception e) {
					JOptionPane.showMessageDialog(ArcGUI.this, "Invalid values. " + e.getMessage(), "Error",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		});

		headerLabel = new JLabel("", JLabel.CENTER);
		statusLabel = new JLabel("", JLabel.CENTER);

		controlPanel = new JPanel();
		portLabel = new JLabel("PORT N%: ");
		portLabel.setFont(new Font("Times New Roman", Font.ITALIC, 12));
		portLabel.setForeground(Color.BLUE);
		portLabel.setBackground(new Color(0, 255, 0));
		portLabel.setPreferredSize(new Dimension(400, 20));
		controlPanel.add(portLabel);
		
				serverPort = new JTextField();
				serverPort.setPreferredSize(new Dimension(400, 30));
				controlPanel.add(serverPort);
		controlPanel.add(serverLabel);
		controlPanel.add(serverName);
		agentNumberLabel = new JLabel("N% OF AGENTS DEPLOYED");
		agentNumberLabel.setFont(new Font("Times New Roman", Font.ITALIC, 12));
		agentNumberLabel.setForeground(Color.BLUE);
		agentNumberLabel.setPreferredSize(new Dimension(400, 20));
		controlPanel.add(agentNumberLabel);
		
				numberAgents = new JTextField();
				numberAgents.setPreferredSize(new Dimension(400, 30));
				controlPanel.add(numberAgents);
		controlPanel.add(tickerLabel);
		controlPanel.add(tickerDuration);
		controlPanel.add(startAttackBtn);
		controlPanel.add(scrollPane);

		Container contentPane = getContentPane();
		contentPane.setPreferredSize(new Dimension(420, 600));
		getContentPane().add(controlPanel, BorderLayout.CENTER);

		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				ArcCoordinatorAgent.doDelete();
			}
		});
	}

	public void setMessageTextArea(String text) {
		messageViewer.setText(text);
	}

	public void displayGUI() {
		pack();
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int centerX = (int) screenSize.getWidth() / 2;
		int centerY = (int) screenSize.getHeight() / 2;
		setLocation(centerX - getWidth() / 2, centerY - getHeight() / 2);
		setResizable(false);
		super.setVisible(true);
	}

	public void updateReceiverCombo() {
	}

}
