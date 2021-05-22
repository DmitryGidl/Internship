package com.game.specification;

import com.game.entity.Player;
import com.game.entity.Profession;
import com.game.entity.Race;
import org.springframework.data.jpa.domain.Specification;

import java.util.Date;


public class PlayerSpecifications {

    public static Specification<Player> filterByName(String name) {
return ((root, query, criteriaBuilder) ->
        name == null ? null :  criteriaBuilder.like(root.get("name"), "%" + name + "%"));
    }
    public static Specification<Player> filterByTitle(String title) {
        return ((root, query, criteriaBuilder) ->
                title == null ? null :  criteriaBuilder.like(root.get("title"), "%" + title + "%"));
    }
    public static Specification<Player> filterByRace(Race race) {
        return ((root, query, criteriaBuilder) ->
                race == null ? null :  criteriaBuilder.equal(root.get("race"), race));
    }
    public static Specification<Player> filterByProfession(Profession profession) {
        return ((root, query, criteriaBuilder) ->
                profession == null ? null :  criteriaBuilder.equal(root.get("profession"), profession));
    }
    public static Specification<Player> filterByBirthday(Long after, Long before ) {

        return ((root, query, criteriaBuilder) -> {
            if(before == null && after == null) return null;

            if(after != null && before == null) {
                Date afterDate = new Date(after);
                return criteriaBuilder.greaterThanOrEqualTo(root.get("birthday"), afterDate);
            }

            if(after == null) {
                Date beforeDate = new Date(before);
                return criteriaBuilder.lessThanOrEqualTo(root.get("birthday"), beforeDate);
            }
            Date afterDate = new Date(after);
            Date beforeDate = new Date(before);

            return criteriaBuilder.between(root.get("birthday"), afterDate,beforeDate);

        });
    }
            public static Specification<Player> filterByBanned(Boolean banned) {
                return ((root, query, criteriaBuilder) -> {
                    if(banned == null) return null;
                    if(banned) return criteriaBuilder.isTrue(root.get("banned"));
                    return criteriaBuilder.isFalse(root.get("banned"));

                });
            }

            public static Specification<Player> filterByExperience(Integer minExperience, Integer maxExperience) {
        return (root, query, criteriaBuilder) -> {
            if(minExperience == null && maxExperience == null) return null;
            if(minExperience !=null && maxExperience == null) return criteriaBuilder.
                    greaterThanOrEqualTo(root.get("experience"), minExperience);

            if(minExperience == null) return criteriaBuilder.lessThanOrEqualTo(root.get("experience"), maxExperience);
            return criteriaBuilder.between(root.get("experience"),minExperience,maxExperience);
        };
        }

        public static Specification<Player> filterByLevel(Integer minLevel, Integer maxLevel) {
        return ((root, query, criteriaBuilder) -> {
            if(minLevel == null && maxLevel == null) return null;
            if(minLevel != null && maxLevel == null) return criteriaBuilder.greaterThanOrEqualTo(root.get("level"),minLevel);
            if(minLevel == null) return criteriaBuilder.lessThanOrEqualTo(root.get("level"), maxLevel);
            return criteriaBuilder.between(root.get("level"), minLevel, maxLevel);
        });
        }



    }
