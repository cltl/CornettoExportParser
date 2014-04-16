package vu.cornetto.pwn;

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
 * Created by IntelliJ IDEA.
 * User: kyoto
 * Date: 2/3/12
 * Time: 4:23 PM
 * To change this template use File | Settings | File Templates.
 */
public class PwnSaxParser extends DefaultHandler{


    static public HashMap<String, String> posMap = new HashMap<String, String>();
    static public HashMap<String, ArrayList<String>> glossMap = new HashMap<String, ArrayList<String>>();
    static public HashMap<String, ArrayList<String>> usageMap = new HashMap<String, ArrayList<String>>();
    static public HashMap<String, ArrayList<String>> anyRelations = new HashMap<String, ArrayList<String>>();
    static public HashMap<String, ArrayList<String>> hyperRelations = new HashMap<String, ArrayList<String>>();
    static String value = "";
    static String sourceId = "";
    static String targetId= "";
    String type = "";
    ArrayList<String> targets = new ArrayList<String>();

    public PwnSaxParser() {
        initParser();
    }

    public void initParser () {
        hyperRelations = new HashMap<String, ArrayList<String>>();
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



    public void startElement(String uri, String localName,
                             String qName, Attributes attributes)
            throws SAXException {

        if (qName.equalsIgnoreCase("ILR")) {
            type = attributes.getValue("type");
        }
        else {
        }
        value = "";
    }//--startElement


    /*

<SYNSET>
<ID>eng-30-00001740-a</ID>
<POS>a</POS><SYNONYM>
<LITERAL sense="1">able</LITERAL>
<WORD>able</WORD></SYNONYM>
<ILR type="near_antonym">eng-30-00002098-a</ILR>
<ILR type="be_in_state">eng-30-05200169-n</ILR>
<ILR type="be_in_state">eng-30-05616246-n</ILR>
<ILR type="eng_derivative">eng-30-05200169-n</ILR>
<ILR type="eng_derivative">eng-30-05616246-n</ILR>
<DEF>(usually followed by `to') having the necessary means or skill or know-how or authority to do something</DEF>
<USAGE>able to swim</USAGE><USAGE>she was able to program her computer</USAGE>
<USAGE>we were at last able to buy a car</USAGE>
<USAGE>able to get a grant for the project</USAGE>
</SYNSET>

    */

    /*
    <SYNSET>
    <STAMP>fra82e3 2010-10-20 15:22:44</STAMP>
    <RIGIDITY rigidScore="0" rigid="false" nonRigidScore="0"/>
    <ILR type="hypernym">eng-30-10058777-n</ILR>
    <ILR type="eng_derivative">eng-30-01097031-v</ILR>
    <ILR type="eng_derivative">eng-30-01518694-a</ILR>
    <ILR type="eng_derivative">eng-30-05640184-n</ILR>
    <ID>eng-30-10622053-n</ID>
    <SYNONYM><LITERAL sense="1">soldier</LITERAL></SYNONYM>
    <DEF>an enlisted man or woman who serves in an army</DEF>
    <USAGE>the soldiers stood at attention</USAGE>
    <VERSION>642</VERSION><POS>n</POS>
    </SYNSET>
     */
    public void endElement(String uri, String localName, String qName)
            throws SAXException {
        if (qName.equalsIgnoreCase("ID")) {
            sourceId = value.trim();
            if (sourceId.equals("eng-30-00007846-n")) {
                System.out.println("person sourceId = " + sourceId);

            }
            if (sourceId.equals("eng-30-10622053-n")) {
                System.out.println("soldier:1 sourceId = " + sourceId);
            }
        }
        else if (qName.equalsIgnoreCase("ILR")) {
            if (type.equalsIgnoreCase("hypernym")) {
                targetId = value.trim();
                targets.add(targetId);
            }
            type = "";
        }
        else if (qName.equalsIgnoreCase("SYNSET")) {
            if ((!sourceId.isEmpty()) && targets.size()>0) {
                hyperRelations.put(sourceId, targets);
            }
            sourceId = "";
            targets = new ArrayList<String>();
        }
    }

    public void characters(char ch[], int start, int length)
            throws SAXException {
        value += new String(ch, start, length);
        // System.out.println("tagValue:"+value);
    }

}
