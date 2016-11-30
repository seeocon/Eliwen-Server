package com.guardian;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.ArrayList;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.BadLocationException;
import javax.swing.text.StyleContext;

import com.guardian.utils.TableCellListener;
import com.rs.cache.Cache;
import com.rs.cache.loaders.ItemDefinitions;
import com.guardian.ItemManager;

public class PriceLoader {

	public static boolean isActive;
	
	public static boolean isDisabled() {
		return true;
	}
	/**
	 * The created instance
	 */
	private static PriceLoader instance;
	
	/**
	 * returns and creates a new instance, only creates the instance
	 * if it hasn't already been created
	 * @return instance
	 */
	public static PriceLoader getInstance() {
		if (instance == null)
			instance = new PriceLoader();
		return instance;
	}
	
	/**
	 * The main frame
	 */
	private static JFrame frame;
	
	/**
	 * Gets the main frame
	 * @return frame
	 */
	public JFrame getFrame() {
		return frame;
	}
	
	public static void main(String[] args) throws IOException {
		Cache.init();
		ItemManager.inits();
		start();
	}
	
	
	/**
	 * Starts the PriceEditor and sets the default theme
	 */
	public static void start() {
		try {
			JFrame.setDefaultLookAndFeelDecorated(true);
			UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
			frame = new JFrame("Price Editor");
			getInstance().initialize();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private JPanel panel = new JPanel();
	private JButton searchButton = new JButton("Search");
	private JTable table = new JTable();
	private JScrollPane scrollPane;
    static JTextPane outputPanel = new JTextPane();
    
    /**
     * Begins creation of the frame, creates the table
     * and adds everything else needed
     */
	public void initialize() {
		long start, end; start = System.currentTimeMillis();
		frame.setPreferredSize(new Dimension(500, 710));
		frame.setLocation(500, 280);
		panel.setLayout(new FlowLayout());
		frame.setResizable(false);		
		searchField = new JTextField();
		table = new JTable();
		table.setEnabled(true);    
		searchField.setBounds(35, 440, 340, 25);
		searchButton.setBounds(390, 440, 75, 24);
		table.setBounds(35, 440, 340, 400);
		addTableListener();
		addButtonListener();
		addSearchListener();    
		frame.add(searchButton);
		frame.add(searchField);
		styleContext = new StyleContext();
		loadTable();
		JScrollPane scrollPane = new JScrollPane(outputPanel);
		scrollPane.setBounds(15, 490, 465, 175);
		frame.add(scrollPane, BorderLayout.CENTER);
		frame.getContentPane().add(panel);
		frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);
		isActive = true;
		end = System.currentTimeMillis();
		append("[Guardian] Done loading Price Editor. (Execution Time: "+((double)(end - start) / 1000)+" seconds)");
	}
	
	/**
	 * Creates and adds the data to the table
	 */
	public void loadTable() {
		 
		DefaultTableModel model = (DefaultTableModel) table.getModel();

		String[] columns = { "Item Id", "Item Name", "Value" } ;
		for (String column : columns) {
			model.addColumn(column);
		}
		
		table.getColumn("Item Id").setMinWidth(75);
		table.getColumn("Item Id").setMaxWidth(75);
		
		table.getColumn("Value").setMinWidth(110);
		table.getColumn("Value").setMaxWidth(110);
		
		ArrayList<Object[]> list = new ArrayList<Object[]>();
		
		for (int i = 0; i < ItemManager.values.length; i++) {
			try {
				int id = ItemManager.values[i].itemId;
				String name = ItemManager.values[i].itemName;
				int price = ItemManager.values[i].value;
				list.add(new Object[]{""+id+"", ""+name+"", ""+formatNumber(price)+""});
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		for (Object[] o : list) {
			model.addRow(o);
		}
		scrollPane = new JScrollPane(table);
		panel = new JPanel();
		panel.setPreferredSize(new Dimension(490, 470));
		panel.add(scrollPane);
	}
	
	/**
	 * Adds the input listener for string being inputted and 
	 * acts accordingly.
	 */
	public void addSearchListener() {
		searchField.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent e) {
	    		String[] cmd = searchField.getText().split(" ");
	            
	    		if (cmd[0].equals("value")) {
	    			if (cmd.length < 1) {
	    				append("Invalid parameters. Must be 'value minvalue (optional maxvalue)'");
	    				return;
	    			}
	    			int minValue = Integer.parseInt(cmd[1]);
	    			int maxValue = cmd.length > 2 ? Integer.parseInt(cmd[2]) : Integer.MAX_VALUE;
	    			searchValues(minValue, maxValue);
	    			return;
	    		}
	    		
	    		if (isNumeric(cmd[0])) {
	    			searchId(cmd[0]);
	    			return;
	    		}
	    		
	    		if (cmd[0].equals("reset")) {
	    			outputPanel.setText("");
	    			searchField.setText("");
	    			return;
	    		}
				
	    		search(searchField.getText());
	    	}
		});
	}
	
	/**
	 * Listens for changes made to the table and applies them appropriately
	 */
	public void addTableListener() {
		Action action = new AbstractAction() {
			private static final long serialVersionUID = -1558313811715863257L;
			public void actionPerformed(ActionEvent e) {
		        TableCellListener tcl = (TableCellListener)e.getSource();
		        
		        int row = tcl.getRow();
		        int column = tcl.getColumn();
		        
		        if (column != 2) {
		        	append("This data will not save.");
		        	return;
		        }
		        
		        int itemId = Integer.valueOf((String)table.getValueAt(row, 0).toString().replace(",", ""));
		        
		        String name = table.getValueAt(row, 1).toString();
		        
		        int newValue = Integer.valueOf(tcl.getNewValue().toString().replace(",", ""));
		        
		        if (ItemManager.update(itemId, newValue)) {
		        	append("Saved Item: "+name+" (New Value: "+ItemManager.getPrice(itemId)+")");
		        }
		    }
		};
		
		new TableCellListener(table, action);
	}
	
	private JTextField searchField;
	
	/**
	 * Checks to see if the string entered is numbers only
	 * @param string to check
	 * @return 
	 * 		true if the string is numeric
	 */
	public boolean isNumeric(String s) {  
		try  {  
	      @SuppressWarnings("unused")
		double d = Double.parseDouble(s);  
	    } catch(NumberFormatException nfe) {  
	      return false;  
	    }  
	    return true;  
	}  
	
	public void searchValues(int minValue, int maxValue) {
		outputPanel.setText("");
        int found = 0;
        for (int row = 0; row <= table.getRowCount() - 1; row++) {
        	int value = Integer.parseInt(table.getValueAt(row, 2).toString().replace(",", ""));
        	if (value >= minValue && value <= maxValue) {
        		if (found >= 1000) {
      			   append("Found 1000+ Results. Limited to prevent long search times.");
      			   return;
      		   	}
        		append(""+table.getValueAt(row, 0)+" - "+table.getValueAt(row, 1)+" (Value: "+table.getValueAt(row, 2)+")");
        		found++;
        	}
        }
        append("Found a total of "+found+" results.");
        searchField.setText("");
	}
	
	/**
	 * Searches the table for an existing item and prints the results
	 * to the output panel at the bottom of the frame.
	 * @param itemName
	 */
	public void search(String itemName) {
		outputPanel.setText("");
        int found = 0;

        for (int row = 0; row <= table.getRowCount() - 1; row++) {
     	   if (table.getValueAt(row, 1).toString().toLowerCase().contains(itemName.toLowerCase())) {
     		   if (found >= 1000) {
     			   append("Found 1000+ Results. Limited to prevent long search times.");
     			   return;
     		   }
     		   append(""+table.getValueAt(row, 0)+" - "+table.getValueAt(row, 1)+" (Value: "+table.getValueAt(row, 2)+")");
     		   found++;
     	   }
        }
        append("Found a total of "+found+" results for '"+itemName+"'");
        searchField.setText("");
	}
	
	/**
	 * Searches the table for give id number and scrolls to its position
	 * @param value
	 */
	public void searchId(String value) {
		int searchId = Integer.parseInt(value);
        for (int row = 0; row <= table.getRowCount() - 1; row++) {
        	int found = Integer.parseInt(table.getValueAt(row, 0).toString());
        	if (found == searchId) {
        		table.scrollRectToVisible(table.getCellRect(row, 0, true));
        		table.setRowSelectionInterval(row, row);
        	}
        }
        searchField.setText("");
	}
	
	private static StyleContext styleContext;
	
	/**
	 * Prints the data to the output panel at the bottom of the frame
	 * @param message
	 */
	public static void append(final String message) {       
        try {
            outputPanel.getDocument().insertString(outputPanel.getDocument().getLength(), message + "\r\n", styleContext.getStyle("black"));
            outputPanel.setCaretPosition(outputPanel.getDocument().getLength());
        } catch (BadLocationException e) {
           
        }
    }
	
	public String getFullString(String[] cmd) {
		String name = "";
		for (int i = 1; i < cmd.length; i++) {
			name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
		}
		return name;
	}
	/**
	 * Adds the listener to see if the search button is pressed
	 * and acts accordingly
	 */
	public void addButtonListener() {
		searchButton.addActionListener(new ActionListener() { 
	        public void actionPerformed(ActionEvent e) { 
	            search(searchField.getText());
	        } 
	    });
	}

	/**
	 * Automatically inserts commas in the appropriate position of a number
	 * @param num
	 * @return
	 */
	public static String formatNumber(int num) {
		return NumberFormat.getInstance().format(num);
	}

	
}
