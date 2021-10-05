package name.bizna.emu65816.opcode;

import name.bizna.emu65816.addressingmode.InstructionCycles;

public class OpCode_TXY
    extends OpCode
{
  public OpCode_TXY(int mCode, InstructionCycles cycles)
  {
    super("TXY", "Transfer Index X to Index Y", mCode, cycles);
  }
}

