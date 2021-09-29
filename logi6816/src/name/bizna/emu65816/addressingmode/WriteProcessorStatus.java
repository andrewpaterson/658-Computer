package name.bizna.emu65816.addressingmode;

import name.bizna.emu65816.Binary;
import name.bizna.emu65816.Cpu65816;

public class WriteProcessorStatus
    extends DataBusCycleOperation
{
  public WriteProcessorStatus()
  {
    super(false, true, true, false, true);
  }

  @Override
  public void execute(Cpu65816 cpu)
  {
    cpu.setPinData(cpu.getCpuStatus().getRegisterValue());
  }
}

