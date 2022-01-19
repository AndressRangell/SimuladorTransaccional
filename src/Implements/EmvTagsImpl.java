/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Implements;

import static Globals.SqlData.*;
import static Globals.Variables.*;
import Interfaces.EmvTags;
import java.sql.SQLException;
import java.util.ArrayList;
import Interfaces.DbHelper;

/**
 *
 * @author ctolo
 */
public class EmvTagsImpl implements EmvTags {

    String tag;
    String name;
    String des;
    ArrayList<EmvTagsImpl> tags = new ArrayList<>();
    EmvTagsImpl emv;

    public EmvTagsImpl() {
    }

    @Override
    public void setAPP(String column, String value) {
        switch (column) {
            case TAG:
                setTag(value);
                break;
            case NAME:
                setName(value);
            case DES:
                setDes(value);
                break;
            default:
                break;
        }
    }

    public boolean sqlQuery(String tipo) {
        DbHelper db = new dbHelperImpl();
        db.setParent(this);
        db.connect();
        try {
            String sql = "SELECT * FROM " + TAGS_TABLE;
            scriptLogs(sql);
            if (!db.tableExists(TAGS_TABLE)) {
                db.createTable(TAGS_TABLE);
            }
            db.execQuery(sql);
            int index;
            while (db.getData().next()) {
                emv = new EmvTagsImpl();
                index = 1;
                for (String string : LISTADO_SQL_TAGS) {
                    emv.setAPP(string, db.getData().getString(index++).trim());
                }
                tags.add(emv);
            }
            db.close();
            db.closeSt();
            return true;
        } catch (SQLException ex) {
            tools.showDialogEx("SQLException", this, ex);
            db.close();
            db.closeSt();
        }
        db.close();
        db.closeSt();
        return false;
    }

    @Override
    public ArrayList<EmvTagsImpl> getAllData(String tipo) {
        if (sqlQuery(tipo)) {
            scriptLogs("Consulta Exitosa!");
        }
        scriptLogs("Cantidad de tags..." + tags.size());
        return tags;
    }

    @Override
    public ArrayList<EmvTagsImpl> getAllData(String tipo, String header) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void scriptLogs(String data) {
        tools.scriptLogs(this, data);
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

}
