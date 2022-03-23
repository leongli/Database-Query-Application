import java.sql.*;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class A02MiddleTier {
    //This class will contain your code for interacting with Database, acquire the query result and display it in the GUI text area.
    
    private String url = "jdbc:mysql://localhost:3306/a02";
    private String username = "root";
    private String password = "Haddockli1!";
    private Connection connection;
    private ArrayList<String> tables = new ArrayList<>(); 
    //private String Date;
    private String from;
    private String to;
    private boolean isall = false;

    A02MiddleTier(){
        //System.out.println("Connecting database...");
        
        try {
            Class.forName("com.mysql.jdbc.Driver");
           // System.out.println("Driver loaded!");
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException("Cannot find the driver in the classpath!", e);
        }
        

        try {
            connection = DriverManager.getConnection(url, username, password);
            System.out.println("Database connected!");
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot connect the database!", e);
        }
    }
    
    public void setFrom(String from){
        this.from = from;
    }

    public void setTo(String to){
        this.to = to;
    }

    public void SelectBuilder(String table) {
        tables.add(table);
    }

    public void setIsAll(boolean flag){
        this.isall = flag;
    }
    public String call() throws SQLException{    
            String s;
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(this.output());
            ResultSetMetaData rsMetaData = rs.getMetaData();
            int count = rsMetaData.getColumnCount();
            s = "";
            while(rs.next()){
                for(int i = 1; i <= count; i++){
                    s += rs.getString(rsMetaData.getColumnName(i)) + " ";
                }
            s += "\n";
            }

        return s;
    }

	public String Join(String a, String b){
        return "(" + a +" JOIN " + b +" ON " + a+".EventID =" + b+".EventID)";
    }

    public String output(){
        String date = "";
        String table = "";
        if (!isall && from != null && to != null){    
            date = (tables.contains("eventconference")) ? "WHERE EventID IN (SELECT EventID FROM eventconference WHERE eventconference.EvDate BETWEEN \'" + this.from + " 00:00:00\' AND \'" + this.to + " 23:59:59\')" 
                    : "WHERE EventID IN (SELECT EventID FROM eventconference WHERE eventconference.EvDate BETWEEN \'" + this.from + " 00:00:00\' AND \'" + this.to + " 23:59:59\')" ;
        } 

        if (tables.size() > 1){
            
            for(int i = 0; i < tables.size() - 1; i++){
                table = Join(tables.get(i), tables.get(i+1));
            }
            return "SELECT * FROM " + table + " " + date;    
        }

        //String date = (isall && tables.contains("eventconference")) ? "WHERE EvDate BETWEEN " + this.from + " AND " + this.to : "";
        // tables.stream().map(i -> i.toString()).collect(Collectors.joining(","))
        return "SELECT * FROM " + tables.stream().map(i -> i.toString()).collect(Collectors.joining(",")) + " " + date ;
    }
    
    public static void main(String [] args) throws SQLException {
        A02MiddleTier db = new A02MiddleTier();
        db.SelectBuilder("eventbook");
        db.SelectBuilder("eventconference");
        db.SelectBuilder("eventJournal");
        db.setFrom("2019-07-13");
        db.setTo("2019-07-30");
       // db.switchIsAll();
        System.out.print(db.output());
        System.out.print(db.call());
    }
}