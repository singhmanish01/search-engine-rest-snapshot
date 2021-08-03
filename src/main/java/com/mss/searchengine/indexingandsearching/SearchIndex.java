package com.mss.searchengine.indexingandsearching;

import com.mss.searchengine.constants.IndexConstants;
import com.mss.searchengine.dto.SearchResponse;
import com.mss.searchengine.service.DocumentService;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Component
public class SearchIndex {

    @Autowired
    private IndexConstants indexConstants;

    @Autowired
    DocumentService documentService;
    
    public SearchIndex() {
    }

    public List<SearchResponse> search(String text) throws Exception {
        IndexSearcher searcher = createSearcher();

        //Search By text
        TopDocs foundDocs = searchByText(text, searcher);
        System.out.println("Total Results :: " + foundDocs.totalHits);

        List<SearchResponse> results = new ArrayList<>();

        for (ScoreDoc sd : foundDocs.scoreDocs) {
            Document d = searcher.doc(sd.doc);
            SearchResponse searchResponse = new SearchResponse();
            searchResponse.setAuthorName(d.get(indexConstants.authorName));
            searchResponse.setDocumentName(d.get(indexConstants.documentName));
            searchResponse.setPublication(d.get(indexConstants.publication));
            searchResponse.setLanguage(d.get(indexConstants.language));
            searchResponse.setDocumentId(d.get(indexConstants.documentId));
            searchResponse.setDocumentType(documentService.findDocumentById(Long.parseLong(d.get(indexConstants.documentId))).getDocumentType().getDocumentType());            
            results.add(searchResponse);
        }

        return results;
    }

    private IndexSearcher createSearcher() throws IOException {
        Directory dir = FSDirectory.open(Paths.get(indexConstants.indexDir));
        IndexReader reader = DirectoryReader.open(dir);
        return new IndexSearcher(reader);
    }

    private TopDocs searchByText(String text, IndexSearcher searcher) throws Exception {
        MultiFieldQueryParser qp = new MultiFieldQueryParser(new String[]{
                indexConstants.content,
                indexConstants.documentName,
                indexConstants.authorName,
                indexConstants.publication,
                indexConstants.language}, new StandardAnalyzer());
        Query query = qp.parse(text);
        System.out.println("query " + query);
        return searcher.search(query, indexConstants.topHundredResults);
    }

}
