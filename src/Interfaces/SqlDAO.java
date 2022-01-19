/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Interfaces;

import java.util.ArrayList;

/**
 *
 * @author ctolo
 * @param <Object>
 */
public interface SqlDAO<Object> {

    void setAPP(String column, String value);

    ArrayList<Object> getAllData(String tipo);

    ArrayList<Object> getAllData(String tipo, String header);

}
