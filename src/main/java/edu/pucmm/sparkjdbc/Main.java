package edu.pucmm.sparkjdbc;

import edu.pucmm.sparkjdbc.encapsulation.Article;
import edu.pucmm.sparkjdbc.encapsulation.Tag;
import edu.pucmm.sparkjdbc.encapsulation.User;
import edu.pucmm.sparkjdbc.services.ArticlesServices;
import edu.pucmm.sparkjdbc.services.BootStrapServices;
import edu.pucmm.sparkjdbc.services.DataBaseServices;

import java.sql.SQLException;

import static spark.Spark.staticFiles;

import edu.pucmm.sparkjdbc.services.TagsServices;
import edu.pucmm.sparkjdbc.utils.Utils;
import spark.ModelAndView;
import spark.template.freemarker.FreeMarkerEngine;

import java.util.ArrayList;
import java.util.Date;
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
            Map<String, Object> articles = new HashMap<>();
            articles.put("articles", ArticlesServices.getInstance().getArticles());
            return renderFreemarker(articles, "index.ftl");
        });

        get("/new-article", (request, response) -> {
            return renderFreemarker(null,"new-article.ftl");
        });

        post("/new-article", (request, response) -> {
            Date todaysDate = new Date();
            java.sql.Date date = new java.sql.Date(todaysDate.getTime());
            User author = new User("autor01256", "Luis Garcia", "123456", "admin"); //This should be deleted when sessions get implemented
            String[] tags = request.queryParams("tags").split(",");

            ArrayList<Tag> tagList = Utils.arrayToTagList(tags);
            Article article = new Article(request.queryParams("title"), request.queryParams("article-body"), author, date, tagList);
            ArticlesServices.getInstance().createArticle(article);
            response.redirect("/");
            return "";
        });
    }
}
