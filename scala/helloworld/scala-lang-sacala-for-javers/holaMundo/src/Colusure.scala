import java.util.concurrent.TimeUnit


object Colusure extends App {
  def diezVecesSegundo(callback:()=> Unit){
    for(i<- 1 to 10){
        callback();
        TimeUnit.SECONDS.sleep(1);
     }
  }
  
  def missatge(){
     println("Repeteix 10 cops") 
  }
  
  //diezVecesSegundo( missatge)
  
  //Ara amb un clousure pur
  diezVecesSegundo(
      ()=>{
        println("Deu cops aquest clousure");
        println("Poso dos per creuremo'ho");
        })
}