package name.bizna.cpu.opcode;

import name.bizna.cpu.addressingmode.InstructionCycles;

public class OpCode_TXS
    extends OpCode
{
  public OpCode_TXS(int mCode, InstructionCycles cycles)
  {
    super("TXS", " Transfer Index X to Stack Pointer Register", mCode, cycles);
  }
}

