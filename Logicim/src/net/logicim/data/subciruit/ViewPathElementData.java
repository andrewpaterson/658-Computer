package net.logicim.data.subciruit;

import net.logicim.data.common.ReflectiveData;

public class ViewPathElementData
    extends ReflectiveData
{
  public long id;
  public String type;

  public ViewPathElementData()
  {
  }

  public ViewPathElementData(long id, String type)
  {
    this.id = id;
    this.type = type;
  }
}

