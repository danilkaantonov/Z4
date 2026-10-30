import java.io.PrintStream;
import java.util.Objects;


interface User {
    void changeLogin(String newLogin);
    String showLogin();

    void changePasword(String oldPasword, String newPasword);
    boolean autenticate(String login, String pasword);
}


abstract class People implements User {
    private String fio;
    private int age;
    private String position;

    private String login;
    private int paswordHash; // храним хеш!

    public People(String fio, int age, String position, String login, String pasword) {
        this.fio = fio;
        this.age = age;
        this.position = position;
        this.login = login;
        this.paswordHash = Objects.hash(pasword);
    }


    @Override
    public void changeLogin(String newLogin) {
        if (newLogin == null || newLogin.isBlank()) {
            System.out.println("Логин не может быть пустым");
            return;
        }
        this.login = newLogin;
        System.out.println("Логин изменен на: " + newLogin);
    }

    @Override
    public String showLogin() {
        return login;
    }

    @Override
    public void changePasword(String oldPasword, String newPasword) {
        if (!autenticate(login, oldPasword)) {
            System.out.println("Старый пароль неверный - смена отклонена!");
            return;
        }
        if (newPasword == null || newPasword.length() < 6) {
            System.out.println("Новый пароль слишком короткий  - мин. 6 символов!");
            return;
        }
        this.paswordHash = Objects.hash(newPasword);
        System.out.println("Пароль успешно обновлен!");
    }

    @Override
    public boolean autenticate(String login, String pasword) {
        return this.login.equals(login) && this.paswordHash == Objects.hash(pasword);
    }

    public String getFio() { return fio; }
    public int getAge() { return age; }
    public String getPosition() { return position; }

    public abstract void givInfo();
}

class Teacher extends People {


    public Teacher(String fio, int age, String position,
                   String login, String pasword) {
        super(fio, age, position, login, pasword);

    }

    @Override
    public void givInfo() {
        System.out.println("[Преподаватель]" +getFio()+"," +getAge()+ "лет," +getPosition()+",логин:" +showLogin());
    }
}

class Student extends People {


    public Student(String fio, int age, String position,
                   String login, String pasword) {
        super(fio, age, position, login, pasword);

    }

    @Override
    public void givInfo() {
        System.out.println("[Студент]" +getFio()+"," +getAge()+ "лет," +getPosition()+",логин:" +showLogin());
    }
}

public class Z4 {
    public static void main(String[] args) {
    	PrintStream o = System.out;
        Teacher t1 = new Teacher("Орлов Дмитрий Константинович", 52,
                "Профессор", "orlov.dk", "Secure#2025");
        Teacher t2 = new Teacher("Белова Ольга Николаевна", 41,
                "Доцент", "belova_on", "TeachMe!77");

        Student s1 = new Student("Громов Никита Андреевич", 21,
                "Студент", "gromov_na", "student_2024");
        Student s2 = new Student("Тихонова Полина Сергеевна", 18,
                "Студент", "tikhonova_ps", "Polina#01");

        People[] people = { t1, t2, s1, s2 };
        for (People p : people) p.givInfo();

        o.println("\n~~~ Проверка входа ~~~");
        o.println("Преподаватель 1 / Secure#2025 -> " + t1.autenticate("orlov.dk", "Secure#2025"));
        o.println("Преподаватель 1 / Secure#2024 -> " + t1.autenticate("orlov.dk", "Secure#2024"));
        o.println("Студент2 / Polina#01  -> " + s2.autenticate("tikhonova_ps", "Polina#01"));

        o.println("\n~~~ Смена пароля ~~~");
        s1.changePasword("wrong_old", "brandNewPass");  // отказ - старый неверный
        s1.changePasword("student_2024", "short");       // отказ - короткий
        s1.changePasword("student_2024", "N1k1ta_2025"); // успех
        o.println("Студент1 / N1k1ta_2025 -> " + s1.autenticate("gromov_na", "N1k1ta_2025"));

        o.println("\n~~~ Смена логина ~~~");
        t2.changeLogin("belova.prof");
        t2.changeLogin(""); // отказ
        t2.givInfo();
    }
}