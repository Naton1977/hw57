package org.example;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Methods {
}

class AddMovie {

    public void doIt(Connection connect) throws SQLException {
        AddMovieDirector addMovieDirector = new AddMovieDirector();
        String action = null;
        int movieId = 0;
        int directorId = 0;
        Scanner scanner = new Scanner(System.in);
        Connection conn = connect;
        Statement stmt = conn.createStatement();
        Movies mov = new Movies();
        System.out.println("Введите название фильма");
        String title = scanner.nextLine();
        if (mov.checkMovies(connect, title)) {
            System.out.println("Такой фильм уже есть в базе данных !!!");
            System.out.println("Хотите добавить актеров ?");
            System.out.println("Введите - 1");
            System.out.println("Хотите добавить жанры");
            System.out.println("Введите - 2");
            System.out.println("Для выхода нажмите любую клавишу");
            action = scanner.nextLine();
        } else {
            System.out.println("Введите год премьеры");
            String releasYear = scanner.nextLine();
            System.out.println("Введите рейтинг");
            String rating = scanner.nextLine();
            System.out.println("Введите длительность фильма");
            String movieLength = scanner.nextLine();
            System.out.println("Введите сюжет фильма");
            String plot = scanner.nextLine();
            directorId = addMovieDirector.doIt(connect);
            if (directorId != 0) {
                conn.setAutoCommit(false);
                try {
                    stmt.executeUpdate("insert into moviess (directorid, title,releasyear,ratind, plot, movielength)" +
                            " value('" + directorId + "', '" + title + "', '" + releasYear + "', '" + rating + "', '" + plot + "', '" + movieLength + "') ");
                    conn.commit();
                } catch (Exception e) {
                    System.out.println("Ошибка добавления фильма в базу данных");
                    conn.rollback();
                }
                ResultSet rs1 = stmt.executeQuery("select MovieId from moviess where Title = '" + title + "' ;");
                while (rs1.next()) {
                    movieId = rs1.getInt("MovieId");
                }
                AddMovieActors addMovieActors = new AddMovieActors();
                addMovieActors.doIt(connect, movieId);
                AddMovieGenre addMovieGenre = new AddMovieGenre();
                addMovieGenre.doIt(connect, movieId);
            }
        }

        if ("1".equals(action)) {
            ResultSet rs1 = stmt.executeQuery("select MovieId from moviess where Title = '" + title + "' ;");
            while (rs1.next()) {
                movieId = rs1.getInt("MovieId");
            }
            AddMovieActors addMovieActors2 = new AddMovieActors();
            addMovieActors2.doIt(connect, movieId);
        }
        if ("2".equals(action)) {
            ResultSet rs1 = stmt.executeQuery("select MovieId from moviess where Title = '" + title + "' ;");
            while (rs1.next()) {
                movieId = rs1.getInt("MovieId");
            }
            AddMovieGenre addMovieGenre = new AddMovieGenre();
            addMovieGenre.doIt(connect, movieId);
        }
    }
}

class AddMovieActors {

    public void doIt(Connection connect, int MovieId) throws SQLException {
        List<Integer> actorMovieId = new ArrayList<>();
        int actorId = 0;
        String actorsLastName;
        Movies mov = new Movies();
        Scanner scanner = new Scanner(System.in);
        Connection conn = connect;
        Statement stmt = conn.createStatement();
        System.out.println("Добавить актеров к фильму");
        do {
            System.out.println("Введите фамилию актера");
            System.out.println("Exit - выход");
            actorsLastName = scanner.nextLine();
            if ("Exit".equals(actorsLastName)) {
                conn.setAutoCommit(false);
                for (int i = 0; i < actorMovieId.size(); i++) {
                    int id = actorMovieId.get(i);
                    try {
                        stmt.executeUpdate("insert into movieactor(MovieId, ActorId) values ('" + MovieId + "', '" + id + "');");
                        conn.commit();
                    } catch (Exception exception) {
                        System.out.println("Произошла ошибка операции добавления в таблицу актеров и фильмов");
                        conn.rollback();
                    }
                }
                conn.setAutoCommit(true);
                break;
            }
            if (mov.checkActor(connect, actorsLastName)) {
                ResultSet rs = stmt.executeQuery("select ActorId from actors where lastname = '" + actorsLastName + "';");
                while (rs.next()) {
                    actorId = rs.getInt("ActorId");
                    actorMovieId.add(actorId);
                }
            } else {
                System.out.println("Введите имя актера");
                String actorsFirstName = scanner.nextLine();
                System.out.println("Введите национальность актера");
                String actorNationality = scanner.nextLine();
                System.out.println("Введите дату рождения актера");
                String actorBirth = scanner.nextLine();
                conn.setAutoCommit(false);
                try {
                    stmt.executeUpdate("insert into actors(FirstName, LastName, Nationality, Birth) values ('" + actorsFirstName + "', '" + actorsLastName + "', '" + actorNationality + "', '" + actorBirth + "');");
                    conn.commit();
                } catch (Exception exception) {
                    System.out.println("Произошла ошибка операции добавления актера в базу");
                    conn.rollback();
                }
                ResultSet rs = stmt.executeQuery("select ActorId from actors where lastname = '" + actorsLastName + "';");
                while (rs.next()) {
                    actorId = rs.getInt("ActorId");
                    actorMovieId.add(actorId);
                }
                conn.setAutoCommit(true);
            }

        } while (true);
    }
}

class AddMovieDirector {
    public int doIt(Connection connection) throws SQLException {
        int directorId = 0;
        boolean returnId = false;
        String directorNationality;
        String directorFirstName;
        String directorBirth;
        Movies mov = new Movies();
        Connection connect = connection;
        Statement stmt = connect.createStatement();
        Scanner scanner = new Scanner(System.in);
        System.out.println("Введите фамилию режисера");
        String director = scanner.nextLine();
        if (mov.checkDirector(connection, director)) {
            ResultSet rs1 = stmt.executeQuery("select directorid  from directors where lastname = '" + director + "';");
            while (rs1.next()) {
                directorId = rs1.getInt("directorid");
                return directorId;
            }

        } else {
            System.out.println("Введите имя режисера");
            directorFirstName = scanner.nextLine();
            System.out.println("Введите национальность режисера");
            directorNationality = scanner.nextLine();
            System.out.println("Введите дату рождения режисера в формате (год-месяц-день)");
            directorBirth = scanner.nextLine();
            connect.setAutoCommit(false);
            try {
                stmt.executeUpdate("insert into directors(FirstName, LastName, Nationality, Birth ) values('" + directorFirstName + "'," +
                        "'" + director + "', '" + directorNationality + "', '" + directorBirth + "');");
                connect.commit();
                returnId = true;
            } catch (Exception e) {
                connect.rollback();
                System.out.println("Ошибка при добавлении данных режисера в базу данных");
            }

        }
        if (returnId) {
            ResultSet rs1 = stmt.executeQuery("select directorid  from directors where lastname = '" + director + "';");
            while (rs1.next()) {
                directorId = rs1.getInt("directorid");
                return directorId;
            }
        }

        return 0;
    }
}

class AddMovieGenre {

    public void doIt(Connection connection, int MovieID) throws SQLException {
        List<Integer> genresMovieId = new ArrayList<>();
        String genre = null;
        int genreId = 0;
        Connection connect = connection;
        Statement stmt = connect.createStatement();
        Scanner scanner = new Scanner(System.in);
        do {
            System.out.println("Введите название жанра");
            System.out.println("Exit - выход");
            String movieGenre = scanner.nextLine();
            if (movieGenre.equals("Exit")) {
                if (genresMovieId.size() > 0) {
                    for (Integer integer : genresMovieId) {
                        genreId = integer;
                        connect.setAutoCommit(false);
                        try {
                            stmt.executeUpdate("insert into moviegenres(MovieId,GenreId) values('" + MovieID + "', '" + genreId + "');");
                            connect.commit();
                        } catch (Exception e) {
                            System.out.println("Возикла ошибка при добавлени жанров в таблицу фильмов и жанров");
                            connect.rollback();
                        }
                    }
                }
                break;
            }
            ResultSet rs = stmt.executeQuery("select genreName from genres where genreName = '" + movieGenre + "' ;");
            while (rs.next()) {
                genre = rs.getString("genreName");
            }
            if (genre == null) {
                connect.setAutoCommit(false);
                try {
                    stmt.executeUpdate("insert into genres(genreName) values('" + movieGenre + "');");
                    connect.commit();
                } catch (Exception e) {
                    System.out.println("Возникла ошибка при бовавлении жанра в таблицу жанров");
                    connect.rollback();
                }

                ResultSet rs1 = stmt.executeQuery("select genreId from genres where genreName = '" + movieGenre + "' ;");
                while (rs1.next()) {
                    genreId = rs1.getInt("genreId");
                    if (genreId != 0) {
                        genresMovieId.add(genreId);
                    }
                }

            } else {
                ResultSet rs1 = stmt.executeQuery("select genreId from genres where genreName = '" + movieGenre + "' ;");
                while (rs1.next()) {
                    genreId = rs1.getInt("genreId");
                    if (genreId != 0) {
                        genresMovieId.add(genreId);
                    }
                }
            }
        } while (true);
    }
}

class FindMovieByNameDirector {
    public void doIt(Connection connection) throws SQLException {
        Connection connect = connection;
        Statement stmt = connect.createStatement();
        Scanner scanner = new Scanner(System.in);
        System.out.println("Введите фамилию режисера");
        String director = scanner.nextLine();
        ResultSet rs = stmt.executeQuery("select distinct moviess.Title as 'Title_inquiry', moviess.ReleasYear as 'ReleasYear_inquiry',\n" +
                " moviess.Ratind as 'Rating_inquiry', moviess.MovieLength as 'MovieLength_inquiry',\n" +
                " concat(directors.firstName, ' ', directors.lastname) as 'Director_inquiry',\n" +
                "group_concat( distinct concat(actors.firstname, ' ', actors.lastname) order by actors.LastName asc separator ' ,') as'Actors_inquiry',\n" +
                " group_concat(distinct genres.genreName order by genres.genreName asc separator ' ,') as 'Genre_inquiry', moviess.Plot as 'Plot_inquiry' from moviess\n" +
                "join moviegenres on moviess.MovieId = moviegenres.MovieId\n" +
                "join genres on moviegenres.genreId = genres.genreId\n" +
                "join directors on moviess.DirectorId = directors.DirectorId\n" +
                "join movieactor on moviess.MovieId = movieactor.MovieId\n" +
                "join actors on movieactor.ActorId = actors.ActorId\n" +
                "where directors.lastname = '" + director + "'\n" +
                "group by moviess.Title\n" +
                "order by moviess.Ratind desc;");
        Movies mov = new Movies();
        mov.printMoviesInformation(rs);
    }
}

class FindMovieByNameActor {
    public void doIt(Connection connection) throws SQLException {
        Connection connect = connection;
        Statement stmt = connect.createStatement();
        Scanner scanner = new Scanner(System.in);
        System.out.println("Введите фамилию актера для поиска");
        String lastName = scanner.nextLine();
        ResultSet rs = stmt.executeQuery("select distinct moviess.Title as 'Title_inquiry', moviess.ReleasYear as 'ReleasYear_inquiry',\n" +
                " moviess.Ratind as 'Rating_inquiry', moviess.MovieLength as 'MovieLength_inquiry',\n" +
                " concat(directors.firstName, ' ', directors.lastname) as 'Director_inquiry',\n" +
                "group_concat( distinct concat(actors.firstname, ' ', actors.lastname) order by actors.LastName asc separator ' ,') as'Actors_inquiry',\n" +
                " group_concat(distinct genres.genreName order by genres.genreName asc separator ' ,') as 'Genre_inquiry', moviess.Plot as 'Plot_inquiry' from moviess\n" +
                "join moviegenres on moviess.MovieId = moviegenres.MovieId\n" +
                "join genres on moviegenres.genreId = genres.genreId\n" +
                "join directors on moviess.DirectorId = directors.DirectorId\n" +
                "join movieactor on moviess.MovieId = movieactor.MovieId\n" +
                "join actors on movieactor.ActorId = actors.ActorId\n" +
                "where actors.lastname = '" + lastName + "'\n" +
                "group by moviess.Title\n" +
                "order by moviess.Ratind desc;");
        Movies mov = new Movies();
        mov.printMoviesInformation(rs);
    }
}

class FindMovieByPremiereYear {
    public void doIt(Connection connection) throws SQLException {
        Connection connect = connection;
        Statement stmt = connect.createStatement();
        Scanner scanner = new Scanner(System.in);
        System.out.println("Введите год премьеры фильма для поиска");
        String year = scanner.nextLine();
        ResultSet rs = stmt.executeQuery("select distinct moviess.Title as 'Title_inquiry', moviess.ReleasYear as 'ReleasYear_inquiry',\n" +
                " moviess.Ratind as 'Rating_inquiry', moviess.MovieLength as 'MovieLength_inquiry',\n" +
                " concat(directors.firstName, ' ', directors.lastname) as 'Director_inquiry',\n" +
                "group_concat( distinct concat(actors.firstname, ' ', actors.lastname) order by actors.LastName asc separator ' ,') as'Actors_inquiry',\n" +
                " group_concat(distinct genres.genreName order by genres.genreName asc separator ' ,') as 'Genre_inquiry', moviess.Plot as 'Plot_inquiry' from moviess\n" +
                "join moviegenres on moviess.MovieId = moviegenres.MovieId\n" +
                "join genres on moviegenres.genreId = genres.genreId\n" +
                "join directors on moviess.DirectorId = directors.DirectorId\n" +
                "join movieactor on moviess.MovieId = movieactor.MovieId\n" +
                "join actors on movieactor.ActorId = actors.ActorId\n" +
                "where year(moviess.ReleasYear) = '" + year + "'\n" +
                "group by moviess.Title\n" +
                "order by moviess.Ratind desc;");
        Movies mov = new Movies();
        mov.printMoviesInformation(rs);
    }
}

class FindMovieByRating {
    public void doIt(Connection connection) throws SQLException {
        Connection connect = connection;
        Statement stmt = connect.createStatement();
        Scanner scanner = new Scanner(System.in);
        System.out.println("Введите рейтинг от которого нужно начинать поиск фильмов");
        String rating = scanner.nextLine();
        ResultSet rs = stmt.executeQuery("select distinct moviess.Title as 'Title_inquiry', moviess.ReleasYear as 'ReleasYear_inquiry',\n" +
                " moviess.Ratind as 'Rating_inquiry', moviess.MovieLength as 'MovieLength_inquiry',\n" +
                " concat(directors.firstName, ' ', directors.lastname) as 'Director_inquiry',\n" +
                "group_concat( distinct concat(actors.firstname, ' ', actors.lastname) order by actors.LastName asc separator ' ,') as'Actors_inquiry',\n" +
                " group_concat(distinct genres.genreName order by genres.genreName asc separator ' ,') as 'Genre_inquiry', moviess.Plot as 'Plot_inquiry' from moviess\n" +
                "join moviegenres on moviess.MovieId = moviegenres.MovieId\n" +
                "join genres on moviegenres.genreId = genres.genreId\n" +
                "join directors on moviess.DirectorId = directors.DirectorId\n" +
                "join movieactor on moviess.MovieId = movieactor.MovieId\n" +
                "join actors on movieactor.ActorId = actors.ActorId\n" +
                "where  moviess.Ratind >= '" + rating + "'\n" +
                "group by moviess.Title\n" +
                "order by moviess.Ratind desc;");

        Movies mov = new Movies();
        mov.printMoviesInformation(rs);
    }
}

class FindMovieByTitle {
    public void doIt(Connection connection) throws SQLException {
        Connection connect = connection;
        Statement stmt = connect.createStatement();
        Scanner scanner = new Scanner(System.in);
        System.out.println("Введите название фильма");
        String title = scanner.nextLine();
        ResultSet rs = stmt.executeQuery("select distinct moviess.Title as 'Title_inquiry', moviess.ReleasYear as 'ReleasYear_inquiry',\n" +
                " moviess.Ratind as 'Rating_inquiry', moviess.MovieLength as 'MovieLength_inquiry',\n" +
                " concat(directors.firstName, ' ', directors.lastname) as 'Director_inquiry',\n" +
                "group_concat( distinct concat(actors.firstname, ' ', actors.lastname) order by actors.LastName asc separator ' ,') as'Actors_inquiry',\n" +
                " group_concat(distinct genres.genreName order by genres.genreName asc separator ' ,') as 'Genre_inquiry', moviess.Plot as 'Plot_inquiry' from moviess\n" +
                "join moviegenres on moviess.MovieId = moviegenres.MovieId\n" +
                "join genres on moviegenres.genreId = genres.genreId\n" +
                "join directors on moviess.DirectorId = directors.DirectorId\n" +
                "join movieactor on moviess.MovieId = movieactor.MovieId\n" +
                "join actors on movieactor.ActorId = actors.ActorId\n" +
                "where  moviess.Title = '" + title + "'\n" +
                "group by moviess.Title;");

        Movies mov = new Movies();
        mov.printMoviesInformation(rs);
    }
}