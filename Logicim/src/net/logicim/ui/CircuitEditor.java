package net.logicim.ui;

import net.logicim.domain.common.Circuit;
import net.logicim.ui.common.View;

import java.util.ArrayList;
import java.util.List;

public class CircuitEditor
{
  protected List<View> views;

  public CircuitEditor()
  {
    Circuit circuit = new Circuit();
    views = new ArrayList<>();

  }
}
