package name.bizna.cpu.addressingmode;

import name.bizna.cpu.Cpu65816;

public class IncrementProgramCounter
    extends Operation
{
  @Override
  public void execute(Cpu65816 cpu)
  {
    cpu.incrementProgramAddress();
  }

  @Override
  public String toString()
  {
    return "PC++";
  }
}

