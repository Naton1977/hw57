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
                    conn.setAutoCommit(true);
                } catch (Exception e) {
                    System.out.println("Ошибка добавления фильма в базу данных");
                    conn.rollback();
                    conn.setAutoCommit(true);
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
                        conn.setAutoCommit(true);
                    } catch (Exception exception) {
                        System.out.println("Произошла ошибка операции добавления в таблицу актеров и фильмов");
                        conn.rollback();
                        conn.setAutoCommit(true);
                    }
                }
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
                    conn.setAutoCommit(true);
                } catch (Exception exception) {
                    System.out.println("Произошла ошибка операции добавления актера в базу");
                    conn.rollback();
                    conn.setAutoCommit(true);
                }
                ResultSet rs = stmt.executeQuery("select ActorId from actors where lastname = '" + actorsLastName + "';");
                while (rs.next()) {
                    actorId = rs.getInt("ActorId");
                    actorMovieId.add(actorId);
                }
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
                connect.setAutoCommit(true);
            } catch (Exception e) {
                connect.rollback();
                System.out.println("Ошибка при добавлении данных режисера в базу данных");
                connect.setAutoCommit(true);
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
                            connect.setAutoCommit(true);
                        } catch (Exception e) {
                            System.out.println("Возикла ошибка при добавлени жанров в таблицу фильмов и жанров");
                            connect.rollback();
                            connect.setAutoCommit(true);
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
                    connect.setAutoCommit(true);
                } catch (Exception e) {
                    System.out.println("Возникла ошибка при бовавлении жанра в таблицу жанров");
                    connect.rollback();
                    connect.setAutoCommit(true);
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

class RemoveMovieFromDatabase {
    public void doIt(Connection connection) throws SQLException {
        int movieId = 0;
        Connection connect = connection;
        Statement stmt = connect.createStatement();
        Scanner scanner = new Scanner(System.in);
        System.out.println("Введите название фильма");
        String title = scanner.nextLine();
        Movies mov = new Movies();
        if (mov.checkMovies(connect, title)) {
            ResultSet rs = stmt.executeQuery("select MovieId from moviess where title = '" + title + "';");
            while (rs.next()) {
                movieId = rs.getInt("MovieId");
            }
            try {
                connect.setAutoCommit(false);
                stmt.executeUpdate("delete from moviess where movieId = '" + movieId + "';");
                System.out.println("Фильм успешно удален из базы данных");
                stmt.executeUpdate("delete from moviegenres where MovieId = '" + movieId + "';");
                stmt.executeUpdate("delete from movieactor where MovieId = '" + movieId + "';");
                connect.commit();
                connect.setAutoCommit(true);

            } catch (Exception e) {
                System.out.println("Произошла ошибка во время удаления фильма из базы данных");
                connect.rollback();
                connect.setAutoCommit(true);
            }

        } else {
            System.out.println("Такого фильма в базе данных нет !!!");
        }
    }
}

class EditMovieTitle {
    public void doIt(Connection connection) throws SQLException {
        String newTitle;
        int movieId = 0;
        Connection connect = connection;
        Statement stmt = connect.createStatement();
        Scanner scanner = new Scanner(System.in);
        System.out.println("Введите название фильма");
        String title = scanner.nextLine();
        Movies mov = new Movies();
        if (mov.checkMovies(connection, title)) {
            System.out.println("Введите новое название фильма");
            newTitle = scanner.nextLine();
            ResultSet rs = stmt.executeQuery("select MovieId from moviess where Title = '" + title + "';");
            while (rs.next()) {
                movieId = rs.getInt("MovieId");
            }
            try {
                connect.setAutoCommit(false);
                stmt.executeUpdate("update moviess set title = '" + newTitle + "' where MovieId = '" + movieId + "';");
                System.out.println("Название фильма успешно обновленно !");
                connect.commit();
                connect.setAutoCommit(true);
            } catch (Exception e) {
                System.out.println("Произошла ошибка при обновлении названия фильма в базе");
                connect.rollback();
                connect.setAutoCommit(true);
            }
        } else {
            System.out.println("Такого фильма в базе нет !!!");
        }

    }
}


class EditMovieRating {
    public void doIt(Connection connection) throws SQLException {
        String newRating;
        int movieId = 0;
        Connection connect = connection;
        Statement stmt = connect.createStatement();
        Scanner scanner = new Scanner(System.in);
        System.out.println("Введите название фильма");
        String title = scanner.nextLine();
        Movies mov = new Movies();
        if (mov.checkMovies(connection, title)) {
            System.out.println("Введите новое значение рейтинга");
            newRating = scanner.nextLine();
            ResultSet rs = stmt.executeQuery("select MovieId from moviess where Title = '" + title + "';");
            while (rs.next()) {
                movieId = rs.getInt("MovieId");
            }
            try {
                connect.setAutoCommit(false);
                stmt.executeUpdate("update moviess set ratind = '" + newRating + "' where MovieId = '" + movieId + "';");
                System.out.println("Рейтинг фильма успешно обновленно !");
                connect.commit();
                connect.setAutoCommit(true);
            } catch (Exception e) {
                System.out.println("Произошла ошибка при обновлении рейтинга фильма в базе");
                connect.rollback();
                connect.setAutoCommit(true);
            }
        } else {
            System.out.println("Такого фильма в базе нет !!!");
        }

    }
}


class EditMoviePremiereYear {
    public void doIt(Connection connection) throws SQLException {
        String newPremiereYear;
        int movieId = 0;
        Connection connect = connection;
        Statement stmt = connect.createStatement();
        Scanner scanner = new Scanner(System.in);
        System.out.println("Введите название фильма");
        String title = scanner.nextLine();
        Movies mov = new Movies();
        if (mov.checkMovies(connection, title)) {
            System.out.println("Введите новую дата премьеры");
            newPremiereYear = scanner.nextLine();
            ResultSet rs = stmt.executeQuery("select MovieId from moviess where Title = '" + title + "';");
            while (rs.next()) {
                movieId = rs.getInt("MovieId");
            }
            try {
                connect.setAutoCommit(false);
                stmt.executeUpdate("update moviess set releasYear = '" + newPremiereYear + "' where MovieId = '" + movieId + "';");
                System.out.println("Дата премьеры успешно измененна !!!");
                connect.commit();
                connect.setAutoCommit(true);
            } catch (Exception e) {
                System.out.println("Произошла ошибка при обновлении даты премьеры");
                connect.rollback();
                connect.setAutoCommit(true);
            }
        } else {
            System.out.println("Такого фильма в базе нет !!!");
        }

    }
}


class EditMovieLength {
    public void doIt(Connection connection) throws SQLException {
        String newMovieLength;
        int movieId = 0;
        Connection connect = connection;
        Statement stmt = connect.createStatement();
        Scanner scanner = new Scanner(System.in);
        System.out.println("Введите название фильма");
        String title = scanner.nextLine();
        Movies mov = new Movies();
        if (mov.checkMovies(connection, title)) {
            System.out.println("Введите новую длительность фильма");
            newMovieLength = scanner.nextLine();
            ResultSet rs = stmt.executeQuery("select MovieId from moviess where Title = '" + title + "';");
            while (rs.next()) {
                movieId = rs.getInt("MovieId");
            }
            try {
                connect.setAutoCommit(false);
                stmt.executeUpdate("update moviess set movieLength = '" + newMovieLength + "' where MovieId = '" + movieId + "';");
                System.out.println("Длительность фильма успешно измененна !!!");
                connect.commit();
                connect.setAutoCommit(true);
            } catch (Exception e) {
                System.out.println("Произошла ошибка при обновлении длительности фильма");
                connect.rollback();
                connect.setAutoCommit(true);
            }
        } else {
            System.out.println("Такого фильма в базе нет !!!");
        }

    }
}

class EditMoviePlot {
    public void doIt(Connection connection) throws SQLException {
        String newMoviePlot;
        int movieId = 0;
        Connection connect = connection;
        Statement stmt = connect.createStatement();
        Scanner scanner = new Scanner(System.in);
        System.out.println("Введите название фильма");
        String title = scanner.nextLine();
        Movies mov = new Movies();
        if (mov.checkMovies(connection, title)) {
            System.out.println("Введите новый сюжет фильма");
            newMoviePlot = scanner.nextLine();
            ResultSet rs = stmt.executeQuery("select MovieId from moviess where Title = '" + title + "';");
            while (rs.next()) {
                movieId = rs.getInt("MovieId");
            }
            try {
                connect.setAutoCommit(false);
                stmt.executeUpdate("update moviess set plot = '" + newMoviePlot + "' where MovieId = '" + movieId + "';");
                System.out.println("Сюжет фильма успешно изменнен !!!");
                connect.commit();
                connect.setAutoCommit(true);
            } catch (Exception e) {
                System.out.println("Произошла ошибка при обновлении сюжета фильма");
                connect.rollback();
                connect.setAutoCommit(true);
            }
        } else {
            System.out.println("Такого фильма в базе нет !!!");
        }

    }
}

class EditMovieActor {
    public void doIt(Connection connection) throws SQLException {
        String movieActor;
        int movieId = 0;
        int actorId = 0;
        Connection connect = connection;
        Statement stmt = connect.createStatement();
        Scanner scanner = new Scanner(System.in);
        System.out.println("Введите название фильма");
        String title = scanner.nextLine();
        Movies mov = new Movies();
        if (mov.checkMovies(connection, title)) {
            ResultSet rs1 = stmt.executeQuery("select MovieId from moviess where Title = '" + title + "';");
            while (rs1.next()) {
                movieId = rs1.getInt("MovieId");
            }

            do {
                ResultSet rs = stmt.executeQuery("select FirstName, LastName from actors\n" +
                        "join movieactor using(ActorId)\n" +
                        "join moviess using(MovieId)\n" +
                        "where MovieId = '" + movieId + "';");
                System.out.println("Актеры снимавшиеся в этом фильме фильма");
                while (rs.next()) {
                    System.out.println(rs.getString("FirstName") + " " + rs.getString("LastName"));
                }

                System.out.println("Введите фамилию актера которого вы хотите удалить из списка актеров");
                System.out.println("Exit - выход");
                movieActor = scanner.nextLine();
                if ("Exit".equals(movieActor)) {
                    break;
                }
                if (mov.checkActor(connection, movieActor)) {
                    ResultSet rs2 = stmt.executeQuery("select ActorId from actors where LastName = '" + movieActor + "';");
                    while (rs2.next()) {
                        actorId = rs2.getInt("ActorId");
                    }
                }
                try {
                    connect.setAutoCommit(false);
                    stmt.executeUpdate("delete from movieactor where ActorId='" + actorId + "';");
                    System.out.println("Актер успешно удален !!!");
                    connect.commit();
                    connect.setAutoCommit(true);
                    System.out.println("Хотите добавить актера к фильму ?");
                    System.out.println(" 1 - добавить");
                    System.out.println("Нажмите любую клавишу если не хотите добавлять актера");
                    String action = scanner.nextLine();
                    if ("1".equals(action)) {
                        AddMovieActors addMovieActors = new AddMovieActors();
                        addMovieActors.doIt(connection, movieId);
                    }
                } catch (Exception e) {
                    System.out.println("Произошла ошибка при удалении актера");
                    connect.rollback();
                    connect.setAutoCommit(true);
                }

            } while (true);
        } else {
            System.out.println("Такого фильма в базе нет !!!");
        }
    }
}

class EditMovieDirector {
    public void doIt(Connection connection) throws SQLException {
        String newMovieDirector;
        int movieId = 0;
        int newDirectorId = 0;
        Connection connect = connection;
        Statement stmt = connect.createStatement();
        Scanner scanner = new Scanner(System.in);
        System.out.println("Введите название фильма");
        String title = scanner.nextLine();
        Movies mov = new Movies();
        if (mov.checkMovies(connection, title)) {
            ResultSet rs1 = stmt.executeQuery("select MovieId from moviess where Title = '" + title + "';");
            while (rs1.next()) {
                movieId = rs1.getInt("MovieId");
            }

            ResultSet rs = stmt.executeQuery("select FirstName, LastName from directors\n" +
                    "join moviess using(DirectorId) where title = '" + title + "';");
            System.out.println("Режисер этого фильма");
            while (rs.next()) {
                System.out.println(rs.getString("FirstName") + " " + rs.getString("LastName"));
            }

            System.out.println("Введите фамилию нового режисера");
            newMovieDirector = scanner.nextLine();
            if (mov.checkDirector(connection, newMovieDirector)) {
                ResultSet rs2 = stmt.executeQuery("select DirectorId from directors where LastName = '" + newMovieDirector + "';");
                while (rs2.next()) {
                    newDirectorId = rs2.getInt("DirectorId");
                    try {
                        connect.setAutoCommit(false);
                        stmt.executeUpdate("update moviess set DirectorId = '" + newDirectorId + "' where MovieId = '" + movieId + "';");
                        System.out.println("Даные режисера успешно измененны !");
                        connect.commit();
                    } catch (Exception e) {
                        connect.rollback();
                        System.out.println("Произошла ошибка обновления режисера");
                    }
                }
            } else {
                System.out.println("Такого режисера в базе нет, его необходимо зарегистрировать");
                AddMovieDirector addMovieDirector = new AddMovieDirector();
                newDirectorId = addMovieDirector.doIt(connection);
                ResultSet rs2 = stmt.executeQuery("select DirectorId from directors where LastName = '" + newMovieDirector + "';");
                while (rs2.next()) {
                    newDirectorId = rs2.getInt("DirectorId");
                    try {
                        connect.setAutoCommit(false);
                        stmt.executeUpdate("update moviess set DirectorId = '" + newDirectorId + "' where MovieId = '" + movieId + "';");
                        System.out.println("Даные режисера успешно измененны !");
                        connect.commit();
                    } catch (Exception e) {
                        connect.rollback();
                        System.out.println("Произошла ошибка обновления режисера");
                    }
                }
            }
        } else {
            System.out.println("Такого фильма в базе нет !!!");
        }
    }
}

