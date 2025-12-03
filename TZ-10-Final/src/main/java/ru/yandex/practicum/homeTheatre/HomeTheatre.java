package ru.yandex.practicum.homeTheatre;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Map;
import java.util.Scanner;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
@SpringBootApplication
public class HomeTheatre {
    public static void main(String[] args) {
        SpringApplication.run(HomeTheatre.class, args);
        final Gson gson = new Gson();
        final Scanner scanner = new Scanner(System.in);
        System.out.print("Введите JSON => ");
        final String input = scanner.nextLine();
        try {
            gson.fromJson(input, Map.class);
            System.out.println("Был введён корректный JSON");
        } catch (JsonSyntaxException exception) {
            System.out.println("Был введён некорректный JSON");
        }
    }
}
