package name.bizna.emu65816.opcode;

import name.bizna.emu65816.Address;
import name.bizna.emu65816.Cpu65816;
import name.bizna.emu65816.addressingmode.InstructionCycles;

import static name.bizna.emu65816.OpCodeName.*;
import static name.bizna.emu65816.Unsigned.toShort;

public class OpCode_JSR
    extends OpCode
{
  public OpCode_JSR(int mCode, InstructionCycles cycles)
  {
    super("JSR", "Jump to new location save return address on Stack.", mCode, cycles);
  }
}

