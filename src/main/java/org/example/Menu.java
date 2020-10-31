package org.example;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Menu implements MenuInterface {
    private String name;
    private List<Menu> menuList = new ArrayList<>();
    private Action<Context> action;

    public Menu(String name) {
        this.name = name;
        action = null;
    }

    public Menu(String name, Action<Context> action) {
        this.name = name;
        this.action = action;
    }

    @Override
    public void make(Context context) throws IOException, SQLException {
        action.doIt(context);
    }

    @Override
    public void remove(Menu menu) {

    }

    @Override
    public void addSubMenu(Menu sabMenu) {
        menuList.add(sabMenu);
    }

    public void print() {
        for (int i = 0; i < menuList.size(); i++) {
            System.out.println((i + 1) + " - " + menuList.get(i).name);
        }
    }

    public boolean action() throws IOException, SQLException {
        boolean status = false;
        Scanner scanner = new Scanner(System.in);
        String action;
        int actionInt = 0;
        System.out.println("exit - выход");
        System.out.println("Выберите действие");
        do {
            try {
                action = scanner.nextLine();
                if ("exit".equals(action)) {
                    status = true;
                    break;
                }
                actionInt = Integer.parseInt(action);
                break;
            } catch (Exception e) {
                System.out.println("Введите правильно действие");
            }
        } while (true);

        for (int i = 0; i < menuList.size(); i++) {
            if ((actionInt - 1) == i) {
                menuList.get(i).make(null);
            }
        }
        return status;
    }

    @Override
    public String toString() {
        return "Menu{" +
                "name='" + name + '\'' +
                '}';
    }
}
