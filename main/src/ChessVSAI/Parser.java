package ChessVSAI;

import java.util.HashMap;
import java.util.Map;

public class Parser {
    public static Map<Integer,Integer> rowMap = new HashMap<>();
    public static Map<Character,Integer> colMap = new HashMap<>();
    public static int[] parse(String input) {
        rowMap.put(1,7);
        rowMap.put(2,6);
        rowMap.put(3,5);
        rowMap.put(4,4);
        rowMap.put(5,3);
        rowMap.put(6,2);
        rowMap.put(7,1);
        rowMap.put(8,0);

        colMap.put('a',0);
        colMap.put('b',1);
        colMap.put('c',2);
        colMap.put('d',3);
        colMap.put('e',4);
        colMap.put('f',5);
        colMap.put('g',6);
        colMap.put('h',7);

        char c = input.charAt(0);
        int num = Integer.parseInt(input.substring(1,2));

        int x1 = rowMap.get(num);
        int y1 = colMap.get(c);
        return new int[]{x1,y1};
    }
}
