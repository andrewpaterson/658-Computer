package name.bizna.emu65816.addressingmode;

import name.bizna.emu65816.Cpu65816;

public class DecrementProgramCounter
    extends Operation
{
  @Override
  public void execute(Cpu65816 cpu)
  {
    cpu.decrementProgramCounter();
  }
}

