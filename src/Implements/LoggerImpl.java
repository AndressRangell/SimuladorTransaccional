/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Implements;

import Interfaces.Fechas;
import Interfaces.Logger;
import static Globals.Variables.*;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class LoggerImpl implements Logger {

    Fechas fecha;

    public LoggerImpl(Fechas fecha) {
        this.fecha = fecha;
    }

    @Override
    public void write(String clase, String data) {
        try {
            fecha.exec();
        } catch (Exception ex) {
            tools.showDialogEx("Fechas Exception", this, ex);
        }
        final String date = fecha.getDate();
        final String time = fecha.getTime();
        try {
            final String fileName = "Logdata-" + date + ".txt";
            File folder = exTools.getFolder(LOGS_TRANS,false);
            final File f = new File(folder.getAbsolutePath() + "\\" + fileName);
            if (f.exists()) {
                final FileReader fr = new FileReader(f);
                try (final BufferedReader br = new BufferedReader(fr)) {
                    final StringBuilder fileData = new StringBuilder();
                    String line;
                    while ((line = br.readLine()) != null) {
                        fileData.append(line).append("\n");
                    }
                    final FileWriter fw = new FileWriter(f);
                    try (BufferedWriter bw = new BufferedWriter(fw)) {
                        bw.write(fileData.toString());
                        bw.append((CharSequence) time).append((CharSequence) " - CLASE: ").append((CharSequence) clase).append((CharSequence) " - ").append((CharSequence) data);
                        bw.newLine();
                        br.close();
                    }
                }
            } else {
                final FileWriter fw = new FileWriter(f);
                try (BufferedWriter bw2 = new BufferedWriter(fw)) {
                    bw2.write("Date Logs: " + date);
                    bw2.newLine();
                    bw2.append((CharSequence) time).append((CharSequence) " - CLASE: ").append((CharSequence) clase).append((CharSequence) " - ").append((CharSequence) data);
                    bw2.newLine();
                }
            }
        } catch (IOException | NullPointerException ex) {
            tools.showDialogEx("Fallo en la creacion del Log... ", clase, ex);
        }
    }
}
