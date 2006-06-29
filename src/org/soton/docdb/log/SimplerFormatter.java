/**
 * 
 */
package org.soton.docdb.log;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

/**
 * @author frm05r
 *
 */
public class SimplerFormatter extends Formatter {
	
    // Line separator string.  This is the value of the line.separator
    // property at the moment that the SimpleFormatter was created.
    @SuppressWarnings("unchecked")
	private String lineSeparator = (String) java.security.AccessController.doPrivileged(
               new sun.security.action.GetPropertyAction("line.separator"));

	/* (non-Javadoc)
	 * @see java.util.logging.Formatter#format(java.util.logging.LogRecord)
	 */
	@Override
	public String format(LogRecord record) {
		StringBuffer sb = new StringBuffer();
		
		String message = formatMessage(record);
		
		sb.append(record.getLevel().getLocalizedName());
		sb.append(": ");
		sb.append(message);
		
		if (record.getThrown() != null) {
		    try {
		        StringWriter sw = new StringWriter();
		        PrintWriter pw = new PrintWriter(sw);
		        record.getThrown().printStackTrace(pw);
		        pw.close();
			sb.append(sw.toString());
		    } catch (Exception ex) {
		    }
		}
		sb.append(lineSeparator);
		
		return sb.toString();
	}

}
