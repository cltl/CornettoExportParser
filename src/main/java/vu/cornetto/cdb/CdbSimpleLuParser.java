package vu.cornetto.cdb;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;
import vu.cornetto.util.FileProcessor;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;


/**
 * Created by IntelliJ IDEA.
 * User: Piek Vossen
 * Date: 11-jul-2007
 * Time: 18:01:13
 * To change this template use File | Settings | File Templates.
 */
public class CdbSimpleLuParser extends DefaultHandler {
    long nTags;
    long nAttrributes;
    HashMap tagCount;
    String value = "";
    public HashMap data;
    //public ArrayList dataList;
    final String header = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + "<cdb>";

    void printTagCounts () {
        Set keySet = tagCount.keySet();
        Iterator keys = keySet.iterator();
        while (keys.hasNext()) {
            String key = (String) keys.next();
            Long l = (Long) tagCount.get(key);
            System.out.println(key+":"+l);
        }
    }

    void initCounts () {
        nTags = 0;
        nAttrributes = 0;
    }

    void printCounts () {
        System.out.println("nTags = " + nTags);
        System.out.println("nAttrributes = " + nAttrributes);
    }

    public void parseContent(String file, String encoding) {
        initCounts();
        String myerror = "";
        data = new HashMap();
        tagCount = new HashMap();
        try {
            String xmlContent = FileProcessor.ReadFileToString(file, encoding);
            value = "";
            SAXParserFactory factory = SAXParserFactory.newInstance();
            factory.setValidating(false);
            SAXParser parser = factory.newSAXParser();
            parser.parse(new ByteArrayInputStream(xmlContent.getBytes(encoding)), this);
            System.out.println("imported lu data = " + data.size());
            printCounts();
            printTagCounts();
        } catch (SAXParseException err) {
            myerror = "\n** Parsing error" + ", line " + err.getLineNumber()
                    + ", uri " + err.getSystemId();
            myerror += "\n" + err.getMessage();
            System.out.println("myerror = " + myerror);
        } catch (SAXException e) {
            Exception x = e;
            if (e.getException() != null)
                x = e.getException();
            myerror += "\nSAXException --" + x.getMessage();
            System.out.println("myerror = " + myerror);
        } catch (Exception eee) {
            myerror += "\nException --" + eee.getMessage();
            System.out.println("myerror = " + myerror);
        }
    }//--c


    public void parseFile(String filePath, String encoding) {
        initCounts();
        String myerror = "";
        data = new HashMap();
        tagCount = new HashMap();
        //dataList = new ArrayList();
        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            factory.setValidating(false);
            SAXParser parser = factory.newSAXParser();
            InputSource inp = new InputSource (new FileReader(filePath));
            inp.setEncoding(encoding);
            System.out.println("encoding = " + encoding);
            parser.parse(inp, this);
            System.out.println("imported lu data = " + data.size());
            printCounts();
            printTagCounts();
        } catch (SAXParseException err) {
            myerror = "\n** Parsing error" + ", line " + err.getLineNumber()
                    + ", uri " + err.getSystemId();
            myerror += "\n" + err.getMessage();
            System.out.println("myerror = " + myerror);
            err.printStackTrace();
        } catch (SAXException e) {
            Exception x = e;
            if (e.getException() != null)
                x = e.getException();
            myerror += "\nSAXException --" + x.getMessage();
            System.out.println("myerror = " + myerror);
            e.printStackTrace();
        } catch (Exception eee) {
            myerror += "\nException --" + eee.getMessage();
            System.out.println("myerror = " + myerror);
            eee.printStackTrace();
        }
        System.out.println("myerror = " + myerror);
    }//--c

    public void parseFile(String filePath) {
        String myerror = "";
        data = new HashMap();
        tagCount = new HashMap();
        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            factory.setValidating(false);
            SAXParser parser = factory.newSAXParser();
            InputSource inp = new InputSource (new FileReader (filePath));
            parser.parse(inp, this);
            System.out.println("imported lu data = " + data.size());
        } catch (SAXParseException err) {
            myerror = "\n** Parsing error" + ", line " + err.getLineNumber()
                    + ", uri " + err.getSystemId();
            myerror += "\n" + err.getMessage();
            System.out.println("myerror = " + myerror);
        } catch (SAXException e) {
            Exception x = e;
            if (e.getException() != null)
                x = e.getException();
            myerror += "\nSAXException --" + x.getMessage();
            System.out.println("myerror = " + myerror);
        } catch (Exception eee) {
            myerror += "\nException --" + eee.getMessage();
            System.out.println("myerror = " + myerror);
        }
        System.out.println("myerror = " + myerror);
    }//--c

/*
<cdb_lu c_seq_nr="3" type="swu" is_complete="true" c_lu_id="c_545144">
<syntax_noun>
    <sy-article>de</sy-article>
    <sy-number/>
    <sy-gender>m/f</sy-gender>
    <sy-complementation/>
</syntax_noun>
<pragmatics>
    <prag-origin/>
    <prag-chronology/>
    <prag-subj-gen/>
    <prag-style/>
    <prag-frequency/>
    <prag-geography/>
    <prag-socGroup/>
    <prag-connotation>offens</prag-connotation>
    <prag-domain general="false" subjectfield=""/>
</pragmatics>
<examples>
<example r_ex_id="c_545144-1">
    <semantics_example>
        <sem-gc-compl/>
        <sem-lc-collocator/>
        <sem-spec-collocator/>
        <sem-gc-gramword/>
        <sem-meaningdescription>een vervelend of lelijk mens</sem-meaningdescription>
        <sem-subtype-argument/>
        </semantics_example>
        <syntax_example>
            <sy-subtype>idiom</sy-subtype>
            <sy-combi/>
            <sy-type>fixed</sy-type>
        </syntax_example>
        <form_example>
            <category>np</category>
            <canonicalform>een draak van een vrouw</canonicalform>
            <textualform/>
            <text-category/>
        </form_example>
    </example>
</examples>
<semantics_noun>
    <sem-type>human</sem-type>
    <sem-reference>common</sem-reference>
    <sem-subclass>persnm kwalificerend</sem-subclass>
    <sem-selrestriction/>
    <sem-countability>count</sem-countability>
    <sem-shift/>
    <sem-definition>
    <sem-resume>pinnige of lelijke vrouw</sem-resume>
    </sem-definition>
</semantics_noun>
<morphology_noun>
    <morpho-type>simpmorph</morpho-type>
    <morpho-plurforms>
    <morpho-plurform>draken</morpho-plurform>
    </morpho-plurforms>
    <morpho-structure/>
</morphology_noun>
<form form-length="" form-cat="noun" form-spelling="draak" form-spelvar=""/>
<sem-definition>
    <sem-defSource>XVD</sem-defSource>
    <sem-def-noun>
    <sem-genus>iemand</sem-genus>
    <sem-specificae>die heel vervelend of lelijk is</sem-specificae>
    </sem-def-noun>
</sem-definition>
</cdb_lu>
 */


    public void startElement(String uri, String localName,
                             String qName, Attributes attributes)
            throws SAXException {
       //System.out.println("qName = " + qName);
        if (tagCount.containsKey(qName)) {
            Long n = (Long) tagCount.get(qName);
            long l = n.longValue();
            l++;
            tagCount.put(qName, new Long(l));
        }
        else {
            tagCount.put(qName, new Long(1));
        }
        this.nTags++;
        this.nAttrributes += attributes.getLength();
        if (qName.equalsIgnoreCase("sem-resume")) {
            parserCdbSynset(attributes);
        }
        else if (qName.equalsIgnoreCase("synonym")) {
            this.parserSynonyms(attributes);
        }
        else if (qName.equalsIgnoreCase("wn_equivalence_relations")) {
        }
        value = "";
    }//--startElement

    public void endElement(String uri, String localName, String qName)
            throws SAXException {
        if (qName.equals("cdb_synset")) {
            //data.put(cdbSynset.getC_sy_id(), cdbSynset);
        }

        else if (qName.equals("definition")) {
        }
        else if (qName.equals("differentiae")) {
        }
        else if (qName.equals("base_concept")) {
        }
    }

    public void characters(char ch[], int start, int length)
            throws SAXException {
        value += new String(ch, start, length);
       // System.out.println("tagValue:"+value);
    }


    void parserCdbSynset (Attributes attributes) {
        for (int i=0; i<attributes.getLength();i++) {
            String type = attributes.getQName(i);
            String value = attributes.getValue(type);
            if (type.equals("c_sy_id")) {
            }
            if (type.equals("d_synset_id")) {
            }
            if (type.equals("posSpecific")) {
            }
            if (type.equals("comment")) {
            }
        }
    }

    void parserSynonyms (Attributes attributes) {
        Syn syn = new Syn();
        for (int i=0; i<attributes.getLength();i++) {
            String type = attributes.getQName(i);
            String value = attributes.getValue(type);
            if (type.equals("c_lu_id")) {
                syn.setC_lu_id(value);
            }
            if (type.equals("c_cid_id")) {
                syn.setC_cid_id(value);;
            }
            if (type.equals("c_lu_id-previewtext")) {
                syn.setPreviewtext(value);
            }
            if (type.equals("status")) {
                syn.setStatus(value);
            }
        }
    }

        ///* This function reads a synset file and stores CdbSynset objects in a hashmap
        ///
        public static void main(String[] args) throws IOException {
            // ENCODING = UTF8;
            try {

                String cdbSynsetFile = args[0];
                FileOutputStream importOutFile = new FileOutputStream(cdbSynsetFile+".imp.xml");
                OutputStreamWriter synsetOut = new OutputStreamWriter(importOutFile, "UTF-8");
                String str = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                        "\n" +
                        "<cdb_lus>\n";
                FileProcessor.storeResult(synsetOut, str);
                CdbSynsetParser cdbSimpleLuParser = new CdbSynsetParser();
               // cdbSimpleLuParser.parseFile(cdbSynsetFile, "UTF-8");
                //cdbSimpleLuParser.parseFileAsBytes(cdbSynsetFile);
                cdbSimpleLuParser.parseFile(cdbSynsetFile);

                Set keySet = cdbSimpleLuParser.synsetIdSynsetMap.keySet();
                Iterator keys = keySet.iterator();
                while (keys.hasNext()) {
                    String key = (String) keys.next();
                    CdbSynset synset = (CdbSynset) cdbSimpleLuParser.synsetIdSynsetMap.get(key);
                    str = synset.toString();
                    FileProcessor.storeResult(synsetOut, str);
                }
                str = "</cdb_lus>\n";
                FileProcessor.storeResult(synsetOut, str);
                synsetOut.flush();
                synsetOut.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }
}