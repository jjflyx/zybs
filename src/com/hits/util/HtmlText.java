package com.hits.util;

import java.util.regex.Pattern;

public class HtmlText {
	 public static String Html2Text(String inputString) {    
	             String htmlStr = inputString;    
	             String textStr ="";    
	             java.util.regex.Pattern p_script;    
	             java.util.regex.Matcher m_script;    
	             java.util.regex.Pattern p_style;    
	             java.util.regex.Matcher m_style;    
	             java.util.regex.Pattern p_html;    
	             java.util.regex.Matcher m_html;    
	               
	             java.util.regex.Pattern p_html1;    
	             java.util.regex.Matcher m_html1;    
	            
	            try {    
	            	 String regEx_script = "<[\\s]*?script[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?script[\\s]*?>"; //定义script的正则表达式{或<script[^>]*?>[\\s\\S]*?<\\/script> }    
	            	 String regEx_style = "<[\\s]*?style[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?style[\\s]*?>"; //定义style的正则表达式{或<style[^>]*?>[\\s\\S]*?<\\/style> }    
	                 String regEx_html = "<[^>]+>"; //定义HTML标签的正则表达式    
	                 String regEx_html1 = "<[^>]+";   
	                 String regEx_str = "&nbsp;";   
	                 String regEx_str2 = "&#160;";
	                 p_script = Pattern.compile(regEx_script,Pattern.CASE_INSENSITIVE);    
	                 m_script = p_script.matcher(htmlStr);    
	                 htmlStr = m_script.replaceAll(""); //过滤script标签    
	     
	                 p_style = Pattern.compile(regEx_style,Pattern.CASE_INSENSITIVE);    
	                 m_style = p_style.matcher(htmlStr);    
	                 htmlStr = m_style.replaceAll(""); //过滤style标签    
	               
	                 p_html = Pattern.compile(regEx_html,Pattern.CASE_INSENSITIVE);    
	                 m_html = p_html.matcher(htmlStr);    
	                 htmlStr = m_html.replaceAll(""); //过滤html标签    
	                   
	                 p_html1 = Pattern.compile(regEx_html1,Pattern.CASE_INSENSITIVE);    
	                 m_html1 = p_html1.matcher(htmlStr);    
	                 htmlStr = m_html1.replaceAll(""); //过滤html标签    
	               
	                 p_html1 = Pattern.compile(regEx_str,Pattern.CASE_INSENSITIVE);    
	                 m_html1 = p_html1.matcher(htmlStr);    
	                 htmlStr = m_html1.replaceAll(" "); //过滤&nbsp;字符串    
	                 p_html1 = Pattern.compile(regEx_str2,Pattern.CASE_INSENSITIVE);    
	                 m_html1 = p_html1.matcher(htmlStr);    
	                 htmlStr = m_html1.replaceAll(" "); //过滤&nbsp;字符串    
	                   
	              textStr = htmlStr;    
	              
	          }catch(Exception e) {    
	                   System.err.println("Html2Text: " + e.getMessage());    
	           }    
	           
	         return textStr;//返回文本字符串    
	    }    
	   
	 public static void main(String[] arg){
	  String strtext = "<TABLE style='TABLE-LAYOUT: fixed' cellSpacing=5 cellPadding=0 width=750 " +
	  		"border=0>\n<TBODY>\n<TR>\n<TD class=title align=middle><STRONG>" +
	  		"对《关于对国家行政机关工作人员执行职务过程中的违法行为能否给予治安处罚的请示》的复函</STRONG>" +
	  		"</TD></TR>\n<TR>\n<TD class=NewsBody>\n<P align=center><FONT size=3></FONT>&nbsp;" +
	  		"</P>\n<P align=center><FONT size=3>国法秘函〔2005〕256号</FONT></P>\n<P><FONT size=3>" +
	  		"安徽省人民政府法制办公室：</FONT></P>\n<P><FONT size=3>　" +
	  		"　你办《关于对国家行政机关工作人员执行职务过程中的违法行为能否给予治安处罚的请示》" +
	  		"（皖府法〔2005〕44号）收悉。经研究，并征求全国人大常委会法工委的意见，现函复如下：" +
	  		"</FONT></P>\n<P><FONT size=3>　　根据有关法律规定，行政机关工作人员在执行职务时在故意或者重大过失侵犯公民合法权益造成损害的，一是承担民事责任，即承担部分或者全部的赔偿费用；二是承担行政责任，即由有关行政机关依法给予行政处分。同时，依照刑法规定，构成犯罪的，还应当承担刑事责任。行政机关工作人员执行职务时的侵权行为，不属于治安管理处罚条例规定的违反治安管理的行为，不应当给予治安管理处罚。</FONT></P>\n<P><FONT size=3></FONT>&nbsp;</P>\n<P align=right><FONT size=3>国务院法制办公室秘书行政司</FONT>" +
	  		"</P>\n<P align=right><FONT size=3>二○○五年七月八日</FONT></P></TD></TR></TBODY></TABLE>n";
	  System.out.println(Html2Text(strtext));
	  
	 }
	}


