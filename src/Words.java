import java.io.IOError;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Random;
import java.util.Scanner;
import java.io.File;

/**
 * @author ii79
 * @version 1.0
 * Class that creates an Array List of 8636 5-letter words
 */
public class Words {
    public static ArrayList<String> list =  new ArrayList<>();


    /**public static void readFile() throws IOException {} **/

    public static void printList() {
        for (int i = 0; i < list.size() ; i++) {
            System.out.println(list.get(i));
        }
    }

    public static ArrayList<String> createList() throws IOException {
        try {
            File file = new File("/Users/ishaperry/Library/CloudStorage/OneDrive-GeorgiaInstituteofTechnology/CS_1331/Jordle1/words.txt");
            Scanner sc = new Scanner(file);
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                if (line.trim().length() == 5) {
                    list.add(line.trim());
                }
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        return list;
    }

    public static void main(String[] args) throws IOException  {
       list = createList();
        Random rand = new Random();
       int wordIndex = rand.nextInt(Words.list.size());
       System.out.println(wordIndex);




    }




}
