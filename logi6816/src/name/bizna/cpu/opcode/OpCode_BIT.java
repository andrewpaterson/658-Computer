package name.bizna.cpu.opcode;

import name.bizna.cpu.addressingmode.InstructionCycles;

public class OpCode_BIT
    extends OpCode
{
  public OpCode_BIT(int mCode, InstructionCycles cycles)
  {
    super("BIT", "Bit Test", mCode, cycles);
  }
}

