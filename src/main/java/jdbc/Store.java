package jdbc;

import html.Post;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public interface Store {
    void save(Post post) throws SQLException, IOException, ClassNotFoundException;

    List<Post> getAll() throws SQLException;

    Post findById(int id) throws SQLException;
}
