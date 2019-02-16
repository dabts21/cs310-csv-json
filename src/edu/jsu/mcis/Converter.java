package edu.jsu.mcis;

import java.io.*;
import java.util.*;
import com.opencsv.*;
import org.json.simple.*;
import org.json.simple.parser.*;

public class Converter {
    
    /*
    
        Consider the following CSV data:
        
        "ID","Total","Assignment 1","Assignment 2","Exam 1"
        "111278","611","146","128","337"
        "111352","867","227","228","412"
        "111373","461","96","90","275"
        "111305","835","220","217","398"
        "111399","898","226","229","443"
        "111160","454","77","125","252"
        "111276","579","130","111","338"
        "111241","973","236","237","500"
        
        The corresponding JSON data would be similar to the following (tabs and
        other whitespace have been added for clarity).  Note the curly braces,
        square brackets, and double-quotes!  These indicate which values should
        be encoded as strings, and which values should be encoded as integers!
        
        {
            "colHeaders":["ID","Total","Assignment 1","Assignment 2","Exam 1"],
            "rowHeaders":["111278","111352","111373","111305","111399","111160",
            "111276","111241"],
            "data":[[611,146,128,337],
                    [867,227,228,412],
                    [461,96,90,275],
                    [835,220,217,398],
                    [898,226,229,443],
                    [454,77,125,252],
                    [579,130,111,338],
                    [973,236,237,500]
            ]
        }
    
        Your task for this program is to complete the two conversion methods in
        this class, "csvToJson()" and "jsonToCsv()", so that the CSV data shown
        above can be converted to JSON format, and vice-versa.  Both methods
        should return the converted data as strings, but the strings do not need
        to include the newlines and whitespace shown in the examples; again,
        this whitespace has been added only for clarity.
    
        NOTE: YOU SHOULD NOT WRITE ANY CODE WHICH MANUALLY COMPOSES THE OUTPUT
        STRINGS!!!  Leave ALL string conversion to the two data conversion
        libraries we have discussed, OpenCSV and json-simple.  See the "Data
        Exchange" lecture notes for more details, including example code.
    
    */ 
    
    @SuppressWarnings("unchecked")
    public static String csvToJson(String csvString) {
        
        String results = "";
        
        try {
            
            CSVReader reader = new CSVReader(new StringReader(csvString));
            List<String[]> full = reader.readAll();
            Iterator<String[]> iterator = full.iterator();
            
            // INSERT YOUR CODE HERE
            JSONObject obj = new JSONObject();
            
            if(iterator.hasNext()){
                String[] colheaders = iterator.next();
                JSONArray jheaders = new JSONArray();
                for (int i = 0; i < colheaders.length; i++){
                    jheaders.add(colheaders[i]);
                    
                }
                obj.put("colHeaders", jheaders);
                
                JSONArray rowheaders = new JSONArray();
                JSONArray data = new JSONArray();
                while(iterator.hasNext()){
                    String[] templine = iterator.next();
                    JSONArray tempdata = new JSONArray();
                    for(int i = 0; i < templine.length; i++){
                        if(i == 0){
                            rowheaders.add(templine[i]);
                        }
                        else{
                            tempdata.add(Integer.parseInt(templine[i]));
                        }
                    }
                    data.add(tempdata);
                }
                obj.put("rowHeaders", rowheaders);
                obj.put("data", data);
            }
             
            
            
            
            
            results = obj.toJSONString();
        }        
        catch(Exception e) { return e.toString(); }
        
        return results.trim();
        
    }
    
    public static String jsonToCsv(String jsonString) {
        
        String results = "";
        
        
        try {

            StringWriter writer = new StringWriter();
            CSVWriter csvWriter = new CSVWriter(writer, ',', '"', '\n');
            
            // INSERT YOUR CODE HERE
            JSONParser parser = new JSONParser();
            JSONObject obj = (JSONObject)parser.parse(jsonString);
            JSONArray colheaders = (JSONArray) obj.get("colHeaders");
            Iterator <String> iterator = colheaders.iterator();
            List<String> csvheaders = new ArrayList<String>();
            
            while(iterator.hasNext()){
                csvheaders.add(iterator.next());
            }
            csvWriter.writeNext(csvheaders.toArray(new String[csvheaders.size()]));
            
            JSONArray rowheaders = (JSONArray) obj.get("rowHeaders");
            iterator = rowheaders.iterator();
            List<List<String>> datalist = new ArrayList<List<String>>();
            
            while(iterator.hasNext()){
                List<String> temp = new ArrayList<String>();
                temp.add(iterator.next());
                datalist.add(temp);
            }
            
            JSONArray data = (JSONArray) obj.get("data"); 
            Iterator<JSONArray> dataIt = data.iterator();
            
            int count = 0;
           while(dataIt.hasNext())
           {
               JSONArray currentJArray = dataIt.next();
               Iterator<Long> is = currentJArray.iterator();
               List<String> record = new ArrayList<String>();
               while(is.hasNext()){
                   record.add(Long.toString(is.next()));
               }
               results += record.toString();
               datalist.get(count).addAll(record);
               record = datalist.get(count);
               csvWriter.writeNext(record.toArray(new String[record.size()]));
               count++;
           }

            
            
            
            
            
            
            
            
            
            csvWriter.close();
            results = writer.toString();
            
            
        }
        
        catch(Exception e) { return e.toString(); }
        
        return results.trim();
        
    }

}