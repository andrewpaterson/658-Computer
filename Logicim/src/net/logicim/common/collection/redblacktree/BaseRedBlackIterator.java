package net.logicim.common.collection.redblacktree;

import java.util.NoSuchElementException;

public abstract class BaseRedBlackIterator<S extends Comparable, T extends TreeItem<S>>
{
  protected RedBlackNode<S, T> first;
  protected RedBlackNode<S, T> current;
  protected S stop;

  public BaseRedBlackIterator(RedBlackTree<S, T> tree)
  {
    first = tree.findFirstNode();
    current = null;
    stop = getNodeKey(tree.findLastNode());
  }

  public BaseRedBlackIterator(RedBlackTree<S, T> tree, S start, S end)
  {
    RedBlackNode<S, T> onOrAfter = tree.findNodeOnOrAfter(start);
    if (onOrAfter != null)
    {
      first = (onOrAfter.getKey().compareTo(end)) <= 0 ? onOrAfter : null;
      current = null;
      stop = getNodeKey(tree.findNodeOnOrBefore(end));
    }
  }

  private S getNodeKey(RedBlackNode<S, T> lastNode)
  {
    if (lastNode != null)
    {
      return lastNode.getKey();
    }
    else
    {
      return null;
    }
  }

  public boolean hasNext()
  {
    if (current == null)
    {
      return first != null;
    }
    else
    {
      return stop != null && current.getKey().compareTo(stop) < 0;
    }
  }

  public RedBlackNode<S, T> getNextNode()
  {
    if (!hasNext())
    {
      throw new NoSuchElementException();
    }

    if (current == null)
    {
      current = first;
      first = null;
    }
    else
    {
      current = current.findNextNodeOrNull();
    }

    return current;
  }
}
