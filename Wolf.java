/* 
 * [Wolf.java]
 * object wolf 
 * eats sheep, fights other wolves, forms the highest lvl of food chain 
 * cannot reproduce 
 * Ali Meshkat 
 * April 4, 2017
 */
import java.util.Random;
import java.util.ArrayList;
class Wolf extends Animal implements Comparable{
  private static int startHealth = 20;
  private static int fightDamage = 1;
  private static int maxHealth = 100;
  
  Wolf( int health){
    super(health);
  }
  
  void eat(Creature prey){  
    if (prey.getPoisoned()){
      super.setPoisoned(true);
    }
    super.setHealth(super.getHealth() + prey.getHealth());
      if (super.getHealth() > maxHealth){
         super.setHealth(maxHealth);
       }
    prey.setHealth(0);
  }


  /*
   * fight   
   * This method accepts a creature(always a wolf)  
   * decreases health from both 
   * more from the one with less hp 
   * uses compareTo to find the stronger wolf 
   */
  void fight(Creature opponent){
    if (this.compareTo(opponent) == -1){ //if this is weaker 
      super.setHealth(super.getHealth() - fightDamage);
      opponent.setHealth(opponent.getHealth() - fightDamage/2);
    }else if (this.compareTo(opponent) == 1){ // if opponent is weaker 
      opponent.setHealth(opponent.getHealth() - fightDamage);
      super.setHealth(super.getHealth() - fightDamage/2);
    }else{ // if equal both take same damage
      super.setHealth(super.getHealth() - fightDamage);
      opponent.setHealth(opponent.getHealth() - fightDamage);
      
    }
  }
  void die(){//////////////////////////////keeep??????/
    super.setHealth(0);
  }
  
  
  /*
   * move  
   * This method accepts the map, and the location of the wolf
   * suggests a move based on the objects surounding the wolf
   * returs a string representing the move eg. +x ,-y
   */
  public String move(Creature[][] map, int i, int j){
    ArrayList<String> foodChoices = new ArrayList<String>();
    
    if (!super.getAlreadyMoved()){
      super.setAlreadyMoved(true); // prevents moving twice in one rotation
      if (i < map[0].length-1){
        if (map[i+1][j] instanceof Sheep){ // if theres a sheep to his top 
          foodChoices.add("+y"); // adds bush to arraylist to so in case of multiple options, the move is random
        }
      }
      if (j < map.length-1){
        if (map[i][j+1] instanceof Sheep){
          foodChoices.add("+x"); 
        }
      }
      if (j > 0){
        if (map[i][j-1] instanceof Sheep){
          foodChoices.add("-x");
        }
      }
      if (i > 0){
        if (map[i-1][j] instanceof Sheep){
          foodChoices.add("-y"); 
        }
      }
      
      Random random = new Random();
      int rand;
      if (foodChoices.size() != 0){ // avoid the direction being returned in case of multiple option of move(more than  1 bush around)
                         // this avoid the same direction always being taken in case of multiple prey
        rand = random.nextInt(foodChoices.size());
        return foodChoices.get(rand);
      }
       // if no sheep around, then random move 
      rand = random.nextInt(4 + 8); //adds a chance for the wolf to move the same path(+8)
      if (rand > 4 && !super.getLastMove().equals("stay")){
        return super.getLastMove();
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
  
    /*
   * compareTo  
   * This method accepts an object
   * compares the health of the two wolves 
   * returs an int showing which is stronger
   */
  public int compareTo(Object o){
    if (((Wolf)o).getHealth() > super.getHealth()){
      return -1;
    }else if (((Wolf)o).getHealth() < super.getHealth()){
      return 1;
    }else{
      return 0;
    }
  }
  
  //getters and setters
  public static void setFightDamage(int damage){
    fightDamage = damage;
  }
  int getFightDamage(){
    return fightDamage;
  }
  public static void setMaxHealth(int health){
    maxHealth = health;
  }
  public static int getMaxHealth(){
    return maxHealth;
  }
  public static void setStartHealth(int sHealth){
    startHealth = sHealth;
  }
  public static int getStartHealth(){
    return startHealth;
  }
}