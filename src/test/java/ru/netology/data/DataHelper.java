package ru.netology.data;

import com.github.javafaker.Faker;
import com.github.javafaker.service.FakeValuesService;
import com.github.javafaker.service.RandomService;
import lombok.Value;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;


public class DataHelper {
    private static Faker faker = new Faker(new Locale("en"));
    private static Faker fakerRus = new Faker(new Locale("ru"));

    private DataHelper() {
    }

    public static CardInfo getCardApproved() {
        return new CardInfo("4444 4444 4444 4441", "APPROVED");

    }

    public static CardInfo getCardDeclined() {

        return new CardInfo("4444 4444 4444 4442", "DECLINED");
    }

    public static String getRandomCardNumber() {
        return faker.business().creditCardNumber();

    }

    public static String GetAShortNumber() {
        int shortNumber = faker.random().nextInt(16);
        return faker.number().digits(shortNumber);
    }


    public static String getInvalidNumner() {
        return "0000000000000000";
    }
    public static String getRandomMonth(int month) {
        return LocalDate.now().plusMonths(month).format(DateTimeFormatter.ofPattern("MM"));
    }
    public static String getInvalidRandomMonth (int month) {
        FakeValuesService fakeValuesService = new FakeValuesService(new Locale("en"), new RandomService());
        return fakeValuesService.numerify("#");

    }
    public static String getInvalidRandomYear (int year) {
        FakeValuesService fakeValuesService = new FakeValuesService(new Locale("en"), new RandomService());
        return fakeValuesService.numerify("#");

    }


    public static String getRandomYear(int year) {
        return LocalDate.now().plusYears(year).format(DateTimeFormatter.ofPattern("yy"));

    }

    public static String getInvalidMonth() {

        return "00";
    }
    public static String getNonExistentMonth() {

        return "13";
    }

    public static String getRandomName() {

        return faker.name().fullName();
    }
    public static String getInvalidOwner() {
        Faker faker = new Faker((new Locale("en")));
        return faker.name().firstName();
    }

    public static String getRandomNameRus() {

        return fakerRus.name().fullName();
    }

    public static String getNumberName() {

        return faker.number().digit();
    }

    public static String getNumberCVC(int code) {

        return faker.number().digits(code);
    }

    public static String getSpecialCharactersName() {

        return "&^%*&^";
    }

    public static String getEmptyField() {
        return "";
    }

    @Value
    public static class CardInfo {
        public String cardNumber;
        public String Status;

    }

}

