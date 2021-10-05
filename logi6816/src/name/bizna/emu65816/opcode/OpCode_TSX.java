package name.bizna.emu65816.opcode;

import name.bizna.emu65816.Binary;
import name.bizna.emu65816.Cpu65816;
import name.bizna.emu65816.addressingmode.InstructionCycles;

public class OpCode_TSX
    extends OpCode
{
  public OpCode_TSX(int mCode, InstructionCycles cycles)
  {
    super("TSX", "Transfer Stack Pointer Register to Index X", mCode, cycles);
  }
}

