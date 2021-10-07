package name.bizna.cpu.opcode;

import name.bizna.cpu.addressingmode.InstructionCycles;

public class OpCode_XCE
    extends OpCode
{
  public OpCode_XCE(int mCode, InstructionCycles cycles)
  {
    super("XCE", "Exchange Carry and Emulation Bits", mCode, cycles);
  }
}

