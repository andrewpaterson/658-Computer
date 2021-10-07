package name.bizna.cpu.addressingmode;

import name.bizna.cpu.Cpu65816;

public class DecrementStackPointer
    extends Operation
{
  @Override
  public void execute(Cpu65816 cpu)
  {
    cpu.decrementStackPointer();
  }

  @Override
  public String toString()
  {
    return "SP--";
  }
}

