package edu.pucmm.sparkjdbc.controllers;

import edu.pucmm.sparkjdbc.encapsulation.Comment;
import edu.pucmm.sparkjdbc.services.CommentsServices;
import edu.pucmm.sparkjdbc.utils.Utils;

import static spark.Spark.*;

public class CommentsController {
    public static void getRoutes() {
        post("/comments/new/:article_id", (request, response) -> {
            Comment comment = new Comment();
            comment.setComment(request.queryParams("comment"));
            CommentsServices.getInstance().createComment(request.params("article_id"), Utils.getCurrentUser().getUid(), comment);
            response.redirect("/articles/" + request.params("article_id"));
            return "";
        });

        post("/articles/:article_id/comments/:comment_id", (request, response) -> {
            CommentsServices.getInstance().deleteComment(request.params("comment_id"));
            response.redirect("/articles/" + request.params("article_id"));
            return "";
        });
    }
}
