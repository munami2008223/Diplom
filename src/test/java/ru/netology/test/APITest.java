package ru.netology.test;

import ru.netology.data.DBHelper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.netology.data.DataHelper;

import static ru.netology.data.APIHelper.payFromCard;
import static ru.netology.data.DBHelper.deleteAllDB;
import static org.junit.jupiter.api.Assertions.*;


public class APITest {
    String approvedCardNumber = DataHelper.getCardApproved().getCardNumber();//одобренная карта
    String declinedCardNumber = DataHelper.getCardDeclined().getCardNumber();//отклоненная карта
    String randomCardNumber = DataHelper.getRandomCardNumber();//отклоненная карта
    private static final int amount = 45_000_00;
    private final String debitPay = "/pay";
    private final String creditPay = "/credit";

    @AfterEach
    void setDownDB() {
        deleteAllDB();
    }

    // оплата по карте

    @Test
    @DisplayName("4.1 Должно быть поле Amount при оплате одобренной дебетовой картой")
    void shouldHaveAmountWithDebitApprovedCard() {
        payFromCard(approvedCardNumber, debitPay, 200);
        assertEquals(amount, DBHelper.getAmountDebitCard());
    }

    @Test
    @DisplayName("4.2 Должен быть статус APPROVED при оплате одобренной дебетовой картой")
    void shouldHaveStatusWithDebitApprovedCard() {
        payFromCard(approvedCardNumber, debitPay, 200);
        assertEquals("APPROVED", DBHelper.getStatusDebitCard());
    }

    @Test
    @DisplayName("4.3 Должно быть поле Payment_Id при оплате одобренной дебетовой картой")
    void shouldHavePaymentIdWithDebitApprovedCard() {
        payFromCard(approvedCardNumber, debitPay, 200);
        assertNotNull(DBHelper.getPaymentId());
    }

    @Test
    @DisplayName("4.4 Должно быть поле Transaction_Id при оплате одобренной дебетовой картой")
    void shouldHaveTransactionIdWithDebitApprovedCard() {
        payFromCard(approvedCardNumber, debitPay, 200);
        assertNotNull(DBHelper.getTransactionIdDebitCard());
    }

    @Test
    @DisplayName("4.5 Поля Payment_Id и Transaction_Id равны при оплате одобренной дебетовой картой")
    void shouldEqualPaymentAndTransactionIdWithDebitApprovedCard() {
        payFromCard(approvedCardNumber, debitPay, 200);
        assertEquals(DBHelper.getPaymentId(), DBHelper.getTransactionIdDebitCard());
    }

    @Test
    @DisplayName("4.6 Должно быть значение NULL в поле Credit_Id при оплате одобренной дебетовой картой")
    void shouldNotHaveCreditIdWithDebitApprovedCard() {
        payFromCard(approvedCardNumber, debitPay, 200);
        assertNull(DBHelper.getCreditId());
    }

    @Test
    @DisplayName("4.7 Должен быть статус DECLINED при оплате заблокированной дебетовой картой")
    void shouldHaveStatusWithDebitDeclinedCard() {
        payFromCard(declinedCardNumber, debitPay, 200);
        assertEquals("DECLINED", DBHelper.getStatusDebitCard());
    }

    @Test
    @DisplayName("4.8 Должно отсутствовать поле Amount при оплате заблокированной дебетовой картой")
    void shouldNotHaveAmountWithDebitDeclinedCard() {
        payFromCard(declinedCardNumber, debitPay, 200);
        assertNull(DBHelper.getAmountDebitCard());
    }

    @Test
    @DisplayName("4.9 Должно отсутствовать поле Payment_Id при оплате заблокированной дебетовой картой")
    void shouldNotHavePaymentIdWithDebitDeclinedCard() {
        payFromCard(declinedCardNumber, debitPay, 200);
        assertNull(DBHelper.getPaymentId());
    }

    @Test
    @DisplayName("4.10 Должно отсутствовать поле Transaction_Id при оплате заблокированной дебетовой картой")
    void shouldNotHaveTransactionIdWithDebitDeclinedCard() {
        payFromCard(declinedCardNumber, debitPay, 200);
        assertNull(DBHelper.getTransactionIdDebitCard());
    }

    @Test
    @DisplayName("4.11 Должно отсутствовать поле Credit_Id при оплате заблокированной дебетовой картой")
    void shouldNotHaveCreditIdWithDebitCardDeclined() {
        payFromCard(declinedCardNumber, debitPay, 200);
        assertNull(DBHelper.getCreditId());
    }

    @Test
    @DisplayName("4.12 Должна вернуться ошибка при оплате рандомной дебетовой картой")
    void shouldReturnFailWithDebitUnknownCard() {
        payFromCard(randomCardNumber, debitPay, 500);
    }

    // кредит по данным карты

    @Test
    @DisplayName("4.13 Должен быть статус APPROVED при оплате одобренной картой в кредит")
    void shouldHaveStatusWithCreditApprovedCard() {
        payFromCard(approvedCardNumber, creditPay, 200);
        assertEquals("APPROVED", DBHelper.getStatusCreditCard());
    }

    @Test
    @DisplayName("4.14 Должно быть поле BankI_d при оплате одобренной картой в кредит")
    void shouldHaveBankIdWithCreditApprovedCard() {
        payFromCard(approvedCardNumber, creditPay, 200);
        assertNotNull(DBHelper.getBankIdCreditCard());
    }

    @Test
    @DisplayName("4.15 Должно быть поле Credit_Id при оплате одобренной картой в кредит")
    void shouldHaveCreditIdWithCreditApprovedCard() {
        payFromCard(approvedCardNumber, creditPay, 200);
        assertNotNull(DBHelper.getCreditId());
    }

    @Test
    @DisplayName("4.16 Должно отсутствовать поле Payment_Id при оплате одобренной картой в кредит")
    void shouldNotHavePaymentIdWithCreditApprovedCard() {
        payFromCard(approvedCardNumber, creditPay, 200);
        assertNull(DBHelper.getPaymentId());
    }

    @Test
    @DisplayName("4.17 Должен быть статус DECLINED при оплате заблокированной картой в кредит")
    void shouldHaveStatusWithCreditDeclinedCard() {
        payFromCard(declinedCardNumber, creditPay, 200);
        assertEquals("DECLINED", DBHelper.getStatusCreditCard());
    }

    @Test
    @DisplayName("4.18 Должно отсутствовать поле Bank_Id при оплате заблокированной картой в кредит")
    void shouldNotHaveBankIdWithCreditDeclinedCard() {
        payFromCard(declinedCardNumber, creditPay, 200);
        assertNull(DBHelper.getBankIdCreditCard());
    }

    @Test
    @DisplayName("4.19 Должно отсутствовать поле Credit_Id при оплате заблокированной картой в кредит")
    void shouldNotHaveCreditIdWithCreditApprovedCard() {
        payFromCard(declinedCardNumber, creditPay, 200);
        assertNull(DBHelper.getCreditId());
    }

    @Test
    @DisplayName("4.20 Должно отсутствовать поле Payment_Id при оплате заблокированной картой в кредит")
    void shouldNotHavePaymentIdWithCreditDeclinedCard() {
        payFromCard(declinedCardNumber, creditPay, 200);
        assertNull(DBHelper.getPaymentId());
    }

    @Test
    @DisplayName("4.21 Должна вернуться ошибка при оплате рандомной картой в кредит")
    void shouldReturnFailWithCreditUnknownCard() {
        payFromCard(randomCardNumber, creditPay, 500);
    }
}
