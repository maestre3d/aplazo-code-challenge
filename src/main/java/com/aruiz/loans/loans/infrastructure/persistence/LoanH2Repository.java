package com.aruiz.loans.loans.infrastructure.persistence;

import com.aruiz.loans.loans.domain.Loan;
import com.aruiz.loans.loans.domain.LoanRepository;
import com.aruiz.loans.loans.domain.Payment;
import com.aruiz.loans.shared.domain.exceptions.ItemNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LoanH2Repository implements LoanRepository {
    private final Connection connection;

    @Autowired
    public LoanH2Repository(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void save(Loan record) throws SQLException {
        String query = "INSERT INTO loans(id, payments) VALUES (?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, record.getId());
            statement.setArray(2,
                    connection.createArrayOf("Payment", record.getPayments().toArray()));
            statement.executeUpdate();
        }

    }

    @Override
    public Loan getById(int id) throws ItemNotFoundException, SQLException {
        String query = "SELECT id, payments FROM loans WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);
            ResultSet rs = statement.executeQuery();
            if (!rs.next())
                return null;

            Object[] arr = (Object[]) rs.getArray("payments").getArray();
            List<Payment> buf = new ArrayList<>(arr.length);
            Arrays.stream(arr).forEach(obj -> buf.add((Payment) obj));
            return new Loan(rs.getInt("id"),
                    buf);
        }
    }
}
