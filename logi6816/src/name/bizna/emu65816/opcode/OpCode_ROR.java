package name.bizna.emu65816.opcode;

import name.bizna.emu65816.Address;
import name.bizna.emu65816.AddressingMode;
import name.bizna.emu65816.Binary;
import name.bizna.emu65816.Cpu65816;

import static name.bizna.emu65816.OpCodeTable.*;
import static name.bizna.emu65816.Unsigned.toByte;
import static name.bizna.emu65816.Unsigned.toShort;

public class OpCode_ROR
    extends OpCode
{
  public OpCode_ROR(String mName, int mCode, AddressingMode mAddressingMode)
  {
    super(mName, mCode, mAddressingMode);
  }

  void DO_ROR_8_BIT(Cpu65816 cpu, int value)
  {
    boolean carryWasSet = cpu.getCpuStatus().carryFlag();
    boolean carryWillBeSet = (value & 0x01) != 0;
    value = toByte(value >> 1);
    if (carryWasSet)
    {
      value = Binary.setBitIn8BitValue(value, 7);
    }
    else
    {
      value = Binary.clearBitIn8BitValue(value, 7);
    }
    cpu.getCpuStatus().setCarryFlag(carryWillBeSet);
    cpu.getCpuStatus().updateSignAndZeroFlagFrom8BitValue(value);
  }

  void DO_ROR_16_BIT(Cpu65816 cpu, int value)
  {
    boolean carryWasSet = cpu.getCpuStatus().carryFlag();
    boolean carryWillBeSet = (value & 0x0001) != 0;
    value = toShort(value >> 1);
    if (carryWasSet)
    {
      value = Binary.setBitIn16BitValue(value, 15);
    }
    else
    {
      value = Binary.clearBitIn16BitValue(value, 15);
    }
    cpu.getCpuStatus().setCarryFlag(carryWillBeSet);
    cpu.getCpuStatus().updateSignAndZeroFlagFrom16BitValue(value);
  }

  /**
   * This file contains implementations for all ROR OpCodes.
   */
  protected void executeMemoryROR(Cpu65816 cpu)
  {
    Address opCodeDataAddress = cpu.getAddressOfOpCodeData(getAddressingMode());

    if (cpu.accumulatorIs8BitWide())
    {
      int value = cpu.readByte(opCodeDataAddress);
      DO_ROR_8_BIT(cpu, value);
      cpu.storeByte(opCodeDataAddress, value);
    }
    else
    {
      int value = cpu.readTwoBytes(opCodeDataAddress);
      DO_ROR_16_BIT(cpu, value);
      cpu.storeTwoBytes(opCodeDataAddress, value);
    }
  }

  protected void executeAccumulatorROR(Cpu65816 cpu)
  {
    if (cpu.accumulatorIs8BitWide())
    {
      int value = Binary.lower8BitsOf(cpu.getA());
      DO_ROR_8_BIT(cpu, value);
      cpu.setA(Binary.setLower8BitsOf16BitsValue(cpu.getA(), value));
    }
    else
    {
      int value = cpu.getA();
      DO_ROR_16_BIT(cpu, value);
      cpu.setA(value);
    }
  }

  @Override
  public void execute(Cpu65816 cpu, int cycle, boolean clock)
  {
    switch (getCode())
    {
      case ROR_Accumulator:                // ROR accumulator
      {
        executeAccumulatorROR(cpu);
        cpu.addToProgramAddressAndCycles(1, 2);
        break;
      }
      case ROR_Absolute:                // ROR #addr
      {
        executeMemoryROR(cpu);
        if (cpu.accumulatorIs8BitWide())
        {
          cpu.addToProgramAddressAndCycles(3, 6);
        }
        else
        {
          cpu.addToProgramAddressAndCycles(3, 8);
        }
        break;
      }
      case ROR_DirectPage:                // ROR Direct Page
      {
        executeMemoryROR(cpu);
        int opCycles = Binary.lower8BitsOf(cpu.getD()) != 0 ? 1 : 0;
        if (cpu.accumulatorIs8BitWide())
        {
          cpu.addToProgramAddressAndCycles(2, 5 + opCycles);
        }
        else
        {
          cpu.addToProgramAddressAndCycles(2, 7 + opCycles);
        }
        break;
      }
      case ROR_AbsoluteIndexedWithX:                // ROR Absolute Indexed, X
      {
        executeMemoryROR(cpu);
        short opCycles = 0;
        if (cpu.accumulatorIs8BitWide())
        {
          cpu.addToProgramAddressAndCycles(3, 7 + opCycles);
        }
        else
        {
          cpu.addToProgramAddressAndCycles(3, 9 + opCycles);
        }
        break;
      }
      case ROR_DirectPageIndexedWithX:                // ROR Direct Page Indexed, X
      {
        executeMemoryROR(cpu);
        int opCycles = Binary.lower8BitsOf(cpu.getD()) != 0 ? 1 : 0;
        if (cpu.accumulatorIs8BitWide())
        {
          cpu.addToProgramAddressAndCycles(2, 6 + opCycles);
        }
        else
        {
          cpu.addToProgramAddressAndCycles(2, 8 + opCycles);
        }
        break;
      }
      default:
        throw new IllegalStateException("Unexpected value: " + getCode());
    }
  }
}

