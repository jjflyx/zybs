package com.hits.common.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.text.ParsePosition;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.*;

/**
 * @author Wizzer.cn
 * @time   2012-9-13 ����10:54:04
 *
 */
public class DateUtil {

    public static String oraDateFormat = "TO_DATE(?, 'yyyy-MM-dd')";
    public static String oraTimeFormat = "TO_DATE(?, 'yyyy-MM-dd HH24:mi:ss')";
    static public SimpleDateFormat MMddYYYY_HHmmss = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    static public SimpleDateFormat HHmmss = new SimpleDateFormat("HH:mm:ss");
    static public SimpleDateFormat yyyy = new SimpleDateFormat("yyyy");
    static public SimpleDateFormat yyyyMMdd = new SimpleDateFormat("yyyy-MM-dd");
    static public SimpleDateFormat yyyyMMdd8 = new SimpleDateFormat("yyyyMMdd");
    static public SimpleDateFormat yyyyMMddE = new SimpleDateFormat("yyyy��MM��dd��E");
    static public SimpleDateFormat yyyyMMddStr = new SimpleDateFormat("yyyy��MM��dd��");
    static public SimpleDateFormat yyyyMMddEStr = new SimpleDateFormat("yyyy-MM-dd E");


    /**
     * �õ���ǰʱ�������
     *
     * @param day
     */
    public static String getYearMonth(String day) {
        if (day == null) return "";
        if (day.length() < 8) return "";
        int n = day.lastIndexOf("-");
        return day.substring(0, n);
    }

    /**
     * �õ���ǰʱ���yyyy�� mm��
     *
     * @param day
     */
    public static String getYearMonthStr(String day) {
        if (day == null) return "";
        if (day.length() < 8) return "";
        return day.substring(0, 4) + "�ꡡ" + day.substring(5, 7) + "��";
    }

    /**
     * �õ���ǰʱ���yyyy�� mm��dd��
     *
     * @param day
     */
    public static String getYearMonthDayStr(String day) {
        if (day == null) return "";
        if (day.length() < 8) return "";
        return day.substring(0, 4) + "�ꡡ" + day.substring(5, 7) + "��" + day.substring(8) + "��";
    }

    /**
     * ����ָ��ʱ�����
     *
     * @param day
     */
    public static int getYear(String day) {
        if (day == null) return 0;
        if (day.length() < 8) return 0;
        return Integer.parseInt(day.substring(0, 4));
    }

    /**
     * ����ָ��ʱ�����
     *
     * @param day
     */
    public static String getMonth(String day) {
        if (day == null) return "0";
        if (day.length() < 8) return "0";
        int m = day.indexOf("-", 0);
        int n = day.lastIndexOf("-");
        String temp = day.substring(m + 1, n);
        if (temp.length() == 1) temp = "0" + temp;
        return temp;
    }

    /**
     * ����ָ��ʱ�����
     *
     * @param day
     */
    public static String getDate(String day) {
        if (day == null) return "0";
        if (day.length() < 8) return "0";
        int n = day.lastIndexOf("-");
        String temp = day.substring(n + 1);
        if (temp.length() == 1) temp = "0" + temp;
        return temp;
    }

    /**
     * ��ʱ��ת��Ϊyyyy-MM-dd HH:mm:ss���ַ�ʱ��
     *
     * @param date
     */
    public static String date2str(java.util.Date date) {
        if (date == null) return "";
        try {
            return MMddYYYY_HHmmss.format(date);
        }
        catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }


    /**
     * ��ʱ��ת��ΪHH:mm:ss���ַ�ʱ��
     *
     */
    public static String getcurHMS() {
        try {
            return HHmmss.format(new java.util.Date());
        }
        catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
   
    /**
     * ���ص�ǰʱ��
     */
    public static String getCurDateTime() {
        try {
            return MMddYYYY_HHmmss.format(new java.util.Date());
        }
        catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * ��ʱ��ת��Ϊyyyy��MM��dd��E���ַ�ʱ��
     *
     * @param date
     */
    public static String date2YMDE(java.util.Date date) {
        if (date == null) return "";
        try {
            return yyyyMMddE.format(date);
        }
        catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * ��ʱ��ת��Ϊyyyy-MM-dd E���ַ�ʱ��
     *
     * @param date
     */
    public static String date2YMDEStr(java.util.Date date) {
        if (date == null) return "";
        try {
            return yyyyMMddEStr.format(date);
        }
        catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * ��ʱ��ת��Ϊyyyy-MM-dd���ַ�ʱ��
     *
     * @param date
     */
    public static String getDateStr(java.util.Date date) {
        if (date == null) return "";
        try {
            return yyyyMMdd.format(date);
        }
        catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * ��ʱ��ת��ΪyyyyMMdd���ַ�ʱ��
     *
     * @param date
     */
    public static String getDateStr8(java.util.Date date) {
        if (date == null) return "";
        try {
            return yyyyMMdd8.format(date);
        }
        catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * ��sqlʱ��ת��Ϊyyyy-MM-dd���ַ�ʱ��
     *
     * @param date
     */
    static public String date2str(java.sql.Date date) {
        if (date == null) return "";
        try {
            return yyyyMMdd.format(date);
        }
        catch (Exception e) {
            e.printStackTrace();
            return "";
        }

    }

    /**
     * ��sqlʱ��ת��Ϊyyyy-MM-dd HH:mm:ss���ַ�ʱ��
     *
     * @param date
     */
    static public String dateTime2str(java.sql.Date date) {
        if (date == null) return "";
        try {
            return MMddYYYY_HHmmss.format(date);
        }
        catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * ��sqlʱ��ת��Ϊyyyy-MM-dd HH:mm:ss���ַ�ʱ��
     *
     * @param date
     */
    static public String dateTime2str(java.sql.Timestamp date) {
        if (date == null) return "";
        try {
            return MMddYYYY_HHmmss.format(date);
        }
        catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * ֻ�õ���ʱ�����ݸ�ʽΪYYYY
     *
     * @param date
     */
    static public String getYYYY(java.util.Date date) {
        if (date == null) return "";
        return yyyy.format(date);
    }

    /**
     * ���ַ�ʱ�� YYYY-MM-DD ת��Ϊ java.sql.Date object
     *
     * @param str
     */
    public static Date str2date(String str) {
        java.sql.Date result = null;
        try {
            java.util.Date udate = yyyyMMdd.parse(str);
            result = new Date(udate.getTime());
            return result;
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    /**
     * ���ַ�ʱ�� YYYY-MM-DD ת��Ϊ java.sql.Date object
     *
     * @param str
     */
    public static Date str2datetime(String str) {
        java.sql.Date result = null;
        try {
            java.util.Date udate = MMddYYYY_HHmmss.parse(str);
            result = new Date(udate.getTime());
            return result;
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public static Timestamp str2sjc(String str) {
        Timestamp result = null;
        try {
            java.util.Date udate = MMddYYYY_HHmmss.parse(str);
            result = new java.sql.Timestamp(udate.getTime());
            return result;
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * ͨ��ǰʱ��õ������ڵ����ڣ��������ڼ��õ������ڼ��Ƕ��ٺ�
     *
     * @param curday
     */
    public static String getNexWeekDayByStr(String curday) {
        try {
            java.util.Date date = str2date(curday);
/*            Calendar rightNow = Calendar.getInstance();
            rightNow.setTime(date);
            int week = rightNow.get(Calendar.DAY_OF_WEEK);
            if (week == 7) week = 6;
            if (week == 1) week = 0;*/
            return getDateStr(new java.util.Date(date.getTime() + 7 * 24 * 3600 * 1000));
        }
        catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * ͨ��ǰʱ��õ������ڵ����ڣ��������ڼ��õ������ڼ����ٺ�
     *
     * @param curday
     */
    public static String getPreWeekDayByStr(String curday) {
        try {
            java.util.Date date = str2date(curday);
/*            Calendar rightNow = Calendar.getInstance();
            rightNow.setTime(date);
            int week = rightNow.get(Calendar.DAY_OF_WEEK);
            if (week == 0) week = 1;
            if (week == 7) week = 8;*/
            return getDateStr(new java.util.Date(date.getTime() - 7 * 24 * 3600 * 1000));
        }
        catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * �õ���ǰ�����Ǳ��µڼ���
     *
     * @param curday
     */
    public static int getMonthWeek(String curday) {
        try {
            java.util.Date date = str2date(curday);
            Calendar rightNow = Calendar.getInstance();
            rightNow.setTime(date);
            return rightNow.get(Calendar.WEEK_OF_MONTH);
        }
        catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * �õ���ǰ�����Ǳ���ڼ���
     *
     * @param curday
     */
    public static int getYearWeek(String curday) {
        try {
            java.util.Date date = str2date(curday);
            Calendar rightNow = Calendar.getInstance();
            rightNow.setTime(date);
            return rightNow.get(Calendar.WEEK_OF_YEAR);
        }
        catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * ͨ��ǰ���ڵõ��ϸ��µ�����
     *
     * @param curday df
     */
    public static String getPreMonthDayStr(String curday) {
        int year = getYear(curday);
        String monthStr = getMonth(curday);
        int month = Integer.parseInt(monthStr);
        if (month <= 1) {
            month = 12;
            year = year - 1;
        } else month = month - 1;
        monthStr = String.valueOf(month);
        if (monthStr.length() == 1) monthStr = "0" + monthStr;
        return year + "-" + monthStr + "-01";

    }

    /**
     * ͨ��ǰ���ڵõ��¸��µ�����
     *
     * @param curday
     */
    public static String getNextMonthDayStr(String curday) {
        int year = getYear(curday);
        String monthStr = getMonth(curday);
        int month = Integer.parseInt(monthStr);
        if (month >= 12) {
            month = 1;
            year = year + 1;
        } else month = month + 1;
        monthStr = String.valueOf(month);
        if (monthStr.length() == 1) monthStr = "0" + monthStr;
        return year + "-" + monthStr + "-01";
    }

    /**
     * �õ���ǰ���ڵ���һ�������
     *
     * @param curday
     */
    public static String getPreDayStr(String curday) {
        java.util.Date date = str2date(curday);
        return date2str(new Date(date.getTime() - 24 * 3600 * 1000));

    }

    /**
     * �õ���ǰ���ڵ���һ�������
     *
     * @param curday
     */
    public static String getNextDayStr(String curday) {
        java.util.Date date = str2date(curday);
        return date2str(new Date(date.getTime() + 24 * 3600 * 1000));
    }
    /**
     * �õ���ǰ���ڵ���һ�������
     *
     * @param curday
     */
    public static String getNthDayStr(String curday,int n) {
        java.util.Date date = str2date(curday);
        return date2str(new Date(date.getTime() + n*24 * 3600 * 1000));

    }

    /**
     * ͨ���ַ����ڵõ������������Ϊһ��
     *
     * @param curday
     */
    public static String getCurMonthDayStr(String curday) {
        int year = getYear(curday);
        String monthStr = getMonth(curday);
        int month = Integer.parseInt(monthStr);
        monthStr = String.valueOf(month);
        if (monthStr.length() == 1) monthStr = "0" + monthStr;
        return year + "-" + monthStr + "-01";
    }

    /**
     * ͨ���ַ������ڵõ��������ǵ�ǰ�����е����ڼ�
     *
     * @param curday
     */
    public static int getCurWeekDayByStr(String curday) {
        try {
            java.util.Date date = str2date(curday);
            Calendar rightNow = Calendar.getInstance();
            rightNow.setTime(date);
            return rightNow.get(Calendar.DAY_OF_WEEK);
        }
        catch (Exception e) {
            e.printStackTrace();
            return 1;
        }
    }

    /**
     * ͨ���ַ����ڵõ���ǰ���ڵ���������
     *
     * @param curday
     */
    public static String[] getallweekdate(String curday) {
        String pandy[] = new String[7];
        int day = getCurWeekDayByStr(curday) - 1;
        String firstday = getAllowPreDay(curday, day);
        for (int i = 0; i < 7; i++) {
            pandy[i] = firstday;
            firstday = getNextDayStr(firstday);
        }
        return pandy;
    }

    /**
     * ͨ���ַ����ڵõ���ǰ���ڵ���������
     *
     * @param curday
     */
    public static String[] getallweekdateCn(String curday) {
        String pandy[] = new String[7];
        int day = getCurWeekDayByStr(curday) - 1;
        String firstday = getAllowPreDay(curday, day);
        for (int i = 0; i < 7; i++) {
            firstday = getNextDayStr(firstday);
            pandy[i] = firstday;
        }
        return pandy;
    }

    /**
     * ͨ��ʼ���ںͽ������ڵõ���֮����������ڵĵ�һ������һ�������
     *
     * @param startdate
     * @param enddate
     */
    public static String[][] getweekfl(String startdate, String enddate) {
        java.util.Date date1 = str2date(startdate);
        java.util.Date date2 = str2date(enddate);
        long weekcount = (date2.getTime() - date1.getTime()) / (24 * 3600 * 1000 * 7) + 1;
        int weekc = (int) weekcount;
        if (getCurWeekDayByStr(startdate) > getCurWeekDayByStr(enddate)) {
            weekc++;
        }
        String pandy[][] = new String[weekc][2];
        String tempdate = startdate;
        for (int i = 0; i < weekc; i++) {
            pandy[i][0] = getallweekdate(tempdate)[0];
            pandy[i][1] = getallweekdate(tempdate)[6];
            tempdate = getNextDayStr(pandy[i][1]);
        }
        return pandy;
    }

    /**
     * ͨ��ʼ���ںͽ������ڵõ�֮�������µĵ�һ������һ�������
     *
     * @param startdate
     * @param enddate
     */
    public static Vector<String[]> getmonthfl(String startdate, String enddate) {
        Vector<String[]> pandy = new Vector<String[]>();
        String tempdate = getCurMonthDayStr(startdate);
        while (tempdate.compareTo(enddate) <= 0) {
            String pan[] = new String[2];
            pan[0] = tempdate;
            pan[1] = getPreDayStr(getNextMonthDayStr(tempdate));
            pandy.add(pan);
            tempdate = getNextMonthDayStr(tempdate);
        }
        return pandy;
    }

    /**
     * ͨ��ʼ���ںͽ������ڵõ�����֮����������ڲ���,�ֿ�
     *
     * @param startdate
     * @param enddate
     */
    public static String getMulday(String startdate, String enddate) {
        String pandy = "";
        String tempdate = startdate;
        while (tempdate.compareTo(enddate) <= 0) {
            pandy += tempdate + ",";
            tempdate = getNextDayStr(tempdate);
        }
        if (pandy.length() > 0)
            pandy = pandy.substring(0, pandy.length() - 1);
        return pandy;
    }
    /*
    * ÿ��������ǰ�����''
    * zl
    */
    public static String getMuldayStr(String startdate, String enddate) {
        String pandy = "'";
        String tempdate = startdate;
        while (tempdate.compareTo(enddate) <= 0) {
            pandy += tempdate + "','";
            tempdate = getNextDayStr(tempdate);
        }
        if (pandy.length() > 0)
            pandy = pandy.substring(0, pandy.length() - 2);
        return pandy;
    }
    public static String getMulyearStr(String startYear, String endYear) {
        String pandy = "'";
        String tempdate = startYear;
        while (tempdate.compareTo(endYear) <= 0) {
            pandy += tempdate + "','";
            tempdate = getNextDayStr(tempdate);
        }
        if (pandy.length() > 0)
            pandy = pandy.substring(0, pandy.length() - 2);
        return pandy;
    }

    /**
     * ͨ��ʼ���ںͽ������ڵõ�����֮���������ڵĵ�һ������һ�첢��,�ֿ�
     *
     * @param startdate
     * @param enddate
     */
    public static String getMulweekday(String startdate, String enddate) {
        String pandy = "";
        String tempdate[][] = getweekfl(startdate, enddate);
        for (int i = 0; i < tempdate.length; i++) {
            pandy += tempdate[i][0] + "--" + tempdate[i][1] + ",";
        }
        if (pandy.length() > 0)
            pandy = pandy.substring(0, pandy.length() - 1);
        return pandy;
    }

    /**
     * ͨ��ʼ���ںͽ������ڵõ�����֮�������µĵ�һ������һ��������,�ֿ�
     *
     * @param startdate
     * @param enddate
     */
    public static String getMulmonthday(String startdate, String enddate) {
        String pandy = "";
        Vector<String[]> tempdate = getmonthfl(startdate, enddate);
        String temp[] = new String[2];
        for (int i = 0; i < tempdate.size(); i++) {
            temp = tempdate.get(i);
            pandy += temp[0] + "--" + temp[1] + ",";

        }
        if (pandy.length() > 0)
            pandy = pandy.substring(0, pandy.length() - 1);
        return pandy;
    }

    /**
     * ͨ��ʼʱ��õ�count�������
     *
     * @param startdate
     * @param count
     */
    public static String getAllowDay(String startdate, int count) {
        String pandy = startdate;
        for (int i = 0; i < count; i++) {
            pandy = getNextDayStr(pandy);
        }
        return pandy;
    }

    /**
     * ͨ��ʼʱ��õ�countǰ������
     *
     * @param startdate
     * @param count
     */
    public static String getAllowPreDay(String startdate, int count) {
        String pandy = startdate;
        for (int i = 0; i < count; i++) {
            pandy = getPreDayStr(pandy);
        }
        return pandy;
    }

    /**
     * ͨ��ʼʱ��õ�count���ں������
     *
     * @param startdate
     * @param count
     */
    public static String getAllowWeek(String startdate, int count) {
        return getAllowDay(startdate, count * 7);
    }

    /**
     * ͨ��ʼʱ��õ�count����ǰ������
     *
     * @param startdate
     * @param count
     */
    public static String getAllowPreWeek(String startdate, int count) {
        return getAllowPreDay(startdate, count * 7);
    }

    /**
     * ͨ��ʼʱ��õ�count�º������
     *
     * @param startdate
     * @param count
     */
    public static String getAllowMonth(String startdate, int count) {
        String tempdate = getCurMonthDayStr(startdate);
        for (int i = 0; i < count; i++) {
            tempdate = getNextMonthDayStr(tempdate);
        }
        return tempdate;
    }

    /**
     * ͨ��ʼʱ��õ�count��ǰ������
     *
     * @param startdate
     * @param count
     */
    public static String getAllowPreMonth(String startdate, int count) {
        String tempdate = getCurMonthDayStr(startdate);
        for (int i = 0; i < count; i++) {
            tempdate = getPreMonthDayStr(tempdate);
        }
        return tempdate;
    }

    /**
     * �õ�һ�����ж�����
     */
    public static int getMonthDaysCounts(String curday) {
        long daycount = 0;
        try {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(str2date(getCurMonthDayStr(curday)));
            Calendar calendar2 = Calendar.getInstance();
            calendar2.setTime(str2date(getNextMonthDayStr(curday)));
            daycount = (calendar2.getTimeInMillis() - calendar.getTimeInMillis()) / (1000 * 60 * 60 * 24);
            return (int) daycount;
        }
        catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * �õ�һ�������е�����
     */
    public static String[] getMonthDays(String curday) {
        int counts = getMonthDaysCounts(curday);
        String pandy[] = new String[counts];
        String day = getCurMonthDayStr(curday);
        pandy[0] = getCurMonthDayStr(curday);
        for (int i = 1; i < counts; i++) {
            day = getNextDayStr(day);
            pandy[i] = day;
        }
        return pandy;
    }

    /**
     * �õ�һ�����ڸ�ʽΪYYYY��MM��DD�գ�MM��DD��
     *
     * @param start
     * @param end
     */
    public static String getymdmd(String start, String end) {
        String ymd = "";
        StringTokenizer fenxi = new StringTokenizer(start, "-");
        StringTokenizer fenxi2 = new StringTokenizer(end, "-");
        ymd += fenxi.nextToken() + "��  ";
        ymd += fenxi.nextToken() + "�� ";
        ymd += fenxi.nextToken() + "�� - ";
        fenxi2.nextToken();
        ymd += fenxi2.nextToken() + "�� ";
        ymd += fenxi2.nextToken() + "��";
        return ymd;
    }

    /**
     * ͨ��ǰ�£��õ���ǰ������yyyy-mm-01
     */
    public static String getFirstCurdata(String curmongth) {
        String pandy = "";
        String cur = getDateStr(new java.util.Date());
        if (curmongth.length() == 1) curmongth = "0" + curmongth;
        pandy = getYear(cur) + "-" + curmongth + "-01";
        return pandy;
    }

    /**
     * ͨ��ǰ�£��õ���ǰ������yyyy-mm-01
     */
    public static String getLastCurdata(String curmongth) {
        String pandy = "";
        String cur = getDateStr(new java.util.Date());
        if (curmongth.length() == 1) curmongth = "0" + curmongth;
        pandy = getYear(cur) + "-" + curmongth + "-" + getMonthDaysCounts(cur);
        return pandy;
    }

    /**
     * �õ���ǰ���ڵõ���ǰ������yyyy-mm-DD
     */
    public static String getToday() {
        return getDateStr(new java.util.Date());
    }

    /**
     * �жϸ������Ƿ�Ϊ������
     *
     * @param date
     */
    public static boolean isWeekend(String date) {
        if (getCurWeekDayByStr(date) == 1 || getCurWeekDayByStr(date) == 7) {
            return true;
        }
        return false;
    }

    /**
     * �жϸ������Ƿ�Ϊ��һʮһ�١�
     *
     * @param date
     */
    public static boolean is51101(String date) {
        String strYue = date.substring(5, 7);
        String strRi = date.substring(8);
        int yue = Integer.parseInt(strYue);
        int ri = Integer.parseInt(strRi);
        if ((yue == 5 || yue == 10) && ri <= 7) {
            return true;
        }
        return false;
    }

    /**
     * ��ʼ��06--15��Ĵ��ڼ���
     */
    public static Map<String, String> initChunJie() {
        String[] ChunJie = {"2006-01-29", "2007-02-18", "2008-02-07", "2009-01-26", "2010-02-14", "2011-02-03", "2012-01-23",
                "2013-02-10", "2014-01-31", "2015-02-20"};

        Map<String, String> dateMap = new HashMap<String, String>();

        for (int i = 0; i < ChunJie.length; i++) {
            String curDate = ChunJie[i];
            int j = 1;
            while (j <= 7) {
                dateMap.put(curDate, curDate);
                curDate = getNextDayStr(curDate);
                j++;
            }
        }
        return dateMap;
    }

    /**
     * �ж��Ƿ�Ϊ���ڡ�
     *
     * @param date
     */
    public static boolean isChunjie(String date) {
        if (initChunJie().containsKey(date)) {
            return true;
        }
        return false;
    }

    /**
     * ���������num�������պ�����ڡ�
     *
     * @param date
     * @param num
     */
    public static String getEndDay(String date, int num) {
        int i = 1;
        String flagMonth = date.substring(5, 7);
        date = getNextDayStr(date);
        if ((!flagMonth.equals("12")) && (!flagMonth.equals("01")) && (!flagMonth.equals("02")) && (!flagMonth.equals("04")) && (!flagMonth.equals("05")) && (!flagMonth.equals("09")) && (!flagMonth.equals("10")))
        {
            while (i <= num) {

                if (!isWeekend(date)) {

                    i++;
                }
                date = getNextDayStr(date);
            }
        } else
        if ((flagMonth.equals("04")) || flagMonth.equals("05") || (flagMonth.equals("09")) || flagMonth.equals("10")) {

            while (i <= num) {
                if ((!isWeekend(date)) && (!is51101(date))) {

                    i++;
                }
                date = getNextDayStr(date);
            }
        } else if ((flagMonth.equals("12")) || flagMonth.equals("01") || flagMonth.equals("02")) {
            while (i <= num) {

                if ((!isWeekend(date)) && (!isChunjie(date))) {
                    i++;
                }
                date = getNextDayStr(date);
            }
        }
        return getPreDayStr(date);
    }


    public static int getWorkDays(String endDate, String startDate) {
        if (endDate.compareToIgnoreCase(startDate) <= 0) {
            return 0;
        }
        int num = 0;
        startDate=getNextDayStr(startDate);
        while(startDate.compareToIgnoreCase(endDate)<=0)
        {
            if((!isWeekend(startDate))&&(!is51101(startDate))&&(!isChunjie(startDate)))
            {
                num++;
            }
            startDate=getNextDayStr(startDate);
        }
        return num;
    }

    /**
     * ������������֮��������һ���+1
     * zl
     * @param date1
     * @param date2
     * @return
     */
    public static long getDifferDays(String date1,String date2){
        Date tempDate1= str2date(date1);
        Date tempDate2= str2date(date2);
        long differDays =0;
        differDays = tempDate2.getTime()-tempDate1.getTime();
        long todaySeconds=1000*24*60*60;
        return differDays/todaySeconds +1;
    }
    /**
     * ��������֮�����Ϊ0+1
     * zl
     * @param year1
     * @param year2
     * @return
     */
    public static int getDifferYears(int year1,int year2){
        return  year2-year1+1;
    }

    /**
     * ͨ���������Ը��ܷ�������ͬ�������Ը���
     * zl
     */
    @SuppressWarnings("null")
	public static String getPreyearStr(String curDate){
    	curDate=StringUtil.null2String(curDate);
        if(curDate==null && curDate.length()<10){
            return "";
        }
//        Date curDt = DateUtil.str2date(curDate);
        String curyear =curDate.substring(0,4);
        String curMonth = curDate.substring(5,7);
        String curDay = curDate.substring(8,10);
        if(curMonth.equals("2")){
            if(runNian(Integer.parseInt(curyear))){
                curDay = "29";
            }
        }
        return (Integer.parseInt(curyear)-1)+"-"+curMonth+"-"+curDay;

    }

    static boolean runNian(int year)//�ж��Ƿ�Ϊ����ķ��� 
    {
        boolean t=false;
        if(year%4==0)
        {
            if(year%100!=0)
            {
                t=true;
            }
            else if(year%400==0)
            {
                t=true;
            }
        }
        return t;
    }
    /**
     * �Ƚ������ַ�ʱ��Ĵ�С
     * @param t1 ʱ��1
     * @param t2 ʱ��2
     * @param parrten ʱ���ʽ :yyyy-MM-dd
     * @return ����long =0��ȣ�>0 t1>t2��<0 t1<t2
     */
    public static long compareStringTime(String t1,String t2,String parrten)
    {
        SimpleDateFormat formatter = new SimpleDateFormat (parrten);
        ParsePosition pos = new ParsePosition(0);
        ParsePosition pos1 = new ParsePosition(0);
        java.util.Date dt1=formatter.parse(t1,pos);
        java.util.Date dt2=formatter.parse(t2,pos1);
        long l=dt1.getTime()-dt2.getTime();
        return l;
    }
    /**
     * ��õ�ǰʱ��
     * @param parrten �����ʱ���ʽ
     * @return ����ʱ��
     */
    public static String getTime(String parrten)
    {
        String timestr;
        if(parrten==null||parrten.equals(""))
        {
            parrten="yyyyMMddHHmmss";
        }
        java.text.SimpleDateFormat sdf=new SimpleDateFormat(parrten);
        java.util.Date cday=new java.util.Date();
        timestr=sdf.format(cday);
        return timestr;
    }


}
