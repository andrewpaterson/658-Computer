package name.bizna.emu65816.opcode;

import name.bizna.emu65816.Cpu65816;
import name.bizna.emu65816.addressingmode.StackResetCycles;

public class OpCode_RES
    extends OpCode
{
  public OpCode_RES()
  {
    super("RES", -1, new StackResetCycles(0xfffc));
  }

  @Override
  public void execute1(Cpu65816 cpu)
  {
    cpu.reset();
  }
}

