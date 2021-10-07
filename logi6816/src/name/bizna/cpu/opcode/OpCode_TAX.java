package name.bizna.cpu.opcode;

import name.bizna.cpu.addressingmode.InstructionCycles;

public class OpCode_TAX
    extends OpCode
{
  public OpCode_TAX(int mCode, InstructionCycles cycles)
  {
    super("TAX", "Transfer Accumulator in Index X", mCode, cycles);
  }
}

