package vu.cornetto.stats;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;

/**
 * Created by piek on 3/28/14.
 */
public class StatsForLUsWithResumes {

    String theForm = "";
    String thePos = "";
    String theSeqNr = "";

    static public void main (String[] args) {
        String luFilePath = args[0];
        //String luFilePath = "/Users/kyoto/Desktop/CDB/2013-AUG-28/cdblu-latest.xml";
        StatsForLexicalUnits parser = new StatsForLexicalUnits();
        parser.readLuFile(luFilePath);
    }

    public void readLuFile (String filePath) {
        try {
            FileOutputStream fos = new FileOutputStream(filePath+".stats.xls");
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
                            Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new ByteArrayInputStream(luBuffer.getBytes("UTF-8")));
                            if (doc!=null) {
                                Node node = doc.getFirstChild();
                                if (node.getNodeName().equals("cdb_lu")) {
                                    NamedNodeMap attributes = node.getAttributes();
                                    Node att = attributes.getNamedItem("c_lu_id");
                                    String c_lu_id = att.getNodeValue();
                                    countNodeFromContent(node);
                                    att = attributes.getNamedItem("c_seq_nr");
                                    theSeqNr = att.getNodeValue();
                                    if (thePos.equalsIgnoreCase("noun")) {

                                    }
                                    else if (thePos.equalsIgnoreCase("verb")) {
                                    }
                                    else if (thePos.equalsIgnoreCase("adj") ||thePos.equalsIgnoreCase("adjective")) {
                                        if (thePos=="adj") thePos = "adjective";
                                    }
                                    else {
                                    }
                                    //fos.write((";"+theForm+";"+thePos+"\n").getBytes());
                                    //fos.write(("#"+theForm+"#"+theSeqNr+"#"+thePos+"\n").getBytes());
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
                                        countNodeFromContent(node);
                                        att = attributes.getNamedItem("c_seq_nr");
                                        theSeqNr = att.getNodeValue();
                                        if (thePos.equalsIgnoreCase("noun")) {
                                        }
                                        else if (thePos.equalsIgnoreCase("verb")) {
                                        }
                                        else if (thePos.equalsIgnoreCase("adj")||thePos.equalsIgnoreCase("adjective")) {
                                        }
                                        else {
                                        }
                                        //fos.write(("#"+theForm+"#"+theSeqNr+"#"+thePos+"\n").getBytes());
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

    public void countNodeFromContent(Node node)
    {   if ((node.getTextContent()!=null) && (node.getTextContent().length()>0)){
        LuElements.update(node.getNodeName());
    }
        if (node.getNodeName().equals("form")) {
            theForm = "";
            thePos = "";
            NamedNodeMap attributes = node.getAttributes();
            Node attSpelling = attributes.getNamedItem("form-spelling");
            Node attCat = attributes.getNamedItem("form-cat");
            theForm = attSpelling.getNodeValue();
            thePos = attCat.getNodeValue();
            if (attCat.getNodeValue().equalsIgnoreCase("noun")) {
            }
            else if (attCat.getNodeValue().equalsIgnoreCase("verb")) {
            }
            else if (attCat.getNodeValue().equalsIgnoreCase("adj")) {
            }
            else if (attCat.getNodeValue().equalsIgnoreCase("adjective")) {
            }
            else if (attCat.getNodeValue().equalsIgnoreCase("adverb")) {
            }
            else {
            }


        }

        NodeList nl = node.getChildNodes();
        for(int i=0, cnt=nl.getLength(); i<cnt; i++)
        {
            Node childNode = nl.item(i);
            countNodeFromContent(childNode);
        }
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
