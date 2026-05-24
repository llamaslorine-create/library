package com.example.book;

import java.util.ArrayList;
import java.util.List;

public class UserData {
    private static List<User> users = new ArrayList<>();
    private static User currentUser;

    static {
        users.add(new User("1", "admin", "123456", "admin", "管理员"));
        users.add(new User("2", "user", "123456", "user", "普通用户"));
    }

    public static User login(String username, String password) {
        for (User user : users) {
            if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                currentUser = user;
                return user;
            }
        }
        return null;
    }

    public static User getCurrentUser() {
        return currentUser;
    }

    public static void setCurrentUser(User user) {
        currentUser = user;
    }

    public static List<User> getAllUsers() {
        return users;
    }

    public static boolean addUser(User user) {
        for (User u : users) {
            if (u.getUsername().equals(user.getUsername())) {
                return false;
            }
        }
        users.add(user);
        return true;
    }

    public static boolean updateUser(User updatedUser) {
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getId().equals(updatedUser.getId())) {
                users.set(i, updatedUser);
                if (currentUser != null && currentUser.getId().equals(updatedUser.getId())) {
                    currentUser = updatedUser;
                }
                return true;
            }
        }
        return false;
    }

    public static boolean deleteUser(String userId) {
        User userToDelete = null;
        for (User user : users) {
            if (user.getId().equals(userId)) {
                userToDelete = user;
                break;
            }
        }
        if (userToDelete != null && !userToDelete.isAdmin()) {
            users.remove(userToDelete);
            return true;
        }
        return false;
    }

    public static User getUserById(String userId) {
        for (User user : users) {
            if (user.getId().equals(userId)) {
                return user;
            }
        }
        return null;
    }
}