package com.theironyard;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Created by joshuakeough on 10/5/16.
 */

public interface GameRepository extends CrudRepository<Game, Integer>{
    List<Game> findByUser(User user);
    List<Game> findByGenre(String genre);
    List<Game> findByReleaseYear(Integer gameYear);
    List<Game> findByReleaseYearLessThan(Integer lessThanGameYear);
    List<Game> findByReleaseYearGreaterThan(Integer greaterThanGameYear);


    Game findFirstByGenre(String genre);
    int countByGenre(String genre);

    @Query("SELECT g from Game g WHERE g.name LIKE ?1%")
    List<Game> findByNameStartsWith(String name);
}
