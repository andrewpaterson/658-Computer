package name.bizna.cpu.addressingmode;

import name.bizna.cpu.Cpu65816;

public class FetchAbsoluteAddressHigh
    extends DataOperation
{
  public FetchAbsoluteAddressHigh(boolean notMemoryLock)
  {
    super(false, true, notMemoryLock, true, true);
  }

  @Override
  public void execute(Cpu65816 cpu)
  {
    int data = cpu.getPins().getData();
    cpu.setAddressHigh(data);
  }

  @Override
  public String toString()
  {
    return "Read(AAH)";
  }
}

