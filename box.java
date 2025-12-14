public class box {
    private int number;
    private boolean[] poss = {true, true, true, true, true, true, true, true, true};
    private int trueCount = 9;
    private int row;
    private int column;
    private int boxNum;

    public box(int number, int row, int column) throws Exception  {
        this.number = number;
        if(number != 0) {
            for(int i = 0; i < 9; i++)  {
                this.poss[i] = false;
            }
        }
        this.row = row;
        this.column = column;
    }

    public void setBox(int boxNum)  {
        this.boxNum = boxNum;
    }

    public void setNumber(int number) throws Exception   {
        this.number = number;
        for(int i = 0; i < 9; i++)  {
            this.poss[i] = false;
        }
    }

    public int setPossibility(int possi)    {
        this.poss[possi] = false;
        this.trueCount--;
        int results = 0;
        if(this.trueCount == 1) {
            for(int i = 0; i < 9; i++)  {
                if(this.poss[i] == true)    {
                    results = i + 1;
                    break;
                }
            }
        }
        return results;
    }

    public int getBox()    {
        return boxNum;
    }

    public int getNumber()  {
        return number;
    }

    public boolean[] getPossibilities() {
        return poss;
    }

    public int getRow() {
        return row;
    }

    public int getColumn()  {
        return column;
    }

    public int getTrues()   {
        return trueCount;
    }
}
