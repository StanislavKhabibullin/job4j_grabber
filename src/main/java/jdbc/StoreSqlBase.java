package jdbc;

import html.Post;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class StoreSqlBase implements Store {
    private static String pathproperties =
            "C:\\projects\\job4j_grabber\\src\\main\\resources\\storeSqlBase.properties";
    private static Connection connect;

    private void init() throws IOException, ClassNotFoundException, SQLException {
        try (FileInputStream in = new FileInputStream(pathproperties)) {
            Properties properties = new Properties();
            properties.load(in);
            Class.forName(properties.getProperty("driver-class-name"));
            connect = DriverManager.getConnection(
                    properties.getProperty("url"),
                    properties.getProperty("username"),
                    properties.getProperty("password")
            );
        }
    }

    private void createTable() throws SQLException {

        Statement statement = connect.createStatement();
        try {
            String sql = String.format(
                    "Create table if not exists "
                            + "razr"
                            + "(id serial primary key,"
                            + "title varchar(250),"
                            + "link varchar(250),"
                            + "description text,"
                            + "created TIMESTAMP NOT NULL UNIQUE);"
            );
            statement.execute(sql);
            System.out.println(getTableScheme(connect, "razr"));
        } finally {
            statement.close();
        }
    }

    private void insert(Post post) throws SQLException {
        try (PreparedStatement preparedStatement = connect.prepareStatement(
                "insert into razr(title, link, description, created) VALUES(?, ?, ?, ?) ON CONFLICT DO NOTHING"
        )) {
            preparedStatement.setString(1, post.getTitle());
            preparedStatement.setString(2, post.getLink());
            preparedStatement.setString(3, post.getDescription());
            preparedStatement.setTimestamp(4, Timestamp.valueOf(post.getCreated()));
            preparedStatement.execute();
        }
    }

    @Override
    public void save(Post post) throws SQLException, IOException, ClassNotFoundException {
            init();
            createTable();
            insert(post);
    }

    @Override
    public List<Post> getAll() throws SQLException {
        List<Post> rezult = new ArrayList<>();
        try (PreparedStatement preparedStatement = connect.prepareStatement("select * from razr")) {
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    rezult.add(new Post(
                         resultSet.getInt("id"),
                            resultSet.getString("title"),
                            resultSet.getString("link"),
                            resultSet.getString("description"),
                            resultSet.getTimestamp("created").toLocalDateTime()
                                ));
                            }
            }
        }
        return rezult;
    }

    @Override
    public Post findById(int id) throws SQLException {
        Post rezult = null;
        try (PreparedStatement preparedStatement = connect.prepareStatement("select * from razr where id = ?")) {
            preparedStatement.setInt(1, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    rezult = new Post(
                            resultSet.getInt("id"),
                            resultSet.getString("title"),
                            resultSet.getString("link"),
                            resultSet.getString("description"),
                            resultSet.getTimestamp("created").toLocalDateTime()
                    );
                                    }
            }
        }
        return rezult;
    }

    public static void main(String[] args) throws SQLException, IOException, ClassNotFoundException {
        StoreSqlBase testBase = new StoreSqlBase();
        testBase.init();
      //  testBase.save(new Post(0, "1", "", "", LocalDateTime.now()));
        List<Post> result = testBase.getAll();
        for (Post element
                :result) {
            System.out.println(element);
        }
        Post finder = testBase.findById(2);
        System.out.println("id = 2; Element - " + finder);

    }

    public static String getTableScheme(Connection connection, String tableName) throws SQLException {
        StringBuilder scheme = new StringBuilder();
        DatabaseMetaData metaData = connection.getMetaData();
        try (ResultSet columns = metaData.getColumns(null, null,
                tableName, null)) {
            scheme.append(String.format("%-15s %-15s%n", "column", "type"));
            while (columns.next()) {
                scheme.append(String.format("%-15s %-15s%n",
                        columns.getString("COLUMN_NAME"),
                        columns.getString("TYPE_NAME")));
            }
        }
        return scheme.toString();
    }
}
