package com.teaminology.hp;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.xml.sax.InputSource;


import com.globalsight.www.webservices.AmbassadorProxy;
import com.globalsight.www.webservices.AmbassadorService;
import com.globalsight.www.webservices.AmbassadorServiceLocator;
import com.globalsight.www.webservices.Ambassador;

public class MainTest
{

    public static void main(String[] args) {
        try {
            AmbassadorProxy proxy = null;
            String url = "http://107.1.165.122/globalsight/services/AmbassadorWebService?wsdl";
            proxy = new AmbassadorProxy(url, "gspaypalpm", "password");
            System.out.println("callwebservice Object created " + proxy);
            String accessToken = proxy.login("paypalanyone", "password");
            System.out.println("accessTokent " + accessToken);
            String ss = "http://10.7.1.7:80/globalsight/cxedocs/PayPal";
            String s = ss.substring(ss.indexOf("globalsight") + 11, ss.length());
            System.out.println(s);

            System.out.println("dfdfdf");


        }
        catch (Exception e) {
            e.printStackTrace();
        }


    }

    public static File unzip(File inFile, File outFolder) {
        final int BUFFER = 2048;
        try {
            BufferedOutputStream out = null;
            ZipInputStream in = new ZipInputStream(
                    new BufferedInputStream(
                            new FileInputStream(inFile)));
            ZipEntry entry;

            File entrySupposedPath = null;
            while ((entry = in.getNextEntry()) != null) {
                System.out.println("Extracting: " + entry);
                int count;
                byte data[] = new byte[BUFFER];

                //We will try to reconstruct the entry directories
                entrySupposedPath = new File(outFolder.getAbsolutePath() + File.separator + entry.getName());
                String name = entry.getName();

                name.substring(0, name.indexOf("/"));
                //   System.out.println("name"+File.separator+entry.getName().indexOf("\\"));
                //Does the parent folder exist?
                if (!entrySupposedPath.getParentFile().exists()) {
                    entrySupposedPath.getParentFile().mkdirs();
                }


                // write the files to the disk
                out = new BufferedOutputStream(
                        new FileOutputStream(outFolder.getPath() + "/" + entry.getName()), BUFFER);
                while ((count = in.read(data, 0, BUFFER)) != -1) {
                    out.write(data, 0, count);
                }
                out.flush();
                out.close();
            }
            in.close();
            return entrySupposedPath;
        }
        catch (Exception e) {
            e.printStackTrace();
            return inFile;
        }
    }

    public static void saveFileFromUrlWithJavaIO(String fileName, String fileUrl)
            throws MalformedURLException, IOException {
        BufferedInputStream in = null;
        FileOutputStream fout = null;
        try {
            in = new BufferedInputStream(new URL(fileUrl).openStream());
            fout = new FileOutputStream(fileName);

            byte data[] = new byte[1024];
            int count;
            while ((count = in.read(data, 0, 1024)) != -1) {
                fout.write(data, 0, count);
            }
        }
        finally {
            if (in != null)
                in.close();
            if (fout != null)
                fout.close();
        }
    }
}
