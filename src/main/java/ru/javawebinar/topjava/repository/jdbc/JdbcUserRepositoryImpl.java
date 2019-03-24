package ru.javawebinar.topjava.repository.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.UserRepository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Repository
@Transactional(readOnly = true)
public class JdbcUserRepositoryImpl implements UserRepository {

    private static final BeanPropertyRowMapper<User> ROW_MAPPER = BeanPropertyRowMapper.newInstance(User.class);

    private final JdbcTemplate jdbcTemplate;

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private final SimpleJdbcInsert insertUser;

    private final ResultSetExtractor<List<User>> resultSetExtractor = new UserResultSetExtractor();

    class UserResultSetExtractor implements ResultSetExtractor<List<User>> {

        private User mapUser(ResultSet rs) throws SQLException {
            User user = new User();
            user.setId(rs.getInt("id"));
            user.setCaloriesPerDay(rs.getInt("calories_per_day"));
            user.setPassword(rs.getString("password"));
            user.setName(rs.getString("name"));
            user.setRegistered(rs.getDate("registered"));
            user.setEmail(rs.getString("email"));
            user.setEnabled(rs.getBoolean("enabled"));
            return user;
        }

        private Role mapRole(ResultSet rs) throws SQLException {
            String roleName = rs.getString("role");
            return roleName == null ? null : Role.valueOf(roleName);
        }

        @Override
        public List<User> extractData(ResultSet rs) throws SQLException, DataAccessException {

            List<User> resultList = new LinkedList<>();

            User currentUser = null;
            Set<Role> roles = null;
            while (rs.next()) {
                int id = rs.getInt("id");

                if (currentUser == null) { // initial object
                    currentUser = mapUser(rs);
                    roles = new HashSet<>();
                } else if (currentUser.getId() != id) { // break
                    currentUser.setRoles(roles);
                    resultList.add(currentUser);
                    currentUser = mapUser(rs);
                    roles = new HashSet<>();
                }
                Role role = mapRole(rs);
                if (role != null) {
                    roles.add(role);
                }
            }
            if (currentUser != null) { // last object
                currentUser.setRoles(roles);
                resultList.add(currentUser);
            }

            return resultList;
        }
    }

    @Autowired
    public JdbcUserRepositoryImpl(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.insertUser = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("users")
                .usingGeneratedKeyColumns("id");

        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    @Transactional
    public User save(User user) {
        BeanPropertySqlParameterSource parameterSource = new BeanPropertySqlParameterSource(user);

        if (user.isNew()) {
            Number newKey = insertUser.executeAndReturnKey(parameterSource);
            user.setId(newKey.intValue());
        } else {

            if (namedParameterJdbcTemplate.update(
                    "UPDATE users SET name=:name, email=:email, password=:password, " +
                            "registered=:registered, enabled=:enabled, calories_per_day=:caloriesPerDay WHERE id=:id", parameterSource) == 0) {
                return null;
            } else {
                //removing roles
                MapSqlParameterSource rolesParameterSource = new MapSqlParameterSource();
                rolesParameterSource.addValue("userId", user.getId());
                rolesParameterSource.addValue("roles", user.getRoles().stream().map(Enum::name).collect(Collectors.toList()));
                namedParameterJdbcTemplate.update("DELETE FROM user_roles WHERE user_id=:userId AND role not in (:roles)", rolesParameterSource);
            }
        }

        //adding roles
        Set<Role> roles = user.getRoles();
        if (!roles.isEmpty()) {
            final MapSqlParameterSource[] batchArgs = new MapSqlParameterSource[roles.size()];
            int i = 0;
            for (Role role : roles) {
                MapSqlParameterSource params = new MapSqlParameterSource();
                params.addValue("userId", user.getId());
                params.addValue("role", role.name());
                batchArgs[i] = params;
                i++;
            }

            namedParameterJdbcTemplate.batchUpdate("INSERT INTO user_roles(user_id, role) VALUES(:userId, :role) ON CONFLICT DO NOTHING;", batchArgs);

        }
        return user;
    }

    @Override
    @Transactional
    public boolean delete(int id) {
        return jdbcTemplate.update("DELETE FROM users WHERE id=?", id) != 0;
    }

    @Override
    public User get(int id) {
        List<User> users = jdbcTemplate.query("SELECT users.*, ur.role FROM users LEFT JOIN user_roles AS ur ON users.id = ur.user_id WHERE users.id=? ORDER BY users.id", resultSetExtractor, id);
        return DataAccessUtils.singleResult(users);
    }

    @Override
    public User getByEmail(String email) {
//        return jdbcTemplate.queryForObject("SELECT * FROM users WHERE email=?", ROW_MAPPER, email);
        List<User> users = jdbcTemplate.query("SELECT users.*, ur.role FROM users LEFT JOIN user_roles AS ur ON users.id = ur.user_id WHERE email=?", resultSetExtractor, email);
        return DataAccessUtils.singleResult(users);
    }

    @Override
    public List<User> getAll() {
        return jdbcTemplate.query("SELECT users.*, ur.role FROM users LEFT JOIN user_roles AS ur ON users.id = ur.user_id ORDER BY name, email, id", resultSetExtractor);
    }
}
