package name.bizna.emu65816.opcode;

import name.bizna.emu65816.Address;
import name.bizna.emu65816.AddressingMode;
import name.bizna.emu65816.Binary;
import name.bizna.emu65816.Cpu65816;

import static name.bizna.emu65816.Binary.is8bitValueNegative;
import static name.bizna.emu65816.OpCodeTable.*;
import static name.bizna.emu65816.Unsigned.toByte;
import static name.bizna.emu65816.Unsigned.toShort;

public class OpCode_ASL
    extends OpCode
{
  public OpCode_ASL(String mName, int mCode, AddressingMode mAddressingMode)
  {
    super(mName, mCode, mAddressingMode);
  }

  void DO_ASL_8_BIT(Cpu65816 cpu, int value)
  {
    boolean newCarry = is8bitValueNegative(value);
    value = toByte(value << 1);
    cpu.getCpuStatus().setCarryFlag(newCarry);
    cpu.getCpuStatus().updateSignAndZeroFlagFrom8BitValue(value);
  }

  /**
   * Arithmetic Shift Left
   * <p>
   * This file contains implementations for all ASL OpCodes.
   */

  protected void executeMemoryASL(Cpu65816 cpu)
  {
    Address opCodeDataAddress = cpu.getAddressOfOpCodeData(getAddressingMode());

    if (cpu.accumulatorIs8BitWide())
    {
      int value = cpu.readByte(opCodeDataAddress);
      boolean newCarry = is8bitValueNegative(value);
      value = toByte(value << 1);
      cpu.getCpuStatus().setCarryFlag(newCarry);
      cpu.getCpuStatus().updateSignAndZeroFlagFrom8BitValue(value);
      cpu.storeByte(opCodeDataAddress, value);
    }
    else
    {
      int value = cpu.readTwoBytes(opCodeDataAddress);
      boolean newCarry = (value & 0x8000) != 0;
      value = toShort(value << 1);
      cpu.getCpuStatus().setCarryFlag(newCarry);
      cpu.getCpuStatus().updateSignAndZeroFlagFrom16BitValue(value);
      cpu.storeTwoBytes(opCodeDataAddress, value);
    }
  }

  protected void executeAccumulatorASL(Cpu65816 cpu)
  {
    if (cpu.accumulatorIs8BitWide())
    {
      int value = Binary.lower8BitsOf(cpu.getA());
      DO_ASL_8_BIT(cpu, value);
      cpu.setA(Binary.setLower8BitsOf16BitsValue(cpu.getA(), value));
    }
    else
    {
      boolean newCarry = (cpu.getA() & 0x8000) != 0;
      cpu.setA(toShort(cpu.getA() << 1));
      cpu.getCpuStatus().setCarryFlag(newCarry);
      cpu.getCpuStatus().updateSignAndZeroFlagFrom16BitValue(cpu.getA());
    }
  }

  @Override
  public void executeOnFallingEdge(Cpu65816 cpu)
  {
    switch (getCode())
    {
      case ASL_Accumulator:                // ASL Accumulator
      {
        executeAccumulatorASL(cpu);
        cpu.addToProgramAddressAndCycles(1, 2);
        break;
      }
      case ASL_Absolute:                // ASL Absolute
      {
        if (cpu.accumulatorIs16BitWide())
        {
          cpu.addToCycles(2);
        }

        executeMemoryASL(cpu);
        cpu.addToProgramAddressAndCycles(3, 6);
        break;
      }
      case ASL_DirectPage:                // ASL Direct Page
      {
        if (cpu.accumulatorIs16BitWide())
        {
          cpu.addToCycles(2);
        }
        if (Binary.lower8BitsOf(cpu.getD()) != 0)
        {
          cpu.addToCycles(1);
        }

        executeMemoryASL(cpu);
        cpu.addToProgramAddressAndCycles(2, 5);
        break;
      }
      case ASL_AbsoluteIndexedWithX:                // ASL Absolute Indexed, X
      {
        if (cpu.accumulatorIs16BitWide())
        {
          cpu.addToCycles(2);
        }

        executeMemoryASL(cpu);
        cpu.addToProgramAddressAndCycles(3, 7);
        break;
      }
      case ASL_DirectPageIndexedWithX:                // ASL Direct Page Indexed, X
      {
        if (cpu.accumulatorIs16BitWide())
        {
          cpu.addToCycles(2);
        }
        if (Binary.lower8BitsOf(cpu.getD()) != 0)
        {
          cpu.addToCycles(1);
        }

        executeMemoryASL(cpu);
        cpu.addToProgramAddressAndCycles(2, 6);
        break;
      }
      default:
        throw new IllegalStateException("Unexpected value: " + getCode());
    }
  }

  @Override
  public void executeOnRisingEdge(Cpu65816 cpu)
  {
  }
}

