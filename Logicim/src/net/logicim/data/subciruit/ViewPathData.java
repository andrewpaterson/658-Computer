package net.logicim.data.subciruit;

import net.logicim.data.common.ReflectiveData;

import java.util.List;

public class ViewPathData
    extends ReflectiveData
{
  public long id;
  public List<ViewPathElementData> elements;
  public long prevId;
  public long nextId;

  public ViewPathData()
  {
  }

  public ViewPathData(long id,
                      List<ViewPathElementData> elements,
                      long prevId,
                      long nextId)
  {
    this.id = id;
    this.elements = elements;
    this.prevId = prevId;
    this.nextId = nextId;
  }
}

