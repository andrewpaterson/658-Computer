package name.bizna.cpu.opcode;

import name.bizna.cpu.addressingmode.InstructionCycles;

public class OpCode_TSX
    extends OpCode
{
  public OpCode_TSX(int mCode, InstructionCycles cycles)
  {
    super("TSX", "Transfer Stack Pointer Register to Index X", mCode, cycles);
  }
}

