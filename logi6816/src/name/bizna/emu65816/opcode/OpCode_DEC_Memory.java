package name.bizna.emu65816.opcode;

import name.bizna.emu65816.Address;
import name.bizna.emu65816.AddressingMode;
import name.bizna.emu65816.Binary;
import name.bizna.emu65816.Cpu65816;
import name.bizna.emu65816.addressingmode.InstructionCycles;

import static name.bizna.emu65816.OpCodeName.*;
import static name.bizna.emu65816.Unsigned.toByte;
import static name.bizna.emu65816.Unsigned.toShort;

public class OpCode_DEC_Memory
    extends OpCode
{
  public OpCode_DEC_Memory(int mCode, InstructionCycles busCycles)
  {
    super("DEC", "Decrement memory; result in memory and update NZ.", mCode, busCycles);
  }

  @Override
  public void execute1(Cpu65816 cpu)
  {
    switch (getCode())
    {
      case DEC_Absolute:
      case DEC_DirectPage:
      case DEC_AbsoluteIndexedWithX:
      case DEC_DirectPageIndexedWithX:
        cpu.decrementData();
      default:
        throw new IllegalStateException("Unexpected value: " + getCode());
    }
  }
}

