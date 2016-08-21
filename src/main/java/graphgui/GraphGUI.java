package graphgui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;

import models.Node;



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
	

	public GraphGUI(int numProcessors) {
		this.numProcessors = numProcessors;
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setBounds(1400, 300, 350, 500);
        setLayout(new BorderLayout());
        
        model = new DefaultTableModel() {
        	@Override
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return false;
            }
        };
        model.addColumn("Time");
        for (int i = 0; i < numProcessors; i++) {
        	model.addColumn("Processor " + Integer.toString(i+1));
        }
        model.setRowCount(0);
        
        
        
        mainPanel = new JPanel();
        setContentPane(mainPanel);
        mainPanel.setLayout(new BorderLayout());
        
        promptPanel = new JPanel();
        
        
        chartPanel = new JPanel(new BorderLayout());
        statsPanel = new JPanel();
        chart = new JTable(model);
        chart.setShowHorizontalLines(false);
        chartScrollPane = new JScrollPane(chart);
        
        chartPanel.add(chartScrollPane, BorderLayout.CENTER);
        
        
        
        
        mainPanel.add(promptPanel, BorderLayout.NORTH);
        mainPanel.add(chartPanel, BorderLayout.CENTER);
        mainPanel.add(statsPanel, BorderLayout.SOUTH);
        
	}
	
	public void setOptimalSchedule(List<Node> optimal) {
		model.setRowCount(0);
		int finalEndTime = 0;
		for (Node n : optimal) {
			int endTime = n.getStartTime() + n.getWeight();
			if (endTime > finalEndTime) {
				finalEndTime = endTime;
			}
		}
		
		model.setRowCount(finalEndTime);
		for(int i = 0; i < finalEndTime; i++) {
			model.setValueAt(i+1, i, 0);
		}
		
		for(Node n : optimal) {
			int processor;
			processor = n.getProcessor();
			int startTime = n.getStartTime();
			int endTime = startTime + n.getWeight();
			String nodeName = n.getName();
			
			for (int j = startTime; j < endTime; j++) {
				model.setValueAt(nodeName, j, processor);
			}
		}
		
		chartPanel.repaint();
	}


}
