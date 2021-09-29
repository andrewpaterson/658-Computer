package name.bizna.emu65816.addressingmode;

import name.bizna.emu65816.Cpu65816;

import java.util.Arrays;
import java.util.List;

public class ClearStop
    extends CycleOperation
{
  public ClearStop()
  {
  }

  @Override
  public void execute(Cpu65816 cpu)
  {
    cpu.clearStop();
  }

  @Override
  public boolean shouldSkipCycle(Cpu65816 cpu)
  {
    return false;
  }
}

