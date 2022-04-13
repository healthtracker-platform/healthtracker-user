package es.upm.miw.betca_tpv_user.api.dtos;

public class Validations {
    public static final String NINE_DIGITS = "\\d{9}";
    public static final String EMAIL = "/^[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,4}$/";

    private Validations() {
    }
}

