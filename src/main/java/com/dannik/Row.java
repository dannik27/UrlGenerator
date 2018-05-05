package com.dannik;

public class Row {

    int id;
    StringBuffer text;
    boolean injection;

    public Row(String text){
        this.text = new StringBuffer(text);
        id = Main.counter.getAndIncrement();
    }

    public String toString(){
        return id + "," + (injection ? "sql" : "normal") + "," + text.toString();
    }

    public String onlyText(){
        return text.toString();
    }

    public void replace(String from, String to){
        while(text.indexOf(from) != -1){
            text.replace(text.indexOf(from), text.indexOf(from) + from.length(), to);
        }
    }

    public void appendInjection(String injectionString){

        injection = injectionString != null;

        if(injection){
            text.delete(text.indexOf("id=") + 3, text.length());
            text.append(injectionString);
        }

    }

    public Row splitWords(){

        int index = 0;
        while(index < text.length()){
            if(splitable(text.charAt(index))){
                text.replace(index, index + 1,
                        ((index != 0)&&(text.charAt(index - 1)) == ' ' ? "" : " ") +
                                text.charAt(index) +
                                ((text.length() != index + 1)&&((text.charAt(index + 1) == ' ')) ? "" : " "));
                index += 1;
            }
            if((text.charAt(index) == '-')&&(index + 1 < text.length())&&(text.charAt(index + 1) == '-')){
                text.replace(index, index + 1, " " + text.charAt(index));
                index +=1;
            }
            index +=1;
        }

        return this;
    }
    
    private boolean splitable(char c){
        if((c == '.')||(c == '/')||(c == ':')||(c == '?')||(c == '=')||(c == '\'')||(c == '%')||
                (c == '#')||(c == ';')||(c == ',')||(c == '(')||(c == ')')||(c == '|')||(c == '\"')){
            return true;
        }else{
            return false;
        }


        
    }





}
