package com.dannik;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
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

    public static int rand(int max){
        return ThreadLocalRandom.current().nextInt(0, max + 1);
    }



    public static void main(String[] args) throws IOException {

        File input = new File("data");
        File output = new File("output");
        output.delete();
        output.createNewFile();

        Stream stream = Files.lines(Paths.get(input.toURI()), StandardCharsets.UTF_8)
                .map(Main::format);

        Files.write(Paths.get(output.toURI()), stream::iterator);

    }




}
