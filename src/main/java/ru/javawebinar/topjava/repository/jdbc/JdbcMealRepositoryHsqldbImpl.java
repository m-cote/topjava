package ru.javawebinar.topjava.repository.jdbc;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.Profiles;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Repository
@Profile(Profiles.HSQL_DB)
public class JdbcMealRepositoryHsqldbImpl extends JdbcMealRepositoryImpl {

    @Override
    public Object jdbcTemplateParameterOf(LocalDateTime param) {
        return Timestamp.valueOf(param);
    }
}
