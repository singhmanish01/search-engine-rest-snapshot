package com.appperfect.searchengine.indexingandsearching;

import com.appperfect.searchengine.constants.IndexConstants;
import com.appperfect.searchengine.model.Language;
import com.appperfect.searchengine.service.DocumentService;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.FSDirectory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.persistence.Index;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Component
public class WriteIndex {

    @Autowired
    private DocumentService documentService;

    @Autowired
    private IndexConstants indexConstants;

    @Scheduled(fixedRate = 100000) // scheduled every 15 minutes
    public void indexing() throws Exception {
        System.out.println("I'm indexing");
        IndexWriter writer = createWriter();
        List<Document> documents = new ArrayList<>();

        long id = 1;
        while (documentService.findById(id)) {
            com.appperfect.searchengine.model.Document retrievedDoc = documentService.findDocumentById(id);
            if (retrievedDoc.getIsIndexed() || !(retrievedDoc.getIsPublic())) {
                id++;
                continue;
            }

            String path = retrievedDoc.getDocumentFilePath();
            PdfManager pdfManager = new PdfManager();
            pdfManager.setFilePath(path);
            String content = pdfManager.toText();

            Language language =  retrievedDoc.getLanguage();
            String lang = language.getLanguage();
            System.out.println("language is: " + lang);
            Document document1 = createDocument(
                    content, retrievedDoc.getAuthorName(),
                    retrievedDoc.getDocumentName(), retrievedDoc.getPublication(), lang, id);
            documents.add(document1);

            documentService.setIndexedFieldTrue(id);
            id++;
        }

        writer.addDocuments(documents);
        writer.commit();
        writer.close();
    }

    private Document createDocument(String content, String authorName, String documentName, String publication, String language, long id) {
        Document document = new Document();

        document.add(new TextField(indexConstants.content, content, Field.Store.NO));
        document.add(new TextField(indexConstants.authorName, authorName, Field.Store.YES));
        document.add(new TextField(indexConstants.publication, publication, Field.Store.YES));
        document.add(new TextField(indexConstants.documentName, documentName, Field.Store.YES));
        document.add(new TextField(indexConstants.language, language, Field.Store.YES));
        document.add(new StringField(indexConstants.documentId, String.valueOf(id), Field.Store.YES));
        return document;
    }

    private IndexWriter createWriter() throws IOException {
        FSDirectory dir = FSDirectory.open(Paths.get(indexConstants.indexDir));
        IndexWriterConfig config = new IndexWriterConfig(new StandardAnalyzer());
        config.setOpenMode(IndexWriterConfig.OpenMode.CREATE_OR_APPEND);
        return new IndexWriter(dir, config);
    }

}
