package name.bizna.emu65816.addressingmode;

import name.bizna.emu65816.Cpu65816;

public class FetchRelativeOffsetLow
    extends DataBusCycleOperation
{
  public FetchRelativeOffsetLow()
  {
    super(true, false, true, true, true);
  }

  @Override
  public void execute(Cpu65816 cpu)
  {
    cpu.setRelativeOffsetLow(cpu.getPinData());
  }
}

