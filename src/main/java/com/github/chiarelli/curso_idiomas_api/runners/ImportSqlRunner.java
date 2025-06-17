package com.github.chiarelli.curso_idiomas_api.runners;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import com.github.chiarelli.curso_idiomas_api.escola.application.usecases.CadastrarAlunoUseCase;

@Service
@Profile("dev") 
public class ImportSqlRunner implements ApplicationRunner {

  private static final Logger logger = LoggerFactory.getLogger(CadastrarAlunoUseCase.class);
    
  private final DataSource dataSource;

  public ImportSqlRunner(DataSource dataSource) {
    this.dataSource = dataSource;
  }

  @Override
  public void run(ApplicationArguments args) throws Exception {

    try (Connection conn = dataSource.getConnection()) {
      String dbProductName = conn.getMetaData().getDatabaseProductName().toLowerCase();

      if (!dbProductName.contains("sql server")) {
        logger.info("O banco de dados não é MSSQL. Ignorando import.sql");
        return;
      }

      // Verifica se já existe alguma tabela/dado (ou use alguma tabela específica
      // como referência)
      if (isDatabaseAlreadyInitialized(conn)) {
        logger.info("O banco já está inicializado. Ignorando import.sql");
        return;
      }

      // Lê e executa o import.sql
      InputStream is = getClass().getClassLoader().getResourceAsStream("database/import.sql");
      String sql = new String(is.readAllBytes(), StandardCharsets.UTF_8);
      var stmt = conn.createStatement();
      try (stmt) {
        for (String part : sql.split(";")) {
          if (!part.trim().isEmpty()) {
            stmt.execute(part);
          }
        }
        logger.info("import.sql executado com sucesso.");
      }

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private boolean isDatabaseAlreadyInitialized(Connection conn) throws SQLException {
    ResultSet rs = conn.getMetaData().getProcedures(null, null, "SP_INIT_MARK");
    return rs.next();        
  }

}

