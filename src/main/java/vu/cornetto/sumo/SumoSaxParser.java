package vu.cornetto.sumo;

import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.SAXParseException;
import org.xml.sax.SAXException;
import org.xml.sax.InputSource;
import org.xml.sax.Attributes;

import javax.xml.parsers.SAXParserFactory;
import javax.xml.parsers.SAXParser;
import java.io.ByteArrayInputStream;
import java.io.FileReader;
import java.util.HashMap;
import java.util.ArrayList;

import vu.cornetto.util.FileProcessor;


/**
 * Created by IntelliJ IDEA.
 * User: piek
 * Date: 10-jul-2006
 * Time: 22:15:38
 * To change this template use Options | File Templates.
 */
public class SumoSaxParser extends DefaultHandler {
    private String value;
    private String type;
    private SumoRelation rel;
    public HashMap pwn15Sumo;
    public HashMap pwn16Sumo;
    public HashMap pwn20Sumo;
    public long nLines = 0;
      public SumoSaxParser() {
        initParser();
    }

    public void initParser () {
            pwn15Sumo = new HashMap();
            pwn16Sumo = new HashMap();
            pwn20Sumo = new HashMap();
            value = "";
            type = "";
    }


    public void parseContent(String file, String encoding) {
        String myerror = "";
        nLines = 0;
        try {
            String xmlContent = FileProcessor.ReadFileToString(file, encoding);
            value = "";
            SAXParserFactory factory = SAXParserFactory.newInstance();
            factory.setValidating(false);
            SAXParser parser = factory.newSAXParser();
            parser.parse(new ByteArrayInputStream(xmlContent.getBytes()), this);
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
    }//--c


    public void parseFile(String filePath, String encoding) {
        String myerror = "";
        nLines = 0;
        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            factory.setValidating(false);
            SAXParser parser = factory.newSAXParser();
            InputSource inp = new InputSource (new FileReader (filePath));
            inp.setEncoding(encoding);
            parser.parse(inp, this);
            System.out.println("last rel = " + rel.getArg2());
            System.out.println("last rel = " + rel.getPwn15Synset_id());
            System.out.println("last rel = " + rel.getPwn16Synset_id());
            System.out.println("last rel = " + rel.getPwn20Synset_id());
            System.out.println("pwn15Sumo = " + pwn15Sumo.size());
            System.out.println("pwn16Sumo = " + pwn16Sumo.size());
            System.out.println("pwn20Sumo = " + pwn20Sumo.size());
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
        System.out.println("nLines = " + nLines);
    }//--c

    public void startElement(String uri, String localName,
                             String qName, Attributes attributes)
            throws SAXException {

       nLines++;
       if (qName.equalsIgnoreCase("ont_relation")) {
            //System.out.println("value = " + value);
            rel = new SumoRelation ();
            for (int i=0; i<attributes.getLength();i++) {
                type = attributes.getQName(i);
                value = attributes.getValue(type);
/*
                System.out.println("value = " + value);
                System.out.println("type = " + type);
*/
                if (type.equalsIgnoreCase("type")) {
                    rel.setRelation(value.trim());
                }
                if (type.equalsIgnoreCase("target20")) {
                    rel.setPwn20Synset_id(value.trim());
                }
                if (type.equalsIgnoreCase("target15")) {
                    rel.setPwn15Synset_id(value.trim());
                }
                if (type.equalsIgnoreCase("target16")) {
                    rel.setPwn16Synset_id(value.trim());
                }
                if (type.equalsIgnoreCase("target20")) {
                    rel.setPwn20Synset_id(value.trim());
                }
                if (type.equalsIgnoreCase("term")) {
                    rel.setArg2(value.trim());
                }
                if (type.equalsIgnoreCase("name")) {
                    rel.setName(value.trim());
                }
                if (type.equalsIgnoreCase("score")) {
                    rel.setScore((new Double(value)).doubleValue());
                }
            }
/*
           if (rel.getPwn15Synset_id().equals("ENG15-02242147-n")) {
               System.out.println("rel.getPwn15Synset_id() = " + rel.getPwn15Synset_id());
               System.out.println("rel.getPwn15Synset_id() = " + rel.getPwn20Synset_id());
           }
           if (rel.getArg2().equals("Certificate")) {
               System.out.println("rel.getArg2() = " + rel.getArg2());
               System.out.println("rel.getPwn15Synset_id() = " + rel.getPwn15Synset_id());
               System.out.println("rel.getPwn16Synset_id() = " + rel.getPwn16Synset_id());
               System.out.println("rel.getPwn20Synset_id() = " + rel.getPwn20Synset_id());

           }
*/
           if (rel.getPwn15Synset_id().length()>0) {
               if (pwn15Sumo.containsKey(rel.getPwn15Synset_id())) {
                    ArrayList rels = (ArrayList) pwn15Sumo.get(rel.getPwn15Synset_id());
                    rels.add(rel);
                    //System.out.println("Added rel = " + rel.getPwn15Synset_id());
                    pwn15Sumo.put(rel.getPwn15Synset_id(), rels);
               }
               else {
                    ArrayList rels = new ArrayList();
                    rels.add(rel);
                    //System.out.println("First rel = " + rel.getPwn15Synset_id());
                    pwn15Sumo.put(rel.getPwn15Synset_id(), rels);
               }
            }
/*
            if (rel.getPwn16Synset_id().equals("ENG16-05021464-n")) {
                System.out.println("rel.getPwn16Synset_id() = " + rel.getPwn16Synset_id());
                System.out.println("rel.getPwn20Synset_id() = " + rel.getPwn20Synset_id());
            }
*/
           if (rel.getPwn16Synset_id().length()>0) {
               if (pwn16Sumo.containsKey(rel.getPwn16Synset_id())) {
                   ArrayList rels = (ArrayList) pwn16Sumo.get(rel.getPwn16Synset_id());
                   rels.add(rel);
                   //System.out.println("Added rel = " + rel.getPwn15Synset_id());
                   pwn16Sumo.put(rel.getPwn16Synset_id(), rels);
               }
               else {
                   ArrayList rels = new ArrayList();
                   rels.add(rel);
                   //System.out.println("First rel = " + rel.getPwn15Synset_id());
                   pwn16Sumo.put(rel.getPwn16Synset_id(), rels);
               }
           }
           if (rel.getPwn20Synset_id().length()>0) {
               if (pwn20Sumo.containsKey(rel.getPwn20Synset_id())) {
                   ArrayList rels = (ArrayList) pwn20Sumo.get(rel.getPwn20Synset_id());
                   rels.add(rel);
                   //System.out.println("Added rel = " + rel.getPwn15Synset_id());
                   pwn20Sumo.put(rel.getPwn20Synset_id(), rels);
               }
               else {
                   ArrayList rels = new ArrayList();
                   rels.add(rel);
                   //System.out.println("First rel = " + rel.getPwn15Synset_id());
                   pwn20Sumo.put(rel.getPwn20Synset_id(), rels);
               }
           }
        }
        else {
            System.out.println("qName = " + qName);
        }
        value = "";
    }//--startElement

    public void endElement(String uri, String localName, String qName)
            throws SAXException {
    }

    public void characters(char ch[], int start, int length)
            throws SAXException {
        value += new String(ch, start, length);
        //System.out.println("tagValue:"+tagValue);
    }

}
