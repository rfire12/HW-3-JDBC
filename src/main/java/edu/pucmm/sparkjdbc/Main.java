package edu.pucmm.sparkjdbc;

import edu.pucmm.sparkjdbc.services.BootStrapServices;
import edu.pucmm.sparkjdbc.services.DataBaseServices;

import java.sql.SQLException;

import static spark.Spark.staticFiles;

import spark.ModelAndView;
import spark.template.freemarker.FreeMarkerEngine;

import java.util.HashMap;
import java.util.Map;

import static spark.Spark.*;


public class Main {
    public static String renderFreemarker(Map<String, Object> model, String templatePath) {
        return new FreeMarkerEngine().render(new ModelAndView(model, templatePath));
    }
    public static void main(String[] args) throws SQLException {

        staticFiles.location("/publico");

        // Iniciando el servicio
        BootStrapServices.startDb();

        // Crear las tablas
        BootStrapServices.createTables();

        // Probando conexión
        DataBaseServices.getInstance().testConnection();

        get("/", (request, response) -> {
            return renderFreemarker(null,"/index.ftl");
        });
    }
}
