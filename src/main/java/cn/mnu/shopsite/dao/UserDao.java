package cn.mnu.shopsite.dao;

import cn.mnu.shopsite.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserDao {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * 根据账号获得用户信息
     *
     * @param account 账号
     * @return null - 账号不存在，非null - 用户信息
     */
    public User getUser(String account) {
        return getUserByAccount(account);
    }

    /**
     * 判断账号是否已存在，用于用户注册账号
     *
     * @param account 账号
     * @return true - 账号已存在，false - 账号不存在
     */
    public boolean accountExists(String account) {
        return getUserByAccount(account) != null;
    }

    /**
     * 根据账号获得用户信息
     *
     * @param account 账号
     * @return null - 账号不存在，非null - 用户信息
     */
    private User getUserByAccount(String account) {
        String sql = "select * from t_user where account = ?";
        List<User> users = jdbcTemplate.query(sql, (rs, rowNum) -> {
            User user = new User();
            user.setAccount(rs.getString("account"));
            user.setName(rs.getString("name"));
            user.setPassword(rs.getString("password"));
            user.setSex(rs.getString("sex"));
            return user;
        }, account);

        return users.isEmpty() ? null : users.get(0);
    }

    public boolean addUser(User user) {
        String sql = "insert into t_user values (?, ?, ?, ?)";

        try {
            jdbcTemplate.update(sql, user.getAccount(), user.getPassword(), user.getName(), user.getSex());
            return true;
        }
        catch(DataAccessException ex) {
            return false;
        }
    }
}
