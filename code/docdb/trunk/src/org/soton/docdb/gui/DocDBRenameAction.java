package org.soton.docdb.gui;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.io.File;

import javax.swing.JFileChooser;

@SuppressWarnings("serial")
public class DocDBRenameAction extends DocDBFileSignatureAction {
	
	protected JFileChooser fileChooser;
	
	public DocDBRenameAction() {
		this.setName("Rename / Move");
		fileChooser = new JFileChooser();
	}

	public void actionPerformed(ActionEvent e) {
		if(this.activeSignature != null) {
			logger.info("Renaming "+activeSignature+".");
			fileChooser.setCurrentDirectory(activeSignature.getFile());
			int iChoice = fileChooser.showSaveDialog((Component) e.getSource());
			
			if(iChoice == JFileChooser.APPROVE_OPTION) {
				File selectedFile = fileChooser.getSelectedFile();
				if(activeSignature.getFile().renameTo(selectedFile)) {
					logger.info(activeSignature.toString()+" renamed to "+ selectedFile);
				} else {
					logger.info("Could not rename "+activeSignature);
				}
			} else {
				logger.info("Action Cancelled.");
			}
			
		}
	}

}
