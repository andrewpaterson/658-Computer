package name.bizna.emu65816.opcode;

import name.bizna.emu65816.Address;
import name.bizna.emu65816.AddressingMode;
import name.bizna.emu65816.Binary;
import name.bizna.emu65816.Cpu65816;

import static name.bizna.emu65816.Binary.is8bitValueNegative;
import static name.bizna.emu65816.OpCodeName.*;
import static name.bizna.emu65816.Unsigned.toByte;
import static name.bizna.emu65816.Unsigned.toShort;

public class OpCode_ROL
    extends OpCode
{
  public OpCode_ROL(String mName, int mCode, InstructionCycles cycles)
  {
    super(mName, mCode, cycles);
  }

  void DO_ROL_8_BIT(Cpu65816 cpu, int value)
  {
    boolean carryWasSet = cpu.getCpuStatus().carryFlag();
    boolean carryWillBeSet = is8bitValueNegative(value);
    value = toByte(value << 1);
    if (carryWasSet)
    {
      value = Binary.setBitIn8BitValue(value, 0);
    }
    else
    {
      value = Binary.clearBitIn8BitValue(value, 0);
    }
    cpu.getCpuStatus().setCarryFlag(carryWillBeSet);
    cpu.getCpuStatus().updateSignAndZeroFlagFrom8BitValue(value);
  }

  void DO_ROL_16_BIT(Cpu65816 cpu, int value)
  {
    boolean carryWasSet = cpu.getCpuStatus().carryFlag();
    boolean carryWillBeSet = (value & 0x8000) != 0;
    value = toShort(value << 1);
    if (carryWasSet)
    {
      value = Binary.setBitIn16BitValue(value, 0);
    }
    else
    {
      value = Binary.clearBitIn16BitValue(value, 0);
    }
    cpu.getCpuStatus().setCarryFlag(carryWillBeSet);
    cpu.getCpuStatus().updateSignAndZeroFlagFrom16BitValue(value);
  }

  /**
   * This file contains implementations for all ROL OpCodes.
   */
  protected void executeMemoryROL(Cpu65816 cpu)
  {
    Address opCodeDataAddress = cpu.getAddressOfOpCodeData(getAddressingMode());

    if (cpu.isMemory8Bit())
    {
      int value = cpu.getDataLow();
      DO_ROL_8_BIT(cpu, value);
      cpu.storeByte(opCodeDataAddress, value);
    }
    else
    {
      int value = cpu.getData();
      DO_ROL_16_BIT(cpu, value);
      cpu.storeTwoBytes(opCodeDataAddress, value);
    }
  }

  protected void executeAccumulatorROL(Cpu65816 cpu)
  {
    if (cpu.isMemory8Bit())
    {
      int value = Binary.getLowByte(cpu.getA());
      DO_ROL_8_BIT(cpu, value);
      cpu.setA(Binary.setLowByte(cpu.getA(), value));
    }
    else
    {
      int value = cpu.getA();
      DO_ROL_16_BIT(cpu, value);
      cpu.setA(value);
    }
  }

  @Override
  public void executeOnFallingEdge(Cpu65816 cpu)
  {
    switch (getCode())
    {
      case ROL_Accumulator:                // ROL accumulator
      {
        executeAccumulatorROL(cpu);
        cpu.addToProgramAddressAndCycles(1, 2);
        break;
      }
      case (0x2E):                // ROL #addr
      {
        executeMemoryROL(cpu);
        if (cpu.isMemory8Bit())
        {
          cpu.addToProgramAddressAndCycles(3, 6);
        }
        else
        {
          cpu.addToProgramAddressAndCycles(3, 8);
        }
        break;
      }
      case (0x26):                // ROL Direct Page
      {
        executeMemoryROL(cpu);
        int opCycles = Binary.getLowByte(cpu.getDirectPage()) != 0 ? 1 : 0;
        if (cpu.isMemory8Bit())
        {
          cpu.addToProgramAddressAndCycles(2, 5 + opCycles);
        }
        else
        {
          cpu.addToProgramAddressAndCycles(2, 7 + opCycles);
        }
        break;
      }
      case (0x3E):                // ROL Absolute Indexed, X
      {
        executeMemoryROL(cpu);
        short opCycles = 0;
        if (cpu.isMemory8Bit())
        {
          cpu.addToProgramAddressAndCycles(3, 7 + opCycles);
        }
        else
        {
          cpu.addToProgramAddressAndCycles(3, 9 + opCycles);
        }
        break;
      }
      case (0x36):                // ROL Direct Page Indexed, X
      {
        executeMemoryROL(cpu);
        int opCycles = Binary.getLowByte(cpu.getDirectPage()) != 0 ? 1 : 0;
        if (cpu.isMemory8Bit())
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

  @Override
  public void executeOnRisingEdge(Cpu65816 cpu)
  {
  }
}

