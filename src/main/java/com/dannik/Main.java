package com.dannik;

import java.io.*;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Main {





    public static int rand(int max){
        return ThreadLocalRandom.current().nextInt(0, max + 1);
    }


    public static AtomicInteger counter = new AtomicInteger(368);

    public static void main(String[] args) {

        String[] input = null;

        try(Scanner scanner = new Scanner(System.in)){
            while (scanner.hasNext()){
                input = scanner.nextLine().split(" ");

                if(input[0].equals("exit")){
                    System.exit(0);
                }

                switch (input[0]){
                    case "g":
                    case "generate_urls_spec":
                        generateUrlsSpec(input[1], Integer.valueOf(input[2]), 0);
                        System.out.println("ok");
                        break;
                    case "generate_urls_spec_params":
                        generateUrlsSpec(input[1], Integer.valueOf(input[2]), 1);
                        System.out.println("ok");
                        break;
                    case "delete_from_start":
                        deleteFromStart(input[1], Integer.valueOf(input[2]));
                        System.out.println("ok");
                        break;
                    case "copy":
                        copyFile(input[1], input[2]);
                        System.out.println("ok");
                        break;
                    case "remove":
                        removeFragment(input[1], input[2]);
                        System.out.println("ok");
                        break;
                    case "split":
                        splitWords(input[1]);
                        System.out.println("ok");
                        break;
                    case "clear_whitespaces":
                        clearWhitespaces(input[1]);
                        System.out.println("ok");
                        break;
                    case "lower":
                        toLowerCase(input[1]);
                        System.out.println("ok");
                        break;
                    case "concat":
                        concatUrls(input[1], input[2]);
                        System.out.println("ok");
                        break;
                    case "union":
                        unionFiles(input[1], input[2]);
                        System.out.println("ok");
                        break;
                    case "prefix":
                        appendPrefix(input[1], input[2], Integer.valueOf(input[3]));
                        System.out.println("ok");
                        break;
                    default:
                        System.out.println("Wrong command");
                }


            }

        }



    }

    private static void clearWhitespaces(String filename) {
        File output = new File(filename);
        List<String> lines = null;
        try {
            lines = Files.lines(Paths.get(filename), StandardCharsets.UTF_8)
                    .map(str -> str.replaceAll("\\s{2,}", " "))
                    .collect(Collectors.toList());
            output.delete();
            output.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try(PrintWriter pw = new PrintWriter(Files.newBufferedWriter(
                Paths.get(output.toURI())))) {
            for(String line: lines){
                pw.println(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void unionFiles(String filename, String s2) {
        File output = new File(filename);
        List<String> lines = null;
        try {
            lines = Files.lines(Paths.get(filename), StandardCharsets.UTF_8)
                    .collect(Collectors.toList());
            output.delete();
            output.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        List<String> file2 = null;
        try {
            file2 = Files.lines(Paths.get(s2), StandardCharsets.UTF_8)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
        }

        try(PrintWriter pw = new PrintWriter(Files.newBufferedWriter(
                Paths.get(output.toURI())))) {

            for(String line: lines){
                pw.println(line);
            }
            for(String line: file2){
                pw.println(line);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void toLowerCase(String filename) {
        File output = new File(filename);
        List<String> lines = null;
        try {
            lines = Files.lines(Paths.get(filename), StandardCharsets.UTF_8)
                    .collect(Collectors.toList());
            output.delete();
            output.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try(PrintWriter pw = new PrintWriter(Files.newBufferedWriter(
                Paths.get(output.toURI())))) {

            for(String line: lines){
                pw.println(line.toLowerCase());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void appendPrefix(String filename, String type,  Integer startindex) {
        File output = new File(filename);
        List<String> lines = null;
        try {
            lines = Files.lines(Paths.get(filename), StandardCharsets.UTF_8)
                    .collect(Collectors.toList());
            output.delete();
            output.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try(PrintWriter pw = new PrintWriter(Files.newBufferedWriter(
                Paths.get(output.toURI())))) {
            for(String line: lines){
                pw.println(startindex + "," + type + "," + line);
                startindex += 1;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void concatUrls(String filename, String s2) {

        File output = new File(filename);
        List<String> lines = null;
        try {
            lines = Files.lines(Paths.get(filename), StandardCharsets.UTF_8)
                    .collect(Collectors.toList());
            output.delete();
            output.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        List<String> file2 = null;
        try {
            file2 = Files.lines(Paths.get(s2), StandardCharsets.UTF_8)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
        }

        try(PrintWriter pw = new PrintWriter(Files.newBufferedWriter(
                Paths.get(output.toURI())))) {
            for(String line: lines){
                pw.println(line + " " + file2.get(rand(file2.size() - 1)));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void splitWords(String filename) {
        File output = new File(filename);
        List<String> lines = null;
        try {
            lines = Files.lines(Paths.get(filename), StandardCharsets.UTF_8)
                    .map(Row::new)
                    .peek(Row::splitWords)
                    .map(Row::onlyText)
                    .collect(Collectors.toList());
            output.delete();
            output.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try(PrintWriter pw = new PrintWriter(Files.newBufferedWriter(
                Paths.get(output.toURI())))) {
            for(String line: lines){
                pw.println(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void removeFragment(String filename, String fragment) {
        File output = new File(filename);
        List<String> lines = null;
        try {
            lines = Files.lines(Paths.get(filename), StandardCharsets.UTF_8)
                    .map(str -> str.replaceAll(fragment, ""))
                    .filter(str -> str.length() > 0)
                    .collect(Collectors.toList());
            output.delete();
            output.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try(PrintWriter pw = new PrintWriter(Files.newBufferedWriter(
                Paths.get(output.toURI())))) {
            for(String line: lines){
                pw.println(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void copyFile(String s, String s1) {

        try {
            File output = new File(s1);
            output.delete();


            Files.copy(Paths.get(s), Paths.get(s1));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void deleteFromStart(String filename, Integer count) {
        File output = new File(filename);
        List<String> lines = null;
        try {
            lines = Files.lines(Paths.get(filename), StandardCharsets.UTF_8)
                    .map(str -> str.substring(count))
                    .collect(Collectors.toList());
            output.delete();
            output.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try(PrintWriter pw = new PrintWriter(Files.newBufferedWriter(
                Paths.get(output.toURI())))) {
            for(String line: lines){
                pw.println(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static void generateUrlsSpec(String filename, int count, int minParams){
        File output = new File(filename);
        output.delete();
        try {
            output.createNewFile();
        } catch (IOException ignore) { }

        try(PrintWriter pw = new PrintWriter(Files.newBufferedWriter(
                Paths.get(output.toURI())))) {

            StringBuffer sb;

            for(int j = 0; j< count; j++){
                int dots = 2 + rand(2);
                int slashes = rand(4);
                int params = minParams + rand(3);

                sb = new StringBuffer();

                for(int i=0; i < dots; i++){
                    sb.append(". ");
                }

                for(int i=0; i < slashes; i++){
                    sb.append("/ ");
                }

                if(params > 0){
                    sb.append("? ");

                    for(int i=0; i < params; i++){
                        sb.append("= & ");
                    }
                }

                sb.replace(sb.length() - 2, sb.length(), "");
                pw.println(sb.toString());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }



    static void dododo() throws Exception{
        List<String> injections = Files.lines(Paths.get("injections"), StandardCharsets.UTF_8)
                .map(str -> str.substring(21))
                .collect(Collectors.toList());

        File input = new File("data1");
        File output = new File("output20");
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
                        //  row.appendInjection(injections.get(rand(injections.size() - 1)));
                        row.splitWords();
                    })
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
