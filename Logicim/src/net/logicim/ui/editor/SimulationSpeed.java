package net.logicim.ui.editor;

import net.logicim.domain.common.LongTime;

public class SimulationSpeed
{
  protected long defaultRunTimeStep;
  protected long runTimeStep;
  protected boolean running;

  public SimulationSpeed()
  {
    this.defaultRunTimeStep = LongTime.nanosecondsToTime(0.25f);
    this.running = false;
    this.runTimeStep = LongTime.nanosecondsToTime(0.25f);
  }

  public long getDefaultRunTimeStep()
  {
    return defaultRunTimeStep;
  }

  public void setDefaultRunTimeStep(long defaultRunTimeStep)
  {
    this.defaultRunTimeStep = defaultRunTimeStep;
  }

  public long getRunTimeStep()
  {
    return runTimeStep;
  }

  public void setRunTimeStep(long runTimeStep)
  {
    this.runTimeStep = runTimeStep;
  }

  public boolean isRunning()
  {
    return running;
  }

  public void setRunning(boolean running)
  {
    this.running = running;
  }

  public boolean toggleTunSimulation()
  {
    running = !running;
    return running;
  }

  public void increaseSimulationSpeed()
  {
    runTimeStep *= 2;
  }

  public void decreaseSimulationSpeed()
  {
    runTimeStep /= 2;
  }

  public void setDefaultSimulationSpeed()
  {
    runTimeStep = defaultRunTimeStep;
  }
}

