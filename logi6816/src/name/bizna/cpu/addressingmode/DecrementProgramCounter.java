package name.bizna.cpu.addressingmode;

import name.bizna.cpu.Cpu65816;

public class DecrementProgramCounter
    extends Operation
{
  @Override
  public void execute(Cpu65816 cpu)
  {
    cpu.decrementProgramCounter();
  }

  @Override
  public String toString()
  {
    return "PC--";
  }
}

