package org.example;

import java.io.IOException;
import java.sql.SQLException;

public interface Action <T>{
    void doIt(T context) throws IOException, SQLException;
}
