package com.hits.util;



import java.io.*;



public class FileUtil
{
   
    public static String getFileCnt(String filePath)
    {
        StringBuffer content = new StringBuffer();
        try
        {
            File temFile = new File(filePath);
            FileReader fileReader = new FileReader(temFile);
            BufferedReader bufferReader = new BufferedReader(fileReader);
            String str = null;
            while ((str = bufferReader.readLine()) != null)
            {
                content.append(str).append("\n");
            }
            bufferReader.close();
            fileReader.close();
        }
        catch (Exception e)
        {
         
        }
        return content.toString();
    }

    public static synchronized boolean setFileCnt(String filePath, String newContent)
    {
        try
        {
            File file = new File(filePath);
            if (!file.exists()) file.createNewFile();
            PrintWriter out = new PrintWriter(new FileWriter(file));
            out.print(newContent);
            out.close();
            return true;
        }
        catch (IOException e)
        {
           
            return false;
        }
    }

   
    public static boolean DelFile(String filePath)
    {
        File file = new File(filePath);
        try
        {
            if (file.exists())
            {
                file.delete();
                return true;
            }
        }
        catch (Exception e)
        {
            
            return false;
        }
        return true;
    }

}


