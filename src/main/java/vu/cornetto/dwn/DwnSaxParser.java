package vu.cornetto.dwn;

import org.xml.sax.SAXParseException;
import org.xml.sax.SAXException;
import org.xml.sax.InputSource;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.SAXParserFactory;
import javax.xml.parsers.SAXParser;
import java.io.ByteArrayInputStream;
import java.io.FileReader;
import java.util.HashMap;

/**
 * Created by IntelliJ IDEA.
 * User: piek
 * Date: 23-mrt-2006
 * Time: 18:36:08
 * To change this template use Options | File Templates.
 */
public class DwnSaxParser  extends DefaultHandler {

    private String value;
    public HashMap DWN;
    private String aLiteral;
    private int aSense;
    private String rType;
    private String target;
    private Synonym aSyn;
    private DwnEquivalenceRelation eRel;
    private DwnInternalRelation iRel;
    private DwnDomainRelation dRel;
    private DwnSynset dwnSyn;


    public void parseContent(String xmlContent) {
        String myerror = "";
        DWN = new HashMap();
        dwnSyn = new DwnSynset();
        try {
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
        DWN = new HashMap();
        dwnSyn = new DwnSynset();
        try {
            value = "";
            SAXParserFactory factory = SAXParserFactory.newInstance();
            factory.setValidating(false);
            SAXParser parser = factory.newSAXParser();
            InputSource inp = new InputSource (new FileReader (filePath));
            inp.setEncoding(encoding);
            parser.parse(inp, this);
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
        if (qName.equalsIgnoreCase("SYNSET")) {
          dwnSyn = new DwnSynset();
        }
        value = "";
    }//--startElement

    public void endElement(String uri, String localName, String qName)
            throws SAXException {
        ///<SYNSET><ID>DUT-00000001-v</ID>
        ////<POS>v</POS>
        ////<SYNONYM><LITERAL>doorlaten<SENSE>1</SENSE></LITERAL></SYNONYM>
        ////<ILR><TYPE>hypernym</TYPE>DUT-00003904-v</ILR><ILR><TYPE>hypernym</TYPE>ENG15-01472320-v</ILR>
        ////<ELR>ENG15-01371393-v<TYPE>eq_near_synonym</TYPE></ELR>
        ////<DEF>2ndOrderEntity;BoundedEvent;Cause;Dynamic;SituationType;Static;</DEF>
        ////</SYNSET>
/*
        System.out.println("qName:"+qName);
        System.out.println("tagValue:"+tagValue);
*/
        if (qName.equalsIgnoreCase("POS")) {
            dwnSyn.setPOS(value.trim());
        }
        if (qName.equalsIgnoreCase("ID")) {
            dwnSyn.setID(value.trim());
        }
        if (qName.equalsIgnoreCase("LITERAL")) {
            aLiteral = value.trim();
        }
        if (qName.equalsIgnoreCase("SENSE")) {
            aSense = new Integer (value.trim()).intValue();
        }
        if (qName.equalsIgnoreCase("SYNONYM")) {
            aSyn = new Synonym (aLiteral, aSense);
            aLiteral = "";
            aSense = -1;
            dwnSyn.addToSYNS(aSyn);
        }

        if (qName.equalsIgnoreCase("TYPE")) {
            rType = value.trim();
        }

        if (qName.equalsIgnoreCase("ILR")) {
            target = value.trim();
            iRel = new DwnInternalRelation(rType, target);
            dwnSyn.addToIRLS(iRel);
            target = "";
            rType = "";
        }

        if (qName.equalsIgnoreCase("ELR")) {
            target = value.trim();
            eRel = new DwnEquivalenceRelation(rType, target);
            dwnSyn.addToERLS(eRel);
            target = "";
            rType = "";
        }


/*
        if (qName.equalsIgnoreCase("DLR")) {
            target = value.trim();
            iRel = new DwnInternalRelation(rType, target);
            dwnSyn.addToIRLS(iRel);
            target = "";
            rType = "";
        }

        if (qName.equalsIgnoreCase("SLR")) {
            target = value.trim();
            iRel = new DwnInternalRelation(rType, target);
            dwnSyn.addToIRLS(iRel);
            target = "";
            rType = "";
        }

*/

       if (qName.equalsIgnoreCase("DEF")) {
           dwnSyn.setDEF(value.trim());
       }

       if (qName.equalsIgnoreCase("SYNSET")) {
           if (dwnSyn.getID().length()>0) {
               if (DWN.containsKey(dwnSyn.getID())) {
                    //// Already taken
               }
               else {
                    DWN.put(dwnSyn.getID(), dwnSyn);
               }
               dwnSyn = new DwnSynset();
           }
       }
    }

    public void characters(char ch[], int start, int length)
            throws SAXException {
        value += new String(ch, start, length);
        //System.out.println("tagValue:"+tagValue);
    }




}
