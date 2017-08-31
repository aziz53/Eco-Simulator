/* 
 * [EcoSystem.java]
 * An Eco-System simulator including wolves, sheep, and plants 
 * Ali Meshkat 
 * April 4, 2017
 */
import java.util.Random;
import java.util.Scanner; 
class EcoSystem{
  public static void main(String[]args){
    Scanner input = new Scanner(System.in);
    

    int bushSpawnRate = 1;      //chance of plant being spawn per null after initial spawings in percent
    
    //initial spawing rates (used respective  to each other )
    int grassSpawnRate, sheepSpawnRate, wolfSpawnRate, bushStartingRate;
    
    //other
    int reproductionHealth = 10;
    int healthLostPerRound = 1;
    int poisonHealthLoss = 2;
    int moveNum = 0;
    int mapSize = 100;
    int bushHealth = 50;

    //inputs for initial setup info 
    System.out.print("grassSpawnRate?");
    grassSpawnRate = input.nextInt();
    System.out.print("bushStartingRate?");
    bushStartingRate = input.nextInt();
    System.out.print("sheepSpawnRate?");
    sheepSpawnRate = input.nextInt();
    System.out.print("wolfSpawnRate?");
    wolfSpawnRate = input.nextInt();
    
    
    //if user wants to change advanced variables 
    System.out.print("Advanced settings?  1.yes   2.No");
    int yesOrNo = input.nextInt();
    if (yesOrNo == 1){
      System.out.print("mapSize?");
      mapSize = input.nextInt();
      System.out.print("reproductionHealth?");
      reproductionHealth = input.nextInt();
      System.out.print("fightDamage?");
      Wolf.setFightDamage(input.nextInt());
      System.out.print("WolfMaxHealth ?");
      Wolf.setMaxHealth(input.nextInt());
      System.out.print("SheepMaxHealth ?");
      Sheep.setMaxHealth(input.nextInt());
      System.out.print("poisonHealthLoss ?");
      poisonHealthLoss = (input.nextInt());
      System.out.print("healthLostPerRound ?");
      healthLostPerRound = (input.nextInt());
      System.out.print("bushHealth ?");
      bushHealth = (input.nextInt());
    }

    Creature[][] map = new Creature[mapSize][mapSize];
    DisplayGrid grid = new DisplayGrid(map);
    createMap(map, grassSpawnRate, bushStartingRate, sheepSpawnRate, wolfSpawnRate, bushHealth);
    
    input.close();
    grid.refresh(moveNum);

    
    do{


      try {
            Thread.sleep(10);
      } catch (InterruptedException ie){}

      
      moveAnimals(map, reproductionHealth);
      reset(map, healthLostPerRound, poisonHealthLoss);
      spawn( map, bushSpawnRate);
      moveNum ++;
      grid.refresh(moveNum);
    }while  (count(map, "sheep") + count(map, "pSheep") != 0 && count(map, "wolf") + count(map, "pWolf")!= 0 );
    
    System.out.println("End");
    System.out.println("Sheeps: " +count(map, "sheep"));
    System.out.println("Poisoned Sheeps: " + count(map, "pSheep"));
    System.out.println("Wolves: " +count(map, "wolf"));
    System.out.println("Poisoned wolves: " +count(map, "pWolf"));
    System.out.println("Poisonous Bushes: " +count(map, "pBush"));
    System.out.println("Bushes: " +count(map, "bush"));
    System.out.println("Empty: " +count(map, "empty"));
    System.out.println("Moves: " +moveNum);

    
                                                                                                                                                                               
                                                                                                                                                                               
                                                                                                                                                                               
                                                                                                                                                                               

  }//////////////////////////////////////////    END OF MAIN    //////////////////////////////////////////////////////////////////////////////////////////
  
  
  
  
  /*
   * createMap  
   * This method accepts the map, and starting spawn rates 
   * and randomly spawns creatures in the map based on the chances 
   */
  static void createMap(Creature[][] map, int grassSpawnRate, int bushStartingRate, int sheepSpawnRate, 
                        int wolfSpawnRate,int  bushHealth){
    Random random = new Random();
    int rand;
    for(int i = 0; i < map[0].length; i++){
      for(int j = 0;j < map.length; j++){
        rand = random.nextInt(grassSpawnRate + bushStartingRate + sheepSpawnRate + wolfSpawnRate +1); 
        //selects object based on the range of rate in which the random number is into eg. 0-10 or 11-24 or 25-44
        if (rand < grassSpawnRate+1){
          map[i][j] = null;
        }else if (grassSpawnRate < rand && rand < grassSpawnRate + bushStartingRate +1 ){
          map[i][j] = new Bush(bushHealth); // creates bush 
          rand = random.nextInt(100); // 0 to 99 random number 
          if(rand == 0){ // one percent chance of getting a poisoned plant
            ((Bush)map[i][j]).setPoisoned(true);
          }
        }else if (grassSpawnRate + bushStartingRate  < rand && rand < grassSpawnRate + bushStartingRate + sheepSpawnRate + 1 ){
          map[i][j] = new Sheep(Sheep.getStartHealth());
        }else if (grassSpawnRate + bushStartingRate + sheepSpawnRate < rand && rand < grassSpawnRate + bushStartingRate + sheepSpawnRate + wolfSpawnRate +1 ){
          map[i][j] = new Wolf(Wolf.getStartHealth());
        }
      }
    }
  }
  

  /*
   * moveAnimals  
   * This method accepts the map, and the reproduction health  
   * runs through the map and runs the move method for each 
   * animal then runs the checkMove method for the move to be made 
   * if possible 
   */
  static void moveAnimals(Creature[][] map,  int reproductionHealth){
    String moveDirection = "";
    for(int i = 0; i < map[0].length; i++){
      for(int j = 0;j < map.length; j++){;
        moveDirection = "";

        //gets the direction of desired move 
        if(map[i][j] instanceof Sheep){         
          moveDirection = ((Sheep)map[i][j]).move(map, i, j);
        }else if (map[i][j] instanceof Wolf){
          moveDirection = ((Wolf)map[i][j]).move(map, i, j);
        }
        
        //moves if possible
        if (moveDirection.equals("+x") && j != map.length-1){  // if +x is the move chosen 
          checkMove(map, i, j, i , j+1, reproductionHealth); //checks +x 
        }else if (moveDirection.equals("-x") && j != 0){
          checkMove(map, i, j, i , j-1, reproductionHealth); 
        }else if (moveDirection.equals("+y") && i != map[0].length-1 ){
          checkMove(map, i, j, i+1 , j, reproductionHealth); 
        }else if(moveDirection.equals("-y") && i != 0){
          checkMove(map, i, j, i-1 , j, reproductionHealth); 
        }else if (!moveDirection.equals("")){ // if type animal and not moved(obstacle in way)
          ((Animal)map[i][j]).setLastMove("stay"); // change last move so taking another path is more likely 
        }
      }
    }
  }
  
  
    /*
   * checkMove  
   * This method accepts the map, the initial coordinates of move being made and the destication coordinates 
   * used in mathod move animal and checks if the move can be made and runs functions required(eg. eat)
   */
  static void checkMove(Creature[][] map, int i , int j, int i2, int j2, int reproductionHealth){
   // System.out.println(map[i][j].getHealth());
    if (map[i][j] instanceof Sheep){ // if sheep is moving
      if (map[i2][j2] instanceof Bush){ // into a bush 

          ((Sheep)map[i][j]).eat(map[i2][j2]); //eats 
          map[i2][j2] = map[i][j];    //moves sheep into new location
          map[i][j] = null;           //sets starting to null
      }else if (map[i2][j2] instanceof Sheep && map[i][j].getHealth() > reproductionHealth && map[i2][j2].getHealth() > reproductionHealth){ // into a sheep 
        boolean alreadyReproduced = false;      //used to prevent one pair of sheeps reproducing more than once in one move 
                                               //as there are 4 ifs in case of full spots
        if(i + 1 < map[0].length){  //avoid arrayIndexOutOfBounds 
          if(!(map[i+1][j] instanceof Wolf) && !(map[i+1][j] instanceof Sheep)){ // if free spot 
            alreadyReproduced = true;   ///
            map[i+1][j] = new Sheep(reproductionHealth ); //new sheep 
            map[i][j].setHealth(map[i][j].getHealth() - reproductionHealth /2); //decreases health from parents 
            map[i2][j2].setHealth(map[i2][j2].getHealth() - reproductionHealth /2);
            if (map[i][j].getPoisoned() || map[i2][j2].getPoisoned()){ // makes him poisoned if parents were poisoned
              map[i+1][j].setPoisoned(true);
            }
          }
        }
        if(i-1 > -1 && !alreadyReproduced){ //if last spot if full, check for one above
          if(!(map[i-1][j] instanceof Wolf) && !(map[i-1][j] instanceof Sheep)){
            alreadyReproduced = true;
            map[i-1][j] = new Sheep(reproductionHealth);       
            map[i][j].setHealth(map[i][j].getHealth() - reproductionHealth /2);
            map[i2][j2].setHealth(map[i2][j2].getHealth() - reproductionHealth /2);
            if (map[i][j].getPoisoned() || map[i2][j2].getPoisoned()){
              map[i-1][j].setPoisoned(true);
            }
          }
        }
        if (j-1 > -1 && !alreadyReproduced){ // check left
          if(!(map[i][j-1] instanceof Wolf) && !(map[i][j-1] instanceof Sheep)){
            alreadyReproduced = true;
            map[i][j-1] = new Sheep(reproductionHealth);       
            map[i][j].setHealth(map[i][j].getHealth() - reproductionHealth /2);
            map[i2][j2].setHealth(map[i2][j2].getHealth() - reproductionHealth /2);
            if (map[i][j].getPoisoned() || map[i2][j2].getPoisoned()){
              map[i][j-1].setPoisoned(true);
            }
          }
        }
        if (j+1 < map.length && !alreadyReproduced){ //check right 
          if(!(map[i][j+1] instanceof Wolf) && !(map[i][j+1] instanceof Sheep)){
            alreadyReproduced = true;
            map[i][j+1] = new Sheep(reproductionHealth);       
            map[i][j].setHealth(map[i][j].getHealth() - reproductionHealth /2);
            map[i2][j2].setHealth(map[i2][j2].getHealth() - reproductionHealth /2);
            if (map[i][j].getPoisoned() || map[i2][j2].getPoisoned()){
              map[i][j+1].setPoisoned(true);
            }
          }
        }
      }else if (map[i2][j2] == null){ // if moving into empty spot
        map[i2][j2] = map[i][j];    
        map[i][j] = null;           
      }
    }else{             // ****if  a wolf is moving**** //
      if(map[i2][j2] instanceof Sheep){ //if there's a sheep
        ((Wolf)map[i][j]).eat(map[i2][j2]); //eats sheep
        map[i2][j2] = map[i][j];   
        map[i][j] = null;           
      }else if (map[i2][j2] instanceof Wolf){ //if there's a wolf
        ((Wolf)map[i][j]).fight(map[i2][j2]); //fights 
      }else if (map[i2][j2] == null){ // if empty 
        map[i2][j2] = map[i][j];   //moves 
        map[i][j] = null;
      }
    }
    
      
    
  }
  
    /*
   * reset  
   * This method accepts the map, and the health losses due to time and poison
   * runs through the map and decreases the healths due to time 
   * and poison, sets already moved to false for each animal, 
   * removes if health is zero
   */
  static void reset(Creature[][] map, int healthLostPerRound, int poisonHealthLoss){ 
    
    for(int i = 0; i < map[0].length; i++){
      for(int j = 0;j < map.length; j++){
        
        if (map[i][j] instanceof Sheep){  //sets alreadyMoved to false for next round
          ((Sheep)map[i][j]).setAlreadyMoved(false);
        }else if (map[i][j] instanceof Wolf){
          ((Wolf)map[i][j]).setAlreadyMoved(false);
        }
        
        if(map[i][j] instanceof Animal){ //deacreases health per round 
          map[i][j].setHealth(map[i][j].getHealth() - healthLostPerRound);  //health decreased per round 
          if (map[i][j].getPoisoned()){
            map[i][j].setHealth(map[i][j].getHealth() - poisonHealthLoss);  //health decreased per round if poisoned(virus)
          }
          if( map[i][j].getHealth() <= 0) { // removes if health less than zero
            map[i][j] = null;
          }
 
          
        }
      }
    }
  }
  
  
    /*
   * spawn  
   * This method accepts the map, the plant spawn rate
   * spawns bushes based on the spawing rate
   * and closeness to other plants, gives
   * 1% chance of spwing a poisonus bush 
   */
  static void spawn(Creature[][] map, int plantSpawnRate){
    for(int i = 0; i < map[0].length; i++){
      for(int j = 0;j < map.length; j++){
        if (map[i][j] == null){// if empty 
          int chance = 0;
          int chancePerBush = 1;
          Random random = new Random();
          int rand = random.nextInt(100); // random number from 0 - 99
          if (i < map[0].length-1){ //prevents IdexOutOfBounds exception
            if (map[i+1][j] instanceof Bush){ // if the null is next to bushes 
              chance += chancePerBush; //spawn chance increases by 10 percent per bush
            }
          }
          if (j < map.length-1){
            if (map[i][j+1] instanceof Bush){
              chance += chancePerBush;
            }
          }
          if (j > 0){
            if (map[i][j-1] instanceof Bush){
              chance += chancePerBush;
            }
          }
          if (i > 0){
            if (map[i-1][j] instanceof Bush){
              chance += chancePerBush;
            }
          }
          if (rand < (plantSpawnRate + chance)){ // if rate less than random 
            map[i][j] = new Bush(50); //2 new plant
            rand = random.nextInt(150);
            if (rand == 0){
              map[i][j].setPoisoned(true); 
            }
          }
        }
      }
    }
  }

  
      /*
   * spawn  
   * This method accepts the map, and a string representing the requested 
   * ccreature including: "sheep" "wolf" "pSheep" etc
   * counts the number of all species, calculates average health 
   * for animals
   * returns the number of requested species or null
   */
  static int count(Creature[][] map, String type){
    int sheepNum = 0;
    int wolfNum = 0;
    int bushNum = 0;
    int pBushNum = 0;
    int pWolfNum = 0;
    int pSheepNum = 0; 
    int emptyNum = 0;
    int sheepAverageHealth = 0;
    int wolfAverageHealth = 0;
    // goes through all of map and counts for each 
    for(int i = 0; i < map[0].length; i++){ 
      for(int j = 0;j < map.length; j++){
        if (map[i][j] instanceof Sheep){ // if a sheep 
          sheepAverageHealth +=((Sheep)map[i][j]).getHealth(); // gets the sum of the healths 
          if (((Sheep)map[i][j]).getPoisoned()){ // if poisoned 
            pSheepNum++; // adds to psheep 
          }else {
            sheepNum++; // adds to healthy sheep 
          }
        }else if (map[i][j] instanceof Wolf){
          wolfAverageHealth +=((Wolf)map[i][j]).getHealth();
          if (((Wolf)map[i][j]).getPoisoned()){
            pWolfNum++;
          }else {
            wolfNum++;
          }
        }else if (map[i][j] instanceof Bush){
          if (((Bush)map[i][j]).getPoisoned()){
            pBushNum++;
          }else {
            bushNum++;
          }
        }else{
          emptyNum++;
        }
        
      }
    }
    
    //returns the number of requested species 
    if(type.equals("sheep")){
      return sheepNum;
    }else if(type.equals("pSheep")){
      return pSheepNum;
    }else if(type.equals("wolf")){
      return wolfNum;
    }else if(type.equals("pWolf")){
      return pWolfNum;
    }else if(type.equals("bush")){
      return bushNum;
    }else if(type.equals("pBush")){
      return pBushNum;
    }else if  (type.equals("empty")) {
      return emptyNum;
    }else if  (type.equals("sheepAverage")) {
      if (sheepNum + pSheepNum != 0){
        return sheepAverageHealth / (sheepNum + pSheepNum);
      }else{
        return 0;
      }
    }else{
      if (wolfNum + pWolfNum != 0){
        return wolfAverageHealth / (wolfNum + pWolfNum);
      }else {
        return 0;
      }
    }
  }

  
}