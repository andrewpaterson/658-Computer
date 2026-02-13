package net.logicim.ui.simulation.component.integratedcircuit.standard.common;

import java.util.Arrays;
import java.util.List;

public class PortViewCreatorList
{
  public List<PortViewCreator> creators;

  public PortViewCreatorList(PortViewCreator... creators)
  {
    this.creators = Arrays.asList(creators);
  }
}

