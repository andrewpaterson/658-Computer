package name.bizna.emu65816.addressingmode;

import name.bizna.emu65816.Cpu65816;

public class FetchRelativeOffsetHigh
    extends DataBusCycleOperation
{
  public FetchRelativeOffsetHigh()
  {
    super(true, false, true, true, true);
  }

  @Override
  public void execute(Cpu65816 cpu)
  {
    cpu.setRelativeOffsetHigh(cpu.getPinData());
  }
}

