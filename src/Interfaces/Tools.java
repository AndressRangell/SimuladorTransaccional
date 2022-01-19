/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Interfaces;

import java.io.File;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author ctolo
 */
public interface Tools {

    String padText(String s, int size, int type, char c);

    boolean readXml(File xmlFile, String option);

    String getBackPath(String newFolder);

    void scriptLogs(Object objClass, String data);

    boolean deleteDirectory(File directory);

    void showDialog(String message, Object objClass, int Type);

    String showInput(String message);

    void showDialogEx(String message, Object objClass, Exception ex);

    String readFile(File file);

    int getTimeTrans(Date fecIni, Date fecFin, TimeUnit unit);

}
