package com.ran.watchsale.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

/**
 * @author Administrator
 */
public class FileUtil {

    /**
     * 在文件里面的指定行插入一行数据
     *
     * @param inFile           文件
     * @param lineno           行号 从0开始,即插入到最上面
     * @param lineToBeInserted 要插入的数据
     */
    public static void insertStringInFile(File inFile, int lineno, String lineToBeInserted) {
        BufferedReader in = null;
        PrintWriter out = null;
        File tempFile = null;
        try {
            File inFilepath = inFile.getParentFile();
            // 临时文件
            tempFile = File.createTempFile("name", ".tmp", inFilepath);
            if (!inFile.exists()) {
                inFile.createNewFile();
            }
            // 输入
            FileInputStream fis = new FileInputStream(inFile);
            in = new BufferedReader(new InputStreamReader(fis));

            // 输出
            FileOutputStream fos = new FileOutputStream(tempFile);
            out = new PrintWriter(fos);

            // 保存一行数据
            String thisLine;
            // 行号从0开始
            int i = 0;
            while ((thisLine = in.readLine()) != null) {
                // 如果行号等于目标行，则输出要插入的数据
                if (i == lineno) {
                    out.println(lineToBeInserted);
                }
                // 输出读取到的数据
                out.println(thisLine);
                // 行号增加
                i++;
            }
            if (i == 0) {
                // 第一次读
                out.println(lineToBeInserted);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                out.flush();
                out.close();
            }
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (inFile != null) {
                // 删除原始文件
                inFile.delete();
                // 把临时文件改名为原文件名
                if (tempFile != null) {
                    tempFile.renameTo(inFile);
                }
            }
        }
    }
}
