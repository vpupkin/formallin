package eu.blky.wdb;

import java.io.IOException; 
import java.util.ArrayList;
import java.util.List; 
import org.apache.lucene.document.Document; 
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.search.Collector;
import org.apache.lucene.search.Scorer;

/** 
 * <b>Description:TODO</b>
 * @author      vipup<br>
 * <br>
 * <b>Copyright:</b>     Copyright (c) 2006-2008 Monster AG <br>
 * <b>Company:</b>       Monster AG  <br>
 * 
 * Creation:  19.04.2012::11:09:53<br> 
 */
public class WdbCollector extends Collector {

	private Scorer scorer;
	private boolean distinct;
	

	public WdbCollector(boolean distinct) {
		this.distinct = distinct;
	}

	@Override
	public void setScorer(Scorer scorer) throws IOException {
		this.scorer = scorer;
		System.out.println("scorer:::"+this.scorer);
	}

	List<String> collected = new ArrayList<String>();
	
	@Override
	public void collect(int i) throws IOException {
		String e = ""+i;
		
		if (distinct ){
			if (!this.collected.contains(e)){
				this.collected.add(e);
			}else{
				System.out.println("duplicated");
				return;
			}
		}else{
			this.collected.add(e);
		}
		
		String toPrint = "i="+i+"((("+collected;
		toPrint = toPrint.length()>80?toPrint.substring(0,40)+"..."+toPrint.substring(toPrint.length()-40):toPrint;
		System.out.println(toPrint );
	}

	List<IndexReader> ir = new ArrayList<IndexReader>() ;
	
	@Override
	public void setNextReader(IndexReader indexreader, int i)
			throws IOException {
		ir.add(indexreader);
		System.out.println("$"+i+":="+indexreader);
	}

	@Override
	public boolean acceptsDocsOutOfOrder() {
		return true;
	}

	public int length() {
		return collected.size();
	}

	public Document doc(int i) throws CorruptIndexException, IOException {
		Document retval =  null;
		for (IndexReader irTmp:ir){ // TODO ??
			retval  = irTmp.document(i);
			break; 
		}
		
		return retval;
	}

}


 