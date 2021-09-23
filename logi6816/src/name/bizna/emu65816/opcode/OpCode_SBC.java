package name.bizna.emu65816.opcode;

import name.bizna.emu65816.*;

import static name.bizna.emu65816.OpCodeTable.*;

public class OpCode_SBC
    extends OpCode
{
  public OpCode_SBC(String mName, byte mCode, AddressingMode mAddressingMode)
  {
    super(mName, mCode, mAddressingMode);
  }

  @Override
  public void execute(Cpu65816 cpu)
  {
  }

  protected void execute8BitSBC(Cpu65816 cpu)
  {
    Address dataAddress = cpu.getAddressOfOpCodeData(getAddressingMode());
    byte value = cpu.readByte(dataAddress);
    byte accumulator = Binary.lower8BitsOf(cpu.getA());
    boolean borrow = !cpu.getCpuStatus().carryFlag();

    short result16Bit = (short) (accumulator - value - (borrow ? 1 : 0));

    // Is there a borrow from the penultimate bit, redo the diff with 7 bits value and find out.
    accumulator &= 0x7F;
    value &= 0x7F;
    byte partialResult = (byte) (accumulator - value - (borrow ? 1 : 0));
    // Is bit 8 set?
    boolean borrowFromPenultimateBit = (partialResult & 0x80) != 0;

    // Is there a borrow from the last bit, check bit 8 for that
    boolean borrowFromLastBit = (result16Bit & 0x0100) != 0;

    boolean overflow = borrowFromLastBit ^ borrowFromPenultimateBit;
    if (overflow)
    {
      cpu.getCpuStatus().setOverflowFlag();
    }
    else
    {
      cpu.getCpuStatus().clearOverflowFlag();
    }

    cpu.getCpuStatus().setCarryFlag(!borrowFromLastBit);

    byte result8Bit = Binary.lower8BitsOf(result16Bit);
    // Update sign and zero flags
    cpu.getCpuStatus().updateSignAndZeroFlagFrom8BitValue(result8Bit);
    // Store the 8 bit result in the accumulator
    cpu.setA(Binary.setLower8BitsOf16BitsValue(cpu.getA(), result8Bit));
  }

  protected void execute16BitSBC(Cpu65816 cpu)
  {
    Address dataAddress = cpu.getAddressOfOpCodeData(getAddressingMode());
    short value = cpu.readTwoBytes(dataAddress);
    short accumulator = cpu.getA();
    boolean borrow = !cpu.getCpuStatus().carryFlag();

    int result32Bit = accumulator - value - (borrow ? 1 : 0);

    // Is there a borrow from the penultimate bit, redo the diff with 15 bits value and find out.
    accumulator &= 0x7FFF;
    value &= 0x7FFF;
    short partialResult = (short) (accumulator - value - (borrow ? 1 : 0));
    // Is bit 15 set?
    boolean borrowFromPenultimateBit = (partialResult & 0x80) != 0;

    // Is there a borrow from the last bit, check bit 16 for that
    boolean borrowFromLastBit = (result32Bit & 0x0100) != 0;

    boolean overflow = borrowFromLastBit ^ borrowFromPenultimateBit;
    if (overflow)
    {
      cpu.getCpuStatus().setOverflowFlag();
    }
    else
    {
      cpu.getCpuStatus().clearOverflowFlag();
    }

    cpu.getCpuStatus().setCarryFlag(!borrowFromLastBit);

    short result16Bit = Binary.lower8BitsOf((short) result32Bit);
    // Update sign and zero flags
    cpu.getCpuStatus().updateSignAndZeroFlagFrom8BitValue((byte) result16Bit);
    // Store the 8 bit result in the accumulator
    cpu.setA(result16Bit);
  }

  protected void execute8BitBCDSBC(Cpu65816 cpu)
  {
    Address dataAddress = cpu.getAddressOfOpCodeData(getAddressingMode());
    byte value = cpu.readByte(dataAddress);
    byte accumulator = Binary.lower8BitsOf(cpu.getA());

    byte result = 0;
    BCD8BitResult bcd8BitResult = Binary.bcdSubtract8Bit(value, accumulator, result, !cpu.getCpuStatus().carryFlag());
    boolean borrow = bcd8BitResult.carry;
    result = bcd8BitResult.value;
    cpu.getCpuStatus().setCarryFlag(!borrow);

    cpu.setA(Binary.setLower8BitsOf16BitsValue(cpu.getA(), result));
    cpu.getCpuStatus().updateSignAndZeroFlagFrom8BitValue(result);
  }

  protected void execute16BitBCDSBC(Cpu65816 cpu)
  {
    Address dataAddress = cpu.getAddressOfOpCodeData(getAddressingMode());
    short value = cpu.readTwoBytes(dataAddress);
    short accumulator = cpu.getA();

    short result = 0;
    BCD16BitResult bcd16BitResult = Binary.bcdSubtract16Bit(value, accumulator, result, !cpu.getCpuStatus().carryFlag());
    boolean borrow = bcd16BitResult.carry;
    result = bcd16BitResult.value;
    cpu.getCpuStatus().setCarryFlag(!borrow);

    cpu.setA(result);
    cpu.getCpuStatus().updateSignAndZeroFlagFrom8BitValue((byte) result);
  }

  protected void executeSBC(Cpu65816 cpu)
  {
    if (cpu.accumulatorIs8BitWide())
    {
      if (cpu.getCpuStatus().decimalFlag())
      {
        execute8BitBCDSBC(cpu);
      }
      else
      {
        execute8BitSBC(cpu);
      }
    }
    else
    {
      if (cpu.getCpuStatus().decimalFlag())
      {
        execute16BitBCDSBC(cpu);
      }
      else
      {
        execute16BitSBC(cpu);
      }
      cpu.addToCycles(1);
    }

    switch (getCode())
    {
      case SBC_Immediate:                 // SBC Immediate
      {
        if (cpu.accumulatorIs16BitWide())
        {
          cpu.addToProgramAddress(1);
        }
        cpu.addToProgramAddress(2);
        cpu.addToCycles(2);
        break;
      }
      case SBC_Absolute:                 // SBC Absolute
      {
        cpu.addToProgramAddress(3);
        cpu.addToCycles(4);
        break;
      }
      case SBC_AbsoluteLong:                 // SBC Absolute Long
      {
        cpu.addToProgramAddress(4);
        cpu.addToCycles(5);
        break;
      }
      case SBC_DirectPage:                 // SBC Direct Page
      {
        if (Binary.lower8BitsOf(cpu.getD()) != 0)
        {
          cpu.addToCycles(1);
        }

        cpu.addToProgramAddress(2);
        cpu.addToCycles(3);
        break;
      }
      case SBC_DirectPageIndirect:                 // SBC Direct Page Indirect
      {
        if (Binary.lower8BitsOf(cpu.getD()) != 0)
        {
          cpu.addToCycles(1);
        }

        cpu.addToProgramAddress(2);
        cpu.addToCycles(5);
        break;
      }
      case SBC_DirectPageIndirectLong:                 // SBC Direct Page Indirect Long
      {
        if (Binary.lower8BitsOf(cpu.getD()) != 0)
        {
          cpu.addToCycles(1);
        }

        cpu.addToProgramAddress(2);
        cpu.addToCycles(6);
        break;
      }
      case SBC_AbsoluteIndexedWithX:                 // SBC Absolute Indexed, X
      {
        if (cpu.opCodeAddressingCrossesPageBoundary(getAddressingMode()))
        {
          cpu.addToCycles(1);
        }

        cpu.addToProgramAddress(3);
        cpu.addToCycles(4);
        break;
      }
      case SBC_AbsoluteLongIndexedWithX:                 // SBC Absolute Long Indexed, X
      {
        cpu.addToProgramAddress(4);
        cpu.addToCycles(5);
        break;
      }
      case SBC_AbsoluteIndexedWithY:                 // SBC Absolute Indexed Y
      {
        if (cpu.opCodeAddressingCrossesPageBoundary(getAddressingMode()))
        {
          cpu.addToCycles(1);
        }
        cpu.addToProgramAddress(3);
        cpu.addToCycles(4);
        break;
      }
      case SBC_DirectPageIndexedWithX:                 // SBC Direct Page Indexed, X
      {
        if (Binary.lower8BitsOf(cpu.getD()) != 0)
        {
          cpu.addToCycles(1);
        }
        if (cpu.opCodeAddressingCrossesPageBoundary(getAddressingMode()))
        {
          cpu.addToCycles(1);
        }
        cpu.addToProgramAddress(2);
        cpu.addToCycles(4);
        break;
      }
      case SBC_DirectPageIndexedIndirectWithX:                 // SBC Direct Page Indexed Indirect, X
      {
        if (Binary.lower8BitsOf(cpu.getD()) != 0)
        {
          cpu.addToCycles(1);
        }
        cpu.addToProgramAddress(2);
        cpu.addToCycles(6);
        break;
      }
      case SBC_DirectPageIndirectIndexedWithY:                 // SBC Direct Page Indirect Indexed, Y
      {
        if (Binary.lower8BitsOf(cpu.getD()) != 0)
        {
          cpu.addToCycles(1);
        }
        if (cpu.opCodeAddressingCrossesPageBoundary(getAddressingMode()))
        {
          cpu.addToCycles(1);
        }
        cpu.addToProgramAddress(2);
        cpu.addToCycles(5);
        break;
      }
      case SBC_DirectPageIndirectLongIndexedWithY:                 // SBC Direct Page Indirect Long Indexed, Y
      {
        if (Binary.lower8BitsOf(cpu.getD()) != 0)
        {
          cpu.addToCycles(1);
        }
        cpu.addToProgramAddress(2);
        cpu.addToCycles(6);
        break;
      }
      case SBC_StackRelative:                 // SBC Stack Relative
      {
        cpu.addToProgramAddress(2);
        cpu.addToCycles(4);
        break;
      }
      case SBC_StackRelativeIndirectIndexedWithY:                 // SBC Stack Relative Indirect Indexed, Y
      {
        cpu.addToProgramAddress(2);
        cpu.addToCycles(7);
        break;
      }
      default:
        throw new IllegalStateException("Unexpected value: " + getCode());
    }
  }
}

