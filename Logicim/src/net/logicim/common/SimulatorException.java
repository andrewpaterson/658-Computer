package net.logicim.common;

public class SimulatorException
    extends RuntimeException
{
  public SimulatorException(String s)
  {
    super(s);
  }

  public SimulatorException(String s, Object... p)
  {
    this(String.format(s, p));
  }
}

