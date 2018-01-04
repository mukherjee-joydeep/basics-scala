object Test1 {
  println("Welcome to the Scala worksheet")       //> Welcome to the Scala worksheet
  var j1:Int = 6                                  //> j1  : Int = 6
  var j2:Int = 7                                  //> j2  : Int = 7
  var tot=j1+j2                                   //> tot  : Int = 13
  println("Total "+tot)                           //> Total 13
  def fact(n:Int):Int={
  	println(n*3)
  	return n*3
  }                                               //> fact: (n: Int)Int
  println("returned value is "+fact(6))           //> 18
                                                  //| returned value is 18
  
}