package uk.ac.soton.dew1g18.ch8;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


public class Helper {

    public String takeInput(String message){
        try {
            System.out.println(message);
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

    public Float[] arrayPrimToObj(float[] arr){
        Float[] yaBoy = new Float[arr.length];
        for(int i=0;i<arr.length;i++){
            yaBoy[i]=arr[i];
        }
        return yaBoy;
    }

    public float[] arrayObjToPrim(Float[] arr){
        float[] yaBoy = new float[arr.length];
        for(int i=0;i<arr.length;i++){
            yaBoy[i]=arr[i];
        }
        return yaBoy;
    }

    public float floatFromDouble(double dub){
        return new Double(dub).floatValue();
    }

    public int intFromDouble(double dub){
        return new Double(dub).intValue();
    }

}


