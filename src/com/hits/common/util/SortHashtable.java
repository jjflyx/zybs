package com.hits.common.util;

import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: Wizzer
 * Date: 2010-2-6
 * Time: 21:01:57
 * To change this template use File | Settings | File Templates.
 */
public class SortHashtable {

    /**
     * �������ƣ�getSortedHashtable
     * ������Hashtable h ���뱻�����ɢ�б�
     * �������������hashtable.entrySet�������򣬲�����
     */
     @SuppressWarnings({ "rawtypes", "unchecked" })
	public static Map.Entry[] getSortedHashtableByKey(Hashtable h) {

       Set set = h.entrySet();

       Map.Entry[] entries = (Map.Entry[]) set.toArray(new Map.Entry[set.size()]);

       Arrays.sort(entries, new Comparator() {
		public int compare(Object arg0, Object arg1) {
           Object key1 = ((Map.Entry) arg0).getKey();
           Object key2 = ((Map.Entry) arg1).getKey();
           return ((Comparable) key1).compareTo(key2);
         }

       });

       return entries;
     }

     /**
     * �������ƣ�getSortedHashtable
     * ������Hashtable h ���뱻�����ɢ�б�
     * �������������hashtable.entrySet�������򣬲�����
     */
     @SuppressWarnings({ "rawtypes", "unchecked" })
     public static Map.Entry[] getSortedHashtableByValue(Hashtable h) {
       Set set = h.entrySet();
       Map.Entry[] entries = (Map.Entry[]) set.toArray(new Map.Entry[set.size()]);

       Arrays.sort(entries, new Comparator() {
		public int compare(Object arg0, Object arg1) {
           int key1 = Integer.parseInt(((Map.Entry) arg0).getValue()
               .toString());
           int key2 = Integer.parseInt(((Map.Entry) arg1).getValue()
               .toString());
           return ((Comparable) key1).compareTo(key2);
         }
       });

       return entries;
     }

     /**
     * @param args
     */
     @SuppressWarnings({ "rawtypes", "unchecked" })
	public static void main(String[] args) {
       Hashtable t = new Hashtable();

       t.put("10-11 02:45������VS���޺մ�����", "������VS���޺մ�����");
       t.put("10-11 02:45�ʸ��³�VSл������", "�ʸ��³�VSл������");
       t.put("10-11 05:00EL�����VS������", "EL�����VS������");
       t.put("10-11 02:00����VS��϶��", "����VS��϶��");

       Map.Entry[] set = getSortedHashtableByKey(t);

       // perportyTable
       for (int i = 0; i < set.length; i++) {

         System.out.println(set[i].getKey().toString());

         System.out.println(set[i].getValue().toString());

       }

     }



}
