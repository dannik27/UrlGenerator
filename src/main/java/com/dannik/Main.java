package com.dannik;

import java.io.*;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Main {

    static List<String> r = Arrays.asList(
            "‘",
            "or","or","or","or","or","or",
            "=","=","=","=","=","=","=","=",
            "like",
            "select","select","select","select","select","select","select",
            "convert",
            "int",
            "char",
            "varchar",
            "nvarchar",
            "and","and","and","and","and",
            "orderby",
            ";",";",";",";",";",";",";",";",";",
            "union","union","union","union","union","union","union","union","union","union",
            "union select",
            "shutdown",
            "exec",
            "xp_cmdshell",
            "sp_execwebtask",
            "if",
            "else",
            "waitfor",
            "--",
            "ascii",
            "bin",
            "hex",
            "unhex",
            "base64",
            "dec",
            "rot13",
            "*",
            "password");

    static List<String> tautologies = Arrays.asList(
            "‘",
            "or",
            "=",
            "like",
            "select");

    static List<String> illegalLogic = Arrays.asList(
            "convert",
            "and",
            "orderby");

    static List<String> piggyBacket = Arrays.asList(
            ";");

    static List<String> unionQueries = Arrays.asList(
            "union",
            "union select");

    static List<String> procedures = Arrays.asList(
            "shutdown",
            "exec",
            "xp_cmdshell",
            "sp_execwebtask");

    static List<String> inferenceAttack = Arrays.asList(
            "and",
            "if",
            "else",
            "waitfor");

    static List<String> alternateEncoding = Arrays.asList(
            "ascii",
            "bin",
            "hex",
            "unhex",
            "base64",
            "dec",
            "rot13");

    static List<List<String>> lists = Arrays.asList(tautologies, illegalLogic, piggyBacket, unionQueries, procedures, inferenceAttack, alternateEncoding);

    static List<String> names = Arrays.asList("tautologies", "illegalLogic", "piggyBacket", "unionQueries", "procedures", "inferenceAttack", "alternateEncoding");


    static int index = 0;

    static String format(String text){

        text = text.substring(0, text.indexOf(","));
        if (text.indexOf(" ") != -1)
            text = text.substring(0, text.indexOf(" "));
        text = text.replace("\"", "");

        text = index++ + "\t" + text;

        if(rand(1) == 1){

            int attack = rand(6);

            int regularCount = rand(2);
            int specialCount = 1;


            for (int i = 0; i < regularCount ; i++){
                text += " " + r.get(rand(r.size() - 1));
            }
            for (int i = 0; i < specialCount ; i++){
                text += " " + lists.get(attack).get(rand(lists.get(attack).size() - 1));
            }



            text = text + "\t" + names.get(attack);
        }else{
            text = text + "\t" + "normal";
        }


        return text;
    }

    static List<String> types = Arrays.asList("T", "U", "B");

    public static int rand(int max){
        return ThreadLocalRandom.current().nextInt(0, max + 1);
    }


    public static AtomicInteger counter = new AtomicInteger(1);

    public static void main(String[] args) throws Exception {


        List<String> injections = Files.lines(Paths.get("injections"), StandardCharsets.UTF_8)
                .map(str -> str.substring(21))
                .collect(Collectors.toList());

        File input = new File("data1");
        File output = new File("output");
        output.delete();
        output.createNewFile();

        try(PrintWriter pw = new PrintWriter(Files.newBufferedWriter(
                Paths.get(output.toURI())))) {



            Files.lines(Paths.get(input.toURI()), StandardCharsets.UTF_8)
                .parallel()
                .map(Row::new)
                .peek(row -> {
                    row.replace("%3F", "?");
                    row.replace("%3D", "=");
                })
                .filter(row -> row.text.indexOf("php?id=") != -1)
                .peek(row ->{
                    row.appendInjection(injections.get(rand(injections.size() - 1)));
                })
               // .peek(Row::splitWords)
                .forEach(pw::println);


        } catch (Exception e) {
            e.printStackTrace();
        }



    }


    public static void createInjectionDatabase() throws IOException {

        File input = new File("injections1");

        input.delete();
        input.createNewFile();

        List<String> urls = Arrays.asList("https://primes.utm.edu/top20/page.php?id=1");
         //       "http://www.ohmsghana.com/pages/services.php?id=1",
         //       "http://www.pidsphil.org/journal_info.php?id=1",
          //      "http://www.bible-history.com/subcat.php?id=1",
          //      "http://modniditky.com/katalog.php?id=1");

        try(PrintWriter pw = new PrintWriter(Files.newBufferedWriter(
                Paths.get(input.toURI())))) {

            String command;
            Process process;
            InputStream is;
            InputStreamReader isr;
            BufferedReader br;

            for(String url : urls) {
                command = String.format("sqlmap --batch -v 5 --level=5 -u %s | grep PAYLOAD", url);

                String line = null;
                try {
                    process = new ProcessBuilder("bash", "-c", command).start();
                    is = process.getInputStream();
                    isr = new InputStreamReader(is);
                    br = new BufferedReader(isr);

                    process.waitFor();
                    while((line = br.readLine()) != null) {
                        pw.println(line);
                    }

                } catch (IOException ignored) {

                }
            }



        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    public static void saveUrlsToFile(String filename) throws IOException {

        File input = new File(filename);

        input.delete();
        input.createNewFile();



        try(PrintWriter pw = new PrintWriter(Files.newBufferedWriter(
                Paths.get(input.toURI())))) {



        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public static String generateInjectionString(String url, String injectionType) {

        String command = String.format("sqlmap --batch -v 0 -u %s --technique=%s | grep Payload:", url, injectionType);

        String line = null;
        try {
            Process process = new ProcessBuilder("bash", "-c", command).start();
            InputStream is = process.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);

            if(!process.waitFor(120, TimeUnit.SECONDS)) {

                process.destroy();
            }else{
                line = br.readLine();
            }



        } catch (IOException | InterruptedException ignored) {

        }

        if(line != null){
            return line.substring(15, line.length());
        }else{
            return null;
        }


    }




}
