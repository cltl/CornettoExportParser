package vu.cornetto.cdb;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;
import vu.cornetto.util.FileProcessor;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.ByteArrayInputStream;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by IntelliJ IDEA.
 * User: piek
 * Date: 25-jul-2006
 * Time: 18:01:55
 * To change this template use Options | File Templates.
 */
public class CdbCidSaxParser extends DefaultHandler {


        private String value;
        private String type;
        private CdbCid cid;
        public HashMap <String, ArrayList<CdbCid>> editorCidMap;

          public CdbCidSaxParser() {
            initParser();
        }

        public void initParser () {
            editorCidMap = new HashMap<String, ArrayList<CdbCid>>();
                value = "";
                type = "";
        }



        public void parseFile(String filePath) {
            System.out.println("filePath = " + filePath);
            String myerror = "";
            try {
                SAXParserFactory factory = SAXParserFactory.newInstance();
                factory.setValidating(false);
                SAXParser parser = factory.newSAXParser();
                InputSource inp = new InputSource (new FileReader (filePath));
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


        public void parseFileAsBytes(String filePath) {
            String myerror = "";
            try {
                byte [] buffer = FileProcessor.ReadFileToBytes (filePath);
                SAXParserFactory factory = SAXParserFactory.newInstance();
                factory.setValidating(false);
                SAXParser parser = factory.newSAXParser();
                parser.parse(new ByteArrayInputStream(buffer), this);
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

    



        public void startElement(String uri, String localName,
                                 String qName, Attributes attributes)
                throws SAXException {
/*
<cid
cid_id="1"              x
form="'s-Hertogenbosch" x
seq_nr="1"              x
c_lu_id="r_n-5616"      x
pos="NOUN"              x
d_lu_id="d_n-258853"    x
d_seq_nr="1"            x
d_sy_id="n_n-500002"    x
r_lu_id="n-5616"        x
r_seq_nr="1"/>          x
score="60.2"            x
name=""                 x
status=""               x
date=""                 x
selected="true"         x
 */
          // System.out.println("qName = " + qName);
           if (qName.equalsIgnoreCase("cid")) {
                cid = new CdbCid();
                for (int i=0; i<attributes.getLength();i++) {
                    type = attributes.getQName(i);
                    //System.out.println("type = " + type);
                    value = attributes.getValue(type);
                    //System.out.println("value = " + value);
                    if (type.equalsIgnoreCase("cid_id")) {
                        cid.setCid(value.trim());
                    }
                    else if (type.equalsIgnoreCase("c_lu_id")) {
                        cid.setC_lu_id(value.trim());
                    }
                    else if (type.equalsIgnoreCase("c_sy_id")) {
                        cid.setC_sy_id(value.trim());
                    }
                    else if (type.equalsIgnoreCase("form")) {
                        cid.setC_form(value.trim());
                    }
                    else if (type.equalsIgnoreCase("pos")) {
                        cid.setC_pos(value.trim());
                    }
                    else if (type.equalsIgnoreCase("date")) {
                        cid.setDate(value.trim());
                    }
                    else if (type.equalsIgnoreCase("name")) {
                        cid.setName(value.trim());
                    }
                    else if (type.equalsIgnoreCase("status")) {
                        cid.setStatus(value.trim());
                    }
                    else if (type.equalsIgnoreCase("d_lu_id")) {
                        cid.setD_lu_id(value.trim());
                    }
                    else if (type.equalsIgnoreCase("r_lu_id")) {
                        cid.setR_lu_id(value.trim());
                    }
                    else if (type.equalsIgnoreCase("d_sy_id")) {
                        cid.setD_sy_id(value.trim());
                    }

                    else if (type.equalsIgnoreCase("score")) {
                        try {
                            cid.setScore(Double.parseDouble (value.trim()));
                        } catch (NumberFormatException e) {
                            //e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                        }
                    }
                    else if (type.equalsIgnoreCase("selected")) {
                        if (value.trim().equals("true")) {
                            cid.setSelected(true);
                        }
                        else {
                            cid.setSelected(false);
                        }
                    }
                    else if (type.equalsIgnoreCase("seq_nr")) {
                        try {
                            cid.setC_seq_nr(Integer.parseInt(value.trim()));
                        } catch (NumberFormatException e) {
                            //e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                        }
                    }
                    else if (type.equalsIgnoreCase("d_seq_nr")) {
                        try {
                            cid.setD_seq_nr(Integer.parseInt(value.trim()));
                        } catch (NumberFormatException e) {
                            //e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                        }
                    }
                    else if (type.equalsIgnoreCase("r_seq_nr")) {
                        try {
                            cid.setR_seq_nr(Integer.parseInt(value.trim()));
                        } catch (NumberFormatException e) {
                            //e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                        }
                    }

                }
                if (editorCidMap.containsKey(cid.getName())) {
                    ArrayList<CdbCid> cids = editorCidMap.get(cid.getName());
                    cids.add(cid);
                    editorCidMap.put(cid.getName(), cids);                    
                }
                else {
                    ArrayList<CdbCid> cids = new ArrayList<CdbCid>();
                    cids.add(cid);
                    editorCidMap.put(cid.getName(), cids);
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
           // System.out.println("tagValue:"+value);
        }

}