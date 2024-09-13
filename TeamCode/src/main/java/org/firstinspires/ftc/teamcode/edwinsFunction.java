class edWinFunctions {
    final int HSV_MAX = 180;
    final int[] RANGES = new int[3];
    RANGES[0] = {170, 10};
    RANGES[1] = {10, 60};
    RANGES[2] = {105, 135};
    /*
     * 0: Red
     * 1: Yellow
     * 2: Blue
     */

    public edWinFunctions(){

    }

    public static int[] get_ivals(int[] colorRange){
        int[] ivals = new int[2];
        if (colorRange.get(0) > colorRange.get(1)){
            ivals[0] = new BetterTuples(colorRange.get(0), HSV_MAX);
            ivals[1] = new BetterTuples(0, colorRange.get(1));
        }
        else {
            ivals[0] = colorRange;
        }
    }

    public static getColor(){

    }
}