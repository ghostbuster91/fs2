package scalaz.stream2

import scala.annotation.tailrec


/**
 * Created by pach on 06/03/14.
 */
object Util {

  implicit class AppendSyntax[A](val self: Vector[A]) extends AnyVal {

    /**
     * Helper to fix performance issue on Vector append Seq
     * hopefully this can be removed in scala 2.11
     */
    def fast_++[B >: A](other: Seq[B]): Vector[B] = {
      @tailrec
      def append(acc:Vector[B], rem:Seq[B]) : Vector[B] = {
      //  debug(s"AP: self: ${self.size} other: ${other.size}")
        if (rem.nonEmpty) append(acc :+ rem.head, rem.tail)
        else acc
      }

      @tailrec
      def prepend(acc:Vector[B], rem:Seq[B]) : Vector[B] = {
      //  debug(s"PREP: self: ${self.size} other: ${other.size}")
        if (rem.nonEmpty) prepend(rem.head +: acc, rem.tail)
        else acc
      }

      if (self.size < other.size) prepend(other.toVector,self)
      else append(self.toVector, other)
    }
  }


  /**
   * Helper to wrap evaluation of `p` that may cause side-effects by throwing exception.
   */
  private[stream2] def Try[F[_], A](p: => Process[F, A]): Process[F, A] =
    try p
    catch {case e: Throwable => Process.Halt(e) }

 /** helper for turning on/off debug **/
  def debug(s: => String) = {
    //println(s)
  }

}
