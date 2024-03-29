package edu.pucmm.sparkjdbc.controllers;

import edu.pucmm.sparkjdbc.encapsulation.Article;
import edu.pucmm.sparkjdbc.encapsulation.Comment;
import edu.pucmm.sparkjdbc.encapsulation.Tag;
import edu.pucmm.sparkjdbc.encapsulation.User;
import edu.pucmm.sparkjdbc.services.ArticlesServices;
import edu.pucmm.sparkjdbc.services.CommentsServices;
import edu.pucmm.sparkjdbc.services.TagsServices;
import edu.pucmm.sparkjdbc.services.UsersServices;
import edu.pucmm.sparkjdbc.utils.Utils;

import java.util.*;

import static spark.Spark.*;

public class ArticlesController {
    public static void getRoutes() {

        before("/new-article", (request, response) -> {
            User user = request.session().attribute("user");
            if(user == null){
                response.redirect("/");
            }
        });

        before("/articles/:id/edit", (request, response) -> {
            User user = request.session().attribute("user");   
            if(user == null){
                response.redirect("/");
            }
        });


        get("/", (request, response) -> {
            Map<String, Object> obj = new HashMap<>();
            obj.put("articles", ArticlesServices.getInstance().getArticles());
            obj.put("tags", TagsServices.getInstance().getTags());
            obj.put("user", request.session().attribute("user"));
            return TemplatesController.renderFreemarker(obj, "index.ftl");
        });

        get("/new-article", (request, response) -> {
            Map<String, Object> obj = new HashMap<>();
            obj.put("user", request.session().attribute("user"));
            return TemplatesController.renderFreemarker(obj, "new-article.ftl");
        });

        post("/new-article", (request, response) -> {
            Date todaysDate = new Date();
            java.sql.Timestamp date = new java.sql.Timestamp(todaysDate.getTime());

            User user = request.session().attribute("user");
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
            ArrayList<Comment> comments = CommentsServices.getInstance().getComments(request.params("id"));
            obj.put("article", article);
            obj.put("comments", comments);
            obj.put("tags", article.getTags());
            obj.put("user", request.session().attribute("user"));
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
            obj.put("user", request.session().attribute("user"));
            return TemplatesController.renderFreemarker(obj, "edit-article.ftl");
        });

        before("/articles/:id/delete", (request, response) -> {
            User user = request.session().attribute("user");
            System.out.println(user);
            if(user == null){
                response.redirect("/");
            }
        });

        post("/articles/:id/delete", (request, response) -> {
            ArticlesServices.getInstance().deleteArticle(request.params("id"));
            System.out.println("ds");
            response.redirect("/");
            return "";
        });
    }
}
