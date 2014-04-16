package vu.cornetto.conversions;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: kyoto
 * Date: 8/24/11
 * Time: 3:35 PM
 * To change this template use File | Settings | File Templates.
 */
public class ExportLUs {


    private String record;
    private String value;
    private String type;
    String theForm;
    String thePos;
    String theSeqNr;
    String theSourceLu;

    public ExportLUs() {
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
  }

    static public void main (String[] args) {
        String luFilePath = args[0];
        ExportLUs parser = new ExportLUs();
        parser.readLuFile(luFilePath);
    }

    public void readLuFile (String filePath) {
        try {
            HashMap<String, ArrayList<String>> map = new HashMap<String, ArrayList<String>>();
            FileOutputStream fos = new FileOutputStream(filePath+".monosemous"+".xml");
            FileInputStream fis = new FileInputStream(filePath);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader in = new BufferedReader(isr);
            String inputLine;
            String luBuffer = "";
            String str = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<monosemous_lus>\n";
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
                                    NamedNodeMap attributes = node.getAttributes();
                                    Node att = attributes.getNamedItem("c_lu_id");
                                    theSourceLu = att.getNodeValue();
                                    att = attributes.getNamedItem("c_seq_nr");
                                    theSeqNr = att.getNodeValue();
                                    getFormPos(node);
                                    String key = theForm+"#"+thePos;
                                    if ((theForm.length()>0)) {
                                        if (map.containsKey(key)) {
                                            ArrayList<String> seqs = map.get(key);
                                            seqs.add(theSourceLu+"#"+theSeqNr);
                                            map.put(key, seqs);
                                        }
                                        else {
                                            ArrayList<String> seqs = new ArrayList<String>();
                                            seqs.add(theSourceLu+"#"+theSeqNr);
                                            map.put(key, seqs);
                                        }
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
                                        if ((theForm.length()>0)) {
                                            String key = theForm+"#"+thePos;
                                            if ((theForm.length()>0)) {
                                                if (map.containsKey(key)) {
                                                    ArrayList<String> seqs = map.get(key);
                                                    seqs.add(theSourceLu+"#"+theSeqNr);
                                                    map.put(key, seqs);
                                                }
                                                else {
                                                    ArrayList<String> seqs = new ArrayList<String>();
                                                    seqs.add(theSourceLu + "#" + theSeqNr);
                                                    map.put(key, seqs);
                                                }
                                            }
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
            Set keySet = map.keySet();
            Iterator keys = keySet.iterator();
            while (keys.hasNext()) {
                String key = (String) keys.next();
                String [] fields1 = key.split("#");
                if (fields1.length!=2) {
                    System.out.println("invalid key = " + key);
                    continue;
                }
                ArrayList<String> seqs = map.get(key);
                for (int i = 0; i < seqs.size(); i++) {
                    String seq = seqs.get(i);
                    String [] fields2 = seq.split("#");
                    if (fields2.length!=2) {
                        System.out.println("invalid seq = " + seq);
                    }
                    else {
                        str = "<lu id=\""+fields2[0]+"\" lemma=\""+fields1[0]+"\" pos=\""+fields1[1]+"\" pol=\""+seqs.size()+"\" seq=\""+fields2[1]+"\"/>\n";
                        fos.write(str.getBytes());
                    }

                }
                if (seqs.size()==1) {
                }
            }
            str = "\n</monosemous_lus>\n";
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
    }}
