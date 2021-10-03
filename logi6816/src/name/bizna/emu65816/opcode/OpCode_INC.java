package name.bizna.emu65816.opcode;

import name.bizna.emu65816.AddressingMode;
import name.bizna.emu65816.Cpu65816;
import name.bizna.emu65816.addressingmode.InstructionCycles;

import static name.bizna.emu65816.OpCodeName.*;

public class OpCode_INC
    extends OpCode
{
  public OpCode_INC(int mCode, InstructionCycles busCycles)
  {
    super("INC", "Increment memory; result in memory and update NZ.", mCode, busCycles);
  }

  @Override
  public void execute1(Cpu65816 cpu)
  {
    switch (getCode())
    {
      case INC_Absolute:
      case INC_DirectPage:
      case INC_AbsoluteIndexedWithX:
      case INC_DirectPageIndexedWithX:
        cpu.incrementData();
        break;
      default:
        throw new IllegalStateException("Unexpected value: " + getCode());
    }
  }
}

