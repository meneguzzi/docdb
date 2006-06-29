package org.soton.docdb.gui;

import java.awt.BorderLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.logging.Logger;

import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import org.soton.docdb.diff.FileSignature;
import org.soton.docdb.diff.SimpleMultimap;

@SuppressWarnings("serial")
public class DuplicatesViewPanel extends JTabbedPane {
	
	@SuppressWarnings("unused")
	private final Logger logger = Logger.getLogger(DuplicatesViewPanel.class.getName());

	private JPanel jNameDuplicatesPanel = null;
	private JPanel jSizeDuplicatesPanel = null;
	private JPanel jDigestDuplicatesPanel = null;
	private JTree jNameDuplicatesTree = null;
	private JTree jSizeDuplicatesTree = null;
	private JTree jDigestDuplicatesTree = null;

	private JScrollPane jNameTreeScrollPane = null;

	private JScrollPane jSizeTreeScrollPane = null;

	private JScrollPane jDigestTreeScrollPane = null;
	
	private MouseListener jDuplicatesTreeMouseListener = null;

	private JPopupMenu jFileSignaturePopupMenu = null;

	protected DocDBDeleteAction docDBDeleteAction;

	protected DocDBRenameAction docDBRenameAction;

	/**
	 * This is the default constructor
	 */
	public DuplicatesViewPanel() {
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
		this.addTab("Name", null, getJNameDuplicatesPanel(), "Name Duplicates");
		this.addTab("Size", null, getJSizeDuplicatesPanel(), "Size Duplicates");
		this.addTab("Digest", null, getJDigestDuplicatesPanel(), "Digest Duplicates");
	}
	
	public void addNameDuplicates(SimpleMultimap<String, FileSignature> duplicateNames) {
		DefaultMutableTreeNode top = new DefaultMutableTreeNode("Name Clashes");
		for (String name : duplicateNames.keySet()) {
			StringBuffer sb = new StringBuffer();
			sb.append("Possible duplicates named '");
			sb.append(name);
			sb.append("': ");
			DefaultMutableTreeNode nameNode = new DefaultMutableTreeNode(name);
			for(FileSignature signature : duplicateNames.getValues(name)) {
				sb.append(System.getProperty("line.separator"));
				sb.append(signature);
				DefaultMutableTreeNode duplicateNameNode = new DefaultMutableTreeNode(signature);
				nameNode.add(duplicateNameNode);
			}
			top.add(nameNode);
			logger.fine(sb.toString());
		}
		this.jNameDuplicatesTree = new JTree(top);
		this.jNameDuplicatesTree.addMouseListener(getJDuplicatesTreeMouseListener());
		this.jNameTreeScrollPane.setViewportView(jNameDuplicatesTree);
		this.setEnabledAt(0, true);
		System.gc();
	}
	
	public void addSizeDuplicates(SimpleMultimap<Long, FileSignature> duplicateSizes) {
		DefaultMutableTreeNode top = new DefaultMutableTreeNode("Size Clashes");
		for (Long size : duplicateSizes.keySet()) {
			StringBuffer sb = new StringBuffer();
			sb.append("Possible duplicates with size '");
			sb.append(size);
			sb.append("': ");
			DefaultMutableTreeNode sizeNode = new DefaultMutableTreeNode(size);
			for(FileSignature signature : duplicateSizes.getValues(size)) {
				sb.append(System.getProperty("line.separator"));
				sb.append(signature);
				DefaultMutableTreeNode duplicateSizeNode = new DefaultMutableTreeNode(signature);
				sizeNode.add(duplicateSizeNode);
			}
			top.add(sizeNode);
			logger.fine(sb.toString());
		}
		this.jSizeDuplicatesTree = new JTree(top);
		this.jSizeDuplicatesTree.addMouseListener(getJDuplicatesTreeMouseListener());
		this.jSizeTreeScrollPane.setViewportView(jSizeDuplicatesTree);
		this.setEnabledAt(1, true);
		System.gc();
	}
	
	public void addDigestDuplicates(SimpleMultimap<String, FileSignature> duplicateDigests) {
		DefaultMutableTreeNode top = new DefaultMutableTreeNode("Digest Clashes");
		for (String digest : duplicateDigests.keySet()) {
			StringBuffer sb = new StringBuffer();
			sb.append("Possible duplicates with digest '");
			sb.append(digest);
			sb.append("': ");
			DefaultMutableTreeNode digestNode = new DefaultMutableTreeNode(digest);
			for(FileSignature signature : duplicateDigests.getValues(digest)) {
				sb.append(System.getProperty("line.separator"));
				sb.append(signature);
				DefaultMutableTreeNode duplicateDigestNode = new DefaultMutableTreeNode(signature);
				digestNode.add(duplicateDigestNode);
			}
			logger.fine(sb.toString());
			top.add(digestNode);
		}
		this.jDigestDuplicatesTree = new JTree(top);
		this.jDigestDuplicatesTree.addMouseListener(getJDuplicatesTreeMouseListener());
		this.jDigestTreeScrollPane.setViewportView(jDigestDuplicatesTree);
		this.setEnabledAt(2, true);
		System.gc();
	}
	
	public void reset() {
		this.setEnabledAt(0, false);
		this.setEnabledAt(1, false);
		this.setEnabledAt(2, false);
	}

	/**
	 * This method initializes jNameDuplicatesPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJNameDuplicatesPanel() {
		if (jNameDuplicatesPanel == null) {
			jNameDuplicatesPanel = new JPanel();
			jNameDuplicatesPanel.setLayout(new BorderLayout());
			jNameDuplicatesPanel.setName("");
			jNameDuplicatesPanel.setVisible(false);
			jNameDuplicatesPanel.add(getJNameTreeScrollPane(), java.awt.BorderLayout.NORTH);
		}
		return jNameDuplicatesPanel;
	}

	/**
	 * This method initializes jSizeDuplicatesPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJSizeDuplicatesPanel() {
		if (jSizeDuplicatesPanel == null) {
			jSizeDuplicatesPanel = new JPanel();
			jSizeDuplicatesPanel.setLayout(new BorderLayout());
			jSizeDuplicatesPanel.setVisible(false);
			jSizeDuplicatesPanel.add(getJSizeTreeScrollPane(), java.awt.BorderLayout.NORTH);
		}
		return jSizeDuplicatesPanel;
	}

	/**
	 * This method initializes jDigestDuplicatesPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJDigestDuplicatesPanel() {
		if (jDigestDuplicatesPanel == null) {
			jDigestDuplicatesPanel = new JPanel();
			jDigestDuplicatesPanel.setLayout(new BorderLayout());
			jDigestDuplicatesPanel.setVisible(false);
			jDigestDuplicatesPanel.add(getJDigestTreeScrollPane(), java.awt.BorderLayout.NORTH);
		}
		return jDigestDuplicatesPanel;
	}

	/**
	 * This method initializes jNameDuplicatesTree	
	 * 	
	 * @return javax.swing.JTree	
	 */
	private JTree getJNameDuplicatesTree() {
		if (jNameDuplicatesTree == null) {
			jNameDuplicatesTree = new JTree(new DefaultMutableTreeNode("Name Clashes"));
		}
		return jNameDuplicatesTree;
	}

	/**
	 * This method initializes jSizeDuplicatesTree	
	 * 	
	 * @return javax.swing.JTree	
	 */
	private JTree getJSizeDuplicatesTree() {
		if (jSizeDuplicatesTree == null) {
			jSizeDuplicatesTree = new JTree(new DefaultMutableTreeNode("Size Clashes"));
		}
		return jSizeDuplicatesTree;
	}

	/**
	 * This method initializes jDigestDuplicatesTree	
	 * 	
	 * @return javax.swing.JTree	
	 */
	private JTree getJDigestDuplicatesTree() {
		if (jDigestDuplicatesTree == null) {
			jDigestDuplicatesTree = new JTree(new DefaultMutableTreeNode("Digest Clashes"));
		}
		return jDigestDuplicatesTree;
	}

	/**
	 * This method initializes jScrollPane	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */
	private JScrollPane getJNameTreeScrollPane() {
		if (jNameTreeScrollPane == null) {
			jNameTreeScrollPane = new JScrollPane();
			jNameTreeScrollPane.setViewportView(getJNameDuplicatesTree());
		}
		return jNameTreeScrollPane;
	}

	/**
	 * This method initializes jScrollPane1	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */
	private JScrollPane getJSizeTreeScrollPane() {
		if (jSizeTreeScrollPane == null) {
			jSizeTreeScrollPane = new JScrollPane();
			jSizeTreeScrollPane.setViewportView(getJSizeDuplicatesTree());
		}
		return jSizeTreeScrollPane;
	}

	/**
	 * This method initializes jScrollPane2	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */
	private JScrollPane getJDigestTreeScrollPane() {
		if (jDigestTreeScrollPane == null) {
			jDigestTreeScrollPane = new JScrollPane();
			jDigestTreeScrollPane.setViewportView(getJDigestDuplicatesTree());
		}
		return jDigestTreeScrollPane;
	}
	
	protected class DuplicatesTreeMouseListener extends MouseAdapter {

		public void mousePressed(MouseEvent e) {
			Object obj = e.getSource();
			JTree tree = null;
			if((obj == getJDigestDuplicatesTree()) ||
				(obj == getJSizeDuplicatesTree()) || 
				(obj == getJNameDuplicatesTree()) ) {
				tree = (JTree) obj;
			} else {
				return;
			}
			
			int selRow = tree.getRowForLocation(e.getX(), e.getY());
			TreePath selPath = tree.getPathForLocation(e.getX(), e.getY());
			if(selRow != -1) {
	             DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) selPath.getLastPathComponent();
	             if(selectedNode.getUserObject() instanceof FileSignature) {
	            	 FileSignature signature = (FileSignature) selectedNode.getUserObject();
	            	 logger.info("Signature "+ signature+" selected");
	            	 if(e.getButton() != MouseEvent.BUTTON1) {
	            		 logger.info("Showing Popup");
	            		 JPopupMenu popup = getJFileSignaturePopupMenu();
	            		 popup.setLabel(signature.getSimpleName());
	            		 docDBDeleteAction.setActiveSignature(signature);
	            		 docDBRenameAction.setActiveSignature(signature);
	            		 popup.show(e.getComponent(),e.getX(), e.getY());
	            	 }
	             } else {
	            	 return;
	             }
	         }
		}
		
	}

	/**
	 * @return Returns the jDuplicatesTreeMouseListener.
	 */
	public MouseListener getJDuplicatesTreeMouseListener() {
		if(jDuplicatesTreeMouseListener == null) {
			jDuplicatesTreeMouseListener = new DuplicatesTreeMouseListener();
		}
		return jDuplicatesTreeMouseListener;
	}

	/**
	 * This method initializes jFileSignaturePopupMenu	
	 * 	
	 * @return javax.swing.JPopupMenu	
	 */
	private JPopupMenu getJFileSignaturePopupMenu() {
		if (jFileSignaturePopupMenu == null) {
			docDBRenameAction = new DocDBRenameAction();
			docDBDeleteAction = new DocDBDeleteAction();
			jFileSignaturePopupMenu = new JPopupMenu();
			jFileSignaturePopupMenu.add(docDBDeleteAction);
			jFileSignaturePopupMenu.add(docDBRenameAction);
		}
		return jFileSignaturePopupMenu;
	}

}
