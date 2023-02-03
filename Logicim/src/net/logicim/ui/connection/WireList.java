package net.logicim.ui.connection;

import java.util.ArrayList;
import java.util.List;

public class WireList
{
  protected List<FullWire> fullWires;
  protected List<PartialWire> partialWires;

  public WireList()
  {
    fullWires = new ArrayList<>();
    partialWires = new ArrayList<>();
  }

  public void add(FullWire fullWire)
  {
    fullWires.add(fullWire);
  }

  public void add(PartialWire partialWire)
  {
    partialWires.add(partialWire);
  }

  public List<FullWire> getFullWires()
  {
    return fullWires;
  }

  public List<PartialWire> getPartialWires()
  {
    return partialWires;
  }
}

