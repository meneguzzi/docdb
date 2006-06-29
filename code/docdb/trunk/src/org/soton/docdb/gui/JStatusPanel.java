/**
 * 
 */
package org.soton.docdb.gui;

import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JLabel;
import javax.swing.JProgressBar;

/**
 * @author frm05r
 *
 */
@SuppressWarnings("serial")
public class JStatusPanel extends JPanel {

	private JLabel jStatusLabel = null;
	private JLabel jStatusTextLabel = null;
	private JProgressBar jStatusProgressBar = null;
	
	public static final int ERROR_MESSAGE = 0;
	public static final int STATUS_MESSAGE = 1;
	public static final int PROGRESS_MESSAGE = 2;

	/**
	 * This is the default constructor
	 */
	public JStatusPanel() {
		super();
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		jStatusTextLabel = new JLabel();
		jStatusTextLabel.setText("Idle");
		jStatusLabel = new JLabel();
		jStatusLabel.setText("Status:");
		this.setLayout(new BorderLayout());
		this.setSize(300, 32);
		this.add(jStatusLabel, java.awt.BorderLayout.WEST);
		this.add(jStatusTextLabel, java.awt.BorderLayout.CENTER);
		this.add(getJStatusProgressBar(), java.awt.BorderLayout.EAST);
	}
	
	protected void setStatusText(int iStatusType, Object message) {
		switch(iStatusType) {
		case ERROR_MESSAGE:
			jStatusTextLabel.setForeground(Color.RED);
			jStatusTextLabel.setText(message.toString());
			break;
		case STATUS_MESSAGE:
			jStatusTextLabel.setForeground(Color.BLACK);
			jStatusTextLabel.setText(message.toString());
			break;
		case PROGRESS_MESSAGE:
			if(message instanceof Number) {
				Number num = (Number) message;
				getJStatusProgressBar().setValue(num.intValue());
			}
			break;
		}
	}
	
	public void setErrorMessage(String text) {
		this.setStatusText(ERROR_MESSAGE, text);
	}
	
	public void setStatusMessage(String text) {
		this.setStatusText(STATUS_MESSAGE, text);
	}
	
	public void setProgressMessage(Number progress) {
		this.setStatusText(PROGRESS_MESSAGE, progress);
	}

	/**
	 * This method initializes jStatusProgressBar	
	 * 	
	 * @return javax.swing.JProgressBar	
	 */
	private JProgressBar getJStatusProgressBar() {
		if (jStatusProgressBar == null) {
			jStatusProgressBar = new JProgressBar();
		}
		return jStatusProgressBar;
	}

}  //  @jve:decl-index=0:visual-constraint="10,10"
