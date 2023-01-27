package com.aruiz.loans;

import com.aruiz.loans.loans.domain.LoanRepository;
import com.aruiz.loans.loans.infrastructure.persistence.LoanH2Repository;
import com.aruiz.loans.shared.infrastructure.persistence.DatabaseHealthChecker;
import com.aruiz.loans.shared.infrastructure.persistence.H2HealthChecker;
import com.aruiz.loans.shared.infrastructure.persistence.H2PoolFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.sql.Connection;
import java.sql.SQLException;

@SpringBootApplication
public class LoansApplication {

	public static void main(String[] args) {
		SpringApplication.run(LoansApplication.class, args);
	}

	@Bean
	public Connection connection() throws SQLException {
		return H2PoolFactory.getConnection("jdbc:h2:mem:loans_service");
	}

	@Bean
	public LoanRepository loanRepository(Connection connection) {
		return new LoanH2Repository(connection);
	}

	@Bean
	public DatabaseHealthChecker databaseHealthChecker(Connection connection) {
		return new H2HealthChecker(connection);
	}
}
