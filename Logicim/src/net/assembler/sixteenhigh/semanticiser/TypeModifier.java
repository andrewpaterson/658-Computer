package net.assembler.sixteenhigh.semanticiser;

import java.util.List;

public class TypeModifier
{
  public int pointerCount;
  public long[] arrayMatrix;

  public TypeModifier(int pointerCount, List<Long> arrayMatrix)
  {
    this.pointerCount = pointerCount;
    this.arrayMatrix = new long[arrayMatrix.size()];
    for (int i = 0; i < arrayMatrix.size(); i++)
    {
      long matrix = arrayMatrix.get(i);
      this.arrayMatrix[i] = matrix;
    }
  }

  public boolean matches(List<Long> other)
  {
    if (arrayMatrix.length == other.size())
    {
      for (int index = 0; index < arrayMatrix.length; index++)
      {
        long arrayLength = arrayMatrix[index];
        if (arrayLength != other.get(index))
        {
          return false;
        }
      }
      return true;
    }
    return false;
  }
}

