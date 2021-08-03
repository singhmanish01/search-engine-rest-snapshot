package com.mss.searchengine.indexingandsearching;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.FSDirectory;
import org.apache.tika.Tika;
import org.apache.tika.exception.TikaException;
import org.apache.tika.io.TikaInputStream;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.sax.BodyContentHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.mss.searchengine.constants.IndexConstants;
import com.mss.searchengine.model.Language;
import com.mss.searchengine.service.DocumentService;

@Component
public class WriteIndex {

    @Autowired
    private DocumentService documentService;

    @Autowired
    private IndexConstants indexConstants;

    @Scheduled(fixedRate = 10000) // scheduled every 15 minutes
    public void indexing() throws Exception {
        System.out.println("I'm indexing");
        IndexWriter writer = createWriter();
        List<Document> documents = new ArrayList<>();

        long id = 1;
        while (documentService.findById(id)) {
            com.mss.searchengine.model.Document retrievedDoc = documentService.findDocumentById(id);
            if (retrievedDoc.getIsIndexed() || !(retrievedDoc.getIsPublic())) {
                id++;
                continue;
            }
            Set<String> extSet = new HashSet<>();
            extSet.add("cpp");
            extSet.add("txt");
            extSet.add("plain");
            extSet.add("java");
            //extSet.add("docx");
            StringBuilder sb;
            String path = retrievedDoc.getDocumentFilePath();
            String content;
            /*if(extSet.contains(retrievedDoc.getDocumentType().getDocumentType())) {
            	System.out.println("for text type");
            	File newFile = new File(path);
            	BufferedReader br = new BufferedReader(new FileReader(newFile));
            	sb = new StringBuilder();
            	String nextLine;
            	while((nextLine = br.readLine())!= null)
            		sb.append(nextLine);
            	br.close();
            	content = sb.toString();
            }
            else if(retrievedDoc.getDocumentType().getDocumentType().compareTo("docx") == 0) {
            	FileInputStream fis = new FileInputStream(retrievedDoc.getDocumentFilePath());
            	XWPFDocument doc = new XWPFDocument(fis);
            	List<XWPFParagraph> para = doc.getParagraphs();
            	sb = new StringBuilder();
            	for(int i = 0;i<para.size();++i)
            		sb.append(para.get(i).getText());
            	doc.close();
            	fis.close();
            	content = sb.toString();
            }
            else if(retrievedDoc.getDocumentType().getDocumentType().compareTo("doc") == 0) {
            	FileInputStream fis = new FileInputStream(retrievedDoc.getDocumentFilePath());
            	HWPFDocument doc = new HWPFDocument(fis);
            	WordExtractor we = new WordExtractor(doc);
            	sb = new StringBuilder();
            	for(int i = 0;i<para.size();++i)
            		sb.append(para.get(i).getText());
            	doc.close();
            	fis.close();
            	content = sb.toString();
            }*/
            if(retrievedDoc.getDocumentType().getDocumentType().compareTo("pdf") == 0){
            	PdfManager pdfManager = new PdfManager();
            	pdfManager.setFilePath(path);
            	content = pdfManager.toText();
            	String[] temp = (new File(path)).getName().split("\\W+");
            	for(String s:temp) content = content + " " + s;
            }
            else if(retrievedDoc.getDocumentType().getDocumentType().compareTo("mp3") == 0 ||  retrievedDoc.getDocumentType().getDocumentType().compareTo("mp4")== 0) {
            	String file = retrievedDoc.getDocumentFilePath();
            	System.out.println("I'm mp3 | mp4.");        		
        		Parser parser = new AutoDetectParser();
        		BodyContentHandler handler = new BodyContentHandler();
        		Metadata metadata = new Metadata();
        		FileInputStream is = new FileInputStream(file);
        		ParseContext context = new ParseContext();
        		parser.parse(is, handler, metadata, context);
        		String fileName = new File(retrievedDoc.getDocumentFilePath()).getName();
        		String[] temp = fileName.split("\\W+");
        		content = handler.toString() + " " + fileName;
        		for(String s:temp) content = content + " " + s;
        		System.out.println(content);        		
            }            
            else {
            	String fileName = new File(retrievedDoc.getDocumentFilePath()).getName();
        		String[] temp = fileName.split("\\W+");        		
            	content = readDoc(retrievedDoc.getDocumentFilePath());
            	for(String s:temp) content = content + " " + s;
            }
            System.out.println("file has - " + content);
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
    
    private String readDoc(String path) {
    	Metadata metadata = new Metadata();
    	File file = new File(path);
    	try (InputStream in = TikaInputStream.get(file, metadata))  {
    	    Tika tika = new Tika();

    	    return tika.parseToString(in, metadata, -1);
    	} catch (IOException | TikaException e) {
    	    return e.getMessage();
    	}
    }
    
}
