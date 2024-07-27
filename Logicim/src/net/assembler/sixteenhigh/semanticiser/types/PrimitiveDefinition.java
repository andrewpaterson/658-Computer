package net.assembler.sixteenhigh.semanticiser.types;

import net.assembler.sixteenhigh.tokeniser.literal.PrimitiveTypeCode;
import net.common.util.StringUtil;

import java.util.List;

public class PrimitiveDefinition
    extends TypeDefinition
{
  public PrimitiveTypeCode type;
  public long[] arrayMatrix;
  public int pointerCount;

  public PrimitiveDefinition(PrimitiveTypeCode type, List<Long> arrayMatrix, int pointerCount)
  {
    super();

    this.pointerCount = pointerCount;
    this.arrayMatrix = new long[arrayMatrix.size()];
    for (int i = 0; i < arrayMatrix.size(); i++)
    {
      long matrix = arrayMatrix.get(i);
      this.arrayMatrix[i] = matrix;
    }

    this.type = type;
  }

  @Override
  public PrimitiveTypeCode getType()
  {
    return type;
  }

  public String print()
  {
    StringBuilder arrays = new StringBuilder();
    if (arrayMatrix.length > 0)
    {
      for (long arrayLength : arrayMatrix)
      {
        arrays.append("[");
        arrays.append(arrayLength);
        arrays.append("]");
      }
    }
    StringBuilder asterisks = StringUtil.pad("*", pointerCount);

    return type + arrays.toString() + asterisks.toString();
  }
}

