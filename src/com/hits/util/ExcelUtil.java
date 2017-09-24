package com.hits.util;

import jxl.Sheet;
import jxl.Cell;
import jxl.Workbook;

import java.io.File;
import java.util.ArrayList;
import javax.servlet.http.HttpSession;

/**
 * Created by IntelliJ IDEA.
 * User: 潘斌
 * Date: 2006-6-3
 * Time: 10:35:15
 * To change this template use File | Settings | File Templates.
 */
public class ExcelUtil
{
    /**
     * 读取EXCEL中的数据
     *
     * @param path
     * @return 一个列表
     */
    public static ArrayList ResdExcel(String path, HttpSession session)
    {
        Workbook rwb = null;
        ArrayList excel = new ArrayList();
        Cell cell;
        try
        {
            rwb = Workbook.getWorkbook(new File(session.getServletContext().getRealPath(path.substring(path.indexOf("/upload")))));  //得到其绝对路径));
            int i = 0, j = 0, k = 0;
            Sheet[] sheets = rwb.getSheets();    //判断有多少张表
            for (k = 0; k < sheets.length; k++)      //读取每一张表内容
            {
                ArrayList excelsheet = new ArrayList();
                for (i = 0; i < sheets[k].getRows(); i++)   //读取每一行内容
                {
                    ArrayList excelrow = new ArrayList();
                    for (j = 0; j < sheets[k].getColumns(); j++)   //读取每一列内容
                    {
                        cell = sheets[k].getCell(j, i);
                        excelrow.add(cell.getContents());
                    }
                    excelsheet.add(excelrow);
                }
                excel.add(excelsheet);
            }
            rwb.close();
        }
        catch (Exception e)
        {
        	e.printStackTrace();
        }
        return excel;
    }
    /**
     * 读取EXCEL中的数据
     *
     * @param path
     * @return 一个列表
     */
    public static ArrayList ResdExcelByRealpath(String path)
    {
        Workbook rwb = null;
        ArrayList excel = new ArrayList();
        Cell cell;
        try
        {
            rwb = Workbook.getWorkbook(new File(path));  //得到其绝对路径));
            int i = 0, j = 0, k = 0;
            Sheet[] sheets = rwb.getSheets();    //判断有多少张表
            for (k = 0; k < sheets.length; k++)      //读取每一张表内容
            {
                ArrayList excelsheet = new ArrayList();
                for (i = 0; i < sheets[k].getRows(); i++)   //读取每一行内容
                {
                    ArrayList excelrow = new ArrayList();
                    for (j = 0; j < sheets[k].getColumns(); j++)   //读取每一列内容
                    {
                        cell = sheets[k].getCell(j, i);
                        excelrow.add(cell.getContents());
                    }
                    excelsheet.add(excelrow);
                }
                excel.add(excelsheet);
            }
            rwb.close();
        }
        catch (Exception e)
        {
            System.out.println("###"+e);
        }
        return excel;
    }
}

