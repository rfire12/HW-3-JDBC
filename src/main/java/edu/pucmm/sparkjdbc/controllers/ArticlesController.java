package edu.pucmm.sparkjdbc.controllers;

import edu.pucmm.sparkjdbc.encapsulation.Article;
import edu.pucmm.sparkjdbc.encapsulation.Tag;
import edu.pucmm.sparkjdbc.encapsulation.User;
import edu.pucmm.sparkjdbc.services.ArticlesServices;
import edu.pucmm.sparkjdbc.services.TagsServices;
import edu.pucmm.sparkjdbc.utils.Utils;

import java.util.*;

import static spark.Spark.*;

public class ArticlesController {
    public static void getRoutes() {
        get("/", (request, response) -> {
            Map<String, Object> obj = new HashMap<>();
            obj.put("articles", ArticlesServices.getInstance().getArticles());
            obj.put("tags", TagsServices.getInstance().getTags());
            return TemplatesController.renderFreemarker(obj, "index.ftl");
        });

        get("/new-article", (request, response) -> {
            return TemplatesController.renderFreemarker(null, "new-article.ftl");
        });

        post("/new-article", (request, response) -> {
            Date todaysDate = new Date();
            java.sql.Timestamp date = new java.sql.Timestamp(todaysDate.getTime());
            User user = Utils.getCurrentUser();
            String[] tags = request.queryParams("tags").split(",");

            ArrayList<Tag> tagList = Utils.arrayToTagList(tags);
            Article article = new Article(request.queryParams("title"), request.queryParams("article-body"), user, date, tagList);
            ArticlesServices.getInstance().createArticle(article);
            response.redirect("/");
            return "";
        });

        get("/articles/:id", (request, response) -> {
            Map<String, Object> obj = new HashMap<>();
            Article article = ArticlesServices.getInstance().getArticle(request.params("id"));
            obj.put("article", article);
            obj.put("comments", article.getComments());
            obj.put("tags", article.getTags());
            return TemplatesController.renderFreemarker(obj, "show-article.ftl");
        });

        post("/articles/:id", (request, response) -> {
            Article article = ArticlesServices.getInstance().getArticle(request.params("id"));
            article.setTitle(request.queryParams("title"));
            article.setInformation(request.queryParams("article-body"));

            String[] tags = request.queryParams("tags").split(",");
            ArrayList<Tag> tagList = Utils.arrayToTagList(tags);
            article.setTags(tagList);

            ArticlesServices.getInstance().updateArticle(article);
            response.redirect("/articles/" + request.params("id"));
            return "";
        });

        get("/articles/:id/edit", (request, response) -> {
            Map<String, Object> obj = new HashMap<>();
            Article article = ArticlesServices.getInstance().getArticle(request.params("id"));
            ArrayList<Tag> tags = article.getTags();
            String tagsTxt = "";
            for (Tag tag : tags) {
                tagsTxt += tag.getTag() + ",";
            }
            if (tagsTxt.endsWith(",")) {
                tagsTxt = tagsTxt.substring(0, tagsTxt.length() - 1);
            }
            obj.put("article", article);
            obj.put("tags", tagsTxt);
            return TemplatesController.renderFreemarker(obj, "edit-article.ftl");
        });

        post("/articles/:id/delete", (request, response) -> {
            ArticlesServices.getInstance().deleteArticle(request.params("id"));
            response.redirect("/");
            return "";
        });
    }
}
