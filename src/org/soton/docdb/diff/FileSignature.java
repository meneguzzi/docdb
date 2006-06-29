/**
 * 
 */
package org.soton.docdb.diff;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author Felipe
 */
public class FileSignature {

	/**
	 * The default message digest algorithm used in calculations.
	 * 
	 * @uml.property name="MD_ALGORITHM" readOnly="true"
	 */
	public static final String MD_ALGORITHM = "MD5";

	/**
	 * The file being stored by this object.
	 * 
	 * @uml.property name="file"
	 */
	private File file;
	
	/**
	 * A cached version of this file's digest code.
	 * @uml.property  name="digest" multiplicity="(0 -1)" dimension="1"
	 */
	private byte[] digest = null;

	/**
	 * Creates a <code>FileSignature</code> object holding the specified file.
	 * @throws Exception If the specified file does not exist or is a directory
	 */
	public FileSignature(File file) throws Exception{
		if(file.isDirectory())
			throw new Exception(file.getName()+" is a directory.");
		if(!file.exists())
			throw new Exception(file.getName()+" does not exist.");
		
		this.file = file;
	}
	
	/**
	 * Returns the message digest for the file held by this object.
	 */
	public byte[] getDigest() {
		if(digest == null) {
			try {
				MessageDigest md = MessageDigest.getInstance(MD_ALGORITHM);
				//md.update()
				FileInputStream stream = new FileInputStream(this.file);
				byte buf[] = new byte[512];
				int read = 0;
				while(stream.available() > 0) {
					read = stream.read(buf);
					md.update(buf, 0, read);
				}
				this.digest = md.digest();
			} catch (NoSuchAlgorithmException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return digest;
	}
	
	public String getSimpleName() {
		String name = this.getName();
		int lastSeparator = name.lastIndexOf(System.getProperty("file.separator"));
		return name.substring((lastSeparator > 0) ? lastSeparator : 0);
	}
	
	@Override
	public String toString() {
		return file.toString();
	}

	/* (non-Javadoc)
	 * @see java.io.File#getName()
	 */
	public String getName() {
		return file.getName();
	}

	/* (non-Javadoc)
	 * @see java.io.File#getPath()
	 */
	public String getPath() {
		return file.getPath();
	}

	/* (non-Javadoc)
	 * @see java.io.File#length()
	 */
	public long length() {
		return file.length();
	}

	/* (non-Javadoc)
	 * @see java.io.File#getAbsolutePath()
	 */
	public String getAbsolutePath() {
		return file.getAbsolutePath();
	}

	/* (non-Javadoc)
	 * @see java.io.File#getCanonicalPath()
	 */
	public String getCanonicalPath() throws IOException {
		return file.getCanonicalPath();
	}

	/**
	 * @return Returns the file.
	 */
	public File getFile() {
		return file;
	}
}
