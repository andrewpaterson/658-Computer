package net.logicim.ui.simulation.component.integratedcircuit.standard.common;

public class PortViewCreator
{
  public String text;
  public String name;
  public boolean clock;
  public boolean inverted;
  public int portCount;

  public PortViewCreator(String text)
  {
    this(text, text, false, false);
  }

  public PortViewCreator(String text, boolean inverted)
  {
    this(text, text, false, inverted);
  }

  public PortViewCreator(String text, boolean clock, boolean inverted)
  {
    this(text, text, 1, clock, inverted);
  }

  public PortViewCreator(String text, int portCount)
  {
    this(text, text, portCount, false, false);
  }

  public PortViewCreator(String text, String name)
  {
    this(text, name, false, false);
  }

  public PortViewCreator(String text, String name, boolean inverted)
  {
    this(text, name, false, inverted);
  }

  public PortViewCreator(String text, String name, boolean clock, boolean inverted)
  {
    this(text, name, 1, clock, inverted);
  }

  public PortViewCreator(String text, String name, int portCount)
  {
    this(text, name, portCount, false, false);
  }

  public PortViewCreator(String text, String name, int portCount, boolean clock, boolean inverted)
  {
    this.text = text;
    this.name = name;
    this.clock = clock;
    this.inverted = inverted;
    this.portCount = portCount;
  }
}

