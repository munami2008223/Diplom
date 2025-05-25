package ru.netology.data;

import com.github.javafaker.Faker;

import lombok.Value;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Random;


public class DataHelper {
    private static final Faker faker = new Faker(new Locale("en"));
    private static final Faker fakerRus = new Faker(new Locale("ru"));

    private DataHelper() {
    }

    public static CardInfo getCardApproved() {//номер карты, внесенной в базу
        return new CardInfo("4444 4444 4444 4441", "APPROVED");

    }

    public static CardInfo getCardDeclined() {//номер заблокированной карты

        return new CardInfo("4444 4444 4444 4442", "DECLINED");
    }

    public static String getRandomCardNumber() {//рандомный номер карты
        return faker.number().digits(16);

    }

    public static String getAShortNumber() {//невалидный короткий номер карты
        int shortNumber = faker.random().nextInt(16);
        return faker.number().digits(shortNumber);
    }


    public static String getRandomMonth(int month) {//рандомный валидный месяц
        return LocalDate.now().plusMonths(month).format(DateTimeFormatter.ofPattern("MM"));
    }


    public static String getRandomYear(int year) {//рандомный валидный год
        return LocalDate.now().plusYears(year).format(DateTimeFormatter.ofPattern("yy"));

    }


    public static String zeroString(int length) {//заполнение поля нулями

        String zeroString = String.format("%0" + length + "d", 0);

        return zeroString;
    }

    public static String getNonExistentMonth() {


        return "13";
    }

    public static String getMonth11() {

        return "11";
    }

    public static String getMonth12() {

        return "12";
    }

    public static String getRandomName() {//рандомные фамилия + имя на латинице

        return faker.name().fullName();
    }

    public static String getInvalidOwner() {//рандомное только имя на латинице
        Faker faker = new Faker((new Locale("en")));
        return faker.name().firstName();
    }

    public static String getRandomNameRus() {//рандомные фамилия+имя на кириллице
        Faker faker = new Faker((new Locale("ru")));
        return faker.name().firstName() + " " + faker.name().lastName();
    }

    public static String getNumberName() {//цифры в имени

        return faker.number().digit();
    }

    public static String getNumberCVC(int code) {//рандомный валидный код

        return faker.number().digits(code);
    }

    public static String getSpecialCharactersName() {//спецсимволы в имени

        return "&^%*&^";
    }

    public static String getEmptyField() {//пустая форма
        return "";
    }

    @Value
    public static class CardInfo {
        public String cardNumber;
        public String Status;

    }

    public static String getRandomStringEn(int length) {//рандомная строка из латинских символов
        String characters = "abcdefghijklmnopqrstuvwxyz";
        Random random = new Random();
        StringBuilder stringBuilder = new StringBuilder();

        for (int i = 0; i < length; i++) {
            int randomIndex = random.nextInt(characters.length());
            stringBuilder.append(characters.charAt(randomIndex));
        }

        return stringBuilder.toString();
    }

    public static String getRandomStringRu(int length) {// рандомная строка из символов на кириллице
        String characters = "абвгдежзийклмнопрстуфхцчшщъыьэюя";
        Random random = new Random();
        StringBuilder stringBuilder = new StringBuilder();

        for (int i = 0; i < length; i++) {
            int randomIndex = random.nextInt(characters.length());
            stringBuilder.append(characters.charAt(randomIndex));
        }

        return stringBuilder.toString();
    }

    public static String getRandomStringSpec(int length) {//рандомная строка из спецсимволов
        String characters = "@#%^&*";
        Random random = new Random();
        StringBuilder stringBuilder = new StringBuilder();

        for (int i = 0; i < length; i++) {
            int randomIndex = random.nextInt(characters.length());
            stringBuilder.append(characters.charAt(randomIndex));
        }

        return stringBuilder.toString();
    }

    public static String getInValidName(int length) {//рандомная строка из символа, пробела и символа
        Random random = new Random();
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < length; i++) {
            if (i % 2 == 0) {
                sb.append((char) ('a' + random.nextInt(26)));
            } else {
                sb.append(" "); // Пробел
            }
        }
        return sb.toString();
    }

    public static String getRandomString(int length) {//рандомная строка из цифр
        String characters = "123456789";
        Random random = new Random();
        StringBuilder stringBuilder = new StringBuilder();

        for (int i = 0; i < length; i++) {
            int randomIndex = random.nextInt(characters.length());
            stringBuilder.append(characters.charAt(randomIndex));
        }

        return stringBuilder.toString();
    }

}

