package edu.jsu.mcis.cs425.Lab4;

import com.opencsv.CSVReader;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.StringReader;
import java.util.Iterator;
import java.util.List;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import java.sql.*;

public class Rates {
    
    public static final String RATE_FILENAME = "rates.csv";
    
    public static List<String[]> getRates(String path) {
        
        StringBuilder s = new StringBuilder();
        List<String[]> data = null;
        String line;
        
        try {
            
            /* Open Rates File; Attach BufferedReader */

            BufferedReader reader = new BufferedReader(new FileReader(path));
            
            /* Get File Data */
            
            while((line = reader.readLine()) != null) {
                s.append(line).append('\n');
            }
            
            reader.close();
            
            /* Attach CSVReader; Parse File Data to List */
            
            CSVReader csvreader = new CSVReader(new StringReader(s.toString()));
            data = csvreader.readAll();
            
        }
        catch (Exception e) { System.err.println( e.toString() ); }
        
        /* Return List */
        
        return data;
        
    }
    
    public static String getRatesAsTable(List<String[]> csv) {
        
        StringBuilder s = new StringBuilder();
        String[] row;
        
        try {
            
            /* Create Iterator */
            
            Iterator<String[]> iterator = csv.iterator();
            
            /* Create HTML Table */
            
            s.append("<table>");
            
            while (iterator.hasNext()) {
                
                /* Create Row */
            
                row = iterator.next();
                s.append("<tr>");
                
                for (int i = 0; i < row.length; ++i) {
                    s.append("<td>").append(row[i]).append("</td>");
                }
                
                /* Close Row */
                
                s.append("</tr>");
            
            }
            
            /* Close Table */
            
            s.append("</table>");
            
        }
        catch (Exception e) { System.err.println( e.toString() ); }
        
        /* Return Table */
        
        return (s.toString());
        
    }
    public static String getRatesAsJson(String code) {
        String server = "jdbc:mysql://localhost:3306/lab4b";
        //String username ="jdbc/db_pool";
        //String password = "CS425!Lab3B";
        String username = "root";
        String password = "CS310";
        String query = "SELECT * FROM rates WHERE code =  ?";
        String results = "";
        PreparedStatement stmt = null;
        Boolean hasresults = false;
        ResultSet resultset = null;
        JSONObject json = new JSONObject();
        JSONObject rates = new JSONObject();
       
        int listLength = 0;
        try{
            Class.forName("com.mysql.jdbc.Driver").newInstance(); 
            Connection conn = DriverManager.getConnection(server, username, password);
            if( code == null){
                stmt = conn.prepareStatement("SELECT * FROM rates");
                System.out.println("Made into null statement!");
                hasresults = stmt.execute();
            
                resultset = stmt.getResultSet();
            
                while ( hasresults ) {
                    System.out.println("Made into hasresults statement!");
                    resultset.next();
                    rates.put(resultset.getString("code"), resultset.getDouble("rate"));
                    
                }
                    stmt.close();
            }
            else{
                stmt = conn.prepareStatement(query);
                stmt.setString(1 , code);
                hasresults = stmt.execute();
            
                resultset = stmt.getResultSet();

                if ( hasresults ) {
                    
                    resultset.next();
                    
                    
                    rates.put(resultset.getString("code"), resultset.getDouble("rate"));
                
                }
                stmt.close();
            }

        }
        catch(Exception e){
            System.out.println(e);
            rates.put("Broken", 101);
        }
        
        String baseCurrency = "USD";
        String dateOfPull = "2019-09-20";
        json.put("rates", rates);
        json.put("base", baseCurrency);
        json.put("date", dateOfPull);
        results = JSONValue.toJSONString(json);
        System.out.println(code + ":)");
        return (results.trim());
    }
    public static String getRatesAsJson(List<String[]> csv) {
        
        String results = "";
        String[] row;
        
        try {
            
            /* Create Iterator */
            
            Iterator<String[]> iterator = csv.iterator();
            
            /* Create JSON Containers */
            
            JSONObject json = new JSONObject();
            JSONObject rates = new JSONObject();            
            
            /* 
             * Add rate data to "rates" container and add "date" and "base"
             * values to "json" container.  See the "getRatesAsTable()" method
             * for an example of how to get the CSV data from the list, and
             * don't forget to skip the header row!
             *
             * *** INSERT YOUR CODE HERE ***
             */
            iterator.next();
            while (iterator.hasNext())
            {
                row = iterator.next();
                String code = row[1];
                rates.put(code ,Double.parseDouble(row[2]));
                    
                    
                
                
            }
            String baseCurrency = "USD";
            String dateOfPull = "2019-09-20";
            json.put("rates", rates);
            json.put("base", baseCurrency);
            json.put("date", dateOfPull);
            
            /* Parse top-level container to a JSON string */
            
            results = JSONValue.toJSONString(json);
            System.err.println(results);
            System.out.println(results);
            
        }
        catch (Exception e) { System.err.println( e.toString() ); }
        
        /* Return JSON string */
        
        return (results.trim());
        
    }

}