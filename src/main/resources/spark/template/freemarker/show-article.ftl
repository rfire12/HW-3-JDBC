<!DOCTYPE html>
<html lang="en">

<#include 'head.ftl'>

<body>

<#include 'navbar.ftl'>

<br/>
<!-- Page Content -->
<div class="container">
    <div class="row">

        <div class="col-md-9">
            <div class="card">
                <div class="card-body">
                    <br/>
                    <h2>${article.title}</h2>
                    <br/>
                    <p>${article.information}</p>
                    <br/>
                    <p>Published by: (AUTHOR) on ${article.date}</p>

                    <br/><br/>
                    <h3>Tags:</h3>
                    <#list tags as tag>
                        <a href="#" class="badge badge-primary">${tag.tag}</a>
                    </#list>
                    <br/><br/><br/>
                    <form action="/comments/new/${article.uid}" method="post">
                        <textarea placeholder="Comment here" class="form-control" name="comment"></textarea>
                        <br/>
                        <button type="submit" class="btn btn-primary">Post</button>
                    </form>
                    <br/>
                    <h3>Comments:</h3>
                    <hr/>
                    <#list comments as comment>
                        <p>
                            AUTHOR commented:
                        </p>
                        <p>
                            ${comment.comment}
                        </p>
                        <form action="/articles/${article.uid}/comments/${comment.uid}" method="post">
                            <button type="submit" class="btn btn-danger">Delete</button>
                        </form>
                        <hr/>
                    </#list>
                </div>
            </div>
        </div>

        <div class="col-md-3">
            <!-- Search Widget -->
            <div class="card my-4">
                <h5 class="card-header">Settings</h5>
                <div class="card-body">
                    <a href="/articles/${article.uid}/edit" class="btn btn-dark">Edit this article</a>
                    <form action="/articles/${article.uid}/delete" method="post">
                        <button type="submit" class="btn btn-danger">Delete this article</button>
                    </form>
                </div>
            </div>
        </div>


    </div>
</div>
<!-- /.container -->

<#include 'footer.ftl'>

</body>

</html>
