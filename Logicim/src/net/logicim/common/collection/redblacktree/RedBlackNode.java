package net.logicim.common.collection.redblacktree;

import net.logicim.common.EmulatorException;

public class RedBlackNode<S extends Comparable, T extends TreeItem<S>>
{
  private RedBlackNode<S, T> left;
  private RedBlackNode<S, T> right;
  private RedBlackNode<S, T> parent;
  private S key;
  private T object;
  private int color;

  protected RedBlackNode(T object)
  {
    this.object = object;
    this.key = object.getTreeKey();
    left = null;
    right = null;
  }

  public RedBlackNode<S, T> getLeft()
  {
    return left;
  }

  public RedBlackNode<S, T> getRight()
  {
    return right;
  }

  public RedBlackNode<S, T> getParent()
  {
    return parent;
  }

  protected void clearParent()
  {
    parent = null;
  }

  public S getKey()
  {
    return key;
  }

  public T getObject()
  {
    return object;
  }

  protected void setObject(T object)
  {
    this.object = object;
    this.key = object.getTreeKey();
  }

  protected int getColor()
  {
    return color;
  }

  protected void setColor(int color)
  {
    this.color = color;
  }

  protected void increaseColor()
  {
    color++;
  }

  protected void decreaseColor()
  {
    color--;
  }

  protected void setLeftChild(RedBlackNode<S, T> child)
  {
    left = child;
    if (child != null)
    {
      child.parent = this;
    }
  }

  protected void setRightChild(RedBlackNode<S, T> child)
  {
    right = child;
    if (child != null)
    {
      child.parent = this;
    }
  }

  protected void replaceWith(RedBlackNode<S, T> replacement)
  {
    if (parent == null)
    {
      return;
    }
    if (this == parent.left)
    {
      parent.setLeftChild(replacement);
    }
    else if (this == parent.right)
    {
      parent.setRightChild(replacement);
    }
    else
    {
      throw new EmulatorException("Child being replaced did not belong to the parent.");
    }
  }

  protected void addNode(RedBlackNode<S, T> newNode)
  {
    int comp = newNode.getKey().compareTo(getKey());
    if (comp > 0)
    {
      if (left == null)
      {
        left = newNode;
        left.parent = this;
      }
      else
      {
        left.addNode(newNode);
      }
    }
    else if (comp < 0)
    {
      if (right == null)
      {
        right = newNode;
        right.parent = this;
      }
      else
      {
        right.addNode(newNode);
      }
    }
    else if (comp == 0)
    {
      throw new EmulatorException("Can't add node with duplicate key [" + newNode.getKey() + "].");
    }
  }

  @SuppressWarnings({"unchecked"})
  public RedBlackNode<S, T> findNextNodeOrNull()
  {
    RedBlackNode<S, T> current = this;

    if (current.getLeft() != null)
    {
      current = current.getLeft().findRightmostNode();
    }
    else
    {
      RedBlackNode<S, T> parent = current.getParent();
      while ((parent != null) && (parent.getKey().compareTo(current.getKey()) < 0))
      {
        parent = parent.getParent();
      }
      current = parent;
    }

    return current;
  }

  @SuppressWarnings({"unchecked"})
  public RedBlackNode<S, T> findPreviousNodeOrNull()
  {
    RedBlackNode<S, T> current = this;

    if (current.getRight() != null)
    {
      current = current.getRight().findLeftmostNode();
    }
    else
    {
      RedBlackNode<S, T> parent = current.getParent();
      while ((parent != null) && (parent.getKey().compareTo(current.getKey()) > 0))
      {
        parent = parent.getParent();
      }
      current = parent;
    }

    return current;
  }

  protected RedBlackNode<S, T> findRightmostNode()
  {
    RedBlackNode<S, T> current = this;
    RedBlackNode<S, T> previous = null;
    while (current != null)
    {
      previous = current;
      current = current.getRight();
    }
    return previous;
  }

  protected RedBlackNode<S, T> findLeftmostNode()
  {
    RedBlackNode<S, T> current = this;
    RedBlackNode<S, T> previous = null;
    while (current != null)
    {
      previous = current;
      current = current.getLeft();
    }
    return previous;
  }

  @Override
  public String toString()
  {
    String leftString = "null";
    if (left != null)
    {
      leftString = left.object.toString();
    }

    String rightString = "null";
    if (right != null)
    {
      rightString = right.object.toString();
    }

    return "left=" + leftString + ", right=" + rightString + ", object=" + object;
  }
}

