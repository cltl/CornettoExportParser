package vu.cornetto.dwn;

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

import vu.cornetto.util.FileProcessor;

/**
 * Created by IntelliJ IDEA.
 * User: piek
 * Date: 4-sep-2006
 * Time: 21:49:29
 * To change this template use Options | File Templates.
 */
public class DwnIdSaxParser extends DefaultHandler {
        private String value;
        private String type;
        private DwnId id;
        public HashMap idMap;
        public HashMap vlisIdMap;
        public HashMap vlisSynsetIdMap;
        long cnt = 0;

          public DwnIdSaxParser() {
            initParser();
        }

        public void initParser () {
                idMap = new HashMap();
                vlisIdMap = new HashMap();
                vlisSynsetIdMap = new HashMap();
                value = "";
                type = "";
                cnt = 0;
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
                System.out.println("last value = " + value);
                System.out.println("last type = " + type);
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
            String myerror = "";
            try {
                SAXParserFactory factory = SAXParserFactory.newInstance();
                factory.setValidating(false);
                SAXParser parser = factory.newSAXParser();
                InputSource inp = new InputSource (new FileReader (filePath));
                inp.setEncoding(encoding);
                System.out.println("encoding = " + encoding);
                parser.parse(inp, this);
                System.out.println("last value = " + value);
                System.out.println("last type = " + type);
                System.out.println("read nr lines cnt = " + cnt);
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

        public void parseFile(String filePath) {
            String myerror = "";
            try {
                SAXParserFactory factory = SAXParserFactory.newInstance();
                factory.setValidating(false);
                SAXParser parser = factory.newSAXParser();
                InputSource inp = new InputSource (new FileReader (filePath));
                parser.parse(inp, this);
                System.out.println("last value = " + value);
                System.out.println("last type = " + type);
                System.out.println("read nr lines cnt = " + cnt);
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

        public void startElement(String uri, String localName,
                                 String qName, Attributes attributes)
                throws SAXException {

//<dwnId form="'s-Gravenhage" seq_nr="1" pos="NOUN" vlisId="258792" synsetId="n_500001"/>

           if (qName.equalsIgnoreCase("dwnId")) {
                //System.out.println("value = " + value);
                id = new DwnId();
                for (int i=0; i<attributes.getLength();i++) {
                    type = attributes.getQName(i);
                    value = attributes.getValue(type);
                    if (type.equalsIgnoreCase("form")) {
                        id.setForm(value.trim());
                    }
                    if (type.equalsIgnoreCase("seq_nr")) {
                        id.setSeqNr(value.trim());
                    }
                    if (type.equalsIgnoreCase("pos")) {
                        id.setPos(value.trim());
                    }
                    if (type.equalsIgnoreCase("vlisId")) {
                        id.setVlisLuId(value.trim());
                    }
                    if (type.equalsIgnoreCase("synsetId")) {
                        id.setDwnSynsetId(value.trim());
                    }
                }
                //String key = id.getForm()+"_"+id.getPos()+"_"+id.getSeqNr();
                 //Form + seq is unique in DWN
                 // Selections are now specified as form_seq_nr, therefore this needs to be the key
                String key = id.getForm()+"_"+id.getSeqNr();
                if (id.getForm().startsWith("Abessini")) {
                    System.out.println("READING THE VLISID key = " + key);
                }
                if (id.getForm().startsWith("Ari")) {
                   System.out.println("READING THE VLISID key = " + key);
                }
                if (id.getForm().indexOf("caf")!=-1) {
                   System.out.println("READING THE VLISID key = " + key);
                }
                cnt++;
                idMap.put(key, id);
                vlisIdMap.put(id.getVlisLuId(), id);
               if (vlisSynsetIdMap.containsKey(id.getDwnSynsetId())) {
                   ArrayList ids = (ArrayList) vlisSynsetIdMap.get(id.getDwnSynsetId());
                   ids.add(id);
                   vlisSynsetIdMap.put(id.getDwnSynsetId(), ids);
               }
               else {
                   ArrayList ids = new ArrayList();
                   ids.add(id);
                   vlisSynsetIdMap.put(id.getDwnSynsetId(), ids);
               }
            }
            else {
                //System.out.println("qName = " + qName);
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
