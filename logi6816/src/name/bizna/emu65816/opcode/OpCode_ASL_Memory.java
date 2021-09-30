package name.bizna.emu65816.opcode;

import name.bizna.emu65816.Cpu65816;
import name.bizna.emu65816.addressingmode.InstructionCycles;

import static name.bizna.emu65816.Binary.is16bitValueNegative;
import static name.bizna.emu65816.Binary.is8bitValueNegative;
import static name.bizna.emu65816.OpCodeName.*;
import static name.bizna.emu65816.Unsigned.toByte;
import static name.bizna.emu65816.Unsigned.toShort;

public class OpCode_ASL_Memory
    extends OpCode
{
  public OpCode_ASL_Memory(int mCode, InstructionCycles busCycles)
  {
    super("ASL", "Shift memory left 1 bit; result in memory and update NZC.", mCode, busCycles);
  }

  @Override
  public void execute1(Cpu65816 cpu)
  {
    switch (getCode())
    {
      case ASL_Absolute:
      case ASL_DirectPage:
      case ASL_AbsoluteIndexedWithX:
      case ASL_DirectPageIndexedWithX:
      {
        if (cpu.isMemory8Bit())
        {
          int operand = cpu.getData();
          boolean carry = is8bitValueNegative(operand);
          operand = toByte(operand << 1);
          cpu.getCpuStatus().setCarryFlag(carry);
          cpu.setData(operand);
        }
        break;
      }
      default:
        throw new IllegalStateException("Unexpected value: " + getCode());
    }
  }

  @Override
  public void execute2(Cpu65816 cpu)
  {
    switch (getCode())
    {
      case ASL_Absolute:
      case ASL_DirectPage:
      case ASL_AbsoluteIndexedWithX:
      case ASL_DirectPageIndexedWithX:
      {
        if (cpu.isMemory16Bit())
        {
          int operand = cpu.getData();
          boolean carry = (operand & 0x8000) != 0;
          operand = toShort(operand << 1);
          cpu.getCpuStatus().setCarryFlag(carry);
          cpu.setData(operand);
        }
        break;
      }
      default:
        throw new IllegalStateException("Unexpected value: " + getCode());
    }
  }
}

