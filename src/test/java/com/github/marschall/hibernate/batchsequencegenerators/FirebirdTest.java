package com.github.marschall.hibernate.batchsequencegenerators;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;
import javax.transaction.Transactional;

import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.rules.SpringClassRule;
import org.springframework.test.context.junit4.rules.SpringMethodRule;
import org.springframework.transaction.PlatformTransactionManager;

import com.github.marschall.hibernate.batchsequencegenerators.HsqlTest.LocalTransactionManagerConfiguration;
import com.github.marschall.hibernate.batchsequencegenerators.configurations.FirebirdConfiguration;

@Transactional
@ContextConfiguration(classes = {FirebirdConfiguration.class, LocalTransactionManagerConfiguration.class})
public class FirebirdTest {

  @ClassRule
  public static final SpringClassRule SPRING_CLASS_RULE = new SpringClassRule();

  @Rule
  public final SpringMethodRule springMethodRule = new SpringMethodRule();

  @Autowired
  private DataSource dataSource;

  private static final String S = "WITH RECURSIVE t(n, level_num) AS ("
          + "SELECT next value for SEQ_PARENT_ID as n, 1 as level_num FROM RDB$DATABASE "
          + "UNION ALL "
          +"SELECT next value for SEQ_PARENT_ID as n, level_num + 1 as level_num  FROM t  WHERE level_num < 10) "
          + "SELECT n FROM t";

  private static final String T = "SELECT NEXT VALUE FOR SEQ_PARENT_ID FROM RDB$DATABASE";

  private static final String H =
          "SELECT next value for seq_parent_id FROM UNNEST(SEQUENCE_ARRAY(1, 10, 1))";
//
//  private static final String S = "WITH RECURSIVE t(n, level_num) AS ("
//          + "SELECT next value for seq_parent_id as n, 1 as level_num "
//          + "UNION "
//          +"SELECT next value for seq_parent_id as n, level_num + 1 as level_num  FROM t  WHERE level_num < ?) "
//          + "SELECT n FROM t";

  @Test
  public void singleRowSelect() throws SQLException {
    try (Connection connection = this.dataSource.getConnection();
         Statement statement = connection.createStatement()) {
//      statement.execute("DROP SEQUENCE SEQ_CHILD_ID");
      statement.execute("CREATE SEQUENCE SEQ_PARENT_ID");
      try (ResultSet resultSet = statement.executeQuery(S)) {
//      try (ResultSet resultSet = statement.executeQuery("SELECT next value for seq_parent_id as n, 1 as level_num  FROM (VALUES(0))")) {
        while (resultSet.next()) {
          System.out.println(resultSet.getLong(1));
        }
      }
    }
  }


  @Configuration
  static class LocalTransactionManagerConfiguration {

    @Autowired
    private DataSource dataSource;

    @Bean
    public PlatformTransactionManager txManager() {
      return new DataSourceTransactionManager(dataSource);
    }

  }

}