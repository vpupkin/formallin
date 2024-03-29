package com.dataServices;

 
import java.io.File;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import javax.xml.bind.JAXBException;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.Filter;
import org.apache.lucene.store.SimpleFSDirectory;
import org.apache.lucene.util.Version;
import org.apache.lucene.index.IndexWriter.MaxFieldLength;
import org.apache.lucene.search.Collector;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.Searcher;
import org.apache.lucene.search.Sort;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.LockObtainFailedException;
import org.apache.lucene.store.RAMDirectory;

import com.objects.*;
import com.dataConversion.*;

import eu.blky.wdb.WdbCollector;

public class AddressSearch {
	private Directory inMemoryIndex;
	private IndexWriter indexWriter;
	private Searcher searcher;
	private StandardAnalyzer standardAnalyzer;
	private QueryParser queryParser;
	private Hashtable<String, Property> addressLookup= null;
	private static AddressSearch addressSearch;
	//Default file path
	private String xmlFilePath = "C:/java/nrhp.xml";
	
	
	private File getDirToIndex() {
		String pathTmp ="./.indexfile";
		File dirToIndex = new File(pathTmp);
		dirToIndex .deleteOnExit();
		return dirToIndex;
	}	

	private AddressSearch() throws CorruptIndexException, LockObtainFailedException, IOException, JAXBException {
		MaxFieldLength mfl = MaxFieldLength .UNLIMITED ;  
		
		inMemoryIndex = new SimpleFSDirectory(getDirToIndex() );
		standardAnalyzer = new StandardAnalyzer(Version.LUCENE_30); 
		indexWriter = new IndexWriter(inMemoryIndex, standardAnalyzer, mfl);
		addressLookup = new Hashtable<String, Property>();
		buildIndex();
		searcher = new IndexSearcher(inMemoryIndex);
		
		queryParser = new QueryParser(Version.LUCENE_30, "data", standardAnalyzer);
		queryParser.setAllowLeadingWildcard(true);
	}
	
	private void buildIndex() throws JAXBException, CorruptIndexException, IOException {
		List<Property> propList = getAddressList();
		Document doc = null;
		StringBuffer sb = null;
		for (Property p: propList) {
			System.out.println(""+p);
			doc = new Document();
			sb= new StringBuffer();
			if ((p.getId()==null)||(p.getAddress()==null)) continue;
			doc.add(new Field("propId", p.getId().toString(),Field.Store.YES, Field.Index.NO));
			if ((p.getAddress()==null) || (p.getCity()==null) || (p.getState()==null))
				continue;
			sb.append(p.getAddress().replaceAll(" ", "")).append(p.getCity().replaceAll(" ", ""));
			doc.add(new Field("data", sb.toString(), Field.Store.YES, Field.Index.ANALYZED));
			indexWriter.addDocument(doc);
			//also add to HashTable
			addressLookup.put(p.getId().toString(), p);
		}
		indexWriter.optimize();
		indexWriter.close();
	}
	
	public List<Property> search(String searchString) throws ParseException, IOException {
		List<Property> results = new ArrayList<Property>();
		Query query = queryParser.parse(searchString);
		
		WdbCollector hits = new WdbCollector(true);
		/*Hits hits = */searcher.search(query,hits );
		int maxCount = 11;
		int countTmp = 0;
		if (hits.length() != 0)
			for (int i=0; i< hits.length(); i++) {
				results.add(addressLookup.get(hits.doc(i).get("propId")));
				countTmp++;
				if (countTmp>maxCount){
					break;
				}
			}
		return results;
	}

	public static synchronized AddressSearch getAddressSearch() throws Exception{
		if (addressSearch == null)
			addressSearch = new AddressSearch();
		return addressSearch;
	}
	
	private List<Property> getAddressList() throws JAXBException {
		Xml2Obj x2o = new Xml2Obj();
		return x2o.getAddressList(xmlFilePath);
	}
	
	public static void initialize(String xmlFile) throws Exception {
		getAddressSearch();
	}
}
