package org.example;


import java.io.IOException;
import java.sql.*;

public class Movies {
    public static final String URL = "jdbc:mysql://localhost/Movies";
    public static final String USER = "root";
    public static final String PASSWORD = "";

    public void createDataBaseMovies() {
        try (Connection connect = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement stmt = connect.createStatement()) {
            connect.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
            System.out.println("Добро пожаловать в программу по работе с базой данных фильмов !!!");
            Menu menu = new Menu("Главное меню");


            Menu menu1 = new Menu("Изменить данные в базе данных", context -> {
                Menu menu1_1 = new Menu("Главное меню пункта 1");

                AddMovie addMovie = new AddMovie();
                Menu menu1p1 = new Menu("Добавить фильм в базу данных", context1 -> {
                    addMovie.doIt(connect);
                });


                Menu menu1p2 = new Menu("Удалить фильм из базы данных", context1 -> {
                    RemoveMovieFromDatabase removeMovieFromDatabase = new RemoveMovieFromDatabase();
                    removeMovieFromDatabase.doIt(connect);
                });

                Menu menu1p3 = new Menu("Редактировать информацию о фильме", context1 -> {
                    Menu menu2 = new Menu("Главное меню пункта 3");

                    Menu menu2p1 = new Menu("Редактировать название фильма", context2 -> {
                        EditMovieTitle editMovieTitle = new EditMovieTitle();
                        editMovieTitle.doIt(connect);
                    });
                    Menu menu2p2 = new Menu("Редактировать рейтинг фильма", context2 -> {
                        EditMovieRating editMovieRating = new EditMovieRating();
                        editMovieRating.doIt(connect);
                    });
                    Menu menu2p3 = new Menu("Редактировать год премьеры", context2 -> {
                        EditMoviePremiereYear editMoviePremiereYear = new EditMoviePremiereYear();
                        editMoviePremiereYear.doIt(connect);
                    });
                    Menu menu2p4 = new Menu("Редактировать информацию о актерах фильмах", context2 -> {
                        EditMovieActor editMovieActor = new EditMovieActor();
                        editMovieActor.doIt(connect);
                    });

                    Menu menu2p5 = new Menu("Редактировать информацию о режисере фильма", context2 -> {
                        EditMovieDirector editMovieDirector = new EditMovieDirector();
                        editMovieDirector.doIt(connect);
                    });

                    Menu menu2p6 = new Menu("Редактировать длительность фильма", context2 -> {
                        EditMovieLength editMovieLength = new EditMovieLength();
                        editMovieLength.doIt(connect);
                    });
                    Menu menu2p7 = new Menu("Редактировать сюжет", context2 -> {
                        EditMoviePlot editMoviePlot = new EditMoviePlot();
                        editMoviePlot.doIt(connect);
                    });

                        menu2.addSubMenu(menu2p1);
                        menu2.addSubMenu(menu2p2);
                        menu2.addSubMenu(menu2p3);
                        menu2.addSubMenu(menu2p4);
                        menu2.addSubMenu(menu2p5);
                        menu2.addSubMenu(menu2p6);
                        menu2.addSubMenu(menu2p7);
                    do {
                        menu2.print();
                    } while (!menu2.action());
                });


                    menu1_1.addSubMenu(menu1p1);
                    menu1_1.addSubMenu(menu1p2);
                    menu1_1.addSubMenu(menu1p3);
                do {
                    menu1_1.print();
                } while (!menu1_1.action());


            });


            Menu menu2 = new Menu("Поиск данных в базе данных", context -> {
                Menu menu_2 = new Menu("Главное меню пункта 2");

                Menu menu2p1 = new Menu("Искать фильм по названию", context1 -> {
                    FindMovieByTitle findMovieByTitle = new FindMovieByTitle();
                    findMovieByTitle.doIt(connect);

                });
                Menu menu2p2 = new Menu("Искать фильм по рейтингу", context1 -> {
                    FindMovieByRating findMovieByRating = new FindMovieByRating();
                    findMovieByRating.doIt(connect);
                });
                Menu menu2p3 = new Menu("Искать фильм по году премьеры", context1 -> {
                    FindMovieByPremiereYear findMovieByPremiereYear = new FindMovieByPremiereYear();
                    findMovieByPremiereYear.doIt(connect);
                });
                Menu menu2p4 = new Menu("Искать фильм по фамилии актера", context1 -> {
                    FindMovieByNameActor findMovieByNameActor = new FindMovieByNameActor();
                    findMovieByNameActor.doIt(connect);
                });

                Menu menu2p5 = new Menu("Искать фильм по фамилии режисера", context1 -> {
                    FindMovieByNameDirector findMovieByNameDirector = new FindMovieByNameDirector();
                    findMovieByNameDirector.doIt(connect);
                });

                Menu menu2p6 = new Menu("Отобразить все фильмы в базе данных", context1 -> {
                    ResultSet rs = stmt.executeQuery("select * from show_all_movies_in_database");
                    ResultSetMetaData metaData = rs.getMetaData();
                    System.out.printf("%25s | %15s | %8s | %20s | %25s | %100s | %50s |", "Название фильма", "Дата премьеры", "Рейтинг", "Продолжительность",
                            "Режисер", "Актеры", "Жанр");
                    System.out.println();
                    while (rs.next()) {
                        System.out.printf("%25s | %15s | %8s | %20s | %25s | %100s | %50s | \n", rs.getString(1),
                                rs.getString(2), rs.getString(3), rs.getString(4)
                                , rs.getString(5), rs.getString(6), rs.getString(7));
                    }
                });


                    menu_2.addSubMenu(menu2p1);
                    menu_2.addSubMenu(menu2p2);
                    menu_2.addSubMenu(menu2p3);
                    menu_2.addSubMenu(menu2p4);
                    menu_2.addSubMenu(menu2p5);
                    menu_2.addSubMenu(menu2p6);
                do {
                    menu_2.print();
                } while (!menu_2.action());
            });


                menu.addSubMenu(menu1);
                menu.addSubMenu(menu2);
            do {
                menu.print();
            } while (!menu.action());

        } catch (SQLException | IOException throwables) {

        }
    }

    public void printMoviesInformation(ResultSet rs1) throws SQLException {
        ResultSet rs = rs1;
        while (rs.next()) {
            System.out.println("----------------------------------------------------------------------------------------");
            System.out.println("Название фильма : " + rs.getString("Title_inquiry"));
            System.out.println("Премьера : " + rs.getString("ReleasYear_inquiry"));
            System.out.println("Рейтинг : " + rs.getString("Rating_inquiry"));
            System.out.println("Продолжительность : " + rs.getString("MovieLength_inquiry"));
            System.out.println("Режисер : " + rs.getString("Director_inquiry"));
            System.out.println("Актеры : " + rs.getString("Actors_inquiry"));
            System.out.println("Жанр : " + rs.getString("Genre_inquiry"));
            System.out.println("Сюжет : " + rs.getString("Plot_inquiry"));
            System.out.println("-----------------------------------------------------------------------------------------");
        }
    }

    public boolean checkDirector(Connection connection, String directorName) throws SQLException {
        Connection conn = connection;
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("select LastName from directors;");
        String directorLastName = directorName;
        while (rs.next()) {
            if (directorLastName.equals(rs.getString("LastName"))) {
                return true;
            }
        }
        return false;
    }

    public boolean checkActor(Connection connection, String actorName) throws SQLException {
        String actorLastName = actorName;
        Connection conn = connection;
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("select LastName from actors;");
        while (rs.next()) {
            if (actorLastName.equals(rs.getString("LastName"))) {
                return true;
            }
        }
        return false;
    }

    public boolean checkMovies(Connection connection, String title) throws SQLException {
        String movieTitle = title;
        Connection conn = connection;
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("select title from moviess;");
        while (rs.next()) {
            if (movieTitle.equals(rs.getString("Title"))) {
                return true;
            }
        }
        return false;
    }

}
