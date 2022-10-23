package net.logicim.common.collection.redblacktree;

public interface TreeItem<T extends Comparable>
{
  void treeNodeThoroughValidate(RedBlackNode<T, TreeItem<T>> node);

  T getTreeKey();
}

