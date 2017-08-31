/* 
 * [Animal.java]
 * abstarct class for sheep and wolves extends creature 
 * Ali Meshkat 
 * April 4, 2017
 */
abstract class Animal extends Creature{
  private boolean alreadyMoved;     // shows whether the animal hsa already moved to avoid being moved twice 
  private boolean poisoned;        //shows if poisoned
  private String lastMove;  //is the last move made by animal  
  
  
  //constrcutor: sets lastMove to stay(no move)
  Animal(int health){
    super(health);
    this.lastMove = "stay";
  }
 
  //adds health gained & runs die for prey 
  abstract void eat(Creature prey);

  
  //getters and setters 
  void setPoisoned(boolean poisoned){
    this.poisoned = poisoned;
  }
  boolean getPoisoned(){
    return this.poisoned;
  }
  void setAlreadyMoved(boolean yesOrNo){
    this.alreadyMoved = yesOrNo;
  }
  boolean getAlreadyMoved(){
    return alreadyMoved;
  }
  void setLastMove(String lastMove){
    this.lastMove = lastMove;
  }
  String getLastMove(){
    return this.lastMove;
  }
}