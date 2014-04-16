package vu.cornetto.pwn;

import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.SAXParseException;
import org.xml.sax.SAXException;
import org.xml.sax.InputSource;
import org.xml.sax.Attributes;

import javax.xml.parsers.SAXParserFactory;
import javax.xml.parsers.SAXParser;
import java.util.HashMap;
import java.util.ArrayList;
import java.io.ByteArrayInputStream;
import java.io.FileReader;

import vu.cornetto.dwn.DwnDomainRelation;
import vu.cornetto.util.FileProcessor;

/**
 * Created by IntelliJ IDEA.
 * User: piek
 * Date: 12-jul-2006
 * Time: 8:01:49
 * To change this template use Options | File Templates.
 */
public class DomainSaxParser extends DefaultHandler {
        private String value;
        private String type;
        private DwnDomainRelation rel;
        public HashMap pwn15Domains;
        public HashMap pwn16Domains;
        public HashMap pwn20Domains;
        public long nLines = 0;

          public DomainSaxParser() {
            initParser();
        }

        public void initParser () {
                pwn15Domains = new HashMap();
                pwn16Domains = new HashMap();
                pwn20Domains = new HashMap();
                value = "";
                type = "";
                nLines = 0;
        }


        public void parseContent(String file, String encoding) {
            String myerror = "";
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
            } catch (SAXException e) {
                Exception x = e;
                if (e.getException() != null)
                    x = e.getException();
                myerror += "\nSAXException --" + x.getMessage();
            } catch (Exception eee) {
                myerror += "\nException --" + eee.getMessage();
            }
        }//--c


        public void parseFile(String filePath, String encoding) {
            String myerror = "";
            try {
                SAXParserFactory factory = SAXParserFactory.newInstance();
                factory.setValidating(false);
                SAXParser parser = factory.newSAXParser();
                InputSource inp = new InputSource (new FileReader (filePath));
                inp.setEncoding(encoding);
                parser.parse(inp, this);
                System.out.println("pwn15Domains = " + pwn15Domains.size());
                System.out.println("pwn16Domains = " + pwn16Domains.size());
                System.out.println("pwn20Domains = " + pwn20Domains.size());
            } catch (SAXParseException err) {
                myerror = "\n** Parsing error" + ", line " + err.getLineNumber()
                        + ", uri " + err.getSystemId();
                myerror += "\n" + err.getMessage();
            } catch (SAXException e) {
                Exception x = e;
                if (e.getException() != null)
                    x = e.getException();
                myerror += "\nSAXException --" + x.getMessage();
            } catch (Exception eee) {
                myerror += "\nException --" + eee.getMessage();
            }
        }//--c

        public void startElement(String uri, String localName,
                                 String qName, Attributes attributes)
                throws SAXException {

           if (qName.equalsIgnoreCase("dom_relation")) {
                //System.out.println("value = " + value);
                rel = new DwnDomainRelation();
                for (int i=0; i<attributes.getLength();i++) {
                    type = attributes.getQName(i);
                    value = attributes.getValue(type);
/*
                System.out.println("value = " + value);
                System.out.println("type = " + type);
*/
                    if (type.equalsIgnoreCase("term")) {
                        rel.setaDomain(value.trim());
                    }
                    if (type.equalsIgnoreCase("target16")) {
                        rel.setPwn16Synset_id(value.trim());
                    }
                    if (type.equalsIgnoreCase("target15")) {
                        rel.setPwn15Synset_id(value.trim());
                    }
                    if (type.equalsIgnoreCase("target20")) {
                        rel.setPwn20Synset_id(value.trim());
                    }
                    if (type.equalsIgnoreCase("name")) {
                        rel.setName(value.trim());
                    }
                    if (type.equalsIgnoreCase("score")) {
                        rel.setScore((new Double(value.trim())).doubleValue());
                    }
                }
                if (pwn15Domains.containsKey(rel.getPwn15Synset_id())) {
                    ArrayList rels = (ArrayList) pwn15Domains.get(rel.getPwn15Synset_id());
                    rels.add(rel);
                    //System.out.println("Added rel = " + rel.getPwn15Synset_id());
                    pwn15Domains.put(rel.getPwn15Synset_id(), rels);
                }
                else {
                    ArrayList rels = new ArrayList();
                    rels.add(rel);
                    //System.out.println("First rel = " + rel.getPwn15Synset_id());
                    pwn15Domains.put(rel.getPwn15Synset_id(), rels);
                }
               if (pwn16Domains.containsKey(rel.getPwn16Synset_id())) {
                   ArrayList rels = (ArrayList) pwn16Domains.get(rel.getPwn16Synset_id());
                   rels.add(rel);
                   //System.out.println("Added rel = " + rel.getPwn15Synset_id());
                   pwn16Domains.put(rel.getPwn16Synset_id(), rels);
               }
               else {
                   ArrayList rels = new ArrayList();
                   rels.add(rel);
                   //System.out.println("First rel = " + rel.getPwn15Synset_id());
                   pwn16Domains.put(rel.getPwn16Synset_id(), rels);
               }
               if (pwn20Domains.containsKey(rel.getPwn20Synset_id())) {
                   ArrayList rels = (ArrayList) pwn20Domains.get(rel.getPwn20Synset_id());
                   rels.add(rel);
                   //System.out.println("Added rel = " + rel.getPwn15Synset_id());
                   pwn20Domains.put(rel.getPwn20Synset_id(), rels);
               }
               else {
                   ArrayList rels = new ArrayList();
                   rels.add(rel);
                   //System.out.println("First rel = " + rel.getPwn15Synset_id());
                   pwn20Domains.put(rel.getPwn20Synset_id(), rels);
               }
            }
            else {
                System.out.println("qName = " + qName);
            }
            nLines++;            
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
