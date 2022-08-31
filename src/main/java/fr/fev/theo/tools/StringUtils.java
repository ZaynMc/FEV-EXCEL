package fr.fev.theo.tools;

import fr.fev.theo.Main;

public class StringUtils {

    public Main pl;

    public StringUtils(Main pl) {
        this.pl = pl;
    }

    public String getwordInString(String sentence, String[] filter, String slipString) {
        for(String st : sentence.split(slipString)) {
            for(String filte : filter) {
                if(st.startsWith(filte)) {
                    return st;
                }
            }

        }
        return "";
    }
}
