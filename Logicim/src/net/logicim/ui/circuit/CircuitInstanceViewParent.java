package net.logicim.ui.circuit;

public class CircuitInstanceViewParent
{
  protected CircuitInstanceViewParent parent;
  protected CircuitInstanceView circuitInstanceView;
  protected String id;

  public CircuitInstanceViewParent(CircuitInstanceViewParent parent, CircuitInstanceView circuitInstanceView, long id)
  {
    this.parent = parent;
    this.circuitInstanceView = circuitInstanceView;
    this.id = Long.toHexString(id);
  }

  public SubcircuitView getCircuitSubcircuitView()
  {
    return circuitInstanceView.getCircuitSubcircuitView();
  }

  public String getId()
  {
    return id;
  }

  public CircuitInstanceViewParent getParent()
  {
    return parent;
  }

  public String get()
  {
    return circuitInstanceView.toString();
  }
}

