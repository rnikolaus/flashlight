package rnikolaus.flashlight;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class Morse {
	final private Map<Character,String> encode = new HashMap<Character,String>();
    final private Map<String,Character> decode = new HashMap<String,Character>();
    final private String SPACE =" ";
    final private String LETTERGAP = SPACE+SPACE+SPACE;
    final private String WORDGAP=SPACE+SPACE+SPACE+SPACE;//Wordgap usually has a lettergap before, so you have the 7 timeunits between words
   
    public Morse() {
        initCode();
    }
    
    private void addValue(Character character, String code){
        encode.put(character, code);
        decode.put(code, character);
    }
    private void initCode(){
        addValue('A', ".-");
        addValue('B', "-...");
        addValue('C', "-.-.");
        addValue('D', "-..");
        addValue('E', ".");
        addValue('F', "..-.");
        addValue('G', "--.");
        addValue('H', "....");
        addValue('I', "..");
        addValue('J', ".---");
        addValue('K', "-.-");
        addValue('L', ".-..");
        addValue('M', "--");
        addValue('N', "-.");
        addValue('O', "---");
        addValue('P', ".--.");
        addValue('Q', "--.-");
        addValue('R', ".-.");
        addValue('S', "...");
        addValue('T', "-");
        addValue('U', "..-");
        addValue('V', "...-");
        addValue('W', ".--");
        addValue('X', "-..-");
        addValue('Y', "-.--");
        addValue('Z', "--..");
        addValue('1', ".----");
        addValue('2', "..---");
        addValue('3', "...--");
        addValue('4', "....-");
        addValue('5', ".....");
        addValue('6', "-....");
        addValue('7', "--...");
        addValue('8', "---..");
        addValue('9', "----.");
        addValue('0', "-----");
    }

    /**
     * Decode the morse code string of a single character
     * @param morseCode
     * @return the character
     * @throws IllegalArgumentException if the morseCode fragment is unknown
     */
    public Character decodeSingle(String morseCode){
        if(!decode.containsKey(morseCode)){
            throw new IllegalArgumentException("Unknown Code: "+morseCode);
        }
        return decode.get(morseCode);
    }
    
    /**
     * Encode a single character to a morse code string
     * @param character
     * @return the morse code string
     * @throws IllegalArgumentException if it doesn't know the character
     */
    public String encodeSingle(Character character){
        if (SPACE.equals(character.toString()))return WORDGAP;
        if(!encode.containsKey(character)){
            throw new IllegalArgumentException("No code for character: "+character);
        }
        return encode.get(character)+LETTERGAP;
    }
    
    /**
     * Encodes any String to an equivalent morse code.
     * Umlauts, etc. are simply ignored
     * @param message
     * @return the morse code string
     */
    public String encodeMessage(String message){
        message = message.trim() 
        		.toUpperCase(Locale.ENGLISH)//the codemap is uppercase....
                .replaceAll("[^\\w\\s]", "")//remove special characters
                .replaceAll("\\s+", " ")//replace multiple spaces, tabs, etc. by a single space
                ;
        String result ="";
        for (char c :message.toCharArray()){
            result+=encodeSingle(c);
        }
        return result;
    }
    
    /**
     * Encodes a message to a list of Boolean signal states
     * @param cleartextMessage
     * @return a list of boolean values to represent the on/off states
     */
    
    public List<Boolean> messageToSignal(String cleartextMessage){	
        List<Boolean> result = new ArrayList<Boolean>();
        for (char c: encodeMessage(cleartextMessage).toCharArray()){
            if (c=='-'){
                for (int i=0;i<3;i++){
                    result.add(Boolean.TRUE);//dahs are 3 timeunits 
                }
                result.add(Boolean.FALSE);
            }else if (c=='.'){
                result.add(Boolean.TRUE);//dits are a single timeunit
                result.add(Boolean.FALSE);
            }else{
                result.add(Boolean.FALSE);
            }
        }
        
        return result;
    }
    

}
