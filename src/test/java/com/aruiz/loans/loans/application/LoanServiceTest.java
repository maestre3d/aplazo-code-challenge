package com.aruiz.loans.loans.application;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

import com.aruiz.loans.loans.domain.InterestType;
import com.aruiz.loans.loans.domain.Loan;
import com.aruiz.loans.loans.domain.LoanRepository;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

public class LoanServiceTest {
    private final LoanService service = new LoanService(mock(LoanRepository.class));

    private record GenerateLoanArgs(InterestType interestType, Double amount, int weekTerms, Double rate) {
    }

    private static Stream<Arguments> generateLoanTestValues() {
        return Stream.of(
                Arguments.of(new GenerateLoanArgs(InterestType.SIMPLE, 10000d, 4, 3.5),
                        2506.7307692307695, 4),
                Arguments.of(new GenerateLoanArgs(InterestType.WEEKLY_COMPOUND, 10000d, 26, 3.5),
                        391.40308902357697, 26),
                Arguments.of(new GenerateLoanArgs(InterestType.MONTHLY_COMPOUND, 10000d, 26, 3.5),
                        391.3954236507236, 26),
                Arguments.of(new GenerateLoanArgs(InterestType.ANNUALLY_COMPOUND, 10000d, 26, 3.5),
                        391.28826825722706, 26)
        );
    }

    @ParameterizedTest
    @MethodSource("generateLoanTestValues")
    void testGenerateLoan(GenerateLoanArgs args, Double expAmount, int expTotalTerms) {
        try {
            GenerateLoan generateLoan = new GenerateLoan(args.interestType.name(), args.amount, args.weekTerms, args.rate);
            Loan loan = service.generateLoan(generateLoan);
            assertEquals(expAmount, loan.getPayments().get(0).amount());
            assertEquals(expTotalTerms, loan.getPayments().size());
            assertNotEquals(0, loan.getId());
        } catch (Exception ignored) {
        }
    }
}
