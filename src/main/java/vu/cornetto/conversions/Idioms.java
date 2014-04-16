package vu.cornetto.conversions;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: Piek Vossen
 * Date: 13-okt-2009
 * Time: 9:09:34
 * To change this template use File | Settings | File Templates.
 */
public class Idioms extends DefaultHandler {

    private String record;
    private String value;
    private String type;
    String theForm;
    String thePos;
    String theSeqNr;
    String theSourceLu;
    String theExampleId;

    public Idioms() {
      initParser();
  }

  public void initParser () {
          theSeqNr="";
          theForm = "";
          thePos = "";
          theSourceLu = "";
          value = "";
          type = "";
          record = "";
          theExampleId = "";
  }

    static public void main (String[] args) {
        String luFilePath = args[0];
        String pos = args[1]; //noun, verb, adjective
        Idioms parser = new Idioms();
        parser.readLuFile(luFilePath, pos);
    }

    public void readLuFile (String filePath, String pos) {
        try {
            FileOutputStream fos = new FileOutputStream(filePath+".idioms."+pos+".xml");
            FileInputStream fis = new FileInputStream(filePath);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader in = new BufferedReader(isr);
            String inputLine;
            String luBuffer = "";
            String str = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<collocations>\n";
            fos.write(str.getBytes());
            while (in.ready()&&(inputLine = in.readLine()) != null) {
                if (inputLine.indexOf("</cdb_lu>")>-1) {
                    int idx_einde = inputLine.indexOf("</cdb_lu>");
                    int next_idx = inputLine.indexOf("<cdb_lu ", idx_einde);
                    if (next_idx==-1) {
                        /// NORMAL BUFFER
                        luBuffer += inputLine;
                        try {
                            Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new ByteArrayInputStream(luBuffer.getBytes("UTF-8")));
                            if (doc!=null) {
                                Node node = doc.getFirstChild();
                                if (node.getNodeName().equals("cdb_lu")) {
                                    theSourceLu = "";
                                    theSeqNr = "";
                                    theExampleId = "";
                                    NamedNodeMap attributes = node.getAttributes();
                                    Node att = attributes.getNamedItem("c_lu_id");
                                    theSourceLu = att.getNodeValue();
                                    att = attributes.getNamedItem("c_seq_nr");
                                    theSeqNr = att.getNodeValue();
                                    getFormPos(node);
                                    if ((theForm.length()>0) &&
                                        (thePos.equals(pos))) {
                                            getIdioms(node, fos);
                                    }
                                    else {
                                        //System.out.println("wrong theForm = " + theForm);
                                    }
                                }
                            }
                        } catch (SAXException e) {
                               e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                        } catch (IOException e) {
                               e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                        } catch (ParserConfigurationException e) {
                               e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                        }
                        /// WE ARE DONE SO WE EMPTY THE BUFFER
                        luBuffer = "";
                    }
                    else {
                        //// MORE THAN ONE LU ON A SINGLE LINE
                        int idx_start = 0;
                        while (idx_start>-1) {
                            luBuffer = inputLine.substring(idx_start, idx_einde+9);
                            //System.out.println("CHUNK luBuffer = " + luBuffer);
                            try {
                                Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new ByteArrayInputStream (luBuffer.getBytes("UTF-8")));
                                if (doc!=null) {
                                    Node node = doc.getFirstChild();
                                    if (node.getNodeName().equals("cdb_lu")) {
                                        theSourceLu = "";
                                        theSeqNr = "";
                                        NamedNodeMap attributes = node.getAttributes();
                                        Node att = attributes.getNamedItem("c_lu_id");
                                        theSourceLu = att.getNodeValue();
                                        att = attributes.getNamedItem("c_seq_nr");
                                        theSeqNr = att.getNodeValue();
                                        getFormPos(node);
                                        if ((theForm.length()>0) &&
                                            (thePos.equals(pos))) {
                                                //System.out.println("theForm = " + theForm);
                                                getIdioms(node, fos);
                                        }
                                    }
                                }
                            } catch (SAXException e) {
                                   e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                            } catch (IOException e) {
                                   e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                            } catch (ParserConfigurationException e) {
                                   e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                            }
                            /// WE ARE DONE SO WE EMPTY THE BUFFER
                            luBuffer = "";
                            idx_start = inputLine.indexOf("<cdb_lu ", idx_einde);
                            if (idx_start>-1) {
                                idx_einde = inputLine.indexOf("</cdb_lu ", idx_start);
                            }
                         }
                    }
                }
                else {
                    if (luBuffer.length()>0) {
                        //// WE ALREADY HAVE SOMETHING IN THE BUFFER SO WE ADD ANY INPUT
                        luBuffer += inputLine;
                    }
                    else if (inputLine.indexOf("<cdb_lu ")>-1) {
                        //// BUFFER IS EMPTY BUT WE FOUND A NEW ENTRY SO WE ADD
                        luBuffer = inputLine;
                    }
                }
            }

            str = "\n</collocations>\n";
            fos.write(str.getBytes());
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void getFormPos (Node lu) {
        theForm = "";
        thePos = "";
        Node node = getSubNode(lu, "form");
        if (node!=null) {
            NamedNodeMap attributes = node.getAttributes();
            Node attSpelling = attributes.getNamedItem("form-spelling");
            Node attCat = attributes.getNamedItem("form-cat");
            theForm = attSpelling.getNodeValue();
            thePos = attCat.getNodeValue();
        }
        else {
            //System.out.println("node.getNodeName() = " + node.getNodeName());
        }
    }


    //                                Node node = doc.getFirstChild();

    public void getIdioms(Node node, FileOutputStream fos)
    {   Node examples = getSubNode (node, "examples");
        if (examples!=null) {
            NodeList children = examples.getChildNodes();
            for (int i=0;i<children.getLength();i++) {
                Node child = children.item(i);
                if (child.getNodeName().equalsIgnoreCase("example")) {
                    if (isFixed(child)) {
                        if (getSynSubtype(child).equals("idiom")) {
                            NamedNodeMap attributes = child.getAttributes();
                            Node att = attributes.getNamedItem("r_ex_id");
                            theExampleId = att.getNodeValue();
                            //System.out.println("theExampleId = " + theExampleId);
                            String str = getCollecationXml(child);
                          //  System.out.println("str = " + str);
                            try {
                                fos.write(str.getBytes());
                            } catch (Exception e) {
                                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                            }
                        }
                    }
                    else {
                       // System.out.println("NOT FIXED");
                    }
                }
                else {
                   // System.out.println("child.getNodeName() = " + child.getNodeName());
                }
            }
        }
    }

    public String getCollecationXml(Node node) {
        ArrayList<String> combis = getCombiPairs(node);
        String str = "<collocation fixed=\""+"true r_ex_id=\""+theExampleId+"\" type=\""+getSynSubtype(node)+"\">\n";
        str += "\t<form lemma=\""+theForm+"\" pos=\""+thePos+"\" seq=\""+theSeqNr+"\" sourceLu=\""+theSourceLu+"\"/>\n";
        str += "\t<canonicalform>"+getCanonicalForm(node)+"</canonicalform>\n";
     //   str += "\t<textualform>"+getTextualForm(node)+"</textualform>\n";
        str += "\t<combipairs>\n";
        for (int i = 0; i < combis.size(); i++) {
            String s =  combis.get(i);
            str += "\t\t"+s;
        }
        str += "\t</combipairs>\n";
        str += "</collocation>\n";
        return str;
    }

    public boolean isFixed (Node node) {
        Node syExample = this.getSubNode(node, "syntax_example");
        /*
        	<syntax_example>
            <sy-type>fixed</sy-type>
            <sy-subtype>idiom</sy-subtype>
        	</syntax_example>
         */
        if (syExample!=null) {
            NodeList children = syExample.getChildNodes();
            for (int i=0;i<children.getLength();i++) {
                Node child = children.item(i);
                //System.out.println("child.getNodeName() = " + child.getNodeName());
                if (child.getNodeName().equalsIgnoreCase("sy-type")) {
                    if (child.getTextContent()!=null) {
                        //System.out.println("child.getTextContent() = " + child.getTextContent());
                        if (child.getTextContent().equalsIgnoreCase("fixed")) {
                            //System.out.println("FIXED");
                            return true;
                        }
                    }
                    else {
                    }
                }
            }
        }
        return false;
    }

    //example r_ex_id="61357">
    public String getTheExampleId (Node node) {
        String str  = "";
        Node syExample = this.getSubNode(node, "example");
        if (syExample!=null) {
            NamedNodeMap attributes = node.getAttributes();
            Node att = attributes.getNamedItem("r_ex_id");
            str = att.getNodeValue();
        }
        return str;
    }

    public String getSynSubtype (Node node) {
        String str  = "";
        Node syExample = this.getSubNode(node, "syntax_example");
        if (syExample!=null) {
            NodeList children = syExample.getChildNodes();
            for (int i=0;i<children.getLength();i++) {
                Node child = children.item(i);
                if (child.getNodeName().equalsIgnoreCase("sy-subtype")) {
                    str = child.getTextContent();
                    break;
                }
                else {
                    if (child.hasChildNodes()) {
                        str = getSynSubtype (child);
                    }
                }
            }
        }
        return str;
    }

    public String getCanonicalForm (Node node) {
        String str  = "";
        NodeList children = node.getChildNodes();
        for (int i=0;i<children.getLength();i++) {
            Node child = children.item(i);
            if (child.getNodeName().equalsIgnoreCase("canonicalform")) {
                str += child.getTextContent();
             //   System.out.println("str = " + str);
                break;
            }
            else {
                if (child.hasChildNodes()) {
                    str += getCanonicalForm (child);
                }
            }
        }
      //  System.out.println("str = " + str);
        return str;
    }

    public String getTextualForm (Node node) {
        String str  = "";
        NodeList children = node.getChildNodes();
        for (int i=0;i<children.getLength();i++) {
            Node child = children.item(i);
            if (child.getNodeName().equalsIgnoreCase("textualform")) {
                str += child.getTextContent();
                break;
            }
            else {
                if (child.hasChildNodes()) {
                    str += getTextualForm (child);
                }
            }
        }
        return str;
    }

    public ArrayList<String> getCombiPairs (Node node) {
        ArrayList<String> pairs = new ArrayList<String>();
        Node syntaxCombiNode = getSubNode(node, "sy-combi");
        if (syntaxCombiNode!=null) {
            NodeList children = syntaxCombiNode.getChildNodes();
            for (int i=0;i<children.getLength();i++) {
                Node child = children.item(i);
                //System.out.println("child.getNodeName() = " + child.getNodeName());
                if (child.getNodeName().equalsIgnoreCase("sy-combipair")) {
                    /*
                        <sy-combipair>
                        <sy-combiword>huis</sy-combiword>
                        <sy-combicat>noun</sy-combicat>
                        </sy-combipair>
                     */
                    String str = "<sy-combipair";
                    NodeList grandchildren = child.getChildNodes();
                   // System.out.println("grandchildren.getLength() = " + grandchildren.getLength());
                    for (int j=0;j<grandchildren.getLength();j++) {
                        Node grandChild = grandchildren.item(j);
                        if (grandChild!=null) {
                            if (grandChild.getNodeName().equalsIgnoreCase("sy-combiword")) {
                               str += " sy-combiword=\""+grandChild.getTextContent()+"\"";
                            }
                            else if (grandChild.getNodeName().equalsIgnoreCase("sy-combicat")) {
                                str += " sy-combicat=\""+grandChild.getTextContent()+"\"";
                            }
                        }
                    }
                    str += "/>\n";
                    pairs.add(str);
                }
            }
        }
        return pairs;
    }

    public Node getSubNode (Node node, String name) {
        NodeList children = node.getChildNodes();
        for (int i=0;i<children.getLength();i++) {
            Node child = children.item(i);
            if (child.getNodeName().equalsIgnoreCase(name)) {
                return child;
            }
            else {
                Node subnode = getSubNode (child, name);
                if (subnode!=null) {
                    return subnode;
                }
            }
        }
        return null;
    }

}
        /*
        	<example r_ex_id="43966">
-
	<form_example>
<canonicalform>met de deur in huis vallen</canonicalform>
<category>vp</category>
</form_example>
-
	<syntax_example>
<sy-type>fixed</sy-type>
<sy-subtype>idiom</sy-subtype>
-
	<sy-combi>
-
	<sy-combipair>
<sy-combiword>huis</sy-combiword>
<sy-combicat>noun</sy-combicat>
</sy-combipair>
-
	<sy-combipair>
<sy-combiword>vallen</sy-combiword>
<sy-combicat>verb</sy-combicat>
</sy-combipair>
</sy-combi>
</syntax_example>
-
	<semantics_example>
-
	<sem-meaningdescription>
zonder inleiding meteen het onderwerp oppakken en een verzoek of mededeling doen
</sem-meaningdescription>
</semantics_example>
</example>
         */



    /*
    <cdb_lu c_seq_nr="1" type="swu" is_complete="true" c_lu_id="r_n-10769">
<form form-cat="noun" form-spelling="deur"/>
-
	<morphology_noun>
<morpho-type>simpmorph</morpho-type>
-
	<morpho-plurforms>
<morpho-plurform>deuren</morpho-plurform>
</morpho-plurforms>
</morphology_noun>
-
	<syntax_noun>
<sy-gender>mf</sy-gender>
<sy-article>de</sy-article>
</syntax_noun>
-
	<semantics_noun>
<sem-reference>common</sem-reference>
<sem-countability>count</sem-countability>
<sem-type>artefact</sem-type>
<sem-subclass>structuur</sem-subclass>
<sem-resume>afsluiting v.e. vertrek, kast, enz.</sem-resume>
</semantics_noun>
-
	<examples>
-
	<example r_ex_id="43948">
-
	<form_example>
<canonicalform>dat vliegt de deur uit</canonicalform>
<category>s</category>
</form_example>
-
	<syntax_example>
<sy-type>fixed</sy-type>
<sy-subtype>idiom</sy-subtype>
-
	<sy-combi>
-
	<sy-combipair>
<sy-combiword>vliegen</sy-combiword>
<sy-combicat>verb</sy-combicat>
</sy-combipair>
-
	<sy-combipair>
<sy-combiword>uit</sy-combiword>
<sy-combicat>prep</sy-combicat>
</sy-combipair>
</sy-combi>
</syntax_example>
-
	<semantics_example>
<sem-meaningdescription>dat vindt veel aftrek</sem-meaningdescription>
</semantics_example>
</example>
-
	<example r_ex_id="43949">
-
	<form_example>
<canonicalform>met de deuren gooien</canonicalform>
<category>vp</category>
</form_example>
-
	<syntax_example>
<sy-type>fixed</sy-type>
<sy-subtype>lexcol</sy-subtype>
-
	<sy-combi>
-
	<sy-combipair>
<sy-combiword>gooien</sy-combiword>
<sy-combicat>verb</sy-combicat>
</sy-combipair>
</sy-combi>
</syntax_example>
-
	<semantics_example>
<sem-meaningdescription>de deuren hard dichtslaan</sem-meaningdescription>
<sem-lc-collocator>action</sem-lc-collocator>
</semantics_example>
</example>
-
	<example r_ex_id="43950">
-
	<form_example>
<canonicalform>dat doet de deur dicht</canonicalform>
<category>s</category>
</form_example>
-
	<syntax_example>
<sy-type>fixed</sy-type>
<sy-subtype>idiom</sy-subtype>
-
	<sy-combi>
-
	<sy-combipair>
<sy-combiword>dicht</sy-combiword>
<sy-combicat>adj</sy-combicat>
</sy-combipair>
-
	<sy-combipair>
<sy-combiword>doen</sy-combiword>
<sy-combicat>verb</sy-combicat>
</sy-combipair>
</sy-combi>
</syntax_example>
-
	<semantics_example>
<sem-meaningdescription>dat geeft de doorslag</sem-meaningdescription>
</semantics_example>
</example>
-
	<example r_ex_id="43951">
-
	<form_example>
<canonicalform>zo gek als een deur zijn</canonicalform>
<category>ap</category>
</form_example>
-
	<syntax_example>
<sy-type>fixed</sy-type>
<sy-subtype>idiom</sy-subtype>
-
	<sy-combi>
-
	<sy-combipair>
<sy-combiword>gek</sy-combiword>
<sy-combicat>adj</sy-combicat>
</sy-combipair>
</sy-combi>
</syntax_example>
-
	<semantics_example>
<sem-meaningdescription>helemaal gek</sem-meaningdescription>
</semantics_example>
</example>
-
	<example r_ex_id="43952">
-
	<form_example>
<canonicalform>de deur niet meer uitkomen</canonicalform>
<category>vp</category>
</form_example>
-
	<syntax_example>
<sy-type>fixed</sy-type>
<sy-subtype>idiom</sy-subtype>
-
	<sy-combi>
-
	<sy-combipair>
<sy-combiword>uitkomen</sy-combiword>
<sy-combicat>verb</sy-combicat>
</sy-combipair>
</sy-combi>
</syntax_example>
-
	<semantics_example>
<sem-meaningdescription>het huis niet meer verlaten</sem-meaningdescription>
</semantics_example>
</example>
-
	<example r_ex_id="43953">
-
	<form_example>
<canonicalform>iemand de deur wijzen</canonicalform>
<category>vp</category>
</form_example>
-
	<syntax_example>
<sy-type>fixed</sy-type>
<sy-subtype>idiom</sy-subtype>
-
	<sy-combi>
-
	<sy-combipair>
<sy-combiword>wijzen</sy-combiword>
<sy-combicat>verb</sy-combicat>
</sy-combipair>
</sy-combi>
</syntax_example>
-
	<semantics_example>
<sem-meaningdescription>iemand bevelen weg te gaan</sem-meaningdescription>
</semantics_example>
</example>
-
	<example r_ex_id="43954">
-
	<form_example>
<canonicalform>bij iemand de deur plat lopen</canonicalform>
<category>vp</category>
</form_example>
-
	<syntax_example>
<sy-type>fixed</sy-type>
<sy-subtype>idiom</sy-subtype>
-
	<sy-combi>
-
	<sy-combipair>
<sy-combiword>lopen</sy-combiword>
<sy-combicat>verb</sy-combicat>
</sy-combipair>
-
	<sy-combipair>
<sy-combiword>plat</sy-combiword>
<sy-combicat>adj</sy-combicat>
</sy-combipair>
</sy-combi>
</syntax_example>
-
	<semantics_example>
<sem-meaningdescription>iemand buitengewoon vaak bezoeken</sem-meaningdescription>
</semantics_example>
</example>
-
	<example r_ex_id="43955">
-
	<form_example>
<canonicalform>met iets langs de deuren gaan</canonicalform>
<category>vp</category>
</form_example>
-
	<syntax_example>
<sy-type>fixed</sy-type>
<sy-subtype>idiom</sy-subtype>
-
	<sy-combi>
-
	<sy-combipair>
<sy-combiword>gaan</sy-combiword>
<sy-combicat>verb</sy-combicat>
</sy-combipair>
-
	<sy-combipair>
<sy-combiword>langs</sy-combiword>
<sy-combicat>prep</sy-combicat>
</sy-combipair>
</sy-combi>
</syntax_example>
-
	<semantics_example>
-
	<sem-meaningdescription>
kopers, belangstellenden ergens voor proberen te vinden
</sem-meaningdescription>
</semantics_example>
</example>
-
	<example r_ex_id="43956">
-
	<form_example>
<canonicalform>buiten de deur eten</canonicalform>
<category>vp</category>
</form_example>
-
	<syntax_example>
<sy-type>fixed</sy-type>
<sy-subtype>idiom</sy-subtype>
-
	<sy-combi>
-
	<sy-combipair>
<sy-combiword>eten</sy-combiword>
<sy-combicat>verb</sy-combicat>
</sy-combipair>
-
	<sy-combipair>
<sy-combiword>buiten</sy-combiword>
<sy-combicat>prep</sy-combicat>
</sy-combipair>
</sy-combi>
</syntax_example>
-
	<semantics_example>
<sem-meaningdescription>in een restaurant eten</sem-meaningdescription>
</semantics_example>
</example>
-
	<example r_ex_id="43957">
-
	<form_example>
<canonicalform>de deuren van [een fabriek] sluiten</canonicalform>
<category>vp</category>
</form_example>
-
	<syntax_example>
<sy-type>fixed</sy-type>
<sy-subtype>idiom</sy-subtype>
-
	<sy-combi>
-
	<sy-combipair>
<sy-combiword>sluiten</sy-combiword>
<sy-combicat>verb</sy-combicat>
</sy-combipair>
</sy-combi>
</syntax_example>
-
	<semantics_example>
-
	<sem-meaningdescription>
het bedrijf dat achter de deuren bestond, opheffen
</sem-meaningdescription>
</semantics_example>
</example>
-
	<example r_ex_id="43958">
-
	<form_example>
<canonicalform>de deur uit zijn</canonicalform>
<category>vp</category>
</form_example>
-
	<syntax_example>
<sy-type>fixed</sy-type>
<sy-subtype>idiom</sy-subtype>
-
	<sy-combi>
-
	<sy-combipair>
<sy-combiword>uit</sy-combiword>
<sy-combicat>adj</sy-combicat>
</sy-combipair>
</sy-combi>
</syntax_example>
-
	<semantics_example>
<sem-meaningdescription>niet meer thuis wonen</sem-meaningdescription>
</semantics_example>
</example>
-
	<example r_ex_id="43959">
-
	<form_example>
<canonicalform>iets achter gesloten deuren behandelen</canonicalform>
<category>vp</category>
</form_example>
-
	<syntax_example>
<sy-type>fixed</sy-type>
<sy-subtype>idiom</sy-subtype>
-
	<sy-combi>
-
	<sy-combipair>
<sy-combiword>behandelen</sy-combiword>
<sy-combicat>verb</sy-combicat>
</sy-combipair>
-
	<sy-combipair>
<sy-combiword>gesloten</sy-combiword>
<sy-combicat>adj</sy-combicat>
</sy-combipair>
</sy-combi>
</syntax_example>
-
	<semantics_example>
<sem-meaningdescription>niet openbaar iets bespreken of behandelen</sem-meaningdescription>
</semantics_example>
</example>
-
	<example r_ex_id="43960">
-
	<form_example>
<canonicalform>voor een gesloten deur staan</canonicalform>
<category>vp</category>
</form_example>
-
	<syntax_example>
<sy-type>fixed</sy-type>
<sy-subtype>idiom</sy-subtype>
-
	<sy-combi>
-
	<sy-combipair>
<sy-combiword>staan</sy-combiword>
<sy-combicat>verb</sy-combicat>
</sy-combipair>
-
	<sy-combipair>
<sy-combiword>gesloten</sy-combiword>
<sy-combicat>adj</sy-combicat>
</sy-combipair>
</sy-combi>
</syntax_example>
-
	<semantics_example>
<sem-meaningdescription>iemand niet thuis treffen</sem-meaningdescription>
</semantics_example>
</example>
-
	<example r_ex_id="43961">
-
	<form_example>
<canonicalform>een open deur intrappen</canonicalform>
<category>vp</category>
</form_example>
-
	<syntax_example>
<sy-type>fixed</sy-type>
<sy-subtype>idiom</sy-subtype>
-
	<sy-combi>
-
	<sy-combipair>
<sy-combiword>intrappen</sy-combiword>
<sy-combicat>verb</sy-combicat>
</sy-combipair>
-
	<sy-combipair>
<sy-combiword>open</sy-combiword>
<sy-combicat>adj</sy-combicat>
</sy-combipair>
</sy-combi>
</syntax_example>
-
	<semantics_example>
<sem-meaningdescription>iets doen, betogen, enz. dat al bekend, gedaan is</sem-meaningdescription>
</semantics_example>
</example>
-
	<example r_ex_id="43962">
-
	<form_example>
<canonicalform>daar is de deur!</canonicalform>
<category>s</category>
</form_example>
-
	<syntax_example>
<sy-type>fixed</sy-type>
<sy-subtype>idiom</sy-subtype>
-
	<sy-combi>
-
	<sy-combipair>
<sy-combiword>daar</sy-combiword>
<sy-combicat>pron</sy-combicat>
</sy-combipair>
</sy-combi>
</syntax_example>
-
	<semantics_example>
<sem-meaningdescription>ga eruit!</sem-meaningdescription>
</semantics_example>
</example>
-
	<example r_ex_id="43963">
-
	<form_example>
<canonicalform>een stok achter de deur</canonicalform>
<category>np</category>
</form_example>
-
	<syntax_example>
<sy-type>fixed</sy-type>
<sy-subtype>idiom</sy-subtype>
-
	<sy-combi>
-
	<sy-combipair>
<sy-combiword>stok</sy-combiword>
<sy-combicat>noun</sy-combicat>
</sy-combipair>
-
	<sy-combipair>
<sy-combiword>achter</sy-combiword>
<sy-combicat>prep</sy-combicat>
</sy-combipair>
</sy-combi>
</syntax_example>
-
	<semantics_example>
<sem-meaningdescription>iets dat je sterk motiveert</sem-meaningdescription>
</semantics_example>
</example>
-
	<example r_ex_id="43964">
-
	<form_example>
<canonicalform>voor de deur staan</canonicalform>
<category>vp</category>
</form_example>
-
	<syntax_example>
<sy-type>fixed</sy-type>
<sy-subtype>idiom</sy-subtype>
-
	<sy-combi>
-
	<sy-combipair>
<sy-combiword>staan</sy-combiword>
<sy-combicat>verb</sy-combicat>
</sy-combipair>
-
	<sy-combipair>
<sy-combiword>voor</sy-combiword>
<sy-combicat>prep</sy-combicat>
</sy-combipair>
</sy-combi>
</syntax_example>
-
	<semantics_example>
<sem-meaningdescription>binnenkort gebeuren</sem-meaningdescription>
</semantics_example>
</example>
-
	<example r_ex_id="43965">
-
	<form_example>
<canonicalform>de deur op een kier zetten</canonicalform>
<category>vp</category>
</form_example>
-
	<syntax_example>
<sy-type>fixed</sy-type>
<sy-subtype>idiom</sy-subtype>
-
	<sy-combi>
-
	<sy-combipair>
<sy-combiword>kier</sy-combiword>
<sy-combicat>noun</sy-combicat>
</sy-combipair>
-
	<sy-combipair>
<sy-combiword>zetten</sy-combiword>
<sy-combicat>verb</sy-combicat>
</sy-combipair>
</sy-combi>
</syntax_example>
-
	<semantics_example>
<sem-meaningdescription>enigszins toegeeflijk worden</sem-meaningdescription>
</semantics_example>
</example>
-
	<example r_ex_id="43966">
-
	<form_example>
<canonicalform>met de deur in huis vallen</canonicalform>
<category>vp</category>
</form_example>
-
	<syntax_example>
<sy-type>fixed</sy-type>
<sy-subtype>idiom</sy-subtype>
-
	<sy-combi>
-
	<sy-combipair>
<sy-combiword>huis</sy-combiword>
<sy-combicat>noun</sy-combicat>
</sy-combipair>
-
	<sy-combipair>
<sy-combiword>vallen</sy-combiword>
<sy-combicat>verb</sy-combicat>
</sy-combipair>
</sy-combi>
</syntax_example>
-
	<semantics_example>
-
	<sem-meaningdescription>
zonder inleiding meteen het onderwerp oppakken en een verzoek of mededeling doen
</sem-meaningdescription>
</semantics_example>
</example>
-
	<example r_ex_id="43967">
-
	<form_example>
<canonicalform>dat is niet naast de deur</canonicalform>
<category>s</category>
</form_example>
-
	<syntax_example>
<sy-type>fixed</sy-type>
<sy-subtype>idiom</sy-subtype>
-
	<sy-combi>
-
	<sy-combipair>
<sy-combiword>naast</sy-combiword>
<sy-combicat>prep</sy-combicat>
</sy-combipair>
</sy-combi>
</syntax_example>
-
	<semantics_example>
<sem-meaningdescription>dat is niet dichtbij</sem-meaningdescription>
</semantics_example>
</example>
</examples>
-
	<sem-definition>
-
	<sem-def-noun>
<sem-genus>afsluiting</sem-genus>
-
	<sem-specificae>
die beweegbaar en verticaal is en die toegang geeft tot een vertrek, kast, enz.
</sem-specificae>
</sem-def-noun>
<sem-defSource>XVD</sem-defSource>
</sem-definition>
</cdb_lu>
     */

    /*
    <cdb_lu c_seq_nr="4" type="swu" is_complete="true" c_lu_id="c_545141">
    <syntax_noun><sy-article>de</sy-article><sy-number/><sy-gender>mf</sy-gender><sy-complementation/></syntax_noun>
    <pragmatics><prag-origin/><prag-chronology/><prag-subj-gen/><prag-style/><prag-frequency/><prag-geography/><prag-socGroup/><prag-domain general="true" subjectfield="plantk"/><prag-connotation/></pragmatics>
    <examples><example r_ex_id="c_545141-1">
    <semantics_example><sem-gc-compl/><sem-lc-collocator/><sem-spec-collocator/><sem-gc-gramword/><sem-meaningdescription>resultaat hebben</sem-meaningdescription><sem-subtype-argument/></semantics_example>
    <syntax_example><sy-subtype>idiom</sy-subtype><sy-combi><sy-combipair><sy-combiword>afwerpen</sy-combiword>
    <sy-combicat>verb</sy-combicat></sy-combipair></sy-combi><sy-type>fixed</sy-type></syntax_example>
    <form_example><category>vp</category><canonicalform>vruchten afwerpen</canonicalform>
    <textualform>de jarenlange voorbereidingen hebben hun vruchten afgeworpen</textualform>
    <text-category>s</text-category></form_example></example>
    <example r_ex_id="c_545141-2">
    <semantics_example><sem-gc-compl/><sem-lc-collocator/><sem-spec-collocator/><sem-gc-gramword/>
    <sem-meaningdescription>het resultaat</sem-meaningdescription>
    <sem-subtype-argument/></semantics_example>
    <syntax_example><sy-subtype>idiom</sy-subtype><sy-combi><sy-combipair>
    <sy-combiword>van</sy-combiword><sy-combicat>prep</sy-combicat></sy-combipair>
    <sy-combipair><sy-combiword>inspanning</sy-combiword>
    <sy-combicat>noun</sy-combicat></sy-combipair></sy-combi><sy-type>fixed</sy-type></syntax_example>
    <form_example><category>np</category>
    <canonicalform>de vrucht van zijn inspanningen</canonicalform>
    <textualform/><text-category/></form_example></example>
    <example r_ex_id="c_545141-3">
    <semantics_example><sem-gc-compl/><sem-lc-collocator/><sem-spec-collocator/><sem-gc-gramword/>
    <sem-meaningdescription>het voordeel ervan hebben</sem-meaningdescription><sem-subtype-argument/>
    </semantics_example><syntax_example>
    <sy-subtype>idiom</sy-subtype><sy-combi>
    <sy-combipair><sy-combiword>van</sy-combiword>
    <sy-combicat>prep</sy-combicat></sy-combipair>
    <sy-combipair><sy-combiword>plukken</sy-combiword>
    <sy-combicat>verb</sy-combicat></sy-combipair></sy-combi><sy-type>fixed</sy-type></syntax_example>
    <form_example><category>vp</category><canonicalform>ergens de vruchten van plukken</canonicalform><textualform/>
    <text-category/></form_example></example></examples>
    <semantics_noun><sem-type>concrother</sem-type>
    <sem-reference>common</sem-reference>
    <sem-subclass>flora (deel v.)</sem-subclass>
    <sem-selrestriction/>
    <sem-countability>count</sem-countability>
    <sem-shift/><sem-definition>
    <sem-resume>resultaten van een inspanning</sem-resume></sem-definition></semantics_noun>
    <morphology_noun>
    <morpho-type>simpmorph</morpho-type>
    <morpho-plurforms><morpho-plurform>vruchten</morpho-plurform></morpho-plurforms>
    <morpho-structure/>
    </morphology_noun>
    <form form-length="" form-cat="noun" form-spelling="vrucht" form-spelvar=""/></cdb_lu>
     */
