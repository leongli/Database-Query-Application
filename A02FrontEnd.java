import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;
//import java.text.*;

public class A02FrontEnd extends JFrame implements ItemListener, ActionListener{

	/**
	 * The purpose of this class is to create the FrontEnd/GUI and provide listener functions.
	 * You cannot write/perform any database interaction functions/actions in this class.
	 * You can only invoke a suitable function from A02MiddleTier class on the click event of Submit button. 
	 */
	JCheckBox eventConference;
    JCheckBox eventJournal;
    JCheckBox eventBook;
    JRadioButton allDates;
    JRadioButton dateRange;
    ButtonGroup dateSelection;
    JTextArea queryOutput;
    JLabel fromLabel;
    JLabel toLabel;
    JTextField fromDate;
    JTextField toDate;
    JButton submitQuery;
	A02MiddleTier app;
    
	public A02FrontEnd() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {

		this.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		
		eventConference = new JCheckBox("EventConference");
		eventConference.setSelected(false);
		eventJournal = new JCheckBox("EventJournal");
		eventJournal.setSelected(false);
		eventBook = new JCheckBox("EventBook");
		eventBook.setSelected(false);        
		eventConference.addItemListener(this);
		eventJournal.addItemListener(this);
		eventBook.addItemListener(this);
		JPanel eventPanel = new JPanel(new GridLayout(0, 1));
		eventPanel.add(eventConference);
		eventPanel.add(eventJournal);
		eventPanel.add(eventBook);
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 0;
        this.add(eventPanel, c);

        JLabel emptyLabel1 = new JLabel("                    \n          ");
        JPanel emptyPanel1 = new JPanel(new GridLayout(0, 1));
        emptyPanel1.add(emptyLabel1);
        c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 1;
		c.gridy = 0;
        this.add(emptyPanel1, c);

        allDates = new JRadioButton("All Events");
        allDates.setSelected(true);
        dateRange = new JRadioButton("Period");
        dateRange.setSelected(false);        
        dateSelection = new ButtonGroup();
        allDates.addItemListener(this);
        dateRange.addItemListener(this);
        dateSelection.add(allDates);
        dateSelection.add(dateRange);        
		JPanel datePanel = new JPanel(new GridLayout(0, 1));
		datePanel.add(allDates);
		datePanel.add(dateRange);

        fromLabel = new JLabel("       From ");
        fromLabel.setEnabled(false);
        toLabel = new JLabel("       To ");
        toLabel.setEnabled(false);
        fromDate = new JTextField();
        fromDate.setEnabled(false);
        toDate = new JTextField();
        toDate.setEnabled(false);
        JPanel dateRangePanel = new JPanel(new GridLayout(0, 4));
		dateRangePanel.add(fromLabel);
		dateRangePanel.add(fromDate);
		dateRangePanel.add(toLabel);
		dateRangePanel.add(toDate);
		datePanel.add(dateRangePanel);
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 2;
		c.gridy = 0;
        this.add(datePanel, c);
        
        JLabel emptyLabel2 = new JLabel("                    \n          ");
        JPanel emptyPanel2 = new JPanel(new GridLayout(0, 1));
        emptyPanel2.add(emptyLabel2);
        c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 1;
		c.gridy = 1;
        this.add(emptyPanel2, c);

        submitQuery = new JButton("Submit");
        submitQuery.addActionListener(this);
		JPanel submitPanel = new JPanel(new GridLayout(0, 1));
		submitPanel.add(submitQuery);
		c.fill = GridBagConstraints.NONE;
		c.anchor = GridBagConstraints.LINE_START;
		c.gridx = 2;
		c.gridy = 2;
        this.add(submitPanel, c);

        JLabel emptyLabel3 = new JLabel("                    \n          ");
        JPanel emptyPanel3 = new JPanel(new GridLayout(0, 1));
        emptyPanel3.add(emptyLabel3);
        c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 1;
		c.gridy = 3;
        this.add(emptyPanel3, c);

        queryOutput = new JTextArea(18,50);
        queryOutput.setText("The\nQuery\nOutput\nwill\nAppear\nhere.");
		JScrollPane outputPanel = new JScrollPane(queryOutput);
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.fill = GridBagConstraints.BOTH;
		c.gridx = 0;
		c.gridy = 4;
        this.add(outputPanel, c);
        

		this.setBounds(50, 50, 600, 500);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	//this function is the listener for three check boxes and two radio buttons
	public void itemStateChanged(ItemEvent e) {

	    Object source = e.getItemSelectable();

	    if (source == eventConference) {
	    	app.SelectBuilder("eventConference", eventConference.isSelected());

	    } else if (source == eventJournal) {
	    	app.SelectBuilder("eventJournal", eventJournal.isSelected());

	    } else if (source == eventBook) {
	    	app.SelectBuilder("eventBook", eventBook.isSelected());

	    } else if (source == dateRange) {
	    	if (dateRange.isSelected()) {
	            fromLabel.setEnabled(true);
	            toLabel.setEnabled(true);
	            fromDate.setEnabled(true);
	            toDate.setEnabled(true);	
				app.setIsAll(false);
	    	}
	    	else {
	            fromLabel.setEnabled(false);
	            toLabel.setEnabled(false);
	            fromDate.setEnabled(false);
	            toDate.setEnabled(false);	
				app.setIsAll(true);    			    		
	    	}
	    	
	    } else if (source == allDates) {
			app.setIsAll(allDates.isSelected());
			app.setTo(null);
			app.setFrom(null);
		}
	    
	}
	public void launchApp(A02MiddleTier app0){
		this.app=app0;
	}
	
    /** Listens to the submit button click */
    public void actionPerformed(ActionEvent e) {
		
    	try {
			if (dateRange.isSelected()){
				app.setFrom(fromDate.getText());
				app.setTo(toDate.getText()); 
			}
			String output=app.output();
			System.out.println(output);
			queryOutput.setText("Query: \n"+output+"\nOutput: \n"+app.call());
			
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
    }

}
