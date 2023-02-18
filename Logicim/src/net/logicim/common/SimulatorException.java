package net.logicim.common;

public class SimulatorException
    extends RuntimeException
{
  public static final String NOT_YEY_IMPLEMENTED = "Not yet implemented.";

  public SimulatorException()
  {
    this(NOT_YEY_IMPLEMENTED);
  }

  public SimulatorException(String s)
  {
    super(s);
  }

  public SimulatorException(String s, Object... p)
  {
    this(String.format(s, p));
  }
}

