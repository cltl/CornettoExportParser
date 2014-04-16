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
public class CdbCidArraySaxParser extends DefaultHandler {
    long nounLU;
    long verbLU;
    long adjLU;
    long advLU;
    long otherLU;
        

        public boolean bigLog = false;
        public HashMap statusMap;
        public HashMap nameMap;
        private String value;
        private String type;
        private CdbCid cid;
        public HashMap cidLuMap;
        public HashMap cidSyMap;
        public HashMap formMap;
        public HashMap entryMap;
        long recordCount;
        int nManual = 0;
        int nSelected = 0;
        int nName = 0;
        int dwnOnly = 0;
        int rbnOnly = 0;
        int nStatus = 0;
        int dwnAndRbn = 0;


          public CdbCidArraySaxParser() {
            initParser();
        }

        public void initParser () {
                bigLog = false;
                cidLuMap = new HashMap();
                cidSyMap = new HashMap();
                formMap = new HashMap();
                entryMap = new HashMap();
                value = "";
                type = "";
                recordCount = 0;
                statusMap = new HashMap();
                nameMap = new HashMap();
            nounLU=0;
            verbLU=0;
            adjLU=0;
            advLU=0;
            otherLU=0;


        }



/*
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
                printStats();
            } catch (SAXParseException err) {
                myerror = "\n** Parsing error" + ", line " + err.getLineNumber()
                        + ", uri " + err.getSystemId();
                myerror += "\n" + err.getMessage();
                System.out.println("myerror = " + myerror);
                printStats();
            } catch (SAXException e) {
                Exception x = e;
                if (e.getException() != null)
                    x = e.getException();
                myerror += "\nSAXException --" + x.getMessage();
                System.out.println("myerror = " + myerror);
                printStats();
            } catch (Exception eee) {
                myerror += "\nException --" + eee.getMessage();
                System.out.println("myerror = " + myerror);
                System.out.println("recordCount = " + recordCount);
            }
            System.out.println("myerror = " + myerror);
        }//--c

*/
        public long parseFile(String filePath) {
            System.out.println("filePath = " + filePath);
            String myerror = "";
            try {
                SAXParserFactory factory = SAXParserFactory.newInstance();
                factory.setValidating(false);
                SAXParser parser = factory.newSAXParser();
                InputSource inp = new InputSource (new FileReader (filePath));
                parser.parse(inp, this);
                System.out.println("Total recordCount = " + recordCount);
                printStats();
            } catch (SAXParseException err) {
                myerror = "\n** Parsing error" + ", line " + err.getLineNumber()
                        + ", uri " + err.getSystemId();
                myerror += "\n" + err.getMessage();
                System.out.println("myerror = " + myerror);
                printStats();
            } catch (SAXException e) {
                Exception x = e;
                if (e.getException() != null)
                    x = e.getException();
                myerror += "\nSAXException --" + x.getMessage();
                System.out.println("myerror = " + myerror);
                printStats();
            } catch (Exception eee) {
                eee.printStackTrace();
                myerror += "\nException --" + eee.getMessage();
                System.out.println("myerror = " + myerror);
                System.out.println("recordCount = " + recordCount);
            }
            System.out.println("myerror = " + myerror);
            return recordCount;
        }//--c


        public long parseFileAsBytes(String filePath) {
            String myerror = "";
            try {
                byte [] buffer = FileProcessor.ReadFileToBytes (filePath);
                SAXParserFactory factory = SAXParserFactory.newInstance();
                factory.setValidating(false);
                SAXParser parser = factory.newSAXParser();
                parser.parse(new ByteArrayInputStream(buffer), this);
                printStats();
            } catch (SAXParseException err) {
                myerror = "\n** Parsing error" + ", line " + err.getLineNumber()
                        + ", uri " + err.getSystemId();
                myerror += "\n" + err.getMessage();
                System.out.println("myerror = " + myerror);
                printStats();
            } catch (SAXException e) {
                Exception x = e;
                if (e.getException() != null)
                    x = e.getException();
                myerror += "\nSAXException --" + x.getMessage();
                System.out.println("myerror = " + myerror);
                System.out.println("recordCount = " + recordCount);
            } catch (Exception eee) {
                myerror += "\nException --" + eee.getMessage();
                System.out.println("myerror = " + myerror);
                System.out.println("recordCount = " + recordCount);
            }
            System.out.println("myerror = " + myerror);
            return recordCount;
        }//--c

    

        void printStats () {
            System.out.println("recordCount\t" + recordCount);
            System.out.println("nManual\t" + nManual);
            System.out.println("nName\t" + nName);
            System.out.println("nSelected\t" + nSelected);
            System.out.println("dwnAndRdn\t" +dwnAndRbn);
            System.out.println("dwnOnly\t" + dwnOnly);
            System.out.println("rbnOnly\t" + rbnOnly);
            System.out.println("nStatus\t" + nStatus);
            System.out.println("\tAll\tNouns\tVerbs\tAdjectives\tAdverbs\tOthers");
            System.out.println("Cid records\t" + (nounLU+ verbLU+ adjLU+ advLU+ otherLU) +"\t"+nounLU+"\t"+verbLU+"\t"+adjLU+"\t"+advLU+"\t"+otherLU);

        }

        public void startElement(String uri, String localName,
                                 String qName, Attributes attributes)
                throws SAXException {

          // System.out.println("qName = " + qName);
           if (qName.equalsIgnoreCase("cid")) {
                recordCount++;
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
                }
                if (cid.getC_lu_id().length()==0) {
                    System.out.println("EMPTY LU FOR CID RECORD:");
                    System.out.println("cid:="+cid.toXmlString());
                }
                else {
                    if ((cid.getScore()==-1) && (cid.getStatus().equals("D-75"))) {
                        cid.setStatus("");                        
                    }
                    if ((cid.getScore()==-1) && (cid.getStatus().equals("D-58"))) {
                        cid.setStatus("");
                    }
                    if ((cid.getScore()==-1) && (cid.getStatus().equals("D-55"))) {
                        cid.setStatus("");
                    }
                    if (cid.getC_pos().toLowerCase().equals("noun")) {
                        nounLU++;
                    }
                    else if (cid.getC_pos().toLowerCase().equals("verb")) {
                        verbLU++;
                    }
                    else if (cid.getC_pos().toLowerCase().equals("adj")) {
                        adjLU++;
                    }
                    else if (cid.getC_pos().toLowerCase().equals("adverb")) {
                        advLU++;
                    }
                    else {
                        otherLU++;
                    }
                    String statusName = cid.getStatus()+":"+cid.getC_pos();
                    if (statusMap.containsKey(statusName)) {
                        Integer count = (Integer) statusMap.get(statusName);
                        count = new Integer (count.intValue()+1);
                        statusMap.put(statusName, count);
                    }
                    else {
                        Integer count = new Integer(1);
                        statusMap.put(statusName, count);
                    }
                    
/*
                    if (statusMap.containsKey(cid.getStatus())) {
                        Integer count = (Integer) statusMap.get(cid.getStatus());
                        count = new Integer (count.intValue()+1);
                        statusMap.put(cid.getStatus(), count);
                    }
                    else {
                        Integer count = new Integer(1);
                        statusMap.put(cid.getStatus(), count);
                    }
 */
                    if (nameMap.containsKey(cid.getName())) {
                        Integer count = (Integer) nameMap.get(cid.getName());
                        count = new Integer (count.intValue()+1);
                        nameMap.put(cid.getName(), count);
                    }
                    else {
                        Integer count = new Integer(1);
                        nameMap.put(cid.getName(), count);
                    }
                    if (cid.getName().length()>0) {
                        nName++;
                    }
                    if (cid.getStatus().length()>0) {
                        this.nManual++;
                    }
                    if (cid.getSelected()) {
                        this.nSelected++;
                    }
                    if (cid.getR_lu_id().equals("")) {
                        if (!cid.getD_lu_id().equals("")) {
                            this.dwnOnly++;
                        }
                        else {
                            ////// both -1 is not possible
                        }
                    }
                    else {
                        if (cid.getD_lu_id().equals("")) {
                            this.rbnOnly++;
                        }
                        else {
                            this.dwnAndRbn++;
                        }
                    }
                    if (cid.getStatus().length()>0) {
                        this.nStatus++;
                    }
                    ///// LUMAP
                    if (cidLuMap.containsKey(cid.getC_lu_id())) {
                        ArrayList cids = (ArrayList) cidLuMap.get(cid.getC_lu_id());
                        cids.add(cid);
                        //System.out.println("cids.size()+\" for:\"+cid.getC_lu_id() = " + cids.size()+" for:"+cid.getC_lu_id());
                        cidLuMap.put(cid.getC_lu_id(), cids);
                    }
                   else {
                        ArrayList cids = new ArrayList();
                        cids.add(cid);
                        cidLuMap.put(cid.getC_lu_id(), cids);
                    }
                    if (entryMap.containsKey(cid.getC_form())) {
                        ArrayList cids = (ArrayList) entryMap.get(cid.getC_form());
                        cids.add(cid);
                        //System.out.println("cids.size()+\" for:\"+cid.getC_lu_id() = " + cids.size()+" for:"+cid.getC_lu_id());
                        entryMap.put(cid.getC_form(), cids);
                    }
                    else {
                        ArrayList cids = new ArrayList();
                        cids.add(cid);
                        entryMap.put(cid.getC_form(), cids);
                    }

                    ///// SYNSETMAP
                    if (cidSyMap.containsKey(cid.getD_sy_id())) {
                        ArrayList cids = (ArrayList) cidSyMap.get(cid.getD_sy_id());
                        cids.add(cid);
                        cidSyMap.put(cid.getD_sy_id(), cids);
                    }
                   else {
                        ArrayList cids = new ArrayList();
                        cids.add(cid);
                        cidSyMap.put(cid.getD_sy_id(), cids);
                    }

                    //// FORM + POS MAP
                    String key = cid.getC_form()+"_"+cid.getC_pos();
                    if (formMap.containsKey(key)) {
                        ArrayList cids = (ArrayList) formMap.get(key);
                        cids.add(cid);
                        formMap.put(key, cids);
                    }
                   else {
                        ArrayList cids = new ArrayList();
                        cids.add(cid);
                        formMap.put(key, cids);
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
           // System.out.println("tagValue:"+value);
        }

}