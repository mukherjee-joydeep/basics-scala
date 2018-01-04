object Test1 {;import org.scalaide.worksheet.runtime.library.WorksheetSupport._; def main(args: Array[String])=$execute{;$skip(58); 
  println("Welcome to the Scala worksheet");$skip(17); 
  var j1:Int = 6;System.out.println("""j1  : Int = """ + $show(j1 ));$skip(17); 
  var j2:Int = 7;System.out.println("""j2  : Int = """ + $show(j2 ));$skip(16); 
  var tot=j1+j2;System.out.println("""tot  : Int = """ + $show(tot ));$skip(24); 
  println("Total "+tot);$skip(58); 
  def fact(n:Int):Int={
  	println(n*3)
  	return n*3
  };System.out.println("""fact: (n: Int)Int""");$skip(40); 
  println("returned value is "+fact(6))}
  
}
