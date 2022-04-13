package es.upm.miw.betca_tpv_user.data.model;

public enum Role {
    ADMIN, PROFESSIONAL, PATIENT, AUTHENTICATED;

    public static final String PREFIX = "ROLE_";

    public static Role of(String withPrefix) {
        return Role.valueOf(withPrefix.replace(Role.PREFIX, ""));
    }

    public String withPrefix() {
        return PREFIX + this.toString();
    }

}
