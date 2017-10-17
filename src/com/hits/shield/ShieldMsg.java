package com.hits.shield;


import java.util.List;


public class ShieldMsg {
	/*
	 * 过滤屏蔽字
	 */
	public static String replaceStr(String note){
		List<CorChar> filterCharList=readPropertiesUtil.readPropertiesList2("filterchar.properties");
		try{
			for (int i=0;i<filterCharList.size();i++) {
				CorChar corc=filterCharList.get(i);
//				System.out.println(note.contains(corc.getYchar()));
				if(note.contains(corc.getYchar())){
					note=note.replace(corc.getYchar(), corc.getRepchar());
				}
			}
		} catch (Exception e) {
			// TODO: handle exceptionsyso
			e.printStackTrace();
		}
		return note;
	}
	public static void main(String[] args) {
		String note = "";
//		System.out.println(note.replace("抵押", "抵 押"));
//		System.out.println(note.replaceAll("抵押", "抵 押"));
		System.out.println(replaceStr(note));
	}
}
