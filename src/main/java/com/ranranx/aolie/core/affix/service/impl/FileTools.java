package com.ranranx.aolie.core.affix.service.impl;

import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipOutputStream;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.io.*;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * 导出文件工具
 */
public class FileTools {

    public static String batchExportAffixFile(String realPath, List<Map<String, Object>> param) {
        if (CollectionUtils.isEmpty(param)) {
            return "导出的参数为空！";
        }
        return exportZipFile(param, realPath, null);
    }

    private static String exportZipFile(List<Map<String, Object>> list, String realPath, String zipFileName) {
        ZipOutputStream zos = null;
        String filename = UUID.randomUUID().toString();
        if (zipFileName != null && StringUtils.hasLength(zipFileName)) {
            filename = zipFileName;
        }
        String fileFullName = realPath + File.separator + filename;
        File file = new File(fileFullName);
        OutputStream os = null;
        FileInputStream fis = null;
        BufferedInputStream bis = null;
        try {
            os = new FileOutputStream(file);
            zos = new ZipOutputStream(os);
            zos.setEncoding("gbk");
            ZipEntry ze = null;
            //输出文件用的字节数组,每次发送2048个字节到输出流：
            byte[] buf = new byte[2048];
            int readLength = 0;
            for (int i = 0; i < list.size(); i++) {
                //list为存放路径的数组 循环可以得到路径和文件名
                String filePath = list.get(i).get("filePath").toString();
                String fileName = list.get(i).get("fileName").toString();

                File f = new File(filePath);
                if (!f.exists()) {
                    continue;
                }
                ze = new ZipEntry(fileName);
                ze.setSize(f.length());
                ze.setTime(f.lastModified());
                zos.putNextEntry(ze);
                fis = new FileInputStream(f);
                bis = new BufferedInputStream(fis);
                while ((readLength = bis.read(buf, 0, 2048)) != -1) {
                    zos.write(buf, 0, readLength);
                }
                zos.closeEntry();
            }
        } catch (IOException ex) {

        } finally {
            if (zos != null) {
                try {
                    zos.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (bis != null) {
                try {
                    bis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return fileFullName;
    }
}
