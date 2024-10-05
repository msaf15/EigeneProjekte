package Chess_1VS1;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class Parser {
    public static Map<Integer,Integer> rowMap = new HashMap<>();
    public static Map<Character,Integer> colMap = new HashMap<>();
    public static Map<Integer,Character> inverseMap = new HashMap<>();
    public static int[] fromCordToIndex(String input) {
        rowMap.put(1, 7);
        rowMap.put(2, 6);
        rowMap.put(3, 5);
        rowMap.put(4, 4);
        rowMap.put(5, 3);
        rowMap.put(6, 2);
        rowMap.put(7, 1);
        rowMap.put(8, 0);

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
        rowMap.clear();
        colMap.clear();
        return new int[]{x1,y1};
    }
    public static String fromIndextoCord(int[] source) {
        inverseMap.put(0, 'a');
        inverseMap.put(1, 'b');
        inverseMap.put(2, 'c');
        inverseMap.put(3, 'd');
        inverseMap.put(4, 'e');
        inverseMap.put(5, 'f');
        inverseMap.put(6, 'g');
        inverseMap.put(7, 'h');


        rowMap.put(7, 1);
        rowMap.put(6, 2);
        rowMap.put(5, 3);
        rowMap.put(4, 4);
        rowMap.put(3, 5);
        rowMap.put(2, 6);
        rowMap.put(1, 7);
        rowMap.put(0, 8);
        char col = inverseMap.get(source[1]);
        int row = rowMap.get(source[0]);
        inverseMap.clear();
        rowMap.clear();
        return col + String.valueOf(row);
    }

    public static String invertCords(String source) {

        rowMap.put(8,1);
        rowMap.put(7,2);
        rowMap.put(6,3);
        rowMap.put(5,4);
        rowMap.put(4,5);
        rowMap.put(3,6);
        rowMap.put(2,7);
        rowMap.put(1,8);
        String s = String.valueOf(source.charAt(0)) + rowMap.get(Integer.parseInt(source.substring(1, 2)));
        rowMap.clear();
        return s;
    }
}
