import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        Model model;
        while(true){
            try{
                System.out.println("Wpisz nazwę bazy:");
                String dbName = scanner.nextLine();

                System.out.println("Wpisz nazwe uzytkownika:");
                String username = scanner.nextLine();

                System.out.println("Wpisz haslo:");
                String password = scanner.nextLine();

                model = new Model(dbName, username, password);
                model.importDatabase();
                //model.showTables();
                break;

            }catch(SQLException sqlException){
                System.out.println("Nie udalo sie polaczyc z baza danych. Sprobuj jeszcze raz");
            }
        }
        model.addTableToList(new Table("Tabela1",
                2,
                1,
                new ArrayList<>(){{add("id"); add("Name");}},
                new ArrayList<>(){{add(String.valueOf(Integer.class)); add(String.valueOf(String.class));}},new Object[][]{{1, "Kuba"}}));
        model.addTableToList(new Table("Tabela2",
                2,
                1,
                new ArrayList<>(){{add("id"); add("Name");}},
                new ArrayList<>(){{add(String.valueOf(Integer.class)); add(String.valueOf(String.class));}},new Object[][]{{1, "Kuba"}}));
        model.addTableToList(new Table("Tabela3",
                2,
                1,
                new ArrayList<>(){{add("id"); add("Name");}},
                new ArrayList<>(){{add(String.valueOf(Integer.class)); add(String.valueOf(String.class));}},new Object[][]{{1, "Kuba"}}));

        //model.showTables();


        try {
            //model.saveTables();
            model.closeConnection();
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }
}
