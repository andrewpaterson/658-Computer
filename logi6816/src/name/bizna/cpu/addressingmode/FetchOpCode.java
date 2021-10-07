package name.bizna.cpu.addressingmode;

import name.bizna.cpu.Cpu65816;

public class FetchOpCode
    extends DataOperation
{
  public FetchOpCode()
  {
    super(true, true, true, true, true);
  }

  @Override
  public void execute(Cpu65816 cpu)
  {
    cpu.setOpCode(cpu.getPinData());
  }

  @Override
  public String toString()
  {
    return "OpCode";
  }
}

