package net.common.collection.redblacktree;

import net.common.SimulatorException;

import java.util.*;

public class RedBlackTree<S extends Comparable, T extends TreeItem<S>>
    implements Iterable<T>
{
  private static final int NEGATIVE_RED = -1;
  private static final int DOUBLE_BLACK = 2;
  public static final int BLACK = 1;
  public static final int RED = 0;

  protected RedBlackNode<S, T> root;

  public RedBlackTree()
  {
    root = null;
  }

  public void add(T obj)
  {
    RedBlackNode<S, T> newNode = new RedBlackNode<S, T>(obj);
    if (root == null)
    {
      root = newNode;
    }
    else
    {
      root.addNode(newNode);
    }
    fixAfterAdd(newNode);
  }

  public void set(T obj)
  {
    RedBlackNode<S, T> node = findNode(obj.getTreeKey());
    if (node != null)
    {
      node.setObject(obj);
    }
    else
    {
      throw new SimulatorException("Can't set node with key [" + obj.getTreeKey() + "].  It does not exist.");
    }
  }

  public T find(Comparable key)
  {
    RedBlackNode<S, T> node = findNode(key);
    if (node != null)
    {
      return node.getObject();
    }
    else
    {
      return null;
    }
  }

  public T findOnOrBefore(Comparable key)
  {
    RedBlackNode<S, T> node = findNodeOnOrBefore(key);
    if (node != null)
    {
      return node.getObject();
    }
    else
    {
      return null;
    }
  }

  public T findOnOrAfter(Comparable key)
  {
    RedBlackNode<S, T> node = findNodeOnOrAfter(key);
    if (node != null)
    {
      return node.getObject();
    }
    else
    {
      return null;
    }
  }

  public RedBlackNode<S, T> findNode(Comparable key)
  {
    RedBlackNode<S, T> current = root;
    while (current != null)
    {
      int d = compare(key, current);
      if (d == 0)
      {
        return current;
      }
      else if (d > 0)
      {
        current = current.getLeft();
      }
      else
      {
        current = current.getRight();
      }
    }
    return null;
  }

  public RedBlackNode<S, T> findNodeOnOrBefore(Comparable key)
  {
    RedBlackNode<S, T> current = root;
    RedBlackNode<S, T> smaller = null;
    while (current != null)
    {
      int d = compare(key, current);
      if (d == 0)
      {
        return current;
      }
      else if (d > 0)
      {
        smaller = current;
        current = current.getLeft();
      }
      else
      {
        current = current.getRight();
      }
    }

    return smaller;
  }

  public RedBlackNode<S, T> findNodeOnOrAfter(Comparable key)
  {
    RedBlackNode<S, T> current = root;
    RedBlackNode<S, T> larger = null;
    while (current != null)
    {
      int d = compare(key, current);
      if (d == 0)
      {
        return current;
      }
      else if (d < 0)
      {
        larger = current;
        current = current.getRight();
      }
      else
      {
        current = current.getLeft();
      }
    }

    return larger;
  }

  private int compare(Comparable key, RedBlackNode<S, T> object2)
  {
    return key.compareTo(object2.getKey());
  }

  public T remove(Comparable key)
  {
    RedBlackNode<S, T> toBeRemoved = findNode(key);

    if (toBeRemoved == null)
    {
      return null;
    }

    T removedObject = toBeRemoved.getObject();

    if (toBeRemoved.getLeft() == null && toBeRemoved.getRight() == null && toBeRemoved == root)
    {
      root = null;
    }
    else if (toBeRemoved.getLeft() == null || toBeRemoved.getRight() == null)
    {
      RedBlackNode<S, T> newChild;
      if (toBeRemoved.getLeft() == null)
      {
        newChild = toBeRemoved.getRight();
      }
      else
      {
        newChild = toBeRemoved.getLeft();
      }

      fixBeforeRemove(toBeRemoved);

      if (toBeRemoved.getParent() == null)
      {
        root = newChild;
        newChild.clearParent();
      }
      else
      {
        toBeRemoved.replaceWith(newChild);
      }
    }
    else
    {
      RedBlackNode<S, T> smallest = toBeRemoved.getRight();
      while (smallest.getLeft() != null)
      {
        smallest = smallest.getLeft();
      }

      toBeRemoved.setObject(smallest.getObject());
      fixBeforeRemove(smallest);
      smallest.replaceWith(smallest.getRight());
    }
    return removedObject;
  }

  public boolean remove(T obj)
  {
    Comparable key = obj.getTreeKey();
    return remove(key) != null;
  }

  public void removeAll()
  {
    root = null;
  }

  public List<T> findAll()
  {
    List<T> result = new ArrayList<T>();
    findRecursive(root, result);
    return result;
  }

  private void findRecursive(RedBlackNode<S, T> n, List<T> result)
  {
    if (n != null)
    {
      findRecursive(n.getRight(), result);
      result.add(n.getObject());
      findRecursive(n.getLeft(), result);
    }
  }

  public RedBlackNode<S, T> findFirstNode()
  {
    return (root != null) ? root.findRightmostNode() : null;
  }

  public T findFirst()
  {
    RedBlackNode<S, T> current = findFirstNode();

    if (current != null)
    {
      return current.getObject();
    }
    else
    {
      return null;
    }
  }

  public RedBlackNode<S, T> findLastNode()
  {
    RedBlackNode<S, T> current = root;
    RedBlackNode<S, T> previous = null;
    while (current != null)
    {
      previous = current;
      current = current.getLeft();
    }
    return previous;
  }

  public T findLast()
  {
    RedBlackNode<S, T> current = findLastNode();

    if (current != null)
    {
      return current.getObject();
    }
    else
    {
      return null;
    }
  }

  public Iterator<T> iterator()
  {
    return new RedBlackTreeIterator<S, T>(this);
  }

  public Iterator<RedBlackNode<S, T>> nodeIterator()
  {
    return new RedBlackTreeNodeIterator<S, T>(this);
  }

  public Iterator<T> iteratorFrom(S start)
  {
    S end;
    T last = findLast();
    if (last == null)
    {
      end = start;
    }
    else
    {
      S lastKey = last.getTreeKey();
      end = start.compareTo(lastKey) >= 0 ? start : lastKey;
    }

    return new RedBlackTreeIterator<S, T>(this, start, end);
  }

  public Iterator<T> iteratorTo(S endGiven)
  {
    S start;
    T first = findFirst();
    if (first == null)
    {
      start = endGiven;
    }
    else
    {
      S firstKey = first.getTreeKey();
      start = firstKey.compareTo(endGiven) >= 0 ? endGiven : firstKey;
    }

    return new RedBlackTreeIterator<S, T>(this, start, endGiven);
  }

  public Iterator<T> iterator(S start, S end)
  {
    return new RedBlackTreeIterator<S, T>(this, start, end);
  }

  private void fixAfterAdd(RedBlackNode<S, T> newNode)
  {
    if (newNode.getParent() == null)
    {
      newNode.setColor(BLACK);
    }
    else
    {
      newNode.setColor(RED);
      if (newNode.getParent().getColor() == RED)
      {
        fixDoubleRed(newNode);
      }
    }
  }

  private void fixBeforeRemove(RedBlackNode<S, T> removed)
  {
    if (removed.getColor() == RED)
    {
      return;
    }

    if (removed.getLeft() != null || removed.getRight() != null)
    {
      if (removed.getLeft() == null)
      {
        removed.getRight().setColor(BLACK);
      }
      else
      {
        removed.getLeft().setColor(BLACK);
      }
    }
    else
    {
      bubbleUp(removed.getParent());
    }
  }

  private void bubbleUp(RedBlackNode<S, T> parent)
  {
    if (parent == null)
    {
      return;
    }
    parent.increaseColor();
    parent.getLeft().decreaseColor();
    parent.getRight().decreaseColor();

    RedBlackNode<S, T> child = parent.getLeft();
    if (fixRed(child))
    {
      return;
    }

    child = parent.getRight();
    if (fixRed(child))
    {
      return;
    }

    if (parent.getColor() == DOUBLE_BLACK)
    {
      if (parent.getParent() == null)
      {
        parent.setColor(BLACK);
      }
      else
      {
        bubbleUp(parent.getParent());
      }
    }
  }

  private boolean fixRed(RedBlackNode<S, T> child)
  {
    if (child.getColor() == NEGATIVE_RED)
    {
      fixNegativeRed(child);
      return true;
    }
    else if (child.getColor() == RED)
    {
      if (child.getLeft() != null && child.getLeft().getColor() == RED)
      {
        fixDoubleRed(child.getLeft());
        return true;
      }
      if (child.getRight() != null && child.getRight().getColor() == RED)
      {
        fixDoubleRed(child.getRight());
        return true;
      }
    }
    return false;
  }

  @SuppressWarnings("Duplicates")
  private void fixDoubleRed(RedBlackNode<S, T> child)
  {
    RedBlackNode<S, T> parent = child.getParent();
    RedBlackNode<S, T> grandParent = parent.getParent();
    if (grandParent == null)
    {
      parent.setColor(BLACK);
      return;
    }
    RedBlackNode<S, T> n1, n2, n3, t1, t2, t3, t4;
    if (parent == grandParent.getLeft())
    {
      n3 = grandParent;
      t4 = grandParent.getRight();
      if (child == parent.getLeft())
      {
        n1 = child;
        n2 = parent;
        t1 = child.getLeft();
        t2 = child.getRight();
        t3 = parent.getRight();
      }
      else
      {
        n1 = parent;
        n2 = child;
        t1 = parent.getLeft();
        t2 = child.getLeft();
        t3 = child.getRight();
      }
    }
    else
    {
      n1 = grandParent;
      t1 = grandParent.getLeft();
      if (child == parent.getLeft())
      {
        n2 = child;
        n3 = parent;
        t2 = child.getLeft();
        t3 = child.getRight();
        t4 = parent.getRight();
      }
      else
      {
        n2 = parent;
        n3 = child;
        t2 = parent.getLeft();
        t3 = child.getLeft();
        t4 = child.getRight();
      }
    }

    if (grandParent == root)
    {
      root = n2;
      n2.clearParent();
    }
    else
    {
      grandParent.replaceWith(n2);
    }

    n1.setLeftChild(t1);
    n1.setRightChild(t2);
    n2.setLeftChild(n1);
    n2.setRightChild(n3);
    n3.setLeftChild(t3);
    n3.setRightChild(t4);
    n2.setColor(grandParent.getColor() - 1);
    n1.setColor(BLACK);
    n3.setColor(BLACK);

    if (n2 == root)
    {
      root.setColor(BLACK);
    }
    else if (n2.getColor() == RED && n2.getParent().getColor() == RED)
    {
      fixDoubleRed(n2);
    }
  }

  private void fixNegativeRed(RedBlackNode<S, T> negRed)
  {
    RedBlackNode<S, T> n1, n2, n3, n4, t1, t2, t3, child;
    RedBlackNode<S, T> parent = negRed.getParent();
    if (parent.getLeft() == negRed)
    {
      n1 = negRed.getLeft();
      n2 = negRed;
      n3 = negRed.getRight();
      n4 = parent;
      t1 = n3.getLeft();
      t2 = n3.getRight();
      t3 = n4.getRight();
      n1.setColor(RED);
      n2.setColor(BLACK);
      n4.setColor(BLACK);
      n2.setRightChild(t1);
      T temp = n4.getObject();
      n4.setObject(n3.getObject());
      n3.setObject(temp);
      n3.setLeftChild(t2);
      n3.setRightChild(t3);
      n4.setRightChild(n3);
      child = n1;
    }
    else
    {
      n4 = negRed.getRight();
      n3 = negRed;
      n2 = negRed.getLeft();
      n1 = parent;
      t3 = n2.getRight();
      t2 = n2.getLeft();
      t1 = n1.getLeft();
      n4.setColor(RED);
      n3.setColor(BLACK);
      n1.setColor(BLACK);
      n3.setLeftChild(t3);
      T temp = n1.getObject();
      n1.setObject(n2.getObject());
      n2.setObject(temp);
      n2.setRightChild(t2);
      n2.setLeftChild(t1);
      n1.setLeftChild(n2);
      child = n4;
    }

    if (child.getLeft() != null && child.getLeft().getColor() == RED)
    {
      fixDoubleRed(child.getLeft());
      return;
    }
    if (child.getRight() != null && child.getRight().getColor() == RED)
    {
      fixDoubleRed(child.getRight());
    }
  }

  public void addAll(Iterable<T> items)
  {
    for (T item : items)
    {
      add(item);
    }
  }

  public boolean sizeExceeds(int limit)
  {
    if (limit < 0)
    {
      return true;
    }

    int count = 0;
    for (Iterator<RedBlackNode<S, T>> it = nodeIterator(); it.hasNext(); )
    {
      RedBlackNode<S, T> node = it.next();
      count++;
      if (count > limit)
      {
        return true;
      }
    }
    return count > limit;
  }

  public int size()
  {
    int count = 0;
    for (Iterator<RedBlackNode<S, T>> it = nodeIterator(); it.hasNext(); )
    {
      RedBlackNode<S, T> node = it.next();
      count++;
    }
    return count;
  }

  public boolean isEmpty()
  {
    return !sizeExceeds(0);
  }

  public boolean containsKey(S key)
  {
    return find(key) != null;
  }

  public void clear()
  {
    removeAll();
  }

  public Set<S> keySet()
  {
    LinkedHashSet<S> keySet = new LinkedHashSet<>();
    for (Iterator<RedBlackNode<S, T>> iterator = nodeIterator(); iterator.hasNext(); )
    {
      RedBlackNode<S, T> node = iterator.next();
      S key = node.getKey();
      keySet.add(key);
    }

    return keySet;
  }
}

