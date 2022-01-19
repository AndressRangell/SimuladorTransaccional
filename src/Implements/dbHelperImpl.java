/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Implements;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import static Globals.SqlData.*;
import static Globals.Variables.*;
import java.sql.DatabaseMetaData;
import Interfaces.DbHelper;

/**
 *
 * @author ctolo
 */
public class dbHelperImpl implements DbHelper {

    String parent;

    public PreparedStatement sql;
    public Connection con = null;
    public ResultSet data;
    Statement st = null;

    @Override
    public ResultSet getData() {
        if (data != null) {
            return data;
        } else {
            return null;
        }
    }

    @Override
    public void connect() {
        scriptLogs("llegada a connect >>" + parent);
        File file = exTools.getFile(DATABASE_NAME);
        String url = getUrl(file.getAbsolutePath());
        try {
            Class.forName("org.sqlite.JDBC");
            con = DriverManager.getConnection(url);
            if (con != null) {
                con.close();
                con = (Connection) DriverManager.getConnection(url);
            } else {
                con = (Connection) DriverManager.getConnection(url);
            }
        } catch (SQLException ex) {
            tools.showDialogEx("Error SQLException ", this, ex);
        } catch (ClassNotFoundException ex) {
            tools.showDialogEx("No se ha podido conectar ClassNotFoundException ", this, ex);
        }
    }

    @Override
    public void close() {
        scriptLogs("llegada a close >>" + parent);
        try {
            if (con != null) {
                con.close();
            }
        } catch (SQLException ex) {
            tools.showDialogEx("Error SQLException ", this, ex);
        }

    }

    @Override
    public void execQuery(String sql) {
        scriptLogs("LLegada a execQuery >>" + parent);
        try {
            st = con.createStatement();
            data = st.executeQuery(sql);
        } catch (SQLException ex) {
            tools.showDialogEx("Error SQLException ", this, ex);
        }
    }

    @Override
    public int execUpdate(String sql) {
        scriptLogs("LLegada a execUpdate");
        try {
            st = con.createStatement();
            st.executeUpdate(sql);
            scriptLogs("Data Updated");
        } catch (SQLException ex) {
            tools.showDialogEx("Error SQLException ", this, ex);
        }
        return 0;
    }

    public void scriptLogs(String data) {
        tools.scriptLogs(this, data);
    }

    @Override
    public void closeSt() {
        try {
            if (st == null) {
                st.close();
            }
        } catch (SQLException ex) {
            tools.showDialogEx("Error SQLException ", this, ex);
        }
    }

    @Override
    public void setParent(Object obj) {
        String father = obj.getClass().getSimpleName();
        this.parent = father;
    }

    @Override
    public boolean tableExists(String tableName) {
        connect();
        try {
            DatabaseMetaData md = con.getMetaData();
            ResultSet rs = md.getTables(null, null, tableName, null);
            return rs.getRow() > 0;
        } catch (SQLException ex) {
            tools.showDialogEx("Error SQLException ", this, ex);
        }
        return false;
    }

    @Override
    public void createTable(String tableName) {
        String query;
        if (tableName.equals(TABLE_DROP)) {
            for (String bd : LISTADO_DB) {
                query = sqlDat.getDropTables(bd);
                tableQuery(query);
            }
        } else {
            query = sqlDat.getCreateTables(tableName);
            tableQuery(query);
            if (!tableName.equals(CUENTAS_TABLE)) {
                tableQuery("DELETE FROM " + tableName);
                String insert = sqlDat.getInsertTables(tableName);
                tableQuery(insert);
            }
        }
    }

    public void tableQuery(String sql) {
        scriptLogs("LLegada a tableQuery >>" + parent);
        try {
            st = con.createStatement();
            st.execute(sql);
        } catch (SQLException ex) {
            tools.showDialogEx("Error SQLException ", this, ex);
        }
    }
}
