import java.sql.*;
// import java.util.ArrayList;
// import java.util.stream.Collectors;

public class A02MiddleTier {
    //This class will contain your code for interacting with Database, acquire the query result and display it in the GUI text area.
    
    private String url = "jdbc:mysql://localhost:3306/a02";
    private String username = "root";
    private String password = "Haddockli1!";
    private Connection connection;
    private String from;
    private String to;
    private boolean isall = true;
    private boolean evC=false, evJ=false, evB=false;


    public A02MiddleTier(){
        
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

    public void SelectBuilder(String table,boolean state) {
        switch(table){
            case "eventConference":
            evC=state;
            break;
            case "eventJournal":
            evJ=state;
            break;
            case "eventBook":
            evB=state;
            break;
        }
    }

    public void setIsAll(boolean flag){
        this.isall = flag;
    }
    public String call() throws SQLException{    
            System.out.println("IN CALL");
            String output=this.output();

            if (output!= "NO EVENT SELECTED"){
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
            

        return "NO EVENT SELECTED";
    }

    public String output(){

        boolean isAllDates=this.from == null && this.to==null && isall;
		//NO EVENT & ALL EVENT  OR  NO EVENT & TO FROM EVENT
		if(evC==false && evJ==false && evB==false) return "NO EVENT SELECTED"; //return error
		
        else if(evC && evJ && evB){ 
            //EVENT CONFERENCE & EVENT BOOK & EVENT JOURNAL & ALL EVENT
            if(isAllDates) 
                return "select distinct (e.ID), e.Name "
                +"from ((a02.eventjournal as ej inner join a02.event as e on e.ID=ej.EventID) "
                +"inner join a02.activityhappens as ah on ah.EventID) "
                +"union "
                +"select distinct (e.ID), e.Name "
                +"from ((a02.eventbook as eb inner join a02.event as e on e.ID=eb.EventID) "
                +"inner join a02.activityhappens as ah on ah.EventID) "
                +"union "
                +"select distinct(e.ID), e.Name "
                +"from (a02.Event as e inner join a02.eventconference as ec on e.ID=ec.EventID);";
            //EVENT CONFERENCE & EVENT BOOK & EVENT JOURNAL & TO FROM EVENT
            else 
                return "select distinct (e.ID), e.Name "
                +"from ((a02.eventjournal as ej inner join a02.event as e on e.ID=ej.EventID) "
                +"inner join a02.activityhappens as ah on ah.EventID) "
                +"where ah.ActivityDate "
                +"between'"+this.from+"' and '"+this.to+"'"
                +"union "
                +"select distinct (e.ID), e.Name "
                +"from ((a02.eventbook as eb inner join a02.event as e on e.ID=eb.EventID) "
                +"inner join a02.activityhappens as ah on ah.EventID) "
                +"where ah.ActivityDate between'"+this.from+"' and '"+this.to+"'"
                +"union "
                +"select distinct(e.ID), e.Name "
                +"from (a02.Event as e inner join a02.eventconference as ec on e.ID=ec.EventID) "
                +"where ec.EvDate "
                +"between'"+this.from+"' and '"+this.to+"';";
        }
        
        else if(evC && evB && evJ==false){
            //EVENT CONFERENCE & EVENT BOOK & ALL EVENT
            if(isAllDates) 
                return "select distinct(e.ID), e.Name "
                +"from (a02.Event as e inner join a02.eventconference as ec on e.ID=ec.EventID) "
                +"where ec.EvDate "
                +"union "
                +"select distinct (e.ID), e.Name "
                +"from ((a02.eventbook as eb inner join a02.event as e on e.ID=eb.EventID) "
                +"inner join a02.activityhappens as ah on ah.EventID) ;";
            //EVENT CONFERENCE & EVENT BOOK & TO FROM EVENT
            else 
                return "select distinct(e.ID), e.Name "
                +"from (a02.Event as e inner join a02.eventconference as ec on e.ID=ec.EventID) "
                +"where ec.EvDate "
                +"between'"+this.from+"' and '"+this.to+"'"
                +"union "
                +"select distinct (e.ID), e.Name "
                +"from ((a02.eventbook as eb inner join a02.event as e on e.ID=eb.EventID) "
                +"inner join a02.activityhappens as ah on ah.EventID) "
                +"where ah.ActivityDate between'"+this.from+"' and '"+this.to+"';";
        }

        else if(evC==false && evB && evJ){
            //EVENT BOOK & EVENT JOURNAL & ALL EVENT
            if(isAllDates) 
                return "select distinct (e.ID), e.Name "
                +"from ((a02.eventbook as eb inner join a02.event as e on e.ID=eb.EventID) "
                +"inner join a02.activityhappens as ah on ah.EventID)"
                +"union "
                +"select distinct (e.ID), e.Name "
                +"from ((a02.eventjournal as ej inner join a02.event as e on e.ID=ej.EventID) " 
                +"inner join a02.activityhappens as ah on ah.EventID);";
            //EVENT BOOK & EVENT JOURNAL & TO FROM EVENT
            else return "select distinct (e.ID), e.Name "
            +"from ((a02.eventjournal as ej inner join a02.event as e on e.ID=ej.EventID) "
            +"inner join a02.activityhappens as ah on ah.EventID) "
            +"where ah.ActivityDate "
            +"between'"+this.from+"' and '"+this.to+"'"
            +"union "
            +"select distinct (e.ID), e.Name "
            +"from ((a02.eventbook as eb inner join a02.event as e on e.ID=eb.EventID) "
            +"inner join a02.activityhappens as ah on ah.EventID) "
            +"where ah.ActivityDate between'"+this.from+"' and '"+this.to+"';";
        }

        else if(evC && evB==false && evJ){
            //EVENT CONFERENCE & EVENT JOURNAL & ALL EVENT
            if(isAllDates) 
                return "select distinct(e.ID), e.Name "
                +"from (a02.Event as e inner join a02.eventconference as ec on e.ID=ec.EventID) "
                +"union "
                +"select distinct (e.ID), e.Name "
                +"from ((a02.eventjournal as ej inner join a02.event as e on e.ID=ej.EventID) "
                +"inner join a02.activityhappens as ah on ah.EventID);";
            //EVENT CONFERENCE & EVENT JOURNAL & TO FROM EVENT
            else 
                return "select distinct(e.ID), e.Name "
                +"from (a02.Event as e inner join a02.eventconference as ec on e.ID=ec.EventID) "
                +"where ec.EvDate "
                +"between'"+this.from+"' and '"+this.to+"'"
                +"union "
                +"select distinct (e.ID), e.Name "
                +"from ((a02.eventjournal as ej inner join a02.event as e on e.ID=ej.EventID) "
                +"inner join a02.activityhappens as ah on ah.EventID) "
                +"where ah.ActivityDate "
                +"between'"+this.from+"' and '"+this.to+"';";
        }

        else if(evC && evB==false && evJ==false){
            //EVENT CONFERENCE & ALL EVENT
            if(isAllDates) 
                return "select distinct(e.ID), e.Name "
                +"from (a02.Event as e inner join a02.eventconference as ec on e.ID=ec.EventID);";
            //EVENT CONFERENCE & TO FROM EVENT
            else 
                return "select distinct(e.ID), e.Name "
                +"from (a02.Event as e inner join a02.eventconference as ec on e.ID=ec.EventID) "
                +"where ec.EvDate "
                +"between'"+this.from+"' and '"+this.to+"';";
        }
        else if(evC==false && evB==false && evJ){
            //EVENT JOURNAL & ALL EVENT
            if(isAllDates) 
                return "select distinct (e.ID), e.Name "
                +"from ((a02.eventjournal as ej inner join a02.event as e on e.ID=ej.EventID) "
                +"inner join a02.activityhappens as ah on ah.EventID);";
            //EVENT JOURNAL & TO FROM EVENT
            else 
                return "select distinct (e.ID), e.Name "
                +"from ((a02.eventjournal as ej inner join a02.event as e on e.ID=ej.EventID) "
                +"inner join a02.activityhappens as ah on ah.EventID) "
                +"where ah.ActivityDate "
                +"between'"+this.from+"' and '"+this.to+"';";
        }
        else if(evC==false && evB && evJ==false){
            //EVENT BOOK & ALL EVENT
            if(isAllDates) 
                return "select distinct (e.ID), e.Name "
                +"from ((a02.eventbook as eb inner join a02.event as e on e.ID=eb.EventID) "
                +"inner join a02.activityhappens as ah on ah.EventID);";
            //EVENT BOOK & TO FROM EVENT
            else 
                return "select distinct (e.ID), e.Name "
                +"from ((a02.eventbook as eb inner join a02.event as e on e.ID=eb.EventID) "
                +"inner join a02.activityhappens as ah on ah.EventID) "
                +"where ah.ActivityDate between'"+this.from+"' and '"+this.to+"';";
        }

		else return "QUERY ERROR";



    }

    
}
