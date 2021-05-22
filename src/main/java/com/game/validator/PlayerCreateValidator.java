package com.game.validator;

import com.game.entity.Player;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.GregorianCalendar;
import java.util.Objects;

@Component
public class PlayerCreateValidator implements Validator {
    String paramMissing = "Required parameters are missing";
    @Override
    public boolean supports(Class<?> clazz) {
        return Player.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
       Player player = (Player) target;

       if( Objects.isNull(player.getName())
               || Objects.isNull(player.getTitle())
               || Objects.isNull(player.getRace())
               || Objects.isNull(player.getProfession())
               || Objects.isNull(player.getBirthday())
               || Objects.isNull((player.getExperience()))) {
           errors.reject("Data_blank", paramMissing);
       } else {

           String name = player.getName().trim();
           String title = player.getTitle().trim();

           if (name.isEmpty()) errors.reject("Data_blank",
                   paramMissing);
           if (title.isEmpty()) errors.reject("Data_blank",
                   paramMissing);

           player.setName(name);
           player.setTitle(title);

           if (name.length() > 12) {
               errors.reject("Length_error", "Name can not exceed 12 characters");
           }
           if (title.length() > 30) {
               errors.reject("Length_error", "Title can not exceed 30 characters");
           }

           if (player.getExperience() < 0 || player.getExperience() > 10_000_000) {
               errors.reject("Experience_error",
                       "Experience can not be negative or exceed 10.000.000");
           }
           if (player.getBirthday().before(new GregorianCalendar(2000, 0, 1).getTime())
                   || player.getBirthday().after(new GregorianCalendar(3000, 0, 1).getTime())) {
               errors.reject("Birthday_error", "Birthday is not valid");
           }


       }
    }
}
