package org.soton.docdb.gui;

import java.awt.event.ActionEvent;

@SuppressWarnings("serial")
public class DocDBDeleteAction extends DocDBFileSignatureAction {
	
	public DocDBDeleteAction() {
		this.setName("Delete");
	}

	public void actionPerformed(ActionEvent e) {
		if(this.activeSignature != null) {
			logger.info("Deleting "+activeSignature+".");
			if(activeSignature.getFile().delete()) {
				logger.info(activeSignature.toString()+" deleted.");
			} else {
				logger.info("Could not delete "+activeSignature);
			}
			
		}
	}

}
