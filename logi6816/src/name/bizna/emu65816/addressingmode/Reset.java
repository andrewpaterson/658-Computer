package name.bizna.emu65816.addressingmode;

import name.bizna.emu65816.Cpu65816;

import java.util.Arrays;
import java.util.List;

public class Reset
    extends CycleOperation
{
  public Reset()
  {
  }

  @Override
  public void execute(Cpu65816 cpu)
  {
    cpu.reset();
  }
}

