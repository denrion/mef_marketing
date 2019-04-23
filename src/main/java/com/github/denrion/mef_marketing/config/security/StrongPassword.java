package com.github.denrion.mef_marketing.config.security;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Size(min = 6, max = 45)
@Pattern.List({
        @Pattern(regexp = "(?=.*[0-9]).+", message = "Password must contain one digit."),
        @Pattern(regexp = "(?=.*[a-z]).+", message = "Password must contain one lowercase letter."),
        @Pattern(regexp = "(?=.*[A-Z]).+", message = "Password must contain one upper letter."),
        @Pattern(regexp = "(?=.*[!@#$%^&*+=?-_()/\".,<>~`;:]).+", message = "Password must contain one special character."),
        @Pattern(regexp = "(?=\\S+$).+", message = "Password must contain no whitespace.")
})
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface StrongPassword {
}
