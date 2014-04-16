package vu.cornetto.cdb;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.Set;
import java.util.Iterator;
import java.io.*;

/**
 * Created by IntelliJ IDEA.
 * User: Piek Vossen
 * Date: 22-apr-2008
 * Time: 16:13:33
 * To change this template use File | Settings | File Templates.
 */
public class MakeSumoTree {
    boolean DEBUG = false;

    HashMap sumoTypeHierarchy;
    HashMap sumoInstances;
    ArrayList parentCache;
    String childCache;

    public MakeSumoTree() {
          sumoTypeHierarchy = new HashMap();
          sumoInstances = new HashMap();
    }



    void readInstances (String filePath) {
        /*
(instance Farad SystemeInternationalUnit)
(instance Weber SystemeInternationalUnit)
         */

        try {
                FileInputStream isLexicon = new FileInputStream (filePath);
                InputStreamReader isr = new InputStreamReader(isLexicon);
                BufferedReader in = new BufferedReader(isr);
                String inputLine;
                while (in.ready()&&(inputLine = in.readLine()) != null) {
                    if (inputLine.indexOf("(")!=-1) {
                        int idx_1 = inputLine.indexOf(" ");
                        int idx_2 = inputLine.indexOf(" ",idx_1+1);
                        int idx_3 = inputLine.indexOf(")",idx_1+2);
                        if ((idx_2 > idx_1) && (idx_1> -1) && balancedBrackets(inputLine)) {
                            String child = inputLine.substring(idx_1+1, idx_2).trim();
                            String parent = inputLine.substring(idx_2, idx_3).trim();
                            if (sumoInstances.containsKey(child)) {
                                ArrayList parents = (ArrayList) sumoInstances.get(child);
                                parents.add(parent);
                                sumoInstances.put(child, parents);
                            }
                            else {
                                ArrayList parents = new ArrayList();
                                parents.add(parent);
                                sumoInstances.put(child, parents);
                            }
                        }
                    }
                    else {
                        //System.out.println("inputLine = " + inputLine);
                    }
                }
                sumoInstances = parentTree (sumoInstances);
                in.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            } catch (IOException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
    }


    void readTypes (String filePath) {
        /*
(subAttribute AbrasiveProductManufacturing OtherNonmetallicMineralProductManufacturing)
(subAttribute AbsoluteMonarchy AuthoritarianRegime)
(subAttribute AbsoluteMonarchy Monarchy)
         */

        try {   FileInputStream isLexicon = new FileInputStream (filePath);
                InputStreamReader isr = new InputStreamReader(isLexicon);
                BufferedReader in = new BufferedReader(isr);
                String inputLine;
                while (in.ready()&&(inputLine = in.readLine()) != null) {
                    if (inputLine.indexOf("(")!=-1) {
                        int idx_1 = inputLine.indexOf(" ");
                        int idx_2 = inputLine.indexOf(" ",idx_1+1);
                        int idx_3 = inputLine.indexOf(")",idx_1+2);
                        if ((idx_3 > idx_2) && (idx_2 > idx_1) && (idx_1> -1) && balancedBrackets(inputLine)) {
                            String child = inputLine.substring(idx_1+1, idx_2).trim();
                            String parent = inputLine.substring(idx_2, idx_3).trim();
                            if (sumoTypeHierarchy.containsKey(child)) {
                                ArrayList parents = (ArrayList) sumoTypeHierarchy.get(child);
                                parents.add(parent);
                                sumoTypeHierarchy.put(child, parents);
                            }
                            else {
                                ArrayList parents = new ArrayList();
                                parents.add(parent);
                                sumoTypeHierarchy.put(child, parents);
                            }
                        }
                    }
                    else {
                        //System.out.println("inputLine = " + inputLine);
                    }
                    sumoTypeHierarchy = parentTree (sumoTypeHierarchy);
                }
                in.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            } catch (IOException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
    }

    HashMap parentTree (HashMap aMap) {
            HashMap newMap = new HashMap();
            Set keySet = aMap.keySet();
            Iterator keys = keySet.iterator();
            long nKeys = 0;
            while (keys.hasNext()) {
                nKeys++;
                String key = (String) keys.next();
/*
                if ((key.equals("PrivateEnterpriseEconomy")) ||
                    (key.equals("CapitalistEconomy"))) {
                    DEBUG = false;
                    System.out.println("n = " + nKeys);
                    System.out.println("key = " + key);
                }
                else {
                    DEBUG = false;
                }
*/

                TaxonomyPath(key, aMap);
                newMap.put(key, parentCache);
                //printParentPath(key, newMap);
            }
            return newMap;
    }

    void TaxonomyPath (String child, HashMap map) {
        parentCache = new ArrayList();
        getParents (child, map);
    }

    void getParents (String child, HashMap map) {
        if (DEBUG) {
            System.out.println("getting parents for child = " + child);
        }
        ArrayList parents = (ArrayList) map.get(child);
        if (parents!=null) {
            for (int i=0;i<parents.size();i++) {
                String parent = (String) parents.get(i);
                if (DEBUG) System.out.println("parent = " + parent);
                if (!parentCache.contains(parent)) {
                    parentCache.add(parent);
                    getParents(parent, map);
                }
                else {
/*
                    parentCache.add(parent);
                    parentCache.add("CYCLE_ERROR");
*/
                    if (DEBUG) System.out.println("CYCLE: child = " + child + "; parent:"+parent);
                    break;
                }
            }
        }
    }

    ArrayList getParents_org (String child, HashMap map) {
        if (DEBUG) {
            System.out.println("getting parents for child = " + child);
        }
        ArrayList allParents = new ArrayList ();
        ArrayList parents = (ArrayList) map.get(child);
        if (parents!=null) {
            allParents = parents;
            for (int i=0;i<parents.size();i++) {
                String parent = (String) parents.get(i);
                if (DEBUG) System.out.println("parent = " + parent);
                ArrayList grandParents = getParents_org(parent, map);
                for (int j=0;j<grandParents.size();j++) {
                    String grandParent = (String) grandParents.get(j);
                    if (DEBUG) System.out.println("grandParent = " + grandParent);
                    if (grandParent.equals(child)) {
                        System.out.println("CYCLE: child = " + child + "; parent:"+parent+"; grandParent:"+ grandParent);
                    }
                    else {
                        if (!allParents.contains(grandParent)) {
                            if (DEBUG) {
                                System.out.println("adding grandParent = " + grandParent);
                            }
                            allParents.add(grandParent);
                        }
                        else {
                            if (DEBUG) {
                                System.out.println("Already have parent = " + parent + "; grandParent:"+ grandParent);
                            }
                            //return allParents;
                        }
                    }
                }
            }
        }
        return allParents;
    }

    static public boolean balancedBrackets (String line) {
        int openBrackets = 0;
        int closedBrackets = 0;
        for (int i=0; i<line.length();i++) {
            char a = line.charAt(i);
            if (a=='(') openBrackets++;
            if (a==')') closedBrackets++;
        }
/*
        System.out.println("closedBrackets = " + closedBrackets);
        System.out.println("closedBrackets = " + closedBrackets);
*/
        return openBrackets==closedBrackets;
    }


    public void saveTree (String filePath, HashMap map) {
        try {
            FileOutputStream fos = new FileOutputStream(filePath);
            Set keySet = map.keySet();
            Iterator keys = keySet.iterator();
            while (keys.hasNext()) {
                String key = (String) keys.next();
                vu.cornetto.util.FileProcessor.storeResult(fos, toParentPath (key, map));
            }
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    String toParentPath (String key, HashMap map) {
        String str = key+":";
        if (map.containsKey(key)) {
            ArrayList parents = (ArrayList) map.get(key);
            for (int i=0;i<parents.size();i++) {
                String parent = (String) parents.get(i);
                str += parent+"#";
            }
        }
        str +="\n";
        return str;
    }

    public static void main (String [] args) {
           String typeFile = args[0];
           String instanceFile = args[1];
           MakeSumoTree tree = new MakeSumoTree();
           tree.readTypes(typeFile);
           System.out.println("sumoTypeHierarchy.size() = "+tree.sumoTypeHierarchy.size());
           tree.saveTree(typeFile+".tree", tree.sumoTypeHierarchy);
           tree.readInstances(instanceFile);
           System.out.println("sumoInstances.size() = "+tree.sumoInstances.size());
           tree.saveTree(instanceFile+".tree", tree.sumoInstances);
    }
}