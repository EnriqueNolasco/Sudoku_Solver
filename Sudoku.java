import java.util.*;

public class Sudoku {
    public static void main(String[] args) throws Exception    {
        Map<Integer, List<box>> rows = new HashMap<>(), columns = new HashMap<>(), boxes = new HashMap<>();
        ArrayList<Map<Integer, List<box>>> group = new ArrayList<>();
        group.add(rows); group.add(columns); group.add(boxes);
        int remaining = 81;

        Scanner scnr = new Scanner(System.in);

        box temp = new box(0, 0, 0);
        for(int i = 0; i < 9; i++)  {
            List<box> temp1 = new ArrayList<>();
            List<box> temp2 = new ArrayList<>();
            for(int j = 0; j < 9; j++)  {
                temp1.add(temp);
                temp2.add(temp);
            }
            columns.put(i, temp1);
            boxes.put(i, temp2);
        }

        System.out.println("Insert row by row in format: X X X X X X X X X X");
        System.out.println("For blank slots, insert 0.");

        int A = 0;
        int B = 0;
        int C = 0;
        for(int i = 1; i <= 9; i++) {
            System.out.print("Row " + i + ": ");
            String row = scnr.nextLine();
            row = row.replaceAll("\\s", "");
            List<box> lBox = new ArrayList<>();
            for(int j = 0; j <= 8; j++) {
                box c = new box(row.charAt(j) - '0', i - 1, j);
                if(c.getNumber() != 0) remaining--;
                lBox.add(c);
                columns.get(j).set((i - 1), c);
                if(i < 4)   {
                    if(j < 3)   {
                        boxes.get(0).set(A, c);
                        c.setBox(0);
                        A++;
                        if(A == 9) A = 0;
                    }
                    else if(j < 6)   {
                        boxes.get(1).set(B, c);
                        c.setBox(1);
                        B++;
                        if(B == 9) B = 0;
                    }
                    else    {
                        boxes.get(2).set(C, c);
                        c.setBox(2);
                        C++;
                        if(C == 9) C = 0;
                    }
                }
                else if(i < 7)  {
                    if(j < 3)   {
                        boxes.get(3).set(A, c);
                        c.setBox(3);
                        A++;
                        if(A == 9) A = 0;
                    }
                    else if(j < 6)   {
                        boxes.get(4).set(B, c);
                        c.setBox(4);
                        B++;
                        if(B == 9) B = 0;
                    }
                    else    {
                        boxes.get(5).set(C, c);
                        c.setBox(5);
                        C++;
                        if(C == 9) C = 0;
                    }
                }
                else    {
                    if(j < 3)   {
                        boxes.get(6).set(A, c);
                        c.setBox(6);
                        A++;
                        if(A == 9) A = 0;
                    }
                    else if(j < 6)   {
                        boxes.get(7).set(B, c);
                        c.setBox(7);
                        B++;
                        if(B == 9) B = 0;
                    }
                    else    {
                        boxes.get(8).set(C, c);
                        c.setBox(8);
                        C++;
                        if(C == 9) C = 0;
                    }
                }
            }
            rows.put(i - 1, lBox);
        }
        scnr.close();

        long startTime = System.nanoTime();

        for(int i = 0; i < 9; i++)  {
            for(box a : rows.get(i))    {
                if(a.getNumber() != 0)   {
                    int change = clearer(group, a);
                    remaining -= change;
                    if(remaining == 0) break;
                }
            }
            if(remaining == 0) break;
        }

        int[] temp0 = new int[2];
        while(remaining > 0)  {
            int event = 0;
            for(int i = 0; i < 9; i++)  {
                for(int j = 0; j < 9; j++)  {
                    temp0 = fun(group, boxes, i, j);
                    remaining -= temp0[0];
                    if(event != 1) event = temp0[1];
                    temp0 = equalBox(group, boxes, i, j);
                    remaining -= temp0[0];
                    if(event != 1) event = temp0[1];
                    temp0 = fun(group, rows, i, j);
                    remaining -= temp0[0];
                    if(event != 1) event = temp0[1];
                    temp0 = equalBox(group, rows, i, j);
                    remaining -= temp0[0];
                    if(event != 1) event = temp0[1];
                    temp0= fun(group, columns, i, j);
                    remaining -= temp0[0];
                    if(event != 1) event = temp0[1];
                    temp0 = equalBox(group, columns, i, j);
                    remaining -= temp0[0];
                    if(event != 1) event = temp0[1];
                    if(remaining == 0) break;
                }
                temp0 = uniqueNumbers(group, boxes, i);
                remaining -= temp0[0];
                if(event != 1) event = temp0[1];
                temp0= uniqueNumbers(group, rows, i);
                remaining -= temp0[0];
                if(event != 1) event = temp0[1];
                temp0 = uniqueNumbers(group, columns, i);
                remaining -= temp0[0];
                if(event != 1) event = temp0[1];
                if(remaining == 0) break;
            }
            if(event == 0)  {
                throw new Exception("Nothing was found. Need more information");
            }
        }

        long endTime = System.nanoTime();
        System.out.println((endTime - startTime) / 1_000_000_000.0);

        scnr.close();

        System.out.println();
        for(int i = 0; i < 9; i++) {
            for(int j = 0; j < 9; j++)    {
                System.out.print(rows.get(i).get(j).getNumber() + " ");
                if(j % 3 == 2) System.out.print(" ");
            }
            System.out.println();
            if(i % 3 == 2) System.out.println();
        }
    }

    public static int specificClearer(ArrayList<Map<Integer, List<box>>> group, Map<Integer, List<box>> map, box a, int check)  {
        int change = 0;
        for(box b : map.get(check)) {
            if(b.getNumber() == 0 && b.getPossibilities()[a.getNumber() - 1])   {
                int redo = b.setPossibility(a.getNumber() - 1);
                if(redo != 0)    {
                    try {
                        b.setNumber(redo);
                    }
                    catch(Exception e)   {
                        e.printStackTrace();
                    }
                    change += clearer(group, b) + 1;
                }
            }
        }
        return change;
    }
    public static int clearer(ArrayList<Map<Integer, List<box>>> group, box a)    {
        int changes = 0;
        changes += specificClearer(group, group.get(0), a, a.getRow());
        changes += specificClearer(group, group.get(1), a, a.getColumn());
        changes += specificClearer(group, group.get(2), a, a.getBox());
        return changes;
    }

    public static int[] fun(ArrayList<Map<Integer, List<box>>> group, Map<Integer, List<box>> map, int i, int j)   {
        int checkRow = 0, checkColumn = 0, check = 0, getter = 0, event = 0; boolean same2 = true;

        ArrayList<Integer> p = new ArrayList<>();
        for(int k = 0; k < 9; k++)  {
            box b = map.get(j).get(k);
            if(b.getNumber() == 0 && b.getPossibilities()[i]) p.add(k);
        }
        if(p.size() == 0);
        else if(p.size() == 1)   {
            box b = map.get(j).get(p.get(0));
            try {
                b.setNumber(i + 1);
            }
            catch(Exception e)   {
                e.printStackTrace();
            }
            event = 1;
            int change = clearer(group, b);
            return new int[] {change + 1, event};
        }
        else    {
            boolean same = true;
            if(map.equals(group.get(2)))   {
                checkRow = map.get(j).get(p.get(0)).getRow();
                checkColumn = map.get(j).get(p.get(0)).getColumn();
            }
            else check = map.get(j).get(p.get(0)).getBox();
            for(int k = 0; k < p.size(); k++)   {
                if(map.equals(group.get(2)))   {
                    if(map.get(j).get(p.get(k)).getRow() != checkRow) same = false;
                    if(map.get(j).get(p.get(k)).getColumn() != checkColumn) same2 = false;
                }
                else    {
                    if(map.get(j).get(p.get(k)).getBox() != check)    {
                        same = false;
                        break;
                    }
                }
            }
            if(map.equals(group.get(2)))   {
                if(same != same2)   {
                    if(same)  {
                        int change = 0;
                        for(box b : group.get(0).get(map.get(j).get(p.get(0)).getRow())) {
                            if(b.getBox() != j && b.getNumber() == 0 && b.getPossibilities()[i])   {
                                int res = b.setPossibility(i);
                                event = 1;
                                if(res != 0) {
                                    try {
                                        b.setNumber(res);
                                    }
                                    catch(Exception e)   {
                                        e.printStackTrace();
                                    }
                                    change += clearer(group, b) + 1;
                                }
                            }
                        }
                        return new int[] {change, event};
                    }
                    else  {
                        int change = 0;
                        for(box b : group.get(1).get(map.get(j).get(p.get(0)).getColumn())) {
                            if(b.getBox() != j && b.getNumber() == 0 && b.getPossibilities()[i])   {
                                int res = b.setPossibility(i);
                                event = 1;
                                if(res != 0) {
                                    try {
                                        b.setNumber(res);
                                    }
                                    catch(Exception e)   {
                                        e.printStackTrace();
                                    }
                                    change += clearer(group, b) + 1;
                                }
                            }
                        }
                        return new int[] {change, event};
                    }
                }
            }
            else    {
                if(same)  {
                    int change = 0;
                    for(box b : group.get(2).get(map.get(j).get(p.get(0)).getBox())) {
                        if(map.equals(group.get(0))) getter = b.getRow();
                        else getter = b.getColumn();
                        if(getter != j && b.getNumber() == 0 && b.getPossibilities()[i])   {
                            int res = b.setPossibility(i);
                            event = 1;
                            if(res != 0) {
                                try {
                                    b.setNumber(res);
                                }
                                catch(Exception e)   {
                                    e.printStackTrace();
                                }
                                change += clearer(group, b) + 1;
                            }
                        }
                    }
                    return new int[] {change, event};
                }
            }
        }
        return new int[] {0, event};
    }

    public static int[] equalBox(ArrayList<Map<Integer, List<box>>> group, Map<Integer, List<box>> map, int i, int j) {
        int event = 0;
        box b = map.get(i).get(j);
        if(b.getNumber() == 0)   {
            ArrayList<Integer> p = new ArrayList<>();
            p.add(j);
            for(int k = j + 1; k < 9; k++) if(Arrays.equals(b.getPossibilities(), map.get(i).get(k).getPossibilities())) p.add(k);
            if(p.size() == b.getTrues()) {
                for(int k = 0; k < 9; k++)  {
                    if(map.get(i).get(k).getNumber() == 0 && !p.contains(k))   {
                        for(int l = 0; l < 9; l++)  {
                            if(b.getPossibilities()[l] && b.getPossibilities()[l] == map.get(i).get(k).getPossibilities()[l])   {
                                int sub = map.get(i).get(k).setPossibility(l);
                                event = 1;
                                if(sub != 0)    {
                                    try {
                                        map.get(i).get(k).setNumber(sub);
                                    }
                                    catch(Exception e)   {
                                        e.printStackTrace();
                                    }
                                    int change = clearer(group, map.get(i).get(k));
                                    return new int[] {change + 1, event};
                                }
                            }
                        }
                    }
                }
            }
        }
        return new int[] {0, event};
    }

    public static int[] uniqueNumbers(ArrayList<Map<Integer, List<box>>> group, Map<Integer, List<box>> map, int i)    {
        int event = 0;
        Map<Integer, ArrayList<Integer>> P = new HashMap<>();
        for(int j = 0; j < 9; j++)  {
            ArrayList<Integer> p = new ArrayList<>();
            for(int k = 0; k < 9; k++)  {
                box b = map.get(i).get(k);
                if(b.getNumber() == 0 && b.getPossibilities()[j]) p.add(k);
            }
            P.put(j, p);
        }
        for(int j = 0; j < 9; j++)  {
            ArrayList<Integer> check = P.get(j);
            ArrayList<Integer> equalList = new ArrayList<>();
            int checkLimit = check.size();
            int checkNum = 1;

            if(check.size() == 0);
            else if(check.size() == 1)   {
                box b =  map.get(i).get(check.get(0));
                try {
                    b.setNumber(j + 1);
                }
                catch(Exception e)   {
                    e.printStackTrace();
                }
                event = 1;
                int change = clearer(group, b);
                return new int[] {change + 1, event};
            }
            else    {
                equalList.add(j);
                for(int k = j + 1; k < 9; k++)  {
                    if(check.equals(P.get(k)))   {
                        equalList.add(k);
                        checkNum++;
                    }
                    if(checkNum == checkLimit) break;
                }
                if(checkNum == checkLimit)  {
                    for(int k : check)  {
                        box b = map.get(i).get(k);
                        for(int l = 0; l < 9; l++)  {
                            if(!equalList.contains(l) && b.getPossibilities()[l])  {
                                int con = b.setPossibility(l);
                                event = 1;
                                if(con != 0)    {
                                    try {
                                        b.setNumber(con);
                                    }
                                    catch(Exception e)   {
                                        e.printStackTrace();
                                    }
                                    int change = clearer(group, b);
                                    return new int[] {change + 1, event};
                                }
                            }
                        }
                    }
                }
            }
        }
        return new int[] {0, event};
    }
}