package vu.cornetto.cdb;

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
import java.util.HashMap;

/**
 * Created by IntelliJ IDEA.
 * User: Piek Vossen
 * Date: 25-aug-2008
 * Time: 22:17:00
 * To change this template use File | Settings | File Templates.
 */
public class CdbLuDomParser extends DefaultHandler {

    public HashMap<String,Document> luMap;
    public HashMap<String,ArrayList<String>> lemmaLuMap;

    public CdbLuDomParser() {
      initParser();
  }

  public void initParser () {
          luMap = new HashMap<String, Document>();
          lemmaLuMap = new HashMap<String,ArrayList<String>>();
  }

    static public void main (String[] args) {
        String luFilePath = args[0];
        CdbLuDomParser parser = new CdbLuDomParser();
        parser.readLuFile(luFilePath);
    }

    public void readLuFile (String filePath) {
        try {
            FileOutputStream fos = new FileOutputStream(filePath+".data.log");
            FileInputStream fis = new FileInputStream(filePath);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader in = new BufferedReader(isr);
            String inputLine;
            String luBuffer = "";
            while (in.ready()&&(inputLine = in.readLine()) != null) {
                //System.out.println(inputLine);
                if (inputLine.indexOf("</cdb_lu>")>-1) {
                    int idx_einde = inputLine.indexOf("</cdb_lu>");
                    int next_idx = inputLine.indexOf("<cdb_lu ", idx_einde);
                    if (next_idx==-1) {
                        /// NORMAL BUFFER
                        luBuffer += inputLine;
                        //System.out.println("NORMAL luBuffer = " + luBuffer);
                        try {
                            Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new ByteArrayInputStream (luBuffer.getBytes("UTF-8")));
                            if (doc!=null) {
                                Node node = doc.getFirstChild();
                                if (node.getNodeName().equals("cdb_lu")) {
                                    NamedNodeMap attributes = node.getAttributes();
                                    Node att = attributes.getNamedItem("c_lu_id");
                                    String c_lu_id = att.getNodeValue();
                                    luMap.put(c_lu_id, doc);
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
                                        NamedNodeMap attributes = node.getAttributes();
                                        Node att = attributes.getNamedItem("c_lu_id");
                                        String c_lu_id = att.getNodeValue();
                                        luMap.put(c_lu_id, doc);
                                        Node formNode= getSubNode(node, "form");
                                        attributes = formNode.getAttributes();
                                        att = attributes.getNamedItem("form-spelling");
                                        String form = att.getNodeValue();
                                        if (lemmaLuMap.containsKey(form)) {
                                           ArrayList<String> ids = lemmaLuMap.get(form);
                                           ids.add(c_lu_id);
                                            lemmaLuMap.put(form, ids);
                                        }
                                        else {
                                            ArrayList<String> ids = new ArrayList<String>();
                                            ids.add(c_lu_id);
                                            lemmaLuMap.put(form, ids);
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

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //<form form-length="" form-cat="noun" form-spelling="vrucht" form-spelvar=""/></cdb_lu>

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
}
