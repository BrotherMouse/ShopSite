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
    public User getNormalUser(String account) {
        return getUserByAccountAndType(account, "user");
    }

    /**
     * 判断账号是否已存在，用于用户注册账号
     *
     * @param account 账号
     * @return true - 账号已存在，false - 账号不存在
     */
    public boolean normalAccountExists(String account) {
        return getNormalUser(account) != null;
    }

    public User getAdminUser(String account) {
        return getUserByAccountAndType(account, "admin");
    }

    public boolean adminAccountExists(String account) {
        return getAdminUser(account) != null;
    }

    /**
     * 根据账号获得用户信息
     *
     * @param account 账号
     * @return null - 账号不存在，非null - 用户信息
     */
    private User getUserByAccountAndType(String account, String type) {
        String sql = "select * from t_user where account = ? and type = ?";
        List<User> users = jdbcTemplate.query(sql, (rs, rowNum) -> {
            User user = new User();
            user.setAccount(rs.getString("account"));
            user.setType(rs.getString("type"));
            user.setName(rs.getString("name"));
            user.setPassword(rs.getString("password"));
            user.setSex(rs.getString("sex"));
            user.setEmail(rs.getString("email"));
            user.setCellphone(rs.getString("cellphone"));
            return user;
        }, account, type);

        return users.isEmpty() ? null : users.get(0);
    }

    private boolean addUser(User user) {
        String sql = "insert into t_user values (?, ?, ?, ?, ?, ?, ?)";

        try {
            jdbcTemplate.update(sql, user.getType(), user.getAccount(), user.getName(), user.getPassword(),
                    user.getSex(), user.getEmail(), user.getCellphone());
            return true;
        }
        catch(DataAccessException ex) {
            return false;
        }
    }

    public boolean addNormalUser(User normalUser) {
        normalUser.setType("user");
        return addUser(normalUser);
    }

    public boolean addAdminUser(User adminUser) {
        adminUser.setType("admin");
        return addUser(adminUser);
    }
}
