package lzw;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.Scanner;
import java.util.ArrayList;
import java.io.File;
import java.io.IOException;
import java.util.Collections;

import static java.lang.Math.pow;

public class Code {
    static boolean Is_In (int end , String x , HashMap<Integer,String> h ){
        boolean existing =false;
        for (int i = 65; i < end; i++) {
            if(x.equals(h.get(i))){
                existing=true;
                break;
            }
        }
        return existing;
    }
    static int What_the_Index ( int end , String x , HashMap<Integer,String> h ){
        int the_index=0;
        for (int i = 65; i < end; i++) {
            if(x.equals(h.get(i))){
                the_index=i;
                break;
            }
        }
        return the_index;
    }
    //---------------------------------decombe-------------------------------------------//
    static void decompressed(String readFileName,String writeFileName,int theOriginal,int theCompressSize){
        ArrayList<Integer> arr =new ArrayList<Integer>();
        int size=0;
        HashMap< Integer, String> table = new HashMap<Integer, String>();
        for (int i=65;i<128;i++){
            table.put(i,((char)i)+"");
        }
        try {
            File myObj = new File(readFileName);
            Scanner input = new Scanner(myObj);
            //first number
            String data = input.nextLine();
            // read the size of tags
            size= Integer.parseInt(data);
            //System.out.println(data);
            while (input.hasNextLine()) {
                // read the array of tags
                data = input.nextLine();
                arr.add(Integer.parseInt(data));
                //System.out.println(data);
            }
            input.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        String text=table.get(arr.get(0)),previous=table.get(arr.get(0)),current;
        int ind=128;
        for (int i=1;i<size;i++){
            int tag=arr.get(i);
            if(table.containsKey(tag)){
                current=table.get(tag);
                table.put(ind,previous+current.charAt(0));
            }
            else{
                current=previous+previous.charAt(0);
                table.put(ind,current);
            }
            text+=current;
            previous=current;
            ind++;
        }
        System.out.println("The compressed text is --> "+text);
        try {
            FileWriter theFile = new FileWriter(writeFileName);
            theFile.write("the text after decompression : \n");
            theFile.write("   -------->   "+text+ "  \n");
            theFile.write("The original Size = "+theOriginal+ "  \n");
            theFile.write("The compressed Size = "+theCompressSize+ "  \n");
            theFile.close();
            System.out.println("Successfully wrote to the file.");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

    }
    public static <string> void main(String[] args)  {
        Scanner input = new Scanner(System.in);
        System.out.println("Enter the compression file name  : ");
        String comFileName=input.next();
        System.out.println("Enter the decompression file name  : ");
        String decomFileName=input.next();
        HashMap<Integer, String> table = new HashMap<Integer, String>();
        ArrayList<Integer> tags = new ArrayList<Integer>();
        for (int i = 65; i < 128; i++) {
            table.put(i, ((char) i) + "");
        }
        System.out.print("Please Input The Text to Compress:  ");
        String text =input.next();
        int index=128;
        int i=0;
        while (i<text.length()) {
            String check = text.substring(i, i + 1);
            while (Is_In(index, check, table) && i != text.length() - 1) {
                i++;
                check += text.charAt(i);
            }
            if (!Is_In(index, check, table)) {
                table.put(index, check);
                check = check.substring(0, check.length() - 1);
                i--;
            }
            tags.add(What_the_Index(index, check, table));
            index++;
            if (i == text.length() - 1 && check.charAt(check.length() - 1) == text.charAt(text.length() - 1))
                break;
            i++;
        }
        for (Integer tag : tags) {
            System.out.println(tag);
        }
        //write in file
        try {
            FileWriter theFile = new FileWriter(comFileName);
            theFile.write(Integer.toString(tags.size())+"\n");
            for (Integer tag : tags) {
                theFile.write(Integer.toString(tag)+"\n");
            }
            theFile.close();
            System.out.println("Successfully wrote to the file.");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

        System.out.println("The Number Of Tags Are : "+tags.size());
        int theOriginal=text.length()*8;
        int maxValue=Collections.max(tags);
        int maxPower=0;
        while(maxValue>pow(2,maxPower)){
            maxPower++;
        }
        int theCompressSize=tags.size()*maxPower;
        System.out.println("The original Size = "+theOriginal);
        System.out.println("The compressed Size = "+theCompressSize);
        System.out.println("/_____________________decompression__________________________/");
        decompressed(comFileName,decomFileName,theOriginal,theCompressSize);
    }
}


