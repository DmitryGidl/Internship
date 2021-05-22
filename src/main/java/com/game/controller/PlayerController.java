package com.game.controller;

import com.game.entity.Player;
import com.game.entity.Profession;
import com.game.entity.Race;
import com.game.exception.IncorrectDataException;
import com.game.exception.PlayerErrorResponse;
import com.game.service.PlayerService;
import com.game.validator.PlayerCreateValidator;
import com.game.validator.PlayerUpdateValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/rest")
public class PlayerController {
    @Autowired
    PlayerService playerService;
    @Autowired
    PlayerCreateValidator playerValidator;
    @Autowired
    PlayerUpdateValidator playerUpdateValidator;

    @GetMapping("/players")
    public List<Player> getPlayers(@RequestParam(value = "name", required = false) String name,
                                   @RequestParam(value = "title", required = false) String  title,
                                   @RequestParam(value = "race", required = false) Race race,
                                   @RequestParam(value = "profession", required = false) Profession profession,
                                   @RequestParam(value = "after", required = false) Long after,
                                   @RequestParam(value = "before", required = false) Long before,
                                   @RequestParam(value = "banned", required = false) Boolean banned,
                                   @RequestParam(value = "minExperience", required = false) Integer minExperience,
                                   @RequestParam(value = "maxExperience", required = false) Integer maxExperience,
                                   @RequestParam(value = "minLevel", required = false) Integer minLevel,
                                   @RequestParam(value = "maxLevel", required = false) Integer maxLevel,
                                   @RequestParam(value = "order", required = false) PlayerOrder order,
                                   @RequestParam(value = "pageNumber", required = false) Integer pageNumber,
                                   @RequestParam(value = "pageSize", required = false) Integer pageSize) {
        if(pageNumber == null) pageNumber = 0;
        if(pageSize == null) pageSize = 3;
        if(order == null) order = PlayerOrder.ID;
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(order.getFieldName()));

        return playerService.findAllByParameters(name, title, race, profession, after, before,
                banned, minExperience, maxExperience, minLevel, maxLevel, pageable);
    }

    @GetMapping("/players/count")
    public Integer getPlayersCount(@RequestParam(value = "name", required = false) String name,
                                   @RequestParam(value = "title", required = false) String  title,
                                   @RequestParam(value = "race", required = false) Race race,
                                   @RequestParam(value = "profession", required = false) Profession profession,
                                   @RequestParam(value = "after", required = false) Long after,
                                   @RequestParam(value = "before", required = false) Long before,
                                   @RequestParam(value = "banned", required = false) Boolean banned,
                                   @RequestParam(value = "minExperience", required = false) Integer minExperience,
                                   @RequestParam(value = "maxExperience", required = false) Integer maxExperience,
                                   @RequestParam(value = "minLevel", required = false) Integer minLevel,
                                   @RequestParam(value = "maxLevel", required = false) Integer maxLevel) {

        return playerService.playerCountByParameters(name, title, race, profession, after, before,
                banned, minExperience, maxExperience, minLevel, maxLevel);
    }

    @PostMapping("/players")
    public Player createPlayer( @Validated @RequestBody Player player, BindingResult bindingResult) {

        playerValidator.validate(player, bindingResult);
        if(bindingResult.hasErrors()) throw new IncorrectDataException("Your data input is Incorrect, "
                + bindingResult.getGlobalError().getDefaultMessage());
        return  playerService.savePlayer(player);
    }


    @GetMapping("/players/{id}")
    public ResponseEntity<Player> getPlayer(@PathVariable long id) {
        if(id < 1) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        if(!playerService.isPlayerExist(id)) return new ResponseEntity<>( HttpStatus.NOT_FOUND);

        Optional<Player> optional = playerService.getPlayer(id);
        Player player = optional.get();
        return new ResponseEntity<>(player, HttpStatus.OK);
    }

    @PostMapping("/players/{id}")
    public ResponseEntity<Player> updatePlayer(@PathVariable long id, @RequestBody @Validated Player updPlayer, BindingResult bindingResult) {
        if(updPlayer == null) return new ResponseEntity<>(HttpStatus.OK);
        if(id < 1) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        if(!playerService.isPlayerExist(id)) return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        playerUpdateValidator.validate(updPlayer, bindingResult);
        if(bindingResult.hasErrors()) throw new IncorrectDataException("Your data input is Incorrect, "
                + bindingResult.getGlobalError().getDefaultMessage());

        Player player = playerService.updatePlayer(id, updPlayer);
        return new ResponseEntity<>(player, HttpStatus.OK);
    }

    @DeleteMapping("/players/{id}")
    public ResponseEntity deletePlayer(@PathVariable long id) {
        if(id<1) return new ResponseEntity( HttpStatus.BAD_REQUEST);
        if(!playerService.isPlayerExist(id)) return new ResponseEntity<>( HttpStatus.NOT_FOUND);
        playerService.deletePlayer(id);
        return new ResponseEntity<>( HttpStatus.OK);

    }





    @ExceptionHandler
    public ResponseEntity<PlayerErrorResponse> handleException(IncorrectDataException e) {
        PlayerErrorResponse playerErrorResponse = new PlayerErrorResponse();
        playerErrorResponse.setStatus(HttpStatus.BAD_REQUEST.value());
        playerErrorResponse.setMessage(e.getMessage());
        playerErrorResponse.setTimeStamp(System.currentTimeMillis());
        return new ResponseEntity<>(playerErrorResponse, HttpStatus.BAD_REQUEST);

    }





}


