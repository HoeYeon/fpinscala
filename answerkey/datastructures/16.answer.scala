def incrementEach(l: List[Int]): List[Int] =
  foldRight(l, Nil:List[Int], (h,t) => Cons(h+1,t))
