/**
 * 
 */
package org.soton.docdb.diff;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;
import java.util.logging.Logger;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * @author Felipe
 */
public class FileScanner implements Runnable {
	
	@SuppressWarnings("unused")
	private final Logger logger = Logger.getLogger(FileScanner.class.getName());
	
	private List<ChangeListener> changeListeners = new Vector<ChangeListener>();

	/**
	 * The directory upon which this object operates.
	 * 
	 * @uml.property name="activeDirectory"
	 */
	private File activeDirectory;
	
	private int currentFileIndex = 0;
	private int totalFiles = 0;

	/**
	 * A <code>HashMap</code> into the files that have been scanned by this
	 * object.
	 * 
	 * @uml.property name="scannedFiles"
	 */
	private HashMap<String, FileSignature> scannedFiles;
	
	private SimpleMultimap<String, String> fileNames;
	
	private SimpleMultimap<Long, String> fileSizes;
	
	private SimpleMultimap<String, String> fileDigests;
	
	public FileScanner(File file) throws Exception {
		if(!file.isDirectory())
			throw new Exception(file.getName()+" is not a directory.");
		this.activeDirectory=file;
		scannedFiles = new HashMap<String, FileSignature>();
		
		fileNames = new SimpleMultimap<String, String>();
		fileSizes = new SimpleMultimap<Long, String>();
		fileDigests = new SimpleMultimap<String, String>();
	}

	/**
	 * Scans files in the specified directory for potential duplicates.
	 */
	public void scanFiles() {
		this.scanDirectory(activeDirectory, true);
	}
	
	protected void scanDirectory(File directory, boolean recurse) {
		if(!directory.isDirectory())
			return;
		File files[] = directory.listFiles();
		this.totalFiles = files.length;
		for (int i = 0; i < files.length; i++) {
			this.currentFileIndex = i;
			try {
				@SuppressWarnings("unused")
				FileSignature signature;
				if(!files[i].isDirectory()) {
					signature = new FileSignature(files[i]);
					if(!this.scannedFiles.containsKey(signature.getCanonicalPath())) {
						logger.fine("Storing "+signature+" in HashTable.");
						this.scannedFiles.put(signature.getCanonicalPath(), signature);
						logger.fine("Storing "+signature+" in the table of names.");
						this.fileNames.put(signature.getSimpleName(), signature.getCanonicalPath());
						logger.fine("Storing "+signature+" in the table of sizes.");
						this.fileSizes.put(signature.length(), signature.getCanonicalPath());
						logger.fine("Storing "+signature+" in the table of digests.");
						this.fileDigests.put(new String(signature.getDigest()), signature.getCanonicalPath());
					} else {
						logger.fine("File "+signature+" has already been scanned.");
					}
				}else {
					if(recurse) {
						logger.fine("Creeping into directory "+files[i]+".");
						this.scanDirectory(files[i], recurse);
					} else {
						logger.fine("Ignoring non-file "+files[i]+".");
					}
				}
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			for (ChangeListener listener : changeListeners) {
				listener.stateChanged(new ChangeEvent(this));
			}
		}
		this.currentFileIndex = this.totalFiles;
		for (ChangeListener listener : changeListeners) {
			listener.stateChanged(new ChangeEvent(this));
		}
	}
	
	public SimpleMultimap<String, FileSignature> findDuplicateNames() {
		SimpleMultimap<String, FileSignature> res = new SimpleMultimap<String, FileSignature>();
		for (String name : fileNames.keySet()) {
			List<String> names = fileNames.getValues(name);
			if(names.size() > 1) {
				for (String fileName : names) {
					res.put(name, scannedFiles.get(fileName));
				}
			}
		}
		
		return res;
	}
	
	public SimpleMultimap<Long, FileSignature> findDuplicateSizes(){
		SimpleMultimap<Long, FileSignature> res = new SimpleMultimap<Long, FileSignature>();
		for (Long size : fileSizes.keySet()) {
			List<String> names = fileSizes.getValues(size);
			if(names.size() > 1) {
				for (String fileName : names) {
					res.put(size, scannedFiles.get(fileName));
				}
			}
		}
		return res;
	}
	
	public SimpleMultimap<String, FileSignature> findDuplicateDigests() {
		SimpleMultimap<String, FileSignature> res = new SimpleMultimap<String, FileSignature>();
		for (String digest : fileDigests.keySet()) {
			List<String> names = fileDigests.getValues(digest);
			if(names.size() > 1) {
				for (String fileName : names) {
					res.put(digest, scannedFiles.get(fileName));
				}
			}
		}
		
		return res;
	}
	
	public void findDuplicates() {
		SimpleMultimap<String, FileSignature> duplicateNames = this.findDuplicateNames();
		for (String name : duplicateNames.keySet()) {
			StringBuffer sb = new StringBuffer();
			sb.append("Possible duplicates named '");
			sb.append(name);
			sb.append("': ");
			for(FileSignature signature : duplicateNames.getValues(name)) {
				sb.append(System.getProperty("line.separator"));
				sb.append(signature);
			}
			logger.info(sb.toString());
		}
		
		SimpleMultimap<Long, FileSignature> duplicateSizes = this.findDuplicateSizes();
		for (Long size : duplicateSizes.keySet()) {
			StringBuffer sb = new StringBuffer();
			sb.append("Possible duplicates with size '");
			sb.append(size);
			sb.append("': ");
			for(FileSignature signature : duplicateSizes.getValues(size)) {
				sb.append(System.getProperty("line.separator"));
				sb.append(signature);
			}
			logger.info(sb.toString());
		}
		
		SimpleMultimap<String, FileSignature> duplicateDigests = this.findDuplicateDigests();
		for (String digest : duplicateDigests.keySet()) {
			StringBuffer sb = new StringBuffer();
			sb.append("Possible duplicates with digest '");
			sb.append(digest);
			sb.append("': ");
			for(FileSignature signature : duplicateDigests.getValues(digest)) {
				sb.append(System.getProperty("line.separator"));
				sb.append(signature);
			}
			logger.info(sb.toString());
		}
	}
	
	public void addChangeListener(ChangeListener listener) {
		this.changeListeners.add(listener);
	}
	
	public boolean removeChangeListener(ChangeListener listener) {
		return this.changeListeners.remove(listener);
	}
	
	/**
	 * @return Returns the currentFileIndex.
	 */
	public int getCurrentFileIndex() {
		return currentFileIndex;
	}

	/**
	 * @return Returns the totalFiles.
	 */
	public int getTotalFiles() {
		return totalFiles;
	}

	public void run() {
		logger.info("Starting thread");
		this.scanFiles();
		this.totalFiles = 0;
		for (ChangeListener listener : changeListeners) {
			listener.stateChanged(new ChangeEvent(this));
		}
		logger.info("Stopping thread");
	}

}
