package net.logicim.ui.circuit;

public class CircuitInstanceViewParent
{
  protected CircuitInstanceViewParent parent;
  protected CircuitInstanceView view;
  protected String id;

  public CircuitInstanceViewParent(CircuitInstanceViewParent parent, CircuitInstanceView view, long id)
  {
    this.parent = parent;
    this.view = view;
    this.id = Long.toHexString(id);
  }

  public SubcircuitView getCircuitSubcircuitView()
  {
    return view.getInstanceSubcircuitView();
  }

  public String getId()
  {
    return id;
  }

  public CircuitInstanceViewParent getParent()
  {
    return parent;
  }

  public CircuitInstanceView getView()
  {
    return view;
  }
}

