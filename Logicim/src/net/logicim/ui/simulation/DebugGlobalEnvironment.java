package net.logicim.ui.simulation;

import net.common.SimulatorException;

public class DebugGlobalEnvironment
{
  public static DebugGlobalEnvironment instance = null;

  public DebugGlobalEnvironment()
  {
    if (instance == null)
    {
      instance = this;
    }
  }

  public static DebugGlobalEnvironment getInstance()
  {
    return instance;
  }

  public static void validateCanCreateComponent()
  {
    DebugGlobalEnvironment debugGlobalEnvironment = DebugGlobalEnvironment.getInstance();
    if (debugGlobalEnvironment == null)
    {
      throw new SimulatorException("DebugGlobalEnvironment instance not set.  Call new DebugGlobalEnvironment().");
    }
  }

  public static void validateCanCreateTrace()
  {
    DebugGlobalEnvironment debugGlobalEnvironment = DebugGlobalEnvironment.getInstance();
    if (debugGlobalEnvironment == null)
    {
      throw new SimulatorException("DebugGlobalEnvironment instance not set.  Call new DebugGlobalEnvironment().");
    }
  }
}

