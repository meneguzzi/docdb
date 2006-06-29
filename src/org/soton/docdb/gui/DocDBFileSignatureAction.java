/**
 * 
 */
package org.soton.docdb.gui;

import java.util.logging.Logger;

import javax.swing.AbstractAction;
import javax.swing.Icon;

import org.soton.docdb.diff.FileSignature;

/**
 * @author frm05r
 *
 */
public abstract class DocDBFileSignatureAction extends AbstractAction {
	protected final Logger logger = Logger.getLogger(DocDBFileSignatureAction.class.getName());
	
	protected FileSignature activeSignature = null;
	
	public void setIcon(Icon icon) {
		this.putValue(AbstractAction.SMALL_ICON, icon);
	}
	
	public void setName(String name) {
		this.putValue(AbstractAction.NAME, name);
	}

	/**
	 * @return Returns the activeSignature.
	 */
	public FileSignature getActiveSignature() {
		if(activeSignature == null) {
			logger.fine("Getting a null signature");
		}
		return activeSignature;
	}

	/**
	 * @param activeSignature The activeSignature to set.
	 */
	public void setActiveSignature(FileSignature activeSignature) {
		this.activeSignature = activeSignature;
	}

}
