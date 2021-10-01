package name.bizna.emu65816.addressingmode;

import name.bizna.emu65816.Cpu65816;
import name.bizna.emu65816.opcode.OpCode;

public class FetchRelativeOffsetLow
    extends DataOperation
{
  public FetchRelativeOffsetLow()
  {
    super(true, false, true, true, true);
  }

  @Override
  public void execute(Cpu65816 cpu, OpCode opCode)
  {
    cpu.clearRelativeOffsetHigh();
    cpu.setRelativeOffsetLow(cpu.getPinData());
  }
}

