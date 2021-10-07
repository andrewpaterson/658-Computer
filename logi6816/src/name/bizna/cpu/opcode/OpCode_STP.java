package name.bizna.cpu.opcode;

import name.bizna.cpu.addressingmode.InstructionCycles;

public class OpCode_STP
    extends OpCode
{
  public OpCode_STP(int mCode, InstructionCycles cycles)
  {
    super("STP", "Stop the Clock", mCode, cycles);
  }
}

