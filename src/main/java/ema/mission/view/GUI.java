package ema.mission.view;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.DefaultListModel;
import javax.swing.DefaultListSelectionModel;
import javax.swing.JButton;
import ema.mission.controller.GuiControleur;

import javax.swing.JList;
import java.awt.GridLayout;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;
import java.awt.Color;
import javax.swing.border.LineBorder;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Font;

public class GUI {
	private GuiControleur guiControleur;

	private JFrame frame;
	private JTextField sujet;
	private JTextField valeur;
	private JTextField page;
	private JList<String> results;
	private DefaultListModel<String> listModel;
	private JScrollPane showResults;
	

	/**
	 * Create the application.
	 * @param guiControleur 
	 * @param string 
	 */
	public GUI(String name, GuiControleur guiControleur) {
		this.guiControleur = guiControleur;
		frame = new JFrame(name);
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	public void initialize() {	
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setBounds(100, 100, 800, 600);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setBounds(180, 10, 600, 30);
		frame.getContentPane().add(panel);
		panel.setLayout(new GridLayout(1, 5, 0, 0));
		
		JLabel lblNewLabel = new JLabel("Sujet");
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		panel.add(lblNewLabel);
		
		sujet = new JTextField();
		sujet.setFont(new Font("Dialog", Font.PLAIN, 10));
		sujet.setEditable(false);
		panel.add(sujet);
		sujet.setColumns(30);
		
		JLabel lblNewLabel_1 = new JLabel("Valeur");
		lblNewLabel_1.setHorizontalAlignment(SwingConstants.CENTER);
		panel.add(lblNewLabel_1);
		
		valeur = new JTextField();
		valeur.setFont(new Font("Dialog", Font.PLAIN, 10));
		valeur.setEditable(false);
		panel.add(valeur);
		valeur.setColumns(30);
		
		JLabel lblPage = new JLabel("Page");
		lblPage.setHorizontalAlignment(SwingConstants.CENTER);
		panel.add(lblPage);
		
		page = new JTextField();
		page.setFont(new Font("Dialog", Font.PLAIN, 10));
		page.setEditable(false);
		panel.add(page);
		page.setText("1");
		page.setColumns(3);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBounds(0, 40, 800, 35);
		frame.getContentPane().add(panel_1);
		
		JButton btnNewButton = new JButton(guiControleur.getPAIRESUIVANT());
		btnNewButton.addActionListener(guiControleur);
		panel_1.add(btnNewButton);
		
//		JCheckBox checkBox = new JCheckBox("New check boxNew check boxNew check boxNew check boxNew check boxNew check boxNew check boxNew check boxNew check boxNew check boxNew check boxNew check boxNew check boxNew check box");
//		checkBox.setBounds(0, 0, 114, 25);
//		frame.getContentPane().add(checkBox);
		
		JPanel panel_3 = new JPanel();
		panel_3.setBounds(0, 530, 800, 35);
		frame.getContentPane().add(panel_3);
		
		JButton btnNewButton_1 = new JButton(guiControleur.getACCEPTER());
		btnNewButton_1.addActionListener(guiControleur);
		panel_3.add(btnNewButton_1);
		
		JButton btnRefuser = new JButton(guiControleur.getREFUSER());
		btnRefuser.addActionListener(guiControleur);
		panel_3.add(btnRefuser);
		
		listModel = new DefaultListModel<>();
		results = new JList<String>(listModel);
//		results.setCellRenderer(new MyCellRenderer());
		results.setSelectionModel(new DefaultListSelectionModel() 
		{
		    @Override
		    public void setSelectionInterval(int index0, int index1)
		    {
		        if(super.isSelectedIndex(index0))
		        {
		            super.removeSelectionInterval(index0, index1);
		        }
		        else 
		        {
		            super.addSelectionInterval(index0, index1);
		        }
		    }
		});
	
		results.setBorder(new TitledBorder(new LineBorder(new Color(184, 207,
				229)), "R\u00E9sultats sur Google", TitledBorder.CENTER, 
				TitledBorder.TOP, null, new Color(255, 200, 0)));
		results.setVisibleRowCount(10);
		
		showResults = new JScrollPane(results);
		showResults.setBounds(10, 80, 780, 450);
		frame.getContentPane().add(showResults);
		
		JButton btnChargerExcel = new JButton(guiControleur.getCHARGEREXCEL());
		btnChargerExcel.addActionListener(guiControleur);
		btnChargerExcel.setBounds(12, 13, 150, 27);
		frame.getContentPane().add(btnChargerExcel);
		
	}
	
	

	public JFrame getFrame() {
		return frame;
	}

	public JTextField getSujet() {
		return sujet;
	}

	public JTextField getValeur() {
		return valeur;
	}

	public JTextField getPage() {
		return page;
	}

	public JList<String> getResults() {
		return results;
	}

	public DefaultListModel<String> getListModel() {
		return listModel;
	}

	public JScrollPane getShowResults() {
		return showResults;
	}
}
