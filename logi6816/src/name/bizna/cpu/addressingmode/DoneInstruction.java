package name.bizna.cpu.addressingmode;

import name.bizna.cpu.Cpu65816;

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

