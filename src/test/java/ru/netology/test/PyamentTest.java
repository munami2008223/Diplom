
package ru.netology.test;

import com.codeborne.selenide.logevents.SelenideLogger;

import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.*;
        import ru.netology.data.DataHelper;
import ru.netology.data.SQLHelper;
import ru.netology.page.PaymentTypesPage;


import static com.codeborne.selenide.Selenide.open;

public class PyamentTest {

    String approvedCardNumber = DataHelper.getCardApproved().getCardNumber();//одобренная карта
    String declinedCardNumber = DataHelper.getCardDeclined().getCardNumber();//отклоненная карта
    String validMonth = DataHelper.getRandomMonth(1);//валидный месяц
    String validYear = DataHelper.getRandomYear(1);//валидный год
    String validOwnerName = DataHelper.getRandomName();//валидное имя
    String validCode = DataHelper.getNumberCVC(3);//валидный CVC
    String currentYar = DataHelper.getRandomYear(0);//текущий год

    @BeforeEach
    public void setUp() {
        open("http://localhost:8080");
    }

    @AfterEach
    public void shouldCleanBase() {
        SQLHelper.cleanBase();//очистка базы
    }

    @BeforeAll
    public static void setUpAll() {
        SelenideLogger.addListener("allure", new AllureSelenide());
    }

    @AfterAll
    public static void tearDownAll() {
        SelenideLogger.removeListener("allure");
    }

    @Test
    @DisplayName("1.1 Оплата по карте, одобренная карта, валидное заполнение полей")
    public void shouldCardPaymentApproved() {
        PaymentTypesPage page = new PaymentTypesPage();
        page.paymentTypesPage();
        var debitCardPage = page.cardPayment();
        debitCardPage.cleanFields();
        debitCardPage.fillCardPaymentForm(approvedCardNumber, validMonth, validYear, validOwnerName, validCode);
        debitCardPage.bankApprovedOperation();
        Assertions.assertEquals("APPROVED", SQLHelper.getCardPayment());
    }

    @Test
    @DisplayName("1.2 Кредит по данным карты, одобренная карта, валидное заполнение полей")
    public void shouldPaymentApproved() {
        PaymentTypesPage page = new PaymentTypesPage();
        page.paymentTypesPage();
        var creditCardPage = page.creditPayment();
        creditCardPage.cleanFields();
        creditCardPage.fillCardPaymentForm(approvedCardNumber, validMonth, validYear, validOwnerName, validCode);
        creditCardPage.bankApprovedOperation();
        Assertions.assertEquals("APPROVED", SQLHelper.getCreditPayment());
    }

    @Test
    @DisplayName("2.1 Оплата по заблокированной карте, валидное заполнение полей")
    public void shouldDeclinedCardPayment() {
        PaymentTypesPage page = new PaymentTypesPage();
        page.paymentTypesPage();
        var debitCardPage = page.cardPayment();
        debitCardPage.cleanFields();
        debitCardPage.fillCardPaymentForm(declinedCardNumber, validMonth, validYear, validOwnerName, validCode);
        debitCardPage.bankDeclinedOperation();
        Assertions.assertEquals("DECLINED", SQLHelper.getCardPayment());
    }

    @Test
    @DisplayName("2.2 Кредит по данным заблокированной карты, валидное заполнение полей")
    public void shouldDeclinedCardCredit() {
        PaymentTypesPage page = new PaymentTypesPage();
        page.paymentTypesPage();
        var creditCardPage = page.creditPayment();
        creditCardPage.cleanFields();
        creditCardPage.fillCardPaymentForm(declinedCardNumber, validMonth, validYear, validOwnerName, validCode);
        creditCardPage.bankDeclinedOperation();
        Assertions.assertEquals("Declined", SQLHelper.getCreditPayment());
    }

    @Test
    @DisplayName("2.3 Оплата по случайному номеру карты, валидное заполнение полей")
    public void shouldCardPaymentRandomNumber() {
        PaymentTypesPage page = new PaymentTypesPage();
        page.paymentTypesPage();
        var debitCardPage = page.cardPayment();
        var getRandomCardNumber = DataHelper.getRandomCardNumber();
        debitCardPage.cleanFields();
        debitCardPage.fillCardPaymentForm(getRandomCardNumber, validMonth, validYear, validOwnerName, validCode);
        debitCardPage.bankDeclinedOperation();

    }

    @Test
    @DisplayName("2.4 Кредит по случайному номеру карты, валидное заполнение полей")
    public void paymentByRandomCardNumber() {
        PaymentTypesPage page = new PaymentTypesPage();
        page.paymentTypesPage();
        var creditCardPage = page.creditPayment();
        var getRandomCardNumber = DataHelper.getRandomString(16);
        creditCardPage.cleanFields();
        creditCardPage.fillCardPaymentForm(getRandomCardNumber, validMonth, validYear, validOwnerName, validCode);
        creditCardPage.bankDeclinedOperation();

    }

    @Test
    @DisplayName("2.5 Отправка пустой формы в блоке Оплата картой")
    public void shouldHandleEmptyFields() {
        PaymentTypesPage page = new PaymentTypesPage();
        page.paymentTypesPage();
        var debitCardPage = page.cardPayment();
        var emptyField = DataHelper.getEmptyField();
        debitCardPage.cleanFields();
        debitCardPage.fillCardPaymentForm(emptyField, emptyField, emptyField, emptyField, emptyField);
        debitCardPage.errorFormat();
    }

    @Test
    @DisplayName("2.6 Отправка пустой формы в блоке Купить в кредит")
    public void paymentEmptyForm() {
        PaymentTypesPage page = new PaymentTypesPage();
        page.paymentTypesPage();
        var creditCardPage = page.creditPayment();
        var emptyField = DataHelper.getEmptyField();
        creditCardPage.cleanFields();
        creditCardPage.fillCardPaymentForm(emptyField, emptyField, emptyField, emptyField, emptyField);
        creditCardPage.errorFormat();
    }

    @Test
    @DisplayName("3.1 Валидация поля Номер карты - латиница")
    public void paymentByRandomCardNumberLatin() {
        PaymentTypesPage page = new PaymentTypesPage();
        page.paymentTypesPage();
        var creditCardPage = page.creditPayment();
        var getRandomStringEn = DataHelper.getRandomStringEn(16);
        creditCardPage.cleanFields();
        creditCardPage.fillCardPaymentForm(getRandomStringEn, validMonth, validYear, validOwnerName, validCode);
        creditCardPage.errorFormat();
    }

    @Test
    @DisplayName("3.2 Валидация поля Номер карты - кириллица")
    public void paymentByRandomCardNumberCyrillic() {
        PaymentTypesPage page = new PaymentTypesPage();
        page.paymentTypesPage();
        var creditCardPage = page.creditPayment();
        var getRandomStringRu = DataHelper.getRandomStringRu(16);
        creditCardPage.cleanFields();
        creditCardPage.fillCardPaymentForm(getRandomStringRu, validMonth, validYear, validOwnerName, validCode);
        creditCardPage.errorFormat();
    }

    @Test
    @DisplayName("3.3 Валидация поля Номер карты - спецсимволы")
    public void paymentByRandomCardNumberSpec() {
        PaymentTypesPage page = new PaymentTypesPage();
        page.paymentTypesPage();
        var creditCardPage = page.creditPayment();
        var getRandomStringSpec = DataHelper.getRandomStringSpec(16);
        creditCardPage.cleanFields();
        creditCardPage.fillCardPaymentForm(getRandomStringSpec, validMonth, validYear, validOwnerName, validCode);
        creditCardPage.errorFormat();
    }

    @Test
    @DisplayName("3.4 Валидация поля Номер карты - меньше 16 цифр")
    public void paymentByShortCardNumber() {
        PaymentTypesPage page = new PaymentTypesPage();
        page.paymentTypesPage();
        var creditCardPage = page.creditPayment();
        var invalidCardNumber = DataHelper.getAShortNumber();
        creditCardPage.cleanFields();
        creditCardPage.fillCardPaymentForm(invalidCardNumber, validMonth, validYear, validOwnerName, validCode);
        creditCardPage.errorFormat();
    }

    @Test
    @DisplayName("3.6 Валидация поля Номер карты - нули")
    public void paymentByZeroCardNumber() {
        PaymentTypesPage page = new PaymentTypesPage();
        page.paymentTypesPage();
        var creditCardPage = page.creditPayment();
        var getInvalidNumber = DataHelper.zeroString(16);
        creditCardPage.cleanFields();
        creditCardPage.fillCardPaymentForm(getInvalidNumber, validMonth, validYear, validOwnerName, validCode);
        creditCardPage.bankDeclinedOperation();
    }

    @Test
    @DisplayName("3.7 Валидация поля Номер карты - пустое")
    public void paymentByEmptyCardNumber() {
        PaymentTypesPage page = new PaymentTypesPage();
        page.paymentTypesPage();
        var creditCardPage = page.creditPayment();
        var emptyCardNumber = DataHelper.getEmptyField();
        creditCardPage.cleanFields();
        creditCardPage.fillCardPaymentForm(emptyCardNumber, validMonth, validYear, validOwnerName, validCode);
        creditCardPage.errorFormat();
    }

    @Test
    @DisplayName("3.8 Валидация поля Номер карты. Ввод граничных значений - 15 цифр")
    public void paymentBy15DigitCardNumber() {
        PaymentTypesPage page = new PaymentTypesPage();
        page.paymentTypesPage();
        var creditCardPage = page.creditPayment();
        var getNumber15 = DataHelper.getRandomString(15);
        creditCardPage.cleanFields();
        creditCardPage.fillCardPaymentForm(getNumber15, validMonth, validYear, validOwnerName, validCode);
        creditCardPage.errorFormat();
    }

    @Test
    @DisplayName("3.10 Валидация поля Месяц - латиница")
    public void paymentByCardMonthLatin() {
        PaymentTypesPage page = new PaymentTypesPage();
        page.paymentTypesPage();
        var creditCardPage = page.creditPayment();
        var latinCardMonth = DataHelper.getRandomStringEn(2);
        creditCardPage.cleanFields();
        creditCardPage.fillCardPaymentForm(approvedCardNumber, latinCardMonth, validYear, validOwnerName, validCode);
        creditCardPage.errorFormat();
    }

    @Test
    @DisplayName("3.11 Валидация поля Месяц - кириллица")
    public void paymentByCardMonthRu() {
        PaymentTypesPage page = new PaymentTypesPage();
        page.paymentTypesPage();
        var creditCardPage = page.creditPayment();
        var ruCardMonth = DataHelper.getRandomStringRu(2);
        creditCardPage.cleanFields();
        creditCardPage.fillCardPaymentForm(approvedCardNumber, ruCardMonth, validYear, validOwnerName, validCode);
        creditCardPage.errorFormat();
    }

    @Test
    @DisplayName("3.12 Валидация поля Месяц - спецсимволы")
    public void paymentByCardMonthSpec() {
        PaymentTypesPage page = new PaymentTypesPage();
        page.paymentTypesPage();
        var creditCardPage = page.creditPayment();
        var cardMonthSpec = DataHelper.getRandomStringSpec(2);
        creditCardPage.cleanFields();
        creditCardPage.fillCardPaymentForm(approvedCardNumber, cardMonthSpec, validYear, validOwnerName, validCode);
        creditCardPage.errorFormat();
    }

    @Test
    @DisplayName("3.13 Валидация поля Месяц. Ввод граничных значений - одна цифра")
    public void paymentWithOneDigitMonth() {
        PaymentTypesPage page = new PaymentTypesPage();
        page.paymentTypesPage();
        var creditCardPage = page.creditPayment();
        var getInvalidRandomMonth = DataHelper.getRandomString(1);
        creditCardPage.cleanFields();
        creditCardPage.fillCardPaymentForm(approvedCardNumber, getInvalidRandomMonth, currentYar, validOwnerName, validCode);
        creditCardPage.errorFormat();
    }

    @Test
    @DisplayName("3.15 Валидация поля Месяц. Ввод граничных значений - нули")
    public void paymentWithMonthZero() {
        PaymentTypesPage page = new PaymentTypesPage();
        page.paymentTypesPage();
        var creditCardPage = page.creditPayment();
        var getInvalidMonth = DataHelper.zeroString(2);
        creditCardPage.cleanFields();
        creditCardPage.fillCardPaymentForm(approvedCardNumber, getInvalidMonth, currentYar, validOwnerName, validCode);
        creditCardPage.errorCardTermValidity();
    }

    @Test
    @DisplayName("3.16 Валидация поля Месяц. Ввод граничных значений - 11")
    public void paymentMonth11() {
        PaymentTypesPage page = new PaymentTypesPage();
        page.paymentTypesPage();
        var creditCardPage = page.creditPayment();
        var month11 = DataHelper.getMonth11();
        creditCardPage.cleanFields();
        creditCardPage.fillCardPaymentForm(approvedCardNumber, month11, validYear, validOwnerName, validCode);
        creditCardPage.bankApprovedOperation();
        Assertions.assertEquals("APPROVED", SQLHelper.getCreditPayment());
    }

    @Test
    @DisplayName("3.17 Валидация поля Месяц. Ввод граничных значений - 12")
    public void paymentMonth12() {
        PaymentTypesPage page = new PaymentTypesPage();
        page.paymentTypesPage();
        var creditCardPage = page.creditPayment();
        var month12 = DataHelper.getMonth12();
        creditCardPage.cleanFields();
        creditCardPage.fillCardPaymentForm(approvedCardNumber, month12, validYear, validOwnerName, validCode);
        creditCardPage.bankApprovedOperation();
        Assertions.assertEquals("APPROVED", SQLHelper.getCreditPayment());
    }

    @Test
    @DisplayName("3.18 Валидация поля Месяц. Ввод граничных значений - 13")
    public void paymentWithNonExistentMonth() {
        PaymentTypesPage page = new PaymentTypesPage();
        page.paymentTypesPage();
        var creditCardPage = page.creditPayment();
        var nonExistentMonth = DataHelper.getNonExistentMonth();
        creditCardPage.cleanFields();
        creditCardPage.fillCardPaymentForm(approvedCardNumber, nonExistentMonth, currentYar, validOwnerName, validCode);
        creditCardPage.errorCardTermValidity();
    }

    @Test
    @DisplayName("3.19 Валидация поля Год - латиница")
    public void paymentYearEn() {
        PaymentTypesPage page = new PaymentTypesPage();
        page.paymentTypesPage();
        var creditCardPage = page.creditPayment();
        var getInvalidYearEn = DataHelper.getRandomStringEn(2);
        creditCardPage.cleanFields();
        creditCardPage.fillCardPaymentForm(approvedCardNumber, validMonth, getInvalidYearEn, validOwnerName, validCode);
        creditCardPage.errorFormat();
    }

    @Test
    @DisplayName("3.20 Валидация поля Год - кириллица")
    public void paymentYearRu() {
        PaymentTypesPage page = new PaymentTypesPage();
        page.paymentTypesPage();
        var creditCardPage = page.creditPayment();
        var getInvalidYearRu = DataHelper.getRandomStringRu(2);
        creditCardPage.cleanFields();
        creditCardPage.fillCardPaymentForm(approvedCardNumber, validMonth, getInvalidYearRu, validOwnerName, validCode);
        creditCardPage.errorFormat();
    }

    @Test
    @DisplayName("3.21 Валидация поля Год - спецсимволы")
    public void paymentYearSpec() {
        PaymentTypesPage page = new PaymentTypesPage();
        page.paymentTypesPage();
        var creditCardPage = page.creditPayment();
        var getInvalidYearSpec = DataHelper.getRandomStringSpec(2);
        creditCardPage.cleanFields();
        creditCardPage.fillCardPaymentForm(approvedCardNumber, validMonth, getInvalidYearSpec, validOwnerName, validCode);
        creditCardPage.errorFormat();
    }

    @Test
    @DisplayName("3.22 Валидация поля Год. Ввод граничных значений - нули")
    public void paymentYearZero() {
        PaymentTypesPage page = new PaymentTypesPage();
        page.paymentTypesPage();
        var creditCardPage = page.creditPayment();
        var getInvalidYearZero = DataHelper.zeroString(2);
        creditCardPage.cleanFields();
        creditCardPage.fillCardPaymentForm(approvedCardNumber, validMonth, getInvalidYearZero, validOwnerName, validCode);
        creditCardPage.termValidityExpired();
    }

    @Test
    @DisplayName("3.23 Валидация поля Год. Ввод граничных значений - одна цифра")
    public void paymentWithOneDigitOfTheYear() {
        PaymentTypesPage page = new PaymentTypesPage();
        page.paymentTypesPage();
        var creditCardPage = page.creditPayment();
        var getInvalidRandomYear = DataHelper.getRandomString(1);
        creditCardPage.cleanFields();
        creditCardPage.fillCardPaymentForm(approvedCardNumber, validMonth, getInvalidRandomYear, validOwnerName, validCode);
        creditCardPage.errorFormat();
    }

    @Test
    @DisplayName("3.25 Валидация поля Год. Ввод граничных значений - текущий месяц и год")
    public void paymentWithCurrentCardDate() {
        PaymentTypesPage page = new PaymentTypesPage();
        page.paymentTypesPage();
        var creditCardPage = page.creditPayment();
        var monthExpired = DataHelper.getRandomMonth(0);
        creditCardPage.cleanFields();
        creditCardPage.fillCardPaymentForm(approvedCardNumber, monthExpired, currentYar, validOwnerName, validCode);
        creditCardPage.bankApprovedOperation();
        Assertions.assertEquals("APPROVED", SQLHelper.getCreditPayment());
    }

    @Test
    @DisplayName("3.26 Валидация поля Год. Ввод граничных значений -  истекший месяц текущий год")
    public void paymentWithExpiredCardMonth() {
        PaymentTypesPage page = new PaymentTypesPage();
        page.paymentTypesPage();
        var creditCardPage = page.creditPayment();
        var monthExpired = DataHelper.getRandomMonth(-1);
        creditCardPage.cleanFields();
        creditCardPage.fillCardPaymentForm(approvedCardNumber, monthExpired, currentYar, validOwnerName, validCode);
        creditCardPage.errorCardTermValidity();
    }

    @Test
    @DisplayName("3.27 Валидация поля Год. Ввод граничных значений -  следующий месяц текущий год")
    public void paymentWithCardMonthNext() {
        PaymentTypesPage page = new PaymentTypesPage();
        page.paymentTypesPage();
        var creditCardPage = page.creditPayment();
        var monthExpired = DataHelper.getRandomMonth(+1);
        creditCardPage.cleanFields();
        creditCardPage.fillCardPaymentForm(approvedCardNumber, monthExpired, currentYar, validOwnerName, validCode);
        creditCardPage.bankApprovedOperation();
        Assertions.assertEquals("APPROVED", SQLHelper.getCreditPayment());
    }

    @Test
    @DisplayName("Валидация поля Год истекший год")
    public void paymentWithExpiredYear() {
        PaymentTypesPage page = new PaymentTypesPage();
        page.paymentTypesPage();
        var creditCardPage = page.creditPayment();
        var expiredYear = DataHelper.getRandomYear(-1);
        creditCardPage.cleanFields();
        creditCardPage.fillCardPaymentForm(approvedCardNumber, validMonth, expiredYear, validOwnerName, validCode);
        creditCardPage.termValidityExpired();
    }

    @Test
    @DisplayName("3.28 Валидация поля Владелец - кириллица")
    public void paymentOwnerCyrillic() {
        PaymentTypesPage page = new PaymentTypesPage();
        page.paymentTypesPage();
        var creditCardPage = page.creditPayment();
        var rusLanguageName = DataHelper.getRandomNameRus();
        creditCardPage.cleanFields();
        creditCardPage.fillCardPaymentForm(approvedCardNumber, validMonth, validYear, rusLanguageName, validCode);
        creditCardPage.errorFormat();
    }

    @Test
    @DisplayName("3.29 Валидация поля Владелец - спец символы")
    public void paymentOwnerSpecialCharacters() {
        PaymentTypesPage page = new PaymentTypesPage();
        page.paymentTypesPage();
        var creditCardPage = page.creditPayment();
        var specSymbolsName = DataHelper.getSpecialCharactersName();
        creditCardPage.cleanFields();
        creditCardPage.fillCardPaymentForm(approvedCardNumber, validMonth, validYear, specSymbolsName, validCode);
        creditCardPage.errorFormat();
    }

    @Test
    @DisplayName("3.30 Валидация поля Владелец - цифры")
    public void ownerPaymentInNumbers() {
        PaymentTypesPage page = new PaymentTypesPage();
        page.paymentTypesPage();
        var creditCardPage = page.creditPayment();
        var digitsName = DataHelper.getNumberName();
        creditCardPage.cleanFields();
        creditCardPage.fillCardPaymentForm(approvedCardNumber, validMonth, validYear, digitsName, validCode);
        creditCardPage.errorFormat();
    }

    @Test
    @DisplayName("Валидация поля Владелец - пустое поле")
    public void paymentOwnerEmpty() {
        PaymentTypesPage page = new PaymentTypesPage();
        page.paymentTypesPage();
        var creditCardPage = page.creditPayment();
        var emptyName = DataHelper.getEmptyField();
        creditCardPage.cleanFields();
        creditCardPage.fillCardPaymentForm(approvedCardNumber, validMonth, validYear, emptyName, validCode);
        creditCardPage.emptyField();
    }

    @Test
    @DisplayName("3.31 Валидация поля Владелец. Ввод граничных значений - одно слово")
    public void paymentOwnerName() {
        PaymentTypesPage page = new PaymentTypesPage();
        page.paymentTypesPage();
        var creditCardPage = page.creditPayment();
        var InvalidOwner = DataHelper.getInvalidOwner();
        creditCardPage.cleanFields();
        creditCardPage.fillCardPaymentForm(approvedCardNumber, validMonth, validYear, InvalidOwner, validCode);
        creditCardPage.errorFormat();
    }

    @Test
    @DisplayName("3.32 Валидация поля Владелец. Ввод граничных значений - по одной букве")
    public void paymentOwnerInValid() {
        PaymentTypesPage page = new PaymentTypesPage();
        page.paymentTypesPage();
        var creditCardPage = page.creditPayment();
        var inValidName = DataHelper.getInValidName(3);
        creditCardPage.cleanFields();
        creditCardPage.fillCardPaymentForm(approvedCardNumber, validMonth, validYear, inValidName, validCode);
        creditCardPage.errorFormat(); //баг успешно
    }

    @Test
    @DisplayName("3.33 Валидация поля CVC - латиница")
    public void paymentInCodeEn() {
        PaymentTypesPage page = new PaymentTypesPage();
        page.paymentTypesPage();
        var creditCardPage = page.creditPayment();
        var codeEn = DataHelper.getRandomStringEn(3);
        creditCardPage.cleanFields();
        creditCardPage.fillCardPaymentForm(approvedCardNumber, validMonth, validYear, validOwnerName, codeEn);
        creditCardPage.errorFormat();
    }

    @Test
    @DisplayName("3.34 Валидация поля CVC - кириллица")
    public void paymentInCodeRu() {
        PaymentTypesPage page = new PaymentTypesPage();
        page.paymentTypesPage();
        var creditCardPage = page.creditPayment();
        var codeRu = DataHelper.getRandomStringRu(3);
        creditCardPage.cleanFields();
        creditCardPage.fillCardPaymentForm(approvedCardNumber, validMonth, validYear, validOwnerName, codeRu);
        creditCardPage.errorFormat();
    }

    @Test
    @DisplayName("3.35 Валидация поля CVC - спецсимволы")
    public void paymentSymbolsInCode() {
        PaymentTypesPage page = new PaymentTypesPage();
        page.paymentTypesPage();
        var creditCardPage = page.creditPayment();
        var specSymbolsCVC = DataHelper.getSpecialCharactersName();
        creditCardPage.cleanFields();
        creditCardPage.fillCardPaymentForm(approvedCardNumber, validMonth, validYear, validOwnerName, specSymbolsCVC);
        creditCardPage.errorFormat();
    }

    @Test
    @DisplayName("3.36 Валидация поля CVC - пустое поле")
    public void paymentCodeEmpty() {
        PaymentTypesPage page = new PaymentTypesPage();
        page.paymentTypesPage();
        var creditCardPage = page.creditPayment();
        var emptyCVC = DataHelper.getEmptyField();
        creditCardPage.cleanFields();
        creditCardPage.fillCardPaymentForm(approvedCardNumber, validMonth, validYear, validOwnerName, emptyCVC);
        creditCardPage.errorFormat();
    }

    @Test
    @DisplayName("3.37 Валидация поля CVC. Граничные значения - две цифры")
    public void paymentTwoDigitsInTheCode() {
        PaymentTypesPage page = new PaymentTypesPage();
        page.paymentTypesPage();
        var creditCardPage = page.creditPayment();
        var twoDigitCVC = DataHelper.getNumberCVC(2);
        creditCardPage.cleanFields();
        creditCardPage.fillCardPaymentForm(approvedCardNumber, validMonth, validYear, validOwnerName, twoDigitCVC);
        creditCardPage.errorFormat();
    }

    @Test
    @DisplayName("3.39 Валидация поля CVC. Граничные значения - нули")
    public void paymentInCodeZero() {
        PaymentTypesPage page = new PaymentTypesPage();
        page.paymentTypesPage();
        var creditCardPage = page.creditPayment();
        var codeZero = DataHelper.zeroString(3);
        creditCardPage.cleanFields();
        creditCardPage.fillCardPaymentForm(approvedCardNumber, validMonth, validYear, validOwnerName, codeZero);
        creditCardPage.errorFormat();
    }


}
