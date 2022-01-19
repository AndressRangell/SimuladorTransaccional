/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Interfaces;

import java.sql.ResultSet;

/**
 *
 * @author ctolo
 */
public interface DbHelper {

    ResultSet getData();

    void connect();

    void close();

    void execQuery(String sql);

    int execUpdate(String sql);

    void closeSt();

    void setParent(Object parent);

    boolean tableExists(String tableName);

    void createTable(String tableName);
}
