package database;

import java.sql.Connection;
import java.sql.DriverManager;

public class ConexionBD {

    public static Connection conectar() {

        try {

            Class.forName("com.mysql.cj.jdbc.Driver");

            Connection conexion = DriverManager.getConnection(
                "jdbc:mysql://127.0.0.1:3306/turnix?useSSL=false&serverTimezone=UTC",
                "root",
                ""
            );

            System.out.println("✅ Conexion correcta a la BD");

            return conexion;

        } catch (Exception e) {

            System.out.println("❌ ERROR CONEXION BD");
            e.printStackTrace();

            return null;
        }
    }
}