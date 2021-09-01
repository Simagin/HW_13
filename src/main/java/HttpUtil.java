package main.java;

import com.google.gson.Gson;

import java.net.URI;
import java.net.http.HttpRequest;

public class HttpUtil {

    private static Gson gson = new Gson();

    private static final String URL = "https://jsonplaceholder.typicode.com";
    private static final String USERS_URL = URL.concat("/users");
    private static final String POSTS_URL = URL.concat("/posts");
    private static final String TODOS_URL = USERS_URL.concat("/%s/todos");

    public static HttpRequest createUser(User user) {
        String jsonUser = gson.toJson(user);
        return HttpRequest.newBuilder()
                .uri(URI.create(USERS_URL))
                .header("Content-type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonUser))
                .build();

    }

    public static HttpRequest changeUser(String id, User user) {
        String jsonUser = gson.toJson(user);
        return HttpRequest.newBuilder()
                .uri(URI.create(USERS_URL + "/" + id))
                .header("Content-type", "application/json")
                .PUT(HttpRequest.BodyPublishers.ofString(jsonUser))
                .build();
    }

    public static HttpRequest deleteUser(String id, User user) {
        String jsonUser = gson.toJson(user);
        return HttpRequest.newBuilder()
                .header("Content-type", "application/json; charset=UTF-8")
                .uri(URI.create(USERS_URL + "/" + id))
                .method("DELETE", HttpRequest.BodyPublishers.ofString(jsonUser))
                .build();
    }

    public static HttpRequest getUsers() {
        return HttpRequest.newBuilder()
                .uri(URI.create(USERS_URL))
                .GET()
                .build();
    }

    public static HttpRequest getUserById(String id) {
        return HttpRequest.newBuilder()
                .uri(URI.create(USERS_URL + "/" + id))
                .GET()
                .build();
    }

    public static HttpRequest getUserByUserName(String userName) {
        return HttpRequest.newBuilder()
                .uri(URI.create(String.format("%s%s%s", USERS_URL, "?username=", userName)))
                .GET()
                .build();
    }

    public static HttpRequest getUserPosts(User user) {
        return HttpRequest.newBuilder()
                .uri(URI.create(String.format("%s/%s%s", USERS_URL, user.getId(), "/posts")))
                .GET()
                .build();
    }

    public static HttpRequest getComments(Post post) {
        return HttpRequest.newBuilder()
                .uri(URI.create(POSTS_URL + "/" + post.getId() + "/comments"))
                .header("Content-type", "application/json")
                .GET()
                .build();
    }

    public static HttpRequest getUserToDos(User user) {
        return HttpRequest.newBuilder()
                .uri(URI.create(String.format(TODOS_URL, user.getId())))
                .header("Content-type", "application/json")
                .GET()
                .build();
    }
}

