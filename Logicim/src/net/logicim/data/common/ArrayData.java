package net.logicim.data.common;

import net.logicim.data.PrimitiveData;

import java.util.ArrayList;
import java.util.List;

public class ArrayData
    extends PrimitiveData
{
  public List<?> list;

  public ArrayData()
  {
    list = new ArrayList<>();
  }
}

