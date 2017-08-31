/* 
 * [Creature.java]
 * abstarct class for all living things 
 * includes health, setters and getters, abstract classes 
 * Ali Meshkat 
 * April 4, 2017
 */
abstract class Creature{
  private int health; //hp of creature 
  private boolean poisoned; //whether poisoned or not 
  
  
  //constructor sets health 
  Creature(int health){
    this.health = health;
  }
  


  
  
  //getters and setters 
  void setHealth(int health){
    this.health = health;
  }
  int getHealth(){
  return this.health;
  }
  void setPoisoned(boolean yesOrNo){
    this.poisoned = yesOrNo;
  }
  boolean getPoisoned(){
    return this.poisoned;
  }
}