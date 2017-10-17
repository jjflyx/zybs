package com.hits.common.util;

import org.hyperic.sigar.Mem;
import org.hyperic.sigar.NetFlags;
import org.hyperic.sigar.NetInterfaceConfig;
import org.hyperic.sigar.Sigar;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashSet;
import java.util.Set;


public class DiskUtils {
  private DiskUtils() {  }

  public static String getSigarSequence() {
    Set<String> result = new HashSet<>();
    try {
      Sigar sigar = new Sigar();
      String[] ifaces = sigar.getNetInterfaceList();
      for (String iface : ifaces) {
        NetInterfaceConfig cfg = sigar.getNetInterfaceConfig(iface);
        if (NetFlags.LOOPBACK_ADDRESS.equals(cfg.getAddress()) || (cfg.getFlags() & NetFlags.IFF_LOOPBACK) != 0
                || NetFlags.NULL_HWADDR.equals(cfg.getHwaddr())) {
          continue;
        }
        String mac = cfg.getHwaddr().replace(":","");
        result.add(mac);
      }
      Mem mem = sigar.getMem();
    }catch (Exception e){
      return null;
    }
    return result.toString();
  }

  /**
   * 验证授权码
   * @param sn 授权码
   * @return
   */
  public static boolean validateSN(String sn){
    boolean result = false;
    //获取产品序列号（Mac地址）
    String number = StringUtil.null2String(DiskUtils.getSigarSequence());
    String snStr = "";
    if(sn != null && !"".equals(sn)){
      String sn1 = sn.toLowerCase();
      snStr = DecodeUtil.licenseDecrypt(sn1); //解密 单位|时间|硬盘号|失效时间
      String[] snArr = snStr.split("\\|");
      if(snArr != null && snArr.length == 4
              && "hits".equals(snArr[0])
              && number.equals(snArr[2])){

        java.util.Date nowdate=new java.util.Date();
        String sxrq = snArr[3];
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        java.util.Date d;
        try {
          d = sdf.parse(sxrq);
          boolean flag = d.after(nowdate);
          return flag;
        } catch (ParseException e) {
          return false;
        }
      }
      String s = "";
    }
    return result;
  }
}