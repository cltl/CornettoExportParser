package vu.cornetto.cdb;

/**
 * Created by IntelliJ IDEA.
 * User: Piek Vossen
 * Date: 4-dec-2007
 * Time: 11:12:20
 * To change this template use File | Settings | File Templates.
 */
public class CdbReader {

/*    public static HashMap ReadFileToDomMap(String fileName, String anENCODING, String tag) {
        HashMap data = new HashMap();
        if (new File(fileName).exists() ) {
            try {

                Document doc;
                ByteArray array;
                FileInputStream fis = new FileInputStream(fileName);
                CdbSynsetDomParser synsetParser;
                InputStreamReader isr = new InputStreamReader(fis, anENCODING);
                //InputStreamReader isr = new InputStreamReader(fis);
                BufferedReader in = new BufferedReader(isr);
                String inputLine;
                //String xmlContent = "<xml version=\"1.0\">\n";
                String xmlContent = "";
                String endTag = "</"+tag+">";
                while (in.ready()&&(inputLine = in.readLine()) != null) {
                    int idx = inputLine.indexOf(endTag);
                    if (idx!=-1) {
                        xmlContent += inputLine.substring (0, idx+endTag.length());
                        try {
                            //System.out.println("xmlContent = " + xmlContent);
                            array = new ByteArray (xmlContent.getBytes(anENCODING), 0, xmlContent.length());
                            doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new ByteArrayInputStream (xmlContent.getBytes(anENCODING)));
                            synsetParser = new CdbSynsetDomParser(doc);
                            //System.out.println("synsetId = " + synsetId);
                            data.put(synsetParser.synset.getC_sy_id(), synsetParser.synset);
                            if ((data.size()%100)==0) {
                                System.out.println("data.size() = " + data.size());
                            }
                        } catch (SAXException e) {
                            System.out.println("xmlContent = " + xmlContent);
                            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                        } catch (ParserConfigurationException e) {
                            System.out.println("xmlContent = " + xmlContent);
                            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                        }
                        xmlContent ="";
                        //xmlContent = "<xml version=\"1.0\">\n";
                    }
                    else {
                        xmlContent += inputLine;
                    }
                }
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return data;
    }

    public static void ReadFileToWMatrix(String fileName, String anENCODING, String tag, int dom) {
        if (new File(fileName).exists() ) {
            try {
                Document doc;
                ByteArray array;
                FileOutputStream fos = new FileOutputStream (fileName+".lex");
                FileInputStream fis = new FileInputStream(fileName);
                CdbSynsetDomParser synsetParser;
                InputStreamReader isr = new InputStreamReader(fis, anENCODING);
                //InputStreamReader isr = new InputStreamReader(fis);
                BufferedReader in = new BufferedReader(isr);
                String inputLine;
                String start = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n";
                String xmlContent = start;
                String endTag = "</"+tag+">";
                long nSynsets =0;
                while (in.ready()&&(inputLine = in.readLine()) != null) {
                    int idx = inputLine.indexOf(endTag);
                    if (idx!=-1) {
                        xmlContent += inputLine.substring (0, idx+endTag.length());
                        try {
                            nSynsets++;
                            //System.out.println("xmlContent = " + xmlContent);
                            if ((nSynsets%100)==0) {
                                System.out.println("nSynsets = " + nSynsets);
                            }
                            doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new ByteArrayInputStream (xmlContent.getBytes(anENCODING)));
                            synsetParser = new CdbSynsetDomParser(doc);
                            String str = "";
                            if (dom==1) {
                                str = synsetParser.synset.toWMatrixDomainsString();
                            }
                            else if (dom==0) {
                                str = synsetParser.synset.toWMatrixVlisString();
                            }
                            //System.out.println("str = " + str);
                            if (str.length()>0) {
                                FileProcessor.storeResult(fos,str);
                            }
                            //System.out.println("synsetId = " + synsetId);
                        } catch (SAXException e) {
                            //System.out.println("xmlContent = " + xmlContent);
                            //e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                            //return;
                        } catch (ParserConfigurationException e) {
                           // System.out.println("xmlContent = " + xmlContent);
                           // e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                           // return;
                        } catch (Exception e) {
                        }
                        xmlContent =start;
                        //xmlContent = "<xml version=\"1.0\">\n";
                    }
                    else {
                        xmlContent += inputLine;
                    }
                }
                in.close();
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args)
    {
        String process = args[0];
        String filePath = args[1];
        String encoding = "UTF-8";
        String tagName = "cdb_synset";
        if (process.equals("WM0")) {
            CdbReader.ReadFileToWMatrix(filePath, encoding, tagName, 0);
        }
        else if (process.equals("WM1")) {
            CdbReader.ReadFileToWMatrix(filePath, encoding, tagName, 1);
        }
        else {
          //  CdbReader.ReadFileToDomMap(filePath, encoding, tagName);
        }
    }*/

}
