package ru.immmus.util;

import ru.immmus.domain.User;

public class UserUtil {
    private UserUtil(){}
    public static boolean isBadUser(User user) {
        return user == null || user.isBanned();
    }
}
