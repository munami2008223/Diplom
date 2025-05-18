package ru.netology.test;

import com.codeborne.selenide.logevents.SelenideLogger;

import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.*;
import ru.netology.data.DataHelper;
import ru.netology.data.SQLHelper;
import ru.netology.page.PaymentTypesPage;


import static com.codeborne.selenide.Selenide.open;

public class CreditCardTest {

    String approvedCardNumber = DataHelper.getCardApproved().getCardNumber();//одобренная карта
    String declinedCardNumber = DataHelper.getCardDeclined().getCardNumber();//отклоненная карта
    String validMonth = DataHelper.getRandomMonth(1);//валидный месяц
    String validYear = DataHelper.getRandomYear(1);//валидный год
    String validOwnerName = DataHelper.getRandomName();//валидное имя
    String validCode = DataHelper.getNumberCVC(3);//валидный CVC
    String currentYar = DataHelper.getRandomYear(0);

    @BeforeEach
    public void setUp() {
        open("http://localhost:8080");
    }

    @AfterEach
    public void shouldCleanBase() {
        SQLHelper.сleanBase();//очистка базы
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
    public void shouldPaymentApproved() {//одобренная карта, валидное заполнение полей
        PaymentTypesPage page = new PaymentTypesPage();
        page.paymentTypesPage();
        var creditCardPage = page.creditPayment();
        creditCardPage.cleanFields();
        creditCardPage.fillCardPaymentForm(approvedCardNumber, validMonth, validYear, validOwnerName, validCode);
        creditCardPage.bankApprovedOperation();
        Assertions.assertEquals("APPROVED", SQLHelper.getCreditPayment());
    }

    @Test
    public void shouldDeclinedCardPayment() {//неодобренная карта, валидное заполнение полей
        PaymentTypesPage page = new PaymentTypesPage();
        page.paymentTypesPage();
        var creditCardPage = page.creditPayment();
        creditCardPage.cleanFields();
        creditCardPage.fillCardPaymentForm(declinedCardNumber, validMonth, validYear, validOwnerName, validCode);
        creditCardPage.bankDeclinedOperation();
        Assertions.assertEquals("Declined", SQLHelper.getCreditPayment());
    }

    @Test
    public void paymentByRandomCardNumber() {//рандомный номер карты, валидное заполнение полей
        PaymentTypesPage page = new PaymentTypesPage();
        page.paymentTypesPage();
        var creditCardPage = page.creditPayment();
        var getRandomCardNumber = DataHelper.getRandomCardNumber();
        creditCardPage.cleanFields();
        creditCardPage.fillCardPaymentForm(getRandomCardNumber, validMonth, validYear, validOwnerName, validCode);
        creditCardPage.bankDeclinedOperation();
        //банк отказал в операции
    }

    //Тестирование поля номера карты
    @Test
    public void paymentByShortCardNumber() {
        PaymentTypesPage page = new PaymentTypesPage();
        page.paymentTypesPage();
        var creditCardPage = page.creditPayment();

        // Test invalid card number
        var invalidCardNumber = DataHelper.GetAShortNumber(); //невалидный номер карты -  меньше 16
        creditCardPage.cleanFields();
        creditCardPage.fillCardPaymentForm(invalidCardNumber, validMonth, validYear, validOwnerName, validCode);
        creditCardPage.errorFormat();
    }

    @Test
    public void paymentByZeroCardNumber() {
        PaymentTypesPage page = new PaymentTypesPage();
        page.paymentTypesPage();
        var creditCardPage = page.creditPayment();

        // Test invalid card number
        var getInvalidNumner = DataHelper.getInvalidNumner(); //невалидный номер карты -  нули
        creditCardPage.cleanFields();
        creditCardPage.fillCardPaymentForm(getInvalidNumner, validMonth, validYear, validOwnerName, validCode);
        creditCardPage.bankDeclinedOperation();
    }

    @Test
    public void paymentByEmptyCardNumber() { // пустое поле номера карты
        PaymentTypesPage page = new PaymentTypesPage();
        page.paymentTypesPage();
        var creditCardPage = page.creditPayment();
        // Test empty card number
        var emptyCardNumber = DataHelper.getEmptyField();
        creditCardPage.cleanFields();
        creditCardPage.fillCardPaymentForm(emptyCardNumber, validMonth, validYear, validOwnerName, validCode);
        creditCardPage.errorFormat();
    }

    //Тестирование поля месяца карты
    @Test
    public void paymentWithExpiredCardMonth() { // истекший месяц
        PaymentTypesPage page = new PaymentTypesPage();
        page.paymentTypesPage();
        var creditCardPage = page.creditPayment();
        // Test expired card month
        var monthExpired = DataHelper.getRandomMonth(-1);
        creditCardPage.cleanFields();
        creditCardPage.fillCardPaymentForm(approvedCardNumber, monthExpired, currentYar, validOwnerName, validCode);
        creditCardPage.errorCardTermValidity();
    }

    @Test
    public void paymentWithCurrentCardDate() { // текущий месяц и год
        PaymentTypesPage page = new PaymentTypesPage();
        page.paymentTypesPage();
        var creditCardPage = page.creditPayment();
        // Test expired card month
        var monthExpired = DataHelper.getRandomMonth(0);
        creditCardPage.cleanFields();
        creditCardPage.fillCardPaymentForm(approvedCardNumber, monthExpired, currentYar, validOwnerName, validCode);
        creditCardPage.bankApprovedOperation();
        Assertions.assertEquals("APPROVED", SQLHelper.getCreditPayment());
    }

    @Test
    public void paymentWithOneDigitMonth() { // одна цифра в месяце
        PaymentTypesPage page = new PaymentTypesPage();
        page.paymentTypesPage();
        var creditCardPage = page.creditPayment();
        // Test expired card month
        var getInvalidRandomMonth = DataHelper.getInvalidRandomMonth(0);
        creditCardPage.cleanFields();
        creditCardPage.fillCardPaymentForm(approvedCardNumber, getInvalidRandomMonth, currentYar, validOwnerName, validCode);
        creditCardPage.errorFormat();
    }

    @Test
    public void paymentWithMonthZero() { // нули в месяце
        PaymentTypesPage page = new PaymentTypesPage();
        page.paymentTypesPage();
        var creditCardPage = page.creditPayment();
        // Test expired card month
        var getInvalidMonth = DataHelper.getInvalidMonth();
        creditCardPage.cleanFields();
        creditCardPage.fillCardPaymentForm(approvedCardNumber, getInvalidMonth, currentYar, validOwnerName, validCode);
        creditCardPage.errorCardTermValidity();
    }

    @Test
    public void paymentWithNonExistentMonth() { // 13 месяц
        PaymentTypesPage page = new PaymentTypesPage();
        page.paymentTypesPage();
        var creditCardPage = page.creditPayment();
        // Test expired card month
        var getNonExistentMonth = DataHelper.getNonExistentMonth();
        creditCardPage.cleanFields();
        creditCardPage.fillCardPaymentForm(approvedCardNumber, getNonExistentMonth, currentYar, validOwnerName, validCode);
        creditCardPage.errorCardTermValidity();
    }

    //Тестирование поля года карты
    @Test
    public void paymentWithExpiredYear() { // истекший год
        PaymentTypesPage page = new PaymentTypesPage();
        page.paymentTypesPage();
        var creditCardPage = page.creditPayment();
        // Test expired card year
        var expiredYear = DataHelper.getRandomYear(-1);
        creditCardPage.cleanFields();
        creditCardPage.fillCardPaymentForm(approvedCardNumber, validMonth, expiredYear, validOwnerName, validCode);
        creditCardPage.termValidityExpired();
    }

    @Test
    public void paymentWithOneDigitOfTheYear() { // одна цифра года
        PaymentTypesPage page = new PaymentTypesPage();
        page.paymentTypesPage();
        var creditCardPage = page.creditPayment();
        // Test expired card year
        var getInvalidRandomYear = DataHelper.getInvalidRandomYear(0);
        creditCardPage.cleanFields();
        creditCardPage.fillCardPaymentForm(approvedCardNumber, validMonth, getInvalidRandomYear, validOwnerName, validCode);
        creditCardPage.errorFormat();
    }

    //Тестирование поля владелец карты
    @Test
    public void paymentOwnerCyrillic() {//кириллица
        PaymentTypesPage page = new PaymentTypesPage();
        page.paymentTypesPage();
        var creditCardPage = page.creditPayment();

        // Test Russian name
        var rusLanguageName = DataHelper.getRandomNameRus();
        creditCardPage.cleanFields();
        creditCardPage.fillCardPaymentForm(approvedCardNumber, validMonth, validYear, rusLanguageName, validCode);
        creditCardPage.errorFormat(); //баг успешно
    }

    @Test
    public void paymentOwnerName() {//только имя
        PaymentTypesPage page = new PaymentTypesPage();
        page.paymentTypesPage();
        var creditCardPage = page.creditPayment();

        var InvalidOwner = DataHelper.getInvalidOwner();
        creditCardPage.cleanFields();
        creditCardPage.fillCardPaymentForm(approvedCardNumber, validMonth, validYear, InvalidOwner, validCode);
        creditCardPage.errorFormat(); //баг операция одобрена
    }

    @Test
    public void ownerPaymentInNumbers() {//цифры в имени
        PaymentTypesPage page = new PaymentTypesPage();
        page.paymentTypesPage();
        var creditCardPage = page.creditPayment();
        // Test digits in name
        var digitsName = DataHelper.getNumberName();
        creditCardPage.cleanFields();
        creditCardPage.fillCardPaymentForm(approvedCardNumber, validMonth, validYear, digitsName, validCode);
        creditCardPage.errorFormat();//баг операция одобрена
    }

    @Test
    public void paymentOwnerSpecialCharacters() {//спец символы в имени
        PaymentTypesPage page = new PaymentTypesPage();
        page.paymentTypesPage();
        var creditCardPage = page.creditPayment();

        // Test special symbols in name
        var specSymbolsName = DataHelper.getSpecialCharactersName();
        creditCardPage.cleanFields();
        creditCardPage.fillCardPaymentForm(approvedCardNumber, validMonth, validYear, specSymbolsName, validCode);
        creditCardPage.errorFormat();//баг операция одобрена
    }

    @Test
    public void paymentOwnerEmpty() {//пустое поле в имени
        PaymentTypesPage page = new PaymentTypesPage();
        page.paymentTypesPage();
        var creditCardPage = page.creditPayment();
        // Test empty name
        var emptyName = DataHelper.getEmptyField();
        creditCardPage.cleanFields();
        creditCardPage.fillCardPaymentForm(approvedCardNumber, validMonth, validYear, emptyName, validCode);
        creditCardPage.emptyField();
    }

    //Тестирование поля CVC карты
    @Test
    public void spaymentTwoDigitsInTheCode() {//две цифры в коде
        PaymentTypesPage page = new PaymentTypesPage();
        page.paymentTypesPage();
        var creditCardPage = page.creditPayment();

        // Test two-digit CVC
        var twoDigitCVC = DataHelper.getNumberCVC(2);
        creditCardPage.cleanFields();
        creditCardPage.fillCardPaymentForm(approvedCardNumber, validMonth, validYear, validOwnerName, twoDigitCVC);
        creditCardPage.errorFormat();
    }

    @Test
    public void paymentOneDigitInTheCode() {//одна цифра в коде
        PaymentTypesPage page = new PaymentTypesPage();
        page.paymentTypesPage();
        var creditCardPage = page.creditPayment();
        // Test one-digit CVC
        var oneDigitCVC = DataHelper.getNumberCVC(1);
        creditCardPage.cleanFields();
        creditCardPage.fillCardPaymentForm(approvedCardNumber, validMonth, validYear, validOwnerName, oneDigitCVC);
        creditCardPage.errorFormat();
    }

    @Test
    public void paymentCodeEmpty() {//пустое поле кода
        PaymentTypesPage page = new PaymentTypesPage();
        page.paymentTypesPage();
        var creditCardPage = page.creditPayment();
        // Test empty CVC
        var emptyCVC = DataHelper.getEmptyField();
        creditCardPage.cleanFields();
        creditCardPage.fillCardPaymentForm(approvedCardNumber, validMonth, validYear, validOwnerName, emptyCVC);
        creditCardPage.errorFormat();//при валидном имени подчеркивается и надпись поле обязательно для заполнения
    }

    @Test
    public void paymentSymbolsInCode() {//символы в коде
        PaymentTypesPage page = new PaymentTypesPage();
        page.paymentTypesPage();
        var creditCardPage = page.creditPayment();
        // Test special symbols in CVC
        var specSymbolsCVC = DataHelper.getSpecialCharactersName();
        creditCardPage.cleanFields();
        creditCardPage.fillCardPaymentForm(approvedCardNumber, validMonth, validYear, validOwnerName, specSymbolsCVC);
        creditCardPage.errorFormat();//при валидном имени подчеркивается и надпись поле обязательно для заполнения, символы не вводятся
    }

    @Test
    public void paymentEmptyForm() {//пустая форма
        PaymentTypesPage page = new PaymentTypesPage();
        page.paymentTypesPage();
        var creditCardPage = page.creditPayment();
        var emptyField = DataHelper.getEmptyField();
        creditCardPage.cleanFields();
        creditCardPage.fillCardPaymentForm(emptyField, emptyField, emptyField, emptyField, emptyField);
        creditCardPage.errorFormat();
    }
}