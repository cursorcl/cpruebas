/**
 * Dec 4, 2009 - ayachan
 */
package cl.eos.util;

/**
 * @author ayachan
 * @param <U> 
 * @param <V> 
 */
public class Pair<U, V>
{
  private U first = null;
  private V second = null;

  public Pair()
  {

  }

  public Pair(U first, V second)
  {
    this.first = first;
    this.second = second;
  }

  /**
   * @return the first
   */
  public U getFirst()
  {
    return first;
  }

  /**
   * @return the second
   */
  public V getSecond()
  {
    return second;
  }

  /**
   * @param first
   *          the first to set
   */
  public void setFirst(U first)
  {
    this.first = first;
  }

  /**
   * @param second
   *          the second to set
   */
  public void setSecond(V second)
  {
    this.second = second;
  }

  @Override
  public int hashCode()
  {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((first == null) ? 0 : first.hashCode());
    result = prime * result + ((second == null) ? 0 : second.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj)
  {
    if (this == obj) return true;
    if (obj == null) return false;
    if (getClass() != obj.getClass()) return false;
    Pair<?, ?> other = (Pair<?, ?>) obj;
    if (first == null)
    {
      if (other.first != null) return false;
    }
    else if (!first.equals(other.first)) return false;
    if (second == null)
    {
      if (other.second != null) return false;
    }
    else if (!second.equals(other.second)) return false;
    return true;
  }

  @Override
  public String toString()
  {
    return "Pair [first=" + first + ", second=" + second + "]";
  }

  
}
