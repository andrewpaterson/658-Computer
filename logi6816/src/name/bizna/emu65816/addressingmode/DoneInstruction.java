package name.bizna.emu65816.addressingmode;

import name.bizna.emu65816.Cpu65816;

public class DoneInstruction
    extends Operation
{
  @Override
  public void execute(Cpu65816 cpu)
  {
    cpu.doneInstruction();
  }


  @Override
  public String toString()
  {
    return "DONE";
  }
}

