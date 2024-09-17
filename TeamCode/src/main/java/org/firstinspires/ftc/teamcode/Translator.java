class Translator {
    public static void main(String[] args){
        while (is_auto()){
            Pixel[] frame = camerainput()
            int color = edWinFunctions.get_color_of_brick(frame);
            // if correct color, pick up
            int TEAM_COLOR;
            if (TEAM_NAME == "red"){
                TEAM_COLOR = 1;
            }
            else if (TEAM_NAME == "blue"){
                TEAM_COLOR = 0;
            }

            /* Jack's Note:
             * I made it so that if the color is the team color or yellow,
             * it'll try to grab it, instead of only picking up our team color.
             */
            if (color == TEAM_COLOR || color == 2){
                pick_up()
            }
        }
    }
}