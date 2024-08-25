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
      enableSimulationCreation = false;
      instance = this;
    }
  }

  public static void validateCanCreateComponent()
  {
    if (!DebugGlobalEnvironment.getInstance().isEnableSimulationCreation())
    {
      throw new SimulatorException("Cannot create Component when simulation creation is disabled");
    }
  }

  public static void validateCanCreateTrace()
  {
    if (!DebugGlobalEnvironment.getInstance().isEnableSimulationCreation())
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

