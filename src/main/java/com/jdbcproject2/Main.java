package com.jdbcproject2;

import com.jdbcproject2.model.Habit;
import com.jdbcproject2.model.User;
import com.jdbcproject2.repository.DbUtils;
import com.jdbcproject2.repository.HabitRepository;
import com.jdbcproject2.repository.UserRepository;

import java.util.Objects;
import java.util.Scanner;


public class Main {

    private static User loggedUser = null;
    private static Scanner scan = new Scanner(System.in);


    public static void main(String[] args) {

        DbUtils.createDatabase();
        DbUtils.createTables();

        System.out.println("Welcome to habits app!\n");

        while (true) {
            if (loggedUser == null) {
                mainMenu();
                int input = scan.nextInt();
                scan.nextLine();

                if (input == 1) {
                    // login
                    login();
                } else if (input == 2) {
                    // register
                    register();
                } else if (input == 0) {
                    // exit the app
                    System.out.println("До скорой встречи!");
                    break;
                } else {
                    System.out.println("\n\tВы ввели неверное значение");
                }
            } else {
                userMenu();
            }
        }

    }

    private static void userMenu() {
        int input = -1;
        String inputStr = null;

        System.out.println("Welcome " + loggedUser.toString());
        do {
            System.out.println("Press 0 - Logout");
            System.out.println("Press 1 - Настройка аккаунта");
            System.out.println("Press 2 - Привычки");

            while (true) {
                try {
                    inputStr = scan.nextLine().trim();
                    input = Integer.parseInt(inputStr);
                    if (input == 0 || input == 1 || input == 2) {
                        break;
                    } else {
                        System.out.println("\n\tВы ввели неверное значение");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("\n\tВы ввели неверное значение");
                }
            }
            // logout
            if (input == 0) {
                loggedUser = null;
                // настройка аккаунта
            } else if (input == 1) {
                accountSettings();
                // привычки
            } else if (input == 2) {
                // todo работает тут
                habitMenu();
            }


        } while (loggedUser != null);
    }

    // part of user menu
    private static void habitMenu() {
        String inputStr = null;
        int input = -1;
        do {
            System.out.println("\nPress 0 - Назад");
            System.out.println("Press 1 - Показать список привычек");
            System.out.println("Press 2 - Добавить привычку");
            System.out.println("Press 3 - Редактировать привычку");
            System.out.println("Press 4 - Удалить привычку");

            try {
                inputStr = scan.nextLine();
                input = Integer.parseInt(inputStr);

                if (input == 0) {
                    break;
                } else if (input == 1) {
                    
                    do {
                        System.out.println("Press 0 - Назад");
                        System.out.println("Press 1 - Фильтровать привычки по статусу выполнения");
                        System.out.println("Press 2 - Фильтровать привычки по дате создания");
                        try {
                            inputStr = scan.nextLine();
                            input = Integer.parseInt(inputStr);

                            if (input == 0) {
                                break;
                            } else if (input == 1) {
                                loggedUser.habits = HabitRepository.getAllHabits(loggedUser.getId(), "выполнения");

                                for (Habit habit : loggedUser.habits) {
                                    System.out.println(habit.toString());
                                }
                            } else if (input == 2) {
                                loggedUser.habits = HabitRepository.getAllHabits(loggedUser.getId(), "создания");
                                for (Habit habit : loggedUser.habits) {
                                    System.out.println(habit.toString());
                                }
                            } else {
                                System.out.println("\n\tВы ввели некорректные данные. Попробуйте еще раз");
                            }
                        } catch (NumberFormatException e) {
                            System.out.println("\n\tВы ввели некорректные данные. Попробуйте еще раз");
                        }
                    } while (input == 0 || input == 1 || input == 2);

                } else if (input == 2) {
                    Habit habit = new Habit();
                    String title = null;
                    String description = null;
                    String frequency = null;
                    boolean isCompleted = false;
                    int userId = loggedUser.getId();


                    System.out.println("Введите имя привычки");
                    do {
                        title = scan.nextLine().trim();
                    } while (title.isEmpty());

                    System.out.println("Введите описание");
                    description = scan.nextLine().trim();

                    System.out.println("Введите частоту ежедневно/еженедельно");
                    do {
                        frequency = scan.nextLine().trim();
                        if (frequency.equals("ежедневно") || frequency.equals("еженедельно")) {
                            break;
                        } else {
                            System.out.println("\t\nВыберите между ежедневно/еженедельно");
                        }
                    } while (true);

                    System.out.println("Выполнена сегодня? да/нет");

                    do {
                        String completed = scan.nextLine().trim();
                        if (completed.matches("да") || completed.matches("нет")) {
                            isCompleted = true;
                            break;
                        } else {
                            System.out.println("\n\tВыберете между да/нет");
                        }
                    } while (true);

                    habit = new Habit();
                    habit.setTitle(title);
                    habit.setDescription(description);
                    habit.setFrequency(frequency);
                    habit.setCompleted(isCompleted);
                    habit.setUserId(userId);

                    loggedUser.habits.add(habit);
                    HabitRepository.addHabit(habit, userId);

                } else if (input == 3) {
                    // nfr
                    System.out.println();

                } else if (input == 4) {

                } else {
                    System.out.println("\n\tВы ввели некорректные данные. Попробуйте еще раз.");
                }

            } catch (NumberFormatException e) {
                System.out.println("\n\tВы ввели некорректные данные. Попробуйте еще раз.");
            }
        } while (true);
    }

    // part of user menu
    private static void accountSettings() {

        String inputStr = null;
        int input = -1;
        System.out.println("Настройки аккаунта");

        do {
            System.out.println("Press 0 - Выйти из настроек");
            System.out.println("Press 1 - Редактировать имя");
            System.out.println("Press 2 - Редактировать email");
            System.out.println("Press 3 - Редактировать пароль");
            System.out.println("Press 100 - Удалить аккаунт");

            try {
                inputStr = scan.nextLine();
                input = Integer.parseInt(inputStr);

            } catch (NumberFormatException e) {
                System.out.println("\n\tВы ввели неверное значение");
            }

            // выйти из настроек
            if (input == 0) {
                break;
            } // редактировать имя
            else if (input == 1) {
                System.out.println("Введите новое имя");
                inputStr = scan.nextLine();
                UserRepository.userChangeName(inputStr, loggedUser.getEmail());
            } // редактировать email
            else if (input == 2) {
                System.out.println("Введите новый email");
                String emailRegex = "^[\\w\\.-]+@[\\w\\.-]+\\.[a-zA-Z]{2,}$";

                do {
                    inputStr = scan.nextLine().trim();
                    if (!inputStr.matches(emailRegex)) {
                        System.out.println("\n\tВы ввели не корректный email. Попробуйте еще раз");
                    } else {
                        int emailCheckResult = UserRepository.isEmailExist(inputStr);
                        if (emailCheckResult == 1) {
                            System.out.println("\n\tТакой email уже существует. Выберите другой email");
                        } else if (emailCheckResult == 0) {
                            System.out.println("Ошибка соединения. Попробуйте еще раз");
                        } else {
                            UserRepository.userChangeEmail(inputStr, loggedUser.getEmail());
                            break;
                        }
                    }
                } while (true);


            } // редактировать пароль
            else if (input == 3) {
                System.out.println("Введите пароль");
                inputStr = scan.nextLine();
                UserRepository.userChangePassword(inputStr, loggedUser.getEmail());
            } // удаляем аккаунт
            else if (input == 100) {
                System.out.println("!!!!!  Вы действительно хотите удалить свой аккаунт? !!!!!!!");
                System.out.println("Напишите 'да' / 'ДА / 'Да' или любой символ/слово для отмены");
                inputStr = scan.nextLine().trim();

                if (inputStr.equals("да")) {
                    UserRepository.userDeleteAccount(loggedUser);
                    loggedUser = null;
                    break;
                }
            } else {
                System.out.println("\n\tВы ввели неверное значение");
            }

        } while (true);


    }

    private static void register() {

        String emailRegex = "^[\\w\\.-]+@[\\w\\.-]+\\.[a-zA-Z]{2,}$";
        int emailCheckResult = -2;
        String name = null;
        String email = null;
        String password = null;

        // имя
        do {
            System.out.println("Введите ваше имя");
            name = scan.nextLine().trim();
            if (name.isEmpty()) {
                System.out.println("\n\tИмя не может быть пустой строкой. Попробуйте снова.");
            }
        } while (name.isEmpty());

        // email
        do {
            System.out.println("Введите email");
            email = scan.nextLine().trim();

            if (!email.matches(emailRegex)) {
                System.out.println("\n\tНеверный формат email. Попробуйте снова.");
                emailCheckResult = -1;
            } else {
                emailCheckResult = UserRepository.isEmailExist(email);
                if (emailCheckResult == 1) {
                    System.out.println("\n\tЭтот email уже существует. Попробуйте другой");
                } else if (emailCheckResult == 0) {
                    System.out.println("\n\tОшибка соединения при проверке существования email. Попробуйте снова.");
                }
            }

        } while (!email.matches(emailRegex) && emailCheckResult != 1);

        // пароль
        do {
            System.out.println("Введите пароль");
            password = scan.nextLine().trim();

            if (password.isEmpty()) {
                System.out.println("\n\tПароль должен содержать хотя бы один символ. Попробуйте снова.");
            }
        } while (password.isEmpty());

        // регистрируем юзера
        UserRepository.registerUser(new User(name, email, password));
    }

    private static void login() {

        String email = null;
        String password = null;
        do {
            System.out.println("Press 0 - Главное меню");
            System.out.println("Введите email");
            email = scan.nextLine();
            if (email.equals("0")) {
                break;
            }
            System.out.println("Введите пароль");
            password = scan.nextLine();
            if (password.equals("0")) {
                break;
            }
// todo сделать что бы подгружала с привычками
            loggedUser = UserRepository.getUserByEmail(email);
            if (loggedUser == null || !Objects.equals(loggedUser.getPassword(), password)) {
                System.out.println("\n\tНеправильные данные или такого пользователя не существует");
            } else {
                break;
            }
        } while (true);
    }

    private static void mainMenu() {
        System.out.println("Press 0 - Остановить приложение");
        System.out.println("Press 1 - Войти");
        System.out.println("Press 2 - Зарегистрироваться");
    }


}
