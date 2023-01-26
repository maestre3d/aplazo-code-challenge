package com.aruiz.loans.loans.infrastructure;

import com.aruiz.loans.loans.domain.Loan;
import com.aruiz.loans.loans.domain.LoanRepository;
import com.aruiz.loans.loans.domain.Payment;
import com.aruiz.loans.shared.domain.exceptions.ItemNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.*;
import java.util.Arrays;

public class LoanH2Repository implements LoanRepository {
    private final Connection connection;

    @Autowired
    public LoanH2Repository(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void save(Loan record) throws SQLException {
//        Connection connection = DriverManager.getConnection("jdbc:h2:mem:loans_service");
        String query = "INSERT INTO loans(id, payments) VALUES (?, ?)";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setInt(1, record.getId());
        statement.setArray(2,
                connection.createArrayOf("Payment", record.getPayments().toArray()));
        statement.executeUpdate();
        statement.close();
    }

    @Override
    public Loan getById(int id) throws ItemNotFoundException, SQLException {
        String query = "SELECT * FROM loans WHERE id = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setInt(1, id);
        ResultSet set = statement.executeQuery();
        if (set.wasNull())
            throw new ItemNotFoundException("loan");

        Loan loan = null;
        while (set.next()) {
            Payment[] a = (Payment[]) set.getArray("payments").getArray();
            loan = new Loan(set.getInt("id"),
                    Arrays.asList(a));
        }
        return loan;
    }
}
