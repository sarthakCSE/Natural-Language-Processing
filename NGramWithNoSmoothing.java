package n.gram;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NGramWithNoSmoothing {  


    public static void main(String[] args) throws IOException {
        NGramWithNoSmoothing b = new NGramWithNoSmoothing(); 
        String fileName = args[0];
        String quote1 = "The president has relinquished his control of the company's board";
        String quote2 = "The chief executive officer said the last year revenue was good";
        b.fileRead(fileName);
        b.NGramNoSmoothing(quote1);        
        b.NGramNoSmoothing(quote2);
    }
    
   
    ArrayList<String> words = new ArrayList<>();
    ArrayList<String> sentences = new ArrayList<>();
    ArrayList<HashMap <String, Integer>> getCount = new ArrayList<HashMap <String, Integer>>();
    ArrayList<HashMap <String, Double>> calcProb = new ArrayList<HashMap <String, Double>>();
    HashMap<String, Integer> occurences = new HashMap<String, Integer>();
    int counts = 0;
    Double probability = 1.0;
    int total1 = 0;
    String temp1, temp2,var1,var2,var3;    
    
    
    
    private void fileRead(String path) throws FileNotFoundException, IOException {
        String splitBy1 = " ";
        for(String line : Files.readAllLines(Paths.get(path) , StandardCharsets.ISO_8859_1))
		{		
            for(String s : line.split(splitBy1))
			{
				if(s.has("_")||s.has("._.")||s.has("''_''")||s.has(",_,")||s.has("$_$"))
					continue;
                words.add(s);                
            }
		}
            
    } 

    void NGramNoSmoothing(String originalSen){       
        
        clearAll();
        String tempSen = originalSen;
        String splitBy = " ";
        
        for(String s : tempSen.split(splitBy)){
            sentences.add(s);
        }

        Iterator it = sentences.listIterator();
        while(it.hasNext()){
            String var = (String) it.next();

            for(int i = 0; i <words.size();i++){
                String var1 = words.get(i);
                if(var.equals(var1))
                    counts++;             
                
            }
            occurences.put(var, counts);
            counts = 0;
        }
        
        for(int i=0;i<sentences.size();i++){
            var1 = sentences.get(i);
            Double total = (occurences.get(var1)).doubleValue();
            
            for(int j=0;j<sentences.size();j++){
                var2 = sentences.get(j);
                for(int l = 0; l<words.size();l++){
                    var3 = words.get(l);
                    if(var1.equals(var3))
                        if(var2.equals(words.get(l+1)))
                            total1++;
                }
                HashMap<String, Integer> temp = new HashMap<String, Integer>();
                temp.put(var2, total1);
                HashMap<String, Double> temp1 = new HashMap<String, Double>();
                temp1.put(var2, total1/total);
                getCount.add(temp);
                calcProb.add(temp1);
               total1=0;
            }
        }

        System.out.println("Bigram Count (No Smoothing):");
        for(int i=0;i<sentences.size();i++)
            System.out.print(sentences.get(i)+"  ");
        System.out.println();
        for(int i=0;i<getCount.size();i++){
            if(i%11==0)
                System.out.print(sentences.get(i/11)+"   ");
            HashMap hash = getCount.get(i);
            
            System.out.print(hash.values()+"  ");
            if((i+1)%11==0){
                System.out.println();
                
            }
            
        }
        System.out.println("Bigram Probability (No Smoothing):");
        for(int i=0;i<sentences.size();i++)
            System.out.print(sentences.get(i)+"  ");
        System.out.println();
        for(int i=0;i<calcProb.size();i++){
            if(i%11==0)             // to frame the table
                System.out.print(sentences.get(i/11)+"   ");
            HashMap h = calcProb.get(i);
            
            System.out.print(h.values()+"  ");
            if((i+1)%11==0){
                System.out.println();
                
            }
            
        }
        
        for(int i=0;i<sentences.size()-1;i++){
            HashMap<String,Double> tempMap = new HashMap<String,Double>();
            tempMap = calcProb.get(i+1);

            String temp = sentences.get(i+1);
            if(tempMap.containsKey(temp))   {         

                probability = probability * tempMap.get(temp);            
            }
        }
        System.out.println("Final probability : "+probability);
        
    }
    
       void clearAll()
    {
        occurences.clear();
        calcProb.clear();
        getCount.clear();
        sentences.clear();
    }
    
    

    
}
