package eu.blky.wdb;

import java.io.IOException;
import java.io.Reader;

/** 
 * <b>Description:TODO</b>
 * @author      vipup<br>
 * <br>
 * <b>Copyright:</b>     Copyright (c) 2006-2008 Monster AG <br>
 * <b>Company:</b>       Monster AG  <br>
 * 
 * Creation:  08.05.2012::16:06:06<br> 
 */
public class WdbReader extends Reader {

	private Wdb o;
	private int done = 0;

	public WdbReader(Wdb o) {
		this.o = o;
	}

	@Override
	public int read(char[] cbuf, int off, int len) throws IOException {
		String string = o.toProperties().toString();
		char[] src = string.toCharArray();
		int retval = Math.min(src.length - done , len);
		if (retval == 0){
			return -1; // EOF
		}
		System.arraycopy(src, done , cbuf, off, retval);
		
		done += retval;
		return retval;
	}

	@Override
	public void close() throws IOException {
		// TODO
	}

}


 