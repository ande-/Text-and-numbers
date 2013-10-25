package machineProject;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

public class MP5GUI extends JFrame {
	
	private static final long serialVersionUID = 1L;
	JPanel contentPane;
	final JTextArea inputArea = new JTextArea(6, 40);
	JComboBox comboBox;
	JPanel lowerPanel;
	JLabel label = new JLabel();
	JScrollPane scrollPane;
	final JTextArea outputArea = new JTextArea(6, 40);
	JButton button;
	Keypad pad;

	/**
	 * Constructor supplies the name, creates a keypad,
	 * and calls the make frame method
	 */
	public MP5GUI() {
		super("GUI");
		pad = new Keypad();
		makeFrame();
	}
	
	/**
	 * makeFrame creates a way to exit and sets up the initial
	 * frame before any direction is selected
	 */
	public void makeFrame() {
		//allow for exit
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});

		//set up frame
		contentPane = (JPanel) getContentPane();
		contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));
		contentPane.add(inputArea);
		
		JPanel directionBar = new JPanel();
		directionBar.setLayout(new FlowLayout());
		JLabel direction = new JLabel("Direction:");
		directionBar.add(direction);
		contentPane.add(directionBar);

		String[] options = {"No Action", "L2N", "N2L", "N2W"}; 
		comboBox = new JComboBox(options);
		directionBar.add(comboBox);
		comboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				sort(e);
			}
		});
		pack();
	}
	
	/**
	 * When a selection is made from the comboBox, a new face will be created.
	 * @param e the action event
	 */
	public void sort(ActionEvent e) {
		//get rid of old lower panel and set up a new one
		if(lowerPanel != null) {
			contentPane.remove(lowerPanel);
		}
		lowerPanel = new JPanel();
		lowerPanel.setLayout(new BoxLayout(lowerPanel, BoxLayout.Y_AXIS));
		outputArea.setText("");
		
		//customize the lower panel based on the combo box command
		comboBox = (JComboBox) e.getSource();
        String selection = (String) comboBox.getSelectedItem();
        if(selection.equals("L2N")) {
            createL2Nface();
        }
        else if(selection.equals("No Action")) {
        	pack();
        }
        else if(selection.equals("N2L")) {
        	createN2Lface();
        }
        else if(selection.equals("N2W")) {
        	createN2Wface();
        }
	}
	
	/**
	 * If L2N is selected from the combo box, this builds the bottom of the interface.
	 */
	public void createL2Nface() {
		JPanel flowPanel = new JPanel();
		flowPanel.setLayout(new FlowLayout());
		
		label.setText("Letters -> Numbers:");
		flowPanel.add(label);
		
		final JTextField outputField = new JTextField(20);
		flowPanel.add(outputField);
		lowerPanel.add(flowPanel);
		
		button = new JButton("L -> N");
		//add action listener to the button
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String output = pad.letters2numbers(inputArea.getText());
				outputField.setText(output);
				repaint();			
			}
		});
		lowerPanel.add(button);
		
		contentPane.add(lowerPanel);
		pack();
	}
	
	/**
	 * If N2L is selected from the combo box, this builds the bottom of the interface.
	 */
	public void createN2Lface() {
		label.setText("Numbers -> Letters:");
		lowerPanel.add(label);
		label.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		scrollPane = new JScrollPane(outputArea);
		lowerPanel.add(scrollPane);
		
		button = new JButton("N -> L");
		//add action listener to the button
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String[] letterGroups = pad.numbers2letters(inputArea.getText());
				String output = "";
				for(int group = 0; group < letterGroups.length; group++) {						
					output += letterGroups[group] + "\n";
				}
				outputArea.setText(output);
				repaint();
			}
			});
		lowerPanel.add(button);
		button.setAlignmentX(Component.CENTER_ALIGNMENT);
		lowerPanel.add(Box.createVerticalStrut(5));
		
		contentPane.add(lowerPanel);
		pack();
	}
	
	/**
	 * If L2W is selected from the combo box, this builds the bottom of the interface.
	 */
	public void createN2Wface() {
		label.setText("Numbers -> Words:");
		lowerPanel.add(label);
		label.setAlignmentX(Component.CENTER_ALIGNMENT);
		JPanel measurements = new JPanel();
		lowerPanel.add(measurements);
		
		//all the stuff for MP5
		measurements.setLayout(new BorderLayout());
		JPanel comparisons = new JPanel();
		comparisons.setLayout(new FlowLayout());
		JPanel timings = new JPanel();
		timings.setLayout(new FlowLayout());
		measurements.add(comparisons, BorderLayout.NORTH);
		measurements.add(timings, BorderLayout.SOUTH);
		timings.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		JLabel lastComparisonLabel = new JLabel("Last Comparison: ");
		final JTextField lastComparisonField = new JTextField("0", 5);
		JLabel averageComparisonsLabel = new JLabel("Average Comparisons: ");
		final JTextField averageComparisonsField = new JTextField("0.0", 10);
		JLabel lastTimingLabel = new JLabel("Last Timing: ");
		final JTextField lastTimingField = new JTextField("0", 5);
		JLabel averageTimingsLabel = new JLabel("Average Timings: ");
		final JTextField averageTimingsField = new JTextField("0.0", 10);
		
		comparisons.add(lastComparisonLabel);
		comparisons.add(lastComparisonField);
		comparisons.add(averageComparisonsLabel);
		comparisons.add(averageComparisonsField);
		timings.add(lastTimingLabel);
		timings.add(lastTimingField);
		timings.add(averageTimingsLabel);
		timings.add(averageTimingsField);
		
		scrollPane = new JScrollPane(outputArea);
		lowerPanel.add(scrollPane);	
		button = new JButton("N -> W");
		//add action listener to the button
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String[] wordGroups = pad.numbers2words(inputArea.getText());
				String output="";
				for(int group = 0; group <wordGroups.length; group++) {
					output += wordGroups[group] + "\n";
				}
				outputArea.setText(output);
				//measurement stuff.  This order is important since getAverageTiming will clear the counts 
				//of lookup calls and we don't want to late divide by 0 when trying to calculate an average
				lastComparisonField.setText(""+pad.getLastComparisons());
				averageComparisonsField.setText(""+pad.getAverageComparisons());
				lastTimingField.setText(""+pad.getLastTiming());
				averageTimingsField.setText(""+pad.getAverageTiming());
				repaint();
			}
			});
		lowerPanel.add(button);
		button.setAlignmentX(Component.CENTER_ALIGNMENT);
		lowerPanel.add(Box.createVerticalStrut(5));
		
		contentPane.add(lowerPanel);
		pack();
	}
}
