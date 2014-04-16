package vu.cornetto.wnlmf;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created with IntelliJ IDEA.
 * User: kyoto
 * Date: 9/3/12
 * Time: 2:13 PM
 * To change this template use File | Settings | File Templates.
 */
public class WordnetLmfSaxParser extends DefaultHandler {

    public HashMap <String, SynsetData> wordnetData;
    public HashMap <String, ArrayList<String>> synsetMap;
    SynsetData synsetData;
    String value = "";
    String gloss = "";
    String sourceId = "";
    String targetId= "";
    String type = "";
    String entry = "";

    public WordnetLmfSaxParser() {
        wordnetData = new HashMap <String, SynsetData>();
        synsetData = new SynsetData();
        synsetMap = new HashMap<String, ArrayList<String>>();
    }


    public void parseFile(String filePath) {
        System.out.println("filePath = " + filePath);
        String myerror = "";
        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            factory.setValidating(false);
            SAXParser parser = factory.newSAXParser();
            InputSource inp = new InputSource (new FileReader(filePath));
            parser.parse(inp, this);
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
            eee.printStackTrace();
            myerror += "\nException --" + eee.getMessage();
            System.out.println("myerror = " + myerror);
        }
        System.out.println("myerror = " + myerror);
    }//--c


    /*
    <LexicalEntry id="clean_out"><Lemma partOfSpeech="v" writtenForm="clean_out" />
<Sense id="clean_out_1" synset="eng-30-00448864-v"><MonolingualExternalRefs><MonolingualExternalRef externalReference="clean_out%2:30:00::" externalSystem="Wordnet3.0" /></MonolingualExternalRefs></Sense>
<Sense id="clean_out_3" synset="eng-30-02314784-v"><MonolingualExternalRefs><MonolingualExternalRef externalReference="clean_out%2:40:00::" externalSystem="Wordnet3.0" /></MonolingualExternalRefs></Sense>
<Sense id="clean_out_2" synset="eng-30-02403408-v"><MonolingualExternalRefs><MonolingualExternalRef externalReference="clean_out%2:41:00::" externalSystem="Wordnet3.0" /></MonolingualExternalRefs></Sense>
</LexicalEntry>

    <Synset baseConcept="1"
    id="eng-30-00194834-r">
    <Definition gloss="in a bewildered manner" />
    <SynsetRelations>
    <SynsetRelation relType="related_to"
    target="eng-30-00000000-n" /></SynsetRelations>
    <MonolingualExternalRefs>
    <MonolingualExternalRef externalReference="dummy" externalSystem="Domain" />
    </MonolingualExternalRefs></Synset>
     */
    public void startElement(String uri, String localName,
                             String qName, Attributes attributes)
            throws SAXException {

     //   System.out.println("qName = " + qName);

        //<Sense id="chickeree_1" synset="eng-30-02357585-n"><MonolingualExternalRefs><MonolingualExternalRef externalReference="chickeree%1:05:00::" externalSystem="Wordnet3.0" /></MonolingualExternalRefs></Sense>

/*        if (qName.equalsIgnoreCase("LexicalEntry")) {
            entry = "";
            synsets = new ArrayList<String>();
            for (int i = 0; i < attributes.getLength(); i++) {
                String name = attributes.getQName(i);
                if (name.equals("writtenForm")) {
                    entry = attributes.getValue(i).trim();
                }
            }
        }
        else */
        if (qName.equalsIgnoreCase("Sense")) {
            entry = "";
            String synsetId = "";
            String senseId = "";
            for (int i = 0; i < attributes.getLength(); i++) {
                String name = attributes.getQName(i);
                if (name.equals("synset")) {
                    synsetId = attributes.getValue(i).trim();
                }
                else if (name.equals("id")) {
                    senseId = attributes.getValue(i).trim();
                }
            }
           // System.out.println("senseId = " + senseId);
           // System.out.println("synsetId = " + synsetId);
            if (!synsetId.isEmpty()) {
                if (synsetMap.containsKey(synsetId)) {
                    ArrayList<String> synsets = synsetMap.get(synsetId);
                    synsets.add(senseId);
                    synsetMap.put(synsetId, synsets);
                }
                else {
                    ArrayList<String> synsets = new ArrayList<String>();
                    synsets.add(senseId);
                    synsetMap.put(synsetId, synsets);
                }
            }
        }
        else
        if (qName.equalsIgnoreCase("Synset")) {
            synsetData = new SynsetData();
            for (int i = 0; i < attributes.getLength(); i++) {
                String name = attributes.getQName(i);
                if (name.equals("id")) {
                    sourceId = attributes.getValue(i).trim();
                }
            }
        }
        else if (qName.equalsIgnoreCase("Definition")) {
            for (int i = 0; i < attributes.getLength(); i++) {
                String name = attributes.getQName(i);
                if (name.equals("gloss")) {
                    gloss = attributes.getValue(i).trim();
                    synsetData.setGloss(gloss);
                }
            }
        }
        else if (qName.equalsIgnoreCase("SynsetRelation")) {
            type = "";
            targetId = "";
            for (int i = 0; i < attributes.getLength(); i++) {
                String name = attributes.getQName(i);
                if (name.equalsIgnoreCase("target")) {
                    targetId = attributes.getValue(i).trim();
                }
                else if (name.equalsIgnoreCase("relType")) {
                    type = attributes.getValue(i).trim();
                }

            }
            if (!type.isEmpty() && !targetId.isEmpty()) {
                WordnetRelation rel = new WordnetRelation(type, targetId);
                synsetData.addRelation(rel);
            }
        }
        else {
        }
    }//--startElement


    public void endElement(String uri, String localName, String qName)
            throws SAXException {


        if (qName.equalsIgnoreCase("Synset")) {
            if ((!sourceId.isEmpty())) {
            //    System.out.println("sourceId = " + sourceId);
                wordnetData.put(sourceId, synsetData);
            }
            sourceId = "";
            gloss = "";
            synsetData = new SynsetData();
        }
    }

    public void characters(char ch[], int start, int length)
            throws SAXException {
        value += new String(ch, start, length);
        // System.out.println("tagValue:"+value);
    }

}
