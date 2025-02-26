package com.mooo.devforin.appservice.config.querydsl;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.boot.model.naming.Identifier;
import org.hibernate.boot.model.naming.PhysicalNamingStrategy;
import org.hibernate.engine.jdbc.env.spi.JdbcEnvironment;

@Slf4j
public class NamingStrategyConfig implements PhysicalNamingStrategy {


    @Override
    public Identifier toPhysicalCatalogName(Identifier identifier, JdbcEnvironment jdbcEnvironment) {
        return identifier;
    }

    @Override
    public Identifier toPhysicalSchemaName(Identifier identifier, JdbcEnvironment jdbcEnvironment) {
        return convert(identifier);
    }

    @Override
    public Identifier toPhysicalTableName(Identifier identifier, JdbcEnvironment jdbcEnvironment) {
        return convert(identifier);
    }

    @Override
    public Identifier toPhysicalSequenceName(Identifier identifier, JdbcEnvironment jdbcEnvironment) {
        return convert(identifier);
    }

    @Override
    public Identifier toPhysicalColumnName(Identifier identifier, JdbcEnvironment jdbcEnvironment) {
        return convert(identifier);
    }

    private Identifier convert(Identifier name) {
        if (name == null) {
            return null;
        }

        StringBuilder sb = new StringBuilder(name.getText());
        for (int i = 1; i < sb.length() - 1; i++) {
          if (Character.isLowerCase(sb.charAt(i - 1)) && Character.isUpperCase(sb.charAt(i))
              && Character.isLowerCase(sb.charAt(i + 1))) {
            sb.insert(i++, '_');
          }
        }

        log.debug("convert sb({})", sb.toString());
    return new Identifier(sb.toString().toLowerCase() , true);
  }
}
