package be03.lukacsrichard.Repository;

import java.sql.SQLException;

public abstract class MainRepository<T> implements IMainRepository<T> {

    DatabaseConnection databaseConnection;

    public MainRepository(){
        try {
            databaseConnection = new DatabaseConnection();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
