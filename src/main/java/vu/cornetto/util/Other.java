package vu.cornetto.util;

import javax.swing.*;
import java.text.SimpleDateFormat;
import java.text.DateFormat;
import java.text.ParsePosition;
import java.text.DecimalFormat;
import java.util.*;
import java.net.URL;
import java.net.URLConnection;
import java.io.File;

/**
 * Created by IntelliJ IDEA.
 * User: Piek Vossen
 * Date: 16-apr-2008
 * Time: 15:46:22
 * To change this template use File | Settings | File Templates.
 */
public class Other {

    static String SEP = System.getProperty("file.separator");
	static boolean DEBUG = false;

    static public  String addCDATAstring (String value) {
        String CDATAvalue = value;
        if (!value.startsWith("<![CDATA[")) {
            CDATAvalue = "<![CDATA["+value+"]]>";
        }
        return CDATAvalue.trim();
    }

    static public long timeString2Seconds (String time) {
        long seconds = 0;
        long minute = 60;
        long hour = 3600;
        if (time.equalsIgnoreCase("ANY")) {
            seconds = -1;
        }
        else if (time.equalsIgnoreCase("1 min")) {
            seconds = minute;
        }
        else if (time.equalsIgnoreCase("2 min")) {
            seconds = minute*2;
        }
        else if (time.equalsIgnoreCase("3 min")) {
            seconds = minute*3;
        }
        else if (time.equalsIgnoreCase("4 min")) {
            seconds = minute*4;
        }
        else if (time.equalsIgnoreCase("5 min")) {
            seconds = minute*5;
        }
        else if (time.equalsIgnoreCase("10 min")) {
            seconds = minute*10;
        }
        else if (time.equalsIgnoreCase("15 min")) {
            seconds = minute*15;
        }
        else if (time.equalsIgnoreCase("20 min")) {
            seconds = minute*20;
        }
        else if (time.equalsIgnoreCase("30 min")) {
            seconds = minute*20;
        }
        else if (time.equalsIgnoreCase("45 min")) {
            seconds = minute*45;
        }
        else if (time.equalsIgnoreCase("1 hour")) {
            seconds = hour;
        }
        else if (time.equalsIgnoreCase("2 hours")) {
            seconds = hour*2;
        }
        else if (time.equalsIgnoreCase("3 hours")) {
            seconds = hour*3;
        }
        else if (time.equalsIgnoreCase("4 hours")) {
            seconds = hour*4;
        }
        else if (time.equalsIgnoreCase("5 hours")) {
            seconds = hour*5;
        }
        else if (time.equalsIgnoreCase("6 hours")) {
            seconds = hour*6;
        }
        else if (time.equalsIgnoreCase("12 hours")) {
            seconds = hour*12;
        }
        else if (time.equalsIgnoreCase("24 hours")) {
            seconds = hour*24;
        }
        return seconds;
    }

    static public String timeSeconds2String (long seconds) {
        String time = "ANY";
        long minute = 60;
        long hour = 3600;
        if (seconds == 60) {
            time = "1 min";
        }
        else if (seconds/minute == 2) {
            time ="2 min";
        }
        else if (seconds/minute == 3) {
            time ="3 min";
        }
        else if (seconds/minute == 4) {
            time ="4 min";
        }
        else if (seconds/minute == 5) {
            time ="5 min";
        }
        else if (seconds/minute == 10) {
            time="10 min";
        }
        else if (seconds/minute == 15) {
            time="15 min";
        }
        else if (seconds/minute == 20) {
            time="20 min";
        }
        else if (seconds/minute == 30) {
            time="30 min";
        }
        else if (seconds/minute == 45) {
            time="45 min";
        }
        else if (seconds == hour) {
            time="1 hour";
        }
        else if (seconds/hour==2) {
            time="2 hours";
        }
        else if (seconds/hour==3) {
            time="3 hours";
        }
        else if (seconds/hour==4) {
            time="4 hours";
        }
        else if (seconds/hour==5) {
            time="5 hours";
        }
        else if (seconds/hour==6) {
            time="6 hours";
        }
        else if (seconds/hour==12) {
            time="12 hours";
        }
        else if (seconds/hour==24) {
            time="24 hours";
        }
        return time;
    }

    static public int findEqualChar (char aChar, String form2, int pos) {
        for (int i=pos;i<form2.length();i++) {
            if (aChar==form2.charAt(i)) {
           //     System.out.println("Found:"+aChar+" at:"+i+ " in:"+form2);
                return i;
            }
        }
        return -1;
    }

    static public int editDistance (String form1, String form2, int maxDistance) {

/*
        System.out.println("form1:"+form1);
        System.out.println("form2:"+form2);
*/

        int editDistance = 0;
        int k = 0;
        int i = 0;
        if (form1.length()<=form2.length()) {
            for (i=0;i<form1.length();i++) {
                if ((editDistance>maxDistance) ||
                    (k>=form2.length())){
                    return editDistance;
                }
/*
                System.out.println("editDistance:"+editDistance);
                System.out.println("i:"+i+";"+form1.charAt(i));
                System.out.println("k:"+k+";"+form2.charAt(k));
*/
                if (form1.charAt(i)!=form2.charAt(k)) {
                    k = findEqualChar(form1.charAt(i),form2, i);
                    if (k!=-1) {
                       editDistance += k-i;
                    }
                    else {
                       editDistance += form2.length()-i;
                        return editDistance;
                    }
                }
                k++;
            }
            editDistance += (form2.length()-k);
        }
        else {
            for (i=0;i<form2.length();i++) {
                if ((editDistance>maxDistance) ||
                    (k>=form1.length())){
                    return editDistance;
                }
/*
                System.out.println("editDistance:"+editDistance);
                System.out.println("i:"+i+";"+form2.charAt(i));
                System.out.println("k:"+k+";"+form1.charAt(k));
*/
                if (form2.charAt(i)!=form1.charAt(k)) {
                    k = findEqualChar(form2.charAt(i),form1, i);
                    if (k!=-1) {
                       editDistance += k-i;
                    }
                    else {
                       editDistance += form1.length()-i;
                       return editDistance;
                    }
                }
                k++;
            }
            editDistance += (form1.length()-k);
        }
//        System.out.println("editDistance:"+editDistance);
        return editDistance;
    }

    static public ArrayList stringToWordArrayList (String text) {
        ArrayList wordList = new ArrayList ();
        String word = "";
        int pos = 0;
        int next = nextSeparator(text, pos);
        while (next!=-1) {
            word = text.substring(pos, next).trim();
            wordList.add(word);
            pos = next+1;
            next = nextSeparator(text, pos);
        }
        word = text.substring(pos).trim();
        wordList.add(word);
        return wordList;
    }

    static public String getNextWord (String text, int pos) {
        String word = "";
        int idx_e = nextSeparator(text, pos);
        if (idx_e!=-1) {
           word = text.substring(pos, idx_e).trim();
        }
        else {
            word = text.substring(pos).trim();
        }
        return word;
    }

    static public String getLastWord (String text) {
        String word = "";
        int idx_e = text.lastIndexOf(" ");
        if (idx_e!=-1) {
           word = text.substring(idx_e).trim();
        }
        else {
            word = text.trim();
        }
        return word;
    }

    static public int nextSeparator (String input, int pos) {
        int next_pos = -1;
        int space = input.indexOf(" ", pos);
        int tab = input.indexOf("\t", pos);
        int newline = input.indexOf("\n", pos);
        if (space != -1) {
            next_pos = space;
        }
        if ((tab != -1) && ((tab<next_pos) || (next_pos ==-1))) {
            next_pos = tab;
        }
        if ((newline != -1) && ((newline<next_pos)  || (next_pos ==-1))) {
            next_pos = newline;
        }
        return next_pos;
    }

    static public DefaultListModel multiWordToWordList(String multiWord) {
        DefaultListModel wordList = new DefaultListModel();
        int pPos = 0;
        int nPos = nextSeparator (multiWord, pPos);
        while (nPos!=-1) {
            String nWord = multiWord.substring(pPos, nPos).trim();
            if (nWord.length()>0) {
                wordList.addElement(nWord);
            }
            pPos = nPos+1;
            nPos = nextSeparator (multiWord, pPos);
        }
        String nWord = multiWord.substring(pPos).trim();
        if (nWord.length()>0) {
            wordList.addElement(nWord);
        }
        return wordList;
    }

    static public ArrayList multiWordToWordArrayList(String multiWord) {
        ArrayList wordList = new ArrayList();
        int pPos = 0;
        int nPos = nextSeparator (multiWord, pPos);
        while (nPos!=-1) {
            String nWord = multiWord.substring(pPos, nPos).trim();
            if (nWord.length()>0) {
                wordList.add(nWord);
            }
            pPos = nPos+1;
            nPos = nextSeparator (multiWord, pPos);
        }
        String nWord = multiWord.substring(pPos).trim();
        if (nWord.length()>0) {
            wordList.add(nWord);
        }
        return wordList;
    }

    static public String getFileWithExtension (String filePath, String extension) {
        String extensionFile = "";
        int idx= filePath.lastIndexOf(".");
        if (idx!=-1) {
                extensionFile = filePath.substring(0, idx)+"."+extension;
        }
        return extensionFile;
    }

    static public String removeDoubleQuotes(String s1) {
        String s2 = s1;
        if (s1.startsWith("\"")) {
            s2 = s2.substring(1);
        }
        if (s1.endsWith("\"")) {
            s2 = s2.substring(0,s2.length()-1);
        }
        return s2;
    }

    static public int validUrl (String aUrlString) {
        int errno = 0;
        if (aUrlString.trim().length()>0) {
            String urlString = aUrlString;
            if (!urlString.startsWith("http://")) {
                urlString = "http://"+aUrlString;
            }
            try {
                URL url = new URL (urlString);
                try {
                    URLConnection connection = url.openConnection();
//                if (connection.getAllowUserInteraction()) {
                    try {
                        connection.connect();
                        errno = 0;
                    }
                    catch (Exception ee) {
//                        ee.printStackTrace();
                        System.out.println(ee.getMessage());
                        errno = -2;
                    }
                }
                catch (Exception eee) {
                    System.out.println(eee.getMessage());
 //                   eee.printStackTrace();
                    errno = -3;
                }
            }
            catch (Exception eeee){
                System.out.println(eeee.getMessage());
//                eeee.printStackTrace();
                errno = -4;
            }
        }
        else {
            errno =  -1;
        }
        return errno;
    }

    static public String getUrlFromHtmlBaseRef(String htmlString) {
        String urlString = "";
        int idx_s = htmlString.indexOf("<base target=_blank href=");
        int idx_e = -1;
        if (idx_s!=-1) {
            idx_e = htmlString.indexOf(">", idx_s);
            if (idx_e>idx_s) {
              urlString = htmlString.substring(idx_s+25, idx_e);
            }
        }
        return urlString;
    }

    static public String  tabString(int n, String symbol, String base) {
        String tab = base;
        for (int i = 0; i<=n;i++) {
            tab+= symbol;
        }
        return tab;
    }
    static public String getDateString () {
        DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT, new Locale("us", "en"));
        Date theDate = new Date (System.currentTimeMillis());
        return df.format(theDate);
    }

    static public String getDateString (long date) {
        DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT, new Locale("us", "en"));
        Date theDate = new Date (date);
        return df.format(theDate);
    }

    static public String getDateStringPrecise () {
        DateFormat df = DateFormat.getDateInstance(DateFormat.MEDIUM, new Locale("us", "en"));
        Date theDate = new Date (System.currentTimeMillis());
        return df.format(theDate);
    }

    static public String getDateStringPrecise (long date) {
        DateFormat df = DateFormat.getDateInstance(DateFormat.MEDIUM, new Locale("us", "en"));
        Date theDate = new Date (date);
        return df.format(theDate);
    }

    static public String getDateStringSafeName () {
        SimpleDateFormat formatter
            = new SimpleDateFormat ("yyyy.MM.dd");
        Date currentTime_1 = new Date(System.currentTimeMillis());
        String dateString = formatter.format(currentTime_1);
        return dateString;
    }

    static public String getDateStringVeryPrecise () {
        SimpleDateFormat formatter
            = new SimpleDateFormat ("yyyy.MM.dd 'at' hh:mm:ss");
        Date currentTime_1 = new Date(System.currentTimeMillis());
        String dateString = formatter.format(currentTime_1);
        return dateString;
    }

    static public String getDateStringVeryPrecise (long date) {
        SimpleDateFormat formatter
            = new SimpleDateFormat ("yyyy.MM.dd 'at' hh:mm:ss");
        Date currentTime_1 = new Date(date);
        String dateString = formatter.format(currentTime_1);
        return dateString;
    }

    static public String getDateStringVeryPrecise2 (long date) {
        SimpleDateFormat formatter
            = new SimpleDateFormat ("yyyy.MM.dd G 'at' hh:mm:ss a zzz");
        Date currentTime_1 = new Date(date);
        String dateString = formatter.format(currentTime_1);
        return dateString;
    }

    static public String getDateStringVeryPrecise2 () {
        SimpleDateFormat formatter
            = new SimpleDateFormat ("yyyy.MM.dd G 'at' hh:mm:ss a zzz");
        Date currentTime_1 = new Date(System.currentTimeMillis());
        String dateString = formatter.format(currentTime_1);
        return dateString;
    }


    static public String getShortDateString () {
        DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT, new Locale("us", "en"));
        Date currentTime_1 = new Date(System.currentTimeMillis());
        String dateString = df.format(currentTime_1);
        return dateString;
    }

    static public String getMediumDateString () {
        DateFormat df = DateFormat.getDateInstance(DateFormat.MEDIUM, new Locale("us", "en"));
        Date currentTime_1 = new Date(System.currentTimeMillis());
        String dateString = df.format(currentTime_1);
        return dateString;
    }

    static public Date getDateValue (String date) {
        Date theDate = null;
        DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT, new Locale("us", "en"));
        try {
            theDate = df.parse(date);
        }
        catch (Exception e) {}
        return theDate;
    }

    static public Date getDateValuePrecise (String date) {
        Date theDate = null;
        DateFormat df = DateFormat.getDateInstance(DateFormat.MEDIUM, new Locale("us", "en"));
        try {
            theDate = df.parse(date);
        }
        catch (Exception e) {}
        return theDate;
    }

    static public Date getDateValueVeryPrecise (String dateString) {
        SimpleDateFormat formatter
            = new SimpleDateFormat ("yyyy.MM.dd 'at' hh:mm:ss");
        Date theDate = null;
        try {
            ParsePosition pos = new ParsePosition(0);
            theDate = formatter.parse(dateString, pos);
//            theDate = df.parse(date);
        }
        catch (Exception e) {}
        return theDate;
    }
    // Parse the previous string back into a Date.

	static public double convertDoubleToNumber (double aDouble) {
	     long aLong = (long) (aDouble*10.0);
		 double newNumber = aLong;
//		 System.out.println("aDOuble:"+aDouble+"; aLong:"+aLong+"; newNumber:"+newNumber);
		 return (double) (newNumber/10.0);
	}

	//doubleStringFormat("###.##", 123456.789);
	static public String doubleStringFormat(String pattern, double number) {
        //Locale.setDefault(Locale.US);
        Locale.setDefault(Locale.GERMAN);
	    DecimalFormat myFormatter = new DecimalFormat(pattern);
	    String output = myFormatter.format(number);

/*
        System.out.println("Double inputt:"+number);
        System.out.println("Double output:"+output);
*/

	    return output;
	}

	static public String longStringFormat(String pattern, long number) {
	    DecimalFormat myFormatter = new DecimalFormat(pattern);
	    String output = myFormatter.format(number);
	    return output;
	}

	static public boolean checkStringNumber(String number) {
      if (number.trim().length()==0) {
          return false;
      }
      boolean DIGIT = false;
	  String numberElements = "1234567890.,";
      String strictNumberElements = "1234567890";
	  for (int i = 0; i<number.length();i++) {
		String aChar = number.substring(i, i+1);
        if (strictNumberElements.indexOf(aChar) != -1) {
            DIGIT = true;
        }
		if (numberElements.indexOf(aChar) == -1)
		{  return false;}
	  }
	  return DIGIT;
	}


    static public int firstDigit(String number, int pos) {
            if (number.trim().length()==0) {
                return -1;
            }
            String numberElements = "1234567890";
            for (int i = pos; i<number.length();i++) {
              String aChar = number.substring(i, i+1);
              if (numberElements.indexOf(aChar) != -1)
              {  return i;}
            }
            return -1;
      }

    static public int lastDigit(String number, int pos) {
            if (number.trim().length()==0) {
                return -1;
            }
            String numberElements = "1234567890";
            for (int i = pos; i>1;i--) {
              String aChar = number.substring(i-1, i);
              if (numberElements.indexOf(aChar) != -1)
              {  return i;}
            }
            return -1;
      }

    static public int firstNonDigit(String number, int pos) {
           if (number.trim().length()==0) {
               return -1;
           }
           String numberElements = "1234567890";
           for (int i = pos; i<number.length();i++) {
             String aChar = number.substring(i, i+1);
             if (numberElements.indexOf(aChar) == -1)
             {  return i;}
           }
           return number.length();
     }

    static public int firstNonNumber(String number, int pos) {
           if (number.trim().length()==0) {
               return -1;
           }
           String numberElements = "1234567890., "; /// space is allowed for "12 000"
           for (int i = pos; i<number.length();i++) {
             String aChar = number.substring(i, i+1);
             if (numberElements.indexOf(aChar) == -1)
             {  return i;}
           }
           return number.length();
     }

    ///// RIGHT TO LEFT
    static public int lastDigit(String number) {
           if (number.trim().length()==0) {
               return -1;
           }
           String numberElements = "1234567890";
           for (int i = number.length()-1; i>0;i--) {
             String aChar = number.substring(i, i-1);
             if (numberElements.indexOf(aChar) != -1)
             {  return i;}
           }
           return -1;
     }

    //// RIGHT TO LEFT
    static public int lastNonDigit(String number, int pos) {
           if (number.trim().length()==0) {
               return -1;
           }
           String numberElements = "1234567890";
           for (int i = pos; i>0;i--) {
             String aChar = number.substring(i-1, i);
             //System.out.println("aChar = " + aChar);
             if (numberElements.indexOf(aChar) == -1)
             {   //System.out.println("NON-DIGIT aChar = " + aChar);
                 return i;}
           }
           return -1;
     }

    public static String reverseString (String inputString) {
        String result = "";
        for (int i = inputString.length()-1; i>=0; i--)
            result +=  inputString.charAt(i);
        return result;
    }

	static public boolean checkAlphaNumeric(String text) {
//	  System.out.println("number:"+number);
	  String numberElements = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890.,";
	  for (int i = 0; i<text.length();i++) {
		String aChar = text.substring(i, i+1);
		if (numberElements.indexOf(aChar) > -1)
		{  return true;}
	  }
//	  System.out.println("OK");
	  return false;
	}

    static public int firstAlphaNumeric(String text) {
	  String numberElements = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890.,";
	  for (int i = 0; i<text.length();i++) {
		String aChar = text.substring(i, i+1);
		if (numberElements.indexOf(aChar) > -1)
		{  return i;}
	  }
	  return -1;
	}


	static public double stringToDouble(String number) {
	    double output = -1;
	    if (checkStringNumber(number))
	    { Double dNumber = new Double(number);
		output = dNumber.doubleValue();
	    }
	    return output;
	}

/*
    static public long doubleStringToLong(String number) {
        Long output = new Long (-1);
        if (checkStringNumber(number)) {
            int idx = number.lastIndexOf(".");
            if (idx ==-1) {
                idx = number.lastIndexOf(",");
            }
            if (idx!=-1) {
                output = new Long (number.substring(idx+1));
            }
            else {
                output = new Long (number);
            }
        }
        return output.longValue();
    }
*/

    static public int stringToInt(String number) {
        int output = -1;
        if (checkStringNumber(number))
        { Integer dNumber = new Integer(number);
          output = dNumber.intValue();
        }
        return output;
    }

    static public long stringToLong(String number) {
        long output = -1;
        if (checkStringNumber(number))
        { Long dNumber = new Long(number);
          output = dNumber.longValue();
        }
        return output;
    }

	static public int languageStringToInt(String language) {
	  int nLang = 0;
	  if (language.equals("English"))
	  {  nLang = 1; }
	  if (language.equals("Dutch"))
	  {  nLang = 2; }
	  if (language.equals("German"))
	  {  nLang = 3; }
	  if (language.equals("French"))
	  {  nLang = 4; }
	  if (language.equals("Italian"))
	  {  nLang = 5; }
	  if (language.equals("Spanish"))
	  {  nLang = 6; }
	  return nLang;
    }

    static public int languageShortStringToInt(String language) {
      int nLang = 0;
      if (language.equals("en"))
      {  nLang = 1; }
      if (language.equals("nl"))
      {  nLang = 2; }
      if (language.equals("de"))
      {  nLang = 3; }
      if (language.equals("fr"))
      {  nLang = 4; }
      if (language.equals("it"))
      {  nLang = 5; }
      if (language.equals("es"))
      {  nLang = 6; }
      if (language.equals("jp"))
      {  nLang = 7; }
      return nLang;
    }

    static public String languageIntToString(int lang) {
	  String strLang = "";
	  if (lang == 1)
	  {  strLang = "English"; }
	  if (lang == 2)
	  {  strLang = "Dutch"; }
	  if (lang == 3)
	  {  strLang = "German"; }
	  if (lang == 4)
	  {  strLang = "French"; }
	  if (lang == 5)
	  {  strLang = "Italian"; }
	  if (lang == 6)
	  {  strLang = "Spanish"; }
	  return strLang;
    }

    static public String languageIntToShortString(int lang) {
	  String strLang = "";
	  if (lang == 1)
	  {  strLang = "en"; }
	  if (lang == 2)
	  {  strLang = "nl"; }
	  if (lang == 3)
	  {  strLang = "de"; }
	  if (lang == 4)
	  {  strLang = "fr"; }
	  if (lang == 5)
	  {  strLang = "ot"; }
	  if (lang == 6)
	  {  strLang = "es"; }
      if (lang == 7)
      {  strLang = "jp"; }
	  return strLang;
    }

    static public String languageVerboseToFullString(String lang) {
	  String strLang = "";
	  if (lang.equalsIgnoreCase("en"))
	  {  strLang = "English"; }
      if (lang.equalsIgnoreCase("nl"))
	  {  strLang = "Dutch"; }
      if (lang.equalsIgnoreCase("de"))
	  {  strLang = "German"; }
      if (lang.equalsIgnoreCase("fr"))
	  {  strLang = "French"; }
      if (lang.equalsIgnoreCase("it"))
	  {  strLang = "Italian"; }
      if (lang.equalsIgnoreCase("es"))
	  {  strLang = "Spanish"; }
      if (lang.equalsIgnoreCase("jp"))
      {  strLang = "Japanese"; }
	  return strLang;
    }

    static public String languageStringVerboseElementsToFullElements (String langString) {
        String fullString = "";
        DefaultListModel lgs = stringToList(langString);
        Enumeration el = lgs.elements();
        while (el.hasMoreElements()) {
            String elString = (String) el.nextElement();
            fullString += languageVerboseToFullString(elString)+" ";
        }
        return fullString.trim();
    }


    /// Was only merging duplicates........??????
    /// Negated the contains....
    static public DefaultListModel mergeofStringLists (DefaultListModel list_1, DefaultListModel list_2) {
        DefaultListModel list_3 = list_1;
        Enumeration el_2 = list_2.elements();
        while (el_2.hasMoreElements()) {
            String el2String = (String) el_2.nextElement();
            if (!list_3.contains(el2String)) {
                list_3.addElement(el2String);
            }
        }
        return list_3;
    }

    static public ArrayList mergeofStringArrayLists (ArrayList list_1, ArrayList list_2) {
        ArrayList list_3 = list_1;
        for (int i=0; i<list_2.size();i++) {
            String el2String = (String) list_2.get(i);
            if (!list_3.contains(el2String)) {
                list_3.add(el2String);
            }
        }
        return list_3;
    }

    static public DefaultListModel intersectionOfStringLists (DefaultListModel list_1, DefaultListModel list_2) {
        DefaultListModel intersection = new DefaultListModel();
        Enumeration el = list_1.elements();
        while (el.hasMoreElements()) {
            String elString = (String) el.nextElement();
//            System.out.println("elStringt:"+elString);
            Enumeration el2 = list_2.elements();
            while (el2.hasMoreElements()) {
                String el2String = (String) el2.nextElement();
//                System.out.println("el2String:"+el2String);
                if (elString.equals(el2String)) {
//                    System.out.println("Intersecting element:"+elString);
                    intersection.addElement(elString);
                    list_2.removeElement(el2);
                    break;
                }
            }

        }
        return intersection;
    }

    static public String intersectionOfStringElements (String s1, String s2) {
        String s3 = "";
        DefaultListModel list1 = stringToList(s1);
        DefaultListModel list2 = stringToList(s2);
        s3 = listToString(intersectionOfStringLists(list1, list2));
        return s3.trim();
    }

    static public String listToString (DefaultListModel list) {
        String theString = "";
        Enumeration el = list.elements();
        while (el.hasMoreElements()) {
            String nextString = (String) el.nextElement();
            theString += nextString+ " ";
        }
        return theString.trim();
    }

    static public String ArrayListToString (ArrayList list) {
        String theString = "";
        for (int i=0;i<list.size();i++){
            String nextString = (String) list.get(i);
            theString += nextString+ " ";
        }
        return theString.trim();
    }

    static public String ArrayListToSemiColonString (ArrayList list) {
        String theString = "";
        for (int i=0;i<list.size();i++){
            String nextString = (String) list.get(i);
            theString += nextString+ " ";
        }
        return theString.trim();
    }

    static public String listToSemiColonString (DefaultListModel list) {
        String theString = "";
        Enumeration el = list.elements();
        while (el.hasMoreElements()) {
            String nextString = (String) el.nextElement();
            theString += nextString+ ";";
        }
        return theString.trim();
    }

    static public DefaultListModel stringToList (String theString) {
        DefaultListModel theList = new DefaultListModel();
        String nextElement = "";
        int idx_s = 0;
        int idx_e = theString.indexOf(" ");
        while (idx_e>-1) {
            nextElement = theString.substring(idx_s, idx_e).trim();
            if (nextElement.length()>0) {
                theList.addElement(nextElement);
            }
            idx_s = idx_e+1;
            idx_e = theString.indexOf(" ", idx_s);
        }
        if (idx_s<theString.length()) {
            nextElement = theString.substring(idx_s).trim();
            if (nextElement.length()>0) {
                theList.addElement(nextElement);
            }
        }
        return theList;
    }

    static public ArrayList stringToArrayList (String theString) {
        ArrayList theList = new ArrayList();
        String nextElement = "";
        int idx_s = 0;
        int idx_e = theString.indexOf(" ");
        while (idx_e>-1) {
            nextElement = theString.substring(idx_s, idx_e).trim();
            if (nextElement.length()>0) {
                theList.add(nextElement);
            }
            idx_s = idx_e+1;
            idx_e = theString.indexOf(" ", idx_s);
        }
        if (idx_s<theString.length()) {
            nextElement = theString.substring(idx_s).trim();
            if (nextElement.length()>0) {
                //System.out.println("last nextElement = " + nextElement);
                theList.add(nextElement);
            }
        }
        return theList;
    }

    static public ArrayList addStringToArrayList (String theString, ArrayList oldList) {
        ArrayList theList = oldList;
        String nextElement = "";
        int idx_s = 0;
        int idx_e = theString.indexOf(" ");
        while (idx_e>-1) {
            nextElement = theString.substring(idx_s, idx_e).trim();
            if (nextElement.length()>0) {
                if (!theList.contains(nextElement)) {
                    theList.add(nextElement);
                }
            }
            idx_s = idx_e+1;
            idx_e = theString.indexOf(" ", idx_s);
        }
        if (idx_s<theString.length()) {
            nextElement = theString.substring(idx_s).trim();
            if (nextElement.length()>0) {
                if (!theList.contains(nextElement)) {
                    theList.add(nextElement);
                }
            }
        }
        return theList;
    }

    static public ArrayList addStringToArrayList (String theString, ArrayList oldList, int size) {
        ArrayList theList = oldList;
        String nextElement = "";
        int idx_s = 0;
        int idx_e = theString.indexOf(" ");
        while (idx_e>-1) {
            nextElement = theString.substring(idx_s, idx_e).trim();
            if (nextElement.length()>size) {
                if (!theList.contains(nextElement)) {
                    theList.add(nextElement);
                }
            }
            idx_s = idx_e+1;
            idx_e = theString.indexOf(" ", idx_s);
        }
        if (idx_s<theString.length()) {
            nextElement = theString.substring(idx_s).trim();
            if (nextElement.length()>size) {
                if (!theList.contains(nextElement)) {
                    theList.add(nextElement);
                }
            }
        }
        return theList;
    }

    static public ArrayList addStringToArrayList (String theString, ArrayList oldList, ArrayList stopwords, int size) {
        ArrayList theList = oldList;
        String nextElement = "";
        int idx_s = 0;
        int idx_e = theString.indexOf(" ");
        while (idx_e>-1) {
            nextElement = theString.substring(idx_s, idx_e).trim();
            if (nextElement.length()>size) {
                if ((!theList.contains(nextElement)) &&
                    (!stopwords.contains(nextElement.toLowerCase())))    {
                    theList.add(nextElement);
                }
            }
            idx_s = idx_e+1;
            idx_e = theString.indexOf(" ", idx_s);
        }
        if (idx_s<theString.length()) {
            nextElement = theString.substring(idx_s).trim();
            if (nextElement.length()>size) {
                if ((!theList.contains(nextElement)) &&
                    (!stopwords.contains(nextElement.toLowerCase())))  {
                    theList.add(nextElement);
                }
            }
        }
        return theList;
    }

    static public ArrayList defaultListToArrayList (DefaultListModel someList) {
        ArrayList theList = new ArrayList();
        Enumeration el = someList.elements();
        while (el.hasMoreElements()) {
            String value = (String) el.nextElement();
            theList.add(value);
        }
        return theList;
    }

    static public int getNumberOfWords (String phrase) {
        String subPhrase = phrase.replaceAll("  ", " ");
        if (phrase.length()==0) {
            return 0;
        }
        int nWords = 1;
        int idx_space = subPhrase.indexOf(" ");
        while (idx_space>-1) {
            nWords ++;
            idx_space = subPhrase.indexOf(" ", idx_space+1);
        }
        return nWords;
    }

    static public String[] stringToArray (String theString) {
        DefaultListModel theList = new DefaultListModel();
        String nextElement = "";
        int idx_s = 0;
        int idx_e = theString.indexOf(" ");
        while (idx_e>-1) {
            nextElement = theString.substring(idx_s, idx_e).trim();
            if (nextElement.length()>0) {
                theList.addElement(nextElement);
            }
            idx_s = idx_e+1;
            idx_e = theString.indexOf(" ", idx_s);
        }
        if (idx_s<theString.length()) {
            nextElement = theString.substring(idx_s).trim();
            if (nextElement.length()>0) {
                theList.addElement(nextElement);
            }
        }
        String [] theArray = stringListToArray(theList);
        return theArray;
    }

    static public String[] stringNlToArray (String theString) {
        DefaultListModel theList = new DefaultListModel();
        String nextElement = "";
        int idx_s = 0;
        int idx_e = theString.indexOf("\n");
        while (idx_e>-1) {
            nextElement = theString.substring(idx_s, idx_e).trim();
            if (nextElement.length()>0) {
                theList.addElement(nextElement);
            }
            idx_s = idx_e+1;
            idx_e = theString.indexOf("\n", idx_s);
        }
        if (idx_s<theString.length()) {
            nextElement = theString.substring(idx_s).trim();
            if (nextElement.length()>0) {
                theList.addElement(nextElement);
            }
        }
        String [] theArray = stringListToArray(theList);
        return theArray;
    }

    static public DefaultListModel stringSemiColonToList (String theString) {
        DefaultListModel theList = new DefaultListModel();
        String nextElement = "";
        int idx_s = 0;
        int idx_e = theString.indexOf(";");
        while (idx_e>-1) {
            nextElement = theString.substring(idx_s, idx_e).trim();
            if (nextElement.length()>0) {
                theList.addElement(nextElement);
            }
            idx_s = idx_e+1;
            idx_e = theString.indexOf(";", idx_s);
        }
        if (idx_s<theString.length()) {
            nextElement = theString.substring(idx_s).trim();
            if (nextElement.length()>0) {
                theList.addElement(nextElement);
            }
        }
        return theList;
    }

    static public String [] stringListToArray (DefaultListModel aList) {
            String [] stringArray = new String[aList.getSize()];
            int i = 0;
            Enumeration el = aList.elements();
            while (el.hasMoreElements()) {
                String element = (String) el.nextElement();
                stringArray[i]= element;
                i++;
            }
            return stringArray;
    }

    static public DefaultListModel sortStringListAlphabetic (DefaultListModel aList) {
        DefaultListModel sortedList = new DefaultListModel();
        TreeSet sorter = new TreeSet();
        Enumeration e = aList.elements();
        while (e.hasMoreElements()) {
            String eString = (String) e.nextElement();
            sorter.add(eString);
        }
        Iterator iter = sorter.iterator();
        while (iter.hasNext()) { //  gtreeNode childNode = findFirstNodeByName(keys[i]);
            String sString = (String) iter.next();
            if (sString.length()>0)  {
                sortedList.addElement(sString);
            }
        }
        return sortedList;
    }

    static public String [] sortStringArrayAlphabetic (String [] aList) {
        String [] sortedList = new String [aList.length];
        TreeSet sorter = new TreeSet();
        for (int i=0;i<aList.length;i++){
            String eString = aList[i];
            sorter.add(eString);
        }
        int j = 0;
        Iterator iter = sorter.iterator();
        while (iter.hasNext()) {
            String sString = (String) iter.next();
            if (sString.length()>0)  {
                //System.out.println("sorted File:"+sString);
                sortedList[j] = sString;
                j++;
            }
        }
        return sortedList;
    }

    static public DefaultListModel sortStringListNumeric (DefaultListModel aList) {
        DefaultListModel sortedList = new DefaultListModel();
        TreeSet sorter = new TreeSet(
                new Comparator() {
                    public int compare(Object a, Object b) {
                        Integer nA = new Integer((String) a);
                        Integer nB = new Integer((String) b);
                        if (nA.equals(nB)) // We force equal frequencies to be inserted
                        {
                            return -1;
                        } else {
                            return (nA.compareTo(nB));
                        }
                    }
                }
        );
        Enumeration e = aList.elements();
        while (e.hasMoreElements()) {
            String eString = (String) e.nextElement();
            sorter.add(eString);
        }
        Iterator iter = sorter.iterator();
        while (iter.hasNext()) { //  gtreeNode childNode = findFirstNodeByName(keys[i]);
            String sString = (String) iter.next();
            if (sString.length()>0)  {
                sortedList.addElement(sString);
            }
        }
        return sortedList;
    }

    static public String [] sortDocFolderListToBtreeArray (String [] docFolders) {
            String [] stringArray = new String[docFolders.length];
            TreeSet sorter = new TreeSet(
                    new Comparator() {
                        public int compare(Object a, Object b) {
                            String itemA = (String) a;
                            String itemB = (String) b;
                            itemA = itemA.substring(itemA.length()-1);
                            itemB = itemB.substring(itemB.length()-1);
                            Integer nA = new Integer(itemA);
                            Integer nB = new Integer(itemB);
                            if (nA.equals(nB)) // We force equal frequencies to be inserted
                            {
                                return -1;
                            } else {
                                return (nA.compareTo(nB));
                            }
                        }
                    }
            );
            for (int i=0;i<docFolders.length;i++) {
                String element = docFolders[i];
                //System.out.println(element);
                sorter.add(element);
            }
            int i = 0;
         //   System.out.println("Sorted order:");
            Iterator iter = sorter.iterator();
            while (iter.hasNext()) {
                String element = (String) iter.next();
//                System.out.println(element);
                stringArray[i]= element;
                i++;
            }
            return stringArray;
    }

    static public String [] sortFileIdListToSortedArray (DefaultListModel aList) {
            String [] stringArray = new String[aList.getSize()];
            TreeSet sorter = new TreeSet(
                    new Comparator() {
                        public int compare(Object a, Object b) {
                            String itemA = (String) a;
                            String itemB = (String) b;
                            Integer nA = new Integer(new File(itemA).getName());
                            Integer nB = new Integer(new File (itemB).getName());
                            if (nA.equals(nB)) // We force equal frequencies to be inserted
                            {
                                return -1;
                            } else {
                                return (nA.compareTo(nB));
                            }
                        }
                    }
            );
          //  System.out.println("List order:");
            Enumeration el = aList.elements();
            while (el.hasMoreElements()) {
                String element = (String) el.nextElement();
//                System.out.println(element);
                sorter.add(element);
            }
            aList = null;
            int i = 0;
         //   System.out.println("Sorted order:");
            Iterator iter = sorter.iterator();
            while (iter.hasNext()) {
                String element = (String) iter.next();
//                System.out.println(element);
                stringArray[i]= element;
                i++;
            }
            return stringArray;
    }

    static public String [] sortFileIdListToSortedArray (ArrayList aList) {
            String [] stringArray = new String[aList.size()];
            TreeSet sorter = new TreeSet(
                    new Comparator() {
                        public int compare(Object a, Object b) {
                            String itemA = (String) a;
                            String itemB = (String) b;
                            Integer nA = new Integer(new File (itemA).getName());
                            Integer nB = new Integer(new File (itemB).getName());
                            if (nA.equals(nB)) // We force equal frequencies to be inserted
                            {
                                return -1;
                            } else {
                                return (nA.compareTo(nB));
                            }
                        }
                    }
            );
          //  System.out.println("List order:");
            for (int i=0; i<aList.size();i++) {
                String element = (String) aList.get(i);
//                System.out.println(element);
                sorter.add(element);
            }
            aList = null;
            int i = 0;
         //   System.out.println("Sorted order:");
            Iterator iter = sorter.iterator();
            while (iter.hasNext()) {
                String element = (String) iter.next();
//                System.out.println(element);
                stringArray[i]= element;
                i++;
            }
            return stringArray;
    }

    static public DefaultListModel stringArrayToList (String [] stringArray) {
            DefaultListModel aList = new DefaultListModel();
            if (stringArray!=null) {
                for (int i = 0; i<stringArray.length;i++) {
                    String element = stringArray[i];
                    aList.addElement(element);
                }
            }
            return aList;
    }

    static public String stringArrayToString (String [] stringArray) {
            String values = "";
            if (stringArray!=null) {
                for (int i = 0; i<stringArray.length;i++) {
                    String element = stringArray[i];
                    values += element + " ";
                }
            }
            return values.trim();
    }

    static public String[] mergeStringArrays (String [] array1, String [] array2) {
        String [] mergedArray = new String [array1.length+array2.length];
        int j = 0;
//        System.out.println(array1.length+":"+array2.length);
        for (int i = 0; i<array1.length;i++) {
            mergedArray[j] = array1[i];
            j++;
        }
        for (int i = 0; i<array2.length;i++) {
            mergedArray[j] = array2[i];
            j++;
        }
        return mergedArray;
    }

    static public int[] MakeRandomSet(int nFiles) {
          int nTestSet = nFiles/10;
          Random generator = new Random(System.currentTimeMillis());
          int [] testSet = new int[nTestSet];
          for (int i = 0; i<nTestSet; i++) {
              testSet [i] = generator.nextInt(nFiles);
          }
          return testSet;
    }


     static public boolean isInIntArray(int nFile, int [] intArray) {
          for (int i = 0; i<intArray.length; i++) {
              if (intArray[i]==nFile) {
                  return true;
              }
          }
          return false;
      }


    static public int deleteAllSubDirs(String dir) {
      if (!dir.endsWith(SEP)) {
          String[] DirList = null;             // Array with files to be processed
          File lF = new File(dir);
          if (!lF.exists())
          {  return 1;
          }
          if (lF.canRead())
          {  DirList = lF.list();
          }
          if (DirList!=null)
          {	  for (int i=0; i< DirList.length; i++)
                { String name = DirList[i];
//    	          System.out.println("Now trying to delete: "+ dir+"\\"+name);
                deleteAllSubDirs(dir+SEP+name);
                }
          }
//    	  System.out.println("Now going to delete: "+ dir);
          lF.delete();
          if (lF.exists())
          {  return -1;}
          else
          { return 0;}
      }
      else {
          return -1;
      }
    }
 //   C:\Data\TwentyOnePublish\Irion\test\crawl_log\wwwgloba.log
    // C:\Data\TwentyOnePublish\Irion\test\crawl_log\wwwgloba.log

    static public void deleteAllFilesFromDir(String dir) {
        String [] DirList = FileProcessor.makeFlatFileList(dir, "log");
        if (DirList.length>0) {
            for (int i=0; i< DirList.length; i++) {
                  String path = DirList[i];
                  File aFile = new File (path);
 //                 System.out.println("Now trying to delete: "+path);
                    if (!aFile.delete()) {
                        System.out.println("Cannot delete file....");
                    }
            }
        }
        else {
            System.out.println("Nothing to delete from: "+dir);
        }
    }

    static public boolean isInList(DefaultListModel aList, String aValue) {
        Enumeration e = aList.elements();
        while (e.hasMoreElements()) {
            String listValue = (String) e.nextElement();
     //       System.out.println(listValue+":"+aValue);
            if (listValue.equals(aValue)) {
     //           System.out.println("Equal");
                return true;
            }
        }
        return false;
    }

    static public int rankInList(DefaultListModel aList, String aValue) {
        int rank = 1;
        Enumeration e = aList.elements();
        while (e.hasMoreElements()) {
            String listValue = (String) e.nextElement();
     //       System.out.println(listValue+":"+aValue);
            if (listValue.equals(aValue)) {
     //           System.out.println("Equal");
                return rank;
            }
            rank++;
        }
        return -1;
    }

	static public String replace_EOLs_by_Space(String aString) {
	  String modifiedString = "";
      if (aString.length()>0) {
          for (int i= 0; i < aString.length(); i++)
          {   if ((aString.charAt(i)=='\n') ||
                  (aString.charAt(i)=='\r'))
            {  modifiedString = modifiedString.trim() + " "; }
            else
            {  modifiedString = modifiedString + aString.charAt(i); }
          }
      }
	  return modifiedString;
	}

    static public String replace_EOLs_by_Space2(String aString) {
      String modifiedString = "";
      int idx = aString.indexOf('\n');
      int idx_s = 0;
      while (idx!=-1) {
          modifiedString += aString.substring(idx_s, idx) + " ";
          idx_s = idx+1;
          idx = aString.indexOf('\n', idx_s);
      }
      if (idx_s < aString.length()) {
          modifiedString += aString.substring(idx_s);
      }
      return modifiedString;
    }


	static public String replace_TRIPLE_EOLs_by_DOUBLE_EOLS(String aString) {
	  String modifiedString = "";
      if (aString.length()>0) {
          for (int i= 0; i < aString.length()-2; i++)
          {   if (!((aString.charAt(i)=='\n') &&
                    (aString.charAt(i+1)=='\n') &&
                  (aString.charAt(i+2)=='\n')))
            {  modifiedString += aString.charAt(i); }
          }
          modifiedString += aString.substring(aString.length()-2);
      }
	  return modifiedString;
	}

	static public String replace_TABs_by_Space(String aString) {
	  String modifiedString = "";
      if (aString.length()>0) {
          for (int i= 0; i < aString.length(); i++)
          {   if (aString.charAt(i)=='\t')
            {  modifiedString = modifiedString + " "; }
            else
            {  modifiedString = modifiedString + aString.charAt(i); }
          }
      }
	  return modifiedString;
	}

	static public String replace_FSLASH_by_UNDERSCORE(String aString) {
	  String modifiedString = "";
      if (aString.length()>0) {
          for (int i= 0; i < aString.length(); i++)
          {   if (aString.charAt(i)=='/')
            {  modifiedString = modifiedString + "_"; }
            else
            {  modifiedString = modifiedString + aString.charAt(i); }
          }
      }
	  return modifiedString;
	}

    static public String replace_QUOTES_by_UNDERSCORES(String aString) {
      String modifiedString = "";
      if (aString.length()>0) {
          for (int i= 0; i < aString.length(); i++)
          {   if (aString.charAt(i)=='\"')
              {  modifiedString = modifiedString + "_"; }
              else if (aString.charAt(i)=='\'')
              {  modifiedString = modifiedString + "_"; }
              else
              {  modifiedString = modifiedString + aString.charAt(i); }
          }
      }
      return modifiedString;
    }

    static public String replace_PIPE_by_UNDERSCORE(String aString) {
      String modifiedString = "";
      if (aString.length()>0) {
          for (int i= 0; i < aString.length(); i++)
          {   if (aString.charAt(i)=='|')
            {  modifiedString = modifiedString + "_"; }
            else
            {  modifiedString = modifiedString + aString.charAt(i); }
          }
      }
      return modifiedString;
    }

	static public String replace_BSLASH_by_UNDERSCORE(String aString) {
	  String modifiedString = "";
      if (aString.length()>0) {
          for (int i= 0; i < aString.length(); i++)
          {   if (aString.charAt(i)=='\\')
            {  modifiedString = modifiedString + "_"; }
            else
            {  modifiedString = modifiedString + aString.charAt(i); }
          }
      }
	  return modifiedString;
	}

    static public String replace_QMARK_by_UNDERSCORE(String aString) {
      String modifiedString = "";
      if (aString.length()>0) {
          for (int i= 0; i < aString.length(); i++)
          {   if (aString.charAt(i)=='?')
            {  modifiedString = modifiedString + "_"; }
            else
            {  modifiedString = modifiedString + aString.charAt(i); }
          }
      }
      return modifiedString;
    }

    static public String replace_SEMICOLON_by_UNDERSCORE(String aString) {
      String modifiedString = "";
      if (aString.length()>0) {
          for (int i= 0; i < aString.length(); i++)
          {   if (aString.charAt(i)==';')
            {  modifiedString = modifiedString + "_"; }
            else
            {  modifiedString = modifiedString + aString.charAt(i); }
          }
      }
      return modifiedString;
    }

    static public String replace_COLON_by_UNDERSCORE(String aString) {
      String modifiedString = "";
      if (aString.length()>0) {
          for (int i= 0; i < aString.length(); i++)
          {   if (aString.charAt(i)==':')
            {  modifiedString = modifiedString + "_"; }
            else
            {  modifiedString = modifiedString + aString.charAt(i); }
          }
      }
      return modifiedString;
    }

	static public String replace_SPACES_by_UNDERSCORE(String aString) {
	  String modifiedString = "";
      if (aString.length()>0) {
          for (int i= 0; i < aString.length(); i++)
          {   if (aString.charAt(i)==' ')
            {  modifiedString = modifiedString + "_"; }
            else
            {  modifiedString = modifiedString + aString.charAt(i); }
          }
      }
	  return modifiedString;
	}

	static public String replace_DOUBLE_SPACEs_by_Space(String aString) {
	  String modifiedString = aString.replaceAll("  ", " ");
	  return modifiedString;
	}

    static public String replace_DOUBLE_SPACEs_by_Space_org(String aString) {
      String modifiedString = "";
      if (aString.length()>0) {
          for (int i= 0; i < aString.length(); i++) {
              if ((aString.charAt(i)==' ')
               && (i < (aString.length()-1))
               && (aString.charAt(i+1)==' '))
                {
                i++;
              }
              else {
                modifiedString = modifiedString + aString.charAt(i);
              }
          }
      }
      return modifiedString;
    }

    static public String remove_HTML_rubbish(String aString) {
        ////&nbsp;
      String modifiedString = "";
      if (aString.length()>0) {
          int idx_cur = 0;
          int idx = aString.indexOf("&nbsp");
          while (idx>-1) {
              modifiedString += aString.substring(idx_cur, idx);
              idx_cur = idx+6;
              idx = aString.indexOf("&nbsp", idx_cur);
          }
          modifiedString += aString.substring(idx_cur);
      }
      return modifiedString;
    }

    static public String changeInitialCase(String text) {
		 String initial = text;
		 if (initial.length()>0) {
		   initial = text.substring(0,1).toUpperCase();
		   initial += text.substring(1);
		 }
		 return initial.trim();
	}


    static boolean hasIniCase(String word) {
        if ((word.startsWith("A")) ||
                (word.startsWith("B")) ||
                (word.startsWith("C")) ||
                (word.startsWith("D")) ||
                (word.startsWith("E")) ||
                (word.startsWith("F")) ||
                (word.startsWith("G")) ||
                (word.startsWith("H")) ||
                (word.startsWith("I")) ||
                (word.startsWith("J")) ||
                (word.startsWith("K")) ||
                (word.startsWith("L")) ||
                (word.startsWith("M")) ||
                (word.startsWith("N")) ||
                (word.startsWith("O")) ||
                (word.startsWith("P")) ||
                (word.startsWith("Q")) ||
                (word.startsWith("R")) ||
                (word.startsWith("S")) ||
                (word.startsWith("T")) ||
                (word.startsWith("U")) ||
                (word.startsWith("V")) ||
                (word.startsWith("W")) ||
                (word.startsWith("X")) ||
                (word.startsWith("Y")) ||
                (word.startsWith("Z"))) {
            return true;
        }
        else {
            return false;
        }
    }

    static public int idxStringArray (String value, String [] array) {
        for (int i = 0; i < array.length; i++) {
            String s = array[i];
            if (s.equals(value)) {
                return i;
            }
        }
        return -1;
    }
}
