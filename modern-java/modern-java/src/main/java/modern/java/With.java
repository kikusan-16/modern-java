package modern.java;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

public class With {
    public static void main(String[] args) {
        // try-with-resources構文を使うことでclose省略
        try(
                FileInputStream fi = new FileInputStream("file.csv");
                InputStreamReader is = new InputStreamReader(fi);
                BufferedReader br = new BufferedReader(is);
        ) {
            String line;
            int i = 0;

            while ((line = br.readLine()) != null) {
                if (i == 0) {
                    System.out.println(line);
                } else {
                    Person p = new Person();
                    String[] row = line.split(",");
                        p.setId(row[0]);
                        p.setName(row[1]);

                        // 処理

                        System.out.println(p.getId() + " " + p.getName());

                    System.out.print("\n");
                    i++;
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
