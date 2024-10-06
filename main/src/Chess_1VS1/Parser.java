package Chess_1VS1;

import java.util.HashMap;

public class Parser {
    public static int[] fromCordToIndex(String input) {
        HashMap<Integer,Integer> rowMap = getCordToIndexRowMap();
        HashMap<Character,Integer> colMap = getCordToIndexColMap();
        char c = input.charAt(0);
        int num = Integer.parseInt(input.substring(1, 2));

        System.out.println(input);
        System.out.println("Parsed row (num): " + num);
        System.out.println("Parsed column (char): " + c);
        System.out.println(rowMap.size());
        System.out.println(colMap.size());
        Integer x1 = rowMap.get(num);
        Integer y1 = colMap.get(c);
        return new int[]{x1, y1};
    }

    public static String fromIndextoCord(int[] source) {
        HashMap<Integer,Integer> rowMap = getFromIndexToCordRowMap();
        HashMap<Integer, Character> colMap = getFromIndexToCordColMap();


        char col = colMap.get(source[1]);
        int row = rowMap.get(source[0]);
        return col + String.valueOf(row);
    }

    public static String invertCords(String source) {
        HashMap<Integer,Integer> rowMap = getInvertCordsRowMap();
        String s = String.valueOf(source.charAt(0)) + rowMap.get(Integer.parseInt(source.substring(1, 2)));
        rowMap.clear();
        return s;
    }

    public static HashMap<Integer,Integer> getCordToIndexRowMap() {
        HashMap<Integer,Integer> rowMap = new HashMap<>();
        rowMap.put(1, 7);
        rowMap.put(2, 6);
        rowMap.put(3, 5);
        rowMap.put(4, 4);
        rowMap.put(5, 3);
        rowMap.put(6, 2);
        rowMap.put(7, 1);
        rowMap.put(8, 0);
        return rowMap;
    }

    public static HashMap<Character,Integer> getCordToIndexColMap() {
        HashMap<Character,Integer> colMap = new HashMap<>();
        colMap.put('a', 0);
        colMap.put('b', 1);
        colMap.put('c', 2);
        colMap.put('d', 3);
        colMap.put('e', 4);
        colMap.put('f', 5);
        colMap.put('g', 6);
        colMap.put('h', 7);
        return colMap;
    }

    public static HashMap<Integer,Integer> getFromIndexToCordRowMap() {
        HashMap<Integer,Integer> rowMap = new HashMap<>();
        rowMap.put(7, 1);
        rowMap.put(6, 2);
        rowMap.put(5, 3);
        rowMap.put(4, 4);
        rowMap.put(3, 5);
        rowMap.put(2, 6);
        rowMap.put(1, 7);
        rowMap.put(0, 8);
        return rowMap;
    }

    public static HashMap<Integer,Character> getFromIndexToCordColMap() {
        HashMap<Integer,Character> colMap = new HashMap<>();
        colMap.put(0, 'a');
        colMap.put(1, 'b');
        colMap.put(2, 'c');
        colMap.put(3, 'd');
        colMap.put(4, 'e');
        colMap.put(5, 'f');
        colMap.put(6, 'g');
        colMap.put(7, 'h');
        return colMap;
    }
    public static HashMap<Integer,Integer> getInvertCordsRowMap() {
        HashMap<Integer,Integer> rowMap = new HashMap<>();
        rowMap.put(8,1);
        rowMap.put(7,2);
        rowMap.put(6,3);
        rowMap.put(5,4);
        rowMap.put(4,5);
        rowMap.put(3,6);
        rowMap.put(2,7);
        rowMap.put(1,8);
        return rowMap;
    }

}
