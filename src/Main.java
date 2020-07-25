import java.sql.SQLException;

public class Main {
    public static void main(String[] args) {
        /*DatabaseConnector dbConnector = new DatabaseConnector("jdbc:mysql://localhost:3306/osadnicy", "root", "");

        ResultSet rs = dbConnector.executeQuery("select * from uzytkownicy");

        while (rs.next()) {

            int id_col = rs.getInt("id");
            String first_name = rs.getString("user");
            String last_name = rs.getString("pass");
            String job = rs.getString("email");

            String p = id_col + " " + first_name + " " + last_name + " " + job;
            System.out.println(p);
        }
        try{

            Model model = new Model("jdbc:mysql://localhost:3306/osadnicy", "root", "");
            model.addTableToList(new Table("Tabela1",
                    2,
                    0,
                    new String[]{"abc", "bcd"},
                    new Object[][]{{13.2,"abc"},{12}}));
            //model.showTables();
            model.saveTables();

        }catch (SQLException sqlException){
            System.out.println(sqlException.getMessage());
        }*/
    }
}
