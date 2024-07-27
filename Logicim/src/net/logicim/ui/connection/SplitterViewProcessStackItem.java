package net.logicim.ui.connection;

import net.logicim.ui.circuit.CircuitInstanceViewPath;
import net.logicim.ui.common.ConnectionView;
import net.logicim.ui.simulation.component.passive.splitter.SplitterView;

public class SplitterViewProcessStackItem
{
  protected SplitterView splitterView;
  protected CircuitInstanceViewPath path;
  protected ConnectionView connection;

  public SplitterViewProcessStackItem(SplitterView splitterView,
                                      CircuitInstanceViewPath path,
                                      ConnectionView connection)
  {
    this.splitterView = splitterView;
    this.path = path;
    this.connection = connection;
  }
}

