package name.bizna.emu65816.addressingmode;

import name.bizna.emu65816.Cpu65816;
import name.bizna.emu65816.opcode.OpCode;

public class FetchRelativeOffsetHigh
    extends DataOperation
{
  public FetchRelativeOffsetHigh()
  {
    super(true, false, true, true, true);
  }

  @Override
  public void execute(Cpu65816 cpu, OpCode opCode)
  {
    cpu.setRelativeOffsetHigh(cpu.getPinData());
  }
}

