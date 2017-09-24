package com.hits.useragentutil;

public class test {
	
	public static void main(String[] args) {
		Browser browser = Browser.parseUserAgentString("Mozilla/5.0 (Windows NT 6.1; WOW64; Trident/7.0; rv:11.0) like Gecko LBBROWSER");	
//		OperatingSystem os = userAgent.getOperatingSystem();
		System.out.println(browser.getName());
		System.out.println(browser.getBrowserType());
	}

}
