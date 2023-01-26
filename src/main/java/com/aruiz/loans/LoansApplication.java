package com.aruiz.loans;

import com.aruiz.loans.loans.domain.LoanRepository;
import com.aruiz.loans.loans.infrastructure.LoanH2Repository;
import com.aruiz.loans.shared.infrastructure.H2PoolFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

@SpringBootApplication
public class LoansApplication {

	public static void main(String[] args) {
		SpringApplication.run(LoansApplication.class, args);
	}

	@Bean
	public Connection connection() throws SQLException {
		Connection connection = H2PoolFactory.getConnection("jdbc:h2:mem:loans_service");
		Statement statement = connection.createStatement();
		statement.execute("CREATE TABLE loans(id INT NOT NULL, payments OBJECT ARRAY)");
		statement.close();
		return connection;
	}

	@Bean
	public LoanRepository loanRepository(Connection connection) {
		return new LoanH2Repository(connection);
	}
}
