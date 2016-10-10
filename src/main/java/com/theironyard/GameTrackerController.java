package com.theironyard;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by joshuakeough on 10/5/16.
 */
@Controller
public class GameTrackerController {
    @Autowired
    GameRepository games;
    @Autowired
    UserRepository users;

    @PostConstruct
    public void init() {
        if (users.count() == 0) {
            User user = new User();
            user.setName("Josh");
            user.setPassword("123");
            users.save(user);
        }
    }

    @RequestMapping(path = "/login", method = RequestMethod.POST)
    public String login(HttpSession session, String userName, String password) throws Exception {
        User user = users.findFirstByName(userName);
        if (user == null) {
            user = new User(userName, password);users.save(user);
        }else if(! password.equals(user.getPassword())){
            throw new Exception("Incorrect password");
        }
        session.setAttribute("userName", userName);
        return "redirect:/";
    }

    @RequestMapping(path = "/logout", method = RequestMethod.POST)
    public String logout(HttpSession session){
        session.invalidate();
        return "redirect:/";
    }



    @RequestMapping(path = "/", method = RequestMethod.GET)
    public String home(HttpSession session, Model model, String genre, String search, Integer gameYear, Integer less, Integer more) {
        String userName = (String)session.getAttribute("userName");
        User user = users.findFirstByName(userName);
        if(user!=null){
            model.addAttribute("user", user);
            model.addAttribute("username", userName);
        }

        List<Game> gameList;
        if(genre != null){
           gameList = (ArrayList)games.findByGenre(genre);
        }else if(less != null){
            gameList = (ArrayList)games.findByReleaseYearLessThan(gameYear);
        }else if(more !=null) {
            gameList = (ArrayList)games.findByReleaseYearGreaterThan(gameYear);
        }else if(search!= null) {
            gameList = (ArrayList)games.findByNameStartsWith(search);
        }else if(gameYear != null) {
            gameList = (ArrayList)games.findByReleaseYear(gameYear);
        }else if(user != null) {
            gameList = (ArrayList)games.findByUser(user);
        }
        else {
           gameList = (ArrayList)games.findAll();
        }

        model.addAttribute("games", gameList);

        return "home";
    }

    @RequestMapping(path = "/add-game", method = RequestMethod.POST)
    public String addGame(HttpSession session, String gameName, String gameGenre, String gamePlatform, int gameYear) {
        String userName = (String)session.getAttribute("userName");
        User user = users.findFirstByName(userName);
        Game game = new Game( gameName,  gamePlatform,  gameGenre,  gameYear, user);
        games.save(game);
        return "redirect:/";
    }






}
