package name.bizna.cpu.opcode;

import name.bizna.cpu.addressingmode.InstructionCycles;

public class OpCode_TAY
    extends OpCode
{
  public OpCode_TAY(int mCode, InstructionCycles cycles)
  {
    super("TAY", "Transfer Accumulator in Index Y", mCode, cycles);
  }
}

