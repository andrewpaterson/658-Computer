package name.bizna.cpu.opcode;

import name.bizna.cpu.addressingmode.InstructionCycles;

public class OpCode_TXY
    extends OpCode
{
  public OpCode_TXY(int mCode, InstructionCycles cycles)
  {
    super("TXY", "Transfer Index X to Index Y", mCode, cycles);
  }
}

