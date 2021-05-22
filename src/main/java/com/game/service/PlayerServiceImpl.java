package com.game.service;

import com.game.entity.Profession;
import com.game.entity.Race;
import com.game.repository.PlayerRepository;
import com.game.entity.Player;
import com.game.specification.PlayerSpecifications;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class PlayerServiceImpl implements PlayerService {

    @Autowired
    PlayerRepository playerRepository;


    public List<Player> findAllByParameters(String name, String title, Race race,
                                            Profession profession, Long after, Long before,
                                            Boolean banned, Integer minExperience,
                                            Integer maxExperience, Integer minLevel, Integer maxLevel, Pageable pageable) {

        return playerRepository.findAll(Specification.where(PlayerSpecifications.filterByName(name)
                .and(PlayerSpecifications.filterByTitle(title)
                        .and(PlayerSpecifications.filterByRace(race)
                                .and(PlayerSpecifications.filterByProfession(profession)
                                        .and(PlayerSpecifications.filterByBirthday(after, before)
                                                .and(PlayerSpecifications.filterByBanned(banned)
                                                        .and(PlayerSpecifications.filterByExperience(minExperience, maxExperience)
                                                                .and(PlayerSpecifications.filterByLevel(minLevel, maxLevel))))))))), pageable).getContent();
    }

    public Integer playerCountByParameters(String name, String title, Race race,
                                           Profession profession, Long after, Long before,
                                           Boolean banned, Integer minExperience,
                                           Integer maxExperience, Integer minLevel, Integer maxLevel) {

        return Math.toIntExact(playerRepository.count(Specification.where(PlayerSpecifications.filterByName(name)
                .and(PlayerSpecifications.filterByTitle(title)
                        .and(PlayerSpecifications.filterByRace(race)
                                .and(PlayerSpecifications.filterByProfession(profession)
                                        .and(PlayerSpecifications.filterByBirthday(after, before)
                                                .and(PlayerSpecifications.filterByBanned(banned)
                                                        .and(PlayerSpecifications.filterByExperience(minExperience, maxExperience)
                                                                .and(PlayerSpecifications.filterByLevel(minLevel, maxLevel)))))))))));
    }

    public Player savePlayer(Player player) {
        player.setId(0);
        if(Objects.isNull(player.getBanned())) player.setBanned(false);
        int experience = player.getExperience();
        player.setLevel(calculateCurrentLevel(experience));
        player.setUntilNextLevel(expUntilNextLevel(experience));
        return playerRepository.save(player);
    }

    public Optional<Player> getPlayer(long id) {
        return playerRepository.findById(id);
    }

    public Integer calculateCurrentLevel(int exp) {
        return Math.toIntExact((long) ((Math.sqrt(2500+200*exp)-50)/100.0));

    }

    public Integer expUntilNextLevel(int exp){
        int currentLevel = calculateCurrentLevel(exp);
        return 50*(currentLevel+1)*(currentLevel+2)-exp;
    }

    public Player updatePlayer(long id, Player updPlayer) {

        Player player = getPlayer(id).get();

        String name = updPlayer.getName();
        String title = updPlayer.getTitle();
        Race race = updPlayer.getRace();
        Profession profession = updPlayer.getProfession();
        Date birthday = updPlayer.getBirthday();
        Boolean banned = updPlayer.getBanned();
        Integer experience = updPlayer.getExperience();


        if(!Objects.isNull(name)) player.setName(name);
        if(!Objects.isNull(title)) player.setTitle(title);
        if(!Objects.isNull(race)) player.setRace(race);
        if(!Objects.isNull(profession)) player.setProfession(profession);
        if(!Objects.isNull(birthday)) player.setBirthday(birthday);
        if(!Objects.isNull(banned)) player.setBanned(banned);
        if(!Objects.isNull(experience)) {
            player.setExperience(experience);
            player.setLevel(calculateCurrentLevel(experience));
            player.setUntilNextLevel(expUntilNextLevel(experience));
        }

        return   playerRepository.save(player);
    }
    public void deletePlayer(long id) {
        playerRepository.deleteById(id);
    }

    public Boolean isPlayerExist(long id) {
        return   playerRepository.existsById(id);
    }











}
