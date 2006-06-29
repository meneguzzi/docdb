/**
 * 
 */
package org.soton.docdb.gui;

import java.awt.BorderLayout;
import javax.swing.JPanel;
import javax.swing.JFrame;

/**
 * @author frm05r
 *
 */
@SuppressWarnings("serial")
public class DocDB extends JFrame {

	private JPanel jContentPane = null;
	private FindDuplicatesPanel findDuplicatesPanel = null;

	/**
	 * This method initializes findDuplicatesPanel	
	 * 	
	 * @return org.soton.docdb.gui.FindDuplicatesPanel	
	 */
	private FindDuplicatesPanel getFindDuplicatesPanel() {
		if (findDuplicatesPanel == null) {
			findDuplicatesPanel = new FindDuplicatesPanel();
		}
		return findDuplicatesPanel;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		DocDB docDB = new DocDB();
		docDB.setVisible(true);

	}

	/**
	 * This is the default constructor
	 */
	public DocDB() {
		super();
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setSize(300, 200);
		this.setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);
		this.setContentPane(getJContentPane());
		this.setTitle("DocDB");
	}

	/**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJContentPane() {
		if (jContentPane == null) {
			jContentPane = new JPanel();
			jContentPane.setLayout(new BorderLayout());
			jContentPane.add(getFindDuplicatesPanel(), java.awt.BorderLayout.CENTER);
		}
		return jContentPane;
	}

}
