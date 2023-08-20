package net.common.collection.redblacktree;

import java.util.Iterator;

public class RedBlackTreeIterator<S extends Comparable, T extends TreeItem<S>>
    extends BaseRedBlackIterator<S, T>
    implements Iterator<T>
{
  public RedBlackTreeIterator(RedBlackTree<S, T> tree)
  {
    super(tree);
  }

  public RedBlackTreeIterator(RedBlackTree<S, T> tree, S start, S end)
  {
    super(tree, start, end);
  }

  @Override
  public T next()
  {
    return getNextNode().getObject();
  }
}

