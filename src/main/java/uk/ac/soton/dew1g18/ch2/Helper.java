package uk.ac.soton.dew1g18.ch2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


public class Helper {

    public String takeInput(){
        try {
            //Enter data using BufferReader
            BufferedReader reader =
                    new BufferedReader(new InputStreamReader(System.in));

            // Reading data using readLine
            String name = reader.readLine();

            // Printing the read line
            System.out.println(name);
            return name;
        }catch(IOException e){
            e.printStackTrace();
            return "Nothing";
        }
    }

}
