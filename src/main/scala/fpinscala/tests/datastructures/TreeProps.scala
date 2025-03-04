package fpinscala.tests.datastructures

import fpinscala.answers.testing.exhaustive.*
import fpinscala.answers.testing.exhaustive.Prop.*
import fpinscala.exercises.datastructures.Tree
import fpinscala.exercises.datastructures.Tree.*

import scala.List as SList

object TreeProps:
  private val genIntTree: Gen[Tree[Int]] =
    def loop(): Gen[Tree[Int]] =
      Gen.boolean.flatMap {
        if _ then Gen.int.map(n => Leaf(n))
        else
          for {
            left <- loop()
            right <- loop()
          } yield Branch(left, right)
      }
    loop()

  private val sizeProp: Prop = forAll(genIntTree) { tree =>
    tree.size == toScalaList(tree).length
  }.tag("Tree.size")

  private val depthProp: Prop = forAll(genIntTree) { tree =>
    tree match
      case Leaf(_)      => tree.depth == 0
      case Branch(l, r) => tree.depth == 1 + l.depth.max(r.depth)
  }.tag("Tree.depth")

  private val mapProp: Prop = forAll(genIntTree) { tree =>
    toScalaList(tree.map(_.toString)) == toScalaList(tree).map(_.map(_.toString))
  }.tag("Tree.map")

  private val foldProp: Prop = forAll(genIntTree) { tree =>
    tree.fold(_.toString, _ + _) == toScalaList(tree).flatMap(_.map(_.toString)).mkString
  }.tag("Tree.fold")

  private val sizeViaFoldProp: Prop = forAll(genIntTree) { tree =>
    tree.sizeViaFold == toScalaList(tree).length
  }.tag("Tree.sizeViaFold")

  private val depthViaFoldProp: Prop = forAll(genIntTree) { tree =>
    tree match
      case Leaf(_)      => tree.depthViaFold == 0
      case Branch(l, r) => tree.depthViaFold == 1 + l.depthViaFold.max(r.depthViaFold)
  }.tag("Tree.depthViaFold")

  private val mapViaFoldProp: Prop = forAll(genIntTree) { tree =>
    toScalaList(tree.mapViaFold(_.toString)) == toScalaList(tree).map(_.map(_.toString))
  }.tag("Tree.mapViaFold")

  private val sizeOfTreeProp: Prop = forAll(genIntTree) { tree =>
    Tree.size(tree) == toScalaList(tree).length
  }.tag("size(tree)")

  private val firstPositiveProp: Prop = forAll(genIntTree) { tree =>
    tree.firstPositive == toScalaList(tree).flatten.find(_ > 0)
  }.tag("Tree.firstPositive")

  private val maximumProp: Prop = forAll(genIntTree) { tree =>
    tree.maximum == toScalaList(tree).max.getOrElse(0)
  }.tag("Tree.maximum")

  private val maximumViaFoldProp: Prop = forAll(genIntTree) { tree =>
    tree.maximumViaFold == toScalaList(tree).max.getOrElse(0)
  }.tag("Tree.maximumViaFold")

  @main def checkTree(): Unit =
    sizeProp.run()
    depthProp.run()
    mapProp.run()
    foldProp.run()
    sizeViaFoldProp.run()
    depthViaFoldProp.run()
    mapViaFoldProp.run()
    sizeOfTreeProp.run()
    firstPositiveProp.run()
    maximumProp.run()
    maximumViaFoldProp.run()

  private def toScalaList[A](t: Tree[A]): SList[Option[A]] = t match
    case Leaf(v)      => SList(Some(v))
    case Branch(l, r) => (Option.empty[A] +: toScalaList(l)) ++ toScalaList(r)
