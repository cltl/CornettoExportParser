package vu.cornetto.util;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

public class FileProcessor {
    static public String selectedFile = "";
    static String inString = "";
    static String err = "";
    static String SEP = System.getProperty("file.separator");
    static String UTF_ENCODING = "UTF-8";


    public static void storeResult(FileOutputStream fos, String serverMsg) {
        if (fos!=null) {
            try {
                fos.write(serverMsg.getBytes(UTF_ENCODING));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else {
            System.out.println("FileOutpurStream is null! Cannot store"+serverMsg);
        }
    }


    public static void storeResultRandomAccessFile(RandomAccessFile fos, String serverMsg) {
        if (fos!=null) {
            try {
                //fos.
                fos.setLength(fos.length()+serverMsg.getBytes().length);
                fos.seek(0);
                fos.write(serverMsg.getBytes());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else {
            System.out.println("RandomAccessFile is null! Cannot store"+serverMsg);
        }
    }

    public static void storeResult(FileOutputStream fos, String serverMsg, String ENCODING) {
        if (fos!=null) {
            try {
                fos.write(serverMsg.getBytes(ENCODING));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else {
            System.out.println("FileOutpurStream is null! Cannot store"+serverMsg);
        }
    }

    public static void storeResult(OutputStreamWriter fos, String serverMsg) {
        if (fos!=null) {
            try {
                fos.write(serverMsg);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else {
            System.out.println("FileOutpurStream is null! Cannot store"+serverMsg);
        }
    }

    static public int copyFile(File inputFile, String outputPath, String outputName, String ENCODING) {
        if (!inputFile.exists()) {
            return -1;
        }
        try {
            DataInputStream in = new DataInputStream(new FileInputStream(inputFile));
            byte[] buffer = new byte[(int) inputFile.length()];
            in.readFully(buffer);
            in.close();
            File outputFolder = new File(outputPath);
            if (!outputFolder.exists()) {
                return -2;
            }
            File outputFile = new File(outputPath + SEP + outputName);
            DataOutputStream out = new DataOutputStream(new FileOutputStream(outputFile));
            out.write(buffer);
        } catch (IOException e) {
            return -3;
        }
        return 0;
    }

    static public String ReadFileToStringBatch(String fileName, String anENCODING) {
        String text = "";
        try {
            File f = new File(fileName);
            if (f.exists()) {
                DataInputStream in = new DataInputStream(new FileInputStream(f));
                byte[] buffer = new byte[(int) f.length()];
                in.readFully(buffer);
                in.close();
                text = new String(buffer, 0, buffer.length, anENCODING);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return text;
    }

    static public String ReadFileToString(String fileName, String anENCODING) {
        String text = "";
        try {
            File f = new File(fileName);
            if (f.exists()) {
                DataInputStream in = new DataInputStream(new FileInputStream(f));
                byte[] buffer = new byte[(int) f.length()];
                in.readFully(buffer);
                in.close();
                text = new String(buffer, 0, buffer.length, anENCODING);
                if (text.getBytes(anENCODING).length != buffer.length) {
                    //AlertDialog dia = new AlertDialog(new JFrame(), "Could not read complete text!", "Please, check the character encoding of the input file.");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return text;
    }

    static public byte[]  ReadFileToBytes(String fileName) {
        byte[] buffer = null;
        try {
            File f = new File(fileName);
            if (f.exists()) {
                DataInputStream in = new DataInputStream(new FileInputStream(f));
                buffer = new byte[(int) f.length()];
                in.readFully(buffer);
                in.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return buffer;
    }

    static public String ReadFileToString(String fileName) {
        String text = "";
        try {
            File f = new File(fileName);
            if (f.exists()) {
                DataInputStream in = new DataInputStream(new FileInputStream(f));
                byte[] buffer = new byte[(int) f.length()];
                in.readFully(buffer);
                in.close();
                text = new String(buffer, 0, buffer.length);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return text;
    }

    static public String ReadFileSampleToString(String fileName, String anENCODING, int MAX) {
        int SIZE = MAX;
        int PART1 = 0;
        int PART2 = 0;
        try {
            File f = new File(fileName);
            DataInputStream in = new DataInputStream(new FileInputStream(f));
            PART1 = (int) f.length() / 3;
            PART2 = (int) f.length() / 2;
/*
             System.out.println("Length:"+(int) f.length());
             System.out.println("PART1:"+PART1);
             System.out.println("PART2:"+PART2);
*/
            if (PART1 < MAX) {
                SIZE = PART1;
//                 System.out.println("SIZE reduced to:"+SIZE);
            }
            SIZE = SIZE;
/*
             System.out.println("SIZE:"+SIZE);
*/
            byte[] buffer = new byte[(int) SIZE];
//             in.readFully(buffer);

            in.read(buffer, 0, SIZE); // reads from offset a number of bytes
//             System.out.println("Read buffer");
            String text1 = new String(buffer, 0, buffer.length, anENCODING);
//             System.out.println("TEXT1:"+text1);

            in.skipBytes(PART1);
            in.read(buffer, 0, SIZE);
//             System.out.println("Read buffer");
            String text2 = new String(buffer, 0, buffer.length, anENCODING);
//             System.out.println("TEXT2:"+text2);

            in.skipBytes(PART2);
            in.read(buffer, 0, SIZE);
//             System.out.println("Read buffer");
            String text3 = new String(buffer, 0, buffer.length, anENCODING);
//             System.out.println("TEXT3:"+text3);

            String text = text1 + text2 + text3;
//             System.out.println(text);
            in.close();
            return text;
        } catch (IOException e) {
            return "";
        }
    }


    static public DefaultListModel ReadFileToList(String fileName, String anENCODING) {
        DefaultListModel lineList = new DefaultListModel();
        if (new File (fileName).exists() ) {
            try {
                FileInputStream fis = new FileInputStream(fileName);
                InputStreamReader isr = new InputStreamReader(fis, anENCODING);
                BufferedReader in = new BufferedReader(isr);
                String inputLine;
                while (in.ready()&&(inputLine = in.readLine()) != null) {
                    //System.out.println(inputLine);
                    if (inputLine.trim().length()>0) {
                        lineList.addElement(inputLine);
                    }
                }
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return lineList;
    }

    static public ArrayList ReadFileToArrayList(String fileName) {
        ArrayList lineList = new ArrayList();
        if (new File (fileName).exists() ) {
            try {
                FileInputStream fis = new FileInputStream(fileName);
                InputStreamReader isr = new InputStreamReader(fis);
                BufferedReader in = new BufferedReader(isr);
                String inputLine;
                while (in.ready()&&(inputLine = in.readLine()) != null) {
                   // System.out.println(inputLine);
                    if (inputLine.trim().length()>0) {
                        lineList.add(inputLine.trim());
                    }
                }
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return lineList;
    }

    static public ArrayList ReadFileToArrayListIgnoreCase(String fileName) {
        ArrayList lineList = new ArrayList();
        if (new File (fileName).exists() ) {
            try {
                FileInputStream fis = new FileInputStream(fileName);
                InputStreamReader isr = new InputStreamReader(fis);
                BufferedReader in = new BufferedReader(isr);
                String inputLine;
                while (in.ready()&&(inputLine = in.readLine()) != null) {
                   // System.out.println(inputLine);
                    if (inputLine.trim().length()>0) {
                        lineList.add(inputLine.toLowerCase().trim());
                    }
                }
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return lineList;
    }

    static public String [] ReadFileToStringArray(String fileName) {
        ArrayList lineList = new ArrayList();
        String [] results = null;
        if (new File (fileName).exists() ) {
            try {
                FileInputStream fis = new FileInputStream(fileName);
                InputStreamReader isr = new InputStreamReader(fis);
                BufferedReader in = new BufferedReader(isr);
                String inputLine;
                while (in.ready()&&(inputLine = in.readLine()) != null) {
                   // System.out.println(inputLine);
                    if (inputLine.trim().length()>0) {
                        lineList.add(inputLine.trim());
                    }
                }
                results = new String [lineList.size()];
                for (int i=0; i<lineList.size();i++) {
                    String line = (String) lineList.get(i);
                    results[i] = line;
                }
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return results;
    }

    static public ArrayList ReadFileToLowerCaseArrayList(String fileName) {
        ArrayList lineList = new ArrayList();
        if (new File (fileName).exists() ) {
            try {
                FileInputStream fis = new FileInputStream(fileName);
                InputStreamReader isr = new InputStreamReader(fis);
                BufferedReader in = new BufferedReader(isr);
                String inputLine;
                while (in.ready()&&(inputLine = in.readLine()) != null) {
                   // System.out.println(inputLine);
                    if (inputLine.trim().length()>0) {
                        lineList.add(inputLine.toLowerCase().trim());
                    }
                }
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return lineList;
    }

    static public ArrayList ReadFileToArrayList(String fileName, String anENCODING) {
        ArrayList lineList = new ArrayList();
        if (new File (fileName).exists() ) {
            try {
                FileInputStream fis = new FileInputStream(fileName);
                InputStreamReader isr = new InputStreamReader(fis, anENCODING);
                BufferedReader in = new BufferedReader(isr);
                String inputLine = "";
                long cnt = 0;
                while (in.ready()&&(inputLine = in.readLine()) != null) {
                    //System.out.println(inputLine);
                    if (inputLine.trim().length()>0) {
                        lineList.add(inputLine);
                        cnt++;
                    }
                }
//                System.out.println("last inputLine = " + inputLine);
                System.out.println("read cnt = " + cnt+" lines....");
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return lineList;
    }

    static public ArrayList ReadFileLowerCaseToArrayList(String fileName, String anENCODING) {
        ArrayList lineList = new ArrayList();
        if (new File (fileName).exists() ) {
            try {
                FileInputStream fis = new FileInputStream(fileName);
                InputStreamReader isr = new InputStreamReader(fis, anENCODING);
                BufferedReader in = new BufferedReader(isr);
                String inputLine;
                while (in.ready()&&(inputLine = in.readLine()) != null) {
                    //System.out.println(inputLine);
                    if (inputLine.trim().length()>0) {
                        lineList.add(inputLine.toLowerCase());
                    }
                }
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return lineList;
    }

    static public Vector ReadFileToVector(String fileName, String anENCODING) {
        Vector lineVector = new Vector();
        if (new File (fileName).exists() ) {
            try {
                FileInputStream fis = new FileInputStream(fileName);
                InputStreamReader isr = new InputStreamReader(fis, anENCODING);
                BufferedReader in = new BufferedReader(isr);
                String inputLine;
                while (in.ready()&&(inputLine = in.readLine()) != null) {
                    //System.out.println(inputLine);
                    if (inputLine.trim().length()>0) {
                        lineVector.addElement(inputLine.trim());
                    }
                }
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return lineVector;
    }

    static public HashMap ReadFileToStringHashMap(String separator, String fileName) {
        HashMap lineHashMap = new HashMap();
        if (new File (fileName).exists() ) {
            try {
                FileInputStream fis = new FileInputStream(fileName);
                InputStreamReader isr = new InputStreamReader(fis);
                BufferedReader in = new BufferedReader(isr);
                String inputLine;
                while (in.ready()&&(inputLine = in.readLine()) != null) {
                    //System.out.println(inputLine);
                    if (inputLine.trim().length()>0) {
                        int idx_s = inputLine.indexOf(separator);
                        if (idx_s>-1) {
                            String key = inputLine.substring(0, idx_s).trim();
                            String value = inputLine.substring(idx_s+1).trim();
/*
                            System.out.println("key = " + key);
                            System.out.println("value = " + value);
*/
                            lineHashMap.put(key, value);
                        }
                    }
                }
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return lineHashMap;
    }

    static public HashMap ReadFileToStringHashMap(String separator, String fileName, String anENCODING) {
        HashMap lineHashMap = new HashMap();
        if (new File (fileName).exists() ) {
            try {
                FileInputStream fis = new FileInputStream(fileName);
                InputStreamReader isr = new InputStreamReader(fis, anENCODING);
                BufferedReader in = new BufferedReader(isr);
                String inputLine;
                while (in.ready()&&(inputLine = in.readLine()) != null) {
                    //System.out.println(inputLine);
                    if (inputLine.trim().length()>0) {
                        int idx_s = inputLine.indexOf(separator);
                        if (idx_s>-1) {
                            String key = inputLine.substring(0, idx_s).trim();
                            String value = inputLine.substring(idx_s+1).trim();
                            //System.out.println("value = " + value);
                            lineHashMap.put(key, value);
                        }
                    }
                }
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return lineHashMap;
    }
    // Save current files: handle not yet having a filename; report to statusBar
    static public boolean SaveFile(String outFileName, String content) {
        try {
            // Open a file of the current name.
            File outFile = new File(outFileName);
            // Create an output writer that writes to that file.
            // FileWriter handles international character encoding conventions
            FileWriter outWriter = new FileWriter(outFile);
            outWriter.write(content);
            outWriter.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Save current files: handle not yet having a filename; report to statusBar
    static public boolean SaveFile(String outFileName, String content, String ENCODING) {
        try {
            // Open a file of the current name.
            File outFile = new File(outFileName);
            // Create an output writer that writes to that file.
            // FileWriter handles international character encoding conventions
            FileOutputStream outWriter = new FileOutputStream(outFile);
//           System.out.println("ENCODING:"+ENCODING);
            outWriter.write(content.getBytes(ENCODING));
            outWriter.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    static public String selectOutputFile(JFrame parent, String inputPath, final String extension) {
        File inputFile = new File(inputPath);
        String outputPath = "";
        JFileChooser fc = new JFileChooser(inputFile.getParent());
        fc.setDialogType(JFileChooser.SAVE_DIALOG);
        fc.setFileFilter(new FileFilter() {
            public boolean accept(File f) {
                return (f.getName().toLowerCase().endsWith(extension) ||
                        f.isDirectory());
            }

            public String getDescription() {
                return (extension + " File");
            }
        });
        int returnVal = fc.showSaveDialog(parent);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fc.getSelectedFile();
            outputPath = file.getAbsolutePath();
        }
        fc.setVisible(false);
        return outputPath;
    }

    static public String getFirstFileWithExtension(String inputPath, String extension) {
        File[] theFileList = null;
        File lF = new File(inputPath);
        if ((lF.canRead()) && lF.isDirectory()) {
            theFileList = lF.listFiles();
            for (int i = 0; i < theFileList.length; i++) {
                String newFilePath = theFileList[i].getAbsolutePath();
                if (theFileList[i].isFile()) {
                    if (newFilePath.toLowerCase().endsWith(extension.toLowerCase())) {
                        return newFilePath;
                    }
                }
            }
        }
        return "";
    }
    static public String[] makeFlatFileList(String inputPath, String filter) {
        String[] acceptedFileList = new String[0];
        File[] theFileList = null;
        File lF = new File(inputPath);
        if ((lF.canRead()) && lF.isDirectory()) {
            theFileList = lF.listFiles();
            for (int i = 0; i < theFileList.length; i++) {
                String newFilePath = theFileList[i].getAbsolutePath();
                if (theFileList[i].isFile()) {
                    if (acceptFile(newFilePath, filter)) {
                        acceptedFileList = addToFileLists(acceptedFileList, newFilePath);
                    }
                }
            }
        }
        return acceptedFileList;
    }

        static public String[] makeRecursiveFileList(String inputPath, String theFilter) {
        long nSelectedFolders = 1;
        String[] acceptedFileList = new String[0];
        String[] nestedFileList = null;
        File[] theFileList = null;
        File lF = new File(inputPath);
        if ((lF.canRead()) && lF.isDirectory()) {
            theFileList = lF.listFiles();
            for (int i = 0; i < theFileList.length; i++) {
                //        System.out.println(theFileList[i].getPath());
                if (theFileList[i].isDirectory()) {
                    nSelectedFolders++;
                }
            }
            for (int i = 0; i < theFileList.length; i++) {
                String newFilePath = theFileList[i].getAbsolutePath();
                if (theFileList[i].isDirectory()) {
                    String nextFileList [] = makeRecursiveFileList(newFilePath, theFilter);
                    nestedFileList = mergeFileLists(nestedFileList, nextFileList);
                    nextFileList = null;
                } else {
                    if (acceptFile(newFilePath, theFilter)) {
                        acceptedFileList = addToFileLists(acceptedFileList, newFilePath);
                    }
                }
            }
            acceptedFileList = mergeFileLists(acceptedFileList, nestedFileList);
            nestedFileList = null;
        } else {
            System.out.println("Cannot access file:" + inputPath + "#");
            if (!lF.exists()) {
                System.out.println("File does not exist!");
            }
        }
        return acceptedFileList;
    }

    static boolean acceptFile(String fileName, String filter) {
/*
       System.out.println("fileName:"+fileName);
       System.out.println("mineFilter:"+mineFilter);
*/
        if (fileName.toLowerCase().endsWith(filter.toLowerCase())) {
            return true;
        } else {
            return false;
        }
    }

    static String[] mergeFileLists(String[] list1, String[] list2) {
        int size = 0;
        String[] mergedList = null;
        if ((list1 != null) && (list2 != null)) {
            size = list1.length + list2.length;
            mergedList = new String[size];
            int k = 0;
            for (int i = 0; i < list1.length; i++) {
                mergedList[k] = list1[i];
                k++;
            }
            for (int i = 0; i < list2.length; i++) {
                mergedList[k] = list2[i];
                k++;
            }
        } else if ((list1 == null) && (list2 != null)) {
            return list2;
        } else if ((list1 != null) && (list2 == null)) {
            return list1;
        }
        return mergedList;
    }

    static String[] addToFileLists(String[] list1, String element) {
        int size = list1.length + 1;
        String[] mergedList = new String[size];
        int k = 0;
        for (int i = 0; i < list1.length; i++) {
            mergedList[k] = list1[i];
            k++;
        }
        mergedList[k] = element;
        return mergedList;
    }

    static public String[] makeFlatDirList(String inputPath) {
        String[] acceptedFileList = new String[0];
        File[] theFileList = null;
        File lF = new File(inputPath);
        if (lF.canRead()) {
            theFileList = lF.listFiles();
            for (int i = 0; i < theFileList.length; i++) {
                String newFilePath = theFileList[i].getAbsolutePath();
                if (theFileList[i].isDirectory()) {
                    acceptedFileList = addToFileLists(acceptedFileList, newFilePath);
                }
            }
        }
        return acceptedFileList;
    }
    
    
} /// end of FileProcessor
