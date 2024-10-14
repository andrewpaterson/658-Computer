package net.logicim.ui.simulation;

import net.common.SimulatorException;

public class DebugGlobalEnvironment
{
  public static DebugGlobalEnvironment instance = null;

  protected boolean enableSimulationCreation;

  public static DebugGlobalEnvironment getInstance()
  {
    return instance;
  }

  public DebugGlobalEnvironment()
  {
    if (instance == null)
    {
      enableSimulationCreation = true;
      instance = this;
    }
  }

  public static void validateCanCreateComponent()
  {
    DebugGlobalEnvironment debugGlobalEnvironment = DebugGlobalEnvironment.getInstance();
    if (debugGlobalEnvironment == null)
    {
      throw new SimulatorException("DebugGlobalEnvironment instance not set.  Call new DebugGlobalEnvironment().");
    }

    if (!debugGlobalEnvironment.isEnableSimulationCreation())
    {
      throw new SimulatorException("Cannot create Component when simulation creation is disabled");
    }
  }

  public static void validateCanCreateTrace()
  {
    DebugGlobalEnvironment debugGlobalEnvironment = DebugGlobalEnvironment.getInstance();
    if (debugGlobalEnvironment == null)
    {
      throw new SimulatorException("DebugGlobalEnvironment instance not set.  Call new DebugGlobalEnvironment().");
    }
    if (!debugGlobalEnvironment.isEnableSimulationCreation())
    {
      throw new SimulatorException("Cannot create Trace when simulation creation is disabled");
    }
  }

  public boolean isEnableSimulationCreation()
  {
    return enableSimulationCreation;
  }

  public void setEnableSimulationCreation(boolean enableSimulationCreation)
  {
    this.enableSimulationCreation = enableSimulationCreation;
  }
}

