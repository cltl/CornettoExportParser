package vu.cornetto.stats;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;

/**
 * Created by IntelliJ IDEA.
 * User: Piek Vossen
 * Date: 25-aug-2008
 * Time: 22:17:00
 * To change this template use File | Settings | File Templates.
 */
public class StatsForLexicalUnits extends DefaultHandler {

    private String record;
    private String value;
    private String type;
    long recordCount;
    long textCount;
    Vector entries;
    Vector nouns;
    Vector verbs;
    Vector adjs;
    Vector others;
    HashMap<String, Integer> polNouns;
    HashMap<String, Integer> polVerbs;
    HashMap<String, Integer> polAdjectives;

    long nounLU;
    long verbLU;
    long adjLU;
    long advLU;
    long otherLU;
    String theForm;
    String thePos;
    String theSeqNr;

    public StatsForLexicalUnits() {
      initParser();
  }

  public void initParser () {
          polNouns= new HashMap<String, Integer>();
          polVerbs = new HashMap<String, Integer>();
          polAdjectives = new HashMap<String, Integer>();
          theSeqNr="";
          theForm = "";
          thePos = "";
          value = "";
          type = "";
          record = "";
          recordCount = 0;
          textCount = 0;
          nounLU =0;
          verbLU =0;
          adjLU =0;
          advLU=0;
          otherLU=0;
          entries = new Vector();
          nouns = new Vector();
          verbs = new Vector();
          adjs = new Vector();
          others = new Vector();
  }

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
                            Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new ByteArrayInputStream (luBuffer.getBytes("UTF-8")));
                            if (doc!=null) {
                                recordCount++;
                                Node node = doc.getFirstChild();
                                if (node.getNodeName().equals("cdb_lu")) {
                                    NamedNodeMap attributes = node.getAttributes();
                                    Node att = attributes.getNamedItem("c_lu_id");
                                    String c_lu_id = att.getNodeValue();
                                    countNodeFromContent(node);
                                    att = attributes.getNamedItem("c_seq_nr");
                                    theSeqNr = att.getNodeValue();
                                    if (thePos.equalsIgnoreCase("noun")) {
                                        if (!nouns.contains(theForm+"#"+thePos))
                                            nouns.add(theForm+"#"+thePos);
                                        if (polNouns.containsKey(theForm+"#"+thePos)) {
                                            Integer cnt = polNouns.get(theForm+"#"+thePos);
                                            cnt++;
                                            polNouns.put(theForm+"#"+thePos, cnt);
                                        }
                                        else {
                                            polNouns.put(theForm+"#"+thePos, 1);
                                        }
                                    }
                                    else if (thePos.equalsIgnoreCase("verb")) {
                                        if (!verbs.contains(theForm+"#"+thePos))
                                            verbs.add(theForm+"#"+thePos);
                                        if (polVerbs.containsKey(theForm+"#"+thePos)) {
                                            Integer cnt = polVerbs.get(theForm+"#"+thePos);
                                            cnt++;
                                            polVerbs.put(theForm+"#"+thePos, cnt);
                                        }
                                        else {
                                            polVerbs.put(theForm+"#"+thePos, 1);
                                        }
                                    }
                                    else if (thePos.equalsIgnoreCase("adj") ||thePos.equalsIgnoreCase("adjective")) {
                                        if (thePos=="adj") thePos = "adjective";
                                        if (!adjs.contains(theForm+"#"+thePos))
                                            adjs.add(theForm+"#"+thePos);
                                        if (polAdjectives.containsKey(theForm+"#"+thePos)) {
                                            Integer cnt = polAdjectives.get(theForm+"#"+thePos);
                                            cnt++;
                                            polAdjectives.put(theForm+"#"+thePos, cnt);
                                        }
                                        else {
                                            polAdjectives.put(theForm+"#"+thePos, 1);
                                        }
                                    }
                                    else {
                                        if (!others.contains(theForm+"#"+thePos))
                                            others.add(theForm+"#"+thePos);
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
                                    recordCount++;
                                    Node node = doc.getFirstChild();
                                    if (node.getNodeName().equals("cdb_lu")) {
                                        NamedNodeMap attributes = node.getAttributes();
                                        Node att = attributes.getNamedItem("c_lu_id");
                                        String c_lu_id = att.getNodeValue();
                                        countNodeFromContent(node);
                                        att = attributes.getNamedItem("c_seq_nr");
                                        theSeqNr = att.getNodeValue();
                                        if (thePos.equalsIgnoreCase("noun")) {
                                            if (!nouns.contains(theForm+"#"+thePos))
                                                nouns.add(theForm+"#"+thePos);
                                            if (polNouns.containsKey(theForm+"#"+thePos)) {
                                                Integer cnt = polNouns.get(theForm+"#"+thePos);
                                                cnt++;
                                                polNouns.put(theForm+"#"+thePos, cnt);
                                            }
                                            else {
                                                polNouns.put(theForm+"#"+thePos, 1);
                                            }
                                        }
                                        else if (thePos.equalsIgnoreCase("verb")) {
                                            if (!verbs.contains(theForm+"#"+thePos))
                                                verbs.add(theForm+"#"+thePos);
                                            if (polVerbs.containsKey(theForm+"#"+thePos)) {
                                                Integer cnt = polVerbs.get(theForm+"#"+thePos);
                                                cnt++;
                                                polVerbs.put(theForm+"#"+thePos, cnt);
                                            }
                                            else {
                                                polVerbs.put(theForm+"#"+thePos, 1);
                                            }
                                        }
                                        else if (thePos.equalsIgnoreCase("adj")||thePos.equalsIgnoreCase("adjective")) {
                                            if (thePos=="adj") thePos = "adjective";
                                            if (!adjs.contains(theForm+"#"+thePos))
                                                adjs.add(theForm+"#"+thePos);
                                            if (polAdjectives.containsKey(theForm+"#"+thePos)) {
                                                Integer cnt = polAdjectives.get(theForm+"#"+thePos);
                                                cnt++;
                                                polAdjectives.put(theForm+"#"+thePos, cnt);
                                            }
                                            else {
                                                polAdjectives.put(theForm+"#"+thePos, 1);
                                            }
                                        }
                                        else {
                                            if (!others.contains(theForm+"#"+thePos))
                                                others.add(theForm+"#"+thePos);
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

            String str = "Entries\t"+entries.size()+"\n";
            str += "\tALL\tNOUNS \tVERBS\tADJECTIVES\tADVERBS\tOTHERS\n";
            str += "Lexical units\t"+(nounLU+verbLU+adjLU+advLU+otherLU)+"\t"+nounLU+"\t"+verbLU+"\t"+adjLU+"\t"+advLU+"\t"+otherLU+"\n";
            str += "Lemmas (form+pos)\t"+(nouns.size()+verbs.size()+adjs.size()+others.size())+"\t"+nouns.size()+"\t"+verbs.size()+"\t"+adjs.size()+"\t"+others.size()+"\n";
            fos.write(str.getBytes());

            String topPolysemyWords = "Most polysemous words:\n";
            HashMap<Integer, Integer> polMap = new HashMap<Integer, Integer>();
            str = "\nPolysemy nouns\n";
            fos.write(str.getBytes());
            Set keySet = polNouns.keySet();
            Iterator keys = keySet.iterator();
            while (keys.hasNext()) {
                String key = (String) keys.next();
                Integer cnt = polNouns.get(key);
                if (cnt>=5) {
                    topPolysemyWords+= key+"\tN\t"+cnt.toString()+"\n";
                }
                if (polMap.containsKey(cnt)) {
                    Integer tel = polMap.get(cnt);
                    tel++;
                    polMap.put(cnt, tel);
                }
                else {
                    polMap.put(cnt, 1);
                }
            }
            keySet = polMap.keySet();
            keys = keySet.iterator();
            while (keys.hasNext()) {
                Integer key = (Integer) keys.next();
                Integer cnt = polMap.get(key);
                str = key+"\t"+cnt+"\n";
                fos.write(str.getBytes());
            }

            polMap = new HashMap<Integer, Integer>();
            str = "\nPolysemy verbs\n";
            fos.write(str.getBytes());
            keySet = polVerbs.keySet();
            keys = keySet.iterator();
            while (keys.hasNext()) {
                String key = (String) keys.next();
                Integer cnt = polVerbs.get(key);
                if (cnt>=5) {
                    topPolysemyWords+= key+"\tV\t"+cnt.toString()+"\n";
                }
                if (polMap.containsKey(cnt)) {
                    Integer tel = polMap.get(cnt);
                    tel++;
                    polMap.put(cnt, tel);
                }
                else {
                    polMap.put(cnt, 1);
                }
            }
            keySet = polMap.keySet();
            keys = keySet.iterator();
            while (keys.hasNext()) {
                Integer key = (Integer) keys.next();
                Integer cnt = polMap.get(key);
                str = key+"\t"+cnt+"\n";
                fos.write(str.getBytes());
            }

            polMap = new HashMap<Integer, Integer>();
            str = "\nPolysemy adjectives\n";
            fos.write(str.getBytes());
            keySet = polAdjectives.keySet();
            keys = keySet.iterator();
            while (keys.hasNext()) {
                String key = (String) keys.next();
                Integer cnt = polAdjectives.get(key);
                if (cnt>2) {
                    topPolysemyWords+= key+"\tN\t"+cnt.toString()+"\n";
                }
                if (polMap.containsKey(cnt)) {
                    Integer tel = polMap.get(cnt);
                    tel++;
                    polMap.put(cnt, tel);
                }
                else {
                    polMap.put(cnt, 1);
                }
            }
            keySet = polMap.keySet();
            keys = keySet.iterator();
            while (keys.hasNext()) {
                Integer key = (Integer) keys.next();
                Integer cnt = polMap.get(key);
                str = key+"\t"+cnt+"\n";
                fos.write(str.getBytes());
            }
            fos.write(topPolysemyWords.getBytes());
            fos.close();
            System.out.println("recordCount = " + recordCount);
            System.out.println("textCount = " + textCount);
            LuElements.printStats();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void countNodeFromContent(Node node)
    {   if ((node.getTextContent()!=null) && (node.getTextContent().length()>0)){
            textCount++;
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
                 nounLU++;
            }
            else if (attCat.getNodeValue().equalsIgnoreCase("verb")) {
                 verbLU++;
            }
            else if (attCat.getNodeValue().equalsIgnoreCase("adj")) {
                 adjLU++;
            }
            else if (attCat.getNodeValue().equalsIgnoreCase("adjective")) {
                 adjLU++;
            }
            else if (attCat.getNodeValue().equalsIgnoreCase("adverb")) {
                 advLU++;
            }
            else {
                 otherLU++;
            }

            //form-cat="noun" form-spelling="vrucht"
            String form = attSpelling.getNodeValue()+":"+attCat.getNodeValue();
            if (!entries.contains(form)) {
                entries.add(form);
               // System.out.println("form = " + form);
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
