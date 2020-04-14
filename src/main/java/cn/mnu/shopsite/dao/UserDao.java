package cn.mnu.shopsite.dao;

import cn.mnu.shopsite.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 用户信息dao
 */
@Repository
public class UserDao {
    /**
     * 操作数据库的对象
     */
    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * 根据账号获得普通用户信息
     *
     * @param account 账号
     * @return null - 账号不存在，非null - 用户信息
     */
    public User getNormalUser(String account) {
        return getUserByAccountAndType(account, "user");
    }

    /**
     * 判断普通用户账号是否已存在
     *
     * @param account 账号
     * @return true - 账号已存在，false - 账号不存在
     */
    public boolean normalAccountExists(String account) {
        return getNormalUser(account) != null;
    }

    /**
     * 根据账号获得管理员信息
     *
     * @param account 账号
     * @return null - 账号不存在，非null - 用户信息
     */
    public User getAdminUser(String account) {
        return getUserByAccountAndType(account, "admin");
    }

    /**
     * 判断管理员账号是否已存在
     *
     * @param account 账号
     * @return true - 账号已存在，false - 账号不存在
     */
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

    /**
     * 添加用户信息
     *
     * @param user 用户信息
     * @return true - 成功，false - 失败（账号已存在）
     */
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

    /**
     * 添加普通用户信息
     *
     * @param normalUser 普通用户信息
     * @return true - 成功，false - 失败（账号已存在）
     */
    public boolean addNormalUser(User normalUser) {
        normalUser.setType("user");
        return addUser(normalUser);
    }

    /**
     * 添加管理员信息
     *
     * @param adminUser 普通用户信息
     * @return true - 成功，false - 失败（账号已存在）
     */
    public boolean addAdminUser(User adminUser) {
        adminUser.setType("admin");
        return addUser(adminUser);
    }
}