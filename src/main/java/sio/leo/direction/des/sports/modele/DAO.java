/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sio.leo.direction.des.sports.modele;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author MENAGER
 */
public class DAO {
//Pour connecter mariadb et la base lardon
    private static Connection cnx = null;
    //pour exécuter des requêtes
    private static Statement smt=null;
    
    //retourne la connexion à la base mariadb lardon
    public static Connection getConnection()
    {
        if(cnx==null)
        {
            SeConnecter();
        }
        return cnx;
    }
    
    //retourne le statement créé avec la connexion pour pouvoir exécuter des requêtes
    public static Statement getStatement()
    {
        if(smt==null)
        {
            try
            {
                smt=(Statement)cnx.createStatement();
            }
            catch(SQLException e)
            {
                System.out.println("Création de Statement impossible");
            }
            
        }
        return smt;
    }
    
    
    
    public static void SeConnecter()
    {
        String url="jdbc:mysql://192.168.5.240:3307/directionsportg1";
        String loginBd="adming1";
        String passwd="adming1";
        try{
            cnx=(Connection)DriverManager.getConnection(url, loginBd, passwd);
            smt=(Statement)cnx.createStatement();
        }
        catch(SQLException e)
        {
            System.out.println("SQL Exception : "+e.toString());
        }
    }
        
}
