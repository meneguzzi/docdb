package org.soton.docdb.gui;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;

import org.soton.docdb.diff.FileScanner;

@SuppressWarnings("serial")
public class FindDuplicatesPanel extends JPanel {
	
	private final Logger logger = Logger.getLogger(FindDuplicatesPanel.class.getName());

	private JTextField jScanFolderTextField = null;
	private JPanel jAddressBarPanel = null;
	private JButton jBrowseButton = null;
	private JButton jScanButton = null;
	private JPanel jScanOptionsPanel = null;
	private JCheckBox jDuplicateNamesCheckBox = null;
	private JCheckBox jDuplicateSizeCheckBox = null;
	private JCheckBox jDuplicateDigestsCheckBox = null;
	private JFileChooser jActiveDirectoryFileChooser = null;
	
	private FileScanner fileScanner = null;
	private Thread tScanner = null;

	private JStatusPanel jStatusPanel = null;

	private DuplicatesViewPanel duplicatesViewPanel = null;
	/**
	 * This is the default constructor
	 */
	public FindDuplicatesPanel() {
		super();
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setLayout(new BorderLayout());
		this.setSize(540, 307);
		this.add(getJpAddressBar(), java.awt.BorderLayout.NORTH);
		this.add(getJScanOptionsPanel(), java.awt.BorderLayout.EAST);
		this.add(getJStatusPanel(), java.awt.BorderLayout.SOUTH);
		this.add(getDuplicatesViewPanel(), java.awt.BorderLayout.CENTER);
	}
	
	/**
	 * This method initializes jActiveDirectoryFileChooser
	 * 
	 * @return javax.swing.JFileChooser
	 */
	private JFileChooser getJActiveDirectoryFileChooser() {
		if(jActiveDirectoryFileChooser == null) {
			jActiveDirectoryFileChooser = new JFileChooser();
			jActiveDirectoryFileChooser.setFileSelectionMode(javax.swing.JFileChooser.DIRECTORIES_ONLY);
		}
		return jActiveDirectoryFileChooser;
	}

	/**
	 * This method initializes jtfScanFolder	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getJtfScanFolder() {
		if (jScanFolderTextField == null) {
			jScanFolderTextField = new JTextField();
			jScanFolderTextField.getDocument().addDocumentListener(new DocumentListener() {

				public void insertUpdate(DocumentEvent e) {
					this.handleDocEvent(e);
				}

				public void removeUpdate(DocumentEvent e) {
					this.handleDocEvent(e);
				}

				public void changedUpdate(DocumentEvent e) {
					this.handleDocEvent(e);
				}
				
				protected void handleDocEvent(DocumentEvent e) {
					int iLength = e.getDocument().getLength();
					String text;
					try {
						text = e.getDocument().getText(0, iLength);
						File fSelectedFile = new File(text);
						if(fSelectedFile.exists() && fSelectedFile.isDirectory()) {
							getJActiveDirectoryFileChooser().setSelectedFile(fSelectedFile);
							getJStatusPanel().setStatusMessage("Idle.");
						} else {
							getJActiveDirectoryFileChooser().setSelectedFile(null);
							getJStatusPanel().setErrorMessage("Invalid file selected.");
						}
					} catch (BadLocationException e1) {
						logger.throwing(this.getClass().getName(), "handleDocEvent",e1);
					}
				}
				
			});
		}
		return jScanFolderTextField;
	}

	/**
	 * This method initializes jpAddressBar	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJpAddressBar() {
		if (jAddressBarPanel == null) {
			jAddressBarPanel = new JPanel();
			jAddressBarPanel.setLayout(new BorderLayout());
			jAddressBarPanel.add(getJtfScanFolder(), java.awt.BorderLayout.CENTER);
			jAddressBarPanel.add(getJbBrowse(), java.awt.BorderLayout.EAST);
			jAddressBarPanel.add(getJScanButton(), java.awt.BorderLayout.SOUTH);
		}
		return jAddressBarPanel;
	}

	/**
	 * This method initializes jbBrowse	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getJbBrowse() {
		if (jBrowseButton == null) {
			jBrowseButton = new JButton();
			jBrowseButton.setText("Browse");
			jBrowseButton.addActionListener(new java.awt.event.ActionListener() {   
				public void actionPerformed(java.awt.event.ActionEvent e) {    
					int iChosenOption = getJActiveDirectoryFileChooser().showOpenDialog(FindDuplicatesPanel.this);
					if(iChosenOption == JFileChooser.APPROVE_OPTION) {
						try {
							getJtfScanFolder().setText(jActiveDirectoryFileChooser.getSelectedFile().getCanonicalPath());
						} catch (IOException e1) {
							logger.throwing(this.getClass().getName(), "getJbBrowse",e1);
						}
					} else {
						getJtfScanFolder().setText("");
					}
				}
			
			});
		}
		return jBrowseButton;
	}

	/**
	 * This method initializes jScanButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getJScanButton() {
		if (jScanButton == null) {
			jScanButton = new JButton();
			jScanButton.setText("Scan");
			jScanButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					File fSelectedFile = getJActiveDirectoryFileChooser().getSelectedFile();
					if(fSelectedFile != null) {
						try {
							doScan(fSelectedFile);
						} catch (Exception e1) {
							logger.throwing(this.getClass().getName(), "getJScanButton",e1);
						}
					}
				}
			});
		}
		return jScanButton;
	}

	/**
	 * This method initializes jScanOptionsPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJScanOptionsPanel() {
		if (jScanOptionsPanel == null) {
			GridLayout gridLayout = new GridLayout();
			gridLayout.setRows(3);
			gridLayout.setColumns(1);
			jScanOptionsPanel = new JPanel();
			jScanOptionsPanel.setLayout(gridLayout);
			jScanOptionsPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Find Duplicate", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, null, null));
			jScanOptionsPanel.add(getJDuplicateNamesCheckBox(), null);
			jScanOptionsPanel.add(getJDuplicateSizeCheckBox(), null);
			jScanOptionsPanel.add(getJDuplicateDigestsCheckBox(), null);
		}
		return jScanOptionsPanel;
	}

	/**
	 * This method initializes jDuplicateNamesCheckBox	
	 * 	
	 * @return javax.swing.JCheckBox	
	 */
	private JCheckBox getJDuplicateNamesCheckBox() {
		if (jDuplicateNamesCheckBox == null) {
			jDuplicateNamesCheckBox = new JCheckBox();
			jDuplicateNamesCheckBox.setText("Names");
			jDuplicateNamesCheckBox.setSelected(true);
			jDuplicateNamesCheckBox.setActionCommand("Names");
		}
		return jDuplicateNamesCheckBox;
	}

	/**
	 * This method initializes jDuplicateSizeCheckBox	
	 * 	
	 * @return javax.swing.JCheckBox	
	 */
	private JCheckBox getJDuplicateSizeCheckBox() {
		if (jDuplicateSizeCheckBox == null) {
			jDuplicateSizeCheckBox = new JCheckBox();
			jDuplicateSizeCheckBox.setText("Sizes");
			jDuplicateSizeCheckBox.setSelected(true);
		}
		return jDuplicateSizeCheckBox;
	}

	/**
	 * This method initializes jDuplicateDigestsCheckBox	
	 * 	
	 * @return javax.swing.JCheckBox	
	 */
	private JCheckBox getJDuplicateDigestsCheckBox() {
		if (jDuplicateDigestsCheckBox == null) {
			jDuplicateDigestsCheckBox = new JCheckBox();
			jDuplicateDigestsCheckBox.setText("Digests");
		}
		return jDuplicateDigestsCheckBox;
	}

	/**
	 * This method initializes jStatusPanel	
	 * 	
	 * @return org.soton.docdb.gui.JStatusPanel	
	 */
	private JStatusPanel getJStatusPanel() {
		if (jStatusPanel == null) {
			jStatusPanel = new JStatusPanel();
		}
		return jStatusPanel;
	}
	
	private void doScan(File fSelectedFile) throws Exception {
		if(tScanner != null && tScanner.isAlive()) {
			getJStatusPanel().setErrorMessage("Scan in Progress");
			return;
		}
		fileScanner = new FileScanner(fSelectedFile);
		fileScanner.addChangeListener(new ChangeListener(){
			public void stateChanged(ChangeEvent e) {
				FileScanner scanner = (FileScanner)e.getSource();
				int status = (int) ((scanner.getCurrentFileIndex()/(float)scanner.getTotalFiles())*100);
				getJStatusPanel().setProgressMessage(status);
				if(scanner.getTotalFiles() == 0) {
					getJStatusPanel().setStatusMessage("Done");
					if(getJDuplicateNamesCheckBox().isSelected())
						getDuplicatesViewPanel().addNameDuplicates(scanner.findDuplicateNames());
					if(getJDuplicateSizeCheckBox().isSelected())
						getDuplicatesViewPanel().addSizeDuplicates(scanner.findDuplicateSizes());
					if(getJDuplicateDigestsCheckBox().isSelected())
						getDuplicatesViewPanel().addDigestDuplicates(scanner.findDuplicateDigests());
				}
			}
			
		});
		tScanner = new Thread(fileScanner);
		getJStatusPanel().setStatusMessage("Scanning");
		tScanner.start();
	}

	/**
	 * This method initializes duplicatesViewPanel	
	 * 	
	 * @return org.soton.docdb.gui.DuplicatesViewPanel	
	 */
	private DuplicatesViewPanel getDuplicatesViewPanel() {
		if (duplicatesViewPanel == null) {
			duplicatesViewPanel = new DuplicatesViewPanel();
		}
		return duplicatesViewPanel;
	}

}  //  @jve:decl-index=0:visual-constraint="10,10"
