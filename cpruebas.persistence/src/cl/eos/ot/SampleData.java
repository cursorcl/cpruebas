package cl.eos.ot;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

public class SampleData extends Application {   
    
  private ObservableList<ObservableList> data;              
  private TableView table = new TableView();          
       
  public static void main(String[] args) {                 
      launch(args);  
  }  
    
  //@Override  
  //public void start(Stage stage) throws Exception {     
  public void buildData(){  
        
      data = FXCollections.observableArrayList();  
                  try{  
                      String []divisions = {"F","D","B","W","M"};  
          String []contactNames = {"Dave","Namrita","Anil","Frank","Bill"};  
                                    
                      Connection conn = getConnection();  
              PreparedStatement ps = conn.prepareStatement(  
                              "select acct_no, name, division, contact from test_customers " +  
                              "order by acct_no" );                          
              ResultSet rs = ps.executeQuery();  
                        
              while (rs.next()) {  
                              String acctNo = rs.getString( "acct_no" );                                  
                              String name = rs.getString( "name" );  
                              String division = rs.getString( "division" );  
                              String contact = rs.getString( "contact" );  
                             
                              ObservableList<String> row = FXCollections.observableArrayList();  
                              for(int i=1 ; i<=rs.getMetaData().getColumnCount(); i++){                      
                              row.add(rs.getString(i));  
                              }                          
                                
                              data.add(row);   
                                
                              System.out.println("Data "+ data );                                                      
                        
                      }  
                        
                    //rs.close();  
            //ps.close();  
            //conn.close();    
                      
                    table.setItems(data);  
                      
                  }                                                                                            
                  catch(Exception e){  
                  e.printStackTrace();  
                  System.out.println("Error on Building Data");   
                  }                                    
 
      //table.setEditable(true);  
 
      TableColumn firstNameCol = new TableColumn("First Name");  
      firstNameCol.setMinWidth(200);  
        
      TableColumn accNumberCol = new TableColumn("Account Number");  
      accNumberCol.setMinWidth(100);          
 
      TableColumn contactsCol = new TableColumn("Contacts");  
      contactsCol.setMinWidth(100);          
                
      TableColumn divisionCol = new TableColumn("Division");  
      divisionCol.setMinWidth(200);         
 
      table.getColumns().addAll(firstNameCol, contactsCol, accNumberCol,divisionCol);   
      System.out.println("Table Value::" + table);  
  }  
        
      @Override  
    public void start(Stage stage) throws Exception {  
        
      table = new TableView();  
      buildData();  
        
      Scene scene = new Scene(table);         
 
      stage.setScene(scene);  
      stage.show();  
 
        
  }  

  private Connection getConnection() throws SQLException, ClassNotFoundException {         
        
      String driver = "org.postgresql.Driver";  
              System.out.println("Connected");  
      String url = "jdbc:postgresql://prod:5432/wrcobb";  
      Class.forName( driver );  
      Connection conn;  
                  conn = DriverManager.getConnection( url, "wrcobb", "wrcobb" );  
                  System.out.println("Enter DB with credentials");  
      conn.setAutoCommit( false );  
      return conn;              
  }          
    
 
  public static class Person {  
 
      private final SimpleStringProperty firstName;  
      private final SimpleStringProperty accNumber;  
      private final SimpleStringProperty division;  
      private final SimpleStringProperty lastName;  
 
      private Person(String fName, String aNumber, String div, String lName) {  
          this.firstName = new SimpleStringProperty(fName);  
          this.accNumber = new SimpleStringProperty(aNumber);  
          this.division = new SimpleStringProperty(div);  
          this.lastName = new SimpleStringProperty(lName);  
      }  
 
      public String getFirstName() {  
          return firstName.get();  
      }  
 
      public void setFirstName(String fName) {  
          firstName.set(fName);  
      }  
 
      public String getAccountNumber() {  
          return accNumber.get();  
      }  
 
      public void setAccountNumber(String aNumber) {  
          accNumber.set(aNumber);  
      }  
 
      public String getDivision() {  
          return division.get();  
      }  
 
      public void setDivision(String div) {  
          division.set(div);  
      }  
        
      public String getLastName() {  
          return lastName.get();  
      }  
 
      public void setLastName(String fName) {  
          lastName.set(fName);  
      }  
  }  
}   