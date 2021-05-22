package com.game.validator;

import com.game.entity.Player;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Objects;

@Component
public class PlayerUpdateValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return Player.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Player player = (Player) target;

        String name = player.getName();
        if (!Objects.isNull(name)) {
            if (name.length() > 12) {
                errors.reject("Length_error", "Name can not exceed 12 characters");
            }
        }

            String title = player.getTitle();
            if (!Objects.isNull(title)) {

                if (title.length() > 30) {
                    errors.reject("Length_error", "Title can not exceed 30 characters");
                }
            }

            Integer experience = player.getExperience();
            if (!Objects.isNull(experience)) {

                if (experience < 0 || experience > 10_000_000) {
                    errors.reject("Experience_error",
                            "Experience can not be negative or exceed 10.000.000");
                }
            }

                Date birthday = player.getBirthday();
                if (!Objects.isNull(birthday)) {

                    if (birthday.before(new GregorianCalendar(2000, 0, 1).getTime())
                            || player.getBirthday().after(new GregorianCalendar(3000, 0, 1).getTime())) {
                        errors.reject("Birthday_error", "Birthday is not valid");
                    }
                }


    }
}


