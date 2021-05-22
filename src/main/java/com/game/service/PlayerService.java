package com.game.service;

import com.game.entity.Player;
import com.game.entity.Profession;
import com.game.entity.Race;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

public interface PlayerService {

    public List<Player> findAllByParameters(String name, String title, Race race,
                                            Profession profession, Long after, Long before,
                                            Boolean banned, Integer minExperience,
                                            Integer maxExperience, Integer minLevel, Integer maxLevel,
                                            Pageable pageable);

    public Integer playerCountByParameters(String name, String title, Race race,
                                           Profession profession, Long after, Long before,
                                           Boolean banned, Integer minExperience,
                                           Integer maxExperience, Integer minLevel, Integer maxLevel);
    public Player savePlayer(Player player);
    public Optional<Player> getPlayer(long id);
    public Boolean isPlayerExist(long id);
    public Player updatePlayer(long id, Player updPlayer);
    public void deletePlayer(long id);



    }
