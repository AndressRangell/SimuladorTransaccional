/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Implements;

import Interfaces.Fechas;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author ctolo
 */
public class FechasImpl implements Fechas {

    private Date date;
    private SimpleDateFormat dfHora;
    private SimpleDateFormat dfFecha;
    private SimpleDateFormat dfFulldate;

    @Override
    public void exec() throws Exception {
        date = new Date();
        dfHora = new SimpleDateFormat("HHmmss");
        dfFecha = new SimpleDateFormat("MMdd");
        dfFulldate = new SimpleDateFormat("YYYYMMddHHmmss");
    }

    @Override
    public String getTime() {
        return dfHora.format(date);
    }

    @Override
    public String getDate() {
        return dfFecha.format(date);
    }

    @Override
    public String getDateTime() {
        return dfFulldate.format(date);
    }

}
