package service;

import Dao.UserDAO;
import Model.User;
import util.PasswordUtil;
import util.Validator;

public class AuthenticationService {
    private final UserDAO userDAO = new UserDAO();

    public String register(String username, String password) {
        if (!Validator.isValidUsername(username))
            return "Tên người dùng phải 3-20 ký tự, chỉ gồm chữ/số/gạch dưới.";
        if (!Validator.isValidPassword(password))
            return "Mật khẩu phải có ít nhất 6 ký tự.";
        boolean ok = userDAO.register(username, PasswordUtil.hash(password));
        return ok ? null : "Tên người dùng đã tồn tại!";
    }

    public User login(String username, String password) {
        User user = userDAO.findByUsername(username);
        if (user == null) return null;
        return PasswordUtil.verify(password, user.getPasswordHash()) ? user : null;
    }
}
