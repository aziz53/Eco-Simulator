/* 
 * [Bush.java]
 * object bush, lowest lvl of the food chain (food for sheep)
 * spaws randomly around the map
 * Ali Meshkat 
 * April 4, 2017
 */
class Bush extends Creature {

  Bush(int health){
    super(health);
  }
  
  void die(){
    super.setHealth(0);
  }
  

  
}
