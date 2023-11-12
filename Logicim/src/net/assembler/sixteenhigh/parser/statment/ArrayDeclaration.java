package net.assembler.sixteenhigh.parser.statment;

import java.util.ArrayList;
import java.util.List;

public class ArrayDeclaration
{
  public List<Long> arrayMatrix;

  public ArrayDeclaration()
  {
    this.arrayMatrix = new ArrayList<>();
  }

  public void addArray(long length)
  {
    arrayMatrix.add(length);
  }
}

