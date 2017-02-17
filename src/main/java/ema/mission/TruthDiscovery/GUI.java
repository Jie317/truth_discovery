package ema.mission.TruthDiscovery;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.JLabel;
import java.awt.GridLayout;
import javax.swing.JTextField;
import javax.swing.JSplitPane;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import java.awt.FlowLayout;
import javax.swing.JTextArea;
import javax.swing.JCheckBox;
import javax.swing.SwingConstants;
import java.awt.Checkbox;
import javax.swing.JScrollPane;
import javax.swing.JList;

public class GUI {

	private JFrame frame;
	private JTextField textField;
	private JTextField textField_1;
	private JTextField textField_2;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GUI window = new GUI();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public GUI() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setBounds(0, 0, 448, 35);
		frame.getContentPane().add(panel);
		panel.setLayout(new BoxLayout(panel, BoxLayout.LINE_AXIS));
		
		JLabel lblNewLabel = new JLabel("Subjet");
		panel.add(lblNewLabel);
		
		textField = new JTextField();
		panel.add(textField);
		textField.setColumns(8);
		
		JLabel lblNewLabel_1 = new JLabel("Valeur");
		panel.add(lblNewLabel_1);
		
		textField_1 = new JTextField();
		panel.add(textField_1);
		textField_1.setColumns(8);
		
		JLabel lblNewLabel_2 = new JLabel("Page");
		panel.add(lblNewLabel_2);
		
		textField_2 = new JTextField();
		panel.add(textField_2);
		textField_2.setColumns(3);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBounds(1, 40, 448, 37);
		frame.getContentPane().add(panel_1);
		
		JButton btnNewButton = new JButton("Rechercher");
		panel_1.add(btnNewButton);
		
		JCheckBox chckbxNewCheckBox = new JCheckBox("url");
		chckbxNewCheckBox.setBounds(0, 0, 42, 25);
		frame.getContentPane().add(chckbxNewCheckBox);
		chckbxNewCheckBox.setToolTipText("");
		
		JPanel panel_3 = new JPanel();
		panel_3.setBounds(0, 233, 448, 37);
		frame.getContentPane().add(panel_3);
		
		JButton btnNewButton_1 = new JButton("Accepter");
		panel_3.add(btnNewButton_1);
		
		JList list_1 = new JList();
		list_1.setBounds(0, 76, 448, 157);
		frame.getContentPane().add(list_1);
	}
}
