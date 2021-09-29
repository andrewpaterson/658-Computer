package name.bizna.emu65816.addressingmode;

import name.bizna.emu65816.Cpu65816;

public class IncrementProgramCounter
    extends CycleOperation
{
  @Override
  public void execute(Cpu65816 cpu)
  {
    cpu.incrementProgramAddress();
  }
}

