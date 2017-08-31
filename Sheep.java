/* 
 * [Sheep.java]
 * object sheep 
 * can reproduce, eat bushes and is eaten by wolves 2nd lvl of food chain 
 * Ali Meshkat 
 * April 4, 2017
 */

import java.util.Random;
import java.util.ArrayList;
class Sheep extends Animal {
  private static int startHealth = 20;
  private static int maxHealth = 100;
  private static int reproductionHealth = maxHealth/2;
  
  Sheep(int health){
    super(health);
  }
  
  /*
   * eat  
   * This method accepts a creature (prey that's being eaten)
   * adds health of bush to sheep's health if it doesnt exceed max 
   */
  void eat(Creature prey){  
      if (prey.getPoisoned()){
        super.setPoisoned(true);
      }
      super.setHealth( super.getHealth() + prey.getHealth());
      if (super.getHealth() > maxHealth){
        super.setHealth(maxHealth);
     }
      prey.setHealth(0);
  }
  
  
  
  void die(){
    super.setHealth(0);
  }
  
  
  
    /*
   * move  
   * This method accepts the map, and the location of the sheep
   * suggests a move based on the objects surounding the sheep
   * returs a string representing the move eg. +x ,-y
   */
  public String move(Creature[][] map, int i, int j){
    ArrayList<String> foodChoices = new ArrayList<String>();
    
    if (!super.getAlreadyMoved()){
      super.setAlreadyMoved(true); // prevents moving twice in one rotation
      if (i < map[0].length-1){
        if (map[i+1][j] instanceof Bush || (map[i+1][j] instanceof Sheep && super.getHealth() > reproductionHealth) ){ // if theres a bush to his top 
          foodChoices.add("+y"); // adds bush to arraylist to so in case of multiple options, the move is random
        }
      }
      if (j < map.length-1){
        if (map[i][j+1] instanceof Bush || (map[i][j+1] instanceof Sheep && super.getHealth() > reproductionHealth)){
          foodChoices.add("+x");
        }
      }
      if (j > 0){
        if (map[i][j-1] instanceof Bush ||(map[i][j-1] instanceof Sheep && super.getHealth() > reproductionHealth)){
         foodChoices.add("-x");
        }
      }
      if (i > 0){
        if (map[i-1][j] instanceof Bush || (map[i-1][j] instanceof Sheep && super.getHealth() > reproductionHealth)){
          foodChoices.add("-y");
        }
      }
      Random random = new Random();
      int rand;
      if (foodChoices.size() != 0){ // avoid the direction being returned in case of multiple option of move(more than  1 bush around)
                              // this avoid the same direction always being taken in case of multiple bushes
        rand = random.nextInt(foodChoices.size());
        return foodChoices.get(rand);
      }
       // if no bush around, then random move 
      rand = random.nextInt(4 + 8); //adds a chance for the sheep to move the same path(+8)
      if (rand > 4 && !super.getLastMove().equals("stay")){
        return super.getLastMove(); // take same move 
      }else if (rand < 4){
        if (rand == 0){
          super.setLastMove("+x"); 
          return "+x";
        }else if (rand == 1){
          super.setLastMove("-x"); 
          return "-x";
        }else if (rand == 2){
          super.setLastMove("+y"); 
          return "+y";
        }else {
          super.setLastMove("-y"); 
          return "-y";
        }
      }
      
    } 
    return "stay";
  }
  
  //getters and setters 
  public static void setMaxHealth(int health){
    maxHealth = health;
  }
  public static int getMaxHealth(){
    return maxHealth;
  }
  public static void setReproductionHealth(int health){
    reproductionHealth = health;
  }
  public static int getReproductionHealth(){
    return reproductionHealth;
  }
  public static void setStartHealth(int sHealth){
    startHealth = sHealth;
  }
  public static int getStartHealth(){
    return startHealth;
  }
  
}