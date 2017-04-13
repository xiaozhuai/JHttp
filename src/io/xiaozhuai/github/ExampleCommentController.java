package io.xiaozhuai.github;

import io.xiaozhuai.github.jhttp.HttpController;

/**
 * Created by xiaozhuai on 17/4/13.
 */
public class ExampleCommentController extends HttpController {

    public void add(){
        response.append("Add a comment");
    }

    public void delete(){
        response.append("Delete a comment");
    }

    public void update(){
        response.append("Update a comment");
    }

}
