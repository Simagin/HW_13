package main.java;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpResponse;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class HomeWorkToHttp {
    private static final HttpClient CLIENT = HttpClient.newHttpClient();

    public static String createNewUser(User user) throws IOException, InterruptedException {
        HttpResponse<String> response = CLIENT.send(HttpUtil.createUser(user), HttpResponse.BodyHandlers.ofString());
        return response.body();
    }

    public static String changeUser(User user) throws IOException, InterruptedException {
        user.setName("SIM");
        HttpResponse<String> response = CLIENT.send(HttpUtil.changeUser("1", user), HttpResponse.BodyHandlers.ofString());
        return response.body();
    }

    public static int deleteUser(User user) throws IOException, InterruptedException {
        HttpResponse<String> re = CLIENT.send(HttpUtil.deleteUser("11", user), HttpResponse.BodyHandlers.ofString());
        return re.statusCode();
    }

    public static String getUsers() throws IOException, InterruptedException {
        HttpResponse<String> response = CLIENT.send(HttpUtil.getUsers(), HttpResponse.BodyHandlers.ofString());
        return response.body();

    }

    public static String getUserById(String id) throws IOException, InterruptedException {
        HttpResponse<String> response = CLIENT.send(HttpUtil.getUserById(id), HttpResponse.BodyHandlers.ofString());
        return response.body();
    }

    public static String getUserByUserName(String userName) throws IOException, InterruptedException {
        HttpResponse<String> response = CLIENT.send(HttpUtil.getUserByUserName(userName), HttpResponse.BodyHandlers.ofString());
        return response.body();
    }

    public static void getUserPostsAndComments(User user) throws IOException, InterruptedException {
        HttpResponse<String> response = CLIENT.send(HttpUtil.getUserPosts(user), HttpResponse.BodyHandlers.ofString());
        List<Post> posts = new Gson().fromJson(response.body(), new TypeToken<List<Post>>() {
        }.getType());
        Post maxPost = Collections.max(posts, Comparator.comparingInt(p -> Integer.parseInt(p.getId())));
        String fileName = "user-" + user.getId() + "-post-" + maxPost.getId() + "-comments.json";

        HttpResponse<Path> comments = CLIENT.send(HttpUtil.getComments(maxPost), HttpResponse.BodyHandlers.ofFile(Paths.get(fileName)));
        System.out.println(comments.body());
    }

    public static List<ToDos> getNotComplitedTodos(User user) throws IOException, InterruptedException {
        HttpResponse<String> response = CLIENT.send(HttpUtil.getUserToDos(user), HttpResponse.BodyHandlers.ofString());
        List<ToDos> toDos = new Gson().fromJson(response.body(), new TypeToken<List<ToDos>>() {
        }.getType());
        return toDos.stream()
                .filter(toDos1 -> !toDos1.isCompleted())
                .collect(Collectors.toList());
    }

    public static void main(String[] args) {
        User user = new User("Oleg", "Simahin", 1, "Typoleva str.",
                "Simagin@JAVA.NET", "123", "Simagin");

        try {

            System.out.println("execute 1.1 ->Create :\n" + createNewUser(user));
            System.out.println("execute 1.2 -> Change Name : \n" + changeUser(user));
            System.out.println("execute 1.3 -> Delete User StatusCode\n" + deleteUser(user));
            System.out.println("execute 1.4 -> All users \n" + getUsers());
            System.out.println("execute 1.5 -> User at ID: 10 \n" + getUserById("10"));
            System.out.println("execute 1.6 -> User with USERNAME: Samantha \n" + getUserByUserName("Samantha"));
            System.out.println("execute 2");
            createNewUser(user);
            getUserPostsAndComments(user);
            System.out.println("execute 3");
            System.out.println(getNotComplitedTodos(user));

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
