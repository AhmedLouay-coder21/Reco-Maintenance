package com.Reco.backend.gateway;

import com.Reco.backend.model.PaymentMethod;
import com.Reco.backend.model.PaymentStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

class MockPaymentGatewayTest {

    private static final String VALID_CARD = "4111111111111111";
    private static final BigDecimal AMOUNT = new BigDecimal("42.50");

    private final MockPaymentGateway gateway = new MockPaymentGateway();

    @Nested
    @DisplayName("amount validation")
    class AmountValidation {

        @Test
        @DisplayName("fails when the amount is null")
        void failsOnNullAmount() {
            PaymentStatus status = gateway.process(null, PaymentMethod.CREDIT_CARD, VALID_CARD);

            assertThat(status).isEqualTo(PaymentStatus.FAILED);
        }

        @ParameterizedTest(name = "amount {0}")
        @ValueSource(strings = {"0", "0.00", "-0.01", "-1", "-250.75"})
        @DisplayName("fails for zero and negative amounts")
        void failsOnNonPositiveAmounts(String amount) {
            PaymentStatus status = gateway.process(
                    new BigDecimal(amount), PaymentMethod.MOCK_GATEWAY, null);

            assertThat(status).isEqualTo(PaymentStatus.FAILED);
        }

        @Test
        @DisplayName("checks the amount before the payment method is considered")
        void checksAmountBeforePaymentMethod() {
            PaymentStatus mockGateway = gateway.process(
                    BigDecimal.ZERO, PaymentMethod.MOCK_GATEWAY, VALID_CARD);
            PaymentStatus creditCard = gateway.process(
                    BigDecimal.ZERO, PaymentMethod.CREDIT_CARD, VALID_CARD);

            assertThat(mockGateway).isEqualTo(PaymentStatus.FAILED);
            assertThat(creditCard).isEqualTo(PaymentStatus.FAILED);
        }

        @ParameterizedTest(name = "amount {0}")
        @ValueSource(strings = {"0.01", "1", "19.99", "1000000"})
        @DisplayName("succeeds for any positive amount")
        void succeedsForPositiveAmounts(String amount) {
            PaymentStatus status = gateway.process(
                    new BigDecimal(amount), PaymentMethod.MOCK_GATEWAY, null);

            assertThat(status).isEqualTo(PaymentStatus.SUCCESS);
        }
    }

    @Nested
    @DisplayName("MOCK_GATEWAY method")
    class MockGatewayMethod {

        @Test
        @DisplayName("succeeds without any payment method details")
        void succeedsWithoutDetails() {
            PaymentStatus status = gateway.process(AMOUNT, PaymentMethod.MOCK_GATEWAY, null);

            assertThat(status).isEqualTo(PaymentStatus.SUCCESS);
        }

        @Test
        @DisplayName("ignores declining card details, since the method short circuits")
        void ignoresDecliningCardDetails() {
            PaymentStatus status = gateway.process(
                    AMOUNT, PaymentMethod.MOCK_GATEWAY, "4000000000000000");

            assertThat(status).isEqualTo(PaymentStatus.SUCCESS);
        }
    }

    @Nested
    @DisplayName("CREDIT_CARD method")
    class CreditCardMethod {

        @ParameterizedTest(name = "details \"{0}\"")
        @ValueSource(strings = {
                "4000000000000000",
                "4111111111110000",
                "0000",
                "00000",
                "12340000",
                "1000 0000"
        })
        @DisplayName("declines card numbers ending in 0000")
        void declinesCardsEndingIn0000(String details) {
            PaymentStatus status = gateway.process(AMOUNT, PaymentMethod.CREDIT_CARD, details);

            assertThat(status).isEqualTo(PaymentStatus.FAILED);
        }

        @ParameterizedTest(name = "details \"{0}\"")
        @ValueSource(strings = {
                "4000 0000 0000 0000",
                " 4000000000000000 ",
                "4000\t0000 0000\n0000"
        })
        @DisplayName("ignores whitespace when matching the card suffix")
        void ignoresWhitespaceWhenMatchingSuffix(String details) {
            PaymentStatus status = gateway.process(AMOUNT, PaymentMethod.CREDIT_CARD, details);

            assertThat(status).isEqualTo(PaymentStatus.FAILED);
        }

        @ParameterizedTest(name = "details \"{0}\"")
        @ValueSource(strings = {
                "4111111111111111",
                "4000000000000002",
                "4111-1111-1111-1111",
                "123",
                "000",
                "1000"
        })
        @DisplayName("approves any other card number")
        void approvesOtherCardNumbers(String details) {
            PaymentStatus status = gateway.process(AMOUNT, PaymentMethod.CREDIT_CARD, details);

            assertThat(status).isEqualTo(PaymentStatus.SUCCESS);
        }

        @ParameterizedTest
        @NullAndEmptySource
        @ValueSource(strings = {"   ", "\t", "0", "000", "0001"})
        @DisplayName("approves when details are missing or cannot end in 0000")
        void approvesWhenDetailsAreMissingOrIrrelevant(String details) {
            PaymentStatus status = gateway.process(AMOUNT, PaymentMethod.CREDIT_CARD, details);

            assertThat(status).isEqualTo(PaymentStatus.SUCCESS);
        }
    }

    @Test
    @DisplayName("always returns SUCCESS or FAILED, never null")
    void neverReturnsNull() {
        PaymentStatus[] statuses = {
                gateway.process(AMOUNT, PaymentMethod.MOCK_GATEWAY, null),
                gateway.process(AMOUNT, PaymentMethod.CREDIT_CARD, null),
                gateway.process(AMOUNT, PaymentMethod.CREDIT_CARD, "0000"),
                gateway.process(BigDecimal.ZERO, PaymentMethod.MOCK_GATEWAY, null)
        };

        assertThat(statuses).doesNotContainNull();
    }
}