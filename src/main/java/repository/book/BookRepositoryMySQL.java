package repository.book;

import model.Book;
import model.builder.BookBuilder;
import model.validator.Notification;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BookRepositoryMySQL implements BookRepository{
    private final Connection connection;

    public BookRepositoryMySQL(Connection connection){
        this.connection = connection;
    }

    @Override
    public List<Book> findAll() {
        String sql = "SELECT * FROM book;";

        List<Book> books = new ArrayList<>();

        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);

            while(resultSet.next()){
                books.add(getBookFromResultSet(resultSet));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return books;
    }

    @Override
    public Optional<Book> findById(Long id) {
        String sql = "SELECT * FROM book WHERE id=?";
        Optional<Book> book = Optional.empty();

        try{
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();

            if(resultSet.next()){
                book = Optional.of(getBookFromResultSet(resultSet));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return book;
    }

    @Override
    public boolean save(Book book) {
        String newSql = "INSERT INTO book(id, author, title, publishedDate, price, stock) VALUES(null, ?, ?, ?, ?, ?);";

        try{
            PreparedStatement statement = connection.prepareStatement(newSql);
            statement.setString(1, book.getAuthor());
            statement.setString(2, book.getTitle());
            statement.setDate(3, Date.valueOf(book.getPublishedDate()));
            statement.setFloat(4, book.getPrice());
            statement.setInt(5, book.getStock());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public boolean delete(Book book) {
        String newSql = "DELETE FROM book WHERE author=? AND title=?;";

        try{
            PreparedStatement statement = connection.prepareStatement(newSql);
            statement.setString(1, book.getAuthor());
            statement.setString(2, book.getTitle());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public void removeAll() {
        String sql = "DELETE FROM book WHERE id>=0;";

        try{
            Statement statement = connection.createStatement();
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Notification<Boolean> sellBooks(Book book, Integer nbOfBooks) {
        String sellSql = "UPDATE book SET stock=? where title=? and author=?;";
        Notification<Boolean> sellNotification = new Notification<>();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sellSql);
            if(book.getStock() < nbOfBooks){
                sellNotification.addError("Number of books introduced exceeds the available stock!");
                sellNotification.setResult(Boolean.FALSE);
                return sellNotification;
            }
            preparedStatement.setInt(1, book.getStock() - nbOfBooks);
            preparedStatement.setString(2, book.getTitle());
            preparedStatement.setString(3, book.getAuthor());
            preparedStatement.executeUpdate();

            if(book.getStock() - nbOfBooks == 0){
                delete(book);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            sellNotification.addError("There was a problem with the database!");
            sellNotification.setResult(Boolean.FALSE);

        }

        sellNotification.setResult(Boolean.TRUE);
        return sellNotification;
    }

    private Book getBookFromResultSet(ResultSet resultSet) throws SQLException{
        return new BookBuilder().setId(resultSet.getLong("id"))
                .setTitle(resultSet.getString("title"))
                .setAuthor(resultSet.getString("author"))
                .setPublishedDate(new java.sql.Date(resultSet.getDate("publishedDate").getTime()).toLocalDate())
                .setPrice(resultSet.getFloat("price"))
                .setStock(resultSet.getInt("stock"))
                .build();
    }
}
