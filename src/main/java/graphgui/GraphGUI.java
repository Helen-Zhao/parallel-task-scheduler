package graphgui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;

import models.Node;
import models.NodeTuple;



@SuppressWarnings("serial")
public class GraphGUI extends JFrame {

	JPanel mainPanel;
	JPanel promptPanel;
	JPanel chartPanel;
	JPanel statsPanel;
	JScrollPane chartScrollPane;
	JTable chart;
	String[] columnNames;
	String[][] rows;
	ArrayList<Node> optimal;
	int numProcessors;
	DefaultTableModel model;
	int finalEndTime = 0;
	int heuristic = 0;
	JLabel progressLabel;
	JLabel endTimeLabel;
	JLabel optimalLabel;
	

	public GraphGUI(int numProcessors) {
		this.numProcessors = numProcessors;
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setBounds(1400, 300, 350, 500);
        //setLayout(new BorderLayout());
        
        model = new DefaultTableModel() {
        	@Override
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return false;
            }
        };
        model.addColumn("Time");
        for (int i = 0; i < numProcessors; i++) {
        	model.addColumn("Proc " + Integer.toString(i+1));
        }
        model.setRowCount(0);
        
        
        
        mainPanel = new JPanel();
        setContentPane(mainPanel);
        mainPanel.setLayout(new BorderLayout());
        
        promptPanel = new JPanel();
        
        //Prompt Panel Components
        optimalLabel = new JLabel("Optimal Schedule");
        
        promptPanel.add(optimalLabel);
        
        
        chartPanel = new JPanel(new BorderLayout());
        statsPanel = new JPanel();
        
        //Stats Panel components
        endTimeLabel = new JLabel("End Time: " + finalEndTime);
        progressLabel = new JLabel("Progress: " + heuristic + "%");
        
        statsPanel.add(endTimeLabel);
        statsPanel.add(progressLabel);
        
        chart = new JTable(model);
        chart.setShowHorizontalLines(false);
        chartScrollPane = new JScrollPane(chart);
        
        chartPanel.add(chartScrollPane, BorderLayout.CENTER);
        
        
        
        
        mainPanel.add(promptPanel, BorderLayout.NORTH);
        mainPanel.add(chartPanel, BorderLayout.CENTER);
        mainPanel.add(statsPanel, BorderLayout.SOUTH);
        
	}
	
	// Prints optimal schedule to the chart
	public void setOptimalSchedule(List<Node> scheduledNodes, HashMap<String, NodeTuple> optimal) {
		model.setRowCount(0);
		finalEndTime = 0;
		for (Node n : scheduledNodes) {
			int endTime = optimal.get(n.getName()).getStartTime() + n.getWeight();
			if (endTime > finalEndTime) {
				finalEndTime = endTime;
				endTimeLabel.setText("End Time: " + finalEndTime);
			}
		}
		
		// Sets time column
		model.setRowCount(finalEndTime);
		for(int i = 0; i < finalEndTime; i++) {
			model.setValueAt(i+1, i, 0);
		}
		
		for(Node n : scheduledNodes) {
			int processor;
			processor = optimal.get(n.getName()).getProcessor();
			int startTime = optimal.get(n.getName()).getStartTime();
			int endTime = startTime + n.getWeight();
			String nodeName = n.getName();
			
			for (int j = startTime; j < endTime; j++) {
				model.setValueAt(nodeName, j, processor);
			}
		}
	}
	
	public void orderSchedule(List<Node> nodeList, HashMap<String, NodeTuple> optimal) {
		HashMap<String, Node> matchedNodes = new HashMap<String, Node>();
		ArrayList<Node> orderArray = new ArrayList<Node>();
		Node earliestNode = nodeList.get(0);
		
		while (true) {
			if (orderArray.size() == nodeList.size()) {
				break;
			}
			int earliestStartTime = Integer.MAX_VALUE;
			for (int i = 0; i < nodeList.size(); i++) {
				
				String nodeTupleKey = nodeList.get(i).getName();
				NodeTuple tuple = optimal.get(nodeTupleKey);
				int tupleStartTime = tuple.getStartTime();
				if (tupleStartTime <= earliestStartTime) {
					if (!matchedNodes.containsKey(nodeList.get(i).getName())) {
						earliestStartTime = tupleStartTime;
						earliestNode = nodeList.get(i);
					}
				}	
			}
			orderArray.add(earliestNode);
			matchedNodes.put(earliestNode.getName(), earliestNode);
			
		}
		
		setOptimalSchedule(orderArray, optimal);
	}
	
	public void calculateHeuristic(int currentBound, int bestBound, int heuristicBound) {
		int max = Math.max(currentBound,  heuristicBound);
		heuristic = (max * 100)/(bestBound);
		progressLabel.setText("Progress: " + heuristic + "%");
	}
	
	public void printOptimalLabel(int optimalFinal) {
		if (optimalFinal == 0) {
			optimalLabel.setText("Final Optimal Schedule");
		}
		else if (optimalFinal == 1) {
			optimalLabel.setText("New Optimal Schedule Found");
		}
		else {
			optimalLabel.setText("Calculating Next Schedule...");
		}
		
	}


}
