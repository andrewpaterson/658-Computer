package net.logicim.common.collection.redblacktree;

import java.util.Iterator;

public class RedBlackTreeNodeIterator<S extends Comparable, T extends TreeItem<S>>
    extends BaseRedBlackIterator<S, T>
    implements Iterator<RedBlackNode<S, T>>
{
  public RedBlackTreeNodeIterator(RedBlackTree<S, T> tree)
  {
    super(tree);
  }

  public RedBlackTreeNodeIterator(RedBlackTree<S, T> tree, S start, S end)
  {
    super(tree, start, end);
  }

  @Override
  public RedBlackNode<S, T> next()
  {
    return getNextNode();
  }
}

