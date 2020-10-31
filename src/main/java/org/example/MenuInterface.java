package org.example;

import java.io.IOException;
import java.sql.SQLException;

public interface MenuInterface {
    void make(Context context) throws IOException, SQLException;

    void remove(Menu menu);

    void addSubMenu(Menu menu);
}
