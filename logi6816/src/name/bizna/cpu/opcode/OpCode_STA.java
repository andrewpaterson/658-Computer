package name.bizna.cpu.opcode;

import name.bizna.cpu.addressingmode.InstructionCycles;

public class OpCode_STA
    extends OpCode
{
  public OpCode_STA(int mCode, InstructionCycles cycles)
  {
    super("STA", "Store Accumulator in Memory", mCode, cycles);
  }
}

